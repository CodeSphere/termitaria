/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *  Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *   
 *  Termitaria is free software; you can redistribute it and/or 
 *  modify it under the terms of the GNU Affero General Public License 
 *  as published by the Free Software Foundation; either version 3 of 
 *  the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.cm.start;

import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.security.access.vote.RoleVoter;

import ro.cs.cm.common.BackConstant;
import ro.cs.cm.common.ConfigParametersProvider;
import ro.cs.cm.common.ExceptionConstant;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.common.PermissionConstant;
import ro.cs.cm.context.CMContext;
import ro.cs.cm.job.AbortProjectDetailJob;
import ro.cs.cm.job.DeleteProjectDetailJob;
import ro.cs.cm.job.DeleteTeamMemberDetailJob;
import ro.cs.cm.job.FinishProjectDetailJob;
import ro.cs.cm.job.OpenProjectDetailJob;

/**
 * @author matti_joona
 * @author alu
 *
 * Servlet prin care se initializeaza contextul aplicatiei. Informatiile de dimensiuni reduse se stocheaza
 * in aceasta zona pentru a fi disponibile catre alte clase care prelucreaza aceste informatii
 */
public class InitApplication extends HttpServlet{

	private final Log logger = LogFactory.getLog(getClass());	

	/**
	 * Initializarea contextului. Aici se preiau nomenclatoarele ce se vor
	 * pastra pe sesiune
	 */
	public void init(ServletConfig conf) throws ServletException {
		logger.info("Initializare aplicatie...");
		try {
            
			ServletContext sc = conf.getServletContext();
			logger.info("*******************************************************");
			logger.info("*                                                     *");
			logger.info("*    INITIATING APPLICATION CLIENT MANAGEMENT->       *");
			logger.info("*                                                     *");
			logger.info("*******************************************************");
            logger.info(IConstant.APP_VERSION.concat("/").concat(IConstant.APP_RELEASE_DATE));
            sc.setAttribute("VERSION", IConstant.APP_VERSION);
            sc.setAttribute("RELEASE_DATE", IConstant.APP_RELEASE_DATE);
            sc.setAttribute("RELEASE_YEAR", IConstant.APP_RELEASE_YEAR);
                                        
            //Nomenclators 
            ListLoader.getInstance().load_nom_resultsPerPage();
            
            // PROJECT
            ListLoader.getInstance().load_nom_projectStatus();
            
            // PROJECT TEAM
            ListLoader.getInstance().load_nom_projectTeamStatus();
            
            // TEAM MEMBER
            ListLoader.getInstance().load_nom_teamMemberStatus();
            
            // CLIENT
            ListLoader.getInstance().load_nom_clientType();
            ListLoader.getInstance().load_nom_clientStatus();
            
            RoleVoter rv = (RoleVoter) CMContext.getApplicationContext().getBean("roleVoter");
            
            // put exceptionContant bean on servletContect
            sc.setAttribute(IConstant.EXCEPTION_CONSTANT, ExceptionConstant.getInstance());  
            sc.setAttribute(IConstant.PERMISSION_CONSTANT, PermissionConstant.getInstance());
            sc.setAttribute(IConstant.BACK_CONSTANT, BackConstant.getInstance());  
            
            // initialize scheduler
            initScheduler(true);
            
            // job for deleting project details
            deleteProjectDetailJob();
            
            // job for finishing project details
            finishProjectDetailJob();
            
            // job for aborting project details
            abortProjectDetailJob();
            
            // open for opening project details
            openProjectDetailJob();                                 
            
            // job for deleting team member details
            deleteTeamMemberDetailJob();
                                                       
            logger.info("Role Prefix: \"" + rv.getRolePrefix() + "\"");
            logger.info("*******************************************************");
			logger.info("*                                                     *");
			logger.info("*   INITIATING APPLICATION END CLIENT MANAGEMENT<-    *");
			logger.info("*                                                     *");
			logger.info("*******************************************************");
			       
		} catch (Exception ex) {
            logger.info("*******************************************************");
			logger.info("*                                                     *");
			logger.info("*        ERROR INITIATING APPLICATION!!!              *");
			logger.info("*                                                     *");
			logger.info("*******************************************************");
			logger.error("", ex);
		}
		logger.info("The application was initiated!");
	}
	
