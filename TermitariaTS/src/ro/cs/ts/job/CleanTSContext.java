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
package ro.cs.ts.job;

import java.util.Calendar;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import ro.cs.ts.common.IConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.ws.client.reports.entity.ReportParams;

public class CleanTSContext extends QuartzJobBean{
	
	/** Logger that is available to subclasses */
	private final Log logger = LogFactory.getLog(getClass());

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		logger.debug("J O B: Clean TS CONTEXT > ");
		
		Map<String,ReportParams> reportParamsMap = (Map<String,ReportParams>) TSContext.getFromContext(IConstant.REPORT_PARAM_MAP);
		
		Long currentTime = Calendar.getInstance().getTimeInMillis();
		int removeCount = 0;
		for (Entry<String,ReportParams> entry : reportParamsMap.entrySet()){
			String entryTimeString = entry.getKey().split("_")[0];
			Long entryTime = Long.parseLong(entryTimeString);
			//logger.debug("Entry time: "+entryTime);
			//logger.debug("Current time - entry time: "+currentTime+" - "+entryTime+ " = "+(currentTime-entryTime));
			if ((currentTime-entryTime) > 600000){//10 min
				reportParamsMap.remove(entry.getKey());
				removeCount++;
			}
		}
		logger.debug("Removed: "+removeCount+" old param maps "+reportParamsMap.size()+" remaining");
		logger.debug("J O B: Clean TS CONTEXT END< ");
		
	}

}
