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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

import ro.cs.logaudit.common.IConstant;
import ro.cs.logaudit.dao.DaoBeanFactory;
import ro.cs.logaudit.dao.IDaoAuditTs;
import ro.cs.logaudit.entity.AuditTsBean;
import ro.cs.logaudit.entity.SearchAuditTsBean;
import ro.cs.logaudit.exception.BusinessException;
import ro.cs.logaudit.exception.ICodeException;

/**
 * Singleton which expose business methods for AuditTSBean 
 * 
 * @author coni
 */
public class BLAuditTs extends BusinessLogic{

	private IDaoAuditTs auditTsDao = DaoBeanFactory.getInstance().getDaoAuditTs();
	
	//singleton implementation
    private static BLAuditTs theInstance = null;
    private BLAuditTs(){};
    static {
        theInstance = new BLAuditTs();
    }
    public static BLAuditTs getInstance() {
        return theInstance;
    }
    
    /**
     * Adds a new audit event
     * @author Coni
     * @param auditBeanProperties
     * @throws BusinessException
     */
    public void add(HashMap<String, Object> auditBeanProperties) throws BusinessException{
    	logger.debug("add - START");
    	try {
    		AuditTsBean auditTsBean = new AuditTsBean();
    		XMLGregorianCalendar date = (XMLGregorianCalendar) auditBeanProperties.get(IConstant.AUDIT_DATE);
    		auditTsBean.setDate(date.toGregorianCalendar().getTime());
    		auditTsBean.setEvent((String) auditBeanProperties.get(IConstant.AUDIT_EVENT));
    		auditTsBean.setMessageRO((String) auditBeanProperties.get(IConstant.AUDIT_MESSAGE_RO));
    		auditTsBean.setMessageEN((String) auditBeanProperties.get(IConstant.AUDIT_MESSAGE_EN));
    		auditTsBean.setOrganisationId((Integer)auditBeanProperties.get(IConstant.AUDIT_ORGANISATION_ID));
    		auditTsBean.setFirstName((String)auditBeanProperties.get(IConstant.AUDIT_FIRTSNAME));
    		auditTsBean.setLastName((String)auditBeanProperties.get(IConstant.AUDIT_LASTNAME));
    		auditTsBean.setPersonId((Integer)auditBeanProperties.get(IConstant.AUDIT_PERSON_ID));
    		auditTsDao.add(auditTsBean);
    	} catch (Exception bexc) {
    		throw new BusinessException(ICodeException.AUDITTS_ADD, bexc);
    	}
    	logger.debug("add - END");
    }
    
    /**
     * Searches for audit ts events after criterion from searchAuditTsBean
     * @author coni
     * 
     * @param searchAuditTsBean - Bean that contains the search criterion
     * @return A list of audit beans
     * @throws BusinessException
     */
    public List<AuditTsBean> getResultsForSearch(SearchAuditTsBean searchAuditTsBean, boolean isDeleteAction, Locale locale) throws BusinessException{
    	logger.debug("getResultsForSearch - START");
    	List<AuditTsBean> res = null;
    	try {
    		res = auditTsDao.getAuditBeanFromSearch(searchAuditTsBean, isDeleteAction, locale);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.AUDITTS_SEARCH, e);
    	}
    	logger.debug("getResultsForSearch - END");
    	return res;
    }
    
	/**
	 * Delete ts audit event identifed by it's id with all the components
	 * 
	 * Return the TS audit event that has been deleted
	 * @author coni
	 * @throws BusinessException 
	 */
	
	public AuditTsBean delete(Integer auditId) throws BusinessException{
		logger.debug("delete - START");
		
		AuditTsBean auditTsBean = null;
		try{
			auditTsBean = auditTsDao.delete(auditId);
		} catch(Exception e){
			throw new BusinessException(ICodeException.AUDITTS_DELETE, e);
		}
		
		logger.debug("delete - END");
		return auditTsBean;
		
	}
}
