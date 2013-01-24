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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.cm.business.BLTeamMember;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.entity.TeamMember;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.web.controller.root.ControllerUtils;
import ro.cs.cm.web.controller.root.RootSimpleFormController;

public class TeamMemberController extends RootSimpleFormController{
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY								= "team.member.";
	
	private final String ADD_SUCCESS							= ROOT_KEY.concat("add.success");
	private final String UPDATE_SUCCESS							= ROOT_KEY.concat("update.success");
	private final String ADD_ERROR								= ROOT_KEY.concat("add.error");			
	private final String UPDATE_ERROR							= ROOT_KEY.concat("update.error");	
	private final String GENERAL_ERROR							= ROOT_KEY.concat("general.error");
	private final String GET_ERROR								= ROOT_KEY.concat("get.error");
			
	//------------------------VIEW------------------------------------------------------------------
	private static final String FORM_VIEW 						= "TeamMember";
	private static final String SUCCESS_VIEW 					= "TeamMember";			
	
	//---------------------COLATERAL-VIEWS------------------------------------------------------------	
	private static final String LISTING_VIEW 					= "TeamMemberListing";	
			
	private static final String PROJECT_ID						= "projectId";
	private static final String TEAM_MEMBER_ID					= "memberId";	
	private static final String TEAM_MEMBERS					= "TEAM_MEMBERS";	
	
	/**
	 * Contstructor - with the basic settings for the controller.
	 */
	public TeamMemberController() {
		// for use in .jsp
		setCommandName("teamMemberBean"); 
		setCommandClass(TeamMember.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
	
	/**
	 * formBackingObject - for every request
	 * 
	 * @author Adelina 
	 * @param request
	 */	
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception{
		// logger debug
		logger.debug("formBackingObject- START");
		
		// used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
						
		// backing object
		TeamMember member = new TeamMember();
		
		Integer projectId = ServletRequestUtils.getIntParameter(request, PROJECT_ID);
		request.setAttribute(PROJECT_ID, projectId);
						
		Integer memberId = ServletRequestUtils.getIntParameter(request, TEAM_MEMBER_ID);
						
		// redirecting regarding the operation
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		logger.debug("action = ".concat(action));
		
		if(action != null) {
			if(IConstant.CMD_ADD.equals(action)) { // add a new team member
				logger.debug("formBackingObject: New");				
				//member.setProjectTeamId(projectTeamId);
			} else if(IConstant.CMD_GET.equals(action)) { // get a team meber
				logger.debug("formBackingObject: Get");		
				member = handleGet(memberId, request, errorMessages, infoMessages);
			}
		
		}	
		
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("formBackingObject END command ".concat(member.toString()));
		
		return member;
		
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
		TeamMember teamMember = (TeamMember) command;
		
		// redirecting regarding the operation
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		logger.debug("action = ".concat(action));
		
		if(action != null) {
			if(IConstant.CMD_SAVE.equals(action)) {
				if(teamMember.getMemberId() > 0) {
					//mav = handleUpdate(request, response, command, errors, errorMessages, infoMessages, locale);
				} else {
					//mav = handleAdd(request, response, command, errors, errorMessages, infoMessages, locale);
				}
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
	 * Adds a team member
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
//	private ModelAndView handleAdd(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
//		logger.debug("handleAdd - START");
//		ModelAndView mav = new ModelAndView(LISTING_VIEW);
//		
//		TeamMember teamMember = (TeamMember) command;
//		teamMember.setStatus(IConstant.NOM_TEAM_MEMBER_STATUS_ACTIVATED);
//		
//		logger.debug("teamMember = " + teamMember);
//		
//		mav.addObject(getCommandName(), teamMember);
//		
//		Integer projectTeamId = ServletRequestUtils.getIntParameter(request, PROJECT_TEAM_ID);
//						
//		try{	
//			
//			// adding the team member to the database
//			BLTeamMember.getInstance().add(teamMember);
//			String teamMemberName = ControllerUtils.getInstance().tokenizeField(teamMember.getFirstName().concat(" ").concat(teamMember.getLastName()), IConstant.NR_CHARS_PER_LINE_MESSAGE);
//			infoMessages.add(messageSource.getMessage(ADD_SUCCESS, new Object[] {teamMemberName}, locale));
//			
//			List<TeamMember> members = BLTeamMember.getInstance().getNewTeamMembersByProjectTeam(projectTeamId);
//			logger.debug("members = " + members);
//			request.setAttribute(TEAM_MEMBERS, members);
//
//		} catch (BusinessException be) {
//			logger.error("", be);
//			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
//					.getLocale(request)));
//		} catch (Exception e){
//			logger.error("", e);
//			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
//					.getLocale(request)));
//		}
//									        	
//		logger.debug("handleAdd - END");
//		
//		return mav;
//	}
	
	/**
	 * Handle the update action
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
//	private ModelAndView handleUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
//		logger.debug("handleUpdate - START");
//		ModelAndView mav = new ModelAndView(LISTING_VIEW);
//		
//		TeamMember teamMember = (TeamMember) command;
//		
//		mav.addObject(getCommandName(), teamMember);
//		
//		Integer projectTeamId = ServletRequestUtils.getIntParameter(request, PROJECT_TEAM_ID);
//					
//		try{	
//			
//			// updating the team member to the database
//			BLTeamMember.getInstance().update(teamMember);
//			String teamMemberName = ControllerUtils.getInstance().tokenizeField(teamMember.getFirstName().concat(" ").concat(teamMember.getLastName()), IConstant.NR_CHARS_PER_LINE_MESSAGE);
//			infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, new Object[] {teamMemberName}, locale));
//			
//			List<TeamMember> members = BLTeamMember.getInstance().getNewTeamMembersByProjectTeam(projectTeamId);
//			logger.debug("members = " + members);
//			request.setAttribute(TEAM_MEMBERS, members);
//
//		} catch (BusinessException be) {
//			logger.error("", be);
//			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
//					.getLocale(request)));
//		} catch (Exception e){
//			logger.error("", e);
//			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
//					.getLocale(request)));
//		}
//									        	
//		logger.debug("handleUpdate - END");
//		
//		return mav;
//	}
//	
	/*/* (non-Javadoc)
	 * @see ro.cs.om.web.controller.root.RootSimpleFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
	 */	
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		logger.debug("referenceData - START");
		//used as a container for error messages
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		Map map = new HashMap();
		
		// adding to model the action from the request
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		map.put(IConstant.REQ_ACTION, action);			
									
		setErrors(request, errorMessages);
		
		logger.debug("referenceData - END");
		return map;
	}	
	
	/**
	 * Get the team member
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @param request
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 */
	private TeamMember handleGet(Integer memberId, HttpServletRequest request, ArrayList<String> errorMessages, ArrayList<String> infoMessages) {
		logger.debug("handleGet - START");
		TeamMember member = null;
		try{
			member = BLTeamMember.getInstance().getByMemberId(memberId);
			logger.debug("member = " + member);
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
		return member;
	}	
}
