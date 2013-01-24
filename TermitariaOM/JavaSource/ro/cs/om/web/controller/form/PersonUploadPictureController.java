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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import ro.cs.om.business.BLPerson;
import ro.cs.om.business.BLPicture;
import ro.cs.om.common.IConstant;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.Picture;
import ro.cs.om.utils.file.FileUtils;
import ro.cs.om.utils.image.ImageUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.entity.PersonPictureWeb;

/**
 * @author dan.damian
 * Uploads a Person's picture on the server.
 */
public class PersonUploadPictureController extends RootSimpleFormController {

	private String PERSON_ID 	= "personId";
	private String PICTURE_ID 	= "pictureId";
	
	public PersonUploadPictureController(){
		setCommandName("pictureBean");
		setCommandClass(PersonPictureWeb.class);
		setFormView("Person_PictureUpload");
		setSuccessView("Person_PictureUploadResponse");
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
		request.setAttribute(PERSON_ID, request.getParameter(PERSON_ID));
		request.setAttribute(PICTURE_ID, request.getParameter(PICTURE_ID));
		return super.formBackingObject(request);
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		int personId = 0;
		
		try {
			personId = Integer.parseInt(request.getParameter(PERSON_ID));
		} catch(NumberFormatException nfex) {
			
		}
		
		if (personId > 0) {
			
			Picture picture = BLPicture.getInstance().getPictureByPersonId(personId);
			if(picture == null){
				picture = new Picture();
				picture.setPictureId(0);
			}
			PersonPictureWeb pictureBean = (PersonPictureWeb) command;
			
			
			if (pictureBean != null) {
				if (pictureBean.getFile() != null) {
					logger.debug("Size: " + pictureBean.getFile().getSize());
					logger.debug("Content Type: " + pictureBean.getFile().getContentType());
					logger.debug("Name: " + pictureBean.getFile().getName());
					logger.debug("Original file name: " + pictureBean.getFile().getOriginalFilename());
					
					String fileExtension = FileUtils.getInstance().getFileExtension(pictureBean.getFile().getOriginalFilename());
					if ("JPG".equals(fileExtension.toUpperCase()) || "PNG".equals(fileExtension.toUpperCase()) || "GIF".equals(fileExtension.toUpperCase()) || "BMP".equals(fileExtension.toUpperCase())) {
						
						picture.setExtension(fileExtension);
						picture.setPicture(ImageUtils.getInstance().createResizedCopy(pictureBean.getFile().getBytes(),
									IConstant.PERSON_PICTURE_WIDTH, IConstant.PERSON_PICTURE_HEIGHT));
						picture.setDateCreated(new Timestamp(System.currentTimeMillis()));
						picture.setDateModified(null);
						picture.setName(pictureBean.getFile().getOriginalFilename());
						picture.setWidth(IConstant.PERSON_PICTURE_WIDTH);
						picture.setHeight(IConstant.PERSON_PICTURE_HEIGHT);
						Person person = BLPerson.getInstance().get(new Integer(personId));
						picture.setPersonId(person.getPersonId());
						if(picture.getPictureId() == 0){
							BLPicture.getInstance().add(picture);
						}
						BLPicture.getInstance().update(picture);
				
					} else {
						logger.error("Nu a fost uploadata poza. Nu este o poza jpg, png, bmp sau gif");	
					}
				} else {
					logger.error("Nu a fost uploadata poza");
				}
				mav.addObject(PICTURE_ID, picture.getPictureId());
			}
		} else {
			logger.error("Nu se poate face relationarea Pozei cu persoana");
		}
		
		return mav;
	}
	

	
	

	
}
