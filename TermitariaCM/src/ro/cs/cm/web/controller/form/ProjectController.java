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

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.cm.business.BLAudit;
import ro.cs.cm.business.BLClient;
import ro.cs.cm.business.BLPerson;
import ro.cs.cm.business.BLProject;
import ro.cs.cm.business.BLProjectTeam;
import ro.cs.cm.business.BLUser;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.common.PermissionConstant;
import ro.cs.cm.entity.Client;
import ro.cs.cm.entity.Project;
import ro.cs.cm.entity.ProjectProjectTeam;
import ro.cs.cm.entity.ProjectTeam;
import ro.cs.cm.entity.TeamMember;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.om.Person;
import ro.cs.cm.web.controller.root.ControllerUtils;
import ro.cs.cm.web.controller.root.RootSimpleFormController;
import ro.cs.cm.web.security.UserAuth;
import ro.cs.cm.ws.client.om.entity.UserSimple;


public class ProjectController  extends RootSimpleFormController {
	
	private static final String SUCCESS_VIEW 				= "Project";
	private static final String FORM_VIEW 					= "Project";
				
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String ROOT_KEY 					= "project.";
	private final String ADD_SUCCESS						= ROOT_KEY.concat("add.success");
	private final String ADD_ERROR							= ROOT_KEY.concat("add.error");
	private final String GET_ERROR							= ROOT_KEY.concat("get.error");	
	private final String UPDATE_SUCCESS						= ROOT_KEY.concat("update.success");
	private final String UPDATE_ERROR						= ROOT_KEY.concat("update.error");
	private final String GENERAL_ERROR						= ROOT_KEY.concat("general.error");			
	private String GET_PERSONS_FROM_ORG_ERROR				= ROOT_KEY.concat("get.persons.by.organisation.error");
	private String GET_CLIENTS_FROM_ORG_ERROR				= ROOT_KEY.concat("get.clients.by.organisation.error");
	private String GET_CLIENT_ERROR							= "client.get.error";
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private final String PROJECT_ID							= "projectId";	
	private final String CLIENT_ID							= "clientId";
	private final String CMD_ADD_PROJECT_FROM_CLIENT_FORM 	= "ADD_PROJECT_FROM_CLIENT_FORM";	
	
	//------------------------OTHER PARAMETERS---------------------------------------------------------			
	private static final String JSON_PERSONS 			= "JSON_PERSONS";
	private static final String JSON_CLIENTS			= "JSON_CLIENTS";
	private final String MODEL_MANAGER_NAME				= "MANAGER_NAME";	
	private final String MODEL_CLIENT_NAME				= "CLIENT_NAME";	
	private final String STATUS							= "STATUS";
	private final String STATUS_PROJECT_TEAM			= "STATUS_PROJECT_TEAM";	
	private final String ORGANIZATION_ID				= "ORGANIZATION_ID";
	private final String ALL_PERSONS 					= "ALL_PERSONS";	
	private final String PROJECT_TEAM_MANAGERID			= "PROJECT_TEAM_MANAGERID";
	private static final String MEMBERS					= "members";
	private static final String NUMBER_OF_MEMBERS		= "nrOfMembers";
	private static final String USER_ID					= "USER_ID";
	
	//--------------------BACK PARAMETERS-------------------------------------------------------------
	private static final String BACK_URL 				= "BACK_URL";
	private static final String NEXT_BACK_URL			= "NEXT_BACK_URL";
	private static final String ENCODE_BACK_URL	 		= "ENCODE_BACK_URL";
		
	public ProjectController() {
		
		setCommandName("projectProjectTeamBean");
		setCommandClass(ProjectProjectTeam.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);		
	}
	

