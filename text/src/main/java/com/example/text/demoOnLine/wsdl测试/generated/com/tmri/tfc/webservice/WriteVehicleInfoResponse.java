
package com.tmri.tfc.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="WriteVehicleInfoReturn" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "writeVehicleInfoReturn"
})
@XmlRootElement(name = "WriteVehicleInfoResponse")
public class WriteVehicleInfoResponse {

    @XmlElement(name = "WriteVehicleInfoReturn")
    protected long writeVehicleInfoReturn;

    /**
     * 获取writeVehicleInfoReturn属性的值。
     * 
     */
    public long getWriteVehicleInfoReturn() {
        return writeVehicleInfoReturn;
    }

    /**
     * 设置writeVehicleInfoReturn属性的值。
     * 
     */
    public void setWriteVehicleInfoReturn(long value) {
        this.writeVehicleInfoReturn = value;
    }

}
