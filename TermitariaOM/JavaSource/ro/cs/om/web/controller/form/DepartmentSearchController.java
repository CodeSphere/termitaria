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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLDepartment;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLOrganisationStructure;
import ro.cs.om.business.BLPerson;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.SearchDepartmentBean;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;
import ro.cs.tools.Tools;



/**
 * @author alu
 * @author dan.damian
 *
 */
public class DepartmentSearchController extends RootSimpleFormController{
	
	private static final String SEARCH_ERROR = "search.error";
	private static final String GENERAL_ERROR= "department.general.search.error";
	private static final String SEARCH_EXCEPTION_ERROR = "organisation.search.exception.error";
	private static final String SEARCH_BRANCH_ERROR = "organisation.search.branch.error";
	private static final String SEARCH_BRANCH_EXCEPTION_ERROR = "organisation.search.branch.exception.error";	
	private static final String SEARCH_RESULTS = "SEARCH_RESULTS";
	private static final String PAGINATION = "pagination";
	private static final String ACTION = "action";
	private static final String LIST_RESULTS_VIEW = "Department_ListDepartments";
	private static final String SEARCH_FORM_BEAN = "searchDepartmentBean";
	private static final String PAGE = "page";
	private static final String NEXT = "next";
	private static final String PREV = "prev";
	private static final String FIRST = "first";
	private static final String LAST = "last";
	private static final String PAGE_NBR = "pagenbr";
	private static final String NUMBER = "nbr";
	private static final String PAGES = "department.pagination.pages";
	private static final String DELETE_ERROR = "department.delete.error";	
	private static final String DELETE_SUCCESS = "department.delete.success";
	private static final String GET_PERSONS_BY_ONLY_DEPT_ERROR = "person.get.by.only.department.error";
	private static final String ORGS = "ORGS";
	private static final String RESULTS_PER_PAGE = "RESULTS_PER_PAGE";	
	private static final String JSON_PERSONS = "JSON_PERSONS";	
	private static final String JSON_DEPARTMENTS = "JSON_DEPARTMENTS";
	
	//--------------------------OTHER PARAMETERS-------------------------------------
	private static final String ALL_ACTIONS 		= "ALL_ACTIONS";
	private static final String BRANCH_DISPLAY 		= "BRANCH_DISPLAY";
	private static final String DELETE_ALL			= "DELETE_ALL";
	
	
	private static final String DELETE_FROM_DEPARTMENT_FORM = "DELETE_FROM_DEPARTMENT_FORM";
	private final String DEPARTMENT_ID						= "departmentId";	

	
	// Number of characters that fit in the panel display header
    // if there are big words
    public static final Integer NR_CHARS_PANEL_HEADER			= 45;
	
	
	public DepartmentSearchController(){
		setFormView("Department_SearchDepartment");
		setCommandName("searchDepartmentBean");
		setCommandClass(SearchDepartmentBean.class);
	}
	
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		logger.debug("formBackingObject");
		
		SearchDepartmentBean sdb = new SearchDepartmentBean();
		sdb.setBranch(String.valueOf(request.getSession().getAttribute(IConstant.SESS_ORGANISATION_NAME)));
		Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		
		// set the initial search parameters
		sdb.setSortDirection(IConstant.ASCENDING);
		sdb.setSortParam("name");
		
		if (organisationId != null) {
			logger.debug("Setting Organisation Id " + organisationId);
			sdb.setOrganisationId(organisationId);			
		}
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		Integer departmentId = ServletRequestUtils.getIntParameter(request, DEPARTMENT_ID);
		logger.debug("departmentId = " + departmentId);
		
