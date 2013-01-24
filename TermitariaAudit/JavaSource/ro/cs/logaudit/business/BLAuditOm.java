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
import ro.cs.logaudit.dao.IDaoAuditOm;
import ro.cs.logaudit.entity.AuditOmBean;
import ro.cs.logaudit.entity.SearchAuditOmBean;
import ro.cs.logaudit.exception.BusinessException;
import ro.cs.logaudit.exception.ICodeException;


/**
 * Singleton which expose business methods for AuditOmBean 
 * 
 * @author coni
 */
public class BLAuditOm extends BusinessLogic{

	private IDaoAuditOm auditOmDao = DaoBeanFactory.getInstance().getDaoAuditOm();
	
	//singleton implementation
    private static BLAuditOm theInstance = null;
    private BLAuditOm(){};
    static {
        theInstance = new BLAuditOm();
    }
    public static BLAuditOm getInstance() {
        return theInstance;
    }
    
    /**
     * @author coni
     * @param auditOmBean
     * @throws BusinessException
     * Adds a new audit event
     */
    public void add(HashMap<String, Object> auditBeanProperties) throws BusinessException{
    	logger.debug("add - START");
    	try {
    		AuditOmBean auditOmBean = new AuditOmBean();
    		XMLGregorianCalendar date = (XMLGregorianCalendar) auditBeanProperties.get(IConstant.AUDIT_DATE);
    		auditOmBean.setDate(date.toGregorianCalendar().getTime());
    		auditOmBean.setEvent((String) auditBeanProperties.get(IConstant.AUDIT_EVENT));
    		auditOmBean.setMessageRO((String) auditBeanProperties.get(IConstant.AUDIT_MESSAGE_RO));
    		auditOmBean.setMessageEN((String) auditBeanProperties.get(IConstant.AUDIT_MESSAGE_EN));
    		auditOmBean.setOrganisationId((Integer)auditBeanProperties.get(IConstant.AUDIT_ORGANISATION_ID));
    		auditOmBean.setFirstName((String)auditBeanProperties.get(IConstant.AUDIT_FIRTSNAME));
    		auditOmBean.setLastName((String)auditBeanProperties.get(IConstant.AUDIT_LASTNAME));
    		auditOmBean.setPersonId((Integer)auditBeanProperties.get(IConstant.AUDIT_PERSON_ID));
    		auditOmDao.add(auditOmBean);
    	} catch (Exception bexc) {
    		throw new BusinessException(ICodeException.AUDITOM_ADD, bexc);
    	}
    	logger.debug("add - END");
    } 
    
    /**
     * Searches for audit om events after criterion from searchAuditOmBean
     * @author alu
     * 
     * @param searchAuditOmBean - Bean that contains the search criterion
     * @return A list of audit beans
     * @throws BusinessException
     */
    public List<AuditOmBean> getResultsForSearch(SearchAuditOmBean searchAuditOmBean, boolean isDeleteAction, Locale locale) throws BusinessException{
    	logger.debug("getResultsForSearch - START");
    	List<AuditOmBean> res = null;
    	try {
    		res = auditOmDao.getAuditBeanFromSearch(searchAuditOmBean, isDeleteAction, locale);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.AUDITOM_SEARCH, e);
    	}
    	logger.debug("getResultsForSearch - END");
    	return res;
    }
    
	/**
	 * Delete om audit event identifed by it's id with all the components
	 * 
	 * Return the OM audit event that has been deleted
	 * @author coni
	 * @throws BusinessException 
	 */
	
	public AuditOmBean delete(Integer auditId) throws BusinessException{
		logger.debug("delete - START");
		
		AuditOmBean auditOmBean = null;
		try{
			auditOmBean = auditOmDao.delete(auditId);
		} catch(Exception e){
			throw new BusinessException(ICodeException.AUDITOM_DELETE, e);
		}
		
		logger.debug("delete - END");
		return auditOmBean;
		
	}

}
