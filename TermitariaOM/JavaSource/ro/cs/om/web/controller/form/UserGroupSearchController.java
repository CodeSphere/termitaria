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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLUserGroup;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.SearchUserGroupBean;
import ro.cs.om.entity.UserGroup;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;

/*
 * @author coni
 */
public class UserGroupSearchController extends RootSimpleFormController{
	
	public static final String FORM_VIEW						= "UserGroup_Search";
	public static final String SUCCESS_VIEW						= "UserGroup_Listing";
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String SEARCH_BRANCH_ERROR 			= "organisation.search.branch.error";
	private static final String SEARCH_BRANCH_EXCEPTION_ERROR 	= "organisation.search.branch.exception.error";
	private static final String SEARCH_ERROR					= "usergroup.search.error";
	private static final String DELETE_ERROR 					= "usergroup.delete.error";
	private static final String DELETE_SUCCESS 					= "usergroup.delete.success";	
	private static final String PAGINATION_ERROR 				= "PAGINATION ERROR!!!!!!!!!!!!!!";
	private static final String ORG_GET_ERROR					= "organisation.get.error";
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private static final String ACTION 							= "action";
	private static final String DELETE_ALL 						= "DELETE_ALL";
	private static final String PAGINATION 						= "pagination";
	private static final String PAGE					 		= "page";
	private static final String NEXT 							= "next";
	private static final String PREV 							= "prev";
	private static final String FIRST 							= "first";
	private static final String LAST 							= "last";
	private static final String PAGE_NBR 						= "pagenbr";
	private static final String NUMBER 							= "nbr";	

	//------------------------OTHER PARAMETERS---------------------------------------------------------
	private static final String BRANCH_DISPLAY					= "BRANCH_DISPLAY";
	private static final String SEARCH_RESULTS					= "USER_GROUPS"; 
	private static final String PAGES						    = "usergroup.pagination.pages";
	private static final String SEARCH_USER_GROUP_BEAN			= "searchUserGroupBean";
	private static final String DELETE_FROM_USER_GROUP_FORM		= "DELETE_FROM_USER_GROUP_FORM";
	private static final String USER_GROUP_ID					= "userGroupId";
	
	// Number of characters that fit in the panel display header
    // if there are big words
    public static final Integer NR_CHARS_PANEL_HEADER			= 30;
	
	
	public UserGroupSearchController() {
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
		setCommandName("searchUserGroupBean");
		setCommandClass(SearchUserGroupBean.class);
	}
	
	protected SearchUserGroupBean formBackingObject(HttpServletRequest request) throws Exception{
		logger.debug("formBackingObject - START");
		
		//set the branch as the organization stored on session
		SearchUserGroupBean searchUserGroupBean = new SearchUserGroupBean();
		searchUserGroupBean.setBranch(String.valueOf(request.getSession().getAttribute(IConstant.SESS_ORGANISATION_NAME)));
		searchUserGroupBean.setOrganisationId(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
		logger.debug("Finished setting the default branch as being the organization stored on the session");
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		Integer userGroupId = ServletRequestUtils.getIntParameter(request, USER_GROUP_ID);
		
		// deletes a usergroup if the request comes from usergroup form
		if(action != null && DELETE_FROM_USER_GROUP_FORM.equals(action) && userGroupId != null) {
			handleDeleteFromUserGroupForm(request, searchUserGroupBean, userGroupId);
		}
				
		
		logger.debug("formBackingObject - END");
		return searchUserGroupBean;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,	HttpServletResponse response, Object command, BindException errors) throws Exception {
		logger.debug("onSubmit - START");
		
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		
		// check if i have delete all action
		if(request.getParameter(ACTION) != null && DELETE_ALL.equals(request.getParameter(ACTION))){
			mav = handleDeleteAll(request, command, infoMessages, errorMessages);
		} else if (request.getParameter(ACTION) != null && PAGINATION.equals(request.getParameter(ACTION))){
			mav = handlePagination(request, command, errorMessages);
		} else {
			mav = handleSearch(request, command, false, errorMessages);
		}	
		
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);

		logger.debug("onSubmit - END");
		return mav;		
	}
	
