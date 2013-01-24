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
package ro.cs.om.web.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import ro.cs.om.business.BLLogo;
import ro.cs.om.entity.Logo;
import ro.cs.om.utils.file.FileUtils;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.security.UserAuth;

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
			if(req.getParameter(ORGANISATION) != null) {
				organisationId = Integer.valueOf(req.getParameter(ORGANISATION));		
			} else {
				organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(req);
			}
			
			logger.debug("organisationId = " + organisationId);
			
			Logo logo = getLogo(organisationId);
			
			if (logo == null) {
				logo = new Logo();
				
				//Setting the One Pixel Picture extension
				logo.setExtension(ONE_PIXEL_PIC_EXTENSION);
				
				//Setting the One Pixel Picture as the picture of this logo
				String themeCode = ((UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getThemeCode();
				String onePixelPicLocation = ONE_PIXEL_PIC_LOCATION.replace(REPLACE_THEME, themeCode);
				onePixelPicLocation = FileUtils.getInstance().getRealPathForResource(onePixelPicLocation);
				logo.setPicture(FileUtils.getInstance().getBytesFromFile(new File(onePixelPicLocation)));
			} 
			
			//Setting response's content type after picture's extension 
			resp.setContentType(getMime(logo.getExtension()));
			
			logger.debug("extenstion = "  + logo.getExtension());
			logger.debug("mime = " + getMime(logo.getExtension()));
			
			//Setting response's length (in bytes)
			resp.setContentLength(logo.getPicture().length);
			
			logger.debug("length = " + logo.getPicture().length);
			
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
	 * Abstract function to be implemented by each concrete implementation of this ImageServlet.
	 * 
	 */
	public Logo getLogo(int organisationId) throws Exception {
		logger.debug("organisationId id: " + organisationId);
		return BLLogo.getInstance().getByOrganisationId(organisationId);
	}
	
	/**
	 * Dumps Picture's content in an OutputStream
	 */
	private void dumpFile(Logo logo, OutputStream outputstream) throws IOException {
		logger.debug("dumpFile - START -");
	
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {		
												
			// Use Buffered Stream for reading/writing.
			bis = new BufferedInputStream(new ByteArrayInputStream(logo.getPicture()));
			bos = new BufferedOutputStream(outputstream);
			byte[] buff = new byte[4096];
			int bytesRead;
			// Simple read/write
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final MalformedURLException e) {
			logger.debug("==========MalformedURLException.==============");
			throw e;
		} catch (final IOException e) {
			logger.debug("==============IOException.==============");
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
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
