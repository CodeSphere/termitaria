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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
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

import ro.cs.cm.business.BLAudit;
import ro.cs.cm.business.BLProject;
import ro.cs.cm.business.BLProjectTeam;
import ro.cs.cm.business.BLUser;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.context.CMContext;
import ro.cs.cm.entity.Client;
import ro.cs.cm.entity.Project;
import ro.cs.cm.entity.ProjectTeam;
import ro.cs.cm.entity.SearchProjectBean;
import ro.cs.cm.entity.TeamMember;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.utils.SerializeEntity;
import ro.cs.cm.web.controller.root.ControllerUtils;
import ro.cs.cm.web.controller.root.RootSimpleFormController;
import ro.cs.cm.web.security.UserAuth;
import ro.cs.cm.ws.client.om.entity.UserSimple;
import ro.cs.cm.ws.client.ts.TSWebServiceClient;

/**
 * The controller that takes care of the search for the projects
 * 
 * @author Adelina
 * 
 */
public class ProjectSearchController extends RootSimpleFormController {

	private static final String SUCCESS_VIEW 				= "Project_Listing";
	private static final String FORM_VIEW 					= "Project_Search";
	
	private static final String ROOT_KEY 					= "project.";
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String SEARCH_ERROR 				= ROOT_KEY.concat("search.error");		
	private static final String PAGINATION_ERROR 			= ROOT_KEY.concat("pagination.error");
	private static final String GENERAL_ERROR				= ROOT_KEY.concat("general.error");
	private String GET_PERSONS_FROM_ORG_ERROR				= ROOT_KEY.concat("get.persons.by.organisation.error");	
	private static final String DELETE_SUCCESS				= ROOT_KEY.concat("delete.success");
	private static final String DELETE_ERROR				= ROOT_KEY.concat("delete.error");	
	private String GET_CLIENTS_FROM_ORG_ERROR				= ROOT_KEY.concat("get.clients.by.organisation.error");	
	private static final String DELETE_PROJECT_TEAM_SUCCESS	= "project.team.delete.success";
	private static final String DELETE_PROJECT_TEAM_ERROR	= "project.team.delete.error";
	private final String FINISH_PROJECT_SUCCESS				= ROOT_KEY.concat("finish.success");
	private final String FINISH_PROJECT_ERROR 				= ROOT_KEY.concat("finish.error");
	private final String ABORT_PROJECT_SUCCESS				= ROOT_KEY.concat("abort.success");
	private final String ABORT_PROJECT_ERROR 				= ROOT_KEY.concat("abort.error");
	private final String ACTIVATE_PROJECT_SUCCESS			= ROOT_KEY.concat("activate.success");
	private final String ACTIVATE_PROJECT_ERROR 			= ROOT_KEY.concat("activate.error");
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private static final String ACTION 						= "action";	
	private static final String PAGE 						= "page";
	private static final String NEXT 						= "next";
	private static final String PREV 						= "prev";
	private static final String FIRST 						= "first";
	private static final String LAST 						= "last";
	private static final String PAGE_NBR 					= "pagenbr";
	private static final String NUMBER 						= "nbr";
	private static final String PAGINATION 					= "pagination";	
	private static final String DELETE_FROM_PROJECT_FORM 	= "DELETE_FROM_PROJECT_FORM";
	private static final String DELETE_FROM_PROJECT_TEAM_FORM = "DELETE_FROM_PROJECT_TEAM_FORM";
	private static final String LIST_PROJECTS_FROM_CLIENT	= "LIST_PROJECTS_FROM_CLIENT";
	private static final String DELETE_ALL					= "DELETE_ALL";	
	private static final String PROJECT_ID					= "projectId";
	private static final String PROJECT_TEAM_ID				= "projectTeamId";
	private final String CMD_FINISH_PROJECT					= "FINISH_PROJECT";
	private final String CMD_ABORT_PROJECT					= "ABORT_PROJECT";
	private final String CMD_ACTIVATE_PROJECT				= "ACTIVATE_PROJECT";
	private final String CMD_FINISH_PROJECT_LISTING			= "FINISH_PROJECT_LISTING";
	private final String CMD_ABORT_PROJECT_LISTING			= "ABORT_PROJECT_LISTING";
	private final String CMD_ACTIVATE_PROJECT_LISTING		= "ACTIVATE_PROJECT_LISTING";
	
