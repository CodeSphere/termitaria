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
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLCostSheet;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLTeamMember;
import ro.cs.ts.business.BLUser;
import ro.cs.ts.cm.Project;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.entity.CostSheet;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootAbstractController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.entity.UserSimple;

/**
 * Used to view info about a cost sheet
 * @author Coni
 *
 */
public class CostSheetViewController extends RootAbstractController {

	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String GET_ERROR								= "costsheet.get.error";	
	
	//------------------------VIEW------------------------------------------------------------------
	private static final String VIEW 							= "CostSheetView";
	
	//------------------------MODEL------------------------------------------------------------------	
	private static final String COST_SHEET_ID					= "costSheetId";
	private static final String COST_SHEET						= "COST_SHEET";
	private final static String IS_MANAGER						= "IS_MANAGER";
	private static final String IS_USER_ALL						= "IS_USER_ALL";
	private static final String HAS_PROJECT						= "HAS_PROJECT";
	
	// Number of characters that fit in a line, for the display panel,
    // if there are big words
    public static final Integer NR_CHARS_PER_LINE				= 50;
    
	public CostSheetViewController() {
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
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		CostSheet costSheet = null;
		boolean isManager = false;
		boolean hasProject = false;
		
		try {
			Integer costSheetId = ServletRequestUtils.getIntParameter(request, COST_SHEET_ID);
			logger.debug("costSheetId: ".concat(costSheetId.toString()));
			
			mav.addObject(COST_SHEET_ID, costSheetId);
			
			if (costSheetId != null) {
				costSheet = BLCostSheet.getInstance().getForView(costSheetId);
				
				//set costSheetReporterName - 
				if (costSheet.getTeamMemberDetail() != null) {
					TeamMember member = BLTeamMember.getInstance().getTeamMember(costSheet.getTeamMemberDetail().getTeamMemberId(), false);
					if (member != null) {
						costSheet.setCostSheetReporterName(member.getFirstName().concat(" ").concat(member.getLastName()));
					}
				} else if (costSheet.getPersonDetail() != null) {
					String[] personIds = new String[1];
					personIds[0] = String.valueOf(costSheet.getPersonDetail().getPersonId());
					List<UserSimple> users = BLUser.getInstance().getUsersByPersonId(personIds);
					if (users != null) {
						costSheet.setCostSheetReporterName(users.get(0).getFirstName().concat(" ").concat(users.get(0).getLastName()));
					}
				}
				if (costSheet.getCostSheetReporterName() != null) {
					costSheet.setCostSheetReporterName(ControllerUtils.getInstance().tokenizeField(costSheet.getCostSheetReporterName(), NR_CHARS_PER_LINE));
				}
				
				//set the projectName
				if (costSheet.getProjectDetails() != null) {
					Project project = BLProject.getInstance().getSimpleProject(costSheet.getProjectDetails().getProjectId());
					if (project != null) {						
						costSheet.setProjectName(ControllerUtils.getInstance().tokenizeField(project.getName(), NR_CHARS_PER_LINE));
																		
						logger.debug("project manager id = " +  project.getManagerId());
						logger.debug("personId = " + userAuth.getPersonId());
						
						if(userAuth.getPersonId() == project.getManagerId()) {
							isManager = true;
						}					
						logger.debug("isManager = " + isManager);
						hasProject = true;
					}
				}

				if (costSheet.getObservation() != null) {
					costSheet.setObservation(ControllerUtils.getInstance().tokenizeField(costSheet.getObservation(), NR_CHARS_PER_LINE));
				}
				
				if (costSheet.getDescription() != null) {
					costSheet.setDescription(ControllerUtils.getInstance().tokenizeField(costSheet.getDescription(), NR_CHARS_PER_LINE));
				}
				
				if (costSheet.getActivityName() != null) {
					costSheet.setActivityName(ControllerUtils.getInstance().tokenizeField(costSheet.getActivityName(), NR_CHARS_PER_LINE));
				}
			}
			
			mav.addObject(COST_SHEET, costSheet);
		} catch(ServletRequestBindingException e){
			logger.error("handleRequestInternal", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(BusinessException be) {
			logger.error("handleRequestInternal", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		boolean isUserAll = false;
		//is USER_ALL
		if (userAuth.hasAuthority(PermissionConstant.getTheInstance().getTS_CostSheetAddAll()) || userAuth.hasAuthority(PermissionConstant.getTheInstance().getTS_CostSheetUpdateAll())) {
			isUserAll = true;			
		} else {
			isUserAll = false;
		}
		
		logger.debug("isUserAll = " + isUserAll);			
		mav.addObject(IS_USER_ALL, isUserAll);
		mav.addObject(IS_MANAGER, isManager);
		mav.addObject(HAS_PROJECT, hasProject);
		
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);	
		
		logger.debug("handleRequestInternal - END");
		
		return mav;
	}
}
