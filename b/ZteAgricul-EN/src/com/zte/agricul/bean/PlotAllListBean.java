package com.zte.agricul.bean;

import java.io.Serializable;
import java.util.List;

public class PlotAllListBean  implements Serializable{
	private String NextLandName ;
	private List<PlotBean>  LandList ;
	private List<CropTypeBean>  UsersList ;
	private List<CropTypeBean>  CropTypeList ;
	private List<CropTypeBean>  CropBrandList;
	private List<CropTypeBean>  CropAdditiveList;
	public String getNextLandName() {
		return NextLandName;
	}
	public void setNextLandName(String nextLandName) {
		NextLandName = nextLandName;
	}
	public List<PlotBean> getLandList() {
		return LandList;
	}
	public void setLandList(List<PlotBean> landList) {
		LandList = landList;
	}
	
	
	public List<CropTypeBean> getUsersList() {
		return UsersList;
	}
	public void setUsersList(List<CropTypeBean> usersList) {
		UsersList = usersList;
	}
	public List<CropTypeBean> getCropTypeList() {
		return CropTypeList;
	}
	public void setCropTypeList(List<CropTypeBean> cropTypeList) {
		CropTypeList = cropTypeList;
	}
	public List<CropTypeBean> getCropBrandList() {
		return CropBrandList;
	}
	public void setCropBrandList(List<CropTypeBean> cropBrandList) {
		CropBrandList = cropBrandList;
	}
	public List<CropTypeBean> getCropAdditiveList() {
		return CropAdditiveList;
	}
	public void setCropAdditiveList(List<CropTypeBean> cropAdditiveList) {
		CropAdditiveList = cropAdditiveList;
	}
	
	
	
}
