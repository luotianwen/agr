package com.zte.agricul.bean;

import java.util.List;

public class GrowthLogParentListBean {

	private String Name ;
	private List<GrowthLogChildListBean>   List;
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public List<GrowthLogChildListBean> getList() {
		return List;
	}
	public void setList(List<GrowthLogChildListBean> list) {
		List = list;
	}
	
	
}
