package net.jsa.arealle.task.pojo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FeatureInfo {
	private static final int INIT_ATTR_MAP_SIZE = 10;
	private String id;
	private String typeId;
	private String name;
	private String stateId;
	private Map<String, String> attrs = new LinkedHashMap<String, String>(INIT_ATTR_MAP_SIZE);
	private Map<String, DomainFeatureInfo> domainInfo;
	private String crsId;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String type) {
		this.typeId = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getAttrs() {
		return attrs;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String state) {
		this.stateId = state;
	}

	public String getCrsId() {
		return crsId;
	}

	public void setCrsId(String crsId) {
		this.crsId = crsId;
	}

	public void setAttrs(Map<String, String> attrs) {
		this.attrs = attrs;
	}
	
	public Map<String, DomainFeatureInfo> getDomainFeatureInfo() {
		if (domainInfo == null) {
			domainInfo = new HashMap<String, DomainFeatureInfo>();
		}

		return domainInfo;
	}
}
