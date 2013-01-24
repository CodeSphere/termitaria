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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLTeamMember;
import ro.cs.ts.business.BLTeamMemberDetail;
import ro.cs.ts.cm.Project;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.entity.TeamMemberDetail;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootAbstractController;
import ro.cs.ts.web.security.UserAuth;

public class TeamMemberViewController extends RootAbstractController {

	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY								= "team.member";		
	private final String GET_ERROR								= ROOT_KEY.concat("get.error");	
	private final String ADD_ERROR								= "team.member.detail.add.error";
	private final String GENERAL_ERROR							= "team.member.detail.general.error";	
	
	
	//------------------------VIEW------------------------------------------------------------------
	private static final String VIEW 							= "TeamMemberView";	

	//------------------------MODEL------------------------------------------------------------------	
	private static final String MEMBER_ID						= "memberId";
	private static final String PROJECT_ID						= "projectId";
	private static final String TEAM_MEMBER						= "TEAM_MEMBER";	
	private static final String TEAM_MEMBER_DETAIL				= "TEAM_MEMBER_DETAIL";
	private static final String PROJECT							= "PROJECT";
	private static final String IS_MANAGER						= "IS_MANAGER";
	
	// Number of characters that fit in a line, for the display panel,
    // if there are big words
    public static final Integer NR_CHARS_PER_LINE				= 40;	   
	
    /**
	 * Contstructor - with the basic settings for the controller.
	 * @author Adelina
	 */
	public TeamMemberViewController() {
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
		
		TeamMember teamMember = new TeamMember();
		
		TeamMemberDetail teamMemberDetail = new TeamMemberDetail();
		
		Project project = null;
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try{
			Integer memberId = ServletRequestUtils.getIntParameter(request, MEMBER_ID);			
			logger.debug("MemberId Id = ".concat(memberId.toString()));
			
			Integer projectId = ServletRequestUtils.getIntParameter(request, PROJECT_ID);
			logger.debug("Project Id = ".concat(projectId.toString()));
														
			if(projectId != null && projectId > 0) {
				project = BLProject.getInstance().get(projectId, true);
			}
			
			// checks if the user that logs in is the manager of the project
			boolean isManager = isManager(project, userAuth.getPersonId(), request, errorMessages);
			mav.addObject(IS_MANAGER, isManager);
						
			if(memberId != null) {
				// call the get method from the business layer
				teamMember = BLTeamMember.getInstance().getTeamMember(memberId, false);
	    		logger.debug("teamMember = " + teamMember);
					    															
				teamMember.setFirstName(ControllerUtils.getInstance().tokenizeField(teamMember.getFirstName(), NR_CHARS_PER_LINE));
				teamMember.setLastName(ControllerUtils.getInstance().tokenizeField(teamMember.getLastName(), NR_CHARS_PER_LINE));
				
				teamMemberDetail = BLTeamMemberDetail.getInstance().getWithAllByTeamMember(memberId);
				logger.debug("teamMemberDetail = " + teamMemberDetail);
				if(teamMemberDetail == null) {
					teamMemberDetail = handleAdd(memberId, project, request, errorMessages);
					logger.debug("teamMemberDetail = " + teamMemberDetail);
				}
				
				if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_TeamMemberView()) || isManager){	
					logger.debug("hasAuthority");
					if(teamMemberDetail != null) {
						if(teamMemberDetail.getObservation() != null) {
							teamMemberDetail.setObservation(ControllerUtils.getInstance().tokenizeField(teamMemberDetail.getObservation(), NR_CHARS_PER_LINE));
						}
						
						mav.addObject(TEAM_MEMBER_DETAIL, teamMemberDetail);
					}
				}
				
				mav.addObject(TEAM_MEMBER, teamMember);	
				mav.addObject(PROJECT, project);
			}
				
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
	 * Checks if a specific person is the manager for a specific project
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @param personId
	 * @param request
	 * @param errorMessages
	 * @return
	 */
	boolean isManager(Project project, Integer personId, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("isManager - START");
		
		boolean isManager = false;
				
		if(personId == project.getManagerId()) {
			isManager = true;
		}
		
		logger.debug("isManager - END, with isManager = " + isManager);
		return isManager;		

	}
	/**
	 * Adds the team member details
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @param project
	 * @param request
	 * @param errorMessages
	 * @return
	 */
	private TeamMemberDetail handleAdd(Integer memberId, Project project, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("handleAdd - START");
				
		Integer teamMemberDetailId = null;
		TeamMemberDetail teamMemberDetail = null;
									
		try{	
			// the user that logs in
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();				
			boolean isManager = isManager(project, userAuth.getPersonId(), request, errorMessages);
			
			// if the user has permission TS_ProjectAddUpdateTeamMemberDetails, can add the project's team member's details
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ProjectAddUpdateTeamMemberDetails()) || isManager){												
				teamMemberDetail = new TeamMemberDetail();
				teamMemberDetail.setTeamMemberId(memberId);
				teamMemberDetail.setStatus(IConstant.NOM_TEAM_MEMBER_DETAIL_STATUS_ACTIVE);			
				// adding the team member details to the database
				teamMemberDetailId = BLTeamMemberDetail.getInstance().add(teamMemberDetail);			
				teamMemberDetail.setTeamMemberDetailId(teamMemberDetailId);
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
		return teamMemberDetail;
	}

}
