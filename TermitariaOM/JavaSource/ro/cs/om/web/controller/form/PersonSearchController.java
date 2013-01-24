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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLDepartment;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLOutOfOffice;
import ro.cs.om.business.BLPerson;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.SearchPersonBean;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;


/**
 * @author alu
 *
 */
public class PersonSearchController extends RootSimpleFormController{
	
	private static final String SEARCH_ERROR = "search.error";	
	private static final String SEARCH_BRANCH_ERROR = "organisation.search.branch.error";
	private static final String SEARCH_BRANCH_EXCEPTION_ERROR = "organisation.search.branch.exception.error";	
	private static final String SEARCH_RESULTS = "SEARCH_RESULTS";
	private static final String PAGINATION = "pagination";
	private static final String DELETE_ALL = "DELETE_ALL";
	private static final String ACTIVATE = "ACTIVATE";
	private static final String DEACTIVATE = "DEACTIVATE";
	private static final String ACTION = "action";
	private static final String LIST_RESULTS_VIEW = "Person_ListPersons";
	private static final String SEARCH_FORM_BEAN = "searchPersonBean";
	private static final String PAGE = "page";
	private static final String NEXT = "next";
	private static final String PREV = "prev";
	private static final String FIRST = "first";
	private static final String LAST = "last";
	private static final String PAGE_NBR = "pagenbr";
	private static final String NUMBER = "nbr";
	private static final String PAGES = "person.pagination.pages";
	private static final String DELETE_ERROR = "person.delete.error";
	private static final String DELETE_SUCCESS = "person.delete.success";
	private static final String ACTIVATE_ERROR = "person.activate.error";
	private static final String ACTIVATE_SUCCESS = "person.activate.success";
	private static final String PERSON_FROM_DISABLED_ORG_ACTIVATE_SUCCESS = "person.from.disabled.organisation.activate.success";
	private static final String DEACTIVATE_ERROR = "person.deactivate.error";
	private static final String DEACTIVATE_SUCCESS = "person.deactivate.success";
	
	private static final String RESULTS_PER_PAGE = "RESULTS_PER_PAGE";
	private static final String DEPARTMENT_FAKE = "department.fake";
	private static final String FIRST_DELETE_OOO = "person.first.delete.ooo";
	
	//---------------------------MODEL ATTRIBUTES------------------------------------
	private static final String MODEL_ORGS = "ORGS";
	private static final String MODEL_DEPARTMENTS = "DEPARTMENTS";	
	private static final String FORM_VIEW = "Person_SearchPerson";
	private static final String SUCCESS_VIEW = "Person_ListPersons";
	
	//--------------------------OTHER PARAMETERS-------------------------------------
	private static final String ALL_ACTIONS 				= "ALL_ACTIONS";
	private static final String BRANCH_DISPLAY 				= "BRANCH_DISPLAY";	
	private final String CMD_RESET_PASSWORD					= "RESET_PASSWORD";	
	private final String DELETE_FROM_PERSON_FORM			= "DELETE_FROM_PERSON_FORM";
	private final String PERSON_ID							= "personId";		
	private static final String JSON_DEPARTMENTS 			= "JSON_DEPARTMENTS";
	private static final String ADMIN_IT					= "ADMIN_IT";
	
		
	private final String RESET_PASSWORD_SUCCESS				= "person.reset.password.success";
	private final String MAIL_SEND_SUCCESS					= "person.mail.success";	
	private final String UPDATE_PASSWORD_ERROR				= "person.update.password.error";
	private final String VIEW_MESSAGES 					 	= "Messages";
	
	// Number of characters that fit in the panel display header
    // if there are big words
    public static final Integer NR_CHARS_PANEL_HEADER			= 50;
	
	public PersonSearchController(){
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
		setCommandName("searchPersonBean");
		setCommandClass(SearchPersonBean.class);
	}
	
