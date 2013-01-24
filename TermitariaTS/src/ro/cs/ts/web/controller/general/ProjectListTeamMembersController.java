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
package ro.cs.ts.web.controller.general;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLProject;
import ro.cs.ts.cm.Project;
import ro.cs.ts.cm.ProjectTeam;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.utils.TeamMemberComparator;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootAbstractController;
import ro.cs.ts.web.security.UserAuth;

public class ProjectListTeamMembersController extends RootAbstractController {
	
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY								= "project.";		
	private final String GENERAL_ERROR							= ROOT_KEY.concat("general.error");
	private final String GET_PROJECT_ERROR						= ROOT_KEY.concat("project.get.error");
		
	//------------------------VIEW------------------------------------------------------------------	
	private static final String VIEW							= "Project_ListTeamMembers";
	private static final String TEAM_MEMBERS					= "TEAM_MEMBERS";
	private static final String PROJECT_NAME					= "PROJECT_NAME";
	private static final String IS_MANAGER						= "IS_MANAGER";
	private static final String PROJECT_ID						= "projectId";		
	private static final String PROJECT							= "project";
	
	//--------------------BACK PARAMETERS-------------------------------------------------------------
	private static final String BACK_URL 				= "BACK_URL";
	private static final String NEXT_BACK_URL			= "NEXT_BACK_URL";	
	
	// Number of characters that fit in the panel display header
    // if there are big words
    public static final Integer NR_CHARS_PANEL_HEADER	= 45;
			
	public ProjectListTeamMembersController() {
		setView(VIEW);
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("handleRequestInternal - START ");
		
		ModelAndView mav = new ModelAndView(getView());
		
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		
		// adding to model the action from the request
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		logger.debug("action = " + action);
		request.setAttribute(IConstant.REQ_ACTION, action);
		
		Integer projectId = ServletRequestUtils.getIntParameter(request,PROJECT_ID);
		logger.debug("Project Id = ".concat(String.valueOf(projectId)));
								
		Set<TeamMember> teamMembers = new HashSet<TeamMember>();
		
		if(projectId != null) {
			// get all the team members for the project
			Project project = null;
			try{
				project = BLProject.getInstance().get(projectId, true);
				if(project != null) {
					ProjectTeam projectTeam = project.getProjectTeam();
					if(projectTeam != null) {
						teamMembers = projectTeam.getTeamMembers();
					}
					request.setAttribute(PROJECT, project);
				}	
			} catch(BusinessException be) {
				logger.error("", be);
				errorMessages.add(messageSource.getMessage(GET_PROJECT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
						.getLocale(request)));
			} catch (Exception e){
				logger.error("", e);
				errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
						.getLocale(request)));
			}
			

			for(TeamMember member : teamMembers) {
				String panelHeaderName = ControllerUtils.getInstance().truncateName(member.getFirstName().concat(" ").concat(member.getLastName()), NR_CHARS_PANEL_HEADER);
				member.setPanelHeaderName(panelHeaderName);
			}	
			
			TreeSet<TeamMember> sortedTeamMembers = new TreeSet<TeamMember>(TeamMemberComparator.getInstance().lastNameComparator());
			sortedTeamMembers.addAll(teamMembers);
			
			// put on mav the team members that belongs to that project
			mav.addObject(TEAM_MEMBERS, sortedTeamMembers);			
	
			// put on mav the project name			
			mav.addObject(PROJECT_NAME, project.getName());
			
			
			// checks if the the user that logs in is the manager of the project						
			boolean isManager = false;
			
			if(userAuth.getPersonId() == project.getManagerId()) {
				isManager = true;
			}
			
			// adding to model the fact the user that logs in is the manager of the project or not
			mav.addObject(IS_MANAGER, isManager);
						
			
			//put the back url
			String backUrl = ServletRequestUtils.getStringParameter(request, BACK_URL);
			
			String servletPath = request.getServletPath();		
			String nextBackUrl = URLEncoder.encode(servletPath.substring(1, servletPath.length()).concat("?").concat(request.getQueryString()), "UTF-8");

			logger.debug("BACK_URL = " + backUrl);
			logger.debug("NEXT_BACK_URL = " + nextBackUrl);		
			
			mav.addObject(BACK_URL, backUrl);		
			mav.addObject(NEXT_BACK_URL, nextBackUrl);								
		}
				
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);	
		
		logger.debug("handleRequestInternal - END");
		return mav;
	}

}
