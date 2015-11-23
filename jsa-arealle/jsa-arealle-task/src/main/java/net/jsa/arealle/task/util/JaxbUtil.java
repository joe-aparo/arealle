package net.jsa.arealle.task.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import net.jsa.arealle.dto.Property;

public class JaxbUtil {
	
	/**
	 * Convert a given property object to an XML string.
	 * 
	 * @param property The property to convert
	 * @return The resulting XML string, or null if there was a conversion error.
	 */
	public static String marshalPropertyToXml(Property property)
	{
		String xml = null;
		
		try {
			StringWriter sw = new StringWriter();
			JAXBContext jaxbCtx = JAXBContext.newInstance(Property.class);
			Marshaller marshaller = jaxbCtx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			QName qName = new QName(Property.class.getPackage().getName(), "property");
			JAXBElement<Property> root = new JAXBElement<Property>(qName, Property.class, property);
			 
			marshaller.marshal(root, sw);
			
			xml = sw.toString();
		} catch (JAXBException ex) {
			System.out.println("Error marshalling property: " + ex.getLinkedException().getMessage());
		}
		
		return xml;
	}
	
	/**
	 * Convert a given XML string to an property object.
	 * 
	 * @param xml The XML string to convert
	 * @return The resulting property object, or null if there was a conversion error.
	 */
	public static Property unmarshalXmlToProperty(String xml) {
		Property property = null;
		
		try {
			JAXBContext jaxbCtx = JAXBContext.newInstance(Property.class);
			Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
			StringReader reader = new StringReader(xml);
			StreamSource src = new StreamSource(reader);
			JAXBElement<Property> root = unmarshaller.unmarshal(src, Property.class);
			
			property = root.getValue();
		} catch (JAXBException ex) {
			System.out.println("Error unmarshalling property from xml: " + xml + 
				"\nError is: " + ex.getLinkedException().getMessage());
		}

		return property;
	}

	/**
	 * Convert a given property object to a JSON string.
	 * 
	 * @param property The property to convert
	 * @return The resulting JSON string, or null if there was a conversion error.
	 */
	public static String marshalPropertyToJson(Property property)
	{
		String json = null;
		
		try {
			JAXBContext jaxbCtx = JAXBContext.newInstance(Property.class);
			Marshaller marshaller = jaxbCtx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty("eclipselink.media-type", "application/json");

			StringWriter sw = new java.io.StringWriter();
			marshaller.marshal(property, sw);
			
			json = sw.toString();

		} catch (JAXBException ex) {
			System.out.println("Error marshalling property: " + ex.getLinkedException().getMessage());
		}
		
		return json;
	}
	
	/**
	 * Convert a given JSON string to a property object.
	 * 
	 * @param json The JSON string to convert
	 * @return The resulting property object, or null if there was a conversion error.
	 */
	public static Property unmarshalJsonToProperty(String json) {
		Property property = null;
		
		try {
			JAXBContext jaxbCtx = JAXBContext.newInstance(Property.class);
			Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
			unmarshaller.setProperty("eclipselink.media-type", "application/json");
			
			StringReader reader = new StringReader(json);
			property = (Property) unmarshaller.unmarshal(reader);
		} catch (JAXBException ex) {
			System.out.println("Error unmarshalling property from json: " + json + 
				"\nError is: " + ex.getLinkedException().getMessage());
		}

		return property;
	}
}
