
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package org.apromore.manager.da;

import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.7
 * Tue May 25 15:00:18 EST 2010
 * Generated source version: 2.2.7
 * 
 */

@javax.jws.WebService(
                      serviceName = "DAManagerService",
                      portName = "DAManager",
                      targetNamespace = "http://www.apromore.org/data_access/service_manager",
                      wsdlLocation = "http://localhost:8080/Apromore-dataAccess/services/DAManager?wsdl",
                      endpointInterface = "org.apromore.manager.da.DAManagerPortType")
                      
public class DAManagerPortTypeImpl implements DAManagerPortType {

    private static final Logger LOG = Logger.getLogger(DAManagerPortTypeImpl.class.getName());

    public org.apromore.manager.model_da.ReadCanonicalOutputMsgType readCanonical(org.apromore.manager.model_da.ReadCanonicalInputMsgType payload) { 
        LOG.info("Executing operation readCanonical");
        System.out.println(payload);
        try {
            org.apromore.manager.model_da.ReadCanonicalOutputMsgType _return = new org.apromore.manager.model_da.ReadCanonicalOutputMsgType();
            org.apromore.manager.model_da.ResultType _returnResult = new org.apromore.manager.model_da.ResultType();
            _returnResult.setMessage("Message1019953211");
            _returnResult.setCode(Integer.valueOf(-1490873284));
            _return.setResult(_returnResult);
            javax.activation.DataHandler _returnCpf = null;
            _return.setCpf(_returnCpf);
            javax.activation.DataHandler _returnAnf = null;
            _return.setAnf(_returnAnf);
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

	public org.apromore.manager.model_da.DeleteProcessVersionsOutputMsgType deleteProcessVersions(org.apromore.manager.model_da.DeleteProcessVersionsInputMsgType payload) { 
        LOG.info("Executing operation deleteProcessVersions");
        System.out.println(payload);
        try {
            org.apromore.manager.model_da.DeleteProcessVersionsOutputMsgType _return = new org.apromore.manager.model_da.DeleteProcessVersionsOutputMsgType();
            org.apromore.manager.model_da.ResultType _returnResult = new org.apromore.manager.model_da.ResultType();
            _returnResult.setMessage("Message-170806933");
            _returnResult.setCode(Integer.valueOf(2071513922));
            _return.setResult(_returnResult);
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

	/* (non-Javadoc)
     * @see org.apromore.manager.da.DAManagerPortType#writeUser(org.apromore.manager.model_manager.WriteUserInputMsgType  payload )*
     */
    public org.apromore.manager.model_da.WriteUserOutputMsgType writeUser(org.apromore.manager.model_da.WriteUserInputMsgType payload) { 
        LOG.info("Executing operation writeUser");
        System.out.println(payload);
        try {
            org.apromore.manager.model_da.WriteUserOutputMsgType _return = new org.apromore.manager.model_da.WriteUserOutputMsgType();
            org.apromore.manager.model_da.ResultType _returnResult = new org.apromore.manager.model_da.ResultType();
            _returnResult.setMessage("Message-562428939");
            _returnResult.setCode(Integer.valueOf(-758833994));
            _return.setResult(_returnResult);
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public org.apromore.manager.model_da.ReadNativeOutputMsgType readNative(org.apromore.manager.model_da.ReadNativeInputMsgType payload) { 
        LOG.info("Executing operation readNative");
        System.out.println(payload);
        try {
            org.apromore.manager.model_da.ReadNativeOutputMsgType _return = new org.apromore.manager.model_da.ReadNativeOutputMsgType();
            org.apromore.manager.model_da.ResultType _returnResult = new org.apromore.manager.model_da.ResultType();
            _returnResult.setMessage("Message1193639058");
            _returnResult.setCode(Integer.valueOf(173617621));
            _return.setResult(_returnResult);
            javax.activation.DataHandler _returnNative = null;
            _return.setNative(_returnNative);
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

	public org.apromore.manager.model_da.DeleteEditSessionOutputMsgType deleteEditSession(org.apromore.manager.model_da.DeleteEditSessionInputMsgType payload) { 
        LOG.info("Executing operation deleteEditSession");
        System.out.println(payload);
        try {
            org.apromore.manager.model_da.DeleteEditSessionOutputMsgType _return = new org.apromore.manager.model_da.DeleteEditSessionOutputMsgType();
            org.apromore.manager.model_da.ResultType _returnResult = new org.apromore.manager.model_da.ResultType();
            _returnResult.setMessage("Message727068823");
            _returnResult.setCode(Integer.valueOf(-1437057026));
            _return.setResult(_returnResult);
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

	public org.apromore.manager.model_da.ReadEditSessionOutputMsgType readEditSession(org.apromore.manager.model_da.ReadEditSessionInputMsgType payload) { 
        LOG.info("Executing operation readEditSession");
        System.out.println(payload);
        try {
            org.apromore.manager.model_da.ReadEditSessionOutputMsgType _return = new org.apromore.manager.model_da.ReadEditSessionOutputMsgType();
            org.apromore.manager.model_da.ResultType _returnResult = new org.apromore.manager.model_da.ResultType();
            _returnResult.setMessage("Message-720198682");
            _returnResult.setCode(Integer.valueOf(677555781));
            _return.setResult(_returnResult);
            javax.activation.DataHandler _returnNative = null;
            _return.setNative(_returnNative);
            org.apromore.manager.model_da.EditSessionType _returnEditSession = new org.apromore.manager.model_da.EditSessionType();
            _returnEditSession.setUsername("Username168029476");
            _returnEditSession.setNativeType("NativeType-869083838");
            _returnEditSession.setProcessId(Integer.valueOf(5296224));
            _returnEditSession.setVersionName("VersionName-1803518212");
            _return.setEditSession(_returnEditSession);
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

	/* (non-Javadoc)
     * @see org.apromore.manager.da.DAManagerPortType#readDomains(org.apromore.manager.model_manager.ReadDomainsInputMsgType  payload )*
     */
    public org.apromore.manager.model_da.ReadDomainsOutputMsgType readDomains(org.apromore.manager.model_da.ReadDomainsInputMsgType payload) { 
        LOG.info("Executing operation readDomains");
        System.out.println(payload);
        try {
            org.apromore.manager.model_da.ReadDomainsOutputMsgType _return = new org.apromore.manager.model_da.ReadDomainsOutputMsgType();
            org.apromore.manager.model_da.ResultType _returnResult = new org.apromore.manager.model_da.ResultType();
            _returnResult.setMessage("Message-1780727428");
            _returnResult.setCode(Integer.valueOf(-48960007));
            _return.setResult(_returnResult);
            org.apromore.manager.model_da.DomainsType _returnDomains = new org.apromore.manager.model_da.DomainsType();
            java.util.List<java.lang.String> _returnDomainsDomain = new java.util.ArrayList<java.lang.String>();
            java.lang.String _returnDomainsDomainVal1 = "_returnDomainsDomainVal-1285756247";
            _returnDomainsDomain.add(_returnDomainsDomainVal1);
            _returnDomains.getDomain().addAll(_returnDomainsDomain);
            _return.setDomains(_returnDomains);
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public org.apromore.manager.model_da.WriteEditSessionOutputMsgType writeEditSession(org.apromore.manager.model_da.WriteEditSessionInputMsgType payload) { 
        LOG.info("Executing operation writeEditSession");
        System.out.println(payload);
        try {
            org.apromore.manager.model_da.WriteEditSessionOutputMsgType _return = new org.apromore.manager.model_da.WriteEditSessionOutputMsgType();
            org.apromore.manager.model_da.ResultType _returnResult = new org.apromore.manager.model_da.ResultType();
            _returnResult.setMessage("Message-608937207");
            _returnResult.setCode(Integer.valueOf(-2071878906));
            _return.setResult(_returnResult);
            _return.setEditSessionCode(Integer.valueOf(-1817881202));
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

	/* (non-Javadoc)
     * @see org.apromore.manager.da.DAManagerPortType#readFormats(org.apromore.manager.model_manager.ReadFormatsInputMsgType  payload )*
     */
    public org.apromore.manager.model_da.ReadFormatsOutputMsgType readFormats(org.apromore.manager.model_da.ReadFormatsInputMsgType payload) { 
        LOG.info("Executing operation readFormats");
        System.out.println(payload);
        try {
            org.apromore.manager.model_da.ReadFormatsOutputMsgType _return = new org.apromore.manager.model_da.ReadFormatsOutputMsgType();
            org.apromore.manager.model_da.ResultType _returnResult = new org.apromore.manager.model_da.ResultType();
            _returnResult.setMessage("Message-1600822620");
            _returnResult.setCode(Integer.valueOf(-456166859));
            _return.setResult(_returnResult);
            org.apromore.manager.model_da.FormatsType _returnFormats = new org.apromore.manager.model_da.FormatsType();
            java.util.List<java.lang.String> _returnFormatsFormat = new java.util.ArrayList<java.lang.String>();
            java.lang.String _returnFormatsFormatVal1 = "_returnFormatsFormatVal-88060529";
            _returnFormatsFormat.add(_returnFormatsFormatVal1);
            _returnFormats.getFormat().addAll(_returnFormatsFormat);
            _return.setFormats(_returnFormats);
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see org.apromore.manager.da.DAManagerPortType#readUser(org.apromore.manager.model_manager.ReadUserInputMsgType  payload )*
     */
    public org.apromore.manager.model_da.ReadUserOutputMsgType readUser(org.apromore.manager.model_da.ReadUserInputMsgType payload) { 
        LOG.info("Executing operation readUser");
        System.out.println(payload);
        try {
            org.apromore.manager.model_da.ReadUserOutputMsgType _return = new org.apromore.manager.model_da.ReadUserOutputMsgType();
            org.apromore.manager.model_da.ResultType _returnResult = new org.apromore.manager.model_da.ResultType();
            _returnResult.setMessage("Message472275652");
            _returnResult.setCode(Integer.valueOf(-1250488380));
            _return.setResult(_returnResult);
            org.apromore.manager.model_da.UserType _returnUser = new org.apromore.manager.model_da.UserType();
            java.util.List<org.apromore.manager.model_da.SearchHistoriesType> _returnUserSearchHistories = new java.util.ArrayList<org.apromore.manager.model_da.SearchHistoriesType>();
            org.apromore.manager.model_da.SearchHistoriesType _returnUserSearchHistoriesVal1 = new org.apromore.manager.model_da.SearchHistoriesType();
            _returnUserSearchHistoriesVal1.setSearch("Search1533821399");
            _returnUserSearchHistoriesVal1.setNum(Integer.valueOf(-1988638089));
            _returnUserSearchHistories.add(_returnUserSearchHistoriesVal1);
            _returnUser.getSearchHistories().addAll(_returnUserSearchHistories);
            _returnUser.setFirstname("Firstname35558464");
            _returnUser.setLastname("Lastname-1323780026");
            _returnUser.setEmail("Email-841708036");
            _returnUser.setUsername("Username-1469933526");
            _returnUser.setPasswd("Passwd1072451077");
            _return.setUser(_returnUser);
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see org.apromore.manager.da.DAManagerPortType#readProcessSummaries(org.apromore.manager.model_manager.ReadProcessSummariesInputMsgType  payload )*
     */
    public org.apromore.manager.model_da.ReadProcessSummariesOutputMsgType readProcessSummaries(org.apromore.manager.model_da.ReadProcessSummariesInputMsgType payload) { 
        LOG.info("Executing operation readProcessSummaries");
        System.out.println(payload);
        try {
            org.apromore.manager.model_da.ReadProcessSummariesOutputMsgType _return = new org.apromore.manager.model_da.ReadProcessSummariesOutputMsgType();
            org.apromore.manager.model_da.ResultType _returnResult = new org.apromore.manager.model_da.ResultType();
            _returnResult.setMessage("Message1755225756");
            _returnResult.setCode(Integer.valueOf(-1340744553));
            _return.setResult(_returnResult);
            org.apromore.manager.model_da.ProcessSummariesType _returnProcessSummaries = new org.apromore.manager.model_da.ProcessSummariesType();
            java.util.List<org.apromore.manager.model_da.ProcessSummaryType> _returnProcessSummariesProcessSummary = new java.util.ArrayList<org.apromore.manager.model_da.ProcessSummaryType>();
            org.apromore.manager.model_da.ProcessSummaryType _returnProcessSummariesProcessSummaryVal1 = new org.apromore.manager.model_da.ProcessSummaryType();
            java.util.List<org.apromore.manager.model_da.VersionSummaryType> _returnProcessSummariesProcessSummaryVal1VersionSummaries = new java.util.ArrayList<org.apromore.manager.model_da.VersionSummaryType>();
            _returnProcessSummariesProcessSummaryVal1.getVersionSummaries().addAll(_returnProcessSummariesProcessSummaryVal1VersionSummaries);
            _returnProcessSummariesProcessSummaryVal1.setOriginalNativeType("OriginalNativeType-937226786");
            _returnProcessSummariesProcessSummaryVal1.setName("Name839482021");
            _returnProcessSummariesProcessSummaryVal1.setId(Integer.valueOf(1090606396));
            _returnProcessSummariesProcessSummaryVal1.setDomain("Domain-1208798099");
            _returnProcessSummariesProcessSummaryVal1.setRanking(Integer.valueOf(539119346));
            _returnProcessSummariesProcessSummaryVal1.setLastVersion("LastVersion1162339198");
            _returnProcessSummariesProcessSummary.add(_returnProcessSummariesProcessSummaryVal1);
            _returnProcessSummaries.getProcessSummary().addAll(_returnProcessSummariesProcessSummary);
            _return.setProcessSummaries(_returnProcessSummaries);
            return _return;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}
