package com.zte.agricul.bean;

import java.io.Serializable;

public class CropBean implements Serializable{
	private String LandID ;
	private String CropTypeName ;
	private String isnew ;
	public String getLandID() {
		return LandID;
	}
	public void setLandID(String landID) {
		LandID = landID;
	}
	public String getCropTypeName() {
		return CropTypeName;
	}
	public void setCropTypeName(String cropTypeName) {
		CropTypeName = cropTypeName;
	}
	public String getIsnew() {
		return isnew;
	}
	public void setIsnew(String isnew) {
		this.isnew = isnew;
	}
	
	
}