	/**
	 * Deletes all the roles or a specific role
	 * @author Coni
	 * @param request
	 * @param command
	 * @param infoMessages
	 * @param errorMessages
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
		SearchUserGroupBean searchUserGroupBean = (SearchUserGroupBean) command;

		handleDeleteAllSimple(request, searchUserGroupBean);
		
		logger.debug("Results per page " + searchUserGroupBean.getResultsPerPage());
		logger.debug("handleDeleteAll - getting the search results");
		List<UserGroup> userGroups = null;
		try{
			userGroups = BLUserGroup.getInstance().getResultsForSearch(searchUserGroupBean, true);
			for(UserGroup userGroup : userGroups) {
				String panelHeaderName = ControllerUtils.getInstance().truncateName(userGroup.getName(), NR_CHARS_PANEL_HEADER);
				userGroup.setPanelHeaderName(panelHeaderName);
			}	
		} catch (BusinessException bexc){
			logger.error("Error while retrieving search results", bexc);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		//adding the search results to the mav
		mav.addObject(SEARCH_RESULTS, userGroups);
		
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchUserGroupBean, PAGES);						
		
		//adding the search user group bean to the mav
		mav.addObject(SEARCH_USER_GROUP_BEAN, searchUserGroupBean);
		
		logger.debug("handleDeleteAll - END");
		return mav;
	}
	
	/**
	 * Deletes userGroups
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param searchUserGroupBean
	 * @throws BusinessException
	 */
	private void handleDeleteAllSimple(HttpServletRequest request, SearchUserGroupBean searchUserGroupBean) throws BusinessException {
		logger.debug("handleDeleteAllSimple - START -");
		
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserGroup userGroup = null;
		
		// ****************** Security *******************************
		if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_DeleteUserGroup())) {
			for(int i = 0; i < searchUserGroupBean.getUserGroupId().length; i++){
				logger.debug("Start deleting user group with id: ".concat((searchUserGroupBean.getUserGroupId()[i]).toString()));
				boolean isDeleted = true;
				try {
					//delete the user group  
					userGroup = BLUserGroup.getInstance().deleteAll(searchUserGroupBean.getUserGroupId()[i]);
					
					//add the new audit event
					try {
						if (!userAuth.isAdminIT()){
							Organisation org = BLOrganisation.getInstance().get(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_USERGROUP_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
								, messageSource.getMessage(IConstant.AUDIT_EVENT_USERGROUP_DELETE_MESSAGE, new Object[] {userGroup.getName(), org.getName()}, new Locale("en"))
								, messageSource.getMessage(IConstant.AUDIT_EVENT_USERGROUP_DELETE_MESSAGE, new Object[] {userGroup.getName(), org.getName()}, new Locale("ro"))
								, ControllerUtils.getInstance().getOrganisationIdFromSession(request) , userAuth.getPersonId());
						}
					} catch (BusinessException exc){
						logger.error(exc);
					}
				} catch (BusinessException bexc){
					logger.error("Error while deleting the group of users", bexc);
					errorMessages.add(messageSource.getMessage(DELETE_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
					isDeleted = false;
				}
				if (isDeleted){		
					String userGroupNameMessage = ControllerUtils.getInstance().tokenizeField(userGroup.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
					infoMessages.add(messageSource.getMessage(DELETE_SUCCESS, new Object[] {userGroupNameMessage}, RequestContextUtils.getLocale(request)));
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
		
	
	/*
	 * Handles the search case
	 * @author coni
	 * @param request
	 * @param command
	 * @param isDeleteAction
	 * @param errorMessages
	 * @throws BusinessException
	 * @throws ClassNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	private ModelAndView handleSearch(HttpServletRequest request, Object command, boolean isDeleteAction, ArrayList<String> errorMessages)  throws BusinessException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException{
		logger.debug("handleSearch - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());		
		SearchUserGroupBean searchUserGroupBean = (SearchUserGroupBean) command;
		
		logger.debug("handleSearch - getting the search results");
		List<UserGroup> userGroups = null;
		try{
			userGroups = BLUserGroup.getInstance().getResultsForSearch(searchUserGroupBean, true);
			for(UserGroup userGroup : userGroups) {
				String panelHeaderName = ControllerUtils.getInstance().truncateName(userGroup.getName(), NR_CHARS_PANEL_HEADER);
				userGroup.setPanelHeaderName(panelHeaderName);
			}	
		} catch (BusinessException bexc){
			logger.error("Error while retrieving search results", bexc);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}

		//adding the search results to the mav
		mav.addObject(SEARCH_RESULTS, userGroups);
		
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchUserGroupBean, PAGES);						
		
		//adding the search user group bean to the mav
		mav.addObject(SEARCH_USER_GROUP_BEAN, searchUserGroupBean);
		
		logger.debug("UserGroupSearchController - handleSearch - END");
		return mav;
	}
	
	/**
	 * Handles the results pagination
	 *
	 * @author coni
	 * 
	 * @param request
	 * @param command
	 * @param errorMessages
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
		SearchUserGroupBean searchUserGroupBean = (SearchUserGroupBean) command;
		
		try {
			if (request.getParameter(PAGE) != null){
				if (NEXT.equals(request.getParameter(PAGE))){
					searchUserGroupBean.setCurrentPage(searchUserGroupBean.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))){
					searchUserGroupBean.setCurrentPage(searchUserGroupBean.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))){
					searchUserGroupBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))){
					searchUserGroupBean.setCurrentPage(searchUserGroupBean.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))){
					if (request.getParameter(PAGE_NBR) != null && !"".equals(request.getParameter(PAGE_NBR))){
						searchUserGroupBean.setCurrentPage(Integer.parseInt(request.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchUserGroupBean.setCurrentPage(-1);
					}
				}
			}
		} catch(Exception e) {
			// something is wrong
			// I will show the first page
			logger.error(PAGINATION_ERROR,e);
			searchUserGroupBean.setCurrentPage(-1);		
		}

		logger.debug("handlePagination - getting the search results");
		List<UserGroup> userGroups = null;
		try{
			userGroups = BLUserGroup.getInstance().getResultsForSearch(searchUserGroupBean, true);
			for(UserGroup userGroup : userGroups) {
				String panelHeaderName = ControllerUtils.getInstance().truncateName(userGroup.getName(), NR_CHARS_PANEL_HEADER);
				userGroup.setPanelHeaderName(panelHeaderName);
			}	
		} catch (BusinessException bexc){
			logger.error("Error while retrieving search results", bexc);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}

		//adding the search results to the mav
		mav.addObject(SEARCH_RESULTS, userGroups);
		
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchUserGroupBean, PAGES);						
		
		//adding the search user group bean to the mav
		mav.addObject(SEARCH_USER_GROUP_BEAN, searchUserGroupBean);
		
		logger.debug("handleSearch - END - size");
		return mav;
		
	}

	
	/**
	 * See if the current organization has branches.
	 * If it has the we display the branch select
	 *
	 * @author mitziuro & coni
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	private boolean manageBranches(HttpServletRequest request, ArrayList<String> errorMessages) throws BusinessException{
		logger.debug("manageBranches START");
		boolean display = false;
		
		Integer organisationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		try {
			Organisation organisation = BLOrganisation.getInstance().get(organisationId);
			//If the organization is company there are no branches
			if(organisation.getType() == IConstant.NOM_ORGANISATION_TYPE_COMPANY) {
				display = false;
			} else {
				List <Organisation> branchList = BLOrganisation.getInstance().getAllOrganisationsForParentId(organisationId);
				if(branchList.size() > 0){
					display = true;
				}
			}
		} catch (BusinessException bexc) {
			logger.error("Error at getting the organization with id:".concat(organisationId.toString()), bexc);
			errorMessages.add(messageSource.getMessage(ORG_GET_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		logger.debug("manageBranches END display:".concat(String.valueOf(display)));
		return display;
		
	}
	
	protected Map referenceData(HttpServletRequest request) throws Exception{
		logger.debug("referenceData - START");
		Map map = new HashMap();
		//load the list of possible results per page
		map.put(IConstant.NOM_RESULTS_PER_PAGE, OMContext.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
		logger.debug("RESULTS PER PAGE CLASSIFIED LIST LOADED");
		
		//used as a container for error messages
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		try {
			//test if we have branches for displaying the branch select
			map.put(BRANCH_DISPLAY, manageBranches(request, errorMessages));
		} catch(BusinessException be) {
			logger.error(be.getMessage(), be);			
			errorMessages.add(messageSource.getMessage(SEARCH_BRANCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(SEARCH_BRANCH_EXCEPTION_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		//setting the error messages, if any
		setErrors(request, errorMessages);
		logger.debug("referenceData - END");
		return map;
	}
	
	/**
	 * Deletes a userGroup that comes from a userGroup form
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param searchUserGroupBean
	 * @param userGroupId
	 * @throws BusinessException
	 */
	private void handleDeleteFromUserGroupForm(HttpServletRequest request, SearchUserGroupBean searchUserGroupBean, Integer userGroupId) throws BusinessException{						
		logger.debug("handleDeleteFromUserGroupForm - START - ");
		
		Integer[] userGroups = new Integer[1];
		userGroups[0] = userGroupId;			
		searchUserGroupBean.setUserGroupId(userGroups);
		handleDeleteAllSimple(request, searchUserGroupBean);
				
		logger.debug("handleDeleteFromUserGroupForm - END - ");
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