	//------------------------OTHER PARAMETERS---------------------------------------------------------	
	private static final String SEARCH_RESULTS 				= "SEARCH_RESULTS";
	private static final String PAGES 						= ROOT_KEY.concat("pagination.pages");			
	private static final String SEARCH_PROJECT_BEAN 		= "searchProjectBean";	
	private static final String COMMAND 					= "command";	
	private static final String JSON_PERSONS 				= "JSON_PERSONS";
	private static final String JSON_CLIENTS				= "JSON_CLIENTS";
	private static final String USER_ID						= "USER_ID";
	private static final String CLIENT_ID					= "clientId";
	private static final String CLIENT_NAME					= "clientName";
	private static final String PROJECT_STATUS				= "PROJECT_STATUS";	
	
	

	// Number of characters that fit in the panel display header
    // if there are big words
    public static final Integer NR_CHARS_PANEL_HEADER	= 45;

	public ProjectSearchController() {
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
		setCommandName(SEARCH_PROJECT_BEAN);
		setCommandClass(SearchProjectBean.class);
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		logger.debug("formBackingObject - START");

		SearchProjectBean spb = new SearchProjectBean();

		// we set the initial search parameters
		spb.setSortDirection(IConstant.ASCENDING);
		spb.setSortParam("name");
					
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		Integer projectId = ServletRequestUtils.getIntParameter(request,PROJECT_ID);
		
		Integer projectTeamId = ServletRequestUtils.getIntParameter(request,PROJECT_TEAM_ID);
				
		// deletes a project if the request comes from project form
		if(action != null && DELETE_FROM_PROJECT_FORM.equals(action) && projectId != null) {
			handleDeleteFromProjectForm(request, spb, projectId);
		} else if(action != null && DELETE_FROM_PROJECT_TEAM_FORM.equals(action) && projectTeamId != null) { // deletes a project team
			handleDeleteProjectTeam(request, projectTeamId);
		} else if(action != null && CMD_FINISH_PROJECT.equals(action) && projectId != null) { // finishes a project					
			handleFinishProject(request, projectId);				
		} else if(action != null && CMD_ABORT_PROJECT.equals(action) && projectId != null) { // abort a project					
			handleAbortProject(request, projectId);
		} else if(action != null && CMD_ACTIVATE_PROJECT.equals(action) && projectId != null) { // activate a project					
			handleActivateProject(request, projectId);
		} else if(action != null && LIST_PROJECTS_FROM_CLIENT.equals(action)) {
			Integer clientId = ServletRequestUtils.getIntParameter(request, CLIENT_ID);			
			if(clientId != null) {
				spb.setClientId(clientId);
			}			
			String clientName = ServletRequestUtils.getStringParameter(request, CLIENT_NAME);			
			request.setAttribute(CLIENT_NAME, clientName);
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

		ModelAndView mav = new ModelAndView(getSuccessView());

		boolean isDeleteAction = false;
		
		SearchProjectBean searchProjectBean = (SearchProjectBean) command;
		
		// if the user didn't select at least one status then we'll make a search on all status
		Set<Byte> statusIds = null;
		if(searchProjectBean.getStatus() == -1) {
			statusIds = new HashSet<Byte>();
			statusIds.add(IConstant.NOM_PROJECT_STATUS_OPENED);			
			statusIds.add(IConstant.NOM_PROJECT_STATUS_CLOSED);
			statusIds.add(IConstant.NOM_PROJECT_STATUS_ABORTED);
		}
		
		// the organizationId
		Integer organizationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		logger.debug("organizationId = " + organizationId);	
		
		// get the clients for the certain organization
		
		List<Client> clients = new ArrayList<Client>();
		try{
			clients = BLProject.getInstance().getClientsForProjects(organizationId);
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_CLIENTS_FROM_ORG_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		logger.debug("nr of clients = " + clients.size() + ", clients = " + clients);
		
		Set<Integer> clientIds = new HashSet<Integer>();
		if(clients != null && clients.size() > 0) {
			for(Client client : clients) {
				clientIds.add(client.getClientId());
			}
			clientIds.add(new Integer(-1));
		} else {
			clientIds.add(new Integer(-1));
		}
					
		// if I have sort action or search
		// action, I have to make a search(handleSearch)
		
		// checks if i have a delete action
		if(request.getParameter(ACTION) != null && DELETE_ALL.equals(request.getParameter(ACTION))) {
			mav = handleDeleteAll(request, command, infoMessages, errorMessages, statusIds, clientIds);
			isDeleteAction = true;
		}
		
		Integer projectId = ServletRequestUtils.getIntParameter(request,PROJECT_ID);
		// checks if i have an update status action
		if(request.getParameter(ACTION) != null && CMD_FINISH_PROJECT_LISTING.equals(request.getParameter(ACTION))) {
			logger.debug("projectId onSubmit = " + request.getParameter(PROJECT_ID));
			if(projectId!= null) {
				handleFinishProject(request, projectId);
			}
		} 
		
		// checks if i have an update status action
		if(request.getParameter(ACTION) != null && CMD_ABORT_PROJECT_LISTING.equals(request.getParameter(ACTION))) {
			logger.debug("projectId onSubmit = " + request.getParameter(PROJECT_ID));
			if(projectId!= null) {
				handleAbortProject(request, projectId);
			}
		} 
		
		// checks if i have an update status action
		if(request.getParameter(ACTION) != null && CMD_ACTIVATE_PROJECT_LISTING.equals(request.getParameter(ACTION))) {
			logger.debug("projectId onSubmit = " + request.getParameter(PROJECT_ID));
			if(projectId!= null) {
				handleActivateProject(request, projectId);
			}
		} 
						
		// search and pagination
		if (request.getParameter(ACTION) != null
				&& PAGINATION.equals(request.getParameter(ACTION))) {
			mav = handlePagination(request, errorMessages, command, statusIds, clientIds);
		} else {
			mav = handleSearch(request, errorMessages, command, isDeleteAction, statusIds, clientIds);
		}			

		setErrors(request, errorMessages);
		setMessages(request, infoMessages);

		logger.debug("onSubmit - END");
		return mav;
	}

	/**
	 * Search for the requested parameters
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param errorMessages
	 * @param command
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handleSearch(HttpServletRequest request,
			ArrayList<String> errorMessages, Object command, boolean isDeleteAction, Set<Byte> statusIds, Set<Integer> clientIds)
			throws BusinessException {
		logger.debug("handleSearch - START");

		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchProjectBean searchProjectBean = (SearchProjectBean) command;

		List<Project> res = null;
		
		//if the organizationId is not set
		//in order to retrieve only its organization projects
		if (searchProjectBean.getOrganizationId() == -1) {
			// set his organisationId
			searchProjectBean.setOrganizationId((ControllerUtils.getInstance().getOrganisationIdFromSession(request)));
		}

		try {
			res = BLProject.getInstance().getFromSearch(searchProjectBean, statusIds, clientIds, isDeleteAction, messageSource);			
					
			for(Project project : res) {
				String panelHeaderName = ControllerUtils.getInstance().truncateName(project.getName(), NR_CHARS_PANEL_HEADER);
				project.setPanelHeaderName(panelHeaderName);
				if(project.getClientId() != -1) {
					Client client = project.getClient();
					String headerName = null;
					if (client.getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
						headerName = client.getC_name();
					} else if (client.getType() == IConstant.NOM_CLIENT_TYPE_PERSON) {
						headerName = client.getP_firstName().concat(" ").concat(client.getP_lastName());
					}
					String clientPanelHeaderName = ControllerUtils.getInstance().truncateName(headerName, NR_CHARS_PANEL_HEADER);
					client.setPanelHeaderName(clientPanelHeaderName);
				}
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
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR,
					new Object[] { ControllerUtils.getInstance()
							.getFormattedCurrentTime() }, RequestContextUtils
							.getLocale(request)));
		}
		
		logger.debug("res = " + res);
		mav.addObject(SEARCH_RESULTS, res);
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		mav.addObject(USER_ID, userAuth.getPersonId());	

		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchProjectBean, PAGES);

		mav.addObject(SEARCH_PROJECT_BEAN, searchProjectBean);
		mav.addObject(COMMAND, command);

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
	private ModelAndView handlePagination(HttpServletRequest request,
			ArrayList<String> errorMessages, Object command, Set<Byte> statusIds, Set<Integer> clientIds)
			throws BusinessException {
		logger.debug("handlePagination - START");

		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchProjectBean searchProjectBean = (SearchProjectBean) command;
		logger.debug("search project bean = " + searchProjectBean);
		
		//if the organizationId is not set
		//in order to retrieve only its organization projects
		if (searchProjectBean.getOrganizationId() == -1) {
			// set his organisationId
			searchProjectBean.setOrganizationId((ControllerUtils.getInstance().getOrganisationIdFromSession(request)));
		}

		try {
			if (request.getParameter(PAGE) != null) {
				if (NEXT.equals(request.getParameter(PAGE))) {
					searchProjectBean.setCurrentPage(searchProjectBean
							.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))) {
					searchProjectBean.setCurrentPage(searchProjectBean
							.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))) {
					searchProjectBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))) {
					searchProjectBean.setCurrentPage(searchProjectBean
							.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))) {
					if (request.getParameter(PAGE_NBR) != null
							&& !"".equals(request.getParameter(PAGE_NBR))) {
						searchProjectBean.setCurrentPage(Integer
								.parseInt(request.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchProjectBean.setCurrentPage(-1);
					}
				}
			}
		} catch (Exception e) {
			// something is wrong
			// I will show the first page
			logger.error(PAGINATION_ERROR, e);
			searchProjectBean.setCurrentPage(-1);
		}

		List<Project> res = null;

		try {
			res = BLProject.getInstance().getFromSearch(searchProjectBean, statusIds, clientIds, false, messageSource);			
			
			for(Project project : res) {
				String panelHeaderName = ControllerUtils.getInstance().truncateName(project.getName(), NR_CHARS_PANEL_HEADER);
				project.setPanelHeaderName(panelHeaderName);
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
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR,
					new Object[] { ControllerUtils.getInstance()
							.getFormattedCurrentTime() }, RequestContextUtils
							.getLocale(request)));
		}
		
		mav.addObject(SEARCH_RESULTS, res);
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		mav.addObject(USER_ID, userAuth.getPersonId());	

		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchProjectBean, PAGES);		

		mav.addObject(SEARCH_PROJECT_BEAN, searchProjectBean);
		mav.addObject(COMMAND, command);
		logger.debug("handlePagination - END");
		return mav;
	}	
	
	
	/**
	 * Deletes a project
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param searchProjectBean
	 * @throws BusinessException
	 */
	private Set<Integer> handleDeleteAllSimple(HttpServletRequest request, SearchProjectBean searchProjectBean) throws BusinessException{
		logger.debug("handleDeleteAllSimple - START -");
		
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		Set<Integer> projectIds = new HashSet<Integer>();
		Set<Integer> teamMemberIds = new HashSet<Integer>();
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		logger.debug("searchProjectBean = " + searchProjectBean);
		logger.debug("starts deleting " + searchProjectBean.getProjectId().length + " projects.");
		Project project = null;
		
		for(int i = 0; i < searchProjectBean.getProjectId().length; i++) {
			logger.debug("Delete issue : " + searchProjectBean.getProjectId()[i]);
			boolean isDeleted = true;
			try{
				project = BLProject.getInstance().deleteAll(searchProjectBean.getProjectId()[i]);				
				projectIds.add(project.getProjectId());
				
				// deletes the project details from ts
				handleDeleteProjectDetails(projectIds);
				
				Set<TeamMember> teamMembers = project.getProjectTeam().getTeamMembers();
				for(TeamMember member: teamMembers) {
					teamMemberIds.add(member.getMemberId());
				}		
				
				logger.debug("teamMembersIds = " + teamMemberIds);
				
				handleDeleteTeamMemberDetails(teamMemberIds);
				
			} catch(BusinessException be){
				logger.error("", be);
				errorMessages.add(messageSource.getMessage(DELETE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
				isDeleted = false;
			} catch (Exception e){
				logger.error("", e);
				errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
						.getLocale(request)));
			}	
			
			if(isDeleted){
				String projectNameMessage = ControllerUtils.getInstance().tokenizeField(project.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				infoMessages.add(messageSource.getMessage(DELETE_SUCCESS, new Object[] {projectNameMessage}, RequestContextUtils.getLocale(request)));
				
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PROJECT_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
								messageSource.getMessage(IConstant.AUDIT_EVENT_PROJECT_DELETE_MESSAGE, new Object[] {project.getName()}, new Locale("en")),
								messageSource.getMessage(IConstant.AUDIT_EVENT_PROJECT_DELETE_MESSAGE, new Object[] {project.getName()}, new Locale("ro")),  
								ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				} catch (Exception exc) {
					logger.error("", exc);
				}
			}			
			
		}
		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("handleDeleteAllSimple - END -");
		return projectIds;
	}
	
	private void handleDeleteProjectTeam(HttpServletRequest request, Integer projectTeamId) {
		logger.debug("handleDelete - START -");
						
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		ProjectTeam projectTeam = null;
			
		boolean isDeleted = true;
		try{
			projectTeam = BLProjectTeam.getInstance().deleteAll(projectTeamId); 
		} catch(BusinessException be){
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(DELETE_PROJECT_TEAM_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
			isDeleted = false;
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}	
		
		if(isDeleted){			
			infoMessages.add(messageSource.getMessage(DELETE_PROJECT_TEAM_SUCCESS, new Object[] {}, RequestContextUtils.getLocale(request)));
		}		
		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("handleDelete - END -");		
	}
	
	/**
	 * Deletes all the projects or a specific project
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param command
	 * @param infoMessages
	 * @param errorMessages
	 * @param statusIds
	 * @param clientIds
	 * @return
	 * @throws BusinessException
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private ModelAndView handleDeleteAll(HttpServletRequest request, Object command, ArrayList<String> infoMessages, ArrayList<String> errorMessages, Set<Byte> statusIds, Set<Integer> clientIds) throws BusinessException, ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		
		logger.debug("handleDeleteAll - START");		
		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchProjectBean searchProjectBean = (SearchProjectBean) command;
						
		handleDeleteAllSimple(request, searchProjectBean);		
						
		logger.debug("Results per page = " + searchProjectBean.getResultsPerPage());
		
		logger.debug("handleDeleteAll - END");
		return mav;
	}	
	
	/**
	 * Deletes a project that comes from a project form
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param searchProjectBean
	 * @param projectId
	 * @throws BusinessException
	 */
	private void handleDeleteFromProjectForm(HttpServletRequest request, SearchProjectBean searchProjectBean, Integer projectId) throws BusinessException {
		logger.debug("handleDeleteFromProjectForm - START");
		
		Integer[] projects = new Integer[1];
		projects[0] = projectId;
		searchProjectBean.setProjectId(projects);
		
		Set<Integer> projectIds = handleDeleteAllSimple(request, searchProjectBean);
		
		handleDeleteProjectDetails(projectIds);
		
		logger.debug("handleDeleteFromProjectForm - END");
	}	
	
	/**
	 * Finish a project
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param projectId
	 * @throws Exception
	 */
	private void handleFinishProject(HttpServletRequest request, Integer projectId) throws Exception {
		logger.debug("handleFinishProject - START");
		
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();	
						
		try{
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// finish the projects
			Project project = BLProject.getInstance().finishProject(projectId);	
			
			// change status to finished for project detail
			Set<Integer> projectIds = new HashSet<Integer>();
			projectIds.add(project.getProjectId());
			handleFinishProjectDetails(projectIds);
			
			String projectName = project.getName();
			infoMessages.add(messageSource.getMessage(FINISH_PROJECT_SUCCESS, new Object[] {projectName} , RequestContextUtils
					.getLocale(request)));
			
			//add the new audit event only if the user is not AdminIT
			try {
				if (!userAuth.isAdminIT()){
					BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PROJECT_FINISH_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
							messageSource.getMessage(IConstant.AUDIT_EVENT_PROJECT_FINISH_MESSAGE, new Object[] {project.getName()}, new Locale("en")),
							messageSource.getMessage(IConstant.AUDIT_EVENT_PROJECT_FINISH_MESSAGE, new Object[] {project.getName()}, new Locale("ro")),  
							ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
				}
			} catch (Exception exc) {
				logger.error("", exc);
			}
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(FINISH_PROJECT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("handleFinishProject - END");			
	}
	
	/**
	 * Abort a project
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param projectId
	 * @throws Exception
	 */
	private void handleAbortProject(HttpServletRequest request, Integer projectId) throws Exception {
		logger.debug("handleAbortProject - START");
		
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		try{
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// aborts the project
			Project project = BLProject.getInstance().abortProject(projectId);
			
			// change status to abort for project detail
			Set<Integer> projectIds = new HashSet<Integer>();
			projectIds.add(project.getProjectId());
			handleAbortProjectDetails(projectIds);
						
			String projectName = project.getName();
			infoMessages.add(messageSource.getMessage(ABORT_PROJECT_SUCCESS, new Object[] {projectName} , RequestContextUtils.getLocale(request)));
			
			//add the new audit event only if the user is not AdminIT
			try {
				if (!userAuth.isAdminIT()){
					BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PROJECT_ABORT_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
							messageSource.getMessage(IConstant.AUDIT_EVENT_PROJECT_ABORT_MESSAGE, new Object[] {project.getName()}, new Locale("en")),
							messageSource.getMessage(IConstant.AUDIT_EVENT_PROJECT_ABORT_MESSAGE, new Object[] {project.getName()}, new Locale("ro")),  
							ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
				}
			} catch (Exception exc) {
				logger.error("", exc);
			}
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(ABORT_PROJECT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
				
		logger.debug("handleAbortProject - END");			
	}
	
	/**
	 * Activate a project
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param projectId
	 * @throws Exception
	 */
	private void handleActivateProject(HttpServletRequest request, Integer projectId) throws Exception {
		logger.debug("handleActivateProject - START");
			
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		try{
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			//activate the project
			Project project = BLProject.getInstance().activateProject(projectId);	
			
			// change the status to opened for project details
			Set<Integer> projectIds = new HashSet<Integer>();
			projectIds.add(project.getProjectId());
			handleOpenProjectDetails(projectIds);
			
			String projectName = project.getName();
			infoMessages.add(messageSource.getMessage(ACTIVATE_PROJECT_SUCCESS, new Object[] {projectName} , RequestContextUtils.getLocale(request)));
			
			//add the new audit event only if the user is not AdminIT
			try {
				if (!userAuth.isAdminIT()){
					BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PROJECT_ACTIVATE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
							messageSource.getMessage(IConstant.AUDIT_EVENT_PROJECT_ACTIVATE_MESSAGE, new Object[] {project.getName()}, new Locale("en")),
							messageSource.getMessage(IConstant.AUDIT_EVENT_PROJECT_ACTIVATE_MESSAGE, new Object[] {project.getName()}, new Locale("ro")),  
							ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
				}
			} catch (Exception exc) {
				logger.error("", exc);
			}
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(ACTIVATE_PROJECT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("handleActivateProject - END");		
	}
	
	/**
	 * Handles the delete project details from TS
	 * 
	 * @author Adelina
	 * 
	 * @param projectIds
	 */
	public void handleDeleteProjectDetails(Set<Integer> projectIds) {
		logger.debug("handleDeleteProjectDetails - START");
		final Exception[] exception = new Exception[1];
		
		final SerializeEntity entity = SerializeEntity.getInstance();
		final File file = entity.createFile(IConstant.FILE_NAME_SERIALIZATION_FOR_DELETE_PROJECT_DETIALS);
		entity.doPersist(projectIds, file);
		
		for(Integer projectId : projectIds) {			
			logger.debug("projectId - " + projectId);
			final Integer id = projectId;
			
			class DeletesProjectDetailRunnable implements Runnable {
				public void run() {						
					logger.debug("DeletesProjectDetailRunnable - run START");
					try{						
						TSWebServiceClient.getInstance().deleteProjectDetails(id);
						entity.doLoad(id, file);
						logger.debug("id = " + id);
					} catch (Exception e) {					
		    			exception[0] = e;
					}
					logger.debug("DeletesProjectDetailRunnable - run END");
				}
			}
			
			Thread thread = new Thread(new DeletesProjectDetailRunnable());		
			thread.start();	
		}
		logger.debug("handleDeleteProjectDetails - END");
	}
	
	
	/**
	 * Handles the finish project details from TS
	 * 
	 * @author Adelina
	 * 
	 * @param projectIds
	 */
	public void handleFinishProjectDetails(Set<Integer> projectIds) {
		logger.debug("handleFinishProjectDetails - START");
		final Exception[] exception = new Exception[1];
		
		final SerializeEntity entity = SerializeEntity.getInstance();
		final File file = entity.createFile(IConstant.FILE_NAME_SERIALIZATION_FOR_FINISH_PROJECT_DETIALS);
		entity.doPersist(projectIds, file);
		
		for(Integer projectId : projectIds) {			
			logger.debug("projectId - " + projectId);
			final Integer id = projectId;
			
			class FinishProjectDetailRunnable implements Runnable {
				public void run() {						
					logger.debug("FinishProjectDetailRunnable - run START");
					try{						
						TSWebServiceClient.getInstance().finishProjectDetails(id);
						entity.doLoad(id, file);
						logger.debug("id = " + id);
					} catch (Exception e) {					
		    			exception[0] = e;
					}
					logger.debug("FinishProjectDetailRunnable - run END");
				}
			}
			
			Thread thread = new Thread(new FinishProjectDetailRunnable());		
			thread.start();	
		}
		logger.debug("handleFinishProjectDetails - END");
	}
	
	/**
	 * Handles the abort project details from TS
	 * 
	 * @author Adelina
	 * 
	 * @param projectIds
	 */
	public void handleAbortProjectDetails(Set<Integer> projectIds) {
		logger.debug("handleAbortProjectDetails - START");
		final Exception[] exception = new Exception[1];
		
		final SerializeEntity entity = SerializeEntity.getInstance();
		final File file = entity.createFile(IConstant.FILE_NAME_SERIALIZATION_FOR_ABORT_PROJECT_DETIALS);
		entity.doPersist(projectIds, file);
		
		for(Integer projectId : projectIds) {			
			logger.debug("projectId - " + projectId);
			final Integer id = projectId;
			
			class AbortProjectDetailRunnable implements Runnable {
				public void run() {						
					logger.debug("AbortProjectDetailRunnable - run START");
					try{						
						TSWebServiceClient.getInstance().abortProjectDetails(id);
						entity.doLoad(id, file);
						logger.debug("id = " + id);
					} catch (Exception e) {					
		    			exception[0] = e;
					}
					logger.debug("AbortProjectDetailRunnable - run END");
				}
			}
			
			Thread thread = new Thread(new AbortProjectDetailRunnable());		
			thread.start();	
		}
		logger.debug("handleAbortProjectDetails - END");
	}

	
	/**
	 * Handles the open project details from TS
	 * 
	 * @author Adelina
	 * 
	 * @param projectIds
	 */
	public void handleOpenProjectDetails(Set<Integer> projectIds) {
		logger.debug("handleOpenProjectDetails - START");
		final Exception[] exception = new Exception[1];
		
		final SerializeEntity entity = SerializeEntity.getInstance();
		final File file = entity.createFile(IConstant.FILE_NAME_SERIALIZATION_FOR_OPEN_PROJECT_DETIALS);
		entity.doPersist(projectIds, file);
		
		for(Integer projectId : projectIds) {			
			logger.debug("projectId - " + projectId);
			final Integer id = projectId;
			
			class OpenProjectDetailRunnable implements Runnable {
				public void run() {						
					logger.debug("OpenProjectDetailRunnable - run START");
					try{						
						TSWebServiceClient.getInstance().openProjectDetails(id);
						entity.doLoad(id, file);
						logger.debug("id = " + id);
					} catch (Exception e) {					
		    			exception[0] = e;
					}
					logger.debug("OpenProjectDetailRunnable - run END");
				}
			}
			
			Thread thread = new Thread(new OpenProjectDetailRunnable());		
			thread.start();	
		}
		logger.debug("handleOpenProjectDetails - END");
	}
	
			
	/**
	 * Handles the delete team members details
	 * 
	 * @author Adelina
	 * 
	 * @param teamMemberIds
	 */
	public void handleDeleteTeamMemberDetails(Set<Integer> teamMemberIds) {
		logger.debug("handleDeleteTeamMemberDetails - START");
		final Exception[] exception = new Exception[1];
		
		final SerializeEntity entity = SerializeEntity.getInstance();
		final File file = entity.createFile(IConstant.FILE_NAME_SERIALIZATION_FOR_DELETE_MEMBER_DETIALS);
		entity.doPersist(teamMemberIds, file);
		
		for(Integer teamMemberId : teamMemberIds) {			
			logger.debug("teamMember - " + teamMemberId);
			final Integer id = teamMemberId;
			
			class DeletesTeamMemberDetailRunnable implements Runnable {
				public void run() {						
					logger.debug("DeletesTeamMemberDetailRunnable - run START");
					try{					
						TSWebServiceClient.getInstance().deleteTeamMemberDetails(id);					
						entity.doLoad(id, file);
						logger.debug("id = " + id);
					} catch (Exception e) {					
		    			exception[0] = e;
					}
					logger.debug("DeletesProjectDetailRunnable - run END");
				}
			}
			
			Thread thread = new Thread(new DeletesTeamMemberDetailRunnable());		
			thread.start();	
		}
		logger.debug("handleDeleteTeamMemberDetails - END");
	}
		
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request) throws Exception{
		logger.debug("referenceData - START");
		//used as a container for error messages
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		Map map = new HashMap();		
		
		//load the list of possible results per page
		map.put(IConstant.NOM_RESULTS_PER_PAGE, CMContext.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
		logger.debug("RESULTS PER PAGE CLASSIFIED LIST LOADED");	
				
		Integer organizationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		logger.debug("organizationId = " + organizationId);		
				
		// list the persons for the organization that is project manager
		List<UserSimple> users = new ArrayList<UserSimple>();
		try{
			users = BLUser.getInstance().getUsersSimpleByOrganizationId(organizationId, true);
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_PERSONS_FROM_ORG_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
			
		map.put(JSON_PERSONS, ControllerUtils.getInstance().
				getPersonsFirstNameLastNameFromOrgAsJSON(users, RequestContextUtils.getLocale(request), 
						errorMessages, messageSource));					
		
		// Adding to model the issue's status
		map.put(PROJECT_STATUS, CMContext.getFromContext(IConstant.NOM_PROJECT_STATUS));
		
		// get the clients for the certain organization
		
		List<Client> clients = new ArrayList<Client>();
		try{
			clients = BLProject.getInstance().getClientsForProjects(organizationId);
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_CLIENTS_FROM_ORG_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		logger.debug("reference data clients = " + clients.size() + ", " + clients);
		
		map.put(JSON_CLIENTS, ControllerUtils.getInstance().
		getClientsFromOrgAsJSON(clients, RequestContextUtils.getLocale(request), 
				errorMessages, messageSource, true));	
		
		setErrors(request, errorMessages);
		logger.debug("referenceData - END");
		
		return map;
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
