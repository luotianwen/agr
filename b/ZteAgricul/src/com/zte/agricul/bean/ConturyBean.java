package com.zte.agricul.bean;

import java.io.Serializable;

public class ConturyBean implements Serializable{
	private String ConID ;
	private String Name ;
	public String getConID() {
		return ConID;
	}
	public void setConID(String conID) {
		ConID = conID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
}
