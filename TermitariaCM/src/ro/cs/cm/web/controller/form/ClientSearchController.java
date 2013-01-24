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
package ro.cs.cm.web.controller.form;

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
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.cm.business.BLAudit;
import ro.cs.cm.business.BLClient;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.common.PermissionConstant;
import ro.cs.cm.context.CMContext;
import ro.cs.cm.entity.Client;
import ro.cs.cm.entity.SearchClientBean;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.web.controller.root.ControllerUtils;
import ro.cs.cm.web.controller.root.RootSimpleFormController;
import ro.cs.cm.web.security.UserAuth;

/**
 * 
 * @author Coni
 *
 */
public class ClientSearchController extends RootSimpleFormController {
	
	private static final String FORM_VIEW 				= "Client_Search";
	private static final String SUCCESS_VIEW	   	 	= "Client_Listing";
	private static final String CLIENT_TYPE				= "CLIENT_TYPE";
	private static final String ACTION 					= "action";
	private static final String DELETEALL 				= "DELETE_ALL";
	private static final String DELETE_FROM_CLIENT_FORM	= "DELETE_FROM_CLIENT_FORM";
	private static final String PAGINATION				= "pagination";
	private static final String SEARCH_ERROR 			= "client.search.error";
	private static final String SEARCH_EXCEPTION_ERROR 	= "client.search.exception.error";
	private static final String GENERAL_ERROR			= "client.general.search.error";
	private static final String SEARCH_RESULTS 			= "SEARCH_RESULTS";
	private static final String PAGES 					= "pagination.pages";
	private static final String SEARCH_CLIENT_BEAN		= "searchClientBean";
	private static final String COMMAND					= "command";
	private static final String PAGE 					= "page";
	private static final String NEXT 					= "next";
	private static final String PREV 					= "prev";
	private static final String FIRST 					= "first";
	private static final String LAST 					= "last";
	private static final String PAGE_NBR 				= "pagenbr";
	private static final String NUMBER 					= "nbr";
	private static final String CLIENT_ID				= "clientId";
	private static final String PAGINATION_ERROR 		= "PAGINATION ERROR!!!!!!!!!!!!!!";
	private static final String DELETE_ERROR 			= "client.delete.error";
	private static final String DELETE_SUCCESS 			= "client.delete.success";
	private static final String CLIENT_TYPE_FIRM		= "CLIENT_TYPE_FIRM";
	private static final String CLIENT_TYPE_PERSON		= "CLIENT_TYPE_PERSON";
	private static final String CLIENT_STATUS_ACTIVE	= "CLIENT_STATUS_ACTIVE";
	
	// Number of characters that fit in the panel display header
    // if there are big words
    public static final Integer NR_CHARS_PANEL_HEADER			= 50;
    
  //--------------------BACK PARAMETERS-------------------------------------------------------------
	private static final String BACK_URL = "BACK_URL";
	
