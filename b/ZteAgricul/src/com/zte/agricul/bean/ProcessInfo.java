package com.zte.agricul.bean;

import java.util.List;

public class ProcessInfo {
	private String Status ;
	private List<ProcessBean>  Result ;
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public List<ProcessBean> getResult() {
		return Result;
	}
	public void setResult(List<ProcessBean> result) {
		Result = result;
	}
	
	
	
}
