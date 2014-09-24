
package com.arekusu.datamover.model.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.arekusu.com}FieldsType" minOccurs="0"/>
 *         &lt;element ref="{http://www.arekusu.com}ReferencesType" minOccurs="0"/>
 *         &lt;element ref="{http://www.arekusu.com}LinksType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="alias" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sourceField" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="destinationField" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "fieldsType",
    "referencesType",
    "linksType"
})
@XmlRootElement(name = "EntityType", namespace = "http://www.arekusu.com")
public class EntityType {

    @XmlElement(name = "FieldsType", namespace = "http://www.arekusu.com")
    protected FieldsType fieldsType;
    @XmlElement(name = "ReferencesType", namespace = "http://www.arekusu.com")
    protected ReferencesType referencesType;
    @XmlElement(name = "LinksType", namespace = "http://www.arekusu.com")
    protected LinksType linksType;
    @XmlAttribute(name = "alias")
    protected String alias;
    @XmlAttribute(name = "sourceField")
    protected String sourceField;
    @XmlAttribute(name = "destinationField")
    protected String destinationField;

    /**
     * Gets the value of the fieldsType property.
     * 
     * @return
     *     possible object is
     *     {@link FieldsType }
     *     
     */
    public FieldsType getFieldsType() {
        return fieldsType;
    }

    /**
     * Sets the value of the fieldsType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldsType }
     *     
     */
    public void setFieldsType(FieldsType value) {
        this.fieldsType = value;
    }

    /**
     * Gets the value of the referencesType property.
     * 
     * @return
     *     possible object is
     *     {@link ReferencesType }
     *     
     */
    public ReferencesType getReferencesType() {
        return referencesType;
    }

    /**
     * Sets the value of the referencesType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferencesType }
     *     
     */
    public void setReferencesType(ReferencesType value) {
        this.referencesType = value;
    }

    /**
     * Gets the value of the linksType property.
     * 
     * @return
     *     possible object is
     *     {@link LinksType }
     *     
     */
    public LinksType getLinksType() {
        return linksType;
    }

    /**
     * Sets the value of the linksType property.
     * 
     * @param value
     *     allowed object is
     *     {@link LinksType }
     *     
     */
    public void setLinksType(LinksType value) {
        this.linksType = value;
    }

    /**
     * Gets the value of the alias property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the value of the alias property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlias(String value) {
        this.alias = value;
    }

    /**
     * Gets the value of the sourceField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceField() {
        return sourceField;
    }

    /**
     * Sets the value of the sourceField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceField(String value) {
        this.sourceField = value;
    }

    /**
     * Gets the value of the destinationField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationField() {
        return destinationField;
    }

    /**
     * Sets the value of the destinationField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationField(String value) {
        this.destinationField = value;
    }

}
