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
package ro.cs.om.utils.encryption;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import ro.cs.om.common.ApplicationObjectSupport;
import sun.misc.BASE64Encoder;

/**
 * @author dd
 */
public class EncryptionUtils extends ApplicationObjectSupport {

	
	private static EncryptionUtils theInstance = null;
	static{
		theInstance = new EncryptionUtils();
	}
	private EncryptionUtils() {}
	
	public static EncryptionUtils getInstance() {
		return theInstance;
	}
	
	private String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
        	int halfbyte = (data[i] >>> 4) & 0x0F;
        	int two_halfs = 0;
        	do {
	            if ((0 <= halfbyte) && (halfbyte <= 9))
	                buf.append((char) ('0' + halfbyte));
	            else
	            	buf.append((char) ('a' + (halfbyte - 10)));
	            halfbyte = data[i] & 0x0F;
        	} while(two_halfs++ < 1);
        }
        return buf.toString();
    }
	
	 public String getSHA1Hash(String text)   throws NoSuchAlgorithmException, UnsupportedEncodingException  {
			MessageDigest md;
			md = MessageDigest.getInstance("SHA-1");
			byte[] sha1hash = new byte[40];
			md.update(text.getBytes("iso-8859-1"), 0, text.length());
			sha1hash = md.digest();
			String sha1HashHex = convertToHex(sha1hash); 
			return sha1HashHex;
	    }
	
	 
	 public String encrypt(String str) {
	        StringBuffer encryptedResult = new StringBuffer();
	        String strWithSalt = str;

	        try {
	            MessageDigest md = MessageDigest.getInstance("SHA1");
	            md.update(strWithSalt.getBytes());
	            byte[] digest = md.digest();

	            for (byte b : digest) {
	                int number = b;
	                // Convert negative numbers
	                number = (number < 0) ? (number + 256) : number;
	                encryptedResult.append(Integer.toHexString(number));
	            }

	        } catch (NoSuchAlgorithmException ex) {
	            System.out.println(ex.toString());
	        }

	        return encryptedResult.toString();
	    }

	 
	 public String getSHA1HashVar2(String message) {
			try {
				byte[] buffer = message.getBytes("UTF-8");
				MessageDigest md = MessageDigest.getInstance("SHA1");
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
	
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String password = "icmadmin";
		
		EncryptionUtils eU = new EncryptionUtils();
		System.out.println(eU.getSHA1Hash(password));
		System.out.println(eU.encrypt(password));
		System.out.println(eU.getSHA1HashVar2(password));
	}

}
