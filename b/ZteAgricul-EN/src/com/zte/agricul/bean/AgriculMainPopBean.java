package com.zte.agricul.bean;

import java.util.List;

public class AgriculMainPopBean {
	private String Land_Count ;
	private String Crops_Count ;
	private String Annual_Yield ;
	
	private List<ProvinceBean>  Province  ;
	private List<CityBean>  City  ;
	private List<CropTypeBean>  CropType  ;
	private List<CropTypeBean>  AnnualOutput; 
	private List<CropTypeBean>  BaseSize;
	public String getLand_Count() {
		return Land_Count;
	}
	public void setLand_Count(String land_Count) {
		Land_Count = land_Count;
	}
	public String getCrops_Count() {
		return Crops_Count;
	}
	public void setCrops_Count(String crops_Count) {
		Crops_Count = crops_Count;
	}
	public String getAnnual_Yield() {
		return Annual_Yield;
	}
	public void setAnnual_Yield(String annual_Yield) {
		Annual_Yield = annual_Yield;
	}
	public List<ProvinceBean> getProvince() {
		return Province;
	}
	public void setProvince(List<ProvinceBean> province) {
		Province = province;
	}
	public List<CityBean> getCity() {
		return City;
	}
	public void setCity(List<CityBean> city) {
		City = city;
	}
	public List<CropTypeBean> getCropType() {
		return CropType;
	}
	public void setCropType(List<CropTypeBean> cropType) {
		CropType = cropType;
	}
	public List<CropTypeBean> getAnnualOutput() {
		return AnnualOutput;
	}
	public void setAnnualOutput(List<CropTypeBean> annualOutput) {
		AnnualOutput = annualOutput;
	}
	public List<CropTypeBean> getBaseSize() {
		return BaseSize;
	}
	public void setBaseSize(List<CropTypeBean> baseSize) {
		BaseSize = baseSize;
	}
	
	
	
}
