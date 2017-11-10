package com.zte.agricul.bean;

import java.io.Serializable;

public class CropTypeBean implements Serializable{
	private String ID ;
	private String Name ;
	private String size;
	private String CropTypeID;
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
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getCropTypeID() {
		return CropTypeID;
	}
	public void setCropTypeID(String cropTypeID) {
		CropTypeID = cropTypeID;
	}
	
}
