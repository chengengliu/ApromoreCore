//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.03.23 at 09:25:45 AM EST 
//


package org.yawlfoundation.yawlschema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreationModeCodeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CreationModeCodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="static"/>
 *     &lt;enumeration value="dynamic"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CreationModeCodeType")
@XmlEnum
public enum CreationModeCodeType {

    @XmlEnumValue("static")
    STATIC("static"),
    @XmlEnumValue("dynamic")
    DYNAMIC("dynamic");
    private final String value;

    CreationModeCodeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CreationModeCodeType fromValue(String v) {
        for (CreationModeCodeType c: CreationModeCodeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
