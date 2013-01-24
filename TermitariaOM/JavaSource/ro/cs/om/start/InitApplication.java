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
package ro.cs.om.start;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
import org.springframework.security.core.token.Token;
import org.springframework.security.access.vote.RoleVoter;

import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLOrganisationStructure;
import ro.cs.om.business.BLOutOfOffice;
import ro.cs.om.common.BackConstant;
import ro.cs.om.common.ConfigParametersProvider;
import ro.cs.om.common.ExceptionConstant;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.OutOfOffice;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.job.CancelOOOJob;
import ro.cs.om.job.TrialJob;
import ro.cs.om.utils.security.SecurityTokenMonitor;

/**
 * Servlet used for context initialisation. Short information needed to be available for functional classes
 *
 * @author matti_joona
 * @author alu
 */
public class InitApplication extends HttpServlet {

	/** Logger that is available to subclasses */
	private final Log logger = LogFactory.getLog(getClass());
	private final static boolean isTrialVersion = false;
	/**
	 * Initializarea contextului. Aici se preiau nomenclatoarele ce se vor
	 * pastra in OMContext
	 */
	public void init(ServletConfig conf) throws ServletException {
		logger.info("Initializare aplicatie...");
		try {
            
			ServletContext sc = conf.getServletContext();
			logger.info("*******************************************************");
			logger.info("*                                                     *");
			logger.info("*    ORGANISATION MANAGEMENT INITIALISATION: BEGIN    *");
			logger.info("*                                                     *");
			logger.info("*******************************************************");
			logger.info(IConstant.APP_VERSION.concat("/").concat(IConstant.APP_RELEASE_DATE));
            sc.setAttribute("VERSION", IConstant.APP_VERSION);
            sc.setAttribute("RELEASE_DATE", IConstant.APP_RELEASE_DATE);
            sc.setAttribute("RELEASE_YEAR", IConstant.APP_RELEASE_YEAR);
            
            //CLASSIFIED LISTS
            ListLoader.getInstance().load_nom_module();
            ListLoader.getInstance().load_nom_resultsPerPage();        
            ListLoader.getInstance().load_nom_organisationType();   
            ListLoader.getInstance().load_nom_group_companies_Type();
         
            RoleVoter rv = (RoleVoter) OMContext.getApplicationContext().getBean("roleVoter");
            
            // put permissionConstant bean on servletContext
            sc.setAttribute(IConstant.PERMISSION_CONSTANT, PermissionConstant.getInstance());
            
            // put exceptionContant bean on servletContect
            sc.setAttribute(IConstant.EXCEPTION_CONSTANT, ExceptionConstant.getInstance());
            
            // put backConstant on bean on servletContext
            sc.setAttribute(IConstant.BACK_CONSTANT, BackConstant.getInstance());
            
            // Set on App Context all Organizations' trees
            OMContext.storeOnContext(IConstant.ORGANISATION_TREES, BLOrganisationStructure.getInstance().getAllOrganisationTrees());
            
            //Set on App Context a map specifying which organisation has the audit module
            OMContext.storeOnContext(IConstant.HAS_AUDIT_CONTEXT_MAP, composeHasAuditContextMap());
            
            // Set up the security context for cross modules authentication and authorization
            OMContext.storeOnContext(IConstant.SECURITY_TOKEN_REPOSITORY, new ConcurrentHashMap<String, Token>());
            SecurityTokenMonitor.getInstance().start();
            logger.debug("Cross modules security context initialized!");
            
            // initialize scheduler
            initScheduler(true);
            
            if (isTrialVersion){
            	// manage the trial version timing
            	manageTrial();
            }
            
            // manage the ooo profiles
            manageOOO();
            
            logger.info("Role Prefix: \"" + rv.getRolePrefix() + "\"");
            logger.info("*******************************************************");
            logger.info("*                                                     *");
			logger.info("*    ORGANISATION MANAGEMENT INITIALISATION: END      *");
			logger.info("*                                                     *");
			logger.info("*******************************************************");
			       
		} catch (Exception ex) {
            logger.info("*******************************************************");
			logger.info("*     EROARE LA INTIALIZAREA APLICATIEI !!!           *");
			logger.info("*******************************************************");
			logger.error("", ex);
		}
		logger.info("Aplicatia a fost initializata...");
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
        	sf.initialize(loader.getResourceAsStream(ConfigParametersProvider.getString("business", IConstant.SCHEDULER_CONFIG_FILE_LOCATION)));
	        Scheduler sched = sf.getScheduler();
			sched.start();
			logger.info("Scheduler started!");
			OMContext.storeOnContext(IConstant.SCHEDULER, sched);
        } catch (SchedulerException e) {
			logger.error("", e);
		}
		logger.debug("initScheduler - END");
	}
	
	/**
	 * Creates the trial period job
	 *
	 * @author alu
	 * @throws SchedulerException
	 */
	private void manageTrial() throws SchedulerException {
		logger.debug("mannageTrial - START-");
		
		final String myJob = "TrialJob";
		
		Scheduler sched = (Scheduler)OMContext.getFromContext(IConstant.SCHEDULER);
		
		JobDetail jobDetail = new JobDetail(myJob, myJob.concat("_Group"), TrialJob.class);
	    
	    // initiate SimpleTrigger with its name(myJob.concat("Trigger")) and group name(myJob.concat("_Group"))
	    SimpleTrigger simpleTrigger = new SimpleTrigger(myJob.concat("Trigger"), myJob.concat("_Group"));
	    // set its start up time - 15 minutes from now
	    simpleTrigger.setStartTime(new Date(System.currentTimeMillis()));
	    // set the interval, how often the job should run (900 second here - 15 minutes) - must be set 
	    simpleTrigger.setRepeatInterval(900000);
	    // set the number of execution of this job
	    // it will run 10000 time and exhaust.
	    simpleTrigger.setRepeatCount(50000);
	    
	    sched.scheduleJob(jobDetail, simpleTrigger);
		
		logger.debug("manageTrial - END-");
	}
	
	/**
	 * Creates cancel ooo jobs every time the application is started
	 *
	 * @author alu
	 * @throws SchedulerException
	 * @throws InterruptedException
	 */
	private void manageOOO() throws SchedulerException, InterruptedException {
		logger.debug("mannageOOO - START-");
		
		final String OOO_ID = "OOO_ID";
		final String myJob = "myJob";
		
		Scheduler sched = (Scheduler)OMContext.getFromContext(IConstant.SCHEDULER);		
		
		List<OutOfOffice> res = new ArrayList<OutOfOffice>();
		try {
			res = BLOutOfOffice.getInstance().getAllOOO();
		} catch (BusinessException e) {			
			logger.error("Error at getting out of office profiles!", e);
		}

		for(OutOfOffice outOfOffice : res) {
			JobDetail jobDetail = 
    			new JobDetail(myJob.concat("_").concat(String.valueOf(outOfOffice.getOutOfOfficeId())), 
    					myJob.concat("_Group"), CancelOOOJob.class);
		    jobDetail.getJobDataMap().put(OOO_ID, outOfOffice.getOutOfOfficeId());              
		    
		    // initiate SimpleTrigger with its name("Trigger_" + oooId) and group name(myJob + "_Group")
		    SimpleTrigger simpleTrigger = new SimpleTrigger("Trigger".concat(String.valueOf(outOfOffice.getOutOfOfficeId())), 
		    		myJob.concat("_Group"));
		    // set its start up time
		    simpleTrigger.setStartTime(outOfOffice.getEndPeriod());
		    // set the interval, how often the job should run (1 second here) - must be set 
		    simpleTrigger.setRepeatInterval(1000);
		    // set the number of execution of this job, set to 1 time. 
		    // it will run 1 time and exhaust.
		    simpleTrigger.setRepeatCount(0);
		    
		    Date now = new Date();
		    		    		    
		    if(now.after(outOfOffice.getEndPeriod())){
		    	logger.debug("now = " + now);			    
			    logger.debug("end period = " + outOfOffice.getEndPeriod());
		    	logger.debug("outOfOffice.getOutOfOfficeId() = " + outOfOffice.getOutOfOfficeId());
	        	String jobName = String.valueOf(myJob).concat("_").concat(String.valueOf(outOfOffice.getOutOfOfficeId()));
	        	sched.deleteJob(jobName, String.valueOf(myJob).concat("_Group"));
	        }
	        sched.scheduleJob(jobDetail, simpleTrigger);
		}
		
		logger.debug("mannageOOO - END-");
	}
	

	public Map<Integer, Boolean> composeHasAuditContextMap() throws BusinessException {
		logger.debug("composeHasAuditContextMap START");
		Map<Integer, Boolean> hasAudit = new HashMap<Integer, Boolean>();
		List<Organisation> allOrganisations = BLOrganisation.getInstance().getAllOrganisationsWithModulesForNom();
		
		for (Organisation org : allOrganisations) {
			Boolean hasAuditModule = false;
			Iterator<Module> organisationModules = org.getModules().iterator();
			while (organisationModules.hasNext()) {
				if (organisationModules.next().getModuleId() == IConstant.AUDIT_MODULE_ID){
					hasAuditModule = true;
				}
			}
			hasAudit.put(org.getOrganisationId(), hasAuditModule);
		}
		logger.debug("composeHasAuditContextMap END");
		return hasAudit;
	}

}
