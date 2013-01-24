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
package ro.cs.cm.utils;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import ro.cs.cm.common.ApplicationObjectSupport;

public class SerializeEntity extends ApplicationObjectSupport {
	
	private static SerializeEntity theInstance = null;	
	private File file;	
		
	private SerializeEntity() {			
	
	}
		
	static {
		theInstance = new SerializeEntity();
	}
	
	public static SerializeEntity getInstance() {
		if(theInstance == null) {
			
		}
		return theInstance;
	}
	
	/**
	 * Serialization
	 * 
	 * @author Adelina
	 * 
	 * @param values
	 */			
	public synchronized void doPersist(Set<Integer> values, File file) { 
		logger.debug("doPersist - BEGIN value: ".concat(String.valueOf(values)));
		
		logger.debug("values = " + values);
		
		try{
			setFile(file);			
			FileOutputStream fileOut = new FileOutputStream(getFile());
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);	        	        		     
	        // write the new value in the file
	        for(Integer value : values) {
	        	out.writeObject(value);
	        }
	        	        	       
	        logger.debug("Closing all output streams...");	    
	        out.close();	        
	        fileOut.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        logger.debug("doPersist - END");
	}
	
	/**
	 * Deserialization
	 * 
	 * @author Adelina
	 * 
	 * @param value
	 * @return
	 */
	public synchronized Integer doLoad(Integer value, File file) {
		logger.debug("doLoad - BEGIN");
		logger.debug("value = " + value);
		Integer element = -1;
		try{
			setFile(file);		
			FileInputStream fileIn = new FileInputStream(getFile());
			if(fileIn.available() != 0) {
				ObjectInputStream in = new ObjectInputStream(fileIn);
				
				Set<Integer> values = new HashSet<Integer>();
				
				while (true) {
					try {
						element = (Integer)in.readObject();
						if(!element.equals(value)) {
							logger.debug("element = " + element);
							values.add(element);
						}
					} catch(EOFException e) {
						break;
					}
				}
				
				logger.debug("values = " + values);
			        
			    doPersist(values, file);
				
				logger.debug("Closing all input streams...");
			
		        in.close();	        
			}
	        fileIn.close();	        	     
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        logger.debug("doLoad - END value: ".concat(String.valueOf(value)));
        return value;
	}
	
	/**
	 * Read what is in the file
	 * 
	 * @author Adelina
	 * 
	 * @return
	 */
	public synchronized Set<Integer> doLoadAll(File file) {
		logger.debug("doLoadAll - BEGIN");
		
		Set<Integer> values = new HashSet<Integer>();
		
		Integer element = -1;
						
		try{			
			setFile(file);
			FileInputStream fileIn = new FileInputStream(getFile());
			if(fileIn.available() != 0) {
				ObjectInputStream in = new ObjectInputStream(fileIn);
										
				while (true) {
					try {
						element = (Integer)in.readObject();					
						logger.debug("element = " + element);
						values.add(element);					
					} catch(EOFException e) {
						break;
					}
				}
				
				logger.debug("Closing all input streams...");
		        in.close();		       
			}
			fileIn.close();
	        	    
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        logger.debug("doLoadAll - END");
        return values;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	/**
	 * Create file by fileName
	 * 
	 * @author Adelina
	 * 
	 * @param fileName
	 * @return
	 */
	public File createFile(String fileName) {
		File file = null;
		try {
			file = new File(FileUtils.getInstance().getRealPathForResource("WEB-INF").concat(File.separator).concat(fileName));
			
			// if the file doesn't exist, create it
			if(!file.exists()) {
				file.createNewFile();
			}
			logger.debug("fisierul se afla = " + file.getAbsolutePath());
		} catch (RootPackageNotFoundException e) {		
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}		
		return file;
	}
}

