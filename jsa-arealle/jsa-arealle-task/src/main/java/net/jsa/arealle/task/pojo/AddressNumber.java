package net.jsa.arealle.task.pojo;

public class AddressNumber {
	private int num;
	private String str;
	
	public int getNumber() {
		return num;
	}
	public void setNumber(int num) {
		this.num = num;
	}
	public String getString() {
		return str;
	}
	public void setString(String str) {
		this.str = str;
	}
	
	public String toString() {
		return "num:" + num + ", str:" + str;
	}
}
