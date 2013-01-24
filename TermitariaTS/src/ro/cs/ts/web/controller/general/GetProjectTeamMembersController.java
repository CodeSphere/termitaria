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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLCostSheet;
import ro.cs.ts.business.BLRecord;
import ro.cs.ts.business.BLTeamMember;
import ro.cs.ts.business.BLUser;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.entity.CostSheet;
import ro.cs.ts.entity.Record;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootAbstractController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.entity.UserSimple;


/**
 * Used to retrieve a project details team members only if the user is PM for that project
 * @author Coni
 *
 */
public class GetProjectTeamMembersController extends RootAbstractController {
	
	private static final String VIEW 						= "GetJsonTeamMembers";
	private static final String PROJECT_ID 					= "projectId";
	private static final String CMD_GET_FROM_RECORD_FORM	= "GET_FROM_RECORD_FORM";
	private static final String CMD_GET_FROM_COST_FORM		= "GET_FROM_COST_FORM";
	private static final String CMD_GET_FROM_SEARCH			= "GET_FROM_SEARCH";
	private static final String RECORD_ID					= "recordId";
	private static final String COST_ID						= "costId";
	
	
	public GetProjectTeamMembersController() {
		setView(VIEW);
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("handleRequestInternal - START");
		
		//used as a container for info/error messages
		ArrayList<String> errorMessages = new ArrayList<String>();
		Integer projectId = new Integer(request.getParameter(PROJECT_ID));
		
		Record record = null;
		CostSheet cost = null;
		TeamMember member = null;
		
		// without the persons/members that are deleted/deactivated/no longer are in the project
		boolean withoutDeletedPersons = false;
		// with the person that is deleted/deactivate/no longer is in the project
		boolean withDeletedPerson = false;
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		logger.debug("action = " + action);
						
		if(action != null) {
			// if we are in the search form
			if(CMD_GET_FROM_SEARCH.equals(action)) {
				// we need to display the deleted/deactivated/ no longer in the project persons/members
				withoutDeletedPersons = false;
			} else { // we don't display them
				withoutDeletedPersons = true;				
				if(CMD_GET_FROM_RECORD_FORM.equals(action)) { // if we are in the record form
					Integer recordId = ServletRequestUtils.getIntParameter(request, RECORD_ID);
					if(recordId != null && recordId > 0) { // if we have an update action
						record = BLRecord.getInstance().getInstance().getAll(recordId); // get the record
						logger.debug("record = " + record);						
						withDeletedPerson = true;
					}
				} else if(CMD_GET_FROM_COST_FORM.equals(action)) { // if we are in the cost form
					Integer costId = ServletRequestUtils.getIntParameter(request, COST_ID);
					if(costId != null && costId > 0) { // if we have an update action
						cost = BLCostSheet.getInstance().getAll(costId); // get the cost
						logger.debug("cost = " + cost);
						withDeletedPerson = true;
					}
				}						
			}
			logger.debug("withoutDeletedPersons = " + withoutDeletedPersons);
		} 	
							
		// add all the persons from the selected organization for autoComplete			
		response.setContentType("text/x-json");
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String jsonTeamMembers = null;
		
		if (projectId != null) {
			//if the request came from the record form for all the persons from the organization()
			if (projectId.equals(IConstant.NOM_RECORD_FORM_PROJECT_SELECT_ORG_OPTION)) {
				List<UserSimple> users = null;
				//if the user has USER_ALL role, it can register time sheets or cost sheets for all the organization persons, otherwise the users list will contain only the user who makes the request 
				if (userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordSearchAll()) || userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordAddAll()) ||
						userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordUpdateAll()) || userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CostSheetSearchAll()) ||
						userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CostSheetAddAll()) || userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CostSheetUpdateAll())) {
					users = BLUser.getInstance().getUsersSimpleByOrganizationId(ControllerUtils.getInstance().getOrganisationIdFromSession(request), withoutDeletedPersons);
					if(withDeletedPerson) {
						// get the person that is responsible for the cost/record
						UserSimple userSimple = null;
						if((record != null && record.getPersonDetail() != null) || (cost != null && cost.getPersonDetail() != null)) {
							String[] personIds = new String[1];
							if(record != null) {
								personIds[0] = String.valueOf(record.getPersonDetail().getPersonId());
							} else {
								personIds[0] = String.valueOf(cost.getPersonDetail().getPersonId());
							}
							List<UserSimple> persons =  BLUser.getInstance().getUsersByPersonId(personIds);
							if(persons != null && persons.size() > 0) {
								userSimple = persons.get(0);
							}													
							logger.debug("userSimple = " + userSimple);
						}
						// and if the user is either deleted or deactivated, add to the users, to display 
						if(userSimple != null && (!users.contains(userSimple))) {
							users.add(userSimple);
						}
					}
				} else {
					UserSimple userSimple = new UserSimple();
					userSimple.setFirstName(userAuth.getFirstName());
					userSimple.setLastName(userAuth.getLastName());
					userSimple.setUserId(userAuth.getPersonId());
				}
				jsonTeamMembers = ControllerUtils.getInstance().getPersonsFirstNameLastNameFromOrgAsJSON(users, RequestContextUtils.getLocale(request), errorMessages, messageSource);
			} else {				
				if(withDeletedPerson) {	
					// get the member that is responsible for the cost/record
					if((record != null && record.getTeamMemberDetail() != null) || (cost != null && cost.getTeamMemberDetail() != null)) {
						if(record != null) {
							member = BLTeamMember.getInstance().getTeamMember(record.getTeamMemberDetail().getTeamMemberId(), false);
						} else {
							member = BLTeamMember.getInstance().getTeamMember(cost.getTeamMemberDetail().getTeamMemberId(), false);
						}
					}
				}
				jsonTeamMembers = ControllerUtils.getInstance().getProjectTeamMembersAsJSON(projectId, RequestContextUtils.getLocale(request), errorMessages, messageSource, userAuth.getPersonId(), withoutDeletedPersons, member);
			}
		}
		
		response.getWriter().write(jsonTeamMembers);
		setErrors(request, errorMessages);
		
		logger.debug("handleRequestInternal - END");
		return null;
	}
}
