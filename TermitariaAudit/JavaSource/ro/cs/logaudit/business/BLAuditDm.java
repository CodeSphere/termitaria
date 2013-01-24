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
import ro.cs.logaudit.dao.IDaoAuditDm;
import ro.cs.logaudit.entity.AuditDmBean;
import ro.cs.logaudit.entity.SearchAuditDmBean;
import ro.cs.logaudit.exception.BusinessException;
import ro.cs.logaudit.exception.ICodeException;


/**
 * Singleton which expose business methods for AuditDmBean 
 * 
 * @author coni
 */
public class BLAuditDm extends BusinessLogic{

	private IDaoAuditDm auditDmDao = DaoBeanFactory.getInstance().getDaoAuditDm();
	
	//singleton implementation
    private static BLAuditDm theInstance = null;
    private BLAuditDm(){};
    static {
        theInstance = new BLAuditDm();
    }
    public static BLAuditDm getInstance() {
        return theInstance;
    }
    
    /**
     * @author coni
     * @param auditDmBean
     * @throws BusinessException
     * Adds a new audit event
     */
    public void add(HashMap<String, Object> auditBeanProperties) throws BusinessException{
    	logger.debug("add - START");
    	try {
    		AuditDmBean auditDmBean = new AuditDmBean();
    		XMLGregorianCalendar date = (XMLGregorianCalendar) auditBeanProperties.get(IConstant.AUDIT_DATE);
    		auditDmBean.setDate(date.toGregorianCalendar().getTime());
    		auditDmBean.setEvent((String) auditBeanProperties.get(IConstant.AUDIT_EVENT));
    		auditDmBean.setMessageRO((String) auditBeanProperties.get(IConstant.AUDIT_MESSAGE_RO));
    		auditDmBean.setMessageEN((String) auditBeanProperties.get(IConstant.AUDIT_MESSAGE_EN));
    		auditDmBean.setOrganisationId((Integer)auditBeanProperties.get(IConstant.AUDIT_ORGANISATION_ID));
    		auditDmBean.setFirstName((String)auditBeanProperties.get(IConstant.AUDIT_FIRTSNAME));
    		auditDmBean.setLastName((String)auditBeanProperties.get(IConstant.AUDIT_LASTNAME));
    		auditDmBean.setPersonId((Integer)auditBeanProperties.get(IConstant.AUDIT_PERSON_ID));
    		auditDmDao.add(auditDmBean);
    	} catch (Exception bexc) {
    		throw new BusinessException(ICodeException.AUDITDM_ADD, bexc);
    	}
    	logger.debug("add - END");
    } 
    
    /**
     * Searches for audit dm events after criterion from searchAuditCmBean
     * @author coni
     * 
     * @param searchAuditDmBean - Bean that contains the search criterion
     * @return A list of audit beans
     * @throws BusinessException
     */
    public List<AuditDmBean> getResultsForSearch(SearchAuditDmBean searchAuditDmBean, boolean isDeleteAction, Locale locale) throws BusinessException{
    	logger.debug("getResultsForSearch - START");
    	List<AuditDmBean> res = null;
    	try {
    		res = auditDmDao.getAuditBeanFromSearch(searchAuditDmBean, isDeleteAction, locale);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.AUDITDM_SEARCH, e);
    	}
    	logger.debug("getResultsForSearch - END");
    	return res;
    }
    
	/**
	 * Delete dm audit event identifed by it's id with all the components
	 * 
	 * Return the DM audit event that has been deleted
	 * @author coni
	 * @throws BusinessException 
	 */
	
	public AuditDmBean delete(Integer auditId) throws BusinessException{
		logger.debug("delete - START");
		
		AuditDmBean auditDmBean = null;
		try{
			auditDmBean = auditDmDao.delete(auditId);
		} catch(Exception e){
			throw new BusinessException(ICodeException.AUDITDM_DELETE, e);
		}
		
		logger.debug("delete - END");
		return auditDmBean;
		
	}

}
