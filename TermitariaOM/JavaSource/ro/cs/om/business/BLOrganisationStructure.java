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
package ro.cs.om.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ro.cs.om.common.IConstant;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.OrgTree;
import ro.cs.om.entity.OrgTreeNode;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Person;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoDepartment;
import ro.cs.om.model.dao.IDaoOrganisation;
import ro.cs.om.model.dao.IDaoPerson;
import ro.cs.om.thread.UpdateOrganisationTree;
import ro.cs.om.utils.organigram.OrganigramUtils;
import ro.cs.tools.Tools;

/**
 *  BLTree
 *	
 *  @author coni
 *
 */
public class BLOrganisationStructure  extends BusinessLogic {

	
	private static BLOrganisationStructure theInstance = null;
    private static IDaoOrganisation orgDao = DaoBeanFactory.getInstance().getDaoOrganisation();
    private static IDaoPerson personDao = DaoBeanFactory.getInstance().getDaoPerson();
    private static IDaoDepartment departmentDao = DaoBeanFactory.getInstance().getDaoDepartment();
    
    
    //singleton implementation
    private BLOrganisationStructure(){};
    
    static {
        theInstance = new BLOrganisationStructure();
    }
    
    public static BLOrganisationStructure getInstance() {
        return theInstance;
    }
    
    /**
     * Returns an Organization tree that includes children Organizations and their departments and persons
     * @param organisationId
     * @throws BusinessException 
     */    
    public OrgTree getOrgTree(int rootOrganisationId) throws BusinessException{
    	logger.debug("getOrgTree BEGIN Organisation Id: ".concat(Integer.toString(rootOrganisationId)));
    	//The tree holding the Organization's children: other Organizations or Departments
    	OrgTree tree =  new OrgTree();
    	try {
	    	//The root of this tree ( a node representing the Organization)
	    	OrgTreeNode root = new OrgTreeNode();
	    	Organisation org = orgDao.get(rootOrganisationId); 
	    	
	    	//Setting the Root Organization's Id
	    	root.setOrganizationId(rootOrganisationId);
	    	
	    	//4Organigram: Retrieving Fake/Default Department for the Root Organization
	    	Department fakeDepartment = orgDao.getDefaultDepartment(rootOrganisationId);
	    	root.setDepartmentId(fakeDepartment.getDepartmentId());
	    	
	    	root.setNodeId(org.getOrganisationId());
	    	root.setNodeName(org.getName());
	    	root.setIsLeaf(false);
	    	root.setLevel(0);
	    	root.setOrganizationStatus(org.getStatus());
	    	
	    	//Setting the Node's Type
	    	switch(org.getType()){
	    		case IConstant.NOM_ORGANISATION_TYPE_COMPANY: root.setNodeType(IConstant.NOM_TREE_NODE_TYPE_COMPANY); break;
	    		case IConstant.NOM_ORGANISATION_TYPE_GROUP_COMPANIES: root.setNodeType(IConstant.NOM_TREE_NODE_TYPE_GROUP_COMPANIES); break;
	    		case IConstant.NOM_ORGANISATION_TYPE_HQ : root.setNodeType(IConstant.NOM_TREE_NODE_TYPE_HQ); break;
	    		case IConstant.NOM_ORGANISATION_TYPE_BRANCH : root.setNodeType(IConstant.NOM_TREE_NODE_TYPE_BRANCH); break;
	    	}
	    	
	    	//Next level nodes
	    	getAllNodesChildren(root, tree, 1);

	    	//Setting root of this tree
	    	tree.setRoot(root);
	    	tree.setRootOrganisationId(rootOrganisationId);
	    	
	    	
	    	if (root.getChildrenNumber() == 0){
	    		tree = null;
	    	}
	    	
    	}catch(Exception ex) {
    		throw new BusinessException(ICodeException.ORGANIGRAM_GET_DEPT_TREE, ex);
    	}
    	logger.debug("getOrgTree END for Organisation Id: ".concat(Integer.toString(rootOrganisationId)));
    	return tree;
    }
  
