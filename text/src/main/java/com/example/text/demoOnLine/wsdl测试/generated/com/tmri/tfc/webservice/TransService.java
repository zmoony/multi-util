
package com.tmri.tfc.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "TransService", targetNamespace = "http://webservice.tfc.tmri.com", wsdlLocation = "file:/D:/project/code/self_util_plugin/scaffold/muti-scaffold/text/src/main/java/com/example/text/demoOnLine/wsdl\u6d4b\u8bd5/jcbk_wsdl.xml")
public class TransService
    extends Service
{

    private final static URL TRANSSERVICE_WSDL_LOCATION;
    private final static WebServiceException TRANSSERVICE_EXCEPTION;
    private final static QName TRANSSERVICE_QNAME = new QName("http://webservice.tfc.tmri.com", "TransService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/D:/project/code/self_util_plugin/scaffold/muti-scaffold/text/src/main/java/com/example/text/demoOnLine/wsdl\u6d4b\u8bd5/jcbk_wsdl.xml");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        TRANSSERVICE_WSDL_LOCATION = url;
        TRANSSERVICE_EXCEPTION = e;
    }

    public TransService() {
        super(__getWsdlLocation(), TRANSSERVICE_QNAME);
    }

    public TransService(WebServiceFeature... features) {
        super(__getWsdlLocation(), TRANSSERVICE_QNAME, features);
    }

    public TransService(URL wsdlLocation) {
        super(wsdlLocation, TRANSSERVICE_QNAME);
    }

    public TransService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, TRANSSERVICE_QNAME, features);
    }

    public TransService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public TransService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns Trans
     */
    @WebEndpoint(name = "Trans")
    public Trans getTrans() {
        return super.getPort(new QName("http://webservice.tfc.tmri.com", "Trans"), Trans.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Trans
     */
    @WebEndpoint(name = "Trans")
    public Trans getTrans(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservice.tfc.tmri.com", "Trans"), Trans.class, features);
    }

    private static URL __getWsdlLocation() {
        if (TRANSSERVICE_EXCEPTION!= null) {
            throw TRANSSERVICE_EXCEPTION;
        }
        return TRANSSERVICE_WSDL_LOCATION;
    }

}
