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
package ro.cs.cm.web.security;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Votes if any {@link ConfigAttribute#getAttribute()} starts with a prefix
 * indicating that it is a role. The default prefix string is <Code>ROLE_</code>,
 * but this may be overridden to any value. It may also be set to empty, which
 * means that essentially any attribute will be voted on. As described further
 * below, the effect of an empty prefix may not be quite desirable.
 * <p>
 * Abstains from voting if no configuration attribute commences with the role
 * prefix. Votes to grant access if there is an exact matching
 * {@link org.springframework.security.GrantedAuthority} to a <code>ConfigAttribute</code>
 * starting with the role prefix. Votes to deny access if there is no exact
 * matching <code>GrantedAuthority</code> to a <code>ConfigAttribute</code>
 * starting with the role prefix.
 * <p>
 * An empty role prefix means that the voter will vote for every
 * ConfigAttribute. When there are different categories of ConfigAttributes
 * used, this will not be optimal since the voter will be voting for attributes
 * which do not represent roles. However, this option may be of some use when
 * using pre-existing role names without a prefix, and no ability exists to
 * prefix them with a role prefix on reading them in, such as provided for
 * example in {@link org.springframework.security.userdetails.jdbc.JdbcDaoImpl}.
 * <p>
 * All comparisons and prefixes are case sensitive.
 *
 * @author Ben Alex
 * @author colin sampaleanu
 * @author dan.damian
 * @version $Id: RoleVoter.java 3209 2008-08-04 13:08:03Z luke_t $
 */
public class CMRoleVoter extends RoleVoter {
    
	/** Logger that is available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());
	

    public int vote(Authentication authentication, Object object, List<ConfigAttribute> config) {
        logger.debug("vote ------ (RolePrefix = \"" + getRolePrefix() + "\") ------------ Config: " + config + "-------------- ");
        logger.debug(authentication);
    	int result = ACCESS_ABSTAIN;
        Iterator iter = config.iterator();
        GrantedAuthority[] authorities = extractAuthorities(authentication);        
        logger.debug("authorities " + authorities);
       
        if (authorities == null) {
        	logger.debug("ACCESS_ABSTAIN");
        	return ACCESS_ABSTAIN;
        }        
        while (iter.hasNext()) {
            ConfigAttribute attribute = (ConfigAttribute) iter.next();

            if (this.supports(attribute)) {
                result = ACCESS_DENIED;
                
                // Attempt to find a matching granted authority
                for (int i = 0; i < authorities.length; i++) {
                    logger.debug("[" + authorities[i] + "]");
                	if (attribute.getAttribute().equals(authorities[i].getAuthority())) {
                		logger.debug("ACCESS_GRANTED");
                		return ACCESS_GRANTED;
                    }
                }
            }
        }
        logger.debug("vote end --------------------------------- ");
        return result;
    }
    
    GrantedAuthority[] extractAuthorities(Authentication authentication) {
    	return (GrantedAuthority[])authentication.getAuthorities().toArray();
    }
}

