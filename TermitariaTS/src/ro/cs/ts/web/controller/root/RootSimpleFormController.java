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
package ro.cs.ts.web.controller.root;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.Tools;

/**
 * @author matti_joona
 * /**
 * Controller which will be extended by other controllers of this type
 * 
 * Workflow (and that defined by superclass):
 * The workflow of this Controller does not differ too much from the one described in the AbstractFormController, except for the fact that overriding of the processFormSubmission method and the showForm method is not necessary, since the view for the respective occasions can be configured externally.
 *
 *  1. After validation of the command object and the perscribed call to onBindAndValidate (for more information on that matter, see the AbstractFormController), the following:
 *  2. call to processFormSubmission which inspects the errors object to see if any errors are available (they could be inserted in the bindAndValidate method
 *  3. If errors occured, the controller will return the formView, giving the user the form again (with possible error message render accordingly)
 *  4. If no errors occurred, a call to onSubmit() using all parameters is done which (in case of the default implementation) calls onSubmit() with just the command object. This allows for convenient overriding of custom hooks
 *  5. After that has finished, the successView is returned (which again, is configurable through the exposed configuration properties)
 *
 * 
 * Workflow of org.springframework.web.servlet.mvc.AbstractFormController (and that defined by superclass):
 *
 *  1. GET request on the controller is received
 *  2. call to formBackingObject() which by default, returns an instance of the commandClass that has been configured (see the properties the superclass exposes), but can also be overriden to - for instance - retrieve an object from the database (that - for instance - needs to be modified using the form)
 *  3. call to initBinder() which allows you to register custom editors for certain fields (often properties of non-primitive or non-Sring types) or the command class. This render appropriate Strings for for instance locales
 *  4. binding of the ServletRequestDataBinder in the request to be able to use the property editors in the form rendering (only if bindOnNewForm is set to true)
 *  5. call to showForm() to return a View that should be rendered (typically the view that renders the form). This method has be overridden in extending classes
 *  6. call to referenceData() to allow you to bind any relevant reference data you might need when editing a form (for instance a List of Locale objects you're going to let the user select one from)
 *  7. model gets exposed and view gets rendered. Continue after user has filled in form
 *  8. POST request on the controller is received
 *  9. if sessionForm is not set, getCommand() is called to retrieve a command class. Otherwise, the controller tries to find the command object which is already bound in the session. If it cannot find the object, it'll do a call to handleInvalidSubmit which - by default - tries to create a new command class and resubmit the form
 * 10. controller tries to put all parameters from the request into the JavaBeans (command object) and if validateOnBinding is set, validation will occur
 * 11. call to onBindAndValidate() which allows you to do custom processing after binding and validation (for instance to perform database persistency)
 * 12. call to processFormSubmission which, in implementing classes returns a sort of successview, for instance congratulating the user with a successfull form submission
 *
 * Controller which will be extended by other controllers of this type
 */
public class RootSimpleFormController extends SimpleFormController{

	protected MessageSource messageSource; 

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	protected void setErrors(HttpServletRequest request, List<String> errors) {
		logger.debug("\t\tsetErrors");
		if (Tools.getInstance().voidListCondition(errors)) return;
		
		if (request.getAttribute(IConstant.REQ_ATTR_ERRORS) != null){
    		List<String> oldErrors = (List<String>)request.getAttribute(IConstant.REQ_ATTR_ERRORS);
    		oldErrors.addAll(errors);
    	} else {
    		request.setAttribute(IConstant.REQ_ATTR_ERRORS, errors);
    	}
    }

    protected void setMessages(HttpServletRequest request, List<String> messages) {
    	logger.debug("\t\tsetMessages");
    	if (Tools.getInstance().voidListCondition(messages)) return;
    	
    	if (request.getAttribute(IConstant.REQ_ATTR_MSGS) != null){
    		List<String> oldErrors = (List<String>)request.getAttribute(IConstant.REQ_ATTR_MSGS);
    		oldErrors.addAll(messages);
    	} else {
    		request.setAttribute(IConstant.REQ_ATTR_MSGS, messages);
    	}
    }
    
