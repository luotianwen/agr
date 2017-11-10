package com.zte.agricul.bean;

public class CropIndexBean {
	private String Crop_Type_ID;
	private String IndexName;
	private String DataName;
	private String ID;
	
	private String data;
	public String getCrop_Type_ID() {
		return Crop_Type_ID;
	}
	public void setCrop_Type_ID(String crop_Type_ID) {
		Crop_Type_ID = crop_Type_ID;
	}
	public String getIndexName() {
		return IndexName;
	}
	public void setIndexName(String indexName) {
		IndexName = indexName;
	}
	public String getDataName() {
		return DataName;
	}
	public void setDataName(String dataName) {
		DataName = dataName;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
