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
package ro.cs.om.entity;

import java.util.Date;

import ro.cs.om.common.IConstant;
import ro.cs.om.utils.common.StringUtils;

/**
 * @author matti_joona
 *
 */
public class OutOfOffice {

	private Person person;
	private int outOfOfficeId;
	private Date startPeriod;
	private Date endPeriod;
	private Person personReplacement;
	private String observation;
	/**
	 * @return the outOfOfficeId
	 */
	public int getOutOfOfficeId() {
		return outOfOfficeId;
	}
	/**
	 * @param outOfOfficeId the outOfOfficeId to set
	 */
	public void setOutOfOfficeId(int outOfOfficeId) {
		this.outOfOfficeId = outOfOfficeId;
	}
	/**
	 * @return the startPeriod
	 */
	public Date getStartPeriod() {
		return startPeriod;
	}
	/**
	 * @param startPeriod the startPeriod to set
	 */
	public void setStartPeriod(Date startPeriod) {
		this.startPeriod = startPeriod;
	}
	/**
	 * @return the endPeriod
	 */
	public Date getEndPeriod() {
		return endPeriod;
	}
	/**
	 * @param endPeriod the endPeriod to set
	 */
	public void setEndPeriod(Date endPeriod) {
		this.endPeriod = endPeriod;
	}
	/**
	 * @return the personReplacement
	 */
	public Person getPersonReplacement() {
		return personReplacement;
	}
	/**
	 * @param personReplacement the personReplacement to set
	 */
	public void setPersonReplacement(Person personReplacement) {
		this.personReplacement = personReplacement;
	}
	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}
	/**
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}
	
	
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public String getTokenizedObservation() {
		return StringUtils.getInstance().tokenizedString(this.observation, IConstant.OOO_TEXT_AREA_ROW_SIZE);
	}

	public String getTruncatedTokenizedObservation() {
		return StringUtils.getInstance().truncatedString(this.observation, IConstant.OOO_TEXT_AREA_ROW_SIZE, IConstant.OOO_TEXT_AREA_SIZE);
	}
	
	/**
	 * @author dd
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("outOfOfficeId = ")	.append(outOfOfficeId)	.append(", ");
		sb.append("startPeriod = ")	.append(startPeriod)	.append(", ");
		sb.append("endPeriod = ")	.append(endPeriod)	.append(", ");
		sb.append("observation = ")			.append(observation)		.append("]");
		return sb.toString();
	}

}
