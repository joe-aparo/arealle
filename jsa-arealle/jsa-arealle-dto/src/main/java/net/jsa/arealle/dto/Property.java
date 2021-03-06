//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7-b41 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.14 at 11:55:48 AM EDT 
//


package net.jsa.arealle.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * <p>Java class for Property complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Property">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="propertyId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="stateId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="townId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parcelFeatureName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parcelId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="roadName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addrNum" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="addrStr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="units" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="unitsx" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="listings" type="{http://arealle.com/service/arealle}PropertyListing" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Property", propOrder = {
    "propertyId",
    "stateId",
    "townId",
    "parcelFeatureName",
    "parcelId",
    "roadName",
    "addrNum",
    "addrStr",
    "type",
    "units",
    "unitsx",
    "listings"
})
@Document
public class Property {

    @XmlElement(required = true)
    protected String propertyId;
    protected String stateId;
    protected String townId;
    protected String parcelFeatureName;
    protected String parcelId;
    protected String roadName;
    protected Integer addrNum;
    protected String addrStr;
    protected String type;
    protected Integer units;
    protected Integer unitsx;
    protected List<PropertyListing> listings;

    /**
     * Gets the value of the propertyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Id
    public String getPropertyId() {
        return propertyId;
    }

    /**
     * Sets the value of the propertyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertyId(String value) {
        this.propertyId = value;
    }

    /**
     * Gets the value of the stateId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateId() {
        return stateId;
    }

    /**
     * Sets the value of the stateId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateId(String value) {
        this.stateId = value;
    }

    /**
     * Gets the value of the townId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTownId() {
        return townId;
    }

    /**
     * Sets the value of the townId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTownId(String value) {
        this.townId = value;
    }

    /**
     * Gets the value of the parcelFeatureName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParcelFeatureName() {
        return parcelFeatureName;
    }

    /**
     * Sets the value of the parcelFeatureName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParcelFeatureName(String value) {
        this.parcelFeatureName = value;
    }

    /**
     * Gets the value of the parcelId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParcelId() {
        return parcelId;
    }

    /**
     * Sets the value of the parcelId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParcelId(String value) {
        this.parcelId = value;
    }

    /**
     * Gets the value of the roadName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoadName() {
        return roadName;
    }

    /**
     * Sets the value of the roadName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoadName(String value) {
        this.roadName = value;
    }

    /**
     * Gets the value of the addrNum property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAddrNum() {
        return addrNum;
    }

    /**
     * Sets the value of the addrNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAddrNum(Integer value) {
        this.addrNum = value;
    }

    /**
     * Gets the value of the addrStr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddrStr() {
        return addrStr;
    }

    /**
     * Sets the value of the addrStr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddrStr(String value) {
        this.addrStr = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the units property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUnits() {
        return units;
    }

    /**
     * Sets the value of the units property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUnits(Integer value) {
        this.units = value;
    }

    /**
     * Gets the value of the unitsx property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUnitsx() {
        return unitsx;
    }

    /**
     * Sets the value of the unitsx property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUnitsx(Integer value) {
        this.unitsx = value;
    }

    /**
     * Gets the value of the listings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertyListing }
     * 
     * 
     */
    public List<PropertyListing> getListings() {
        if (listings == null) {
            listings = new ArrayList<PropertyListing>();
        }
        return this.listings;
    }

}
