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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLOrganisationStructure;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.OrgTree;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.SearchOrganisationBean;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;

/**
 * @author alu
 * @author dan.damian
 * 
 */
public class OrganisationSearchController extends RootSimpleFormController {

	private static final String SEARCH_ERROR = "search.error";
	private static final String SEARCH_EXCEPTION_ERROR = "search.exception.error";
	private static final String SEARCH_RESULTS = "SEARCH_RESULTS";
	private static final String PAGINATION = "pagination";
	private static final String DELETEALL = "DELETE_ALL";
	private static final String ID = "ID";
	private static final String NAME = "NAME";
	private static final String ACTION = "action";
	private static final String LIST_RESULTS_VIEW = "Organisation_ListOrganisations";
	private static final String SEARCH_FORM_BEAN = "searchOrganisationBean";
	private static final String PAGE = "page";
	private static final String NEXT = "next";
	private static final String PREV = "prev";
	private static final String FIRST = "first";
	private static final String LAST = "last";
	private static final String PAGE_NBR = "pagenbr";
	private static final String NUMBER = "nbr";
	private static final String PAGES = "organisation.pagination.pages";
	private static final String RESULTS_PER_PAGE = "RESULTS_PER_PAGE";
	private static final String ORGANISATION_ID = "organisationId";	
	private static final String CHANGE_STATUS_ENABLE_SUCCESS = "organisation.enable.success";
	private static final String CHANGE_STATUS_DISABLE_SUCCESS = "organisation.disable.success";
	private static final String UPDATE_ERROR = "organisation.update.error";
	private static final String UPDATESTATUS = "updatestatus";
	private static final String DELETE_ERROR = "organisation.delete.error";
	private static final String DELETE_OTHER_MODULES_ERROR = "organisation.delete.modules.error";
	private static final String DELETE_SUCCESS = "organisation.delete.success";
	private static final String ORGANIZATIONS_DELETE_SUCCESS = "organisations.delete.success";
	private static final String SESSION_ORGANIZATION_DELETED_VIEW = "Organization_RedirectSelectOrganization";
	private static final String DISPLAY_ORGANIZATION_DELETED_MESSAGE = "DISPLAY_ORGANIZATION_DELETED_MESSAGE";
	private static final String UPDATE_ORGANIZATIONS_TREE_STRUCTURE_ERROR = "organisation.update.all.trees.error";
	private static final String DELETE_FROM_ORGANISATION_FORM = "DELETE_FROM_ORGANISATION_FORM";
	private static final String ORGANISATION_TYPE	= "ORGANISATION_TYPE";
	private static boolean sessionOrgIsDeleted = false;
	
	// Number of characters that fit in a line, for the display panel,
    // if there are big words
    public static final Integer NR_CHARS_PER_LINE				= 28;

