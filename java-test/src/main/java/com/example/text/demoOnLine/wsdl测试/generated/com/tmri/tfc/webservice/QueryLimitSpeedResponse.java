
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
 *         &lt;element name="QueryLimitSpeedReturn" type="{http://www.w3.org/2001/XMLSchema}long"/>
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
    "queryLimitSpeedReturn"
})
@XmlRootElement(name = "QueryLimitSpeedResponse")
public class QueryLimitSpeedResponse {

    @XmlElement(name = "QueryLimitSpeedReturn")
    protected long queryLimitSpeedReturn;

    /**
     * ��ȡqueryLimitSpeedReturn���Ե�ֵ��
     * 
     */
    public long getQueryLimitSpeedReturn() {
        return queryLimitSpeedReturn;
    }

    /**
     * ����queryLimitSpeedReturn���Ե�ֵ��
     * 
     */
    public void setQueryLimitSpeedReturn(long value) {
        this.queryLimitSpeedReturn = value;
    }

}