		// deletes a department if the request comes from department form
		if(action != null && DELETE_FROM_DEPARTMENT_FORM.equals(action) && departmentId != null) {		
			handleDeleteFromDepartmentForm(request, sdb, departmentId);
		}
		
		
		return sdb;
	}	
		
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit - START");
		//used as a container for info/error messages
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		
		if (request.getParameter(ACTION) != null && PAGINATION.equals(request.getParameter(ACTION))){
			mav = handlePagination(request, command, errorMessages);
		}else if (DELETE_ALL.equals(request.getParameter(ACTION))){
			mav = handleDeleteAll(request, command, infoMessages, errorMessages);
		} else {
			mav = handleSearch(command, request, errorMessages);
		}
		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		
		logger.debug("onSubmit - END");
		
		
		return mav;
	}
	
	/**
	 * Handles the results pagination
	 *
	 * @author alu
	 * @author dan.damian
	 * 
	 * @param request
	 * @param command
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handlePagination(HttpServletRequest request, Object command , ArrayList<String> errorMessages) throws BusinessException{
		logger.debug("handlePagination - START");
		
		ModelAndView mav = new ModelAndView(LIST_RESULTS_VIEW);
		SearchDepartmentBean searchDepartmentBean = (SearchDepartmentBean) command;
		
		try {
			if (request.getParameter(PAGE) != null){
				if (NEXT.equals(request.getParameter(PAGE))){
					searchDepartmentBean.setCurrentPage(searchDepartmentBean.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))){
					searchDepartmentBean.setCurrentPage(searchDepartmentBean.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))){
					searchDepartmentBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))){
					searchDepartmentBean.setCurrentPage(searchDepartmentBean.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))){
					if (request.getParameter(PAGE_NBR) != null && !"".equals(request.getParameter(PAGE_NBR))){
						searchDepartmentBean.setCurrentPage(Integer.parseInt(request.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchDepartmentBean.setCurrentPage(-1);
					}
				}
			}
		} catch(Exception e) {
			// something is wrong
			// I will show the first page
			logger.error("PAGINATION ERROR!",e);
			searchDepartmentBean.setCurrentPage(-1);		
		}	
		
		List<Department> res = null;
		
		try{
			res = BLDepartment.getInstance().getFromSearch(searchDepartmentBean, false);
			
			for(Department dept : res) {
				String panelHeaderName = ControllerUtils.getInstance().truncateName(dept.getName(), NR_CHARS_PANEL_HEADER);
				dept.setPanelHeaderName(panelHeaderName);
			}		
			
		} catch(BusinessException be) {
			logger.error(be.getMessage(), be);	
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_EXCEPTION_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		mav.addObject(SEARCH_RESULTS, res);		
		
		//if we have all the actions we put them on mav
		mav.addObject(ALL_ACTIONS, manageActions(searchDepartmentBean, request));
		
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchDepartmentBean, PAGES);
		
		mav.addObject(SEARCH_FORM_BEAN, searchDepartmentBean);
		logger.debug("handlePagination - END");
		return mav;
	}
	
	/**
	 * Handles the search for Organizations form submission
	 *
	 * @author alu
	 * @author dan.damian
	 * @author mitziuro
	 * 
	 * @param command
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handleSearch(Object command, HttpServletRequest request, ArrayList<String> errorMessages) throws BusinessException{
		logger.debug("handleSearch - START");
				
		ModelAndView mav = new ModelAndView(LIST_RESULTS_VIEW);
		SearchDepartmentBean searchDepartmentBean = (SearchDepartmentBean) command;	
		
		logger.debug("SearchDepartmentBean = " + searchDepartmentBean);
		
		logger.debug("Results per page " + searchDepartmentBean.getResultsPerPage());
						
		List<Department> res = null;
		try{
			res = BLDepartment.getInstance().getFromSearch(searchDepartmentBean, false);
			
			for(Department dept : res) {				
				String panelHeaderName = ControllerUtils.getInstance().truncateName(dept.getName(), NR_CHARS_PANEL_HEADER);
				dept.setPanelHeaderName(panelHeaderName);
			}					
		
		} catch(BusinessException be) {
			logger.error(be.getMessage(), be);	
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_EXCEPTION_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}	
		
		Tools.getInstance().printList(logger, res);
		
		//if we have all the actions we put them on mav
		mav.addObject(ALL_ACTIONS, manageActions(searchDepartmentBean, request));
		
		mav.addObject(SEARCH_RESULTS, res);
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchDepartmentBean, PAGES);
		mav.addObject(SEARCH_FORM_BEAN, searchDepartmentBean);
		
		return mav;
	}
	
	
	/**
	 * Handles the search for log form submission
	 *
	 * @author dan.damian
	 * 
	 * @param command
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handleDeleteAll(HttpServletRequest request, Object command, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException{
		logger.debug("handleDeleteAll  BEGIN");
		
		ModelAndView mav = new ModelAndView(LIST_RESULTS_VIEW);
		SearchDepartmentBean searchDepartmentBean = (SearchDepartmentBean) command;
				
		
		handleDeleteAllSimple(request, searchDepartmentBean);
		
		logger.debug("Results per page " + searchDepartmentBean.getResultsPerPage());
		List<Department> res = BLDepartment.getInstance().getFromSearch(searchDepartmentBean, true);
		
		for(Department dept : res) {
			String panelHeaderName = ControllerUtils.getInstance().truncateName(dept.getName(), NR_CHARS_PANEL_HEADER);
			dept.setPanelHeaderName(panelHeaderName);
		}		
		mav.addObject(SEARCH_RESULTS, res);
		
		//if we have all the actions we put them on mav
		mav.addObject(ALL_ACTIONS, manageActions(searchDepartmentBean, request));
		
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchDepartmentBean, PAGES);
		mav.addObject(SEARCH_FORM_BEAN, searchDepartmentBean);
		
		logger.debug("handleDelete END");
		return mav;
	}
	
	/**
	 * Deletes departments
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param searchDepartmentBean
	 * @throws BusinessException
	 */
	private void handleDeleteAllSimple(HttpServletRequest request, SearchDepartmentBean searchDepartmentBean) throws BusinessException {
		
		logger.debug("handleDeleteAllSimple - START -");
		
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		logger.debug(searchDepartmentBean);
		logger.debug("start deleting " + searchDepartmentBean.getDepartmentId().length + " department...");
		
		// ****************** Security *******************************
		if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_DeptDelete())) {
			for(int i = 0; i < searchDepartmentBean.getDepartmentId().length; i++) {
				logger.debug("Deleting Department: " + searchDepartmentBean.getDepartmentId()[i]);
				Department department = null;
				boolean deleted = true;
				try{
					try{
						BLPerson.getInstance().getPersonByOnlyOneDepartment(searchDepartmentBean.getOrganisationId(), searchDepartmentBean.getDepartmentId()[i]);
					} catch(BusinessException be) {
						logger.error("", be);
						errorMessages.add(messageSource.getMessage(GET_PERSONS_BY_ONLY_DEPT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime(), department.getName()}, RequestContextUtils.getLocale(request)));
					}
					department = BLDepartment.getInstance().delete(searchDepartmentBean.getDepartmentId()[i]);
				}catch(BusinessException bexc) {
					logger.error("", bexc);
					errorMessages.add(messageSource.getMessage(DELETE_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime(), department.getName()}, RequestContextUtils.getLocale(request)));
					deleted = false;
				}
				
				if (deleted) {					
					String departmentNameMessage = ControllerUtils.getInstance().tokenizeField(department.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
					
					infoMessages.add(messageSource.getMessage(DELETE_SUCCESS, new Object[] {departmentNameMessage}, RequestContextUtils.getLocale(request)));
					//Update all the organizations structures that include this department as a node
					BLOrganisationStructure.getInstance().updateAllOrganisationsStructure(department.getOrganisation().getOrganisationId());
					
					//add the new audit event
					try {
						if (!userAuth.isAdminIT()){
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_DEPARTMENT_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
								, messageSource.getMessage(IConstant.AUDIT_EVENT_DEPARTMENT_DELETE_MESSAGE, new Object[] {department.getName(), department.getOrganisation().getName()}, new Locale("en"))
								, messageSource.getMessage(IConstant.AUDIT_EVENT_DEPARTMENT_DELETE_MESSAGE, new Object[] {department.getName(), department.getOrganisation().getName()}, new Locale("ro"))
								, department.getOrganisation().getOrganisationId(), userAuth.getPersonId());
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
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		logger.debug("referenceData - START");
		//used as a container for error messages
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
		Map map = new HashMap();
		try {
			map.put(ORGS, BLOrganisation.getInstance().getAllOrganisationsForNom());
			map.put(RESULTS_PER_PAGE, OMContext.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));								
			Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			map.put(JSON_PERSONS, ControllerUtils.getInstance().
					getPersonsFirstNameLastNameFromOrgAsJSON(organisationId, RequestContextUtils.getLocale(request), 
							errorMessages, messageSource));				
			map.put(JSON_DEPARTMENTS, ControllerUtils.getInstance().getDepartmentFromOrgAsJSON(organisationId, RequestContextUtils.getLocale(request),
					errorMessages, messageSource));					
			
		} catch (Exception e) {
			logger.error("referenceData", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));

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
		
		setErrors(request, errorMessages);
		logger.debug("referenceData - END");
		return map;
	}
	
	 /**
	 * 
	 * if we have all the actions for a department to use 
	 * search for a branch will not display this actions
	 * @author mihai
	 *
	 * @param searchDepartmentBean
	 * @param request
	 * @return
	 */
	private boolean manageActions(SearchDepartmentBean searchDepartmentBean, HttpServletRequest request){
		logger.debug("manageActions START");
		boolean actions;
		Integer organisationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		
		//if we search for the organisation that is on the context we have all the options
		if(organisationId.intValue() == searchDepartmentBean.getOrganisationId()){
			actions = true;
		} else {
			actions  = false;
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
	 * Deletes a department that comes from a department form 
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param searchDepartmentBean
	 * @param departmentId
	 * @throws BusinessException
	 */
	private void handleDeleteFromDepartmentForm(HttpServletRequest request, SearchDepartmentBean searchDepartmentBean, Integer departmentId) throws BusinessException{						
		logger.debug("handleDeleteFromDepartmentForm - START - ");
		
		Integer[] departments = new Integer[1];
		departments[0] = departmentId;			
		searchDepartmentBean.setDepartmentId(departments);
		handleDeleteAllSimple(request, searchDepartmentBean);
				
		logger.debug("handleDeleteFromDepartmentForm - END - ");
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
	
}
