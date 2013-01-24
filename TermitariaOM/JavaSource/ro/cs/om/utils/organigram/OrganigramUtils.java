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
package ro.cs.om.utils.organigram;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import net.sf.json.JSONObject;

import org.springframework.security.core.context.SecurityContextHolder;

import ro.cs.om.business.BLDepartment;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.common.ApplicationObjectSupport;
import ro.cs.om.common.ConfigParametersProvider;
import ro.cs.om.common.IConstant;
import ro.cs.om.entity.OrgTree;
import ro.cs.om.entity.OrgTreeNode;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Person;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.utils.file.FileUtils;
import ro.cs.om.utils.image.ImageUtils;
import ro.cs.om.web.security.UserAuth;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author dan.damian
 *
 */
public class OrganigramUtils extends ApplicationObjectSupport {

	
	private static String REPLACE_THEME = "replaceTheme";
	
	private static String ORGANISATION_NODE_PICTURE_LOCATION = "themes".concat(File.separator).
											concat(REPLACE_THEME).concat(File.separator).concat("images").
											concat(File.separator).concat("organigram").concat(File.separator).concat("Org.jpg");	
	
	private static String DEPARTMENT_NODE_PICTURE_LOCATION = "themes".concat(File.separator).
											concat(REPLACE_THEME).concat(File.separator).concat("images").
											concat(File.separator).concat("organigram").concat(File.separator).concat("Dept.jpg");
	
	
	
	//Parameters for drawing
	public static int 	 H_SPACE 			= 0;
	public static int 	 V_SPACE 			= 0;
	public static int 	 NODE_WIDTH 		= 0;
	public static int 	 NODE_HEIGHT 		= 0;
	private static int 	 LABEL_MAX_LENGTH 	= 0;
	private static Color LINE_COLOR 		= null;
	private static Color TEXT_COLOR_TITLE	= null;
	private static Color TEXT_COLOR_MANAGER	= null;
	
	
	private static OrganigramUtils theInstance = null;
	
	
	static{
		theInstance = new OrganigramUtils();
		try {
			H_SPACE = ConfigParametersProvider.getConfigStringAsInt("organigram.H_SPACE");
			V_SPACE = ConfigParametersProvider.getConfigStringAsInt("organigram.V_SPACE");
			NODE_WIDTH = ConfigParametersProvider.getConfigStringAsInt("organigram.NODE_WIDTH");
			NODE_HEIGHT = ConfigParametersProvider.getConfigStringAsInt("organigram.NODE_HEIGHT");
			LABEL_MAX_LENGTH = ConfigParametersProvider.getConfigStringAsInt("organigram.LABEL_MAX_LENGTH");
			LINE_COLOR = ConfigParametersProvider.getConfigStringAsColor("organigram.LINE_COLOR");
			TEXT_COLOR_TITLE = ConfigParametersProvider.getConfigStringAsColor("organigram.TEXT_COLOR_TITLE");
			TEXT_COLOR_MANAGER = ConfigParametersProvider.getConfigStringAsColor("organigram.TEXT_COLOR_MANAGER");
		}catch(Exception ex) {
			
		}
	}
	
	private OrganigramUtils() {}
	
	public static OrganigramUtils getInstance() {
		return theInstance;
	}
	
	
	
	
	/**
	 * Draw a Node from the Organigram
	 * @author dan.damian 
	 */
	private void drawNode(Graphics2D g, OrgTreeNode node, String nodeOrgImg, String nodeDeptImg, boolean root) {
		//First draw the Node Info
		drawNodeInfo(g, node, nodeOrgImg, nodeDeptImg);
		if (!root) {
			//If it's not a root Node draw top Line
			drawTopLine(g, node);
		} 
		
		if ( node.getChildrenNumber() > 0) {
			//If it has children
			drawBottomLine(g, node);
			//It it has more than one child
			if (node.getChildrenNumber() > 1) {
				drawHorizontalLine(g, node);
			}
			//For all it's children
			for(int i = 0; i < node.getChildrenNumber(); i++) {
				//Do the same...
				drawNode(g, node.getChildren().get(i), nodeOrgImg, nodeDeptImg, false);
			}
		}
			
	}
	
