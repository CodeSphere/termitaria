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
package ro.cs.ts.web.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.oxm.XmlMappingException;
import org.springframework.security.core.context.SecurityContextHolder;

import ro.cs.ts.common.IConstant;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.WSClientException;
import ro.cs.ts.utils.FileUtils;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.WSLogo;


/**
 * @author dd
 * 
 */
public class LogoServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final String ORGANISATION = "ORG";
	
	private static String REPLACE_THEME = "replaceTheme";
	
	private static String ONE_PIXEL_PIC_LOCATION = "themes".concat(File.separator).concat(REPLACE_THEME).concat(File.separator).
								concat("images").concat(File.separator).concat("onePixel.jpg");
	
	private static String ONE_PIXEL_PIC_EXTENSION = "jpg";
	
	protected Log logger = LogFactory.getLog(this.getClass());
	
		@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.debug("doGet - START -");
		doPost(req, resp);
		logger.debug("doGet - END -");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.debug("doPost - START -");
		ServletOutputStream sos = null;
		logger.debug("-----------------------------------------");
		
		try {
			//Servlet's OutputStream
			sos = resp.getOutputStream();
			Integer organisationId  = null;
			
			WSLogo logo = null;
			
			//if there is an exception in getting the logo we display the pixel
			try{
				logo = getLogo((Integer) req.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID));
			}catch (Exception e) {
				logger.debug("Exception at finding logo: Using default pixel");
			}
			
			if (logo == null || logo.getPicture() == null) {
				
				logo = new WSLogo();
				
				//Setting the One Pixel Picture extension
				logo.setExtension(ONE_PIXEL_PIC_EXTENSION);
				
				String themeCode = IConstant.STANDARD_THEME;
				//Setting the One Pixel Picture as the picture of this logo
				try{
					themeCode = ((UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getThemeCode();
				} catch (Exception e) {
					logger.debug("Exception at getting the theme");
					
				}
				
				String onePixelPicLocation = ONE_PIXEL_PIC_LOCATION.replace(REPLACE_THEME, themeCode);
				onePixelPicLocation = FileUtils.getInstance().getRealPathForResource(onePixelPicLocation);
				logo.setPicture(FileUtils.getInstance().getBytesFromFile(new File(onePixelPicLocation)));
			} 
			
			//test if we have logo picture
			if(logo.getPicture() != null){
				//Setting response's content type after picture's extension 
				resp.setContentType(getMime(logo.getExtension()));
				
				//Setting response's length (in bytes)
				resp.setContentLength(logo.getPicture().length);
			} else {
				resp.setContentType("image/gif");
				resp.setContentLength(1);
			}
			
			
			//Writing the picture
			dumpFile(logo, sos);
			
		} catch (Exception ex) {
			logger.error("", ex);
		} finally {
			//Flushing and Closing OutputStream
			sos.flush();
			sos.close();
		}
		logger.debug("doPost - END -");
	}
	
	/**
	 * Get the logo
	 * @author mitziuro
	 * @param organisationId
	 * @return
	 * @throws WSClientException 
	 * @throws IOException 
	 * @throws BusinessException 
	 * @throws XmlMappingException 
	 */
	private WSLogo getLogo(int organisationId) throws XmlMappingException, BusinessException, IOException, WSClientException{
		return OMWebServiceClient.getInstance().getLogo(organisationId);
	}
	
	/**
	 * Dumps Picture's content in an OutputStream
	 */
	private void dumpFile(WSLogo logo, OutputStream outputstream) throws IOException {
		logger.debug("dumpFile - START -");
		byte buffer[] = new byte[4096];
		BufferedInputStream bufferedinputstream = new BufferedInputStream(
				new ByteArrayInputStream(logo.getPicture()));
		int i;
		while ((i = bufferedinputstream.read(buffer, 0, 4096)) != -1)
			outputstream.write(buffer, 0, i);
		bufferedinputstream.close();
		logger.debug("dumpFile - END -");
	}

	/**
	 * Returns the mime type according to an extension
	 */
	private String getMime(String extension) {
		String s1 = extension.toUpperCase();
		if (s1.equals("GIF"))
			return "image/gif";
		if (s1.equals("JPG") || s1.equals("JPEG") || s1.equals("JPE"))
			return "image/jpeg";
		if (s1.startsWith("TIF"))
			return "image/tiff";
		if (s1.startsWith("PNG"))
			return "image/png";
		if (s1.equals("IEF"))
			return "image/ief";
		if (s1.equals("BMP"))
			return "image/bmp";
		if (s1.equals("RAS"))
			return "image/x-cmu-raster";
		if (s1.equals("PNM"))
			return "image/x-portable-anymap";
		if (s1.equals("PBM"))
			return "image/x-portable-bitmap";
		if (s1.equals("PGM"))
			return "image/x-portable-graymap";
		if (s1.equals("PPM"))
			return "image/x-portable-pixmap";
		if (s1.equals("RGB"))
			return "image/x-rgb";
		if (s1.equals("XBM"))
			return "image/x-xbitmap";
		if (s1.equals("XPM"))
			return "image/x-xpixmap";
		if (s1.equals("XWD"))
			return "image/x-xwindowdump";
		else
			return "application/octet-stream";
	}

}
