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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import ro.cs.om.business.BLPerson;
import ro.cs.om.common.ApplicationObjectSupport;
import ro.cs.om.utils.file.FileUtils;

/**
 * @author alu
 * 4 Feb 2010
 */

public class TrialJob extends ApplicationObjectSupport implements Job {
	
	private final static Integer trialPeriod = new Integer(43200); // in minutes = 30 days
	private final static Integer incrementalTimeValue = new Integer(15); // minutes
	private final static String filename = "FCJob.ser"; // Flying crocodile job ;)
	private final static Integer defaultValue = new Integer(1);
	
	public void execute(JobExecutionContext jobCtx) throws JobExecutionException {
		logger.debug("TrialJob execute - START");
		
		Integer runningTime;
		File file = null;
		
		try {
			file = new File(FileUtils.getInstance().getRealPathForResource("WEB-INF").concat(File.separator).concat("classes").concat(File.separator).concat("ro").
					concat(File.separator).concat("cs").concat(File.separator).concat("om").concat(File.separator).concat("job").concat(File.separator).concat(filename));
			
			// if the file doesn't exist, create it
			if (!file.exists()){
				file.createNewFile();
				doPersist(file, defaultValue);
			} else {
				// get the value for application running time and increment it with incrementalTimeValue
				runningTime = doLoad(file) + incrementalTimeValue;
				
				if (runningTime >= trialPeriod) {
					// if the running time has past the trial period, make the application unusable: disable all person
					BLPerson.getInstance().disableAll();
				} else {
					// it means that it is still in the trial period; persist the new running time
					doPersist(file, runningTime);
				}
			}	
		} catch (Exception e) {
			logger.error("ERROR... trial period still available", e);
		}
		
		logger.debug("TrialJob execute - END");
	}
	
	private void doPersist(File file, Integer value) throws IOException{
		logger.debug("doPersist - BEGIN value: ".concat(String.valueOf(value)));
		
		FileOutputStream fileOut = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);

        // write the new value
        out.writeObject(value);

        logger.debug("Closing all output streams...");
        out.close();
        fileOut.close();
        
        logger.debug("doPersist - END");
	}
	
	private Integer doLoad(File file) throws IOException, ClassNotFoundException{
		logger.debug("doLoad - BEGIN");
		Integer value = -1;
		
		FileInputStream fileIn = new FileInputStream(file);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		
		// get the value from the 
        value = (Integer) in.readObject();
        
        logger.debug("Closing all input streams...");
        in.close();
        fileIn.close();

        logger.debug("doLoad - END value: ".concat(String.valueOf(value)));
        return value;
	}

}