	/**
	 * Draw Node's Informations and the Rectangle representing the Node itself
	 * @author dan.damian 
	 */
	private void drawNodeInfo(Graphics2D g, OrgTreeNode node, String nodeOrgImg, String nodeDeptImg) {
		//Draw a Rectangle representing the Node
		

		String imgUrl = nodeDeptImg;
		if (node.getNodeType() != IConstant.NOM_TREE_NODE_TYPE_DEPARTMENT) {
			imgUrl = nodeOrgImg;
		}

		
		try {
			g.drawImage(ImageIO.read(new File(imgUrl)), node.getXPosition(), node.getYPosition(), Color.blue, null);
		} catch (IOException e) {
			g.drawRect(node.getXPosition(), node.getYPosition(), NODE_WIDTH, NODE_HEIGHT);			
		}
		
		
		String label = node.getNodeName();
		//If Department Name it's too big will short it a little
		if (label.length() > LABEL_MAX_LENGTH) {
			label = label.substring(0, LABEL_MAX_LENGTH - 3);
			label +="...";
		}
		
		//Switch to Text Color for Title
		g.setColor(TEXT_COLOR_TITLE);
		g.drawString(label, node.getXPosition() + (NODE_WIDTH - label.length() * 6) / 2, node.getYPosition() + 15);
		
		//Retrieving Department's Manager
		Person manager =  null;
		try {
			manager = BLDepartment.getInstance().getDepartmentsManager(node.getDepartmentId());
		} catch(Exception ex) {}
		
		String managerName = "";
		
		if (manager != null)  {
			managerName = manager.getFirstName().concat(" ").concat(manager.getLastName());
			
			if (managerName.length() > LABEL_MAX_LENGTH) {
				managerName = managerName.substring(0, LABEL_MAX_LENGTH - 3);
				managerName +="...";
			}
		}
		//Switch to Text Color for Manager		
		g.setColor(TEXT_COLOR_MANAGER);
		g.drawString(managerName, node.getXPosition() + (NODE_WIDTH - managerName.length() * 6) / 2, node.getYPosition() + 40);
		//Back to line Color
		g.setColor(LINE_COLOR);
	}
	
	/**
	 * Draws the Top Line 
	 * @author dan.damian 
	 */
	private void drawTopLine(Graphics2D g, OrgTreeNode node) {
		g.drawLine(node.getXPosition() + (NODE_WIDTH / 2), node.getYPosition() - (V_SPACE / 2), node.getXPosition() + 
				(NODE_WIDTH / 2), node.getYPosition() - 2);
	}

	/**
	 * Draws the Bottom Line
	 * @author dan.damian 
	 */
	private void drawBottomLine(Graphics2D g, OrgTreeNode node) {
		g.drawLine(node.getXPosition() + (NODE_WIDTH / 2), node.getYPosition() + NODE_HEIGHT, node.getXPosition() + 
			(NODE_WIDTH / 2), node.getYPosition() + NODE_HEIGHT + V_SPACE /  2);
	}
	
	/**
	 * Draws the Horizontal Line
	 * @author dan.damian 
	 */
	private void drawHorizontalLine(Graphics2D g, OrgTreeNode node) {
		g.drawLine(node.getChildren().get(0).getXPosition() + (NODE_WIDTH / 2),
			node.getYPosition() + NODE_HEIGHT + (V_SPACE / 2), node.getChildren().
			get(node.getChildrenNumber() - 1).getXPosition() + (NODE_WIDTH / 2), 
			node.getYPosition() + NODE_HEIGHT + (V_SPACE / 2));
	}

