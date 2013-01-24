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
package ro.cs.cm.web.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.AbstractFormController;

public class MultiFormBeanCheckingInterceptor<K extends AbstractFormController> extends
HandlerInterceptorAdapter {

	private Log logger = LogFactory.getLog(getClass());

	/**
	* Stores the references between controllers and classes to create. Use a
	* Hashmap containing a string key and vector of the commands.
	*/
	private Map<String, Vector<K>> controllers = new HashMap<String, Vector<K>>();

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
	       ModelAndView modelAndView) throws Exception {
	
	// at this point the controller has executed and returned a model so lets
	// go sort out what we need to do.
	String viewName = this.getViewName(modelAndView.getViewName());
	this.logger.debug("Post processing view: " + viewName);
	
	// Now get the vector for the view.
	Vector<K> controllerList = this.getControllers().get(viewName);
	if (controllerList == null) {
	          this.logger.debug("View is not in list. Exiting.");
	    return;
	}
	
	  // Loop and create the beans.
	  String beanKey;
	String cmdName;
	Object cmdBean;
	Class cmdClass;
	boolean sessionBeanFound;
	boolean modelBeanFound;
	for (K c : controllerList) {
	
	       cmdName = c.getCommandName();
	          cmdClass = c.getCommandClass();
	        beanKey = c.getClass().getName() + ".FORM." + cmdName;
	
	     Map<String, Object> model = modelAndView.getModel();
	
	         //Look for an establish bean we can use.
	       sessionBeanFound = request.getSession().getAttribute(beanKey) != null;
	         modelBeanFound = model.containsKey(cmdName);
	
	       //Try and get a reference to an established bean using either
	         //the session bean or model bean. Otherwise create a new one.
	          if (sessionBeanFound) {
	                this.logger.debug("Bean present under id:" + beanKey);
	                 cmdBean = request.getSession().getAttribute(beanKey);
	          } else if (modelBeanFound) {
	                   this.logger.debug("Bean present in model.");
	                   cmdBean = model.get(cmdName);
	          } else {
	               this.logger.debug("Creating new bean.");
	               cmdBean = cmdClass.newInstance();
	      }
	
	          //Now put the bean in session and model if not already there.
	          request.getSession().setAttribute(beanKey, cmdBean);
	           model.put(cmdName, cmdBean);
	
	}
	
	  // Done, call the parent.
	super.postHandle(request, response, handler, modelAndView);
	}

	/**
	* Set to store the data.
	*
	* @param controllers
	*           A Map containing the revelant controllers as keys and name of
	*           the property on then that returns the view name as the value.
	*/
	public void setControllers(Map<K, String> controllers) {
	
	// Loop through the passed controllers and assemble the data.
	  String viewName;
	for (K c : controllers.keySet()) {
	
	         this.logger.debug("Setting controller form bean for " + c.getCommandName());
	
	       if (!c.isSessionForm()) {
	              throw new BeanInitializationException("Controller is not set for session beans.");
	     }
	
	          // get the view name.
	          try {
	                  viewName = this.getViewName((String)controllers.get(c));	                		  	                
	                  this.logger.debug(viewName + " => bean " + c.getCommandClass().getName());
	          } catch (Exception e) {
	                throw new BeanInitializationException("Could not extract view name from controller", e);
	          }
	    
	          // Check and create a vector for it .
	          if (!this.controllers.containsKey(viewName)) {
	                 this.controllers.put(viewName, new Vector<K>());
	         }
	
	          // Now add the controller to the list,
	         this.controllers.get(viewName).add(c);
	 }
	} 

	/**
	* Internal routine used to strip redirect: off the front of the view names
	* and suffixes off the back so
	* we can find them correctly regardless of whether they are doing
	* redirects.
	*/
	private String getViewName(String viewName) {
	  String view[] = viewName.split("[:\\.]");
	  switch (view.length) {
	         case 1:
	                return viewName;
	       case 2:
	                return view[0].equalsIgnoreCase("redirect") ? view[1] : view[0];
	       case 3:
	                return view[1];
	        default:
	               throw new IllegalArgumentException(viewName + " does not appear to be a valid view.");
	 }
	
	}

	public Map<String, Vector<K>> getControllers() {
		return controllers;
	}

}
