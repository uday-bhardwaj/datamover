
package com.arekusu.datamover.model.jaxb;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="alias" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="column" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 *
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "FieldType", namespace = "http://www.arekusu.com")
public class FieldType {

    @XmlAttribute(name = "alias")
    protected String alias;
    @XmlAttribute(name = "column")
    protected String column;

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
     * Gets the value of the column property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumn() {
        return column;
    }

    /**
     * Sets the value of the column property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumn(String value) {
        this.column = value;
    }

}
