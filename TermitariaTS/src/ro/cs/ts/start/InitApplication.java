/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *    Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *     
 *    Termitaria is free software; you can redistribute it and/or 
 *    modify it under the terms of the GNU Affero General Public License 
 *    as published by the Free Software Foundation; either version 3 of 
 *    the License, or (at your option) any later version.
 *    
 *    This program is distributed in the hope that it will be useful, 
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *    GNU Affero General Public License for more details.
 *    
 *    You should have received a copy of the GNU Affero General Public License 
 *    along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.ts.start;

import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.vote.RoleVoter;

import ro.cs.ts.common.BackConstant;
import ro.cs.ts.common.ExceptionConstant;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.ws.client.reports.entity.ReportParams;

/**
 * @author matti_joona
 * @author alu
 *
 * Servlet prin care se initializeaza contextul aplicatiei. Informatiile de dimensiuni reduse se stocheaza
 * in aceasta zona pentru a fi disponibile catre alte clase care prelucreaza aceste informatii
 */
public class InitApplication extends HttpServlet{

	private static Log logger = LogFactory.getLog(InitApplication.class);
	private static String CONFIG = "config";

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
			logger.info("*        INITIATING APPLICATION TIME SHEET->          *");
			logger.info("*                                                     *");
			logger.info("*******************************************************");
            logger.info(IConstant.APP_VERSION.concat("/").concat(IConstant.APP_RELEASE_DATE));
            sc.setAttribute("VERSION", IConstant.APP_VERSION);
            sc.setAttribute("RELEASE_DATE", IConstant.APP_RELEASE_DATE);
            sc.setAttribute("RELEASE_YEAR", IConstant.APP_RELEASE_YEAR);
            
            //Nomenclators         
            
            // RESULTS PER PAGE
            ListLoader.getInstance().load_nom_resultsPerPage(); 
            
            // PROJECT
            ListLoader.getInstance().load_nom_projectStatus();
            
            //BILLABLE
            ListLoader.getInstance().load_nom_billable();
            
            // TIME UNIT
            ListLoader.getInstance().load_nom_timeUnit();
            
            //SUPPORTED LANGUAGES
            ListLoader.getInstance().load_nom_supportedLanguages();
            
            //REPORT SUBTOTAL INTERVAL
            ListLoader.getInstance().load_nom_reportSubtotalInterval();
            
            //REPORT SUBTOTAL INTERVAL
            ListLoader.getInstance().load_nom_pricesComputeType();

            //REPORT PARAMS MAP
            TSContext.storeOnContext(IConstant.REPORT_PARAM_MAP, new HashMap<String,ReportParams>());
            
            RoleVoter rv = (RoleVoter) TSContext.getApplicationContext().getBean("roleVoter");
            
            // put exceptionContant bean on servletContect
            sc.setAttribute(IConstant.EXCEPTION_CONSTANT, ExceptionConstant.getInstance());  
            sc.setAttribute(IConstant.PERMISSION_CONSTANT, PermissionConstant.getInstance());
            sc.setAttribute(IConstant.BACK_CONSTANT, BackConstant.getInstance()); 
            
            logger.info("Role Prefix: \"" + rv.getRolePrefix() + "\"");
            logger.info("*******************************************************");
			logger.info("*                                                     *");
			logger.info("*       INITIATING APPLICATION END TIME SHEET<-       *");
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

}
