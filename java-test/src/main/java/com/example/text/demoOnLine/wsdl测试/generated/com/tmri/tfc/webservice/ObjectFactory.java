
package com.tmri.tfc.webservice;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tmri.tfc.webservice package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tmri.tfc.webservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QuerySyncTime }
     * 
     */
    public QuerySyncTime createQuerySyncTime() {
        return new QuerySyncTime();
    }

    /**
     * Create an instance of {@link QueryLimitSpeed }
     * 
     */
    public QueryLimitSpeed createQueryLimitSpeed() {
        return new QueryLimitSpeed();
    }

    /**
     * Create an instance of {@link InitTrans }
     * 
     */
    public InitTrans createInitTrans() {
        return new InitTrans();
    }

    /**
     * Create an instance of {@link InitTransResponse }
     * 
     */
    public InitTransResponse createInitTransResponse() {
        return new InitTransResponse();
    }

    /**
     * Create an instance of {@link WriteVehicleInfoResponse }
     * 
     */
    public WriteVehicleInfoResponse createWriteVehicleInfoResponse() {
        return new WriteVehicleInfoResponse();
    }

    /**
     * Create an instance of {@link GetLastMessageResponse }
     * 
     */
    public GetLastMessageResponse createGetLastMessageResponse() {
        return new GetLastMessageResponse();
    }

    /**
     * Create an instance of {@link WriteVehicleInfo }
     * 
     */
    public WriteVehicleInfo createWriteVehicleInfo() {
        return new WriteVehicleInfo();
    }

    /**
     * Create an instance of {@link QuerySyncTimeResponse }
     * 
     */
    public QuerySyncTimeResponse createQuerySyncTimeResponse() {
        return new QuerySyncTimeResponse();
    }

    /**
     * Create an instance of {@link GetLastMessage }
     * 
     */
    public GetLastMessage createGetLastMessage() {
        return new GetLastMessage();
    }

    /**
     * Create an instance of {@link QueryLimitSpeedResponse }
     * 
     */
    public QueryLimitSpeedResponse createQueryLimitSpeedResponse() {
        return new QueryLimitSpeedResponse();
    }

}
