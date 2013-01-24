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

import java.lang.reflect.InvocationTargetException;
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
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLLocalization;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLRole;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.Localization;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Role;
import ro.cs.om.entity.SearchRoleBean;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;

/**
 * @author alu
 *
 */
public class RoleSearchController extends RootSimpleFormController{
	
	private static final String FORM_VIEW = "Role_Search";
	private static final String SUCCESS_VIEW = "Role_Listing";
	private static final String SEARCH_RESULTS = "ROLES";
	private static final String SEARCH_ROLE_BEAN = "searchRoleBean";
	private static final String COMMAND = "command";
	private static final String PAGES = "role.pagination.pages";
	private static final String PAGINATION = "pagination";
	private static final String ACTION = "action";
	private static final String PAGE = "page";
	private static final String NEXT = "next";
	private static final String PREV = "prev";
	private static final String FIRST = "first";
	private static final String LAST = "last";
	private static final String PAGE_NBR = "pagenbr";
	private static final String NUMBER = "nbr";	
	private static final String DELETE_ALL = "DELETE_ALL";	
	private static final String DELETE_ERROR = "role.delete.error";
	private static final String HAS_ASSOCIATED_PERSON = "role.has.associated.person";
	private static final String DELETE_NOT_ROLE = "role.not.delete";
	
	private static final String SEARCH_BRANCH_ERROR = "organisation.search.branch.error";
	private static final String SEARCH_BRANCH_EXCEPTION_ERROR = "organisation.search.branch.exception.error";
	private static final String DELETE_SUCCESS = "role.delete.success";
	private static final String SEARCH_ERROR = "role.search.error";
	private static final String PAGINATION_ERROR = "PAGINATION ERROR!!!!!!!!!!!!!!";
	private static final String ORGANISATIONS = "ORGANISATIONS";	
	private static final String MODULES = "MODULES";
	private static final String DELETE_FROM_ROLE_FORM = "DELETE_FROM_ROLE_FORM";
	private static final String ROLE_ID 			  = "roleId";

	
	//--------------------------OTHER PARAMETERS-------------------------------------
	private static final String ALL_ACTIONS 		= "ALL_ACTIONS";
	private static final String BRANCH_DISPLAY 		= "BRANCH_DISPLAY";
	
	public RoleSearchController(){
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
		setCommandName("searchRoleBean");
		setCommandClass(SearchRoleBean.class);
	}
	
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		
		logger.debug("formBackingObject - START");
		SearchRoleBean srb = new SearchRoleBean();
		
		//we set the initial search parameters
		srb.setSortDirection(IConstant.ASCENDING);
		srb.setSortParam("name");
		
		try {
			//set the branch the existent organisation
			srb.setBranch(String.valueOf(request.getSession().getAttribute(IConstant.SESS_ORGANISATION_NAME)));
			request.setAttribute(ORGANISATIONS, BLOrganisation.getInstance().getAllOrganisationsForNom());
			logger.debug("ORAGANISATIONS CLASSIFIED LIST LOADED");
		} catch(BusinessException be) {
			logger.error("Exception at getting all the organisations for nomenclator!!!", be);
		}
		
		//if any user except admin_IT is logged on, I have to retrieve the organisationId ( and its modules )in order to permit operations only on roles
		//from that specific organisation
		Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		
		if ( organisationId != null ){
			srb.setOrganisationId(organisationId.intValue());
			logger.debug("Setting Organisation ID in role request " + organisationId);
			Set<Module> modules = BLOrganisation.getInstance().getModules( organisationId );
			if ((modules != null) && (modules.size() != 0)){
				request.setAttribute(MODULES, modules);
			} 
		}
		
		request.setAttribute(IConstant.NOM_RESULTS_PER_PAGE, OMContext.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
		logger.debug("RESULTS PER PAGE CLASSIFIED LIST LOADED");
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		Integer roleId = ServletRequestUtils.getIntParameter(request, ROLE_ID);
		
		// deletes a role if the request comes from role form
		if(action != null && DELETE_FROM_ROLE_FORM.equals(action) && roleId != null) {		
			handleDeleteFromRoleForm(request, srb, roleId);
		}
		
		logger.debug("formBackingObject - END");
		return srb;
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit - START");
		
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		

		boolean isDeleteAction = false;
		// check if i have delete all action
		if(request.getParameter(ACTION) != null && DELETE_ALL.equals(request.getParameter(ACTION))){
			mav = handleDeleteAll(request, command, infoMessages, errorMessages);
			isDeleteAction = true;
		} else if (request.getParameter(ACTION) != null && PAGINATION.equals(request.getParameter(ACTION))){
			mav = handlePagination(request, command, errorMessages);
		} else {
			mav = handleSearch(request, command, isDeleteAction, errorMessages);
		}
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);

