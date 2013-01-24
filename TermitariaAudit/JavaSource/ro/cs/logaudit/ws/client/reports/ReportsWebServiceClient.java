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
package ro.cs.logaudit.ws.client.reports;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.MessageContext;
import org.springframework.util.StopWatch;

import ro.cs.logaudit.common.ApplicationObjectSupport;
import ro.cs.logaudit.exception.ICodeException;
import ro.cs.logaudit.exception.WSClientException;
import ro.cs.logaudit.utils.date.DateUtils;
import ro.cs.logaudit.ws.client.reports.entity.AuditEventsReportParams;


/**
 * 
 * Singleton which exposes methods for the OM Web Service Client
 * @author coni
 *
 */
public class ReportsWebServiceClient extends ApplicationObjectSupport {

	private static final HashMap<String, String> xmlTypeAttributesValues = new HashMap<String, String>();
	
	private static final String xsiNamespaceUri						    = "http://www.w3.org/2001/XMLSchema-instance";
	private static final String xsiNamespacePrefix 						= "xsi";
	private static final String xsNamespaceUri							= "http://www.w3.org/2001/XMLSchema";
	private static final String xsNamespacePrefix 						= "xs";
	private static final String reportsNamespacePrefix					= "rep";
	
	//singleton implementation
    private static ReportsWebServiceClient theInstance = null;
    private ReportsWebServiceClient(){};
    static {
        theInstance = new ReportsWebServiceClient();
        xmlTypeAttributesValues.put("java.lang.String", xsNamespacePrefix.concat(":string"));
        xmlTypeAttributesValues.put("long", xsNamespacePrefix.concat(":long"));
        xmlTypeAttributesValues.put("double", xsNamespacePrefix.concat(":double"));
        xmlTypeAttributesValues.put("java.util.Date", xsNamespacePrefix.concat(":dateTime"));
        xmlTypeAttributesValues.put("boolean", xsNamespacePrefix.concat(":boolean"));
        xmlTypeAttributesValues.put("javax.xml.datatype.XMLGregorianCalendar", xsNamespacePrefix.concat(":dateTime"));
        xmlTypeAttributesValues.put("int", xsNamespacePrefix.concat(":int"));
        xmlTypeAttributesValues.put("java.lang.Integer", xsNamespacePrefix.concat(":int")); 
    }
    public static ReportsWebServiceClient getInstance() {
        return theInstance;
    }
	
