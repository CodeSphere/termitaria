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
package ro.cs.om.common;

/**
 * For back constants
 * 
 * @author Adelina
 *
 */

public class BackConstant {

	
	//------------------------------OUT OF OFFICE----------------------------------
	
	private final String OM_Back_NewOO0FromSearch	  				= "NEW_OO0_FROM_SEARCH";
	
	private final String OM_Back_NewOOOFromListForPerson			= "NEW_OOO_FROM_LIST_FOR_PERSON";
	
	private final String OM_Back_EditOOOFromListForPerson			= "EDIT_OOO_FROM_LIST_FOR_PERSON";	

	private final String OM_Back_EditOOOFromSearch					= "EDIT_OOO_FROM_SEARCH";
	
	
	//------------------------------DEPARTAMENT----------------------------------
			
	private final String OM_Back_AddDepartmentFromOrganisation		= "ADDDEPARTMENTFROMORGANISATION";	
	
	
	//------------------------------PERSON----------------------------------
	
	private final String OM_Back_NewPersonAdmin						= "NEWADMIN";
	
	private final String OM_Back_SavePerson 						= "SAVE";
	
	private final String OM_Back_SavePersonAdmin 					= "SAVEADMIN";
	
	private final String OM_Back_NewPersonFromOrganisation 			= "NEWPERSONFROMORGANISATION";
	
	private final String OM_Back_NewPersonFromDepartment 			= "NEWPERSONFROMDEPARTMENT";
	
	private final String OM_BACK_EditPersonFromDepartment			= "GET_FROM_DEPARTMENT";
			
	
	//------------------------------ORGANISATION----------------------------------
	
	private final String OM_Back_Get_Organisation					= "GET";
	
	private final String OM_Back_AddOrganisationCompany				= "ADDCOMPANY";
	
	private final String OM_Back_AddOrganisationBranch				= "ADDBRANCH";

	private final String OM_Back_Get_Organisation_From_Search		= "GET_FROM_SEARCH";
		
							
	//singleton implementation
    private static BackConstant theInstance = null;
    
    static {
        theInstance = new BackConstant();
    }
    public static BackConstant getInstance() {
        return theInstance;
    }
    
    private BackConstant() {
    	
    }
	
    // getters(for jsp calls)	
	
	/**
	 * @return the OM_Back_NewOO0FromSearch
	 */
	public String getOM_Back_NewOO0FromSearch() {
		return OM_Back_NewOO0FromSearch;
	}

	/**
	 * @return the OM_Back_NewOOOFromListForPersons
	 */
	public String getOM_Back_NewOOOFromListForPerson() {
		return OM_Back_NewOOOFromListForPerson;
	}
	
	/**
	 * @return the OM_Back_EditOOOListForPerson
	 */
	public String getOM_Back_EditOOOFromListForPerson() {
		return OM_Back_EditOOOFromListForPerson;
	}

	/**
	 * @return the OM_Back_EditOOOFromSearch
	 */
	public String getOM_Back_EditOOOFromSearch() {
		return OM_Back_EditOOOFromSearch;
	}	

	/**
	 * @return the OM_Back_AddDepartmentFromOrganisation
	 */
	public String getOM_Back_AddDepartmentFromOrganisation() {
		return OM_Back_AddDepartmentFromOrganisation;
	}

	/**
	 * @return the OM_Back_NewPersonAdmin
	 */
	public String getOM_Back_NewPersonAdmin() {
		return OM_Back_NewPersonAdmin;
	}

	/**
	 * @return the OM_Back_SavePerson
	 */
	public String getOM_Back_SavePerson() {
		return OM_Back_SavePerson;
	}

	/**
	 * @return the OM_Back_SavePersonAdmin
	 */
	public String getOM_Back_SavePersonAdmin() {
		return OM_Back_SavePersonAdmin;
	}

	/**
	 * @return the OM_Back_NewPersonFromOrganisation
	 */
	public String getOM_Back_NewPersonFromOrganisation() {
		return OM_Back_NewPersonFromOrganisation;
	}

	/**
	 * @return the OM_Back_NewPersonFromDepartment
	 */
	public String getOM_Back_NewPersonFromDepartment() {
		return OM_Back_NewPersonFromDepartment;
	}

	/**
	 * @return the OM_Back_Get_Organisation
	 */
	public String getOM_Back_Get_Organisation() {
		return OM_Back_Get_Organisation;
	}

	/**
	 * @return the OM_Back_AddOrganisationCompany
	 */
	public String getOM_Back_AddOrganisationCompany() {
		return OM_Back_AddOrganisationCompany;
	}

	/**
	 * @return the OM_Back_AddOrganisationBranch
	 */
	public String getOM_Back_AddOrganisationBranch() {
		return OM_Back_AddOrganisationBranch;
	}
	
	/**
	 * @return the OM_BACK_EditPersonFromDepartment
	 */
	public String getOM_BACK_EditPersonFromDepartment() {
		return OM_BACK_EditPersonFromDepartment;
	}
	
	/**
	 * @return the OM_Back_Get_Organisation_From_Search
	 */
	public String getOM_Back_Get_Organisation_From_Search() {
		return OM_Back_Get_Organisation_From_Search;
	}
	
	
		     
}
