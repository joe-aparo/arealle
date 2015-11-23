package net.jsa.arealle.task.process;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.jsa.arealle.task.util.TownNameNormalizer;

@Component
public class TownFeatureReader extends DomainFeatureReader {
	
	@Resource(name="townNameNormalizer")
	private TownNameNormalizer townNameNormalizer;
	
	@Override
	protected String getFeatureName(Map<String, Object> data) {
		return townNameNormalizer.normalize((String) data.get("TOWN"));
	}
	
	protected Map<String, String> getFeatureAttrs(Map<String, Object> data) {
		Map<String, String> attrs = new LinkedHashMap<String, String>();
		
		attrs.put("SOURCE_ID", data.get("TOWN_ID").toString());
		attrs.put("TOWN_TYPE", (String) data.get("TYPE"));
		Long townPop = (Long) data.get("POP2010");
		if (townPop != null) {
			attrs.put("POPULATION", String.format("%08d", townPop.intValue()));
		}
		
		return attrs;
	}
}
