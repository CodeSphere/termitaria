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
package ro.cs.ts.common;


/**
 * @author coni
 * @author Adelina
 *
 */
public class PermissionConstant {
	
	
	////////////////////////////////TS////////////////////////////////
	
	// the basic permission from the TS module
	private final String TS_Basic							= "TS_Basic";
		
	// gives the permission to advanced search a project
	// search for all the projects in the organization
	private final String TS_ProjectAdvancedSearch			= "TS_ProjectAdvancedSearch"; 
	
	// gives the permission to advanced view a project
	// the advanced view contains also the project details of the project
	private final String TS_ProjectAdvancedView				= "TS_ProjectAdvancedView";
	
	// gives the permission to add and update project details for the project
	private final String TS_ProjectAddUpdateProjectDetails	 = "TS_ProjectAddUpdateProjectDetails";
	
	// gives the permission to add and update the team members details
	// that belongs to the project
	private final String TS_ProjectAddUpdateTeamMemberDetails = "TS_ProjectAddUpdateTeamMemberDetails";
	
	// gives the permission to receive notifications
	private final String TS_NotificationReceive			 	  = "TS_NotificationReceive";
	
	// gives permission to advanced search an activity
	// search for all the activities from all projects 
	// and those that belongs directly to the organization
	private final String TS_ActivityAdvancedSearch			= "TS_ActivityAdvancedSearch";
		
	// gives permission to advanced add and update an activity
	// advanced, means that the activity can be added for 
	// a project and also for an organization
	private final String TS_ActivityAdvancedAddUpdate 		= "TS_ActivityAdvancedAddUpdate";
	
	// gives permission to delete an activity
	private final String TS_ActivityDelete					= "TS_ActivityDelete";
	
	// gives permission to advanced search the person
	// search for all the persons from organization
	// and those external persons
	private final String TS_PersonAdvancedSearch			= "TS_PersonAdvancedSearch";
	
	// gives the permission to add and update the person details
	// that is not an external person
	private final String TS_PersonDetailAddUpdate			= "TS_PersonDetailAddUpdate";
	
	// gives permission to advanced view a person
	// the advanced view contains also the person details informations about the person
	private final String TS_PersonAdvancedView				= "TS_PersonAdvancedView";
	
	// gives permission to view the informations about
	// a team member
	private final String TS_TeamMemberView					= "TS_TeamMemberView";
			
	//gives the permission to search for all the users(persons from inside and outside the organization) records,
	//registered for any project or per organization  	
	private final String TS_RecordSearchAll					= "TS_RecordSearchAll";
	
	//gives the permission to delete the records of any user(person from inside and outside the organization) 
	//registered for any project or per organization 
	private final String TS_RecordDeleteAll					= "TS_RecordDeleteAll";
	
	//gives the permission to update the records of any user(person from inside and outside the organization) 
	//registered for any project or per organization 
	private final String TS_RecordUpdateAll					= "TS_RecordUpdateAll";
	
	//gives the permission to add records for all the users(persons from inside and outside the organization)
	//for any project or per organization  
	private final String TS_RecordAddAll					= "TS_RecordAddAll";
	
	//gives the permission to view the cost price and billing price for the activities, work and overtime 
	//hours for a team member and work and overtime hours for a person from organization
	private final String TS_RecordViewCosts					= "TS_RecordViewCosts";
	
	//gives the permission to search for all the users(persons from inside and outside the organization) cost sheets,
	//registered for any project or per organization  
	private final String TS_CostSheetSearchAll				= "TS_CostSheetSearchAll";
	
	//gives the permission to delete the cost sheets of any user(person from inside and outside the organization) 
	//registered for any project or per organization 
	private final String TS_CostSheetDeleteAll				= "TS_CostSheetDeleteAll";
	
	//gives the permission to update the cost sheets of any user(person from inside and outside the organization) 
	//registered for any project or per organization 
	private final String TS_CostSheetUpdateAll				= "TS_CostSheetUpdateAll";
	
	//gives the permission to add cost sheets for all the users(persons from inside and outside the organization)
	//for any project or per organization  
	private final String TS_CostSheetAddAll					= "TS_CostSheetAddAll";
	
	//gives the permission to view the Currency tab and search for currencies
	private final String TS_CurrencySearch					= "TS_CurrencySearch";
	
	//gives the permission to add currencies
	private final String TS_CurrencyAdd						= "TS_CurrencyAdd";
	
	//gives the permission to update currencies
	private final String TS_CurrencyUpdate					= "TS_CurrencyUpdate";

	//gives the permission to delete currencies
	private final String TS_CurrencyDelete					= "TS_CurrencyDelete";
	
	//gives the permission to search for exchanges in all the projects; user that don't have these permission, can only search
	//for exchanges in the projects they are PM
	private final String TS_ExchangeSearchAll				= "TS_ExchangeSearchAll";
	
	//gives the permission to add exchanges in all the projects; user that don't have these permission, can only add
	//exchanges in the projects they are PM
	private final String TS_ExchangeAddAll					= "TS_ExchangeAddAll";
	