    /**
     * Finds all Node's Children and for each Child it's Children an so on...
     * @param node the Parent Node
     * @param tree the Tree
     */
    public void getAllNodesChildren(OrgTreeNode node, OrgTree tree, int level){
    	logger.debug("getNodeChildren BEGIN NodeId: ".concat(String.valueOf(node.getNodeId())).
    					concat(" of Type: ").concat(String.valueOf(node.getNodeType())));
    	
    	//Add Node to HashMap
    	tree.addNodeToHashMap(node);
    	
    	//Increment level if it's the case
    	if (level > tree.getLevels()) {
    		tree.setLevels(level);
    	}
    	
    	//******************************************************************************************
    	//FIND BIGGER CHILDREN: Companies, Head Quarters, Branches, SubBranches 
    	//******************************************************************************************
    	//If the node is an Organization I need to find all its Organization children and all its Departments
    	if (node.getNodeType() == IConstant.NOM_TREE_NODE_TYPE_COMPANY 			|| 
    		node.getNodeType() == IConstant.NOM_TREE_NODE_TYPE_GROUP_COMPANIES 	||
    		node.getNodeType() == IConstant.NOM_TREE_NODE_TYPE_HQ 				|| 
    		node.getNodeType() == IConstant.NOM_TREE_NODE_TYPE_BRANCH ) {
    		
    		//Find all it's children but represented by their fake/department Department
    		List<Department> fakeDepartments = orgDao.getDefaultDepartmentsForParentId(node.getOrganizationId());
    		
    		for(Department d: fakeDepartments){
    				//Create a new node
    				OrgTreeNode subOrgNode = new OrgTreeNode();
    				
    				//4Organigram: Setting fake/default departmentId
    				subOrgNode.setDepartmentId(d.getDepartmentId());
	    			//4Organigram: Setting organizationId (this node is actually representing an Organization)
    				subOrgNode.setOrganizationId(d.getOrganisation().getOrganisationId());

    				//Setting the node id ( an Organization id)
    				subOrgNode.setNodeId(d.getOrganisation().getOrganisationId());
    				
    				//Setting node name (Organization's name, for the fact that this node is representing an Organization)
    				subOrgNode.setNodeName(d.getOrganisation().getName());
    				
    				switch(d.getOrganisation().getType()){
    					case IConstant.NOM_ORGANISATION_TYPE_COMPANY: 
    							subOrgNode.setNodeType(IConstant.NOM_TREE_NODE_TYPE_COMPANY); break;
    					case IConstant.NOM_ORGANISATION_TYPE_GROUP_COMPANIES: 
    							subOrgNode.setNodeType(IConstant.NOM_TREE_NODE_TYPE_GROUP_COMPANIES); break;
    					case IConstant.NOM_ORGANISATION_TYPE_HQ : 
    							subOrgNode.setNodeType(IConstant.NOM_TREE_NODE_TYPE_HQ); break;
    					case IConstant.NOM_ORGANISATION_TYPE_BRANCH : 
    							subOrgNode.setNodeType(IConstant.NOM_TREE_NODE_TYPE_BRANCH); break;
    				}

    				subOrgNode.setLevel(level);
    				subOrgNode.setOrganizationStatus(d.getOrganisation().getStatus());
    				
    				//Adding Child to Parent's Children list
    				node.addChild(subOrgNode);
    				
    				//And so on...
    				getAllNodesChildren(subOrgNode, tree, level + 1);
    				//************************************************
    		} 
    	} 
    	
    	//******************************************************************************************
    	//FIND SMALLER CHILDREN: Departments 
    	//******************************************************************************************
    	List<Department> firstLevelDepartments = null;

    	firstLevelDepartments = departmentDao.listFirstLevelSubDepartments(node.getDepartmentId());	
        
    	//Is this node a Leaf ?
    	if (firstLevelDepartments.size() == 0 && node.getChildrenNumber() == 0) {
			node.setIsLeaf(true);
			tree.incLeafsNo();
			node.setLeafNo(tree.getLeafsNo());
		
			//4Organigram: Calculating x,y position
			node.setXPosition((tree.getLeafsNo() - 1) * (OrganigramUtils.NODE_WIDTH + OrganigramUtils.H_SPACE) + OrganigramUtils.H_SPACE);
    		node.setYPosition((level - 1) * (OrganigramUtils.NODE_HEIGHT + OrganigramUtils.V_SPACE) + OrganigramUtils.V_SPACE);
		
			//If this is a Leaf we can stop !
			return;
   		} 
    	
		for (Department d: firstLevelDepartments){
			//Create a new Node
			OrgTreeNode subDeptNode = new OrgTreeNode();
			//Setting departmentId
    		subDeptNode.setDepartmentId(d.getDepartmentId());
			//Setting the node id ( a department id) , name and type
			subDeptNode.setNodeId(d.getDepartmentId());
			subDeptNode.setNodeName(d.getName());    
			subDeptNode.setNodeType(IConstant.NOM_TREE_NODE_TYPE_DEPARTMENT);
			
			subDeptNode.setLevel(level);

			//Adding Child to Parent's Children list
			node.addChild(subDeptNode);
			
			//And so on...
			getAllNodesChildren(subDeptNode, tree, level + 1);
			//************************************************
   		}

    
		//4Organigram: Calculating x,y position
		node.setXPosition( (node.getChildren().get(0).getXPosition() +  node.getChildren().get(node.getChildrenNumber() - 1).getXPosition()) / 2);
		node.setYPosition( (OrganigramUtils.NODE_HEIGHT + OrganigramUtils.V_SPACE) * (level - 1) + OrganigramUtils.V_SPACE);
		logger.debug("Node " + node.getNodeId() + " of type " + node.getNodeType() + " with " + node.getChildrenNumber() + " children");
    }
    
