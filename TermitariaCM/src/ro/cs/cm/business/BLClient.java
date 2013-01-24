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
package ro.cs.cm.business;

import java.util.Collections;
import java.util.List;

import ro.cs.cm.common.IConstant;
import ro.cs.cm.entity.Client;
import ro.cs.cm.entity.SearchClientBean;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.exception.ICodeException;
import ro.cs.cm.model.dao.DaoBeanFactory;
import ro.cs.cm.model.dao.IDaoClient;
import ro.cs.cm.utils.ClientComparator;

/**
 * Singleton which exposes business methods for Client bean
 * @author Coni
 *
 */
public class BLClient extends BusinessLogic{

	private IDaoClient clientDao = DaoBeanFactory.getInstance().getDaoClient();
	
	//singleton implementation
	private static BLClient theInstance = null;
	private BLClient() {};
	static {
		theInstance = new BLClient();
	}
	public static BLClient getInstance(){
		return theInstance;
	}
	
	/**
	 * Searches for clients using the criterion defined in searchClientBean
	 * @author Coni
	 * @param searchClientBean
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException
	 */
	public List<Client> getResultsForSearch(SearchClientBean searchClientBean, boolean isDeleteAction) throws BusinessException{
		logger.debug("getResultsForSearch - START");
		List<Client> result = null;
		try {
			result = clientDao.getClientBeanFromSearch(searchClientBean, isDeleteAction);
			//sort the results by name, if it is required
			if (searchClientBean.getSortParam() != null && IConstant.NOM_CLIENT_SORT_PARAM_NAME.equals(searchClientBean.getSortParam())) {
				//sorting the records list
	    		//------sort the list
				Collections.sort(result, ClientComparator.getInstance().clientNameComparator());
		
				//ascending or descending
				if(searchClientBean.getSortDirection() == IConstant.DESCENDING){
					Collections.reverse(result);
				}
			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CLIENT_SEARCH, e);
		}
		logger.debug("getResultsForSearch - END");
		return result;
	}
	
	/**
	 * Deletes the client with the specified id
	 * @author Coni
	 * @param clientId
	 * @return
	 * @throws BusinessException
	 */
	public Client delete(int clientId) throws BusinessException{
		logger.debug("delete - START");
		Client result = null;
		try {
			result = clientDao.delete(clientId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CLIENT_SEARCH, e);
		}
		logger.debug("delete - END");
		return result;
	}
	
	/**
	 * Gets a client with all its components
	 * 
	 * @author Coni
	 * @param clientId
	 * @return
	 * @throws BusinessException 
	 */
	public Client getAll(int clientId) throws BusinessException {
		logger.debug("get - START getting client with id: ".concat(String.valueOf(clientId)));
		Client client = null;
		try {
			client = clientDao.getAll(clientId);
		} catch (Exception e){
			throw new BusinessException(ICodeException.CLIENT_GET, e);
		}
		logger.debug("get - END");
		return client;
	}
	
	/**
	 * Updates the client with the specified id
	 * 
	 * @author Coni
	 * @param client
	 * @throws BusinessException
	 */
	public void update(Client client) throws BusinessException {
		logger.debug("update - START updating client with id: ".concat(String.valueOf(client.getClientId())));
		try {
			clientDao.update(client);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CLIENT_UPDATE, e);
		}
		logger.debug("update - END");
	}
	
	/**
	 * Adds a new client
	 * @author Coni
	 * @param client
	 * @throws BusinessException
	 */
	public void add(Client client) throws BusinessException {
		logger.debug("add - START");
		try {
			clientDao.add(client);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CLIENT_ADD, e);
		}
		logger.debug("add - END");
	}
	
		/**
	 * Get Clients by OrganizationId
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @return
	 * @throws BusinessException 
	 */
	public List<Client> getClientsByOrganizationId(Integer organizationId) throws BusinessException {
		logger.debug("getClientsByOrganizationId - START , organizationid = ".concat(String.valueOf(organizationId)));
		List<Client> clients = null;
		try{
			clients = clientDao.getClientsByOrganizationId(organizationId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CLIENT_GET_BY_ORGANIZATION_ID, e);
		}
		logger.debug("getClientsByOrganizationId -  END");
		return clients;
	}
	
	/**
	 * Returns a client
	 * 
	 * @author Adelina
	 * @throws BusinessException 
	 */
	public Client get(int clientId) throws BusinessException {
		logger.debug("get - START - id=".concat(String.valueOf(clientId)));
		
		Client  client = null;
		
		try{
			client = clientDao.get(clientId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CLIENT_GET, e);
		}
		
		logger.debug("get - END");
		return client;
	}
}
