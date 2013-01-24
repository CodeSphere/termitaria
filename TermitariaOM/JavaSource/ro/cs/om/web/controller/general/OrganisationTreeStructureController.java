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
/**
 * @author coni
 * Used to delete an organization or to modify its status directly from a tree structure,
 * update the tree stored on context and render the new structure
 * 
 */
package ro.cs.om.web.controller.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLOrganisationStructure;
import ro.cs.om.business.BLPerson;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.OrgTree;
import ro.cs.om.entity.OrgTreeNode;
import ro.cs.om.entity.Organisation;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;
import ro.cs.om.web.security.UserAuth;

public class OrganisationTreeStructureController extends RootAbstractController{
	
	//------------------------ATTRIBUTES--------------------------------------------------------------
	private static String ACTION						= "action";
	private static String DELETE						= "delete";
	private static String ENABLE						= "enable";
	private static String DISABLE						= "disable";
	private static String CURRENT_ORGANIZATION_ID		= "currentOrganisationId";
	
	//------------------------MODEL-------------------------------------------------------------------
	private static String FIRST_LEVEL_NODES 			= "FIRST_LEVEL_NODES";
	private static String MODEL_ORGANIZATION			= "ORGANIZATION";	
	
	//------------------------MESSAGES----------------------------------------------------------------
	private static final String DELETE_ERROR 								= "organisation.delete.error";
	private static final String DELETE_SUCCESS 								= "organisation.delete.success";
	private static final String UPDATE_ORGANIZATIONS_TREE_STRUCTURE_ERROR	= "organisation.update.all.trees.error";
	private static final String UPDATE_SUCCESS 								= "organisation.update.success";
	private static final String UPDATE_ERROR 								= "organisation.update.error";
	private static final String ORGANIZATION_GET_ERROR						= "organisation.get.error";
	private static final String ORGANISATION_HAS_ADMIN						= "organisation.has.admin.error";
	
