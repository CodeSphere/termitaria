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
import java.util.HashSet;
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
import ro.cs.om.business.BLPermission;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.Localization;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.Permission;
import ro.cs.om.entity.SearchPermissionBean;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.entity.PermissionWeb;
import ro.cs.om.web.security.UserAuth;

/**
 * @author alu
 *
 */
public class PermissionSearchController extends RootSimpleFormController{
	
	private static final String FORM_VIEW = "Permission_Search";
	private static final String SUCCESS_VIEW = "Permission_Listing";
	private static final String SEARCH_RESULTS = "PERMISSIONS";
	private static final String PAGINATION = "pagination";
	private static final String ACTION = "action";
	private static final String SEARCH_ERROR = "permission.search.error";
	private static final String PAGE = "page";
	private static final String NEXT = "next";
	private static final String PREV = "prev";
	private static final String FIRST = "first";
	private static final String LAST = "last";
	private static final String PAGE_NBR = "pagenbr";
	private static final String NUMBER = "nbr";
	private static final String PAGES = "permission.pagination.pages";
	private static final String SEARCH_PERMISSION_BEAN = "searchPermissionBean";
	private static final String COMMAND = "command";
	private static final String DELETE_ALL = "DELETE_ALL";	
	private static final String DELETE_ERROR = "permission.delete.error";	
	private static final String DELETE_SUCCESS = "permission.delete.success";
	private static final String PAGINATION_ERROR = "PAGINATION ERROR!!!!!!!!!!!!!!";	
	private static final String DELETE_FROM_PERMISSION_FORM = "DELETE_FROM_PERMISSION_FORM"; 
	private static final String PERMISSION_ID				= "permissionId";
	
	public PermissionSearchController(){
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
		setCommandName("searchPermissionBean");
		setCommandClass(SearchPermissionBean.class);
	}
	
