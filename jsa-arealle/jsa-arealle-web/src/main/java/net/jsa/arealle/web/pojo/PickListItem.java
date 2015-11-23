package net.jsa.arealle.web.pojo;

public class PickListItem {
	private String value;
	private String label;
	
	public PickListItem(String value, String label) {
		setValue(value);
		setLabel(label);
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}