	public ClientSearchController(){
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
		setCommandName("searchClientBean");
		setCommandClass(SearchClientBean.class);
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception{
		
		logger.debug("formBackingObject - START");
		
		// used as a container for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		SearchClientBean searchClientBean = new SearchClientBean();
		
		//we set the initial search parameters
		searchClientBean.setSortDirection(IConstant.ASCENDING);
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		
		//put the back url
		String backUrl = ServletRequestUtils.getStringParameter(request, BACK_URL);
		request.setAttribute(BACK_URL, backUrl);
		
		Integer clientId = ServletRequestUtils.getIntParameter(request, CLIENT_ID);
		
		// deletes a client if the request comes from client form
		if(action != null && DELETE_FROM_CLIENT_FORM.equals(action) && clientId != null) {		
			handleDeleteFromClientForm(request, searchClientBean, clientId, infoMessages, errorMessages);
		}
		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("formBackingObject - END");
		return searchClientBean;
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		logger.debug("onSubmit - START");
		// used as a container for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();

		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		
		try {
			boolean isDeleteAction = false;
			
			//check if i have deleteAll action
			if (request.getParameter(ACTION) != null && DELETEALL.equals(request.getParameter(ACTION))) {
				handleDeleteAll(request, command, infoMessages, errorMessages);
				isDeleteAction = true;
			}
			if (request.getParameter(ACTION) != null && PAGINATION.equals(request.getParameter(ACTION))) {
				mav = handlePagination(request, errorMessages, command);
			} else {
				mav = handleSearch(request, command, errorMessages, isDeleteAction);
			}
		} catch (BusinessException bexc) {
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(SEARCH_EXCEPTION_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		//setting all messages on response
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		//add the clients types
		mav.addObject(CLIENT_TYPE_FIRM, IConstant.NOM_CLIENT_TYPE_FIRM);
		mav.addObject(CLIENT_TYPE_PERSON, IConstant.NOM_CLIENT_TYPE_PERSON);
		
		logger.debug("onSubmit - END");
		return mav;
	}
	
	private ModelAndView handleSearch(HttpServletRequest request, Object command, ArrayList<String> errorMessages, boolean isDeleteAction) throws BusinessException{
		logger.debug("handeSearch - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		SearchClientBean searchClientBean =  (SearchClientBean) command;
		
		//set the client name as sort param if no one is set
		if (searchClientBean.getSortParam() == null || "".equals(searchClientBean.getSortParam())) {
			searchClientBean.setSortParam(IConstant.NOM_CLIENT_SORT_PARAM_NAME);
		}
		
		List<Client> res = null;
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//if the organizationId is not set and the user is not adminIT, I must set the user organization id, 
		//in order to retrieve only its organization clients
		if (searchClientBean.getOrganizationId() == -1 && !userAuth.isAdminIT()) {
			// if the user is not adminIT, set his organisationId
			searchClientBean.setOrganizationId((ControllerUtils.getInstance().getOrganisationIdFromSession(request)));
		}
		
		try {
			res = BLClient.getInstance().getResultsForSearch(searchClientBean, isDeleteAction);
			//set the client info panel header name
			for(Client client : res) {
				String headerName = null;
				if (client.getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
					headerName = client.getC_name();
				} else if (client.getType() == IConstant.NOM_CLIENT_TYPE_PERSON) {
					headerName = client.getP_firstName().concat(" ").concat(client.getP_lastName());
				}
				String panelHeaderName = ControllerUtils.getInstance().truncateName(headerName, NR_CHARS_PANEL_HEADER);
				client.setPanelHeaderName(panelHeaderName);
			}
			mav.addObject(SEARCH_RESULTS, res);
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR,
					new Object[] { be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime() },
					RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR,
					new Object[] { ControllerUtils.getInstance().getFormattedCurrentTime() }, RequestContextUtils.getLocale(request)));
		}
		
		// find the number of pages shown in pagination area		
		ControllerUtils.getInstance().findPagesLimit(searchClientBean, PAGES);		
		
		mav.addObject(SEARCH_CLIENT_BEAN, searchClientBean);
		mav.addObject(COMMAND, command);
		
		logger.debug("handleSearch - END - res.size=".concat(String.valueOf(res.size())));
		
		return mav;
	}
	
	/**
	 * Handles the results pagination
	 * 
	 * @author Coni
	 * 
	 * @param request
	 * @param command
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handlePagination(HttpServletRequest request, ArrayList<String> errorMessages, Object command) throws BusinessException {
		logger.debug("handlePagination - START");

		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchClientBean searchClientBean = (SearchClientBean) command;
		
		//set the client name as sort param if no one is set
		if (searchClientBean.getSortParam() == null || "".equals(searchClientBean.getSortParam())) {
			searchClientBean.setSortParam(IConstant.NOM_CLIENT_SORT_PARAM_NAME);
		}
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//if the organizationId is not set and the user is not adminIT, I must set the user organization id, 
		//in order to retrieve only its organization clients
		if ((searchClientBean.getOrganizationId() == -1) && !userAuth.isAdminIT()){
			// if the user is not adminIT, set his organisationId
			searchClientBean.setOrganizationId((ControllerUtils.getInstance().getOrganisationIdFromSession(request)));
		}

		try {
			if (request.getParameter(PAGE) != null) {
				if (NEXT.equals(request.getParameter(PAGE))) {
					searchClientBean.setCurrentPage(searchClientBean.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))) {
					searchClientBean.setCurrentPage(searchClientBean.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))) {
					searchClientBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))) {
					searchClientBean.setCurrentPage(searchClientBean.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))) {
					if (request.getParameter(PAGE_NBR) != null && !"".equals(request.getParameter(PAGE_NBR))) {
						searchClientBean.setCurrentPage(Integer.parseInt(request.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchClientBean.setCurrentPage(-1);
					}
				}
			}
		} catch (Exception e) {
			// something is wrong
			// I will show the first page
			logger.error(PAGINATION_ERROR, e);
			searchClientBean.setCurrentPage(-1);
		}	

		List<Client> res = null; 
		try {
									
			res = BLClient.getInstance().getResultsForSearch(searchClientBean, false);
			//set the client info panel header name
			for(Client client : res) {
				String headerName = null;
				if (client.getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
					headerName = client.getC_name();
				} else if (client.getType() == IConstant.NOM_CLIENT_TYPE_PERSON) {
					headerName = client.getP_firstName().concat(" ").concat(client.getP_lastName());
				}
				String panelHeaderName = ControllerUtils.getInstance().truncateName(headerName, NR_CHARS_PANEL_HEADER);
				client.setPanelHeaderName(panelHeaderName);
			}
			mav.addObject(SEARCH_RESULTS, res);
						
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, 
					new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime() },
					RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, 
					new Object[] { ControllerUtils.getInstance().getFormattedCurrentTime() }, 
					RequestContextUtils.getLocale(request)));
		}
		
		
		mav.addObject(SEARCH_RESULTS, res);

		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchClientBean, PAGES);

		mav.addObject(SEARCH_CLIENT_BEAN, searchClientBean);
		mav.addObject(COMMAND, command);

		logger.debug("handlePagination - END");
		return mav;
	}
	
	/**
	 * Deletes all the clients or a specific client
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
		
		SearchClientBean searchClientBean = (SearchClientBean) command;

		logger.debug(searchClientBean);
		logger.debug("start deleting " + searchClientBean.getClientId().length + " client(s).");
		
		handleDeleteAllSimple(request, searchClientBean, infoMessages, errorMessages);
		
		logger.debug("Results per page " + searchClientBean.getResultsPerPage());
		logger.debug("handleDeleteAll - END");
	}
	
	/**
	 * Deletes clients
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
	private void handleDeleteAllSimple(HttpServletRequest request, SearchClientBean searchClientBean, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, SecurityException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		logger.debug("handleDeleteAllSimple - START ");
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Client client = null;

		// ****************** Security *******************************
		if (userAuth.hasAuthority(PermissionConstant.getInstance().getCM_ClientDelete())) {
			for (int i = 0; i < searchClientBean.getClientId().length; i++) {
				logger.debug("Delete client : " + searchClientBean.getClientId()[i]);	
				
				boolean isDeleted = true;
				
					try {
						client = BLClient.getInstance().delete(searchClientBean.getClientId()[i]);
					} catch (BusinessException be) {
						logger.error("", be);
						errorMessages.add(messageSource.getMessage(DELETE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime() },
								RequestContextUtils.getLocale(request)));
						isDeleted = false;
					}
					if (isDeleted) {
						String clientName = null;
						if (client.getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
							clientName = client.getC_name();
						} else {
							clientName = client.getP_firstName().concat(" ").concat(client.getP_lastName());
						}
						infoMessages.add(messageSource.getMessage(DELETE_SUCCESS, new Object[] { clientName }, RequestContextUtils.getLocale(request)));
						
						//add the new audit event only if the user is not AdminIT
						try {
							if (!userAuth.isAdminIT()){
								BLAudit.getInstance().add(IConstant.AUDIT_EVENT_CLIENT_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
										messageSource.getMessage(IConstant.AUDIT_EVENT_CLIENT_DELETE_MESSAGE, new Object[] {clientName}, new Locale("en")),
										messageSource.getMessage(IConstant.AUDIT_EVENT_CLIENT_DELETE_MESSAGE, new Object[] {clientName}, new Locale("ro")),  
										ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
							}
						} catch (Exception exc) {
							logger.error("", exc);
						}
					}
			}
		} else {
			errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
		}

		logger.debug("handleDeleteAllSimple - END ");
	}
	
	/**
	 * Deletes a client that comes from a client form
	 * 
	 * @author Coni
	 * @param request
	 * @param searchPersonBean
	 * @param personId
	 * @throws BusinessException
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	private void handleDeleteFromClientForm(HttpServletRequest request, SearchClientBean searchClientBean, Integer clientId, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, SecurityException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException{						
		logger.debug("handleDeleteFromClientForm - START - ");
		
		Integer[] clientsIds = new Integer[1];
		clientsIds[0] = clientId;			
		searchClientBean.setClientId(clientsIds);
		handleDeleteAllSimple(request, searchClientBean, infoMessages, errorMessages);
				
		logger.debug("handleDeleteFromClientForm - END - ");
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) {
		logger.debug("referenceData - START");
		Map map = new HashMap();
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		try {
			// adding to model the clients types
			map.put(CLIENT_TYPE, CMContext.getFromContext(IConstant.NOM_CLIENT_TYPE));
			
			// adding to model the results per page for search results
			map.put(IConstant.NOM_RESULTS_PER_PAGE, CMContext
					.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
			
			//adding the active client status
			map.put(CLIENT_STATUS_ACTIVE, IConstant.NOM_CLIENT_STATUS_ACTIVE);
		} catch (Exception e){
			logger.error("referenceData", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		logger.debug("referenceData - END");
		return map;
	}
}
