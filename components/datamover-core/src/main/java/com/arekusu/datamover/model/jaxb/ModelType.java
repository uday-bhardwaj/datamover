
package com.arekusu.datamover.model.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{http://www.arekusu.com}DefinitionType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "definitionType"
})
@XmlRootElement(name = "ModelType", namespace = "http://www.arekusu.com")
public class ModelType {

    @XmlElement(name = "DefinitionType", namespace = "http://www.arekusu.com", required = true)
    protected DefinitionType definitionType;
    @XmlAttribute(name = "version")
    protected String version;

    /**
     * Gets the value of the definitionType property.
     * 
     * @return
     *     possible object is
     *     {@link DefinitionType }
     *     
     */
    public DefinitionType getDefinitionType() {
        return definitionType;
    }

    /**
     * Sets the value of the definitionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefinitionType }
     *     
     */
    public void setDefinitionType(DefinitionType value) {
        this.definitionType = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

}
