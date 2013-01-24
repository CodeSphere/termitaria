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

import ro.cs.ts.business.BLActivity;
import ro.cs.ts.business.BLCostSheet;
import ro.cs.ts.business.BLCurrency;
import ro.cs.ts.business.BLExchange;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLProjectDetails;
import ro.cs.ts.cm.Project;
import ro.cs.ts.entity.Activity;
import ro.cs.ts.entity.CostSheet;
import ro.cs.ts.entity.Exchange;
import ro.cs.ts.entity.ProjectDetails;
import ro.cs.ts.entity.SearchCostSheetBean;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootAbstractController;
import ro.cs.ts.web.security.UserAuth;

public class ProjectDetailsExtendedController extends RootAbstractController{
	
	//------------------------VIEW------------------------------------------------------------------	
	private static final String VIEW						= "ProjectDetailsExtended";
	
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String ROOT_KEY					= "project.";		
	private static final String GET_ERROR					= ROOT_KEY.concat("get.error");	
		
	//------------------------OTHER PARAMETERS---------------------------------------------------------	
	private final static String MODEL_ACTIVITIES			= "ACTIVITIES";
	private final static String MODEL_TEAM_MEMBERS          = "TEAM_MEMBERS";
	private final static String MODEL_COSTS          		= "COSTS";
	private final static String MODEL_EXCHANGES          	= "EXCHANGES";	
	
			
	public ProjectDetailsExtendedController() {
		setView(VIEW);
	}
	

	/**
	 * @author Andreea
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse result) throws Exception {
		logger.debug("handleRequestInternal - START");
		
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		ModelAndView mav = new ModelAndView();
		
		Integer projectId;
		try {
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			projectId = ServletRequestUtils.getIntParameter(request, "projectId");
			ProjectDetails projectDetails = BLProjectDetails.getInstance().getByProjectId(projectId);
			Project project = BLProject.getInstance().getProjectWithAll(projectId);
			
			
			//adding the projectId
			mav.addObject("PROJECT_ID", projectId);
			
			
			//adding the activities
			List<Activity> activities = BLActivity.getInstance().getByProjectId(projectId);
			mav.addObject(MODEL_ACTIVITIES, activities);
			
			
			//adding the team members
			mav.addObject(MODEL_TEAM_MEMBERS, project.getProjectTeam().getTeamMembers());
			
			
			//adding costs
			SearchCostSheetBean scsb = new SearchCostSheetBean();
			scsb.setOrganizationId(userAuth.getOrganisationId());
			scsb.setProjectId(projectId);
			scsb.setTeamMemberId(-1);
			scsb.setCurrentPage(1);
			scsb.setNbrOfResults(1000);
			scsb.setSortParam("date");
			scsb.setSortDirection(1);
			List<CostSheet> costSheets = BLCostSheet.getInstance().getResultsForSearch(scsb, false);

			mav.addObject(MODEL_COSTS, costSheets);
			
			
			//adding exchange
			List<Exchange> exchanges = BLExchange.getInstance().getByProjectDetailId(projectDetails.getProjectDetailId());

			for (int i = 0; i < exchanges.size(); i++) {
				exchanges.get(i).setFirstCurrency(BLCurrency.getInstance().getAll(exchanges.get(i).getFirstCurrencyId()));
				exchanges.get(i).setSecondCurrency(BLCurrency.getInstance().getAll(exchanges.get(i).getSecondCurrencyId()));
			}
			
			mav.addObject(MODEL_EXCHANGES, exchanges);
			
		} catch(ServletRequestBindingException e){
			logger.error("handleRequestInternal", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		
		
		//Publish Info/Error messages
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);

		logger.debug("handleRequestInternal - END ");

		return mav;
	}
	
}
