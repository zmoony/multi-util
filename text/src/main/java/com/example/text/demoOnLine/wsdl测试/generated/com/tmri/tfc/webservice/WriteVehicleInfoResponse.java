
package com.tmri.tfc.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
     * ��ȡwriteVehicleInfoReturn���Ե�ֵ��
     * 
     */
    public long getWriteVehicleInfoReturn() {
        return writeVehicleInfoReturn;
    }

    /**
     * ����writeVehicleInfoReturn���Ե�ֵ��
     * 
     */
    public void setWriteVehicleInfoReturn(long value) {
        this.writeVehicleInfoReturn = value;
    }

}
