package com.zte.agricul.bean;

import java.io.Serializable;

public class PlotAllListInfo implements Serializable{
	private PlotAllListBean Result ;
	private String Status ;
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public PlotAllListBean getResult() {
		return Result;
	}
	public void setResult(PlotAllListBean result) {
		Result = result;
	}
	
}
