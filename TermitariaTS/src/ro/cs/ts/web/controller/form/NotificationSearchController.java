/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *    Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *     
 *    Termitaria is free software; you can redistribute it and/or 
 *    modify it under the terms of the GNU Affero General Public License 
 *    as published by the Free Software Foundation; either version 3 of 
 *    the License, or (at your option) any later version.
 *    
 *    This program is distributed in the hope that it will be useful, 
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *    GNU Affero General Public License for more details.
 *    
 *    You should have received a copy of the GNU Affero General Public License 
 *    along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.ts.web.controller.form;

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

import ro.cs.ts.business.BLNotification;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.Notification;
import ro.cs.ts.entity.SearchNotificationBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;

/**
 * @author alu
 * 5 Jan 2010
 */
public class NotificationSearchController extends RootSimpleFormController{
	
	private static final String FORM_VIEW 					= "Notification_Search";
	private static final String SUCCESS_VIEW 				= "Notification_Listing";
	private static final String SEARCH_RESULTS 				= "NOTIFICATIONS";
	private static final String NOTIFICATION_SEARCH_BEAN 	= "notificationSearchBean";

	private static final String ROOT_KEY 					= "notification.";
	private static final String PAGES 						= ROOT_KEY.concat("pagination.pages");
	private static final String SEARCH_ERROR 				= ROOT_KEY.concat("search.error");
	private static final String GENERAL_ERROR   			= ROOT_KEY.concat("general.error");
	private static final String DELETE_ERROR 				= ROOT_KEY.concat("delete.error");
	private static final String DELETE_ALL_ERROR			= ROOT_KEY.concat("delete.all.error");
	private static final String DELETE_MESSAGE 				= ROOT_KEY.concat("delete.success");
	private static final String DELETE_ALL_MESSAGE 			= ROOT_KEY.concat("delete.all.success");
	private static final String REFERENCE_DATA_ERROR		= ROOT_KEY.concat("reference.error");
	
	private static final String PAGINATION_ERROR 			= "pagination.error";
	
	private static final String PAGINATION 					= "pagination";
	private static final String ACTION 						= "action";
	private static final String PAGE 						= "page";
	private static final String NEXT 						= "next";
	private static final String PREV 						= "prev";
	private static final String FIRST 						= "first";
	private static final String LAST 						= "last";
	private static final String PAGE_NBR 					= "pagenbr";
	private static final String NUMBER 						= "nbr";	
	private static final String RESULTS_PER_PAGE			= "RESULTS_PER_PAGE";
	private static final String DELETE						= "DELETE";
	private static final String DELETE_ALL					= "DELETE_ALL";

