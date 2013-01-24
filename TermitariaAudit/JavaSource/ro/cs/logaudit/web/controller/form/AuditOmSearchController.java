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
package ro.cs.logaudit.web.controller.form;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.logaudit.business.BLAuditOm;
import ro.cs.logaudit.business.BLOrganisation;
import ro.cs.logaudit.common.IConstant;
import ro.cs.logaudit.common.PermissionConstant;
import ro.cs.logaudit.context.AuditContext;
import ro.cs.logaudit.entity.AuditOmBean;
import ro.cs.logaudit.entity.SearchAuditOmBean;
import ro.cs.logaudit.exception.BusinessException;
import ro.cs.logaudit.web.controller.root.ControllerUtils;
import ro.cs.logaudit.web.controller.root.RootSimpleFormController;
import ro.cs.logaudit.web.security.UserAuth;

/**
 * @author alu
 * @author Adelina
 * @author Coni
 */
public class AuditOmSearchController extends RootSimpleFormController{
	
	private static final String FORM_VIEW = "AuditOmSearch";
	private static final String SUCCESS_VIEW = "ListAuditOmResults";
	private static final String SEARCH_ERROR = "audit.om.search.error";
	private static final String SEARCH_EXCEPTION_ERROR = "audit.om.search.exception.error";
	private static final String DELETE_ERROR = "audit.om.delete.error";
	private static final String DELETE_SUCCESS = "audit.om.delete.success";
	private static final String GENERAL_ERROR= "audit.om.general.search.error";
	private static final String GET_ALL_ORGANISATIONS_ERROR = "organisations.get.all.error";
	private static final String SEARCH_RESULTS = "SEARCH_RESULTS";
	private static final String PAGINATION = "pagination";
	private static final String ACTION = "action";	
	private static final String SEARCH_AUDIT_OM_BEAN = "searchAuditOmBean";
	private static final String PAGE = "page";
	private static final String NEXT = "next";
	private static final String PREV = "prev";
	private static final String FIRST = "first";
	private static final String LAST = "last";
	private static final String PAGE_NBR = "pagenbr";
	private static final String NUMBER = "nbr";
	private static final String PAGES = "pagination.pages";
	private static final String IS_ADMIN_IT = "IS_ADMIN_IT";
	private static final String ORGANIZATIONS_ALL = "ORGANIZATIONS_ALL";
	private static final String COMMAND = "command";
	private static final String PAGINATION_ERROR = "PAGINATION ERROR!!!!!!!!!!!!!!";
	private static final String MODULE_ID = "moduleId";
	private static final String DELETE_ALL = "DELETE_ALL";
	private static final String JSON_PERSONS = "JSON_PERSONS";
	private static final String LOCALE = "LOCALE";
	private static final String TODAY_DATE = "TODAY_DATE";

