package com.zte.agricul.bean;

import java.io.Serializable;
import java.util.List;

public class AgriculMainBean implements Serializable{
	
	
	
	private List<ConturyBean>  Contury;
	private List<ProvinceBean>  Province;
	private List<CityBean>  City;
	private List<AgriculListBean> BaseList;
	private List<CityBean>  UsersList;
	
	
	public List<AgriculListBean> getBaseList() {
		return BaseList;
	}

	public void setBaseList(List<AgriculListBean> baseList) {
		BaseList = baseList;
	}

	public List<ProvinceBean> getProvince() {
		return Province;
	}

	public void setProvince(List<ProvinceBean> province) {
		Province = province;
	}

	public List<CityBean> getCity() {
		return City;
	}

	public void setCity(List<CityBean> city) {
		City = city;
	}

	public List<ConturyBean> getContury() {
		return Contury;
	}

	public void setContury(List<ConturyBean> contury) {
		Contury = contury;
	}

	public List<CityBean> getUsersList() {
		return UsersList;
	}

	public void setUsersList(List<CityBean> usersList) {
		UsersList = usersList;
	}
	
	
}
