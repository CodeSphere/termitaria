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

import java.util.HashMap;

/**
 * Organisation's Tree.
 * It holds one Organization complex structure.
 * It contains Company Groups, Companies, Head Quarters and Branches, and also 
 * for each type of organization mentioned earlier, it's Departments. 
 * 
 * @author coni
 * 
 */
public class OrgTree {

	private int rootOrganisationId;

	private int levels = 0;
	private int leafsNo = 0;
	
	private OrgTreeNode root;

	/**
	 * The Tree's HashMap of Organization Nodes holding direct references to all the organization nodes in
	 * this tree. It's used for a faster retrieval of one node.
	 */
	private HashMap<Integer, OrgTreeNode> nodesHashMap = new HashMap<Integer, OrgTreeNode>();

	/**
	 * Adds a node to the Tree's OrganisationHashMap of Nodes using the node id  as key
	 */
	public void addNodeToHashMap(OrgTreeNode node) {
		nodesHashMap.put(node.getNodeId(), node);
	}

	/**
	 * Returns an Organization node in this tree identified by nodeId
	 */
	public OrgTreeNode  getNodeFromHashMap(int nodeId) {
		return nodesHashMap.get(new Integer(nodeId));
	}
	
	/**
	 * @return the root
	 */
	public OrgTreeNode getRoot() {
		return root;
	}

	/**
	 * @param root
	 *            the root to set
	 */
	public void setRoot(OrgTreeNode root) {
		this.root = root;
	}

	/**
	 * @return the rootOrganisationId
	 */
	public int getRootOrganisationId() {
		return rootOrganisationId;
	}

	/**
	 * @param rootOrganisationId
	 * the rootOrganisationId to set
	 */
	public void setRootOrganisationId(int rootOrganisationId) {
		this.rootOrganisationId = rootOrganisationId;
	}

	public void incLevels(){
		levels++;
	}
	
	public int getLevels() {
		return levels;
	}
	
	public void incLeafsNo(){
		leafsNo++;
	}
	
	public int getLeafsNo() {
		return leafsNo;
	}

	/**
	 * @param levels the levels to set
	 */
	public void setLevels(int levels) {
		this.levels = levels;
	}

	
}

