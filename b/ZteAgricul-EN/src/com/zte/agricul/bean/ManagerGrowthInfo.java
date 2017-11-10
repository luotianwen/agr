package com.zte.agricul.bean;

import java.util.List;

public class ManagerGrowthInfo {
	private List<ManagerGrowthBean>  Result ;
	private String Status ;
	public List<ManagerGrowthBean> getResult() {
		return Result;
	}
	public void setResult(List<ManagerGrowthBean> result) {
		Result = result;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	
}