	/**
	 * Bindings for request parameters
	 * 
	 * @author Adelina
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		logger.debug("start - initBinder");
		
		binder.registerCustomEditor(Set.class, "projectTeam.persons", new CustomCollectionEditor(Set.class){
			protected Object convertElement(Object element){
				if(element != null){
					Integer personId = new Integer((String)element);
					logger.debug("person : convertElement for Id " + personId);
					Person person = null;
					try{
						person =  BLPerson.getInstance().get(personId);			
					} catch(BusinessException e){						
						logger.debug("There was an error");
					}
					logger.debug("start - initBinder");
					return person;
				}
				logger.debug("start - initBinder");
				return null;
			}			
		});		
	}
	
			
	/**
	 * It runs at every cycle
	 * 
	 * @author Adelina
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("formBackingObject - START");
				
		// used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
		// locale 
		Locale locale = RequestContextUtils.getLocale(request);		
		
		// backing object
		ProjectProjectTeam projectProjectTeam = new ProjectProjectTeam();
		
		try{			
			// backing object id
			Integer projectId = -1;		
			
			String pid = ServletRequestUtils.getStringParameter(request, PROJECT_ID);			
			if(pid != null) {
				projectId = Integer.parseInt(pid);				
			} 

			request.setAttribute(PROJECT_ID, projectId);			
			logger.debug("projectId = ".concat(String.valueOf(projectId)));
			
			// redirecting regarding the operation
			String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
			logger.debug("action = ".concat(action));
					
			if(action != null) {
				// check if i have to edit a project
				if(IConstant.CMD_GET.equals(action)) {
					logger.debug("formBackingObject: Get");		
					projectProjectTeam.setProject(handleGet(projectId, request, errorMessages, infoMessages, locale));
					projectProjectTeam.setProjectTeam(handleGetProjectTeam(projectId, request, errorMessages, infoMessages, locale));				
				} else if(IConstant.CMD_ADD.equals(action)) { // or add a new project
					logger.debug("formBackingObject: New");
					projectProjectTeam.setProject(new Project()); 
					projectProjectTeam.setProjectTeam(new ProjectTeam()); 					
				} else if(CMD_ADD_PROJECT_FROM_CLIENT_FORM.equals(action)) {
					logger.debug("formBackingObject : New from Client");
					Integer clientId = ServletRequestUtils.getIntParameter(request, CLIENT_ID);
					request.setAttribute(CLIENT_ID, clientId);
					Project project = new Project();
					project.setClientId(clientId);
					projectProjectTeam.setProject(project);
					projectProjectTeam.setProjectTeam(new ProjectTeam()); 	
				}
			}
		} catch (Exception e) {
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("formBackingObject END command ".concat(projectProjectTeam.toString()));
		
		return projectProjectTeam;
	}
	
	/**
	 * It runs on every submit (request Method = POST)
	 * It detects the case by action parameter and dispatch to and appropriate handler.
	 * 
	 * @author Adelina
	 */
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		logger.debug("onSubmit - START");
		
		// for model and view
		ModelAndView mav = new ModelAndView();
		//for locale
		Locale locale = RequestContextUtils.getLocale(request);
		
		//used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
		// the command class
		ProjectProjectTeam projectProjectTeam = (ProjectProjectTeam) command;
		Project project = projectProjectTeam.getProject();
		ProjectTeam projectTeam = projectProjectTeam.getProjectTeam();
		projectTeam.setProjectId(project.getProjectId());
		
		// redirecting regarding the operation
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		logger.debug("action = ".concat(action));
		
		if(action != null) {
			if(IConstant.CMD_SAVE.equals(action)) {
				if(project.getProjectId() > 0) {
					project = handleUpdate(request, response, project, errors, errorMessages, infoMessages, locale);
					handleUpdateProjectTeam(request, response, projectTeam, project, errors, errorMessages, infoMessages, locale);					
				} else {
					project = handleAdd(request, response, project, errors, errorMessages, infoMessages, locale);					
					request.setAttribute(PROJECT_ID, project.getProjectId());							
					handleAddProjectTeam(request, response, projectTeam, project, errors, errorMessages, infoMessages, locale);
				}				
				mav = new ModelAndView(getSuccessView());
				mav.addObject(getCommandName(), projectProjectTeam);
			} 
		}
				
		//Publish Info/Error messages
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		//Add referenceData objects to model
		mav.addAllObjects(referenceData(request, command, errors));
		
		logger.debug("onSubmit - END");
		