	private void initScheduler(boolean activated) {
		logger.debug("initScheduler - START");
		if (!activated) {
			logger.error("Scheduler deactivated !");
			return;
		}
		
        try {
			// create scheduler
        	StdSchedulerFactory sf = new StdSchedulerFactory();
        	ClassLoader loader = this.getClass().getClassLoader();
        	sf.initialize(loader.getResourceAsStream(ConfigParametersProvider.getString("config", IConstant.SCHEDULER_CONFIG_FILE_LOCATION)));
	        Scheduler sched = sf.getScheduler();
			sched.start();
			logger.info("Scheduler started!");
			CMContext.storeOnContext(IConstant.SCHEDULER, sched);
        } catch (SchedulerException e) {
			logger.error("", e);
		}
        
		logger.debug("initScheduler - END");
	}
	
	/**
	 * Job to delete project details
	 *
	 * @author Adelina
	 * 
	 * @throws SchedulerException
	 */
	private void deleteProjectDetailJob() throws SchedulerException {
		logger.debug("deleteProjectDetailJob - START-");
		
		final String myJob = "DeleteProjectDetailJob";
		
		Scheduler sched = (Scheduler)CMContext.getFromContext(IConstant.SCHEDULER);
		
		JobDetail jobDetail = new JobDetail(myJob, myJob.concat("_Group"), DeleteProjectDetailJob.class);
	    
	    // initiate SimpleTrigger with its name(myJob.concat("Trigger")) and group name(myJob.concat("_Group"))
	    SimpleTrigger simpleTrigger = new SimpleTrigger(myJob.concat("Trigger"), myJob.concat("_Group"));
	    // set its start up time - now
	    simpleTrigger.setStartTime(new Date(System.currentTimeMillis()));
	    // set the interval, how often the job should run (3600 second here - 60 minutes = 1h) - must be set 
	    simpleTrigger.setRepeatInterval(3600000);  
	    // set the number of execution of this job
	    // it will run 1 time and exhaust.
	    simpleTrigger.setRepeatCount(0);
	    
	    sched.scheduleJob(jobDetail, simpleTrigger);
		
		logger.debug("deleteProjectDetailJob - END-");
	}		
	
	/**
	 * Job to delete team member details
	 *
	 * @author Adelina
	 * 
	 * @throws SchedulerException
	 */
	private void deleteTeamMemberDetailJob() throws SchedulerException {
		logger.debug("deleteTeamMemberDetailJob - START-");
		
		final String myJob = "DeleteTeamMemberDetailJob";
		
		Scheduler sched = (Scheduler)CMContext.getFromContext(IConstant.SCHEDULER);
		
		JobDetail jobDetail = new JobDetail(myJob, myJob.concat("_Group"), DeleteTeamMemberDetailJob.class);
	    
	    // initiate SimpleTrigger with its name(myJob.concat("Trigger")) and group name(myJob.concat("_Group"))
	    SimpleTrigger simpleTrigger = new SimpleTrigger(myJob.concat("Trigger"), myJob.concat("_Group"));
	    // set its start up time - now
	    simpleTrigger.setStartTime(new Date(System.currentTimeMillis()));
	    // set the interval, how often the job should run (3600 second here - 60 minutes = 1h) - must be set 
	    simpleTrigger.setRepeatInterval(3600000);  
	    // set the number of execution of this job
	    // it will run 1 time and exhaust.
	    simpleTrigger.setRepeatCount(0);
	    
	    sched.scheduleJob(jobDetail, simpleTrigger);
		
		logger.debug("deleteTeamMemberDetailJob - END-");
	}		
	
