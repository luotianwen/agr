package com.zte.agricul.bean;

import java.util.List;

public class CropsChartInfo {
	private String Status ;
	private List<CropsChartBean>  Result ;
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public List<CropsChartBean> getResult() {
		return Result;
	}
	public void setResult(List<CropsChartBean> result) {
		Result = result;
	}
}
