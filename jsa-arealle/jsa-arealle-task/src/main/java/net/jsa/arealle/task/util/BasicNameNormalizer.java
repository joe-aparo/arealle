package net.jsa.arealle.task.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Base name normalizer.
 * 
 * @author jsaparo
 *
 */
@Component
public class BasicNameNormalizer implements INameNormalizer {
	public static final String NORTH = "North";
	public static final String EAST = "East";
	public static final String WEST = "West";
	public static final String SOUTH = "South";
	
	private static final Set<String> DIRECTIONAL_SET = new HashSet<String>(10);
	
	static {
		DIRECTIONAL_SET.add("n");
		DIRECTIONAL_SET.add("no");
		DIRECTIONAL_SET.add("e");
		DIRECTIONAL_SET.add("ea");
		DIRECTIONAL_SET.add("w");
		DIRECTIONAL_SET.add("we");
		DIRECTIONAL_SET.add("s");
		DIRECTIONAL_SET.add("so");		
	}
	
	private Map<String, String> lookups = new HashMap<String, String>(100);
	
	public String normalize(String nameIn) {
		if (StringUtils.isEmpty(nameIn)) {
			return nameIn;
		}
		
		StringBuilder nameOut = new StringBuilder(nameIn.length());
		
		String[] parts = normalizeParts(StringUtils.splitPreserveAllTokens(nameIn.trim().replace("&nbsp;", " ")));

		int ct = 0;
		for (String part : parts) {
			if (!StringUtils.isEmpty(part)) {
				if (ct++ > 0) {
					nameOut.append(' ');
				}
				nameOut.append(part);
			}
		}
		
		return nameOut.toString();
	}
	
	protected String[] normalizeParts(String[] parts) {
		
		for (int i = 0; i < parts.length; i++) {
			parts[i] = normalizePart(parts[i]);
		}
		
		return parts;
	}

	protected String normalizePart(String part) {
		String lcPart = part.toLowerCase();
		String repl = lookup(lcPart);
		
		return StrUtils.properCase(repl != null ? repl : lcPart);
	}
	
	protected String lookup(String part) {
		if (StringUtils.isEmpty(part)) {
			return part;
		}
		
		return lookups.get(part);
	}

	protected void addLookup(String from, String to) {
		lookups.put(from.toLowerCase(), to); // look-ups are by lowercase
	}
	
	protected boolean isDirectionalPart(String part) {
		return DIRECTIONAL_SET.contains(part);
	}
	
	protected void addDirectionalLookups() {
		addLookup("n", NORTH);
		addLookup("no", NORTH);
		addLookup("e", EAST);
		addLookup("ea", EAST);
		addLookup("w", WEST);
		addLookup("we", WEST);
		addLookup("s", SOUTH);
		addLookup("so", SOUTH);		
	}
}