    protected void setWarnings(HttpServletRequest request, List<String> messages) {
    	logger.debug("\t\tsetWarnings");
    	if(Tools.getInstance().voidListCondition(messages)) return;
    	
    	if(request.getAttribute(IConstant.REQ_ATTR_WARN) != null) {
    		List<String> oldWarnings = (List<String>)request.getAttribute(IConstant.REQ_ATTR_WARN);
    		oldWarnings.addAll(messages);
    	} else {
    		request.setAttribute(IConstant.REQ_ATTR_WARN, messages);
    	}
    }

    //----------------------------- METHODS FROM SimpleFormController ------------------------------------
	@Override
	protected void doSubmitAction(Object command) throws Exception {
		logger.debug("doSubmitAction(command) - command: " + command);
		super.doSubmitAction(command);
	}

	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request,
			Object command) {
		boolean answer = super.isFormChangeRequest(request, command);
		logger.debug(answer + ": isFormChangeRequest (request, command) commnad: " + command);
		return answer;
	}

	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		boolean answer = super.isFormChangeRequest(request); 
		logger.debug(answer + ": isFormChangeRequest(request)");
		return answer;
	}

	@Override
	protected void onFormChange(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onFormChange(request, response, command, errors) command: " + command);
		super.onFormChange(request, response, command, errors);
	}

	@Override
	protected void onFormChange(HttpServletRequest request,
			HttpServletResponse response, Object command) throws Exception {
		logger.debug("onFormChange(request, response, command) command: " + command);
		super.onFormChange(request, response, command);
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit(request, response, command, errors) commnad: " + command);
		return super.onSubmit(request, response, command, errors);
	}

	@Override
	protected ModelAndView onSubmit(Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit(command, errors) command: " + command);
		return super.onSubmit(command, errors);
	}

	@Override
	protected ModelAndView onSubmit(Object command) throws Exception {
		logger.debug("onSubmit(command) command: " + command);
		return super.onSubmit(command);
	}

	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("processFormSubmission(request, response, command, errors) command: " + command);
		return super.processFormSubmission(request, response, command, errors);
	}

	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		logger.debug("referenceData(request, command, errors) command: " + command);
		return super.referenceData(request, command, errors);
	}

	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		logger.debug("referenceData(request)");
		return super.referenceData(request);
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {
		logger.debug("showForm(request, response, errors, controlModel)");
		return super.showForm(request, response, errors, controlModel);
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
		logger.debug("showForm(request, response, errors)");
		return super.showForm(request, response, errors);
	}

	@Override
	protected boolean suppressValidation(HttpServletRequest request,
			Object command) {
		boolean answer = super.suppressValidation(request, command); 
		logger.debug(answer + ": suppressValidation(request, command)");
		return answer;
	}
	
	
	//----------------------------- METHODS FROM AbstractSimpleFormController ------------------------------------

	@Override
	protected Object currentFormObject(HttpServletRequest request,
			Object sessionFormObject) throws Exception {
		logger.debug("currentFormObject(request,	sessionFormObject)");
		return super.currentFormObject(request, sessionFormObject);
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		logger.debug("formBackingObject(request)");
		return super.formBackingObject(request);
	}

	@Override
	protected String getFormSessionAttributeName() {
		logger.debug("getFormSessionAttributeName()");
		return super.getFormSessionAttributeName();
	}

	@Override
	protected String getFormSessionAttributeName(HttpServletRequest request) {
		logger.debug("getFormSessionAttributeName(request)");
		return super.getFormSessionAttributeName(request);
	}

	@Override
	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("handleInvalidSubmit(request, response)");
		return super.handleInvalidSubmit(request, response);
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		logger.debug("handleRequestInternal(request, response)");
		return super.handleRequestInternal(arg0, arg1);
	}

	@Override
	protected boolean isFormSubmission(HttpServletRequest request) {
		boolean answer = super.isFormSubmission(request); 
		logger.debug(answer + ": isFormSubmission(request)");
		return answer;
	}

	@Override
	protected void onBindOnNewForm(HttpServletRequest request, Object command,
			BindException errors) throws Exception {
		logger.debug("onBindOnNewForm(request, command, errors)");
		super.onBindOnNewForm(request, command, errors);
	}

	@Override
	protected void onBindOnNewForm(HttpServletRequest request, Object command)
			throws Exception {
		logger.debug("onBindOnNewForm(request, command)");
		super.onBindOnNewForm(request, command);
	}
	
	//----------------------------- METHODS FROM BaseCommandController ------------------------------------

	@Override
	protected ServletRequestDataBinder createBinder(HttpServletRequest request,
			Object command) throws Exception {
		logger.debug("createBinder(request, command) command: " + command);
		return super.createBinder(request, command);
	}

	@Override
	protected void initApplicationContext() {
		logger.debug("\tINIT: initApplicationContext()");
		super.initApplicationContext();
	}

	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		logger.debug("initBinder(request, binder)");
		super.initBinder(request, binder);
	}

	@Override
	protected void onBind(HttpServletRequest request, Object command,
			BindException errors) throws Exception {
		logger.debug("onBind(request, command, errors) command: " + command);
		super.onBind(request, command, errors);
	}

	@Override
	protected void onBind(HttpServletRequest request, Object command)
			throws Exception {
		logger.debug("onBind(request, command) command: " + command);
		super.onBind(request, command);
	}

	@Override
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) throws Exception {
		logger.debug("onBindAndValidate(request, command, errors) command: " + command);
		super.onBindAndValidate(request, command, errors);
	}

	@Override
	protected boolean suppressBinding(HttpServletRequest request) {
		boolean answer = super.suppressBinding(request);
		logger.debug(answer + ": suppressBinding(request)");
		return answer;
	}

	@Override
	protected boolean suppressValidation(HttpServletRequest request,
			Object command, BindException errors) {
		boolean answer = super.suppressValidation(request, command, errors); 
		logger.debug(answer + ": suppressValidation(request, command, errors)");
		return answer;
	}
	

	@Override
	protected boolean suppressValidation(HttpServletRequest request) {
		boolean answer = super.suppressValidation(request); 
		logger.debug(answer + ": suppressValidation(request");
		return answer;
	}

	@Override
	protected boolean useDirectFieldAccess() {
		boolean answer = super.useDirectFieldAccess(); 
		logger.debug(answer + ": useDirectFieldAccess()");
		return answer;
	}

	
	//----------------------------- METHODS FROM AbstractController ------------------------------------
	@Override
	public ModelAndView handleRequest(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		logger.debug("handleRequest(request, response)");
		return super.handleRequest(arg0, arg1);
	}

	//----------------------------- METHODS FROM WebApplicationObjectSupport ---------------------------
	@Override
	protected void initApplicationContext(ApplicationContext arg0) {
		logger.debug("\tINIT: initApplicationContext(ApplicationContext)");
		super.initApplicationContext(arg0);
	}

	@Override
	protected void initServletContext(ServletContext servletContext) {
		logger.debug("\tINIT: initServletContext(ServletContext)");
		super.initServletContext(servletContext);
	}

	@Override
	protected boolean isContextRequired() {
		boolean answer = super.isContextRequired();
		logger.debug(answer + ": isContextRequired()");
		return answer;
	}

	
	//----------------------------- METHODS FROM ApplicationObjectSupport ------------------------------
	
	@Override
	protected Class requiredContextClass() {
		logger.debug("requiredContextClass()");
		return super.requiredContextClass();
	}          
}
