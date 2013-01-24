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
package ro.cs.ts.ws.server.entity;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import ro.cs.ts.common.IConstant;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ro.cs.ts.ws.server.entity package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	
	private final static QName _EndpointException_QNAME = new QName(IConstant.TS_WS_SERVER_NAMESPACE, "TSEndpointExceptionBean");
    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ro.cs.ts.ws.server.entity
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TSEndpointExceptionBean }
     * 
     */
    public TSEndpointExceptionBean createTSEndpointExceptionBean() {
        return new TSEndpointExceptionBean();
    }

    /**
     * Create an instance of {@link DMEEndpointExceptionBean }
     * 
     */
    @XmlElementDecl(namespace = IConstant.TS_WS_SERVER_NAMESPACE, name = "TSEndpointExceptionBean")
    public JAXBElement<TSEndpointExceptionBean> createEndpointException(TSEndpointExceptionBean value) {
        return new JAXBElement<TSEndpointExceptionBean>(_EndpointException_QNAME, TSEndpointExceptionBean.class, null, value);
    }
    
    /**
     * Create an instance of {@link ProjectReportDatasourceRequest }
     * 
     */
    public ProjectReportDatasourceRequest createProjectReportDatasourceRequest() {
        return new ProjectReportDatasourceRequest();
    }
    
    /**
     * Create an instance of {@link TSReportGetDataCriteria }
     * 
     */
    public TSReportGetDataCriteria createTSReportGetDataCriteria() {
        return new TSReportGetDataCriteria();
    }
    
    /**
     * Create an instance of {@link TimeSheetReportDatasourceRequest }
     * 
     */
    public TimeSheetReportDatasourceRequest createTimeSheetReportDatasourceRequest() {
        return new TimeSheetReportDatasourceRequest();
    }
    
    /**
     * Create an instance of {@link ProjectReportDatasourceResponse }
     * 
     */
    public ProjectReportDatasourceResponse createProjectReportDatasourceResponse() {
        return new ProjectReportDatasourceResponse();
    }
    
    /**
     * Create an instance of {@link TimeSheetReportDatasourceResponse }
     * 
     */
    public TimeSheetReportDatasourceResponse createTimeSheetReportDatasourceResponse() {
        return new TimeSheetReportDatasourceResponse();
    }          
    
    /**
     * Create an instance of {@link WSCostSheet }
     * 
     */
    public WSCostSheet createWSCostSheet() {
        return new WSCostSheet();
    }
    
    /**
     * Create an instance of {@link WSCostSheets }
     * 
     */
    public WSCostSheets createWSCostSheets() {
        return new WSCostSheets();
    }
    
    /**
     * Create an instance of {@link WSRecord }
     * 
     */
    public WSRecord createWSRecord() {
        return new WSRecord();
    }
    
    /**
     * Create an instance of {@link WSRecords }
     * 
     */
    public WSRecords createWSRecords() {
        return new WSRecords();
    }
    
    /**
     * Create an instance of {@link GetProjectIdForDeleteRequest }
     * 
     */
    public GetProjectIdForDeleteRequest createGetProjectIdForDeleteRequest() {
        return new GetProjectIdForDeleteRequest();
    }
    
    /**
     * Create an instance of {@link GetProjectIdForFinishRequest }
     * 
     */
    public GetProjectIdForFinishRequest createGetProjectIdForFinishRequest() {
        return new GetProjectIdForFinishRequest();
    }
    
    /**
     * Create an instance of {@link GetProjectIdForAbortRequest }
     * 
     */
    public GetProjectIdForAbortRequest createGetProjectIdForAbortRequest() {
        return new GetProjectIdForAbortRequest();
    }
    
    /**
     * Create an instance of {@link GetProjectIdForOpenRequest }
     * 
     */
    public GetProjectIdForOpenRequest createGetProjectIdForOpenRequest() {
        return new GetProjectIdForOpenRequest();
    }
    
           
    /**
     * Create an instance of {@link GetProjectIdForOpenRequest }
     * 
     */
    public GetTeamMemberIdForDeleteRequest createGetTeamMemberIdForDeleteRequest() {
        return new GetTeamMemberIdForDeleteRequest();
    }
    
    
    /**
     * Create an instance of {@link WSProjectId }
     * 
     */
    public WSProjectId createWSProjectId() {
    	return new WSProjectId();
    }
    
    /**
     * Create an instance of {@link WSActivity }
     * 
     */
    public WSActivity createWSActivity() {
        return new WSActivity();
    }
    
     
    
    /**
     * Create an instance of {@link GetActivitiesByProjectIdRequest }
     * 
     */
    public GetActivitiesByProjectIdRequest createGetActivitiesByProjectIdRequest() {
        return new GetActivitiesByProjectIdRequest();
    }
    
    /**
     * Create an instance of {@link GetActivitiesByProjectIdResponse }
     * 
     */
    public GetActivitiesByProjectIdResponse createGetActivitiesByProjectIdResponse() {
        return new GetActivitiesByProjectIdResponse();
    }
    
    /**
     * Create an instance of {@link GetRecordDetailsBySessionIdRequest }
     * 
     */
    public GetRecordDetailsBySessionIdRequest createGetRecordIdBySessionIdRequest() {
        return new GetRecordDetailsBySessionIdRequest();
    }
    
    /**
     * Create an instance of {@link GetRecordDetailsBySessionIdResponse }
     * 
     */
    public GetRecordDetailsBySessionIdResponse createGetRecordIdBySessionIdResponse() {
        return new GetRecordDetailsBySessionIdResponse();
    }
    

    /**
     * Create an instance of {@link SendStopTimeSheetRequest }
     * 
     */
    public SendStopTimeSheetRequest createSendStopTimeSheetRequest() {
        return new SendStopTimeSheetRequest();
    }
    
    /**
     * Create an instance of {@link SendStopTimeSheetResponse }
     * 
     */
    public SendStopTimeSheetResponse createSendStopTimeSheetResponse() {
        return new SendStopTimeSheetResponse();
    }
}
