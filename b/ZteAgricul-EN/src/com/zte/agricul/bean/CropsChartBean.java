package com.zte.agricul.bean;

import java.util.List;

public class CropsChartBean {

	private List<String > landname ;
	private List<String > time ;
	private String name ;
	private List<String > list1;
	private List<String > list2;
	private List<WeatherBean>  weatherList ;
	private String  verData ;
	public List<String> getLandname() {
		return landname;
	}
	public void setLandname(List<String> landname) {
		this.landname = landname;
	}
	public List<String> getTime() {
		return time;
	}
	public void setTime(List<String> time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getList1() {
		return list1;
	}
	public void setList1(List<String> list1) {
		this.list1 = list1;
	}
	public List<String> getList2() {
		return list2;
	}
	public void setList2(List<String> list2) {
		this.list2 = list2;
	}
	public List<WeatherBean> getWeatherList() {
		return weatherList;
	}
	public void setWeatherList(List<WeatherBean> weatherList) {
		this.weatherList = weatherList;
	}
	public String getVerData() {
		return verData;
	}
	public void setVerData(String verData) {
		this.verData = verData;
	}
	
}
