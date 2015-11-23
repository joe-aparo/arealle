package net.jsa.arealle.task.util;

import org.springframework.stereotype.Component;

/**
 * Normalize feature names.
 * 
 * @author jsaparo
 *
 */
@Component
public class FeatureNameNormalizer extends BasicNameNormalizer {

	public FeatureNameNormalizer() {
		addDirectionalLookups();

		addLookup("cr", "Conservation Area");
		addLookup("wma", "Conservation Area");
		addLookup("wce", "Conservation Area");
		addLookup("st", "Street");
		addLookup("cons", "Conservation Area");
		addLookup("rd", "Road");
		addLookup("nwr", "Conservation Area");
		addLookup("ave", "Avenue");
		addLookup("mt", "Mount");
		addLookup("plgd", "Playground");
		addLookup("jr", "Junior");
		addLookup("dr", "Drive");
		addLookup("rt", "Route");
		addLookup("se", "South east");
		addLookup("sch", "School");
		addLookup("hs", "High School");
		addLookup("rec", "Recreation");
		addLookup("reg", "Regional");
		addLookup("con", "Conservation");
		addLookup("wcr", "Conservation Area");
		addLookup("pk", "Park");
		addLookup("conserv", "Conservation");
		addLookup("gr", "Great");
		addLookup("ja", "James");
		addLookup("ln", "Lane");
		addLookup("lndg", "Landing");
		addLookup("cir", "Circle");
		addLookup("wpr", "Conservation Area");
		addLookup("ar", "Area");
		addLookup("pt", "Point");
		addLookup("wwi", "WWI");
		addLookup("wldlf", "Wildlife");
		addLookup("cc", "Country Club");
		addLookup("railtrail", "Rail Trail");
		addLookup("refg", "Refuge");
		addLookup("hq", "Headquarters");
		addLookup("jr/sr", "Junior/Senior");
		addLookup("sr", "Senior");
		addLookup("ct", "Court");
		addLookup("av", "Avenue");
		addLookup("natl", "National");
		addLookup("wldlife", "Wildlife");
		addLookup("clb", "Club");
		addLookup("ne", "North East");
		addLookup("nr", "Near");
		addLookup("proj", "Project");
		addLookup("sq", "Square");
		addLookup("voc", "Vocational");
		addLookup("wdlf", "Wildlife");
		addLookup("ballfields", "Ball Field");
		addLookup("bcm", "Bay Club at Mattapoisett");
		addLookup("bldg", "Building");
		addLookup("ca", "Conservation Area");
		addLookup("hsp", "Hospital");
		addLookup("ext", "Extension");
		addLookup("gt", "Great");
		addLookup("hw", "Highway");
		addLookup("hwy", "Highway");
		addLookup("jfk", "JFK");
		addLookup("rgnl", "Regional");
		addLookup("rr", "Rail Road");
		addLookup("rte", "Route");
		addLookup("trst", "Trust");
		addLookup("womans", "Womens");
	}	
}
