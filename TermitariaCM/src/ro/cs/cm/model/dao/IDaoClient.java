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
package ro.cs.cm.model.dao;

import java.util.List;

import ro.cs.cm.entity.Client;
import ro.cs.cm.entity.SearchClientBean;
import ro.cs.cm.exception.BusinessException;

/**
 * Dao Interface, implemented by DaoClientImpl
 * @author Coni
 *
 */
public interface IDaoClient {
	
	/**
	 * Searches for clients using the criterion defined in searchClientBean
	 * @author Coni
	 * @param searchClientBean
	 * @param isDeleteAction
	 * @return
	 */
	public List<Client> getClientBeanFromSearch(SearchClientBean searchClientBean, boolean isDeleteAction);
	
	/**
	 * Deletes the client with the specified id
	 * @author Coni
	 * @param clientId
	 * @return
	 * @throws BusinessException
	 */
	public Client delete(int clientId);
	
	/**
	 * Gets a client with all its components
	 * 
	 * @author Coni
	 * @param clientId
	 * @return
	 */
	public Client getAll(int clientId);
	
	/**
	 * Updates the client with the specified id
	 * 
	 * @author Coni
	 * @param client
	 * @throws BusinessException
	 */
	public void update(Client client);
		
	/**
	 * Adds a new client
	 * 
	 * @author Coni
	 * @param client
	 * @throws BusinessException
	 */
	public void add(Client client);
	
	/**
	 * Get Clients by OrganizationId
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @return
	 */
	public List<Client> getClientsByOrganizationId(Integer organizationId);
	
	/**
	 * Returns a client
	 * 
	 * @author Adelina
	 */
	public Client get(int clientId);

}
