package ind.jsa.crib.ds.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simple set of dataset related options passed in as a single
 * set of values on AbstractDataSet construction.
 * 
 * @author jo26419
 *
 */
public class DataSetOptions {
	private List<String> propertyOrdering;
	private Set<String> identityProperties;
	private Set<String> referenceProperties;
	private Set<String> readableProperties;
	private Set<String> writableProperties;
	private Set<String> filterableProperties;
	private Map<String, Object> defaultParamValues = new LinkedHashMap<String, Object>();
	private boolean caseInsensitiveSearch = false;
	
	public List<String> getPropertyOrdering() {
		return propertyOrdering;
	}
	
	public void setPropertyOrdering(List<String> propertyOrdering) {
		this.propertyOrdering = propertyOrdering;
	}
	
	public Set<String> getIdentityProperties() {
		return identityProperties;
	}
	
	public void setIdentityProperties(Set<String> identityProperties) {
		this.identityProperties = identityProperties;
	}
	
	public Set<String> getReferenceProperties() {
		return referenceProperties;
	}
	
	public void setReferenceProperties(Set<String> referenceProperties) {
		this.referenceProperties = referenceProperties;
	}
	
	public Set<String> getReadableProperties() {
		return readableProperties;
	}

	public void setReadableProperties(Set<String> readableProperties) {
		this.readableProperties = readableProperties;
	}

	public Set<String> getWritableProperties() {
		return writableProperties;
	}
	
	public void setWritableProperties(Set<String> writableProperties) {
		this.writableProperties = writableProperties;
	}
	
	public Set<String> getFilterableProperties() {
		return filterableProperties;
	}
	
	public void setFilterableProperties(Set<String> filterableProperties) {
		this.filterableProperties = filterableProperties;
	}

	public Map<String, Object> getDefaultParamValues() {
		return defaultParamValues;
	}

	public void setDefaultParamValues(Map<String, Object> defaultParamValues) {
		this.defaultParamValues = defaultParamValues;
	}

	public boolean isCaseInsensitiveSearch() {
		return caseInsensitiveSearch;
	}

	public void setCaseInsensitiveSearch(boolean caseInsensitiveSearch) {
		this.caseInsensitiveSearch = caseInsensitiveSearch;
	}
}
