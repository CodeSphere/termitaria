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
package ro.cs.ts.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import ro.cs.ts.common.ApplicationObjectSupport;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.context.TSContext;

/**
 * @author dan.damian
 *
 */
public class FileUtils extends ApplicationObjectSupport {
	
	private final static String MIME_TYPE_CONFIGURATION		= "collection.zone.mime.type";
	private final static String DEFAULT_MIME_TYPE 			= "application/octet-stream";
	private final static String ATTRIBUTE_NAME				= "value";
	private static String ROOT_PACKAGE 						= "ro";
	private static String LOCATION_OF_CLASSES 				= "WEB-INF";
	private static String KB								= "KB";
	
	private static FileUtils theInstance = null;
	static{
		theInstance = new FileUtils();
	}
	private FileUtils() {}
	
	public static FileUtils getInstance() {
		return theInstance;
	}
	
	/**
	 * 
	 * Get the name from a java.io.File object
	 * @author mitziuro
	 *
	 * @param file
	 * @return
	 */
	public String getFileName(File file){
		String fileName = file.getName();
		return getFileName(fileName);
	}
	
	/**
	 * 
	 * Get the name from a java.lang.String object
	 * @author mitziuro
	 *
	 * @param file
	 * @return
	 */
	public String getFileName(String file){
	
		String name = null;
		//we get only the name from the file name
		if(file.contains(".")) {
			name = file.substring(0, file.lastIndexOf('.'));
		} else {
			name = file;
		}
		return name;
	}
	
	/**
	 * 
	 * Get the extension for a java.io.File object
	 * @author mitziuro
	 *
	 * @param file
	 * @return
	 */
	public String getFileExtension(File file){
		
		String fileName = file.getName();
		return getFileExtension(fileName);
		
	}
	
	/**
	 * 
	 * Get the extension for a java.lang.String object
	 * @author mitziuro
	 *
	 * @param file
	 * @return
	 */
	public String getFileExtension(String fileName) {
		
		String extension = null;
		//put the extension
		extension = fileName.substring(fileName.lastIndexOf('.')+1, fileName.length());
		//if the file doesn't have extension
		if(extension.length() == fileName.length()){
			extension = "";
		}
		
		return extension;
		
	}
	
	/**
	 * 
	 * Get the extension from mimetype
	 * @author mitziuro
	 *
	 * @param mimeType
	 * @return
	 */
	public String getFileExtensionFromMimeType(String mimeType) {
		
		String extension = ((Map<String, String>) TSContext.getFromContext(IConstant.NOM_EXTENSION_TYPE)).get(mimeType);
		
		//if we don't find an extansion we don't apply one
		if(extension == null) {
			return "";
		}
		
		return ".".concat(extension);
		
	}
	
	/**
	 * Prepares the name of the element for insertion in the xPath
	 * @author mitziuro
	 * 
	 */
	public String getfileNameToXpathName(String fileName){
		
		StringBuffer sb = new StringBuffer();
		
		//Special case '_'
		for(int i=0; i<fileName.length(); i++){
			if(fileName.charAt(i) == '_'){
				sb.append("\\_");
			} else {
				sb.append(fileName.charAt(i));
			}
		}	
		
		return sb.toString();
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
		//Location the pictures behind nodes
	    URL url = Thread.currentThread().getContextClassLoader().getResource(ROOT_PACKAGE);
	    if (url == null) {
	    	throw new RootPackageNotFoundException("Root package not found !");
	    }
	    String realPathLocation = url.getFile();
	    realPathLocation = realPathLocation.substring(0, realPathLocation.indexOf(LOCATION_OF_CLASSES) - 1);
	    realPath.append(realPathLocation).append(File.separator).append(projectRelativeResourcePath);
	    
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
            throw new IOException("Could not completely read file " + file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        
        return bytes;
    }
    
    /**
     * Returns size of file as text
     * @author mitziuro
     * @param size
     * @return
     */
    public String getSizeAsText(long size){
   
    	long result = 0;
    	long percentage = 0;
    	
    	result = (size / 1024);
 
    	percentage = ((size - (size / 1024)) * 100 )/ 1024;
    	
    	if(percentage == 0){
    		return result + " " +KB;
    	}
    	
    	return result + "." + String.valueOf(percentage).substring(0,String.valueOf(percentage).length() > 3 ? 2 : String.valueOf(percentage).length()) + " " +KB;
   	
    }
    
}
