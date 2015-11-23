package net.jsa.arealle.task.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Road name normalizer.
 * 
 * @author jsaparo
 *
 */
@Component
public class RoadNameNormalizer extends BasicNameNormalizer {
	
	private static Map<String, String> MISC_ROAD_CONVERSIONS = new HashMap<String, String>(100);
	
	public RoadNameNormalizer() {
		addDirectionalLookups();
		
		addLookup("aly", "alley");
		addLookup("ave", "avenue");
		addLookup("av", "avenue");
		addLookup("bv", "boulevard");
		addLookup("blvd", "boulevard");
		addLookup("cir", "circle");
		addLookup("cr", "circle");
		addLookup("ctr", "center");
		addLookup("ct", "court");
		addLookup("crt", "court");
		addLookup("courte", "court");
		addLookup("cv", "cove");
		addLookup("dr", "drive");
		addLookup("drv", "drive");
		addLookup("hlnds", "highlands");
		addLookup("hghlnds", "highlands");
		addLookup("hl", "hill");
		addLookup("ht", "heights");
		addLookup("hts", "heights");
		addLookup("hwy", "highway");
		addLookup("hy", "highway");
		addLookup("is", "island");
		addLookup("isl", "island");
		addLookup("ln", "lane");
		addLookup("lndg", "landing");
		addLookup("lp", "loop");
		addLookup("pr", "pier");
		addLookup("pk", "park");
		addLookup("pl", "place");
		addLookup("pd", "pond");
		addLookup("pnd", "pond");
		addLookup("pt", "point");
		addLookup("pts", "points");
		addLookup("rd", "road");
		addLookup("rg", "ridge");
		addLookup("rdg", "ridge");
		addLookup("sh", "shore");
		addLookup("shr", "shore");
		addLookup("sq", "square");
		addLookup("sqr", "square");
		addLookup("sm", "summit");
		addLookup("smt", "summit");
		addLookup("st", "street");
		addLookup("str", "street");
		addLookup("tr", "terrace");
		addLookup("ter", "terrace");
		addLookup("wy", "way");
		addLookup("ex", "extension");
		addLookup("ext", "extension");
		addLookup("exn", "extension");
		addLookup("exd", "extension");
		addLookup("ph", "path");
		addLookup("pth", "path");
		addLookup("bk", "brook");
		addLookup("brk", "brook");
		addLookup("crs", "crossing");
		addLookup("mt", "mount");
		addLookup("whf", "wharf");
	}

	// Saints alive! St could be confused with Street
	static {
		MISC_ROAD_CONVERSIONS.put("st", "Saint");
		MISC_ROAD_CONVERSIONS.put("snt", "Saint");
	}

	protected String[] normalizeParts(String[] parts) {
		
		// Drop trailing direction if it exists - it's confusing/useless
		int lastIx = parts.length-1;
		String lastPart = parts[lastIx];
		if (isDirectionalPart(lastPart)) { 
			parts = StrUtils.chop(parts, lastIx, lastIx);
			lastIx--;
		}

		// Convert known abbreviations that could be confused with common abbreviations
		for (int i = 0; i < parts.length; i++) {
			if (i < lastIx) {  // Don't apply to last component - reserved for road type
				parts[i] = replaceCommonNonRoadPart(parts[i]);
			}
		}
		
		return super.normalizeParts(parts);
	}

	private String replaceCommonNonRoadPart(String part) {
		return MISC_ROAD_CONVERSIONS.containsKey(part) ? MISC_ROAD_CONVERSIONS.get(part) : part;
	}
}
