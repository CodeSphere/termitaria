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
import ro.cs.logaudit.dao.IDaoAuditCm;
import ro.cs.logaudit.entity.AuditCmBean;
import ro.cs.logaudit.entity.AuditDmBean;
import ro.cs.logaudit.entity.SearchAuditCmBean;
import ro.cs.logaudit.exception.BusinessException;
import ro.cs.logaudit.exception.ICodeException;

public class BLAuditCm extends BusinessLogic {

	private IDaoAuditCm auditCmDao = DaoBeanFactory.getInstance().getDaoAuditCm();
	
	//singleton implementation
    private static BLAuditCm theInstance = null;
    private BLAuditCm(){};
    static {
        theInstance = new BLAuditCm();
    }
    public static BLAuditCm getInstance() {
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
    		AuditCmBean auditCmBean = new AuditCmBean();
    		XMLGregorianCalendar date = (XMLGregorianCalendar) auditBeanProperties.get(IConstant.AUDIT_DATE);
    		auditCmBean.setDate(date.toGregorianCalendar().getTime());
    		auditCmBean.setEvent((String) auditBeanProperties.get(IConstant.AUDIT_EVENT));
    		auditCmBean.setMessageRO((String) auditBeanProperties.get(IConstant.AUDIT_MESSAGE_RO));
    		auditCmBean.setMessageEN((String) auditBeanProperties.get(IConstant.AUDIT_MESSAGE_EN));
    		auditCmBean.setOrganisationId((Integer)auditBeanProperties.get(IConstant.AUDIT_ORGANISATION_ID));
    		auditCmBean.setFirstName((String)auditBeanProperties.get(IConstant.AUDIT_FIRTSNAME));
    		auditCmBean.setLastName((String)auditBeanProperties.get(IConstant.AUDIT_LASTNAME));
    		auditCmBean.setPersonId((Integer)auditBeanProperties.get(IConstant.AUDIT_PERSON_ID));
    		auditCmDao.add(auditCmBean);
    	} catch (Exception bexc) {
    		throw new BusinessException(ICodeException.AUDITCM_ADD, bexc);
    	}
    	logger.debug("add - END");
    }
    
    /**
     * Searches for audit cm events after criterion from searchAuditCmBean
     * @author coni
     * 
     * @param searchAuditCmBean - Bean that contains the search criterion
     * @return A list of audit beans
     * @throws BusinessException
     */
    public List<AuditCmBean> getResultsForSearch(SearchAuditCmBean searchAuditCmBean, boolean isDeleteAction, Locale locale) throws BusinessException{
    	logger.debug("getResultsForSearch - START");
    	List<AuditCmBean> res = null;
    	try {
    		res = auditCmDao.getAuditBeanFromSearch(searchAuditCmBean, isDeleteAction, locale);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.AUDITCM_SEARCH, e);
    	}
    	logger.debug("getResultsForSearch - END");
    	return res;
    }
    
	/**
	 * Delete cm audit event identifed by it's id with all the components
	 * 
	 * Return the CM audit event that has been deleted
	 * @author coni
	 * @throws BusinessException 
	 */
	
	public AuditCmBean delete(Integer auditId) throws BusinessException{
		logger.debug("delete - START");
		
		AuditCmBean auditCmBean = null;
		try{
			auditCmBean = auditCmDao.delete(auditId);
		} catch(Exception e){
			throw new BusinessException(ICodeException.AUDITCM_DELETE, e);
		}
		
		logger.debug("delete - END");
		return auditCmBean;
		
	}

}