		return mav;
	}
	
	/**
	 * Adds a project
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private Project handleAdd(HttpServletRequest request, HttpServletResponse response, Project project, BindException errors, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("handleAdd - START");		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try{		
			// adding the project to the database
			project = BLProject.getInstance().addAll(project);			
									
			String projectName = ControllerUtils.getInstance().tokenizeField(project.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
			infoMessages.add(messageSource.getMessage(ADD_SUCCESS, new Object[] {projectName}, locale));

			//add the new audit event only if the user is not AdminIT
			try {
				if (!userAuth.isAdminIT()){
					BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PROJECT_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
							messageSource.getMessage(IConstant.AUDIT_EVENT_PROJECT_ADD_MESSAGE, new Object[] {project.getName()}, new Locale("en")),
							messageSource.getMessage(IConstant.AUDIT_EVENT_PROJECT_ADD_MESSAGE, new Object[] {project.getName()}, new Locale("ro")),  
							ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
				}
			} catch (Exception exc) {
				logger.error("", exc);
			}
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
									        	
		logger.debug("handleAdd - END");
		
		return project;
	}
	
	/**
	 * Adds a project team
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private void handleAddProjectTeam(HttpServletRequest request, HttpServletResponse response, ProjectTeam projectTeam, Project project, BindException errors, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("handleAdd - START");						
		try{		
			projectTeam.setStatus(IConstant.NOM_PROJECT_TEAM_STATUS_ACTIVATED);	
			projectTeam.setProjectId(project.getProjectId());
			Set<Person> persons = projectTeam.getPersons();			
			if(persons == null || persons.size() == 0) {
				persons = new HashSet<Person>();
			} 
			persons.add(project.getManager());			
			projectTeam.setPersons(persons);	
			
			String jsonString = ServletRequestUtils.getStringParameter(request, MEMBERS);
			logger.debug("jsonString = " + jsonString);
			
			Integer nrOfMembers = ServletRequestUtils.getIntParameter(request, NUMBER_OF_MEMBERS);
			logger.debug("nrOfMembers = " + nrOfMembers);
			
			if(jsonString == "") {
				jsonString="{}";
			}
			
			JSONObject JObject = new JSONObject(jsonString);
			
			List<TeamMember> externalMembers = new ArrayList<TeamMember>();
			
			if(nrOfMembers > 0) {
				for(int i = 0; i < nrOfMembers; i++) {
					JSONArray object = (JSONArray)JObject.get("members");
					TeamMember member = new TeamMember();
					
					Class clazz = member.getClass();
					
					//get all object fields
					Field[] fields = clazz.getDeclaredFields();
					Integer nrOfFields = fields.length;						
																									
					member.setFirstName((String)(object.getJSONObject(i).get(fields[1].getName())));
					member.setLastName((String)(object.getJSONObject(i).get(fields[2].getName())));
					member.setEmail((String)(object.getJSONObject(i).get(fields[3].getName())));
					member.setAddress((String)(object.getJSONObject(i).get(fields[4].getName())));							
					member.setPhone((String)(object.getJSONObject(i).get(fields[5].getName())));
					member.setObservation((String)(object.getJSONObject(i).get(fields[6].getName())));
					member.setDescription((String)(object.getJSONObject(i).get(fields[7].getName())));
					member.setPersonId(new Integer(0));				
					member.setStatus((byte)1);					
					
					logger.debug("team member = " + member);
					logger.debug("isExternal = " + member.getIdExternal());
					externalMembers.add(member);
				}
			}						
			// adding the project team to the database
			BLProjectTeam.getInstance().add(projectTeam, externalMembers);	
					
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
									        	
		logger.debug("handleAdd - END");		
	}
	
	/**
	 * Updates a project
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private Project handleUpdate(HttpServletRequest request, HttpServletResponse response, Project project, BindException errors, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("handleUpdate - START");													
		try{			
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
			
			if(project.isProjectNoClient()) {
				project.setClientId(new Integer(-1));
			}
						
			// updating the project to the database
			project = BLProject.getInstance().updateAll(project);				
						
			if(project.getClientId() != null && project.getClientId() == -1) {
				project.setProjectNoClient(true);
			} else {
				project.setProjectNoClient(false);
			}
			
			String projectName = ControllerUtils.getInstance().tokenizeField(project.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
			infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, new Object[] {projectName}, locale));			
			
			//add the new audit event only if the user is not AdminIT
			try {
				if (!userAuth.isAdminIT()){
					BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PROJECT_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
							messageSource.getMessage(IConstant.AUDIT_EVENT_PROJECT_UPDATE_MESSAGE, new Object[] {project.getName()}, new Locale("en")),
							messageSource.getMessage(IConstant.AUDIT_EVENT_PROJECT_UPDATE_MESSAGE, new Object[] {project.getName()}, new Locale("ro")),  
							ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
				}
			} catch (Exception exc) {
				logger.error("", exc);
			}
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		logger.debug("handleUpdate - END");			
		return project;
	}
	
	/**
	 * Updates a project team
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private void handleUpdateProjectTeam(HttpServletRequest request, HttpServletResponse response, ProjectTeam projectTeam, Project project, BindException errors, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("handleUpdate - START");					
		try{
			Set<Person> persons = projectTeam.getPersons();		
			persons.add(project.getManager());			
			projectTeam.setPersons(persons);			
			
			String jsonString = ServletRequestUtils.getStringParameter(request, MEMBERS);
			logger.debug("jsonString = " + jsonString);
			
			Integer nrOfMembers = ServletRequestUtils.getIntParameter(request, NUMBER_OF_MEMBERS);
			logger.debug("nrOfMembers = " + nrOfMembers);
			
			if(jsonString == "") {
				jsonString="{}";
			}
			
			JSONObject JObject = new JSONObject(jsonString);
			
			List<TeamMember> externalMembers = new ArrayList<TeamMember>();
			
			if(nrOfMembers > 0) {
				for(int i = 0; i < nrOfMembers; i++) {
					JSONArray object = (JSONArray)JObject.get("members");
					TeamMember member = new TeamMember();
					
					Class clazz = member.getClass();
					
					//get all object fields
					Field[] fields = clazz.getDeclaredFields();
					Integer nrOfFields = fields.length;						
					
					String memberId = (String)(object.getJSONObject(i).get(fields[0].getName()));
					logger.debug("memberId = " + memberId);
					member.setMemberId(Integer.valueOf(memberId));
					member.setFirstName((String)(object.getJSONObject(i).get(fields[1].getName())));
					member.setLastName((String)(object.getJSONObject(i).get(fields[2].getName())));
					member.setEmail((String)(object.getJSONObject(i).get(fields[3].getName())));
					member.setAddress((String)(object.getJSONObject(i).get(fields[4].getName())));							
					member.setPhone((String)(object.getJSONObject(i).get(fields[5].getName())));
					member.setObservation((String)(object.getJSONObject(i).get(fields[6].getName())));
					member.setDescription((String)(object.getJSONObject(i).get(fields[7].getName())));
					member.setPersonId(new Integer(0));				
					member.setStatus((byte)1);					
					member.setProjectTeamId(projectTeam.getProjectTeamId());	
					logger.debug("team member = " + member);
					logger.debug("isExternal = " + member.getIdExternal());
					externalMembers.add(member);
				}
			}		
			logger.debug("externalMembers = " + externalMembers);
			// updating the project team to the database
			BLProjectTeam.getInstance().update(projectTeam, externalMembers);

		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		logger.debug("handleUpdate - END");		
	}
	
	/**
	 * Gets a project
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @param request
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 */
	private Project handleGet(Integer projectId, HttpServletRequest request, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) {
		logger.debug("handleGet - START");
		Project project = null;
		try{
			// call the get method from the business layer
			project = BLProject.getInstance().getWithAll(projectId);
			Person manager = project.getManager();
			logger.debug("manager = " + manager);
						
			
			// setting a blank manager if it hasn't any
			if(manager == null) {
				project.setManager(new Person());
			}
									
			if(project.getClientId() != null && project.getClientId() == -1) {
				project.setProjectNoClient(true);
			} else {
				project.setProjectNoClient(false);
			}
						
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		logger.debug("handleGet - END");
		return project;
	}		
	
	
	/**
	 * Gets a project team
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @param request
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 */
	private ProjectTeam handleGetProjectTeam(Integer projectId, HttpServletRequest request, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) {
		logger.debug("handleGet - START");
		ProjectTeam projectTeam = null;
		try{
			// call the get method from the business layer
			projectTeam = BLProjectTeam.getInstance().get(projectId, false);				
			logger.debug("projectTeam = " + projectTeam);						
			
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		logger.debug("handleGet - END");
		return projectTeam;
	}
		
	/*/* (non-Javadoc)
	 * @see ro.cs.om.web.controller.root.RootSimpleFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
	 */	
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		logger.debug("referenceData - START");
		
		//used as a container for error messages
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		Map map = new HashMap();		
		
		// the command class
		ProjectProjectTeam projectProjectTeam = (ProjectProjectTeam) command;
			
		// put the projectId attribute on request
		Integer projectId = (Integer)request.getAttribute(PROJECT_ID);		
		request.setAttribute(PROJECT_ID, projectId);
		
		// adding to model the action from the request
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		map.put(IConstant.REQ_ACTION, action);
		
		// put the back url
		String backUrl = ServletRequestUtils.getStringParameter(request, BACK_URL);		
		String servletPath = request.getServletPath();		
		String nextBackUrl = URLEncoder.encode(servletPath.substring(1, servletPath.length()).concat("?").concat(request.getQueryString()), "UTF-8");

		map.put(BACK_URL, backUrl);		
		map.put(NEXT_BACK_URL, nextBackUrl);
		map.put(ENCODE_BACK_URL, URLEncoder.encode(backUrl, "UTF-8"));
		
		// the id of the organization
		Integer organizationId = (Integer)request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		map.put(ORGANIZATION_ID, organizationId);
										
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
		
		map.put(USER_ID, userAuth.getPersonId());
		
		// if we have the permission to see all the projects by different managers
		// choose for a project manager for the project
		if(userAuth.hasAuthority(PermissionConstant.getInstance().getCM_ProjectAdvancedAdd())){		
			
			// list the persons for the organization that is project manager
			List<UserSimple> usersPM = new ArrayList<UserSimple>();
			try{
				usersPM = BLUser.getInstance().getUsersSimpleByOrganizationId(organizationId, true);
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
					getPersonsFirstNameLastNameFromOrgAsJSON(usersPM, RequestContextUtils.getLocale(request), 
							errorMessages, messageSource));		
		} else {
			if(projectProjectTeam.getProject().getProjectId() <= 0) {
				projectProjectTeam.getProject().setManagerId(userAuth.getPersonId());
				projectProjectTeam.getProject().setManager(BLPerson.getInstance().get(userAuth.getPersonId()));
			}
		}				
		
		// manager name
		logger.debug("manager name");
		if(projectProjectTeam.getProject().getManagerId() != null) {
			logger.debug("project.getManagerId()" + projectProjectTeam.getProject().getManagerId());
			Person manager = projectProjectTeam.getProject().getManager();
			logger.debug("manager = " + manager);
			if(manager != null && manager.getPersonId() > 0) {								
				map.put(MODEL_MANAGER_NAME, manager.getFirstName().concat(" ").concat(manager.getLastName()));			
			}			
		}		
		
		// get the clients for the certain organization		
		List<Client> clients = new ArrayList<Client>();
		try{
			clients = BLClient.getInstance().getClientsByOrganizationId(organizationId);
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_CLIENTS_FROM_ORG_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		map.put(JSON_CLIENTS, ControllerUtils.getInstance().
		getClientsFromOrgAsJSON(clients, RequestContextUtils.getLocale(request), 
				errorMessages, messageSource, false));	
		
		// client name
		logger.debug("client name");
		Integer clientId = projectProjectTeam.getProject().getClientId();
		logger.debug("clientId = " + clientId);
		if(clientId != null){
			if(clientId > 0) {
				logger.debug("project.getClientId() = " + projectProjectTeam.getProject().getClientId());
				Client client = new Client();
				try{
					client = BLClient.getInstance().get(projectProjectTeam.getProject().getClientId());
				} catch (BusinessException be) {
					logger.error("", be);
					errorMessages.add(messageSource.getMessage(GET_CLIENT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
							.getLocale(request)));
				} catch (Exception e){
					logger.error("", e);
					errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
							.getLocale(request)));
				}
				logger.debug("client = " + client);
				if(client != null && client.getClientId() > 0) {			
					if(client.getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
						map.put(MODEL_CLIENT_NAME, client.getC_name());
					} else {
						map.put(MODEL_CLIENT_NAME, client.getP_firstName().concat(" ").concat(client.getP_lastName()));
					}
					
				}
			} 
		}
										
		//Adding to model the status of the issue
		map.put(STATUS, projectProjectTeam.getProject().getStatus());
				
		// list with all the persons from the organization
		List<UserSimple> users = new ArrayList<UserSimple>();
		
		try {
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
		
		logger.debug("users by organizationId = " + users);
		
		projectId = projectProjectTeam.getProjectTeam().getProjectId();
		logger.debug("projectTeam projectId = " + projectId);
		
		if(projectId != null) {
		
			Project project = BLProject.getInstance().getWithManger(projectId);
			map.put(CLIENT_ID, project.getClientId());
							
			// adding to model the id of the manager of the project
			map.put(PROJECT_TEAM_MANAGERID, project.getManagerId())	;	
			
			List<UserSimple> newUsers = new ArrayList<UserSimple>();
		
			for(TeamMember member : projectProjectTeam.getProjectTeam().getTeamMembers()) {
				if(member.getStatus() != IConstant.NOM_TEAM_MEMBER_STATUS_DELETED) {
					UserSimple userSimple = new UserSimple();
					userSimple.setUserId(member.getPersonId());
					newUsers.add(userSimple);
				}
			}
			
			users.removeAll(newUsers);
			
			logger.debug("newUsers = " + newUsers);
			logger.debug("users = " + users);
			
			map.put(STATUS_PROJECT_TEAM, projectProjectTeam.getProjectTeam().getStatus());		
		
		} else {
			map.put(PROJECT_TEAM_MANAGERID, new Integer(0));	
			map.put(STATUS_PROJECT_TEAM, new Integer(1));	
		}
		
		if(users != null) {
			map.put(ALL_PERSONS, users);
		} else {
			map.put(ALL_PERSONS, new HashSet<Person>());
		}						
						
		setErrors(request, errorMessages);
		
		logger.debug("referenceData - END");
		return map;
	}
	
	

}
