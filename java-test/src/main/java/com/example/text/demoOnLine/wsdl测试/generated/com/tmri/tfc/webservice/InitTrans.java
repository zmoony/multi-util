
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
 *         &lt;element name="kkbh" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fxlx" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cdh" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="info" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "kkbh",
    "fxlx",
    "cdh",
    "info"
})
@XmlRootElement(name = "InitTrans")
public class InitTrans {

    @XmlElement(required = true)
    protected String kkbh;
    @XmlElement(required = true)
    protected String fxlx;
    protected long cdh;
    @XmlElement(required = true)
    protected String info;

    /**
     * ��ȡkkbh���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKkbh() {
        return kkbh;
    }

    /**
     * ����kkbh���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKkbh(String value) {
        this.kkbh = value;
    }

    /**
     * ��ȡfxlx���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFxlx() {
        return fxlx;
    }

    /**
     * ����fxlx���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFxlx(String value) {
        this.fxlx = value;
    }

    /**
     * ��ȡcdh���Ե�ֵ��
     * 
     */
    public long getCdh() {
        return cdh;
    }

    /**
     * ����cdh���Ե�ֵ��
     * 
     */
    public void setCdh(long value) {
        this.cdh = value;
    }

    /**
     * ��ȡinfo���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfo() {
        return info;
    }

    /**
     * ����info���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfo(String value) {
        this.info = value;
    }

}
