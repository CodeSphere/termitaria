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
package ro.cs.logaudit.business;

import java.util.ArrayList;
import java.util.List;

import ro.cs.logaudit.entity.OrganisationBean;
import ro.cs.logaudit.exception.BusinessException;
import ro.cs.logaudit.exception.ICodeException;
import ro.cs.logaudit.ws.client.om.OMWebServiceClient;
import ro.cs.logaudit.ws.client.om.entity.WSOrganisation;




/**
 * Singleton which expose business methods for Organisation item
 *
 * @author coni
 */
public class BLOrganisation extends BusinessLogic {
	
    private static BLOrganisation theInstance = null;
    
    //singleton implementation
    private BLOrganisation(){};
    static {
        theInstance = new BLOrganisation();
    }
    public static BLOrganisation getInstance() {
        return theInstance;
    }
    
    /**
     * @author coni
     * Gets a list with all the organizations available from the OM Web Service
     * @return 
     * @throws BusinessException 
     */
    public List<OrganisationBean> getAllOrganisations() throws BusinessException{
    	logger.debug("getAllOrganisations START");
    	List<OrganisationBean> allOrganisations = new ArrayList<OrganisationBean>();
    	try {
    		List<WSOrganisation> organisationsList = OMWebServiceClient.getInstance().getAllOrganisations(); 
    		for ( WSOrganisation org : organisationsList) {
    			OrganisationBean organisationBean = new OrganisationBean();
    			organisationBean.setOrganisationId(org.getOrganisationId());
    			organisationBean.setOrganisationName(org.getOrganisationName());
    			allOrganisations.add(organisationBean);
    		}
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.ORGANISATION_GET_ALL, e);
    	}
    	logger.debug("getAllOrganisations END");
    	return allOrganisations;
    }

}

	
