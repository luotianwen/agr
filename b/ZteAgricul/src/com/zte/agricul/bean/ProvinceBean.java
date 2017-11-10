package com.zte.agricul.bean;

import java.io.Serializable;

public class ProvinceBean implements Serializable {
	private String ProID ;
	private String Name ;
	public String getProID() {
		return ProID;
	}
	public void setProID(String proID) {
		ProID = proID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
}