	/**
	 * Method which uses Axis2 Web Service Client for retrieving report files from the report module
	 * @throws WSClientException 
	 */
	public DataHandler getAuditEventsReport(AuditEventsReportParams reportParams) throws WSClientException{
		logger.debug("getAuditEventsReport START");
		StopWatch sw = new StopWatch();
		sw.start("getAuditEventsReport");
		ServiceClient leafReportsAxis2ServiceClient = null;
		DataHandler response =  null;
		try {
			leafReportsAxis2ServiceClient = Axis2Utils.getInstance().getLeafReportsAxis2ServiceClient();
			OperationClient mepClient = leafReportsAxis2ServiceClient.createClient(ServiceClient.ANON_OUT_IN_OP);
			MessageContext axisMessageContext = new MessageContext();
			
			SOAPFactory soapFactory = OMAbstractFactory.getSOAP11Factory();
			SOAPEnvelope soapEnvelope = soapFactory.getDefaultEnvelope();
			OMNamespace omNs = soapFactory.createOMNamespace(IReportsWsClientConstant.REPORTS_WEB_SERVICE_NAMESPACE, reportsNamespacePrefix);
			OMNamespace xsiNs = soapFactory.createOMNamespace(xsiNamespaceUri, xsiNamespacePrefix);
			OMNamespace xsNs = soapFactory.createOMNamespace(xsNamespaceUri, xsNamespacePrefix);

			//create the root element of the SOAP message body payload
			OMElement payloadRootElement = soapFactory.createOMElement(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_ROOT_PAYLOAD_LOCALPART, omNs);
			payloadRootElement.declareNamespace(xsiNs);
			payloadRootElement.declareNamespace(xsNs);

			//create the report parameters element of the payload
			OMElement reportParamsElement = soapFactory.createOMElement(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_PARAMS_LOCALPART, omNs);
			
			//create the properties element
			OMElement propertiesElement = soapFactory.createOMElement(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_PROPERTIES_LOCALPART, omNs);
			
			//create the properties map entries elements
			Iterator it = reportParams.getProperties().entrySet().iterator();
			while (it.hasNext()){
				Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
				String value;
				if ( pairs.getValue() != null && (reportParams.getPropertiesTypes().get(pairs.getKey())).equals("java.util.Date") ){
					value = DateUtils.getInstance().dateToXmlGregorianCalendar((Date)pairs.getValue()).toString();
				} else {
					value = String.valueOf(pairs.getValue());
				}
				//create a map entry element
				OMAttribute elementType = soapFactory.createOMAttribute("type", xsiNs, xmlTypeAttributesValues.get(reportParams.getPropertiesTypes().get(pairs.getKey())));
				OMElement entryElement = createMapEntryElement(pairs.getKey(), value, soapFactory, elementType);
				propertiesElement.addChild(entryElement);
			}
			
			reportParamsElement.addChild(propertiesElement);
			
			//add the organisationId element to the payload root and the root to the SOAP body
			payloadRootElement.addChild(reportParamsElement);
			soapEnvelope.getBody().addChild(payloadRootElement);
					

			//sign the axis2 envelope
			soapEnvelope = Axis2Utils.getInstance().signSoapEnvelope(soapEnvelope);

			axisMessageContext.setEnvelope(soapEnvelope);
			mepClient.addMessageContext(axisMessageContext);
			logger.debug("---------------------- START SENDING REPORT REQUEST -----------------------");
			logger.debug("SOAP Message Envelope: ".concat("\n"));
			logger.debug(soapEnvelope.toString());
			//send the message
			mepClient.execute(true);

			//retrieve the attachments map from the SOAP Message respone
			HashMap<String, MessageContext> messageContexts = leafReportsAxis2ServiceClient.getLastOperationContext().getMessageContexts();
			MessageContext responseMessageContext = messageContexts.get("In");
			Map<String, DataHandler> attachments = (Map<String, DataHandler>) responseMessageContext.attachments.getMap();
			
			//get the SOAP Message Response payload and the FileInfo elements
			OMElement responsePayload = responseMessageContext.getEnvelope().getBody().getFirstElement();
			
			//write every attachment content to a file with a name specified in the soap response
			Iterator<OMElement> fileInfoElements = responsePayload.getChildren();
			while(fileInfoElements.hasNext()){
				//every reportFile element has the following children elements : reportFileName and reportFileAttachmentId which
				//will be retrieved as attachmentId
				OMElement fileInfoElement = fileInfoElements.next();
				String attachmentId = fileInfoElement.getFirstChildWithName(new QName(IReportsWsClientConstant.REPORTS_WEB_SERVICE_NAMESPACE, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_RESPONSE_ATTACHMENT_ID_LOCALPART)).getText();
				String reportFileName = fileInfoElement.getFirstChildWithName(new QName(IReportsWsClientConstant.REPORTS_WEB_SERVICE_NAMESPACE, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_RESPONSE_REPORT_FILE_NAME_LOCALPART)).getText();
		   		logger.debug("Received the report file named: ".concat(reportFileName).concat(" and with the attachment Id: ").concat(attachmentId));
		   		response = attachments.get(attachmentId);
			}
		} catch (AxisFault e) {
			if (e.getDetail() != null){
				throw new WSClientException(e.getDetail().getFirstChildWithName(new QName(IReportsWsClientConstant.REPORTS_WEB_SERVICE_NAMESPACE, IReportsWsClientConstant.AXIS_FAULT_DETAIL_CODE_LOCALPART)).getText()
					, e.getDetail().getFirstChildWithName(new QName(IReportsWsClientConstant.REPORTS_WEB_SERVICE_NAMESPACE, IReportsWsClientConstant.AXIS_FAULT_DETAIL_MESSAGE_LOCALPART)).getText(), e);
			} else {
				throw new WSClientException(e.getMessage(), e);
			}
		} catch (Exception e) {
			throw new WSClientException(ICodeException.REPORTS_WSCLIENT_AUDIT_EVENTS_REPORT_REQEUST, e);
		}
		logger.debug("END - getAuditEventsReport");
		sw.stop();
		logger.debug(sw.prettyPrint());
		return response;
	}
	
	public OMElement createMapEntryElement(String key, String value, SOAPFactory soapFactory, OMAttribute type) throws WSClientException {
		try {
			//create the entry, key and value elements
			OMElement entryElement = soapFactory.createOMElement(new QName(IReportsWsClientConstant.MAP_ENTRY_LOCALPART));
			OMElement keyElement = soapFactory.createOMElement(new QName(IReportsWsClientConstant.MAP_KEY_LOCALPART));
			OMElement valueElement = soapFactory.createOMElement(new QName(IReportsWsClientConstant.MAP_VALUE_LOCALPART));
			
			//set the key and value elements' text
			keyElement.setText(key);
			valueElement.setText(value);
			//the type attribute must be added to the value element in order to be unmarshalled on web service endpoint
			valueElement.addAttribute(type);
			
			//add the key and value elements to the entry element
			entryElement.addChild(keyElement);
			entryElement.addChild(valueElement);
			
			return entryElement;
		} catch (Exception exc) {
			throw new WSClientException(ICodeException.REPORTS_WSCLIENT_CREATE_MAP_ENTRIES, exc);
		}
		
	}

}
