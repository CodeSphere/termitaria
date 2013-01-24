/*
 *    This file is part of Termitaria, a project management tool 
   Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
    
   Termitaria is free software; you can redistribute it and/or 
   modify it under the terms of the GNU Affero General Public License 
   as published by the Free Software Foundation; either version 3 of 
   the License, or (at your option) any later version.
   
   This program is distributed in the hope that it will be useful, 
   but WITHOUT ANY WARRANTY; without even the implied warranty of 
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
   GNU Affero General Public License for more details.
   
   You should have received a copy of the GNU Affero General Public License 
   along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 */
/**
 * 
 * HISTORY:
 * To be completed when a stable version of the file is modified.
 * 
*/

//========================================================================================================
/**
 * GLOBAL VARIABLES
 */

//========================================================================================================
var browser = navigator.appName;
var b_version = navigator.appVersion;
var version = parseFloat(b_version);
var moduleName = 'Content Management';
var nbResultsPerPage = 5;
var nbOfPagesPerPanel = 5;
var DM_THEME = null;

//-------------------------------------JAVA SCRIPT FORM ENTITIES---------------------------------------------
var OLD_ENTITY = null;
var ENTITY = null;
var PROJECT_ID = null;
var TEAM_MEMBER = 0
var JSON_STR = "";
var DELETED_MEMBER_ID = 0;
var ID_JSON_EXTERNAL_MEMEBER = null;

var PERSON_BIRTH_YEAR = 0;
var PERSON_BIRTH_MONTH = 0;
var PERSON_BIRTH_DAY = 0;
