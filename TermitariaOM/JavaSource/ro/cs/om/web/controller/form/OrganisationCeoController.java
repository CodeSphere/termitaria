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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLDepartment;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLPerson;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Person;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;

/**
 * Controller for OrganistionCeo
 * 
 * @author Adelina
 * 
 */
public class OrganisationCeoController extends RootSimpleFormController {

	private static final String FORM_VIEW 				= "OrganisationCeo";
	private static final String SUCCESS_VIEW			= "OrganisationCeo";
	
	private final String MODEL_MANAGER_NAME				= "MANAGER_NAME";
	private final String MODEL_JSON_PERSONS				= "JSON_PERSONS";	
		
	private static final String GET_ERROR 				= "department.get.error";

	private static final String UPDATE_SUCCESS 			= "organisation.ceo.success";
	private static final String UPDATE_ERROR 			= "organisation.ceo.error";
	private static final String GET_PERSON_ERROR		= "person.get.error";	

	public OrganisationCeoController() {
		setCommandName("departmentBean");
		setCommandClass(Department.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		logger.debug("formBackingObject - START");
		ArrayList<String> errorMessages = new ArrayList<String>();		
		logger.debug("formBackingObject - END");
		// return the fake department
		return handleGet(request,errorMessages);
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit - START");

		ModelAndView mav = null;
		Department departmentBean = (Department) command;

		Locale locale = RequestContextUtils.getLocale(request);
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();

		// check if i have department id
		if (departmentBean.getDepartmentId() != -1) {
			mav = handleUpdate(departmentBean, locale, errorMessages, infoMessages);
		}

		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		//Add referenceData objects to model
		mav.addAllObjects(referenceData(request, departmentBean, errors));	
		
		logger.debug("onSubmit - END");
		return mav;
	}
	
	/**
	 * Get the fake department from the organisation
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @return Department
	 * @throws ServletRequestBindingException
	 * @throws BusinessException
	 */
	private Department handleGet(HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("handleGet - START");
	
		Department department = null;
		try {
			department = BLDepartment.getInstance()
					.getFakeForOrganization(
							ControllerUtils.getInstance()
									.getOrganisationIdFromSession(request));
		} catch (BusinessException be) {
			logger.error("handleGet", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR,
					new Object[] {
							be.getCode(),
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() }, RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("handleGet", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR,
					new Object[] {
							null,
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() }, RequestContextUtils.getLocale(request)));
		}	
			
		logger.debug("handleGet - END");
		return department;
	}

	/**
	 * Handles the update of the CEO of the organisation. The CEO for an organisation
	 * 
	 * @author Adelina
	 * 
	 * @param department
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @return ModelAndView
	 */
	private ModelAndView handleUpdate(Object command, Locale locale,
			ArrayList<String> errorMessages, ArrayList<String> infoMessages) {
		logger.debug("handleUpdate - START");
		ModelAndView mav = new ModelAndView(getSuccessView());
		try {
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			// if the user has permission OM_OrgAddCeo, can add the CEO for the organisation
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_OrgAddCeo())){
				Department department = (Department) command;	
				mav.addObject(getCommandName(), department);
				
				BLDepartment.getInstance().updateCEO(department);
				infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, null,
						locale));
				
				//add the new audit event
				try {
					Organisation org = BLOrganisation.getInstance().get(department.getOrganisationId());
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ORGANISATION_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
								, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_UPDATE_MESSAGE, new Object[] {org.getName()}, new Locale("en"))
								, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_UPDATE_MESSAGE, new Object[] {org.getName()}, new Locale("ro"))
								, org.getOrganisationId(), userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				// else, it has not the right to add the CEO for the organisation
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}

		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR,
					new Object[] {
							be.getCode(),
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() }, locale));
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR,
					new Object[] {
							null,
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() }, locale));
		}
		logger.debug("handleUpdate - END");
		return mav;
	}
	
	/* (non-Javadoc)
	 * @see ro.cs.om.web.controller.root.RootSimpleFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		logger.debug("referenceData - START");
		
		Map modelAux = new HashMap();		
		ArrayList<String> errorMessages = new ArrayList<String>();
	
		Department department = (Department) command;
		
		Integer managerId = department.getManagerId();
		
		logger.debug("department manager = " + managerId);
		
		if(managerId != null) {
			Person departmentManager = null;
			try {
				departmentManager = BLPerson.getInstance().get(managerId);
				if(departmentManager != null) {
					logger.debug("department manager = " + departmentManager.getFirstName().concat(" ").
							concat(departmentManager.getLastName()));
				}
			} catch (BusinessException be) {
				logger.error("referenceData", be);
				errorMessages.add(messageSource.getMessage(GET_PERSON_ERROR,
						new Object[] {
								be.getCode(),
								ControllerUtils.getInstance()
										.getFormattedCurrentTime() }, RequestContextUtils.getLocale(request)));
			} catch (Exception e) {
				logger.error("referenceData", e);
				errorMessages.add(messageSource.getMessage(GET_PERSON_ERROR,
						new Object[] {
								null,
								ControllerUtils.getInstance()
										.getFormattedCurrentTime() }, RequestContextUtils.getLocale(request)));
			}
			if (departmentManager != null) {
				modelAux.put(MODEL_MANAGER_NAME, departmentManager.getFirstName().concat(" ").
						concat(departmentManager.getLastName()));
			}
		}
						
		Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);		
		modelAux.put(MODEL_JSON_PERSONS, ControllerUtils.getInstance().
				getPersonsActivated(organisationId, RequestContextUtils.getLocale(request), 
							errorMessages, messageSource));
									
		setErrors(request, errorMessages);
		
		logger.debug("referenceData - END");
		return modelAux;
	}
}