	//gives the permission to update exchanges in all the projects; user that don't have these permission, can only update
	//exchanges in the projects they are PM
	private final String TS_ExchangeUpdateAll				= "TS_ExchangeUpdateAll";

	//gives the permission to delete exchanges from all the projects; user that don't have these permission, can only delete
	//exchanges from the projects they are PM
	private final String TS_ExchangeDeleteAll				= "TS_ExchangeDeleteAll";
	
	// gives the permission to view reports
	private final String TS_ReportsView 					= "TS_ReportsView";		

	/////////////////////////////// SUPER //////////////////////////////
	private final String _Super								= "Super";
	
	
	//singleton implementation
    private static PermissionConstant theInstance = null;
    static {
        theInstance = new PermissionConstant();
    }
    public static PermissionConstant getInstance() {
        return theInstance;
    }
    
    // getters(for jsp calls)
	public String get_Super() {
		return _Super;
	}

	public String getTS_CurrencySearch() {
		return TS_CurrencySearch;
	}

	public String getTS_CurrencyAdd() {
		return TS_CurrencyAdd;
	}

	public String getTS_CurrencyUpdate() {
		return TS_CurrencyUpdate;
	}

	public String getTS_CurrencyDelete() {
		return TS_CurrencyDelete;
	}

	public String getTS_Basic() {
		return TS_Basic;
	}

	public String getTS_RecordSearchAll() {
		return TS_RecordSearchAll;
	}

	public String getTS_RecordDeleteAll() {
		return TS_RecordDeleteAll;
	}

	public String getTS_RecordUpdateAll() {
		return TS_RecordUpdateAll;
	}

	public String getTS_RecordAddAll() {
		return TS_RecordAddAll;
	}

	public String getTS_CostSheetSearchAll() {
		return TS_CostSheetSearchAll;
	}

	public static PermissionConstant getTheInstance() {
		return theInstance;
	}

	/**
	 * @return the tS_ProjectAdvancedSearch
	 */
	public String getTS_ProjectAdvancedSearch() {
		return TS_ProjectAdvancedSearch;
	}
	
	/**
	 * @return the tS_ProjectAddUpdateProjectDetails
	 */
	public String getTS_ProjectAddUpdateProjectDetails() {
		return TS_ProjectAddUpdateProjectDetails;
	}

	/**
	 * @return the tS_ProjectAddUpdateTeamMemberDetails
	 */
	public String getTS_ProjectAddUpdateTeamMemberDetails() {
		return TS_ProjectAddUpdateTeamMemberDetails;
	}

	/**
	 * @return the tS_ActivityAdvancedSearch
	 */
	public String getTS_ActivityAdvancedSearch() {
		return TS_ActivityAdvancedSearch;
	}

	/**
	 * @return the tS_ActivityAdvancedAddUpdate
	 */
	public String getTS_ActivityAdvancedAddUpdate() {
		return TS_ActivityAdvancedAddUpdate;
	}

	/**
	 * @return the tS_ActivityDelete
	 */
	public String getTS_ActivityDelete() {
		return TS_ActivityDelete;
	}

	/**
	 * @return the tS_ProjectAdvancedView
	 */
	public String getTS_ProjectAdvancedView() {
		return TS_ProjectAdvancedView;
	}

	/**
	 * @return the tS_PersonAdvancedSearch
	 */
	public String getTS_PersonAdvancedSearch() {
		return TS_PersonAdvancedSearch;
	}

	public String getTS_CostSheetDeleteAll() {
		return TS_CostSheetDeleteAll;
	}

	public String getTS_CostSheetUpdateAll() {
		return TS_CostSheetUpdateAll;
	}

	public String getTS_CostSheetAddAll() {
		return TS_CostSheetAddAll;
	}

	public String getTS_ExchangeSearchAll() {
		return TS_ExchangeSearchAll;
	}

	public String getTS_ExchangeAddAll() {
		return TS_ExchangeAddAll;
	}

	public String getTS_ExchangeUpdateAll() {
		return TS_ExchangeUpdateAll;
	}

	public String getTS_ExchangeDeleteAll() {
		return TS_ExchangeDeleteAll;
	}

	public static void setTheInstance(PermissionConstant theInstance) {
		PermissionConstant.theInstance = theInstance;
	}

	/**
	 * @return the tS_PersonDetailAddUpdate
	 */
	public String getTS_PersonDetailAddUpdate() {
		return TS_PersonDetailAddUpdate;
	}

	/**
	 * @return the tS_PersonAdvancedView
	 */
	public String getTS_PersonAdvancedView() {
		return TS_PersonAdvancedView;
	}

	/**
	 * @return the tS_TeamMemberView
	 */
	public String getTS_TeamMemberView() {
		return TS_TeamMemberView;
	}

	/**
	 * @return the tS_RecordViewCosts
	 */
	public String getTS_RecordViewCosts() {
		return TS_RecordViewCosts;
	}

	/**
	 * @return the TS_ReportsView
	 */
	public String getTS_ReportsView() {
		return TS_ReportsView;
	}

	/**
	 * @return the tS_NotificationReceive
	 */
	public String getTS_NotificationReceive() {
		return TS_NotificationReceive;
	}		
			
}
