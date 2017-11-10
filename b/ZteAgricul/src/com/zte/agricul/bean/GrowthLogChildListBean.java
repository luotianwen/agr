package com.zte.agricul.bean;

import java.util.List;

public class GrowthLogChildListBean {
	private String ID ;
	private String Land_ID ;
	private String Land_Name ;
	private String Growth_Pro_ID ;
	private String Growth_Pro_Name ;
	private String Content ;
	private String TimeTitle ;
	private String Time ;
	private List<String >  ImgList ;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getLand_ID() {
		return Land_ID;
	}
	public void setLand_ID(String land_ID) {
		Land_ID = land_ID;
	}
	public String getLand_Name() {
		return Land_Name;
	}
	public void setLand_Name(String land_Name) {
		Land_Name = land_Name;
	}
	public String getGrowth_Pro_ID() {
		return Growth_Pro_ID;
	}
	public void setGrowth_Pro_ID(String growth_Pro_ID) {
		Growth_Pro_ID = growth_Pro_ID;
	}
	public String getGrowth_Pro_Name() {
		return Growth_Pro_Name;
	}
	public void setGrowth_Pro_Name(String growth_Pro_Name) {
		Growth_Pro_Name = growth_Pro_Name;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getTimeTitle() {
		return TimeTitle;
	}
	public void setTimeTitle(String timeTitle) {
		TimeTitle = timeTitle;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public List<String> getImgList() {
		return ImgList;
	}
	public void setImgList(List<String> imgList) {
		ImgList = imgList;
	}
	
}