	//--------------------------------------PARAMETERS-----------------------------------
	private static final String ISSUED_DATE					= "issuedDate";
	private static final String TODAY_DATE					= "TODAY_DATE";
	
	
	public NotificationSearchController(){
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
		setCommandName(NOTIFICATION_SEARCH_BEAN);
		setCommandClass(SearchNotificationBean.class);
	}
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		logger.debug("initBinder - START");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(sdf, true));
		
        logger.debug("initBinder - END");
	}
	
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		logger.debug("formBackingObject - START");
		
		ArrayList<String> errorMessages = new ArrayList<String>();
		SearchNotificationBean snb = new SearchNotificationBean();
		
		try{
			
			//we set the initial search parameters
			snb.setSortDirection(IConstant.DESCENDING);
			snb.setSortParam(ISSUED_DATE);
			
			setErrors(request, errorMessages);
		} catch(Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		logger.debug("formBackingObject - END");
		return snb;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit - START");
		
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		try {
			// check if i have delete
			if(request.getParameter(ACTION) != null && DELETE_ALL.equals(request.getParameter(ACTION))){
				mav = handleDeleteAll(request, command, infoMessages, errorMessages);
			} else if(request.getParameter(ACTION) != null && DELETE.equals(request.getParameter(ACTION))){
				mav = handleDelete(request, command, infoMessages, errorMessages);
			} else if (request.getParameter(ACTION) != null && PAGINATION.equals(request.getParameter(ACTION))){
				mav = handlePagination(request, command, errorMessages);
			} else {
				mav = handleSearch(request, command, errorMessages);	
			}	
			
			
		} catch(Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		//Publish Info/Error messages
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("onSubmit - END");		
		return mav;
	}
	
	/**
	 * @author alu
	 */
	protected Map referenceData(HttpServletRequest request) throws Exception {
		logger.debug("referenceData - START");
		
		ArrayList<String> errorMessages = new ArrayList<String>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		try{
			//results per page
			map.put(RESULTS_PER_PAGE, TSContext.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
			//put the today's date
			map.put(TODAY_DATE, ControllerUtils.getInstance().getLocaleDate(new GregorianCalendar()));
			
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(REFERENCE_DATA_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}		
		setErrors(request, errorMessages);
		
		logger.debug("referenceData - END");
		return map;
	}
	
	private ModelAndView handleSearch(HttpServletRequest request, Object command, ArrayList<String> errorMessages) {
		logger.debug("handleSearch - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchNotificationBean searchNotificationBean = (SearchNotificationBean) command;
		List<Notification> res = null;
		
		try {
			res = BLNotification.getInstance().getNotificationsFromSearch(searchNotificationBean, false);
			
			// find the number of pages shown in pagination area
			ControllerUtils.getInstance().findPagesLimit(searchNotificationBean, PAGES);
			
		} catch(BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		mav.addObject(SEARCH_RESULTS, res);
		// add the search bean on command
		mav.addObject(NOTIFICATION_SEARCH_BEAN, searchNotificationBean);
				
		logger.debug("handleSearch - END ");
		return mav;
	}

	private ModelAndView handlePagination(HttpServletRequest request, Object command, ArrayList<String> errorMessages) {
		logger.debug("handlePagination - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchNotificationBean searchNotificationBean = (SearchNotificationBean) command;
		
		try {
			if (request.getParameter(PAGE) != null){
				if (NEXT.equals(request.getParameter(PAGE))){
					searchNotificationBean.setCurrentPage(searchNotificationBean.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))){
					searchNotificationBean.setCurrentPage(searchNotificationBean.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))){
					searchNotificationBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))){
					searchNotificationBean.setCurrentPage(searchNotificationBean.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))){
					if (request.getParameter(PAGE_NBR) != null && !"".equals(request.getParameter(PAGE_NBR))){
						searchNotificationBean.setCurrentPage(Integer.parseInt(request.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchNotificationBean.setCurrentPage(-1);
					}
				}
			}
		} catch(Exception e) {
			// something is wrong
			// I will show the first page
			logger.error(PAGINATION_ERROR,e);
			searchNotificationBean.setCurrentPage(-1);		
		}
	
		List<Notification> res = null;
		
		try {
			res = BLNotification.getInstance().getNotificationsFromSearch(searchNotificationBean, false);
		} catch(BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}			
			
		mav.addObject(SEARCH_RESULTS, res);
		
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchNotificationBean, PAGES);
		
		// add the command object on mav
		mav.addObject(NOTIFICATION_SEARCH_BEAN, searchNotificationBean);
			
		logger.debug("handlePagination - END");
		return mav;
	}

	private ModelAndView handleDelete(HttpServletRequest request, Object command, ArrayList<String> infoMessages, ArrayList<String> errorMessages) {
		logger.debug("handleDelete - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchNotificationBean searchNotificationBean = (SearchNotificationBean) command;
		
		List<Integer> notificationIds = new ArrayList<Integer>();
		for(int i = 0; i < searchNotificationBean.getNotificationId().length; i++){
			logger.debug("Delete notification : " + searchNotificationBean.getNotificationId()[i]);
			notificationIds.add(searchNotificationBean.getNotificationId()[i]);
		}
			
		try {
			// delete all notifications
			BLNotification.getInstance().delete(notificationIds);
			infoMessages.add(messageSource.getMessage(DELETE_MESSAGE, null, RequestContextUtils.getLocale(request)));
		} catch(BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(DELETE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}						
		
		// after the deletion, make the search
		List<Notification> notifications = null;
		try {
			notifications = BLNotification.getInstance().getNotificationsFromSearch(searchNotificationBean, true);
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchNotificationBean, PAGES);
		mav.addObject(SEARCH_RESULTS, notifications);
		mav.addObject(NOTIFICATION_SEARCH_BEAN, searchNotificationBean);
		
		logger.debug("handleDelete - END");
		return mav;

	}
	
	private ModelAndView handleDeleteAll(HttpServletRequest request, Object command, ArrayList<String> infoMessages, ArrayList<String> errorMessages){
		logger.debug("handleDeleteAll - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchNotificationBean searchNotificationBean = (SearchNotificationBean) command;
		UserAuth userAuth = (UserAuth)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			BLNotification.getInstance().deleteAll(userAuth.getPersonId());
			infoMessages.add(messageSource.getMessage(DELETE_ALL_MESSAGE, null, RequestContextUtils.getLocale(request)));
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(DELETE_ALL_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		mav.addObject(SEARCH_RESULTS, null);
		mav.addObject(NOTIFICATION_SEARCH_BEAN, searchNotificationBean);
		
		logger.debug("handleDeleteAll - END");
		return mav;
	}

}



































