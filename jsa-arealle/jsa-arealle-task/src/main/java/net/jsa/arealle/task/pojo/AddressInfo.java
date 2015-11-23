package net.jsa.arealle.task.pojo;

public class AddressInfo {
	private AddressNumber address = new AddressNumber();
	private String unit;
	private String roadName;

	public AddressNumber getAddress() {
		return address;
	}
	
	public void setAddress(AddressNumber address) {
		this.address = address;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getRoadName() {
		return roadName;
	}
	
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	
	public String toString() {
		return "roadName:" + roadName + ", unit:" + unit + ", address:" + address.toString();
	}
}
