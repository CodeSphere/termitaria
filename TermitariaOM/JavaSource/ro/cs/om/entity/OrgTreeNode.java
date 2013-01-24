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

import java.util.ArrayList;
import java.util.List;


/**
 * A Node from Organization's Tree
 * 
 * @author coni
 * 
 */
public class OrgTreeNode {

	
	private int departmentId;
	private int organizationId;
	private int nodeId;
	private String nodeName;
	private byte nodeType;
	private boolean isLeaf;
	private int leafNo;
	private int level;
	private int xPosition;
	private int yPosition;
	private String titleInfo;
	private List<OrgTreeNode> children;
	private int organizationStatus = -1;

	
	
	public String getTitleInfo() {
		return titleInfo;
	}

	public void setTitleInfo(String titleInfo) {
		this.titleInfo = titleInfo;
	}

	public byte getNodeType() {
		return nodeType;
	}

	public void setNodeType(byte nodeType) {
		this.nodeType = nodeType;
	}

	/**
	 * @param position the xPosition to set
	 */
	public void setXPosition(int position) {
		xPosition = position;
	}

	/**
	 * @param position the yPosition to set
	 */
	public void setYPosition(int position) {
		yPosition = position;
	}

	/**
	 * @return the nodeId
	 */
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * @param nodeId
	 *            the nodeId to set
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * @return the children
	 */
	public List<OrgTreeNode> getChildren() {
		return children;
	}

	/**
	 * Adds a new child node.
	 */
	public void addChild(OrgTreeNode node) {
		if (children != null) {
			children.add(node);
		} else {
			children = new ArrayList<OrgTreeNode>();
			children.add(node);
		}
	}

	/**
	 * @return number of Children 
	 */
	public int getChildrenNumber() {
		if (children != null) {
			return children.size();
		} else {
			return 0;
		}
	}

	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param nodeName the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * @return the isLeaf
	 */
	public boolean isLeaf() {
		return isLeaf;
	}

	/**
	 * @param isLeaf the isLeaf to set
	 */
	public void setIsLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	/**
	 * @return the leafNo
	 */
	public int getLeafNo() {
		return leafNo;
	}

	/**
	 * @param leafNo the leafNo to set
	 */
	public void setLeafNo(int leafNo) {
		this.leafNo = leafNo;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

		
	public int getXPosition() {
		return xPosition;
	}
	
	public int getYPosition() {
		return yPosition;
	}
	
	/**
	 * @return the organizationId
	 */
	public int getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	
	/**
	 * @return the departmentId
	 */
	public int getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("departmentId = ")   .append(departmentId)   .append(", ");
		sb.append("organizationId = ") .append(organizationId) .append(", ");
		sb.append("nodeId = ")         .append(nodeId)         .append(", ");
		sb.append("nodeName = ")       .append(nodeName)       .append(", ");
		sb.append("nodeType = ")       .append(nodeType)       .append(", ");
		sb.append("isLeaf = ")         .append(isLeaf)         .append(", ");
		sb.append("leafNo = ")         .append(leafNo)         .append(", ");
		sb.append("level = ")          .append(level)          .append(", ");
		sb.append("xPosition = ")      .append(xPosition)      .append(", ");
		sb.append("yPosition = ")      .append(yPosition)      .append(", ");
		sb.append("titleInfo = ")      .append(titleInfo)      .append(", ");
		sb.append("children = ")       .append(children)       .append("]");
	return sb.toString();
	}

	public int getOrganizationStatus() {
		return organizationStatus;
	}

	public void setOrganizationStatus(int organizationStatus) {
		this.organizationStatus = organizationStatus;
	}

}