	protected Object formBackingObject(HttpServletRequest request)	throws Exception {
		logger.debug("formBackingObject");
		
		SearchPersonBean spb = new SearchPersonBean();	
		
		// set the initial search parameters
		spb.setSortDirection(IConstant.ASCENDING);
		spb.setSortParam("lastName");
		
		spb.setBranch(String.valueOf(request.getSession().getAttribute(IConstant.SESS_ORGANISATION_NAME)));
		
		Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		
		if (organisationId != null) {
			logger.debug("Setting Organisation Id " + organisationId);
			spb.setOrganisationId(organisationId);
		}			
				
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		Integer personId = ServletRequestUtils.getIntParameter(request, PERSON_ID);
		
		// deletes a person if the request comes from person form
		if(action != null && DELETE_FROM_PERSON_FORM.equals(action) && personId != null) {		
			handleDeleteFromPersonForm(request, spb, personId);
		}
		
		return spb;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit - START");
		//used as a container for info/error messages
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		Locale locale = RequestContextUtils.getLocale(request);
		
		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		
		String action = request.getParameter(ACTION);
		logger.debug("action = " + action);
			
		if(action != null) {
			// activate or deactivate a person
			if (ACTIVATE.equals(action)){
				handleActivate(request, command, infoMessages, errorMessages);
			} else if (DEACTIVATE.equals(request.getParameter(ACTION))){
				handleDeactivate(request, command, infoMessages, errorMessages);
			}
			
			// check if i have deletaAll action, to delete persons
			if (DELETE_ALL.equals(action)){
				mav = handleDeleteAll(request, command, infoMessages, errorMessages);
			}
			
			if(CMD_RESET_PASSWORD.equals(action)) { 
				logger.debug("reset password");
				mav = handleResetPassword(request, response, command, errors, errorMessages, infoMessages, locale);
			} 							
		}
		
		// if i have sort action or delete or activate/deactivate action or search
		// action, i have to make a search
		if (action != null && PAGINATION.equals(action)){
			mav = handlePagination(request, command, errorMessages);
		} else {
			mav = handleSearch(command, request, errorMessages);
		}
	
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);

		mav.addAllObjects(referenceData(request));
		
