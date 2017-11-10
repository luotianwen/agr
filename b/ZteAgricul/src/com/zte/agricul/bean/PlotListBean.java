package com.zte.agricul.bean;

import java.util.List;

public class PlotListBean {
	private List<String > ImageList ;
	private List<PlotBean>  BaseList ;
	public List<String> getImageList() {
		return ImageList;
	}
	public void setImageList(List<String> imageList) {
		ImageList = imageList;
	}
	public List<PlotBean> getBaseList() {
		return BaseList;
	}
	public void setBaseList(List<PlotBean> baseList) {
		BaseList = baseList;
	}
	
}
