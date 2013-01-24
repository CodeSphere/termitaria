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
package ro.cs.logaudit.web.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Encoder;

/**
 * @author dan.damian
 */
public class EncryptionUtils {

	protected final Log logger = LogFactory.getLog(getClass());
	
	private static EncryptionUtils theInstance = null;
	static{
		theInstance = new EncryptionUtils();
	}
	private EncryptionUtils() {}
	
	public static EncryptionUtils getInstance() {
		return theInstance;
	}
	
	protected static String MD5 	= "MD5";
	protected static String SHA1 	= "SHA1";
	protected static String SHA256 	= "SHA256";
	protected static String SHA384 	= "SHA384";
	protected static String SHA512 	= "SHA512";
	
	
	private String getHash(String message, String algorithm) {
		try {
			byte[] buffer = message.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(buffer);
			byte raw[] = md.digest();
			String hash = (new BASE64Encoder()).encode(raw);
			return hash;
		} catch(NoSuchAlgorithmException e) {
			System.err.println(e);
		} catch (UnsupportedEncodingException e) {
			System.err.println(e);			
		}
		return null;
	}
	
	
	public String getMD5Hash(String message) {
		return getHash(message, MD5);
	}
	
	public String getSHA1Hash(String message) {
		return getHash(message, SHA1);
	}
}
