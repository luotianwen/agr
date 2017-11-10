package com.zte.agricul.bean;

import java.io.Serializable;
import java.util.List;

public class AgriculListBean implements Serializable{
	private String  Name ;
	private String  ID ;
	private String  ImagePath ;
	private String Address ;
	private String UserName ;
	private String  Contury_ID ;
	private String  Province_ID ;
	private String  City_ID ;
	private String  District_ID ;
	private String Phone ;
	private String  Longitude ;
	private String Latitude ;
	private String Frost_Free_Period_MinDay ;
	private String Frost_Free_Period_MaxDay ;
	private String Effective_Accumulated_Temp ;
	private String Annual_Arecipitation ;
	private String Soil_Properties ;
	private String Soil_pH;
	private String N_P_K_Content ;
	private String Organic_Matter_Content ;
	private String Medium_Trace_Element ;
	private String Previous_Crop_Species ;
	
	
	private String Previous_Crop_Yield ;
	private String Size ;
	private String AnnualOutput ;
	private String Remark ;
	private String BaseManagerUserID ;
	
	private List<CropBean>   CropList ;
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
	public String getImagePath() {
		return ImagePath;
	}
	public void setImagePath(String imagePath) {
		ImagePath = imagePath;
	}
	public List<CropBean> getCropList() {
		return CropList;
	}
	public void setCropList(List<CropBean> cropList) {
		CropList = cropList;
	}
	public String getContury_ID() {
		return Contury_ID;
	}
	public void setContury_ID(String contury_ID) {
		Contury_ID = contury_ID;
	}
	public String getProvince_ID() {
		return Province_ID;
	}
	public void setProvince_ID(String province_ID) {
		Province_ID = province_ID;
	}
	public String getCity_ID() {
		return City_ID;
	}
	public void setCity_ID(String city_ID) {
		City_ID = city_ID;
	}
	public String getDistrict_ID() {
		return District_ID;
	}
	public void setDistrict_ID(String district_ID) {
		District_ID = district_ID;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getFrost_Free_Period_MinDay() {
		return Frost_Free_Period_MinDay;
	}
	public void setFrost_Free_Period_MinDay(String frost_Free_Period_MinDay) {
		Frost_Free_Period_MinDay = frost_Free_Period_MinDay;
	}
	public String getFrost_Free_Period_MaxDay() {
		return Frost_Free_Period_MaxDay;
	}
	public void setFrost_Free_Period_MaxDay(String frost_Free_Period_MaxDay) {
		Frost_Free_Period_MaxDay = frost_Free_Period_MaxDay;
	}
	public String getEffective_Accumulated_Temp() {
		return Effective_Accumulated_Temp;
	}
	public void setEffective_Accumulated_Temp(String effective_Accumulated_Temp) {
		Effective_Accumulated_Temp = effective_Accumulated_Temp;
	}
	public String getAnnual_Arecipitation() {
		return Annual_Arecipitation;
	}
	public void setAnnual_Arecipitation(String annual_Arecipitation) {
		Annual_Arecipitation = annual_Arecipitation;
	}
	public String getSoil_Properties() {
		return Soil_Properties;
	}
	public void setSoil_Properties(String soil_Properties) {
		Soil_Properties = soil_Properties;
	}
	public String getSoil_pH() {
		return Soil_pH;
	}
	public void setSoil_pH(String soil_pH) {
		Soil_pH = soil_pH;
	}
	public String getN_P_K_Content() {
		return N_P_K_Content;
	}
	public void setN_P_K_Content(String n_P_K_Content) {
		N_P_K_Content = n_P_K_Content;
	}
	public String getOrganic_Matter_Content() {
		return Organic_Matter_Content;
	}
	public void setOrganic_Matter_Content(String organic_Matter_Content) {
		Organic_Matter_Content = organic_Matter_Content;
	}
	public String getMedium_Trace_Element() {
		return Medium_Trace_Element;
	}
	public void setMedium_Trace_Element(String medium_Trace_Element) {
		Medium_Trace_Element = medium_Trace_Element;
	}
	public String getPrevious_Crop_Species() {
		return Previous_Crop_Species;
	}
	public void setPrevious_Crop_Species(String previous_Crop_Species) {
		Previous_Crop_Species = previous_Crop_Species;
	}
	public String getPrevious_Crop_Yield() {
		return Previous_Crop_Yield;
	}
	public void setPrevious_Crop_Yield(String previous_Crop_Yield) {
		Previous_Crop_Yield = previous_Crop_Yield;
	}
	public String getSize() {
		return Size;
	}
	public void setSize(String size) {
		Size = size;
	}
	public String getAnnualOutput() {
		return AnnualOutput;
	}
	public void setAnnualOutput(String annualOutput) {
		AnnualOutput = annualOutput;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getBaseManagerUserID() {
		return BaseManagerUserID;
	}
	public void setBaseManagerUserID(String baseManagerUserID) {
		BaseManagerUserID = baseManagerUserID;
	}
	
	
}