	public OrganisationSearchController() {
		setFormView("Organisation_SearchOrganisation");
		setSuccessView("Organisation_ListOrganisations");
		setCommandName("searchOrganisationBean");
		setCommandClass(SearchOrganisationBean.class);
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		logger.debug("formBackingObject - START");
		
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		SearchOrganisationBean searchOrgBean = new SearchOrganisationBean();

		// set the initial search parameters
		searchOrgBean.setSortDirection(IConstant.ASCENDING);
		searchOrgBean.setSortParam("name");
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		Integer organisationId = ServletRequestUtils.getIntParameter(request, ORGANISATION_ID);
		
		// deletes an organisation if the request comes from organisation form
		if(action != null && DELETE_FROM_ORGANISATION_FORM.equals(action) && organisationId != null) {		
			handleDeleteFromOrganisationForm(request, searchOrgBean, organisationId);
			if (request.getParameter(DISPLAY_ORGANIZATION_DELETED_MESSAGE) != null) {
				infoMessages.add(messageSource.getMessage(
						ORGANIZATIONS_DELETE_SUCCESS, null, RequestContextUtils
								.getLocale(request)));
			}
		}
		
		// setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("formBackingObject - END");
		return searchOrgBean;
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit - START");
		// used as a container for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();

		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		boolean isChanged = false;
		
		SearchOrganisationBean searchOrganisationBean = (SearchOrganisationBean) command;
		
		//If the user didn't select at least one type then we'll make a search on all types		
		Set<Byte> typesIds = null;
		if (searchOrganisationBean.getType() == -1) {
			typesIds = new HashSet<Byte>();			
			typesIds.add(IConstant.NOM_ORGANISATION_TYPE_COMPANY);
			typesIds.add(IConstant.NOM_ORGANISATION_TYPE_BRANCH);
			typesIds.add(IConstant.NOM_ORGANISATION_TYPE_GROUP_COMPANIES);
			typesIds.add(IConstant.NOM_ORGANISATION_TYPE_HQ);
		}
					
		if (request.getParameter(ACTION) != null
			&& UPDATESTATUS.equals(request.getParameter(ACTION))) {
			logger.debug("update status onsubmit");
			if (request.getParameter(ORGANISATION_ID) != null) {
				logger.debug("update status onsubmit = "
						+ request.getParameter(ORGANISATION_ID));
				handleUpdateStatus(request, errorMessages, infoMessages);
				isChanged = true;
			}
		}

		if (request.getParameter(ACTION) != null
				&& DELETEALL.equals(request.getParameter(ACTION))) {
			SearchOrganisationBean org = null;
			if (request.getParameter(ID) != null) {
				org = new SearchOrganisationBean();
				Integer[] ids = { Integer.valueOf(request.getParameter(ID)) };
				String name = request.getParameter(NAME);
				org.setOrganisationId(ids);
				org.setName(name);
			}
			mav = handleDeleteAll(request, searchOrganisationBean, errorMessages,
					infoMessages, org, typesIds);
		} else if (request.getParameter(ACTION) != null
				&& PAGINATION.equals(request.getParameter(ACTION))) {
			mav = handlePagination(request, errorMessages, searchOrganisationBean, typesIds);
		} else {
			mav = handleSearch(request, searchOrganisationBean, errorMessages, isChanged, typesIds);
		}	

		if (request.getParameter(DISPLAY_ORGANIZATION_DELETED_MESSAGE) != null) {
			infoMessages.add(messageSource.getMessage(
					ORGANIZATIONS_DELETE_SUCCESS, null, RequestContextUtils
							.getLocale(request)));
		}
		// setting all messages on response
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
	 * @param errorMessages
	 * @param command
	 * @param typesIds
	 * @return
	 * @throws BusinessException
	 */	
	private ModelAndView handlePagination(HttpServletRequest request, ArrayList<String> errorMessages,
			Object command, Set<Byte> typesIds) throws BusinessException {
		logger.debug("handlePagination - START");

		ModelAndView mav = new ModelAndView(LIST_RESULTS_VIEW);
		SearchOrganisationBean searchOrganisationBean = (SearchOrganisationBean) command;

		try {
			if (request.getParameter(PAGE) != null) {
				if (NEXT.equals(request.getParameter(PAGE))) {
					searchOrganisationBean
							.setCurrentPage(searchOrganisationBean
									.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))) {
					searchOrganisationBean
							.setCurrentPage(searchOrganisationBean
									.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))) {
					searchOrganisationBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))) {
					searchOrganisationBean
							.setCurrentPage(searchOrganisationBean
									.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))) {
					if (request.getParameter(PAGE_NBR) != null
							&& !"".equals(request.getParameter(PAGE_NBR))) {
						searchOrganisationBean.setCurrentPage(Integer
								.parseInt(request.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchOrganisationBean.setCurrentPage(-1);
					}
				}
			}
		} catch (Exception e) {
			// something is wrong
			// I will show the first page
			logger.error("PAGINATION ERROR!", e);
			searchOrganisationBean.setCurrentPage(-1);
		}

		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		try {
			
		
		// ****************** Security *******************************
		if (userAuth.hasAuthority(PermissionConstant.getInstance().get_Super())) {
			List<Organisation> res = BLOrganisation.getInstance()
					.getFromSearch(searchOrganisationBean, false, typesIds);
			for(Organisation organisation: res) {
				organisation.setPanelConfirmationName(ControllerUtils.getInstance().tokenizeField(organisation.getName(), NR_CHARS_PER_LINE));
			}
			mav.addObject(SEARCH_RESULTS, res);			
		} else {
			errorMessages.add(messageSource.getMessage(
					IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils
							.getLocale(request)));
		}
		
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
			errorMessages.add(messageSource.getMessage(SEARCH_EXCEPTION_ERROR,
					new Object[] { ControllerUtils.getInstance()
							.getFormattedCurrentTime() }, RequestContextUtils
							.getLocale(request)));
		}

		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchOrganisationBean,
				PAGES);

		mav.addObject(SEARCH_FORM_BEAN, searchOrganisationBean);
		setErrors(request, errorMessages);
		logger.debug("handlePagination - END");
		return mav;
	}

	/**
	 * Handles the search for Organizations form submission
	 * 
	 * @author alu
	 * @author dan.damian
	 * 
	 * @param request
	 * @param command
	 * @param errorMessages
	 * @param isChanged
	 * @param typesIds
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handleSearch(HttpServletRequest request, Object command, ArrayList<String> errorMessages, boolean isChanged, Set<Byte> typesIds)
			throws BusinessException {
		logger.debug("handleSearch - START");

		ModelAndView mav = new ModelAndView(LIST_RESULTS_VIEW);

		SearchOrganisationBean searchOrganisationBean = (SearchOrganisationBean) command;
		logger.debug("Results per page "
				+ searchOrganisationBean.getResultsPerPage());
		List<Organisation> res = null;
		try{
		res = BLOrganisation.getInstance().getFromSearch(
					searchOrganisationBean, isChanged, typesIds);
		
		for(Organisation organisation: res) {
			organisation.setPanelConfirmationName(ControllerUtils.getInstance().tokenizeField(organisation.getName(), NR_CHARS_PER_LINE));
		}
			
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
			errorMessages.add(messageSource.getMessage(SEARCH_EXCEPTION_ERROR,
						new Object[] { ControllerUtils.getInstance()
								.getFormattedCurrentTime() }, RequestContextUtils
								.getLocale(request)));
		}
			
		mav.addObject(SEARCH_RESULTS, res);

		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchOrganisationBean,
				PAGES);
		mav.addObject(SEARCH_FORM_BEAN, searchOrganisationBean);
		
		return mav;
	}

	private void handleUpdateStatus(HttpServletRequest request,
			ArrayList<String> errorMessages, ArrayList<String> infoMessages) {
		logger.debug("handleUpdateStatus - START");

		Locale locale = RequestContextUtils.getLocale(request);

		try {
			// the user that is autenticated
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();

			// ****************** Security *******************************
			// if the user has permission Super, can change status (activate or
			// dezactivate) an organisation
			if (userAuth.hasAuthority(PermissionConstant.getInstance()
					.get_Super())) {
				Organisation organisation = BLOrganisation.getInstance().get(
						ServletRequestUtils.getIntParameter(request,
								ORGANISATION_ID));
				BLOrganisation.getInstance().updateStatus(
						ServletRequestUtils.getIntParameter(request,
								ORGANISATION_ID));
				String organisationNameMessage = ControllerUtils.getInstance().tokenizeField(organisation.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				if(organisation.getStatus() == IConstant.NOM_ORGANISATION_INACTIVE){
					infoMessages.add(messageSource.getMessage(CHANGE_STATUS_ENABLE_SUCCESS,
							new Object[] { organisationNameMessage },
							RequestContextUtils.getLocale(request)));
					
					//add the new audit event
					try {
						//if the organisation is added by the adminIT, the audit event organisationId will be the added organisation id and
						//no first and last name will be sent
						String lastName;
						if (userAuth.isAdminIT()){
							lastName = new String("");
						} else {
							lastName = userAuth.getLastName();
						}
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ORGANISATION_ENABLE_TYPE, userAuth.getFirstName(), lastName
								, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_ENABLE_MESSAGE, new Object[] {organisation.getName()}, new Locale("en"))
								, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_ENABLE_MESSAGE, new Object[] {organisation.getName()}, new Locale("ro"))
								, ServletRequestUtils.getIntParameter(request, ORGANISATION_ID), userAuth.getPersonId());
					} catch (BusinessException exc){
						logger.error(exc);
					}
				} else {
					infoMessages.add(messageSource.getMessage(CHANGE_STATUS_DISABLE_SUCCESS,
							new Object[] { organisationNameMessage },
							RequestContextUtils.getLocale(request)));
					
					//add the new audit event
					try {
						//if the organisation is updated by the adminIT, the audit event organisationId will be the updated organisation id and
						//no first and last name will be sent
						String lastName;
						if (userAuth.isAdminIT()){
							lastName = new String("");
						} else {
							lastName = userAuth.getLastName();
						}
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ORGANISATION_DISABLE_TYPE, userAuth.getFirstName(), lastName
								, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_DISABLE_MESSAGE, new Object[] {organisation.getName()}, new Locale("en"))
								, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_DISABLE_MESSAGE, new Object[] {organisation.getName()}, new Locale("ro"))
								, ServletRequestUtils.getIntParameter(request, ORGANISATION_ID), userAuth.getPersonId());
					} catch (BusinessException exc){
						logger.error(exc);
					}
				}
			} else {
				// else, has not right to change status an organisation
				errorMessages.add(messageSource.getMessage(
						IConstant.SECURITY_NO_RIGHTS, null, locale));
			}

		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR,
					new Object[] {
							be.getCode(),
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() },
					RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR,
					new Object[] { ControllerUtils.getInstance()
							.getFormattedCurrentTime() }, RequestContextUtils
							.getLocale(request)));
		}
		logger.debug("handleUpdateStatus - END");
	}

	/**
	 * 
	 * Deletes the organisation
	 * @author mitziuro
	 * @author Adelina
	 *
	 * @param request
	 * @param command
	 * @param errorMessages
	 * @param infoMessages
	 * @param searchOrganisation
	 * @param typesIds
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handleDeleteAll(HttpServletRequest request,
			Object command, ArrayList<String> errorMessages,
			ArrayList<String> infoMessages,
			SearchOrganisationBean searchOrganisation, Set<Byte> typesIds) throws BusinessException {
		logger.debug("handleDeleteAll - START");

		SearchOrganisationBean org = null;		
	
		// the user that is authenticated
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		if (searchOrganisation == null) {
			org = (SearchOrganisationBean) command;
		} else {
			org = searchOrganisation;
		}

		handleDeleteAllSimple(request, org);
		
		// if the organization from the current session has been deleted, a view
		// that performs
		// a redirect to the organization select page will be returned
		if (sessionOrgIsDeleted && userAuth.isAdminIT()) {
			ModelAndView mav = new ModelAndView(
					SESSION_ORGANIZATION_DELETED_VIEW);
			return mav;
		} else {
			org = (SearchOrganisationBean) command;
			ModelAndView mav = new ModelAndView(LIST_RESULTS_VIEW);
			logger.debug("Results per page " + org.getResultsPerPage());
				
			try {
				List<Organisation> orgs = BLOrganisation.getInstance().getFromSearch(org, true, typesIds);
				for(Organisation organisation: orgs) {
					organisation.setPanelConfirmationName(ControllerUtils.getInstance().tokenizeField(organisation.getName(), NR_CHARS_PER_LINE));
				}
				mav.addObject(SEARCH_RESULTS, orgs);
			} catch (BusinessException bexc) {
				logger.error("", bexc);
				errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {
					bexc.getCode(),	ControllerUtils.getInstance().getFormattedCurrentTime() },
						RequestContextUtils.getLocale(request)));
			}

			// find the number of pages shown in pagination area
			ControllerUtils.getInstance().findPagesLimit(org, PAGES);
			mav.addObject(SEARCH_FORM_BEAN, org);

			logger.debug("handleDeleteAll - END");
			return mav;
		}
	}
	
	/**
	 * Deletes organisations
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param org
	 * @throws BusinessException
	 */
	private void handleDeleteAllSimple(HttpServletRequest request, SearchOrganisationBean org) throws BusinessException {
		
		logger.debug("handleDeleteAllSimple - START -");
		
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		Locale locale = RequestContextUtils.getLocale(request);

		// the user that is authenticated
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		logger.debug(org);
		logger.debug("start deleting " + org.getOrganisationId().length
				+ " organisations.");
		HashMap<Integer, String> orgIdsAndNames = null;
		boolean isDeleted = true;
		int parentId = -1;
		for (int i = 0; i < org.getOrganisationId().length; i++) {
			logger.debug("Delete organisation : " + org.getOrganisationId()[i]);
			//initialize the parameters for every organisation
			//that we want to delete
			isDeleted = true;
			parentId = -1;
			
			// ****************** Security *******************************
			if (userAuth.hasAuthority(PermissionConstant.getInstance()
					.get_Super())) {
				try {
					Organisation organisation = BLOrganisation.getInstance()
							.get(org.getOrganisationId()[i]);
					if (organisation != null) {
						//delete the organization and all its children organizations
						orgIdsAndNames = (HashMap<Integer, String>) BLOrganisation.getInstance()
								.deleteOrgs(org.getOrganisationId()[i]);
						parentId = organisation.getParentId();
					} else {
						orgIdsAndNames = null;
					}
				} catch (BusinessException be) {
					logger.error("", be);
					
					//test if the errror came from the other modules
					if(be.getCode().equals(ICodeException.ORG_DEL_OTHER_MODULE)){
						errorMessages.add(messageSource.getMessage(DELETE_OTHER_MODULES_ERROR,
								new Object[] {
										be.getCode(),
										ControllerUtils.getInstance()
												.getFormattedCurrentTime() },
								RequestContextUtils.getLocale(request)));
						
					} else {
						//other errors 
						errorMessages.add(messageSource.getMessage(DELETE_ERROR,
								new Object[] {
										be.getCode(),
										ControllerUtils.getInstance()
												.getFormattedCurrentTime() },
								RequestContextUtils.getLocale(request)));
					}
					
					isDeleted = false;
				}
				if (isDeleted && orgIdsAndNames != null) {
					//get the organizations trees map stored on context and remove from it the tree of the organizations that have been deleted
					HashMap<Integer, OrgTree> allOrganisationsTrees = (HashMap<Integer, OrgTree>) OMContext
					.getOrganisationsTreesMapFromContext(IConstant.ORGANISATION_TREES);
					Set<Integer> orgIds	= (Set<Integer>)orgIdsAndNames.keySet();
					for (Integer orgId : orgIds){
						allOrganisationsTrees.remove(orgId);
						// test whether or not one of the deleted organizations( the one selected or one of its 
						// children organizations) is the organization from the session
						if (orgId.intValue() == (ControllerUtils.getInstance().getOrganisationIdFromSession(request).intValue())){
							sessionOrgIsDeleted = true;
						}
					}
					//add messages of successfully deleted organizations
					Set<String> names = new HashSet<String>();
					names.addAll(orgIdsAndNames.values());
					for (String name : names) {
						String organisationNameMessage = ControllerUtils.getInstance().tokenizeField(name, IConstant.NR_CHARS_PER_LINE_MESSAGE);
						infoMessages.add(messageSource.getMessage(DELETE_SUCCESS, new Object[] { organisationNameMessage },RequestContextUtils.getLocale(request)));
					}
					if (parentId != -1) {
						// Update all the organizations structures that included
						// organization as a child node
						try {
							BLOrganisationStructure.getInstance().updateAllOrganisationsStructure(parentId);
						} catch (Exception exc) {
							logger.error("Error at updating the parent organization tree structure", exc);
							errorMessages.add(messageSource.getMessage(UPDATE_ORGANIZATIONS_TREE_STRUCTURE_ERROR,new Object[] {exc.getMessage(),
								ControllerUtils.getInstance().getFormattedCurrentTime() }, RequestContextUtils.getLocale(request)));
						} 
					}
					
					//add the audit events for the deleted organizations
					Iterator it = orgIdsAndNames.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Integer, String> pairs = (Map.Entry<Integer, String>)it.next();
						//add the new audit event
						try {
							//if the organisation is deleted by the adminIT, the audit event organisationId will be the deleted organisation id and
							//no first and last name will be sent
							String lastName;
							if (userAuth.isAdminIT()){
								lastName = new String("");
							} else {
								lastName = userAuth.getLastName();
							}
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ORGANISATION_DELETE_TYPE, userAuth.getFirstName(), lastName
									, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_DELETE_MESSAGE, new Object[] {pairs.getValue()}, new Locale("en"))
									, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_DELETE_MESSAGE, new Object[] {pairs.getValue()}, new Locale("ro"))
									, pairs.getKey(), userAuth.getPersonId());
						} catch (BusinessException exc){
							logger.error(exc);
						}
					}
					
				}
			} else {
				errorMessages.add(messageSource.getMessage(
						IConstant.SECURITY_NO_RIGHTS, null, locale));
			}

		}
			
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("handleDeleteAllSimple - END -");
	}
		

	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map map = new HashMap();
		
		// adding to model the organisation's type
		map.put(ORGANISATION_TYPE, OMContext.getFromContext(IConstant.NOM_ORGANISATION_TYPE));
		
		// adding to model the results per page for search results
		map.put(RESULTS_PER_PAGE, OMContext
				.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
		
		if (request.getParameter(DISPLAY_ORGANIZATION_DELETED_MESSAGE) != null) {
			map.put(DISPLAY_ORGANIZATION_DELETED_MESSAGE, request
					.getParameter(DISPLAY_ORGANIZATION_DELETED_MESSAGE));
		}
		return map;
	}
	
	
	/**
	 * Deletes an organisation that comes from an organisation form
	 * 
	 * @author Adelina	
	 * 
	 * @param request
	 * @param searchOrgBean
	 * @param organisationId
	 * @throws BusinessException
	 */
	private void handleDeleteFromOrganisationForm(HttpServletRequest request, SearchOrganisationBean searchOrgBean, Integer organisationId) throws BusinessException{						
		logger.debug("handleDeleteFromOrganisationForm - START - ");
					
		Integer[] organisations = new Integer[1];
		organisations[0] = organisationId;			
		searchOrgBean.setOrganisationId(organisations);
		handleDeleteAllSimple(request, searchOrgBean);
										
		logger.debug("handleDeleteFromOrganisationForm - END - ");
	}
	
	/* (non-Javadoc)
	 * @see ro.cs.om.web.controller.root.RootSimpleFormController#showForm(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.validation.BindException, java.util.Map)
	 */
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {	
		
		logger.debug("showForm - START -");
		
		// the user that is authenticated
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		Integer organisationId = ServletRequestUtils.getIntParameter(request, ORGANISATION_ID);
		if(action != null && DELETE_FROM_ORGANISATION_FORM.equals(action) && organisationId != null) {		
			if (sessionOrgIsDeleted && userAuth.isAdminIT()) {
				ModelAndView mav = new ModelAndView(SESSION_ORGANIZATION_DELETED_VIEW);
				if (request.getParameter(DISPLAY_ORGANIZATION_DELETED_MESSAGE) != null) {
					mav.addObject(DISPLAY_ORGANIZATION_DELETED_MESSAGE, request
							.getParameter(DISPLAY_ORGANIZATION_DELETED_MESSAGE));
				}
				
				return mav;
			}
		}		
		return super.showForm(request, errors, getFormView(), controlModel);
	}

}
