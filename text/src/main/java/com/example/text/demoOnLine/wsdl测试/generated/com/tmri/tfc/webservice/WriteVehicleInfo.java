
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
 *         &lt;element name="hphm" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="hpzl" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="gcsj" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="clsd" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="clxs" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="wfdm" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cwkc" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="hpys" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cllx" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fzhpzl" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fzhphm" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fzhpys" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="clpp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="clwx" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="csys" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tplj" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tp1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tp2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tp3" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tztp" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "hphm",
    "hpzl",
    "gcsj",
    "clsd",
    "clxs",
    "wfdm",
    "cwkc",
    "hpys",
    "cllx",
    "fzhpzl",
    "fzhphm",
    "fzhpys",
    "clpp",
    "clwx",
    "csys",
    "tplj",
    "tp1",
    "tp2",
    "tp3",
    "tztp"
})
@XmlRootElement(name = "WriteVehicleInfo")
public class WriteVehicleInfo {

    @XmlElement(required = true)
    protected String kkbh;
    @XmlElement(required = true)
    protected String fxlx;
    protected long cdh;
    @XmlElement(required = true)
    protected String hphm;
    @XmlElement(required = true)
    protected String hpzl;
    @XmlElement(required = true)
    protected String gcsj;
    protected long clsd;
    protected long clxs;
    @XmlElement(required = true)
    protected String wfdm;
    protected long cwkc;
    @XmlElement(required = true)
    protected String hpys;
    @XmlElement(required = true)
    protected String cllx;
    @XmlElement(required = true)
    protected String fzhpzl;
    @XmlElement(required = true)
    protected String fzhphm;
    @XmlElement(required = true)
    protected String fzhpys;
    @XmlElement(required = true)
    protected String clpp;
    @XmlElement(required = true)
    protected String clwx;
    @XmlElement(required = true)
    protected String csys;
    @XmlElement(required = true)
    protected String tplj;
    @XmlElement(required = true)
    protected String tp1;
    @XmlElement(required = true)
    protected String tp2;
    @XmlElement(required = true)
    protected String tp3;
    @XmlElement(required = true)
    protected String tztp;

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
     * 获取hphm属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHphm() {
        return hphm;
    }

    /**
     * 设置hphm属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHphm(String value) {
        this.hphm = value;
    }

    /**
     * 获取hpzl属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHpzl() {
        return hpzl;
    }

    /**
     * 设置hpzl属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHpzl(String value) {
        this.hpzl = value;
    }

    /**
     * 获取gcsj属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGcsj() {
        return gcsj;
    }

    /**
     * 设置gcsj属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGcsj(String value) {
        this.gcsj = value;
    }

    /**
     * 获取clsd属性的值。
     * 
     */
    public long getClsd() {
        return clsd;
    }

    /**
     * 设置clsd属性的值。
     * 
     */
    public void setClsd(long value) {
        this.clsd = value;
    }

    /**
     * 获取clxs属性的值。
     * 
     */
    public long getClxs() {
        return clxs;
    }

    /**
     * 设置clxs属性的值。
     * 
     */
    public void setClxs(long value) {
        this.clxs = value;
    }

    /**
     * 获取wfdm属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWfdm() {
        return wfdm;
    }

    /**
     * 设置wfdm属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWfdm(String value) {
        this.wfdm = value;
    }

    /**
     * 获取cwkc属性的值。
     * 
     */
    public long getCwkc() {
        return cwkc;
    }

    /**
     * 设置cwkc属性的值。
     * 
     */
    public void setCwkc(long value) {
        this.cwkc = value;
    }

    /**
     * 获取hpys属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHpys() {
        return hpys;
    }

    /**
     * 设置hpys属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHpys(String value) {
        this.hpys = value;
    }

    /**
     * 获取cllx属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCllx() {
        return cllx;
    }

    /**
     * 设置cllx属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCllx(String value) {
        this.cllx = value;
    }

    /**
     * 获取fzhpzl属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFzhpzl() {
        return fzhpzl;
    }

    /**
     * 设置fzhpzl属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFzhpzl(String value) {
        this.fzhpzl = value;
    }

    /**
     * 获取fzhphm属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFzhphm() {
        return fzhphm;
    }

    /**
     * 设置fzhphm属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFzhphm(String value) {
        this.fzhphm = value;
    }

    /**
     * 获取fzhpys属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFzhpys() {
        return fzhpys;
    }

    /**
     * 设置fzhpys属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFzhpys(String value) {
        this.fzhpys = value;
    }

    /**
     * 获取clpp属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClpp() {
        return clpp;
    }

    /**
     * 设置clpp属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClpp(String value) {
        this.clpp = value;
    }

    /**
     * 获取clwx属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClwx() {
        return clwx;
    }

    /**
     * 设置clwx属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClwx(String value) {
        this.clwx = value;
    }

    /**
     * 获取csys属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCsys() {
        return csys;
    }

    /**
     * 设置csys属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCsys(String value) {
        this.csys = value;
    }

    /**
     * 获取tplj属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTplj() {
        return tplj;
    }

    /**
     * 设置tplj属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTplj(String value) {
        this.tplj = value;
    }

    /**
     * 获取tp1属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTp1() {
        return tp1;
    }

    /**
     * 设置tp1属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTp1(String value) {
        this.tp1 = value;
    }

    /**
     * 获取tp2属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTp2() {
        return tp2;
    }

    /**
     * 设置tp2属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTp2(String value) {
        this.tp2 = value;
    }

    /**
     * 获取tp3属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTp3() {
        return tp3;
    }

    /**
     * 设置tp3属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTp3(String value) {
        this.tp3 = value;
    }

    /**
     * 获取tztp属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTztp() {
        return tztp;
    }

    /**
     * 设置tztp属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTztp(String value) {
        this.tztp = value;
    }

}
