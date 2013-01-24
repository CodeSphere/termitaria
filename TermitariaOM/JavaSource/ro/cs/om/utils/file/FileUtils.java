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
package ro.cs.om.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;

import ro.cs.om.common.ApplicationObjectSupport;

/**
 * @author dan.damian
 *
 */
public class FileUtils extends ApplicationObjectSupport {

	
	private static String ROOT_PACKAGE = "ro";
	private static String LOCATION_OF_CLASSES = "WEB-INF";
	
	
	private static FileUtils theInstance = null;
	static{
		theInstance = new FileUtils();
	}
	private FileUtils() {}
	
	public static FileUtils getInstance() {
		return theInstance;
	}
	
	public String getFileExtension(String fileName) {
		return fileName.substring(fileName.indexOf('.') + 1, fileName.length());
	}
	
	/**
	 * Return the real path for a Resource(any kind of file) located in the Project.
	 * 
	 * @author dan.damian
	 * @param projectRelativeResourcePath the relative to the root path (WebContent) of the project  
	 * @return
	 * @throws RootPackageNotFoundException 
	 */
	public String getRealPathForResource(String projectRelativeResourcePath) throws RootPackageNotFoundException {
		StringBuffer realPath = new StringBuffer();
		
	    URL url = Thread.currentThread().getContextClassLoader().getResource(ROOT_PACKAGE);
	    	  
	    if (url == null) {
	    	throw new RootPackageNotFoundException("Root package not found !");
	    }
	    String realPathLocation = url.getFile();	    
	    realPathLocation = realPathLocation.substring(0, realPathLocation.indexOf(LOCATION_OF_CLASSES) - 1);	    
	    String realPathLocationDecoded = new String();
	    try{
	    	realPathLocationDecoded = URLDecoder.decode(realPathLocation.toString());
	    } catch (IllegalArgumentException ex) {
	    	ex.printStackTrace();
	    }
	    
	    realPath.append(realPathLocationDecoded).append(File.separator).append(projectRelativeResourcePath);
	    	    	   	    	   	    	  
	    return realPath.toString();
	}
	
	
	/**
	 * Returns the contents of the file in a byte array.
	 */
    public byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
    
        if (length > Integer.MAX_VALUE) {
            System.out.println("File is too large");
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        
        return bytes;
    }

}
