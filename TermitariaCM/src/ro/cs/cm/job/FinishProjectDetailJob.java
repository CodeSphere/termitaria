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
package ro.cs.cm.job;

import java.io.File;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import ro.cs.cm.common.ApplicationObjectSupport;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.utils.SerializeEntity;
import ro.cs.cm.ws.client.ts.TSWebServiceClient;

public class FinishProjectDetailJob extends ApplicationObjectSupport implements Job {
			
	public void execute(JobExecutionContext jobCtx) throws JobExecutionException {
		logger.debug("FinishProjectDetailJob execute - START");
		
		final Exception[] exception = new Exception[1];
		
	final SerializeEntity entity = SerializeEntity.getInstance();
		
		synchronized(entity) {
			
			final File file = SerializeEntity.getInstance().createFile(IConstant.FILE_NAME_SERIALIZATION_FOR_FINISH_PROJECT_DETIALS);
			
			Set<Integer> projectIds = SerializeEntity.getInstance().doLoadAll(file);
			logger.debug("projectIds = " + projectIds);
								
			entity.doPersist(projectIds,file);
			
			if(projectIds.size() > 0) {
				for(Integer projectId : projectIds) {			
					logger.debug("projectId - " + projectId);
					final Integer id = projectId;
					
					class FinishProjectDetailRunnable implements Runnable {
						public void run() {
							logger.debug("iddddddddddddddd = " + id);
							logger.debug("FinishProjectDetailRunnable - run START");
							try{						
								TSWebServiceClient.getInstance().finishProjectDetails(id);
								entity.doLoad(id, file);
							} catch (Exception e) {					
				    			exception[0] = e;
							}
							logger.debug("FinishProjectDetailRunnable - run END");
						}
					}
					
					Thread thread = new Thread(new FinishProjectDetailRunnable());		
					thread.start();		
				}			
			}
		}
			
		logger.debug("FinishProjectDetailJob execute - END");
	}	

}
