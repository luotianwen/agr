package com.zte.agricul.bean;

import java.io.Serializable;

public class UserBean implements Serializable{
	private String ID;
	private String Name ;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	
	
}
