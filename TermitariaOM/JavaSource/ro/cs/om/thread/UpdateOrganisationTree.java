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
package ro.cs.om.thread;
import java.util.HashMap;

import ro.cs.om.business.BLOrganisationStructure;
import ro.cs.om.common.ApplicationObjectSupport;
import ro.cs.om.common.IConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.OrgTree;

/**
 *  Thread used to update an organisation tree after any modification in an organisation structure
 *  @author coni
 */

public class UpdateOrganisationTree extends ApplicationObjectSupport implements Runnable {
	private int organisationId;
	
	public UpdateOrganisationTree( int organisationId ){
		this.organisationId = organisationId;
	}
	
	public int getOrganisationId() {
		return organisationId;
	}
	
	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}
	
	public void run() {
		logger.debug("Start updating tree for organization with id: ".concat(String.valueOf(organisationId)));
		try {
			//this thread must get the lock of OMContext in order not permit the retrieval of
			//all organisations trees map when displaying the organisation overview
			synchronized(OMContext.class) {
				//get the new structure tree obtained after updating the organization 
				OrgTree organisationTree = BLOrganisationStructure.getInstance().getOrgTree(organisationId);
				//update the organizations trees map stored on context 
				HashMap<Integer,OrgTree> allOrganisationsTrees = (HashMap<Integer, OrgTree>)OMContext.getOrganisationsTreesMapFromContext(IConstant.ORGANISATION_TREES);
				allOrganisationsTrees.put(organisationId, organisationTree);
			} 
		} catch( Exception e){
			logger.error("ERROR while updating the tree of the organisation with id :".concat(String.valueOf(organisationId)), e);
		}
		logger.debug("End updating tree for organization with id: ".concat(String.valueOf(organisationId)));
	}

}
