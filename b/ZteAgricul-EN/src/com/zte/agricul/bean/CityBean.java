package com.zte.agricul.bean;

import java.io.Serializable;

public class CityBean implements Serializable {
	private String ProID ;
	private String CityID ;
	private String DisID ;
	private String Name ;
	private String ID ;
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
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getCityID() {
		return CityID;
	}
	public void setCityID(String cityID) {
		CityID = cityID;
	}
	public String getDisID() {
		return DisID;
	}
	public void setDisID(String disID) {
		DisID = disID;
	}
	
}