    /**
     * @return a HashMap with ALL Organisation's Departments Trees
     * @throws BusinessException 
     */
    public HashMap<Integer, OrgTree> getAllOrganisationTrees() throws BusinessException {
		logger.debug("getAllOrganisationTrees BEGIN");
    	HashMap<Integer, OrgTree> trees = new HashMap<Integer, OrgTree>();
		//Retrieving Organizations list
		List<Organisation> organisations = orgDao.list();
		Organisation org = null;
		//For each Organization:
		for(int i =0; i < organisations.size(); i++) {
			org = organisations.get(i);
			//Generate it's Tree of Departments and add it to the HashMap
			OrgTree organisationTree = getOrgTree(org.getOrganisationId());
			logger.debug("Organisation " + org.getOrganisationId() + " tree " + organisationTree);
			trees.put(new Integer(org.getOrganisationId()), organisationTree);
		}
		logger.debug("getAllOrganisationDepartmentsTrees END");
		return trees;
	}
    
    
    
    /**
     * Returns a subTree form Organisation's Departments Tree, with 
     * root being this Node
     */
    
    public List<Integer> getSubTreeAsList(OrgTreeNode node) {
    	logger.debug("getSubTreeAsList BEGIN for node ".concat(node.toString()));
    	List<Integer> departmentIds = new ArrayList<Integer>();
    	//Node's Children
    	List<OrgTreeNode> children = node.getChildren();
    	if (children == null) { return departmentIds; }
    	logger.debug("Children: " + children.size());
    	for(int i = 0; i < children.size(); i++) {
    		//Adds this Child to list
    		departmentIds.add(children.get(i).getNodeId());
    		//Adds Child's Children to list
    		departmentIds.addAll(getSubTreeAsList(children.get(i)));
    	}
    	logger.debug("return ".concat(Integer.toString(departmentIds.size())).concat(" departmentIds"));
    	return departmentIds;
    }
	
    
    /**
     * Returns a list of Persons being all inferior to this Person
     * @author dan.damian
     * 
     * @param personId this Person id
     * @return
     * @throws BusinessException
     */
    public List<Person> getInferiors(int organisationId, int personId) throws BusinessException {
    	logger.debug("getInferiors for " + personId);
    	//List with inferiors
    	List<Person> inferiors = new ArrayList<Person>();
    	try {
	    	//Getting Department Id for which this Person is a Manager
	    	List<Department> departments = departmentDao.getForManager(personId);
	    	if (departments == null) {
	    		logger.debug("This Person is not a Manager");
	    		return null;
	    	} else {
	    		logger.debug("This person is Manager over this Departments: ");
	    		Tools.getInstance().printList(logger, departments);
	    	}
	    	//Retrieving Organisation's Departments Tree
	    	OrgTree tree = getOrgTree(organisationId);
	    	//Retrieve Node information for Departments over this person is a Manager
	    	Set<Integer> departmentsSetIds = new TreeSet<Integer>();
	    	for(int i = 0; i < departments.size(); i++) {
		    	//This node is considered the Root of this Departments SubTree
		    	OrgTreeNode root = tree.getNodeFromHashMap(departments.get(i).getDepartmentId());
		    	logger.debug("ROOT OF SUBTREE " + root);
		    	//Retrieve a list containing all the Departments from this SubTree
		    	List<Integer> subDepartmentsIds = getSubTreeAsList(root);
		    	subDepartmentsIds.add(new Integer(root.getNodeId()));
		    	logger.debug("PRINT CE A RETURNAT PENTRU DEP ROOT " + root);
		    	Tools.getInstance().printList(logger, subDepartmentsIds);
		    	departmentsSetIds.addAll(subDepartmentsIds);
	    	}
	    	logger.debug("PRINT CE A RETURNAT LA FINAL");
	    	Tools.getInstance().printSet(logger, departmentsSetIds);
	    	Integer[] departmentIdsArray = new Integer[departmentsSetIds.size()];
	    	departmentsSetIds.toArray(departmentIdsArray);
	    	inferiors = personDao.getFromDepartments(departmentIdsArray);
    	} catch(Exception ex) {
    		throw new BusinessException(ICodeException.ORGANIGRAM_GET_INFERIORS, ex);
    	}
    	
    	return inferiors; 
    }
    