		//we change the view because we have errors
		if(errorMessages.size() > 0){
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		}

		logger.debug("onSubmit - END");
		return mav;		
	}
	
	/**
	 * Deletes all the roles or a specific role
	 * @author Adelina
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private ModelAndView handleDeleteAll(HttpServletRequest request, Object command, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		
		logger.debug("handleDeleteAll - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());		
		SearchRoleBean searchRoleBean = (SearchRoleBean) command;
		
		handleDeleteAllSimple(request, searchRoleBean);
		
		logger.debug("Results per page " + searchRoleBean.getResultsPerPage());
		List<Role> roles = null;
		try {
			roles = BLRole.getInstance().getResultsForSearch(searchRoleBean, true);
			Localization localization = null;
			for(Role r : roles){
				String locale = RequestContextUtils.getLocale(request).getLanguage();
				localization = BLLocalization.getInstance().getByLocale(r.getDescription().getLocalizationId(), locale.toUpperCase());
				r.setSketch(ControllerUtils.getInstance().getDescriptionForLocale(localization, locale));
			}
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		mav.addObject(SEARCH_RESULTS, roles);
	
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchRoleBean, PAGES);						
		mav.addObject(SEARCH_ROLE_BEAN, searchRoleBean);
		
		//if we have all the actions we put them on mav
		mav.addObject(ALL_ACTIONS, manageActions(searchRoleBean, request));
		
		
		logger.debug("handleDeleteAll - END");
		return mav;

	}
	
	/**
	 * Deletes roles
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param searchRoleBean
	 * @throws BusinessException
	 */
	private void handleDeleteAllSimple(HttpServletRequest request, SearchRoleBean searchRoleBean) throws BusinessException {
		
		logger.debug("handleDeleteAllSimple - START -");
		
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		logger.debug(searchRoleBean);
		logger.debug("start deleting " + searchRoleBean.getRoleId().length + " roles.");
		Role role = null;
		boolean hasAssociatedPerson = false;
		
		// ****************** Security *******************************
		if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_RoleDelete())) {
			for(int i = 0; i < searchRoleBean.getRoleId().length; i++){
				logger.debug("Delete role : " + searchRoleBean.getRoleId()[i]);					
				try{
					hasAssociatedPerson = BLRole.getInstance().hasRoleAssociatePerson(searchRoleBean.getRoleId()[i]);
				} catch (BusinessException be) {
					logger.error("", be);
					errorMessages.add(messageSource.getMessage(HAS_ASSOCIATED_PERSON, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
				}
				boolean isDeleted = true;
				if(!hasAssociatedPerson) {
					try{
						role = BLRole.getInstance().deleteAll(searchRoleBean.getRoleId()[i]);	
						
						//add the new audit event
						try {
							Organisation org = BLOrganisation.getInstance().get(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
							if (!userAuth.isAdminIT()){
								BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ROLE_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
									, messageSource.getMessage(IConstant.AUDIT_EVENT_ROLE_DELETE_MESSAGE, new Object[] {role.getName(), org.getName()}, new Locale("en"))
									, messageSource.getMessage(IConstant.AUDIT_EVENT_ROLE_DELETE_MESSAGE, new Object[] {role.getName(), org.getName()}, new Locale("ro"))
									, ControllerUtils.getInstance().getOrganisationIdFromSession(request) , userAuth.getPersonId());
							}
						} catch (BusinessException exc){
							logger.error(exc);
						}
					}catch(BusinessException be){
						logger.error("", be);
						errorMessages.add(messageSource.getMessage(DELETE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
						isDeleted = false;
					}			
					if(isDeleted){
						String roleNameMessage = ControllerUtils.getInstance().tokenizeField(role.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
						infoMessages.add(messageSource.getMessage(DELETE_SUCCESS, new Object[] {roleNameMessage}, RequestContextUtils.getLocale(request)));
					}			
				} else {
					errorMessages.add(messageSource.getMessage(DELETE_NOT_ROLE, new Object[] {BLRole.getInstance().get(searchRoleBean.getRoleId()[i]).getName()}, RequestContextUtils.getLocale(request)));
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

	private ModelAndView handleSearch(HttpServletRequest request, Object command, boolean isDeleteAction, ArrayList<String> errorMessages)  throws BusinessException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException{
		logger.debug("handleSearch - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());		
		SearchRoleBean searchRoleBean = (SearchRoleBean) command;
		
		List<Role> res = null;
		
		try{
			
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_RoleSearch())){
				res = BLRole.getInstance().getResultsForSearch(searchRoleBean, isDeleteAction);
				Localization localization = null;
				//setting localization by locale
				for(Role role : res){
					String locale = RequestContextUtils.getLocale(request).getLanguage();
					localization = BLLocalization.getInstance().getByLocale(role.getDescription().getLocalizationId(), locale.toUpperCase());
				
					role.setSketch(ControllerUtils.getInstance().getDescriptionForLocale(localization, locale));
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
			}
			
		} catch (BusinessException be) {
			logger.error("", be);
			setSuccessView(IConstant.FORM_VIEW_MESSAGES);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		mav.addObject(SEARCH_RESULTS, res);
		
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchRoleBean, PAGES);
		
		//if we have all the actions we put them on mav
		mav.addObject(ALL_ACTIONS, manageActions(searchRoleBean, request));
		
		mav.addObject(SEARCH_ROLE_BEAN, searchRoleBean);
		mav.addObject(COMMAND, command);
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
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private ModelAndView handlePagination(HttpServletRequest request, Object command, ArrayList<String> errorMessages) throws BusinessException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		logger.debug("handlePagination - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchRoleBean searchRoleBean = (SearchRoleBean) command;
		
		try {
			if (request.getParameter(PAGE) != null){
				if (NEXT.equals(request.getParameter(PAGE))){
					searchRoleBean.setCurrentPage(searchRoleBean.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))){
					searchRoleBean.setCurrentPage(searchRoleBean.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))){
					searchRoleBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))){
					searchRoleBean.setCurrentPage(searchRoleBean.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))){
					if (request.getParameter(PAGE_NBR) != null && !"".equals(request.getParameter(PAGE_NBR))){
						searchRoleBean.setCurrentPage(Integer.parseInt(request.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchRoleBean.setCurrentPage(-1);
					}
				}
			}
		} catch(Exception e) {
			// something is wrong
			// I will show the first page
			logger.error(PAGINATION_ERROR,e);
			searchRoleBean.setCurrentPage(-1);		
		}
		// isDeleteAction - false
	
		List<Role> res = null;
		
		try{
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_RoleSearch())){
				res = BLRole.getInstance().getResultsForSearch(searchRoleBean, false);	
				Localization localization = null;
				for(Role role : res){
					String locale = RequestContextUtils.getLocale(request).getLanguage();
					localization = BLLocalization.getInstance().getByLocale(role.getDescription().getLocalizationId(), locale.toUpperCase());
				
					role.setSketch(ControllerUtils.getInstance().getDescriptionForLocale(localization, locale));
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
			}					
		} catch (BusinessException be) {
			logger.error("", be);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
			
			
		mav.addObject(SEARCH_RESULTS, res);
		
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchRoleBean, PAGES);
		
		//if we have all the actions we put them on mav
		mav.addObject(ALL_ACTIONS, manageActions(searchRoleBean, request));
		
		mav.addObject(SEARCH_ROLE_BEAN, searchRoleBean);
		mav.addObject(COMMAND, command);
		logger.debug("handlePagination - END");
		return mav;
	}
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		logger.debug("referenceData - START");
		Map map = new HashMap();
		
		//used as a container for error messages
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
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
	 * @param searchPersonBean
	 * @param request
	 * @return
	 */
	private boolean manageActions(SearchRoleBean searchRoleBean, HttpServletRequest request){
		logger.debug("manageActions START");
		boolean actions;
		Integer organisationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		
		//if we search for the organisation that is on the context we have all the options
		if(organisationId.intValue() == searchRoleBean.getOrganisationId()){
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
	 * Deletes a role that comes from a role form
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param searchRoleBean
	 * @param roleId
	 * @throws BusinessException
	 */
	private void handleDeleteFromRoleForm(HttpServletRequest request, SearchRoleBean searchRoleBean, Integer roleId) throws BusinessException{						
		logger.debug("handleDeleteFromRoleForm - START - ");
		
		Integer[] roles = new Integer[1];
		roles[0] = roleId;			
		searchRoleBean.setRoleId(roles);
		handleDeleteAllSimple(request, searchRoleBean);
				
		logger.debug("handleDeleteFromRoleForm - END - ");
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