	public OrganisationTreeStructureController(){
		setView("OrganisationTreeStructure");
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response){
		logger.debug("handleRequestInternal - START");
		ModelAndView mav = null;
		// used as a container for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
							
		try {
			//if the action parameter doesn't exist on the request, I have to retrieve the organization's stored on session tree structure, 
			//otherwise I have to delete an organization/update an organization status and update all the organizations trees map stored on the context
			if (request.getParameter(ACTION) != null){				
				Integer organizationId = Integer.parseInt(ServletRequestUtils.getRequiredStringParameter(request, CURRENT_ORGANIZATION_ID));												
				if (request.getParameter(ACTION).equals(DELETE)){
					logger.debug("Is delete organization action for organizationId: ".concat(organizationId.toString()));
					mav = handleDeleteOrganization(organizationId, request, errorMessages, infoMessages);
				} else if (request.getParameter(ACTION).equals(ENABLE) || request.getParameter(ACTION).equals(DISABLE)){
					logger.debug("Is update organization status action for organizationId: ".concat(organizationId.toString()));
					mav = handleUpdateOrganizationStatus(organizationId, request, errorMessages, infoMessages);
				}
			} else {
				Integer organizationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
				logger.debug("Get organization tree structure for organizationId: ".concat(organizationId.toString()));
				// ****************** Security *******************************
				UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_ViewTreeComplete())) {
					mav = getOrganizationTreeStructure(organizationId, true, request, errorMessages);
				} else {
					mav = getOrganizationTreeStructure(organizationId, false, request, errorMessages);
				}
			}
		} catch ( Exception e) {
			logger.error("Error at getting required request param", e);
		}
		
		logger.debug("handleRequestInternal - END");
		return mav;
	}

	/**
	 * Deletes the organization chosen from the tree structure
	 * @param organizationId
	 * 
	 */
	protected ModelAndView handleDeleteOrganization(Integer organizationId, HttpServletRequest request,
			ArrayList<String> errorMessages, ArrayList<String> infoMessages ){
		logger.debug("handleDeleteOrganization - START");
		
		Locale locale = RequestContextUtils.getLocale(request);
		HashMap<Integer, String> orgIdsAndNames = null;
		int parentId = -1;
		boolean isDeleted = true;
		ModelAndView mav = new ModelAndView(getView());
		// the user that is authenticated
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//Test if the has the permission to delete children organizations of its own organization
		if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_OrgChildDelete())){
			try {
				Organisation organisation = BLOrganisation.getInstance().get(organizationId);
				if (organisation != null) {
					//delete the organization and its children organizations; the map returned contains all 
					//the deleted organizations names and their Ids
					orgIdsAndNames = (HashMap<Integer, String>) BLOrganisation.getInstance().deleteOrgs(organizationId);
					parentId = organisation.getParentId();
				} else {
					orgIdsAndNames = null;
				}
			} catch (BusinessException be) {
				logger.error("Error while trying to delete organization", be);
				errorMessages.add(messageSource.getMessage(DELETE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime() },
						RequestContextUtils.getLocale(request)));
				isDeleted = false;
			}
		
			if (isDeleted && orgIdsAndNames != null) {
				//get the organizations trees map stored on context and remove from it the tree of the organizations that have been deleted
				HashMap<Integer, OrgTree> allOrganisationsTrees = (HashMap<Integer, OrgTree>) OMContext
				.getOrganisationsTreesMapFromContext(IConstant.ORGANISATION_TREES);
				Set<Integer> orgIds	= (Set<Integer>)orgIdsAndNames.keySet();
				for (Integer orgId : orgIds){
					allOrganisationsTrees.remove(orgId);
				}
				//add messages of successfully deleted organizations
				Set<String> names = new HashSet<String>();
				names.addAll(orgIdsAndNames.values());
				for (String name : names) {
					infoMessages.add(messageSource.getMessage(DELETE_SUCCESS, new Object[] { name },RequestContextUtils.getLocale(request)));
				}
				if (parentId != -1) {
					try {
						BLOrganisationStructure.getInstance().updateAllOrganisationsStructure(parentId);
					} catch (Exception exc) {
						logger.error("Error at updating the parent organization tree structure", exc);
						errorMessages.add(messageSource.getMessage(UPDATE_ORGANIZATIONS_TREE_STRUCTURE_ERROR,new Object[] {exc.getMessage(),
								ControllerUtils.getInstance().getFormattedCurrentTime() }, RequestContextUtils.getLocale(request)));
					} 
				}
			}
		} else {
			errorMessages.add(messageSource.getMessage(
					IConstant.SECURITY_NO_RIGHTS, null, locale));
		}
		logger.debug("handleDeleteOrganization - END");
		return mav;
	}
	
	/**
	 * Updates the status of the organization chosen from the tree structure
	 * @param organizationId
	 * 
	 */
	protected ModelAndView handleUpdateOrganizationStatus(Integer organizationId, HttpServletRequest request,
			ArrayList<String> errorMessages, ArrayList<String> infoMessages ){
		logger.debug("handleUpdateOrganizationStatus - START");
		Locale locale = RequestContextUtils.getLocale(request);
		ModelAndView mav = new ModelAndView(getView());
		
		try {
			// the user that is autenticated
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			// ****************** Security *******************************
			// test if the user has the permission to update the organization status 
			if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_OrgChildChangeStatus())) {
				Organisation organisation = BLOrganisation.getInstance().get(organizationId);
				BLOrganisation.getInstance().updateStatus(organizationId);
				BLOrganisationStructure.getInstance().updateAllOrganisationsStructure(organizationId);
				infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS,new Object[] { organisation.getName() },RequestContextUtils.getLocale(request)));
			} else {
				// if the user doesn't have permission to update the organization status, an error message will be displayed
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(),ControllerUtils.getInstance()
				.getFormattedCurrentTime() }, RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] { ControllerUtils.getInstance().getFormattedCurrentTime() }, RequestContextUtils
				.getLocale(request)));
		}
		logger.debug("handleUpdateOrganizationStatus - END");
		return mav;
	}
	
	
	/**
	 * Retrieves the organization tree structure
	 * @param organizationId
	 * 
	 */
	protected ModelAndView getOrganizationTreeStructure(Integer rootOrganisationId, boolean getAllTreeStructure, 
			HttpServletRequest request, ArrayList<String> errorMessages){
		logger.debug("getOrganizationTreeStructure - START");
		ModelAndView mav = new ModelAndView(getView());
		//Retrieving Organization Tree of Organizations, Departments from App Context 
		HashMap<Integer, OrgTree> treesMap = (HashMap<Integer, OrgTree>)OMContext.getOrganisationsTreesMapFromContext(IConstant.ORGANISATION_TREES);
		OrgTree tree = treesMap.get(rootOrganisationId);
		if (tree == null) {
			logger.debug("tree null");
			return mav;
		}
		//Retrieving the root of the tree
		OrgTreeNode root = tree.getRoot();
		List<OrgTreeNode> children = root.getChildren();
		//Building a JSON Representation of the first level of the Organization Tree
		JSONArray firstLevelNodes = new JSONArray();
		OrgTreeNode node = null;
		for(int i=0; i < root.getChildrenNumber();i++){
			node = children.get(i);	
			if ( (getAllTreeStructure & node.getNodeType() != IConstant.NOM_TREE_NODE_TYPE_DEPARTMENT && node.getNodeType() != IConstant.NOM_TREE_NODE_TYPE_PERSON)
					|| node.getNodeType() == IConstant.NOM_TREE_NODE_TYPE_DEPARTMENT ){
				JSONObject firstLevelNode = new JSONObject();
				firstLevelNode.accumulate("label", node.getNodeName());				
				//labelId contains the query string sent to back to server when a node from the tree is clicked;
				//currentOrganisationId is used to retrieve all the persons from a department and represents the 
				//organisation the department belongs to
				if ( node.getNodeType() == IConstant.NOM_TREE_NODE_TYPE_DEPARTMENT ){
					firstLevelNode.accumulate("labelElId", "currentOrganisationId=" + tree.getRootOrganisationId() + "&nodeId=" + node.getNodeId() + "&type=" + node.getNodeType());				
					firstLevelNode.accumulate("departmentId", node.getDepartmentId());				
				}
				else if ( node.getNodeType() != IConstant.NOM_TREE_NODE_TYPE_PERSON ){
					firstLevelNode.accumulate("labelElId", "currentOrganisationId=" + node.getNodeId() + "&nodeId=" + node.getNodeId() + "&type=" + node.getNodeType());
					firstLevelNode.accumulate("organizationStatus", node.getOrganizationStatus());
				}
				firstLevelNode.accumulate("isLeaf", node.getChildrenNumber() == 0 ? true: false);
				
				if(node.getChildrenNumber() == 0) {										 
					try {
						boolean hasAdmin = BLPerson.getInstance().hasAdminByOrganisation(node.getOrganizationId());													
						firstLevelNode.accumulate("hasAdmin", hasAdmin);
						firstLevelNode.accumulate("orgChildId", node.getOrganizationId());
					} catch(BusinessException bexc){
						logger.error("", bexc);
						errorMessages.add(messageSource.getMessage(ORGANISATION_HAS_ADMIN, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
					} catch(Exception ex){
						logger.error("", ex);
						errorMessages.add(messageSource.getMessage(ORGANISATION_HAS_ADMIN, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
					}					
				} else {
					firstLevelNode.accumulate("hasAdmin", true);
					firstLevelNode.accumulate("orgChildId", -1);
				}
				
				firstLevelNode.accumulate("type", node.getNodeType());
				firstLevelNode.accumulate("title", node.getTitleInfo());
				//adding this department to jsonArray
				firstLevelNodes.add(firstLevelNode);
			}
		}		
		mav.addObject(FIRST_LEVEL_NODES, firstLevelNodes.toString());
		try {			
			mav.addObject(MODEL_ORGANIZATION, BLOrganisation.getInstance().get(rootOrganisationId));
		} catch (BusinessException bexc) {
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(ORGANIZATION_GET_ERROR, new Object[] {bexc.getCode(),ControllerUtils.getInstance()
					.getFormattedCurrentTime() }, RequestContextUtils.getLocale(request)));
		}
		logger.debug("getOrganizationTreeStructure - END");
		return mav;
	}

}
