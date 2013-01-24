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
package ro.cs.cm.web.controller.general;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.cm.business.BLProject;
import ro.cs.cm.business.BLTeamMember;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.entity.Project;
import ro.cs.cm.entity.ProjectTeam;
import ro.cs.cm.entity.TeamMember;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.web.controller.root.ControllerUtils;
import ro.cs.cm.web.controller.root.RootAbstractController;

/**
 * 
 * @author Adelina
 *
 */
public class TeamMemberListingController extends RootAbstractController{
		
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY								= "team.member.";
	private final String DELETE_SUCCESS							= ROOT_KEY.concat("delete.success");
	private final String ADD_SUCCESS							= ROOT_KEY.concat("add.success");
	private final String UPDATE_SUCCESS							= ROOT_KEY.concat("update.success");
	
	private final String GET_ERROR								= ROOT_KEY.concat("get.error");	
	private final String GET_EXTERNAL_MEMBERS_ERROR				= ROOT_KEY.concat("get.external.error");
	private final String GENERAL_ERROR							= ROOT_KEY.concat("general.error");
	private final String GET_PROJECT_ERROR						= "project.get.error";
	
	private final String CMD_GET_FROM_ADD						= "GET_FROM_ADD";
	private final String CMD_ACTION_TYPE						= "ACTION_TYPE";
	