	public AuditOmSearchController(){
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
		setCommandName(SEARCH_AUDIT_OM_BEAN);
		setCommandClass(SearchAuditOmBean.class);
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		logger.debug("formBackingObject - START");
		SearchAuditOmBean searchAuditOmBean = new SearchAuditOmBean();
		
		logger.debug("formBackingObject - END");
		return searchAuditOmBean;
	}
	
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		logger.debug("initBinder - START");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(
				sdf, true));

		logger.debug("initBinder - END");
	}
			
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit - START");
		
		//used as a container for info/error messages
		
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		
		try {
			boolean isDeleteAction = false;
			// check if i have deleteAll action
			if (request.getParameter(ACTION) != null
					&& DELETE_ALL.equals(request.getParameter(ACTION))) {
				handleDeleteAll(request, command, infoMessages, errorMessages);
				isDeleteAction = true;
			}
			if (request.getParameter(ACTION) != null && PAGINATION.equals(request.getParameter(ACTION))){
				mav = handlePagination(request, errorMessages, command);
			} else {
				mav = handleSearch(request, command, errorMessages, isDeleteAction);
			}
		} catch(BusinessException be) {
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(SEARCH_EXCEPTION_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}


		//setting all messages on response
		
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);

		//put the locale
		mav.addObject(LOCALE, RequestContextUtils.getLocale(request).toString());
		
		logger.debug("onSubmit - END");
		return mav;
	}
	
	/**
	 * 
	 * Handles the search for audit form submission
	 *
	 * @author Adelina
	 * 
	 * @param request
	 * @param command
	 * @param errorMessages
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handleSearch(HttpServletRequest request, Object command, ArrayList<String> errorMessages, boolean isDeleteAction) throws BusinessException{
		logger.debug("handleSearch - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		SearchAuditOmBean searchAuditOmBean = (SearchAuditOmBean) command;
		
		List<AuditOmBean> res = null;
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if ((searchAuditOmBean.getOrganisationId() == -1) && !userAuth.isAdminIT()){
			logger.debug("organisationId == -1");
			// if the user is not adminIT, set his organisationId
			searchAuditOmBean.setOrganisationId((ControllerUtils.getInstance()
					.getOrganisationIdFromSession(request)));
		}
		
		try{
			res = BLAuditOm.getInstance().getResultsForSearch(searchAuditOmBean, isDeleteAction, RequestContextUtils.getLocale(request));
			mav.addObject(SEARCH_RESULTS, res);
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR,
					new Object[] {
							be.getCode(),
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() },
					RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR,
					new Object[] { ControllerUtils.getInstance()
							.getFormattedCurrentTime() }, RequestContextUtils
							.getLocale(request)));
		}
		
		
		
		// find the number of pages shown in pagination area		
		ControllerUtils.getInstance().findPagesLimit(searchAuditOmBean, PAGES);		
		
		mav.addObject(SEARCH_AUDIT_OM_BEAN, searchAuditOmBean);
		mav.addObject(COMMAND, command);
		
		logger.debug("handleSearch - END - res.size=".concat(String.valueOf(res.size())));
		
		return mav;
	}
		
	
	/**
	 * Handles the results pagination
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param command
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handlePagination(HttpServletRequest request, ArrayList<String> errorMessages,
			Object command) throws BusinessException {
		logger.debug("handlePagination - START");

		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchAuditOmBean searchAuditOmBean = (SearchAuditOmBean) command;
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if ((searchAuditOmBean.getOrganisationId() == -1) && !userAuth.isAdminIT()){
			// if the user is not adminIT, set his organisationId
			searchAuditOmBean.setOrganisationId((ControllerUtils.getInstance()
					.getOrganisationIdFromSession(request)));
		}

		try {
			if (request.getParameter(PAGE) != null) {
				if (NEXT.equals(request.getParameter(PAGE))) {
					searchAuditOmBean
							.setCurrentPage(searchAuditOmBean.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))) {
					searchAuditOmBean
							.setCurrentPage(searchAuditOmBean.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))) {
					searchAuditOmBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))) {
					searchAuditOmBean.setCurrentPage(searchAuditOmBean.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))) {
					if (request.getParameter(PAGE_NBR) != null
							&& !"".equals(request.getParameter(PAGE_NBR))) {
						searchAuditOmBean.setCurrentPage(Integer.parseInt(request
								.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchAuditOmBean.setCurrentPage(-1);
					}
				}
			}
		} catch (Exception e) {
			// something is wrong
			// I will show the first page
			logger.error(PAGINATION_ERROR, e);
			searchAuditOmBean.setCurrentPage(-1);
		}	

		List<AuditOmBean> res = null; 
		try {
									
			res = BLAuditOm.getInstance().getResultsForSearch(searchAuditOmBean, false, RequestContextUtils.getLocale(request));
			mav.addObject(SEARCH_RESULTS, res);
						
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR,
					new Object[] {
							be.getCode(),
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() },
					RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR,
					new Object[] { ControllerUtils.getInstance()
							.getFormattedCurrentTime() }, RequestContextUtils
							.getLocale(request)));
		}
		
		
		mav.addObject(SEARCH_RESULTS, res);

		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchAuditOmBean, PAGES);

		mav.addObject(SEARCH_AUDIT_OM_BEAN, searchAuditOmBean);
		mav.addObject(COMMAND, command);

		logger.debug("handlePagination - END");
		return mav;
	}
	
	/**
	 * Deletes all the audit events or a specific audit event
	 * 
	 * @author coni
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	private void handleDeleteAll(HttpServletRequest request, Object command, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, SecurityException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		logger.debug("handleDeleteAll - START");
		
		SearchAuditOmBean searchAuditOmBean = (SearchAuditOmBean) command;

		logger.debug(searchAuditOmBean);
		logger.debug("start deleting " + searchAuditOmBean.getAuditId().length
				+ " audit om event(s).");
		
		handleDeleteAllSimple(request, searchAuditOmBean);
		
		logger.debug("Results per page " + searchAuditOmBean.getResultsPerPage());
		logger.debug("handleDeleteAll - END");
	}
	
	/**
	 * Deletes audit events
	 * 
	 * @author coni
	 * 
	 * @param request
	 * @param searchAuditOmBean
	 * @throws BusinessException
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void handleDeleteAllSimple(HttpServletRequest request, SearchAuditOmBean searchAuditOmBean) throws BusinessException, SecurityException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		logger.debug("handleDeleteAllSimple - START ");
		
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		AuditOmBean auditOmBean = null;

		if (userAuth.hasAuthority(PermissionConstant.getInstance().getAUDIT_Delete())) {
			for (int i = 0; i < searchAuditOmBean.getAuditId().length; i++) {
				logger.debug("Delete audit : " + searchAuditOmBean.getAuditId()[i]);	
				
				boolean isDeleted = true;
				
					try {
						auditOmBean = BLAuditOm.getInstance().delete(searchAuditOmBean.getAuditId()[i]);
					} catch (BusinessException be) {
						logger.error("", be);
						errorMessages.add(messageSource.getMessage(DELETE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime() },
								RequestContextUtils.getLocale(request)));
						isDeleted = false;
					}
					if (isDeleted) {
						Method method = Class.forName(AuditOmBean.class.getName()).getDeclaredMethod("getMessage" + RequestContextUtils.getLocale(request).toString().toUpperCase(), new Class[]{});
						String auditMessage = (String) method.invoke(auditOmBean, new Object[] {});
						infoMessages.add(messageSource.getMessage(DELETE_SUCCESS, new Object[] {auditMessage }, 
								RequestContextUtils.getLocale(request)));
					}
			}
		} else {
			errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
		}

		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("handleDeleteAllSimple - END ");
	}
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		ArrayList<String> errorMessages  = new ArrayList<String>();
		Map map = new HashMap();
		
		try {
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			map.put(IConstant.NOM_RESULTS_PER_PAGE, AuditContext
					.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
				
			map.put(MODULE_ID, IConstant.NOM_MODULE_OM_LABEL_KEY);
			
			map.put(IConstant.NOM_AUDIT_EVENTS_OM, AuditContext.getFromContext(IConstant.NOM_AUDIT_EVENTS_OM));
			logger.debug("CLASSIFIED LISTS LOADED");
			
			map.put(IS_ADMIN_IT, userAuth.isAdminIT());
			
			//add the current date
			map.put(TODAY_DATE, ControllerUtils.getInstance().getLocaleDate(new GregorianCalendar()));
			
			if (!userAuth.isAdminIT()){
				map.put(JSON_PERSONS, 
				ControllerUtils.getInstance().getPersonsFromOrgAsJSON((ControllerUtils.getInstance().getOrganisationIdFromSession(request)), RequestContextUtils.getLocale(request), errorMessages, messageSource));
			}
			
			try {
				if (userAuth.isAdminIT()){
					map.put(ORGANIZATIONS_ALL, BLOrganisation.getInstance().getAllOrganisations());
				}
			} catch (BusinessException be) {
				logger.error(be.getMessage(), be);			
				errorMessages.add(messageSource.getMessage(GET_ALL_ORGANISATIONS_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
			}
		} catch (Exception e) {
			logger.error("referenceData", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		//setting all messages on response
		setErrors(request, errorMessages);

		return map;
	}
}