    /**
     * Returns a list of Persons being all superior to this Person
     * @author dan.damian
     * 
     * @param personId this Person id
     * @return
     * @throws BusinessException
     */
    public List<Person> getSuperiors(int personId) throws BusinessException {
    	//Finding in which departments this person resides
    	Person hero = personDao.getWithDepartments(personId);
    	Set<Department> herosDepartments = hero.getDepts();
    	//TODO getSuperiors to be implemented if needed !
    	return null;
    }
    
    /**
     *  Updates all organizations structures that include a specific organization 
     *  after any modification in its structure
     * 	@author coni
     * 
     */
    public void updateAllOrganisationsStructure(int organizationId){
    	logger.debug("Start updating all the organizations structures that include the organization with id: ".concat(String.valueOf(organizationId)));
    	updateOrgStructure(organizationId);
    	logger.debug("End updating all the organizations structures that include the organization with id: ".concat(String.valueOf(organizationId)));
    }
    
    /**
     * Called recursive in order to update all the organizations structures that include this one as a child node 
     * @author coni
     * @param organisationId
     */
	public void updateOrgStructure(int organisationId){
		logger.debug("Start the thread that updates the organization structure for organization ID :".concat(String.valueOf(organisationId)));
		//Start the thread that updates the organization structure
		new Thread(new UpdateOrganisationTree(organisationId)).start();
		try {
			Organisation org = BLOrganisation.getInstance().get(organisationId);
			//if the organization has a parent organization, I have to update its structure too
			if (org.getParentId() != -1){
				updateOrgStructure(org.getParentId());
			}
		} catch (Exception e){
			logger.error("ERROR at obtaining the organisation with id: ".concat(String.valueOf(organisationId).concat(" in UpdateAllOrganisationsTrees ")), e);
		}
	}
}
