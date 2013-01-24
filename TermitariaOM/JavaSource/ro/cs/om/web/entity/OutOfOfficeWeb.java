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
package ro.cs.om.web.entity;

import java.util.Date;

import ro.cs.om.common.IConstant;
import ro.cs.om.utils.common.StringUtils;

/**
 * @author alu
 *
 */
public class OutOfOfficeWeb {
	
	private Integer personId;
	private int outOfOfficeId = -1;
	private Date startPeriod;
	private Date endPeriod;
	private Integer personReplacementId = -1;
	private String observation;
	
	public Integer getPersonId() {
		return personId;
	}
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	public int getOutOfOfficeId() {
		return outOfOfficeId;
	}
	public void setOutOfOfficeId(int outOfOfficeId) {
		this.outOfOfficeId = outOfOfficeId;
	}
	public Date getStartPeriod() {
		return startPeriod;
	}
	public void setStartPeriod(Date startPeriod) {
		this.startPeriod = startPeriod;
	}
	public Date getEndPeriod() {
		return endPeriod;
	}
	public void setEndPeriod(Date endPeriod) {
		this.endPeriod = endPeriod;
	}
	public Integer getPersonReplacementId() {
		return personReplacementId;
	}
	public void setPersonReplacementId(Integer personReplacementId) {
		this.personReplacementId = personReplacementId;
	}
	public String getObservation() {
		return observation;
	}
	public void setObservation(String observation) {
		this.observation = observation;
	}	
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("outOfOfficeId = ")	.append(outOfOfficeId)		.append(", ");
		sb.append("startPeriod = ")		.append(startPeriod)		.append(", ");
		sb.append("endPeriod = ")		.append(endPeriod)			.append(", ");
		sb.append("observation = ")		.append(observation)		.append("]");
		return sb.toString();
	}
}
