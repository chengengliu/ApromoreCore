package org.apromore.toolbox.da;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.9
 * Sun Dec 05 14:27:19 CET 2010
 * Generated source version: 2.2.9
 * 
 */
 
@WebService(targetNamespace = "http://www.apromore.org/data_access/service_toolbox", name = "DAToolboxPortType")
@XmlSeeAlso({org.apromore.toolbox.model_da.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface DAToolboxPortType {

    @WebResult(name = "ReadCanonicalsOutputMsg", targetNamespace = "http://www.apromore.org/data_access/model_toolbox", partName = "payload")
    @WebMethod(operationName = "ReadCanonicals")
    public org.apromore.toolbox.model_da.ReadCanonicalsOutputMsgType readCanonicals(
        @WebParam(partName = "payload", name = "ReadCanonicalsInputMsg", targetNamespace = "http://www.apromore.org/data_access/model_toolbox")
        org.apromore.toolbox.model_da.ReadCanonicalsInputMsgType payload
    );

    @WebResult(name = "ReadProcessSummariesOutputMsg", targetNamespace = "http://www.apromore.org/data_access/model_toolbox", partName = "payload")
    @WebMethod(operationName = "ReadProcessSummaries")
    public org.apromore.toolbox.model_da.ReadProcessSummariesOutputMsgType readProcessSummaries(
        @WebParam(partName = "payload", name = "ReadProcessSummariesInputMsg", targetNamespace = "http://www.apromore.org/data_access/model_toolbox")
        org.apromore.toolbox.model_da.ReadProcessSummariesInputMsgType payload
    );

    @WebResult(name = "StoreCpfOutputMsg", targetNamespace = "http://www.apromore.org/data_access/model_toolbox", partName = "payload")
    @WebMethod(operationName = "StoreCpf")
    public org.apromore.toolbox.model_da.StoreCpfOutputMsgType storeCpf(
        @WebParam(partName = "payload", name = "StoreCpfInputMsg", targetNamespace = "http://www.apromore.org/data_access/model_toolbox")
        org.apromore.toolbox.model_da.StoreCpfInputMsgType payload
    );
}
