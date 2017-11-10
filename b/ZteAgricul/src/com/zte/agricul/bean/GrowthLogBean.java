package com.zte.agricul.bean;

import java.util.List;

public class GrowthLogBean {
	private List<String >  LandNameList ;
	private List<GrowthLogParentListBean>   LogList;
	private List<CropIndexBean>  CropIndex;
	
	private List<String>  AdditiveList;
	
	public List<String> getLandNameList() {
		return LandNameList;
	}
	public void setLandNameList(List<String> landNameList) {
		LandNameList = landNameList;
	}
	public List<GrowthLogParentListBean> getLogList() {
		return LogList;
	}
	public void setLogList(List<GrowthLogParentListBean> logList) {
		LogList = logList;
	}
	public List<CropIndexBean> getCropIndex() {
		return CropIndex;
	}
	public void setCropIndex(List<CropIndexBean> cropIndex) {
		CropIndex = cropIndex;
	}
	public List<String> getAdditiveList() {
		return AdditiveList;
	}
	public void setAdditiveList(List<String> additiveList) {
		AdditiveList = additiveList;
	}
	
	
}
