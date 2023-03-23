
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
     * 获取kkbh属性的值。
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
     * 设置kkbh属性的值。
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
     * 获取fxlx属性的值。
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
     * 设置fxlx属性的值。
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
     * 获取cdh属性的值。
     * 
     */
    public long getCdh() {
        return cdh;
    }

    /**
     * 设置cdh属性的值。
     * 
     */
    public void setCdh(long value) {
        this.cdh = value;
    }

    /**
     * 获取info属性的值。
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
     * 设置info属性的值。
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
