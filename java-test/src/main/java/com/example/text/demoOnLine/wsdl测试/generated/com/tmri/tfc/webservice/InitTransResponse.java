
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
 *         &lt;element name="InitTransReturn" type="{http://www.w3.org/2001/XMLSchema}long"/>
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
    "initTransReturn"
})
@XmlRootElement(name = "InitTransResponse")
public class InitTransResponse {

    @XmlElement(name = "InitTransReturn")
    protected long initTransReturn;

    /**
     * ��ȡinitTransReturn���Ե�ֵ��
     * 
     */
    public long getInitTransReturn() {
        return initTransReturn;
    }

    /**
     * ����initTransReturn���Ե�ֵ��
     * 
     */
    public void setInitTransReturn(long value) {
        this.initTransReturn = value;
    }

}