	protected Object formBackingObject(HttpServletRequest request)	throws Exception {

		logger.debug("formBackingObject - START");
		
		SearchPermissionBean spb = new SearchPermissionBean();
		//we set the initial search parameters
		spb.setSortDirection(IConstant.ASCENDING);
		spb.setSortParam("name");
		
		request.setAttribute(IConstant.NOM_RESULTS_PER_PAGE, OMContext.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
		logger.debug("RESULTS PER PAGE CLASSIFIED LIST LOADED");
		
		Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		if(organisationId != null){			
			Set<Module> modules = BLOrganisation.getInstance().getModules( organisationId );
			if ( (modules != null) && (modules.size() != 0)) {
				request.setAttribute(IConstant.NOM_MODULES, modules);
				logger.debug("MODULES CLASSIFIED LIST LOADED");
			}
		}
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		Integer permissionId = ServletRequestUtils.getIntParameter(request, PERMISSION_ID);
		
		// deletes a permission if the request comes from permission form
		if(action != null && DELETE_FROM_PERMISSION_FORM.equals(action) && permissionId != null) {		
			handleDeleteFromPermissionForm(request, spb, permissionId);
		}
		
		logger.debug("formBackingObject - END");
		return spb;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit - START");
		
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		
		boolean isDeleteAction = false;			
		SearchPermissionBean spb = (SearchPermissionBean) command;
			
		//If the user didn't select at least one Module then we'll make a search on all
		//Organization's Modules
		//All Organization's Modules
		Set<Integer> modulesIds = null;
		if (spb.getModuleId() == -1) {
			modulesIds = new HashSet<Integer>();
			Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			Set<Module> modules = BLOrganisation.getInstance().getModules( organisationId );
			if ( (modules != null) && (modules.size() != 0)) {
				for ( Module module : modules ){
					modulesIds.add(module.getModuleId());
				}
			}
		}
			
		// check if i have delete all action
		if(request.getParameter(ACTION) != null && DELETE_ALL.equals(request.getParameter(ACTION))){
			mav = handleDeleteAll(request, command, infoMessages, errorMessages, modulesIds);
			isDeleteAction = true;
		} else	if (request.getParameter(ACTION) != null && PAGINATION.equals(request.getParameter(ACTION))){
			mav = handlePagination(request, command, errorMessages, modulesIds);
		} else {
			mav = handleSearch(request, command, isDeleteAction, errorMessages, modulesIds);
		}			
		
		//setting all messages on response
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		logger.debug("onSubmit - END");
		return mav;
	}

	/**
	 * Deletes all the permissions or a specific permission
	 * @author Adelina
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private ModelAndView handleDeleteAll(HttpServletRequest request, Object command, ArrayList<String> infoMessages, ArrayList<String> errorMessages, Set<Integer> modulesIds) throws BusinessException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		logger.debug("handleDeleteAll - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());			
		SearchPermissionBean searchPermissionBean = (SearchPermissionBean) command;
						
		handleDeleteAllSimple(request, searchPermissionBean);

		logger.debug("Results per page " + searchPermissionBean.getResultsPerPage());
	
		List<PermissionWeb> permissions = null;
		
		try{
		
			permissions = BLPermission.getInstance().getResultsForSearch(searchPermissionBean, true, modulesIds);
			Localization localization = null;
		
			for(PermissionWeb permission : permissions){
				String locale = RequestContextUtils.getLocale(request).getLanguage();
				localization = BLLocalization.getInstance().getByLocale(permission.getDescription().getLocalizationId(), locale.toUpperCase());
			
				permission.setSketch(ControllerUtils.getInstance().getDescriptionForLocale(localization, locale));
			}
		
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		mav.addObject(SEARCH_RESULTS, permissions);
	
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchPermissionBean, PAGES);
		mav.addObject(SEARCH_PERMISSION_BEAN, searchPermissionBean);
		
		logger.debug("handleDeleteAll - END");
		return mav;

	}
	
	/**
	 * Deletes permissions
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param searchPermissionBean
	 * @throws BusinessException
	 */
	private void handleDeleteAllSimple(HttpServletRequest request, SearchPermissionBean searchPermissionBean) throws BusinessException {
		
		logger.debug("handleDeleteAllSimple - START -");
		
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		logger.debug(searchPermissionBean);
		logger.debug("start deleting " + searchPermissionBean.getPermissionId().length + " permission.");
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
		
		// ****************** Security *******************************
		if(userAuth.hasAuthority(PermissionConstant.getInstance().get_Super())){
		
			for(int i = 0; i < searchPermissionBean.getPermissionId().length; i++){
				logger.debug("Delete permission : " + searchPermissionBean.getPermissionId()[i]);
				Permission permission = null;
				boolean isDeleted = true;
				try{
					permission = BLPermission.getInstance().deleteAll(searchPermissionBean.getPermissionId()[i]); 
					
					//add the new audit event
					try {
						if (!userAuth.isAdminIT()){
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PERMISSION_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
								, messageSource.getMessage(IConstant.AUDIT_EVENT_PERMISSION_DELETE_MESSAGE, new Object[] {permission.getName()}, new Locale("en"))
								, messageSource.getMessage(IConstant.AUDIT_EVENT_PERMISSION_DELETE_MESSAGE, new Object[] {permission.getName()}, new Locale("ro"))
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
					String permissionNameMessage = ControllerUtils.getInstance().tokenizeField(permission.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
					infoMessages.add(messageSource.getMessage(DELETE_SUCCESS, new Object[] {permissionNameMessage}, RequestContextUtils.getLocale(request)));
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
	private ModelAndView handlePagination(HttpServletRequest request, Object command, ArrayList<String> errorMessages, Set<Integer> modulesIds) throws BusinessException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		logger.debug("handlePagination - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchPermissionBean searchPermissionBean = (SearchPermissionBean) command;
		
		try {
			if (request.getParameter(PAGE) != null){
				if (NEXT.equals(request.getParameter(PAGE))){
					searchPermissionBean.setCurrentPage(searchPermissionBean.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))){
					searchPermissionBean.setCurrentPage(searchPermissionBean.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))){
					searchPermissionBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))){
					searchPermissionBean.setCurrentPage(searchPermissionBean.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))){
					if (request.getParameter(PAGE_NBR) != null && !"".equals(request.getParameter(PAGE_NBR))){
						searchPermissionBean.setCurrentPage(Integer.parseInt(request.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchPermissionBean.setCurrentPage(-1);
					}
				}
			}
		} catch(Exception e) {
			// something is wrong
			// I will show the first page
			logger.error(PAGINATION_ERROR,e);
			searchPermissionBean.setCurrentPage(-1);		
		}
		
		List<PermissionWeb> res = null;
		
		try{
			res = BLPermission.getInstance().getResultsForSearch(searchPermissionBean, false, modulesIds);
			Localization localization = null;
		
			for(PermissionWeb permission : res){
				String locale = RequestContextUtils.getLocale(request).getLanguage();
				if (permission.getDescription() != null){
					localization = BLLocalization.getInstance().getByLocale(permission.getDescription().getLocalizationId(), locale.toUpperCase());	
					permission.setSketch(ControllerUtils.getInstance().getDescriptionForLocale(localization, locale));
				}

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
		ControllerUtils.getInstance().findPagesLimit(searchPermissionBean, PAGES);
		
		mav.addObject(SEARCH_PERMISSION_BEAN, searchPermissionBean);
		mav.addObject(COMMAND, command);
		logger.debug("handlePagination - END");
		return mav;
	}
	
	/**
	 * Handles the search for permission form submission
	 *
	 * @author alu
	 * 
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
	private ModelAndView handleSearch(HttpServletRequest request, Object command, boolean isDeleteAction, ArrayList<String> errorMessages, Set<Integer> modulesIds) throws BusinessException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		logger.debug("handleSearch - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());		
		SearchPermissionBean searchPermissionBean = (SearchPermissionBean) command;
		
		List<PermissionWeb> res = null;
		
		try{
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_PermissionView())){
				res = BLPermission.getInstance().getResultsForSearch(searchPermissionBean, isDeleteAction, modulesIds);
				Localization localization = null;
			
				for(PermissionWeb permission : res){
					String locale = RequestContextUtils.getLocale(request).getLanguage();
					localization = BLLocalization.getInstance().getByLocale(permission.getDescription().getLocalizationId(), locale.toUpperCase());
			
					permission.setSketch(ControllerUtils.getInstance().getDescriptionForLocale(localization, locale));
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
		ControllerUtils.getInstance().findPagesLimit(searchPermissionBean, PAGES);
		
		mav.addObject(SEARCH_PERMISSION_BEAN, searchPermissionBean);
		mav.addObject(COMMAND, command);
						
		return mav;
	}		
		
	
	/**
	 * Deletets a permission that comes from a permission form
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param searchPermissionBean
	 * @param permissionId
	 * @throws BusinessException
	 */
	private void handleDeleteFromPermissionForm(HttpServletRequest request, SearchPermissionBean searchPermissionBean, Integer permissionId) throws BusinessException{						
		logger.debug("handleDeleteFromPermissionForm - START - ");
		
		Integer[] permissions = new Integer[1];
		permissions[0] = permissionId;			
		searchPermissionBean.setPermissionId(permissions);
		handleDeleteAllSimple(request, searchPermissionBean);
				
		logger.debug("handleDeleteFromPermissionForm - END - ");
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