	/**
	 * Creates the finish project details job
	 *
	 * @author Adelina
	 * 
	 * @throws SchedulerException
	 */
	private void finishProjectDetailJob() throws SchedulerException {
		logger.debug("finishProjectDetailJob - START-");
		
		final String myJob = "FinishProjectDetailJob";
		
		Scheduler sched = (Scheduler)CMContext.getFromContext(IConstant.SCHEDULER);
		
		JobDetail jobDetail = new JobDetail(myJob, myJob.concat("_Group"), FinishProjectDetailJob.class);
	    
	    // initiate SimpleTrigger with its name(myJob.concat("Trigger")) and group name(myJob.concat("_Group"))
	    SimpleTrigger simpleTrigger = new SimpleTrigger(myJob.concat("Trigger"), myJob.concat("_Group"));
	    // set its start up time - now
	    simpleTrigger.setStartTime(new Date(System.currentTimeMillis()));
	    // set the interval, how often the job should run (3600 second here - 60 minutes = 1h) - must be set 
	    simpleTrigger.setRepeatInterval(3600000);  
	    // set the number of execution of this job
	    // it will run 1 time and exhaust.
	    simpleTrigger.setRepeatCount(0);
	    
	    sched.scheduleJob(jobDetail, simpleTrigger);
		
		logger.debug("finishProjectDetailJob - END-");
	}
	
	/**
	 * Creates the abort project details job
	 *
	 * @author Adelina
	 * 
	 * @throws SchedulerException
	 */
	private void abortProjectDetailJob() throws SchedulerException {
		logger.debug("abortProjectDetailJob - START-");
		
		final String myJob = "AbortProjectDetailJob";
		
		Scheduler sched = (Scheduler)CMContext.getFromContext(IConstant.SCHEDULER);
		
		JobDetail jobDetail = new JobDetail(myJob, myJob.concat("_Group"), AbortProjectDetailJob.class);
	    
	    // initiate SimpleTrigger with its name(myJob.concat("Trigger")) and group name(myJob.concat("_Group"))
	    SimpleTrigger simpleTrigger = new SimpleTrigger(myJob.concat("Trigger"), myJob.concat("_Group"));
	    // set its start up time - now
	    simpleTrigger.setStartTime(new Date(System.currentTimeMillis()));
	    // set the interval, how often the job should run (3600 second here - 60 minutes = 1h) - must be set 
	    simpleTrigger.setRepeatInterval(3600000);  
	    // set the number of execution of this job
	    // it will run 1 time and exhaust.
	    simpleTrigger.setRepeatCount(0);
	    
	    sched.scheduleJob(jobDetail, simpleTrigger);
		
		logger.debug("abortProjectDetailJob - END-");
	}
	
	/**
	 * Creates the open project details job
	 *
	 * @author Adelina
	 * 
	 * @throws SchedulerException
	 */
	private void openProjectDetailJob() throws SchedulerException {
		logger.debug("openProjectDetailJob - START-");
		
		final String myJob = "OpenProjectDetailJob";
		
		Scheduler sched = (Scheduler)CMContext.getFromContext(IConstant.SCHEDULER);
		
		JobDetail jobDetail = new JobDetail(myJob, myJob.concat("_Group"), OpenProjectDetailJob.class);
	    
	    // initiate SimpleTrigger with its name(myJob.concat("Trigger")) and group name(myJob.concat("_Group"))
	    SimpleTrigger simpleTrigger = new SimpleTrigger(myJob.concat("Trigger"), myJob.concat("_Group"));
	    // set its start up time - now
	    simpleTrigger.setStartTime(new Date(System.currentTimeMillis()));
	    // set the interval, how often the job should run (3600 second here - 60 minutes = 1h) - must be set 
	    simpleTrigger.setRepeatInterval(3600000);  
	    // set the number of execution of this job
	    // it will run 1 time and exhaust.
	    simpleTrigger.setRepeatCount(0);
	    
	    sched.scheduleJob(jobDetail, simpleTrigger);
		
		logger.debug("openProjectDetailJob - END-");
	}
}