	//------------------------VIEW------------------------------------------------------------------
	private static final String VIEW 							= "TeamMemberListing";	
	private static final String TEAM_MEMBERS					= "TEAM_MEMBERS";				
	private static final String PROJECT_ID						= "projectId";	
	private static final String MEMBERS							= "members";
	private static final String NUMBER_OF_MEMBERS				= "nrOfMembers";
	private static final String FIRST_NAME						= "firstName";
	private static final String LAST_NAME						= "lastName";	
		
	
	/**
	 * Contstructor - with the basic settings for the controller.
	 * @author Adelina
	 */
	public TeamMemberListingController() {
		setView(VIEW);		
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse result) throws Exception {
		logger.debug("handleRequestInternal - START");
		
		// used for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
				
		ModelAndView mav = new ModelAndView();
		
		List<TeamMember> members = new ArrayList<TeamMember>();
				
		
		try{						
			// redirecting regarding the operation
			String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
			logger.debug("action = ".concat(action));
			
			Integer projectId = ServletRequestUtils.getIntParameter(request, PROJECT_ID);
			logger.debug("projectId = " + projectId);
			
			Integer projectTeamId = new Integer(-1);
														
			if(projectId != null && projectId > 0) {
				Project project = getProject(projectId, errorMessages, request);
				logger.debug("project = " + project);
				ProjectTeam team = project.getProjectTeam();
				if(team != null) {
					projectTeamId = team.getProjectTeamId();
				}
			}
			
			logger.debug("project team id = " + projectTeamId);
			
			// add on mav the projectTeamId
			mav.addObject(PROJECT_ID, projectId);
															
			if((action != null)) {		
				
				if(projectTeamId > 0) {
					// gets the external members
					members = handleGetExternalTeamMembersByProjectTeam(projectTeamId, errorMessages, request);
					logger.debug("get external members from data base = " + members);
					
				}
				
				if(CMD_GET_FROM_ADD.equals(action)) {																			
					String jsonString = ServletRequestUtils.getStringParameter(request, MEMBERS);
					logger.debug("jsonString = " + jsonString);
					
					Integer nrOfMembers = ServletRequestUtils.getIntParameter(request, NUMBER_OF_MEMBERS);
					logger.debug("nrOfMembers = " + nrOfMembers);														
					JSONObject JObject = new JSONObject(jsonString);
					
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
							member.setStatus((byte)1);															
							member.setFirstName((String)(object.getJSONObject(i).get(fields[1].getName())));
							member.setLastName((String)(object.getJSONObject(i).get(fields[2].getName())));
							member.setEmail((String)(object.getJSONObject(i).get(fields[3].getName())));
							member.setAddress((String)(object.getJSONObject(i).get(fields[4].getName())));							
							member.setPhone((String)(object.getJSONObject(i).get(fields[5].getName())));
							member.setObservation((String)(object.getJSONObject(i).get(fields[6].getName())));
							member.setDescription((String)(object.getJSONObject(i).get(fields[7].getName())));
							member.setPersonId(new Integer(0));
							member.setProjectTeamId(projectTeamId);							
							member.setIdExternal(object.getJSONObject(i).getInt("id"));
							
							logger.debug("team member = " + member);
							logger.debug("isExternal = " + member.getIdExternal());
							
							// if the member already exists in the database and comes in within the JSON object
							// remove from the members, so the user, can see only the latest version (updated) of the team member
							if(member.getMemberId() > 0 && members != null && members.size() > 0 && members.contains(member)) {								
								members.remove(member);
							}
									
							// if the member has an empty firstName and an empty lastName means that the
							// member already exists in the database, but we want to delete it, so we simulate this process
							// by not adding the team member to the list of members
							if(!member.getFirstName().isEmpty() && !member.getLastName().isEmpty()) {
								members.add(member);
							}
						}
					}
										
					String actionType = request.getParameter(CMD_ACTION_TYPE);
					String firstName = request.getParameter(FIRST_NAME);
					String lastName = request.getParameter(LAST_NAME);
					String teamMemberName = ControllerUtils.getInstance().tokenizeField(firstName.concat(" ").concat(lastName), IConstant.NR_CHARS_PER_LINE_MESSAGE);
					logger.debug("Building message with action type: "+actionType+" FirstName: "+firstName+" LastName: "+lastName);
					
					if (actionType.equals(IConstant.CMD_ADD)){
						infoMessages.add(messageSource.getMessage(ADD_SUCCESS, new Object[] {teamMemberName}, RequestContextUtils.getLocale(request)));
					}else if (actionType.equals(IConstant.CMD_DELETE)){
						infoMessages.add(messageSource.getMessage(DELETE_SUCCESS, new Object[] {teamMemberName}, RequestContextUtils.getLocale(request)));
					}else if (actionType.equals(IConstant.CMD_UPDATE)){
						infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, new Object[] {teamMemberName}, RequestContextUtils.getLocale(request)));
					}									
				} 
			}
			
			logger.debug("members size = " + members.size() + " ,members = " + members);			
			// add them on mav
			mav.addObject(TEAM_MEMBERS, members);
			
		} catch(ServletRequestBindingException e){
			logger.error("handleRequestInternal", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}	
				
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);		
		
		logger.debug("handleRequestInternal - END");
		
		return mav;
	}		
	
	/**
	 * Gets the external team members identified by the projectTeamId
	 * 
	 * @author Adelina
	 * 
	 * @param projectTeamId
	 * @param errorMessages
	 * @param request
	 * @return
	 */
	private List<TeamMember> handleGetExternalTeamMembersByProjectTeam(Integer projectTeamId, ArrayList<String> errorMessages, HttpServletRequest request) {
		logger.debug("handleGetNewTeamMembersByProjectTeam - START");
		List<TeamMember> members = null;
		try{
			members = BLTeamMember.getInstance().getNewTeamMembersByProjectTeam(projectTeamId);
		} catch(BusinessException be){
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(GET_EXTERNAL_MEMBERS_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		logger.debug("handleGetNewTeamMembersByProjectTeam - END");
		return members;
	}
	
	/**
	 * Get project by projectId
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @param errorMessages
	 * @param request
	 * @return
	 */
	private Project getProject(Integer projectId, ArrayList<String> errorMessages, HttpServletRequest request) {
		logger.debug("getProject -  START");
		Project project = null;
		try{
			project = BLProject.getInstance().getForView(projectId);
		} catch(BusinessException be){
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(GET_PROJECT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		logger.debug("getProject - END");
		return project;
	}
}
