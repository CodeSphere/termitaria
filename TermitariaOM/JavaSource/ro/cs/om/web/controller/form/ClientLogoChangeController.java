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
package ro.cs.om.web.controller.form;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLLogo;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.entity.Logo;
import ro.cs.om.entity.Organisation;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.utils.file.FileUtils;
import ro.cs.om.utils.image.ImageUtils;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.entity.LogoPictureWeb;
import ro.cs.om.web.security.UserAuth;

/**
 * Changes Client's Logo
 * @author dd
 */
public class ClientLogoChangeController extends RootSimpleFormController {

	private static final String ORGANISATION = "ORG";
	private static final String NOAPPLY = "NOAPPLY";
	
	public ClientLogoChangeController(){
		setCommandName("clientLogoBean");
		setCommandClass(LogoPictureWeb.class);
		setFormView("ClientLogoChange");   
		setSuccessView("ClientLogoChange");
	}
	
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
				throws ServletException {
	// to actually be able to convert Multipart instance to byte[]
	// we have to register a custom editor
	binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	// now Spring knows how to handle multipart object and convert them
	}
	
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		
		logger.debug("formBackingObject");

		int organisationId;
		
		if(request.getParameter(ORGANISATION) != null) {
			organisationId = Integer.valueOf(request.getParameter(ORGANISATION));			
			if(organisationId == ControllerUtils.getInstance().getOrganisationIdFromSession(request)) {
				request.setAttribute(NOAPPLY, NOAPPLY);
			}
		} else {
			organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			request.setAttribute(NOAPPLY, NOAPPLY);
		}
		
		request.setAttribute(ORGANISATION, organisationId);
		Logo logo = BLLogo.getInstance().getByOrganisationId(organisationId);
		if (logo != null) {
			LogoPictureWeb logoPictureBean = new LogoPictureWeb();
			logoPictureBean.setLogoId(logo.getLogoId());
			return logoPictureBean; 
		}
		
		//return super.formBackingObject(request);
		return new LogoPictureWeb();
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
						
		logger.debug("onSubmit - START -");
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		int organisationId;
		
		if(request.getParameter(ORGANISATION) != null) {
			organisationId = Integer.valueOf(request.getParameter(ORGANISATION));
		} else {
			organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		}
		
		
		if (organisationId > 0) {
			
			Logo logo = new Logo();
			LogoPictureWeb logoPictureBean = (LogoPictureWeb) command;
			
			
			if (logoPictureBean != null) {
				if (logoPictureBean.getFile() != null) {
					logger.debug("Size: " + logoPictureBean.getFile().getSize());
					logger.debug("Content Type: " + logoPictureBean.getFile().getContentType());
					logger.debug("Name: " + logoPictureBean.getFile().getName());
					logger.debug("Original file name: " + logoPictureBean.getFile().getOriginalFilename());
					
					String fileExtension = FileUtils.getInstance().getFileExtension(logoPictureBean.getFile().getOriginalFilename());
					if ("JPG".equals(fileExtension.toUpperCase()) || "PNG".equals(fileExtension.toUpperCase()) || "GIF".equals(fileExtension.toUpperCase()) || "BMP".equals(fileExtension.toUpperCase())) {
						
						int[] dimensions = ImageUtils.getInstance().getImageDimensions(logoPictureBean.getFile().getBytes());
						if(dimensions[0] <= IConstant.LOGO_WIDTH && dimensions[1] <= IConstant.LOGO_HEIGHT) {
							logo.setExtension(fileExtension);
							logo.setPicture(ImageUtils.getInstance().createResizedCopy(logoPictureBean.getFile().getBytes(),
										IConstant.LOGO_WIDTH, IConstant.LOGO_HEIGHT));
							logo.setDateCreated(new Timestamp(System.currentTimeMillis()));
							logo.setDateModified(null);
							logo.setOrganisationId(organisationId);
							UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			

							// ****************** Security *******************************
							if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_ChangeLogo())){						
								if (logoPictureBean.getLogoId() > 0) {
									logo.setLogoId(logoPictureBean.getLogoId());
									BLLogo.getInstance().update(logo);
								} else {
									BLLogo.getInstance().add(logo);
								}
								
								//add the new audit event
								try {
									Organisation org = BLOrganisation.getInstance().get(organisationId);
									if (!userAuth.isAdminIT()){
										BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ORGANISATION_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
												, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_UPDATE_MESSAGE, new Object[] {org.getName()}, new Locale("en"))
												, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_UPDATE_MESSAGE, new Object[] {org.getName()}, new Locale("ro"))
												, organisationId, userAuth.getPersonId());
									}
								} catch (BusinessException exc){
									logger.error(exc);
								}
							} else {
								errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
							}
							
							response.getWriter().write("<img src=\"servlet/LogoServlet?ORG=" + organisationId + "\"/>");
						}else {
							logger.error("The Logo was not uploaded. It's dimensions exceeded " + IConstant.LOGO_WIDTH + "x" + IConstant.LOGO_HEIGHT);
							errorMessages.add(messageSource.getMessage("organization.logo.invalidFileDimensions", null,  RequestContextUtils.getLocale(request)));
							response.getWriter().write("<div class='error_msg'>" + messageSource.getMessage("organization.logo.invalidFileDimensions", null,  RequestContextUtils.getLocale(request)) + "</div>");
						}
							
													
					} else {
						logger.error("The Logo was not uploaded. It's not a jpg, png, bmp or gif picture");
						errorMessages.add(messageSource.getMessage("organisation.logo.invalidFileType", null,  RequestContextUtils.getLocale(request)));
						response.getWriter().write("<div class='error_msg'>" + messageSource.getMessage("organisation.logo.invalidFileType", null,  RequestContextUtils.getLocale(request)) + "</div>");
					}
				} else {
					logger.error("Logo was not uploaded");
				}
			}
		} else {
			logger.error("A relation between this Logo and this Organization could not be established.");
		}
		setErrors(request, errorMessages);
		logger.debug("onSubmit - END -");
		return null;
	}
	

		
}
