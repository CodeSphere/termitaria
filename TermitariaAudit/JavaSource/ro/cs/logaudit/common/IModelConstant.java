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
package ro.cs.logaudit.common;

/**
 * User for constant of the model layer
 * 
 * @author coni
 */

public interface IModelConstant {

	//Hibernate entities'name
	//----------------------------------------- AUDIT OM --------------------------------------------------------------
	String auditomEntity											= "auditom";
	String auditomForDeleteEntity									= "auditomForDelete";
	String auditomReportsEntity										= "auditomReports";
	
	//----------------------------------------- AUDIT DM --------------------------------------------------------------
	String auditdmEntity											= "auditdm";
	String auditdmForDeleteEntity									= "auditdmForDelete";
	String auditdmReportsEntity										= "auditdmReports";
	
	//----------------------------------------- AUDIT TS --------------------------------------------------------------
	String audittsEntity											= "auditts";
	String audittsForDeleteEntity									= "audittsForDelete";
	String audittsReportsEntity										= "audittsReports";
	
	//----------------------------------------- AUDIT TS --------------------------------------------------------------
	String auditcmEntity											= "auditcm";
	String auditcmForDeleteEntity									= "auditcmForDelete";
	String auditcmReportsEntity										= "auditcmReports";

}
