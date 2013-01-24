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
package ro.cs.om.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import ro.cs.om.business.BLOutOfOffice;
import ro.cs.om.common.ApplicationObjectSupport;

/**
 * @author alu
 *
 */
public class CancelOOOJob extends ApplicationObjectSupport implements Job {
	
	public static final String OOO_ID = "OOO_ID";
	
	public void execute(JobExecutionContext jobCtx) throws JobExecutionException {
		logger.debug("CancelOOOJob execute - START");
		
		int oooId = -1;
			
		try {
			// get oooId in order to delete the out of office profile
			oooId = (Integer)jobCtx.getJobDetail().getJobDataMap().get(OOO_ID);
			
			BLOutOfOffice.getInstance().delete(oooId);
			
			logger.debug("OOO PROFILE DELETED - id: ".concat(String.valueOf(oooId)).concat(" end time: ".concat(String.valueOf(jobCtx.getFireTime()))));
		} catch (Exception e) {
			logger.error("ERROR AT DELETING OOO_PROFILE - id:".concat(String.valueOf(oooId)).concat(" JOB NAME:").concat(jobCtx.getJobDetail().getName()), e);
		}
		
		logger.debug("CancelOOOJob execute - END");
	}

}
