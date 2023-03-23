
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
     * ��ȡhphm���Ե�ֵ��
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
     * ����hphm���Ե�ֵ��
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
     * ��ȡhpzl���Ե�ֵ��
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
     * ����hpzl���Ե�ֵ��
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
     * ��ȡgcsj���Ե�ֵ��
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
     * ����gcsj���Ե�ֵ��
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
     * ��ȡclsd���Ե�ֵ��
     * 
     */
    public long getClsd() {
        return clsd;
    }

    /**
     * ����clsd���Ե�ֵ��
     * 
     */
    public void setClsd(long value) {
        this.clsd = value;
    }

    /**
     * ��ȡclxs���Ե�ֵ��
     * 
     */
    public long getClxs() {
        return clxs;
    }

    /**
     * ����clxs���Ե�ֵ��
     * 
     */
    public void setClxs(long value) {
        this.clxs = value;
    }

    /**
     * ��ȡwfdm���Ե�ֵ��
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
     * ����wfdm���Ե�ֵ��
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
     * ��ȡcwkc���Ե�ֵ��
     * 
     */
    public long getCwkc() {
        return cwkc;
    }

    /**
     * ����cwkc���Ե�ֵ��
     * 
     */
    public void setCwkc(long value) {
        this.cwkc = value;
    }

    /**
     * ��ȡhpys���Ե�ֵ��
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
     * ����hpys���Ե�ֵ��
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
     * ��ȡcllx���Ե�ֵ��
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
     * ����cllx���Ե�ֵ��
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
     * ��ȡfzhpzl���Ե�ֵ��
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
     * ����fzhpzl���Ե�ֵ��
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
     * ��ȡfzhphm���Ե�ֵ��
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
     * ����fzhphm���Ե�ֵ��
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
     * ��ȡfzhpys���Ե�ֵ��
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
     * ����fzhpys���Ե�ֵ��
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
     * ��ȡclpp���Ե�ֵ��
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
     * ����clpp���Ե�ֵ��
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
     * ��ȡclwx���Ե�ֵ��
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
     * ����clwx���Ե�ֵ��
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
     * ��ȡcsys���Ե�ֵ��
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
     * ����csys���Ե�ֵ��
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
     * ��ȡtplj���Ե�ֵ��
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
     * ����tplj���Ե�ֵ��
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
     * ��ȡtp1���Ե�ֵ��
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
     * ����tp1���Ե�ֵ��
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
     * ��ȡtp2���Ե�ֵ��
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
     * ����tp2���Ե�ֵ��
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
     * ��ȡtp3���Ե�ֵ��
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
     * ����tp3���Ե�ֵ��
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
     * ��ȡtztp���Ե�ֵ��
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
     * ����tztp���Ե�ֵ��
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
