/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *    Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *     
 *    Termitaria is free software; you can redistribute it and/or 
 *    modify it under the terms of the GNU Affero General Public License 
 *    as published by the Free Software Foundation; either version 3 of 
 *    the License, or (at your option) any later version.
 *    
 *    This program is distributed in the hope that it will be useful, 
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *    GNU Affero General Public License for more details.
 *    
 *    You should have received a copy of the GNU Affero General Public License 
 *    along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.ts.web.validator;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ro.cs.ts.business.BLCurrency;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.web.security.UserAuth;


/**
 * Validator used to check the uniqueness of the name and initials of a currency
 * @author alexandru.dobre
 *
 */
public class CurrencyFormValidator implements Validator{
	
	private static Log logger = LogFactory.getLog(CurrencyFormValidator.class);
	

	@Override
	public boolean supports(Class clazz) {
		return Currency.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {		
		logger.debug("Validate currency START>");
		Currency currency = (Currency) target;
		
		logger.debug("Currency name: "+currency.getName());
		logger.debug("Currency initials: "+currency.getInitials());
		logger.debug("Currency ID: "+currency.getCurrencyId());
		
		try {
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Integer organisationId = new Integer (userAuth.getOrganisationId());
			List<Currency> currencyList = BLCurrency.getInstance().getByNameOrInitials(currency.getName(), currency.getInitials(), organisationId);
			
			boolean rejectName = false;
			boolean rejectInitials = false;
			
			if (currency.getCurrencyId() <= 0){
				logger.debug("We do not have a currency ID defined, we are trying to add a new currency");
				if (currencyList != null && !currencyList.isEmpty()){
					for (Currency matchedCurrency : currencyList){
						if (matchedCurrency.getName().equals(currency.getName())) rejectName= true;
						if (matchedCurrency.getInitials().equals(currency.getInitials())) rejectInitials = true;
					}
				}
				
			}else {
				logger.debug("We are trying to update a currency");
				if (currencyList != null && !currencyList.isEmpty()){
					for (Currency matchedCurrency : currencyList){
						if (matchedCurrency.getCurrencyId() != currency.getCurrencyId()){
							if (matchedCurrency.getName().equals(currency.getName())) rejectName= true;
							if (matchedCurrency.getInitials().equals(currency.getInitials())) rejectInitials = true;
						}
					}
				}
				
			}
			
			if (rejectName)	errors.rejectValue("name", "currency.form.name.unique.error");
			if (rejectInitials)	errors.rejectValue("initials", "currency.form.initials.unique.error");
			
		} catch (BusinessException e) {
			errors.rejectValue("name", "currency.form.server.error");
			errors.rejectValue("initials", "currency.form.server.error");
			logger.error("", e);		
		}
		
		
		
		logger.debug("Validate currency END>");
		
	}

}