	/**
	 * Creates the PDF file containing a JPEG Picture representing our Organigram.
	 * It then writes this PDF File in an OutputStream.
	 * Particular the OutputStream it's ServletOutputStream.
	 * @author dan.damian 
	 */
	public void createPDFOrganigram(OutputStream os, OrgTree tree) throws Exception {
		//The PDF Document
		Document document = new Document();
		//Organigram dimensions
		int height 	= calculateOrganigramHeight(tree);
		int width 	= calculateOrganigramWidth(tree);
		//Setting Document's dimensions
		document.setPageSize(new Rectangle(width + 200, height + 200));
		
		PdfWriter.getInstance(document, os);
		document.open();
		
		// Retrieving the Organization
		Organisation org = BLOrganisation.getInstance().get(tree.getRoot().getOrganizationId());
		document.add(new Paragraph(org.getName()));
		
		// Create an Image
	    BufferedImage img =  new BufferedImage(width, height,   BufferedImage.TYPE_INT_RGB);

 	    // Get the Image's Graphics, and draw.
	    Graphics2D g = img.createGraphics();
	    g.setColor(Color.white);
	    g.fillRect(0,0, width, height);
	    
	    //Start drawing the Organigram with one node, it's root...
	    g.setColor(LINE_COLOR);
	    
	    
	    String themeCode = ((UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getThemeCode();
	    
	    String orgPicLocation = ORGANISATION_NODE_PICTURE_LOCATION.replace(REPLACE_THEME, themeCode);	  
	    String deptPicLocation = DEPARTMENT_NODE_PICTURE_LOCATION.replace(REPLACE_THEME, themeCode);
	    
	    logger.debug("Org = " + ORGANISATION_NODE_PICTURE_LOCATION + ", " + orgPicLocation);
	    logger.debug("Dept = " + DEPARTMENT_NODE_PICTURE_LOCATION + ", " + deptPicLocation);
	    
	    //Location of the Image file that will represent an Organization Box
	    String nodeOrgImage = FileUtils.getInstance().getRealPathForResource(orgPicLocation);
	    
	    //Location of the Image file that will represent a Department Box
	    String nodeDeptImage = FileUtils.getInstance().getRealPathForResource(deptPicLocation);
	    
	    logger.debug("nodeOrgImage = " + nodeOrgImage);
	    logger.debug("nodeDeptImage = " + nodeDeptImage);
	    
	    drawNode(g, tree.getRoot(), nodeOrgImage, nodeDeptImage, true);
	    
	    //Add the Image to PDF
		com.lowagie.text.Image image = 
			com.lowagie.text.Image.getInstance(ImageUtils.getInstance().bufferedImageToByteArray(img));
		document.add(image);
		
		//Close the Document
		document.close();
	}

	

	/**
	 * ******************************************************************************************************************************************************
	 * 								OrgStructTree																											*	
	 * ******************************************************************************************************************************************************
	 */
	
	/**
	 * Calculates Organigram Width based in informations from Organigram Tree
	 * @author dan.damian 
	 * @return 
	 */
	public int calculateOrganigramWidth(OrgTree tree) {
		return H_SPACE + tree.getLeafsNo() * NODE_WIDTH + ( tree.getLeafsNo() - 1 ) * H_SPACE + H_SPACE;
	}
	
	/**
	 * Calculates Organigram Height based in informations from Organigram Tree
	 * @author dan.damian 
	 * @return
	 */
	public int calculateOrganigramHeight(OrgTree tree) {
		return V_SPACE + tree.getLevels() * NODE_HEIGHT + ( tree.getLevels() - 1 ) * V_SPACE + V_SPACE;
	}
	
	/**
	 * Returns a JSON Partial Representation of this OrgDeptTree Object
	 * @author dan.damian 
	 */
	public String generateJSONTree(OrgTree tree) throws BusinessException {
		logger.debug("generateJSONTree BEGIN");
		JSONObject rootJSON = new JSONObject();
		if (tree == null) {
			return null;
		}
		
		rootJSON.accumulate("xPosition", tree.getRoot().getXPosition() + 100);
		rootJSON.accumulate("yPosition", tree.getRoot().getYPosition());
		rootJSON.accumulate("label", tree.getRoot().getNodeName());
		rootJSON.accumulate("departmentId", tree.getRoot().getDepartmentId());
		rootJSON.accumulate("level", 0);
		rootJSON.accumulate("leafNo", tree.getRoot().getLeafNo());
		Person generalDirector =  BLDepartment.getInstance().getDepartmentsManager(tree.getRoot().getDepartmentId());
		String generalDirectorsName = "";
		if (generalDirector != null) {
			generalDirectorsName = generalDirector.getFirstName().concat(" ").concat(generalDirector.getLastName());
		}
		rootJSON.accumulate("manager", generalDirectorsName);
		
		for(int i = 0; i < tree.getRoot().getChildrenNumber(); i++) {
			OrgTreeNode node = tree.getRoot().getChildren().get(i);
			rootJSON.accumulate("children", getNode(node));
		}
		rootJSON.accumulate("vspace", OrganigramUtils.V_SPACE);
		rootJSON.accumulate("hspace", OrganigramUtils.H_SPACE);
		rootJSON.accumulate("nodeWidth", OrganigramUtils.NODE_WIDTH);
		rootJSON.accumulate("nodeHeight", OrganigramUtils.NODE_HEIGHT);
		
		
		rootJSON.accumulate("organigramWidth", OrganigramUtils.getInstance().calculateOrganigramWidth(tree));
		rootJSON.accumulate("organigramHeight", OrganigramUtils.getInstance().calculateOrganigramHeight(tree));
		
		logger.debug("generateJSONTree END");
		
		return rootJSON.toString();
	}
	
	/**
	 * Returns a JSON Partial Representation of this OrgDeptTreeNode object
	 * @author dan.damian
	 */ 
	public JSONObject getNode(OrgTreeNode node) throws BusinessException {
		JSONObject nodeJSON = new JSONObject();
		nodeJSON.accumulate("xPosition", node.getXPosition() + 100);
		nodeJSON.accumulate("yPosition", node.getYPosition());
		nodeJSON.accumulate("label", node.getNodeName());
		nodeJSON.accumulate("type", node.getNodeType());
		nodeJSON.accumulate("departmentId", node.getDepartmentId());
		nodeJSON.accumulate("level", node.getLevel());
		nodeJSON.accumulate("leafNo", node.getLeafNo());
		Person departmentManager =  BLDepartment.getInstance().getDepartmentsManager(node.getDepartmentId());
		nodeJSON.accumulate("manager", (departmentManager != null ? departmentManager.getFirstName() + " " + departmentManager.getLastName(): ""));
		
		for(int i = 0; i < node.getChildrenNumber(); i++) {
			OrgTreeNode child = node.getChildren().get(i);
			nodeJSON.accumulate("children", getNode(child));
		}
		
		return nodeJSON;
	}
}
