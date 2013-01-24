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

import java.util.ArrayList;
import java.util.List;

import ro.cs.cm.entity.SearchPersonBean;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.exception.ICodeException;
import ro.cs.cm.om.Person;
import ro.cs.cm.ws.client.om.OMWebServiceClient;
import ro.cs.cm.ws.client.om.entity.UserSimple;
import ro.cs.cm.ws.client.om.entity.WSUser;


/**
 * Singleton which expose business methods for Person item
 * 
 * @author Adelina
 *
 */
public class BLPerson extends BusinessLogic {
			
	// singleton implementation
	private static BLPerson theInstance = null;
	
	private BLPerson() {
		
	}
	
	static {
		theInstance = new BLPerson();
	}
	
	public static BLPerson getInstance() {
		return theInstance;
	}
	
	
	/**
	 * Get a person identified by id
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @return
	 * @throws BusinessException 
	 */
	public Person get(Integer personId) throws BusinessException {
		logger.debug("get - START project with id =".concat(Integer.toString(personId)));
		Person person = null;
		try{			
			WSUser user = OMWebServiceClient.getInstance().getPersonSimple(personId).getPerson();
			if(user != null) {
				person = new Person();
				person.setPersonId(user.getId());
				person.setFirstName(user.getFirstName());
				person.setLastName(user.getLastName());	
				person.setStatus(user.getStatus());
				person.setEmail(user.getEmail());
			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PERSON_GET, e);
		}
		logger.debug("get - END project with id =".concat(Integer.toString(personId)));
		return person;
	}
	
	/**
	 * @author Coni
	 * @param firstName
	 * @param lastName
	 * @param organizationId
	 * @param withDeleted
	 * @return
	 * @throws BusinessException
	 */
	public List<Person> getFromSearch(String firstName, String lastName, Integer organizationId, boolean withDeleted) throws BusinessException {
		logger.debug("getFromSearch - START");
		List<Person> persons = new ArrayList<Person>();
		try {
			SearchPersonBean spb = new SearchPersonBean();
			spb.setFirstName(firstName);
			spb.setLastName(lastName);
			spb.setOrganizationId(organizationId);
			spb.setWithDeleted(withDeleted);
			List<UserSimple> usersSimple = OMWebServiceClient.getInstance().getPersonFromSearch(spb).getPersons();
			if (usersSimple != null) {
				for (UserSimple user : usersSimple) {
					Person person = new Person();
					person.setFirstName(user.getFirstName());
					person.setLastName(user.getLastName());
					person.setPersonId(user.getUserId());
					persons.add(person);
				}
			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PERSON_GET_FROM_SEARCH, e);
		}
		logger.debug("getFromSearch - END");
		return persons;
	}
}