		logger.debug("onSubmit - END");
		return mav;
	}
	
	/**
	 * Handles the results pagination
	 *
	 * @author alu
	 * 
	 * @param request
	 * @param command
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handlePagination(HttpServletRequest request, Object command, ArrayList<String> errorMessages) throws BusinessException{
		logger.debug("handlePagination - START");
		
		ModelAndView mav = new ModelAndView(LIST_RESULTS_VIEW);
		SearchPersonBean searchPersonBean = (SearchPersonBean) command;
		
		try {
			if (request.getParameter(PAGE) != null){
				if (NEXT.equals(request.getParameter(PAGE))){
					searchPersonBean.setCurrentPage(searchPersonBean.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))){
					searchPersonBean.setCurrentPage(searchPersonBean.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))){
					searchPersonBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))){
					searchPersonBean.setCurrentPage(searchPersonBean.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))){
					if (request.getParameter(PAGE_NBR) != null && !"".equals(request.getParameter(PAGE_NBR))){
						searchPersonBean.setCurrentPage(Integer.parseInt(request.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchPersonBean.setCurrentPage(-1);
					}
				}
			}
		} catch(Exception e) {
			// something is wrong
			// I will show the first page
			logger.error("PAGINATION ERROR!",e);
			searchPersonBean.setCurrentPage(-1);		
		}	
		
		Integer sessionOrgId = (Integer)request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		List<Person> res = null;
		
		try{
			res = BLPerson.getInstance().getFromSearch(searchPersonBean, false , sessionOrgId);	
			for(Person person : res) {
				String headerName = person.getFirstName().concat(" ").concat(person.getLastName());
				String panelHeaderName = ControllerUtils.getInstance().truncateName(headerName, NR_CHARS_PANEL_HEADER);
				person.setPanelHeaderName(panelHeaderName);
			}
		} catch(BusinessException be) {
			logger.error(be.getMessage(), be);	
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		mav.addObject(SEARCH_RESULTS, res);		
		
		//if we have all the actions we put them on mav
		mav.addObject(ALL_ACTIONS, manageActions(searchPersonBean, request));
				
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchPersonBean, PAGES);
		
		mav.addObject(SEARCH_FORM_BEAN, searchPersonBean);
		logger.debug("handlePagination - END");
		return mav;
	}
	
	/**
	 * Handles the search for log form submission
	 *
	 * @author alu
	 * 
	 * @param command
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handleSearch(Object command, HttpServletRequest request, ArrayList<String> errorMessages) throws BusinessException{
		logger.debug("handleSearch - START");
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ModelAndView mav = new ModelAndView(LIST_RESULTS_VIEW);
		SearchPersonBean searchPersonBean = (SearchPersonBean) command;							
		
		Integer sessionOrgId = (Integer)request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
						
		logger.debug("Results per page " + searchPersonBean.getResultsPerPage());
		
		List<Person> res = null;	
		
		try{
			res = BLPerson.getInstance().getFromSearch(searchPersonBean, false, sessionOrgId);			
			for(Person person : res) {
				String headerName = person.getFirstName().concat(" ").concat(person.getLastName());
				String panelHeaderName = ControllerUtils.getInstance().truncateName(headerName, NR_CHARS_PANEL_HEADER);
				person.setPanelHeaderName(panelHeaderName);
			}	
		} catch(BusinessException be) {
			logger.error(be.getMessage(), be);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		mav.addObject(SEARCH_RESULTS, res);

		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchPersonBean, PAGES);
		
		//if we have all the actions we put them on mav
		mav.addObject(ALL_ACTIONS, manageActions(searchPersonBean, request));
		
		mav.addObject(SEARCH_FORM_BEAN, searchPersonBean);
		logger.debug("handleSearch - END  ");
		return mav;
	}
	

	/**
	 * Handles the Delete All command
	 * If we have a param that is != null, it means that we have a delete action from the PersonForm.jsp page,
	 * that deletes a single person
	 *
	 * @author dan.damian
	 * @author Adelina
	 * 
	 * @param command
	 * @return
	 * @throws BusinessException 
	 * @throws ServletRequestBindingException
	 */
	private ModelAndView handleDeleteAll(HttpServletRequest request, Object command, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, ServletRequestBindingException {
		logger.debug("handleDeleteAll  BEGIN");
		
		ModelAndView mav = new ModelAndView(LIST_RESULTS_VIEW);		
		SearchPersonBean searchPersonBean = (SearchPersonBean) command;
																	
		handleDeleteAllSimple(request, searchPersonBean);
		logger.debug("searchPersonBean = " + searchPersonBean);
				
		logger.debug("handleDelete END");
		
		return mav;	
	}
		
	/**
	 * Deletes persons
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param searchPersonBean
	 * @throws BusinessException
	 */
	private void handleDeleteAllSimple(HttpServletRequest request, SearchPersonBean searchPersonBean) throws BusinessException {
		
		logger.debug("handleDeleteAllSimple - START -");
		
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<Person> oooPersonReplacements = (List<Person>) BLOutOfOffice.getInstance().getOOOPersonReplacementsFromIds(searchPersonBean.getPersonId());
		if ( oooPersonReplacements != null && oooPersonReplacements.size() != 0){
			String oooReplacementPersonsNames = "";
			for ( Person pers: oooPersonReplacements ){
				if ( oooReplacementPersonsNames != "" ) {
					oooReplacementPersonsNames = oooReplacementPersonsNames.concat(", ");
				}
				oooReplacementPersonsNames = oooReplacementPersonsNames.concat(pers.getFirstName()).concat(" ").concat(pers.getLastName());
			}
			errorMessages.add(messageSource.getMessage(FIRST_DELETE_OOO, new Object[] { oooReplacementPersonsNames }, RequestContextUtils.getLocale(request)));
		}	
		// ****************** Security *******************************
		else if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_PersonDelete()) ) {
			logger.debug(searchPersonBean);
			logger.debug("start deleting " + searchPersonBean.getPersonId().length + " persons...");
			
			for(int i = 0; i < searchPersonBean.getPersonId().length; i++) {
				logger.debug("Deleting Person: " + searchPersonBean.getPersonId()[i]);
				Person person = null;
				boolean deleted = true;
				try{
					person = BLPerson.getInstance().delete(searchPersonBean.getPersonId()[i], ControllerUtils.getInstance().getOrganisationIdFromSession(request));
				}catch(BusinessException bexc) {
					logger.error("", bexc);
					errorMessages.add(messageSource.getMessage(DELETE_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime(), searchPersonBean.getPersonId()[i]}, RequestContextUtils.getLocale(request)));
					deleted = false;
				}
				
				if (deleted) {
					Set<Department> depts = person.getDepts();
					List<Department> departments = new ArrayList<Department>(depts);
					logger.debug("person departments = " + depts);
					Department fakeDepartment = departments.get(0);
					logger.debug("fake Department = " + fakeDepartment);
					logger.debug("fake Department manager id = " + fakeDepartment.getManagerId());				
					logger.debug("personId = " + person.getPersonId());
					if(fakeDepartment.getManagerId() != null && fakeDepartment.getManagerId() == person.getPersonId()) {					
						fakeDepartment.setManagerId(null);
						BLDepartment.getInstance().updateMangerId(fakeDepartment);
					}
					
					List<Department> departmentz = BLDepartment.getInstance().getForManager(person.getPersonId());
					for(Department dept : departmentz) {
						dept.setManagerId(null);
						BLDepartment.getInstance().updateMangerId(dept);
					}
										
					infoMessages.add(messageSource.getMessage(DELETE_SUCCESS, new Object[] {person.getFirstName(), person.getLastName()}, RequestContextUtils.getLocale(request)));
					
					//add the new audit event
					try {
						if (!userAuth.isAdminIT()){
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PERSON_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
								, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_DELETE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("en"))
								, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_DELETE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("ro"))
								, ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						}
					} catch (BusinessException exc){
						logger.error(exc);
					}
				}
			}
		} else {
			errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
		}
		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("handleDeleteAllSimple - END -");		
	}


	/**
	 * Activates one employee's account
	 *
	 * @author dan.damian
	 * 
	 * @param command
	 * @return
	 * @throws BusinessException 
	 * @throws BusinessException
	 */
	private void handleActivate(HttpServletRequest request, Object command, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException {
		logger.debug("handleActivate  BEGIN");
		SearchPersonBean searchPersonBean = (SearchPersonBean) command;
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		boolean activated = true;
		
		// ****************** Security *******************************
		if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_PersonChangeStatus()) ) {
			try{
				UserAuth userAuthToBeActivated = BLPerson.getInstance().getUserAuthByPersonId(searchPersonBean.getPersonId()[0]);
				logger.debug(userAuthToBeActivated.toString());
				userAuthToBeActivated.setEnabled(true);
				userAuthToBeActivated.setStatus(IConstant.NOM_PERSON_STATUS_ACTIVATED);				
				BLPerson.getInstance().updateUserAuth(userAuthToBeActivated);
				
				//add the new audit event
				try {
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PERSON_ENABLE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_ENABLE_MESSAGE, new Object[] {userAuthToBeActivated.getFirstName().concat(" ").concat(userAuthToBeActivated.getLastName())}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_ENABLE_MESSAGE, new Object[] {userAuthToBeActivated.getFirstName().concat(" ").concat(userAuthToBeActivated.getLastName())}, new Locale("ro"))
							, ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			}catch(BusinessException bexc) {
				logger.error("", bexc);
				errorMessages.add(messageSource.getMessage(ACTIVATE_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
				activated = false;
			}catch(Exception e) {
				logger.error("", e);
				errorMessages.add(messageSource.getMessage(ACTIVATE_ERROR, new Object[] {e.getMessage(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
				activated = false;
			}
			
			if (activated) {
				if(ControllerUtils.getInstance().getOrganisationStatusFromSession(request) == IConstant.NOM_ORGANISATION_INACTIVE) {
					errorMessages.add(messageSource.getMessage(PERSON_FROM_DISABLED_ORG_ACTIVATE_SUCCESS, new Object[]{ControllerUtils.getInstance().getOrganisationNameFromSession(request)}, RequestContextUtils.getLocale(request)));
				} else {
					infoMessages.add(messageSource.getMessage(ACTIVATE_SUCCESS, new Object[] {}, RequestContextUtils.getLocale(request)));
				}
			}
		} else {
			errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
		}
		
		logger.debug("handleActivate END");
	}


	/**
	 * Deactivates one employee's account
	 *
	 * @author dan.damian
	 * 
	 * @param command
	 * @return
	 * @throws BusinessException 
	 * @throws BusinessException
	 */
	private void handleDeactivate(HttpServletRequest request, Object command, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException {
		logger.debug("handleDeactivate  BEGIN");
		SearchPersonBean searchPersonBean = (SearchPersonBean) command;
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		boolean activated = true;
		
		// ****************** Security *******************************
		if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_PersonChangeStatus()) ) {
			try{
				UserAuth userAuthToBeActivated = BLPerson.getInstance().getUserAuthByPersonId(searchPersonBean.getPersonId()[0]);
				userAuthToBeActivated.setEnabled(false);
				userAuthToBeActivated.setStatus(IConstant.NOM_PERSON_STATUS_DISABLED);
				logger.debug(userAuthToBeActivated.toString());
				BLPerson.getInstance().updateUserAuth(userAuthToBeActivated);
				
				//add the new audit event
				try {
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PERSON_DISABLE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_DISABLE_MESSAGE, new Object[] {userAuthToBeActivated.getFirstName().concat(" ").concat(userAuthToBeActivated.getLastName())}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_DISABLE_MESSAGE, new Object[] {userAuthToBeActivated.getFirstName().concat(" ").concat(userAuthToBeActivated.getLastName())}, new Locale("ro"))
							, ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			}catch(BusinessException bexc) {
				logger.error("", bexc);
				errorMessages.add(messageSource.getMessage(DEACTIVATE_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
				activated = false;
			} catch(Exception e) {
				logger.error("", e);
				errorMessages.add(messageSource.getMessage(DEACTIVATE_ERROR, new Object[] {e.getMessage(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
				activated = false;
			}
			
			if (activated) {
				infoMessages.add(messageSource.getMessage(DEACTIVATE_SUCCESS, null, RequestContextUtils.getLocale(request)));
			}
		} else {
			errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
		}
		
		
		logger.debug("handleDeactivate END");
	}
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		logger.debug("referenceData - START");
		Map map = new HashMap();
		//used as a container for error messages
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
		Integer organizationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		
		try {
			map.put(MODEL_ORGS, BLOrganisation.getInstance().getAllOrganisationsForNom());	
			
			map.put(JSON_DEPARTMENTS, ControllerUtils.getInstance().getDepartmentsWithFakeFromOrgAsJSON(request, organizationId, RequestContextUtils.getLocale(request),
					errorMessages, messageSource));
			
			Set<Department> departments = BLOrganisation.getInstance().getDepartments(organizationId);
			
			for(Department department : departments){			
				if(department.getStatus() == IConstant.NOM_DEPARTMENT_FAKE){
					department.setName(messageSource.getMessage(DEPARTMENT_FAKE, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
				}
			}
			
			map.put(MODEL_DEPARTMENTS, departments);
			map.put(RESULTS_PER_PAGE, OMContext.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
		} catch(Exception e) {
			logger.error("Exception at getting all organizations for nomenclator!!!", e);
			//TODO DE ADAUGAT mesaj in errorMessages
		}
						
		try {
			//test if we have branches for displaying the branch select
			map.put(BRANCH_DISPLAY, manageBranches(request));
		} catch(BusinessException be) {
			logger.error(be.getMessage(), be);			
			errorMessages.add(messageSource.getMessage(SEARCH_BRANCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(SEARCH_BRANCH_EXCEPTION_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		boolean isAdminIT = ((UserAuth)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isAdminIT();
		
		map.put(ADMIN_IT, isAdminIT);	
		
		setErrors(request, errorMessages);
		logger.debug("referenceData - END");
		return map;
	}
	
	/**
	 * 
	 * if we have all the actions for a department to use 
	 * search for a branch will not display this actions
	 * @author mitziuro
	 *
	 * @param searchPersonBean
	 * @param request
	 * @return
	 */
	private boolean manageActions(SearchPersonBean searchPersonBean, HttpServletRequest request){
		logger.debug("manageActions START");
		boolean actions;
		Integer organisationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		
		//if we search for the organisation that is on the context we have all the options
		if(organisationId.intValue() == searchPersonBean.getOrganisationId()){
			actions = true;
		} else {
			//if we search in all branches we can edit
			if(searchPersonBean.getOrganisationId() == IConstant.SEARCH_ALL_BRANCH_PERSONS){
				actions = true;
			} else {
				actions = false;
			}
		}
			
		logger.debug("manageActions END actions: ".concat(String.valueOf(actions)));
		return actions;
	}
	
	/**
	 * See if the current organisation has branches.
	 * If it has the we display the branch select
	 *
	 * @author mitziuro
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	private boolean manageBranches(HttpServletRequest request) throws BusinessException{
		logger.debug("manageBranches START");
		boolean display = false;
		
		Integer organisationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		
		Organisation organisation = BLOrganisation.getInstance().get(organisationId);
		//If the organisation is company there are no branches
		if(organisation.getType() == IConstant.NOM_ORGANISATION_TYPE_COMPANY) {
			display = false;
		} else {
			List <Organisation> branchList = BLOrganisation.getInstance().getAllOrganisationsForParentId(organisationId);
			if(branchList.size() > 0){
				display = true;
			}
		}
		
		logger.debug("manageBranches END display:".concat(String.valueOf(display)));
		return display;
		
	}
	
	/**
	 * 
	 * @author Adelina
	 * @param request
	 * @param response
	 * @param command
	 * @param bindEx
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return ModelAndView
	 */	
	private ModelAndView handleResetPassword(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindEx, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale){
		logger.debug("handleResetPassword - START-");	
		ModelAndView mav = new ModelAndView(VIEW_MESSAGES);
		SearchPersonBean searchPerson = (SearchPersonBean) command;		
						
		try{
			
			Integer personId = ServletRequestUtils.getIntParameter(request, "personId");
			logger.debug("debug = " + personId);
			Person person = BLPerson.getInstance().get(personId);	
			
			logger.debug("person = " + person);
			logger.debug("person pass = " + person.getPassword());
			
			//the user that is authenticated
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			// if the user has permission OM_ResetPassword, can reset the password
			if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_ResetPassword())) {			
				logger.debug("Sending mail to Person with id: ".concat(String.valueOf(searchPerson.getPersonId())));				
	    		String password = BLPerson.getInstance().resetPassword(person);
	    		
	    		logger.debug("after reset password ");
	    		logger.debug("person pass = " + person.getPassword());

	    		BLPerson.getInstance().sendPasswordByEmailToUser(person, password, locale);
    			infoMessages.add(messageSource.getMessage(MAIL_SEND_SUCCESS, new Object[] {person.getEmail()}, locale));
				
				infoMessages.add(messageSource.getMessage(RESET_PASSWORD_SUCCESS, new Object[] {person.getFirstName(), person.getLastName()}, locale));
				
				//add the new audit event
				try {
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PERSON_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_UPDATE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_UPDATE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("ro"))
							, ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else{ 
				// else, it has not right to reset the password
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(UPDATE_PASSWORD_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch(Exception ex) {
			logger.error("referenceData", ex);
			errorMessages.add(messageSource.getMessage(UPDATE_PASSWORD_ERROR, new Object[] {ex.getMessage(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		logger.debug("handleResetPassword - END -");
	
		return mav;
	}
	
	/* (non-Javadoc)
	 * @see ro.cs.om.web.controller.root.RootSimpleFormController#showForm(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.validation.BindException, java.util.Map)
	 */
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {	
		return super.showForm(request, errors, getFormView(), controlModel);
	}
	
	/**
	 * Deletes a person that comes from a person form
	 * 
	 * @author Adelina
	 * @param request
	 * @param searchPersonBean
	 * @param personId
	 * @throws BusinessException
	 */
	private void handleDeleteFromPersonForm(HttpServletRequest request, SearchPersonBean searchPersonBean, Integer personId) throws BusinessException{						
		logger.debug("handleDeleteFromPersonForm - START - ");
		
		Integer[] persons = new Integer[1];
		persons[0] = personId;			
		searchPersonBean.setPersonId(persons);
		handleDeleteAllSimple(request, searchPersonBean);
				
		logger.debug("handleDeleteFromPersonForm - END - ");
	}	
}
