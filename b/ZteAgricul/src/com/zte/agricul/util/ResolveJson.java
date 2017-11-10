package com.zte.agricul.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.zte.agricul.app.Constants;
import com.zte.agricul.bean.CropsChartBean;
import com.zte.agricul.bean.CropsChartInfo;
import com.zte.agricul.bean.WeatherBean;

public class ResolveJson {
	// 解析push返回消息
	public static String unUidParse(Context context, String str) {

		String islogin = "";
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				Constants.SHARE_PRE_FILE, Context.MODE_PRIVATE);
		try {
			JSONObject json = new JSONObject(str);
			islogin = json.getString("Status");

			Logger.d("dddd", "islogin==" + islogin);
			if (!"0".equals(islogin)) {
				return islogin;
			}
			JSONObject jsonArray = json.getJSONObject("Result");

			Constants.uid = jsonArray.optString("User_ID");
			Constants.userName = jsonArray.optString("UserName");
			sharedPreferences.edit()
			.putString(Constants.USER_INFO, jsonArray.toString()).commit();
			
			sharedPreferences.edit()
					.putString(Constants.USER_ID, Constants.uid).commit();
			sharedPreferences.edit()
					.putString(Constants.USER_NAME, Constants.userName)
					.commit();
			sharedPreferences.edit().putBoolean(Constants.field_is_login, true)
					.commit();
			Constants.IS_LOGIN = true;
			
			JSONArray jsonArray2 = jsonArray.getJSONArray("UserRoleLost");
			String aaa ="";
			for (int i = 0; i < jsonArray2.length(); i++) {
				aaa = aaa+jsonArray2.getJSONObject(i).getString("RoleRightID");
			}
			System.out.println("aaa==="+aaa);
			sharedPreferences.edit().putString(Constants.USER_PERMISSION, aaa)
			.commit();
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return islogin;
	}
	
	public static String uploadResultParse(String str) {
		String result = "";
		try {
			JSONObject json = new JSONObject(str);
			result = json.getString("Status");
		} catch (JSONException e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}
	
	
	// 新闻列表数据
	public static CropsChartInfo cropsChartParse(String str) {
		CropsChartInfo  chartInfo  = new CropsChartInfo() ;
		try {
			JSONObject json = new JSONObject(str);
			if (!"0".equals(json.getString("Status"))) {
				return null;
			}
			chartInfo.setStatus(json.getString("Status"));
			JSONArray jsonArray = json.getJSONArray("Result");
			List<CropsChartBean>  cropBeans= new ArrayList<CropsChartBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				if (i==1||i==2||i==(jsonArray.length()-1)) {
					CropsChartBean cropBean = new CropsChartBean();
					JSONArray jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("landname");
					List<String >  landList = new ArrayList<String>();
					for (int j = 0; j < jsonArray2.length(); j++) {
						landList.add(jsonArray2.get(j).toString());
					}
					cropBean.setLandname(landList);
					Logger.d("aaaa", "land==="+cropBean.getLandname().toString());
					
					JSONArray jsonArray3 = jsonArray.getJSONObject(i).getJSONArray("time");
					List<String >  timeList = new ArrayList<String>();
					for (int j = 0; j < jsonArray3.length(); j++) {
						timeList.add(jsonArray3.get(j).toString());
					}
					cropBean.setTime(timeList);
					Logger.d("aaaa", "time==="+cropBean.getTime().toString());
					if (i==(jsonArray.length()-1)) {
						JSONArray jsonArray6 = jsonArray.getJSONObject(i).getJSONArray("list1");
						List<WeatherBean >  weatherBeans = new ArrayList<WeatherBean>();
						for (int j = 0; j < jsonArray6.length(); j++) {
							WeatherBean  weatherBean = new WeatherBean();
							weatherBean.setC(jsonArray6.getJSONObject(j).optString("c"));
							System.out.println("=="+jsonArray6.getJSONObject(j).optString("c"));
							weatherBean.setW(jsonArray6.getJSONObject(j).optString("w"));
							weatherBean.setW_night(jsonArray6.getJSONObject(j).optString("w_night"));
							weatherBean.setC_night(jsonArray6.getJSONObject(j).optString("c_night"));
							weatherBean.setTime(jsonArray6.getJSONObject(j).optString("time"));
							weatherBeans.add(weatherBean);
						}
						cropBean.setWeatherList(weatherBeans);
					}else {
						JSONArray jsonArray4 = jsonArray.getJSONObject(i).getJSONArray("list1");
						List<String >  list1 = new ArrayList<String>();
						for (int j = 0; j < jsonArray4.length(); j++) {
							list1.add(jsonArray4.get(j).toString());
						}
						cropBean.setList1(list1);
						Logger.d("aaaa", "list1==="+cropBean.getList1().toString());
						JSONArray jsonArray5 = jsonArray.getJSONObject(i).getJSONArray("list2");
						List<String >  list2 = new ArrayList<String>();
						for (int j = 0; j < jsonArray5.length(); j++) {
							list2.add(jsonArray5.get(j).toString());
						}
						cropBean.setList2(list2);
						Logger.d("aaaa", "list2==="+cropBean.getList2().toString());
					}
					cropBeans.add(cropBean);
				}
				
			}
			
			chartInfo.setResult(cropBeans);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return chartInfo;
	}
	
	
	
	// 新闻列表数据
	public static CropsChartInfo cropsChartParse2(String str) {
		CropsChartInfo  chartInfo  = new CropsChartInfo() ;
		try {
			JSONObject json = new JSONObject(str);
			if (!"0".equals(json.getString("Status"))) {
				return null;
			}
			chartInfo.setStatus(json.getString("Status"));
			JSONArray jsonArray = json.getJSONArray("Result");
			List<CropsChartBean>  cropBeans= new ArrayList<CropsChartBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
					CropsChartBean cropBean = new CropsChartBean();
					JSONArray jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("landname");
					List<String >  landList = new ArrayList<String>();
					for (int j = 0; j < jsonArray2.length(); j++) {
						landList.add(jsonArray2.get(j).toString());
					}
					cropBean.setLandname(landList);
					Logger.d("aaaa", "land==="+cropBean.getLandname().toString());
					
					JSONArray jsonArray3 = jsonArray.getJSONObject(i).getJSONArray("time");
					List<String >  timeList = new ArrayList<String>();
					for (int j = 0; j < jsonArray3.length(); j++) {
						timeList.add(jsonArray3.get(j).toString());
					}
					cropBean.setTime(timeList);
					cropBean.setName(jsonArray.getJSONObject(i).optString("name"));
					cropBean.setVerData(jsonArray.getJSONObject(i).optString("verData"));
					Logger.d("aaaa", "time==="+cropBean.getTime().toString());
					if ("天气数据".equals(jsonArray.getJSONObject(i).optString("name"))) {
						JSONArray jsonArray6 = jsonArray.getJSONObject(i).getJSONArray("list1");
						List<WeatherBean >  weatherBeans = new ArrayList<WeatherBean>();
						for (int j = 0; j < jsonArray6.length(); j++) {
							WeatherBean  weatherBean = new WeatherBean();
							weatherBean.setC(jsonArray6.getJSONObject(j).optString("c"));
							System.out.println("=="+jsonArray6.getJSONObject(j).optString("c"));
							weatherBean.setW(jsonArray6.getJSONObject(j).optString("w"));
							weatherBean.setW_night(jsonArray6.getJSONObject(j).optString("w_night"));
							weatherBean.setC_night(jsonArray6.getJSONObject(j).optString("c_night"));
							weatherBean.setTime(jsonArray6.getJSONObject(j).optString("time"));
							weatherBeans.add(weatherBean);
						}
						cropBean.setWeatherList(weatherBeans);
					}else {
						JSONArray jsonArray4 = jsonArray.getJSONObject(i).getJSONArray("list1");
						List<String >  list1 = new ArrayList<String>();
						for (int j = 0; j < jsonArray4.length(); j++) {
							list1.add(jsonArray4.get(j).toString());
						}
						cropBean.setList1(list1);
						Logger.d("aaaa", "list1==="+cropBean.getList1().toString());
						JSONArray jsonArray5 = jsonArray.getJSONObject(i).getJSONArray("list2");
						List<String >  list2 = new ArrayList<String>();
						for (int j = 0; j < jsonArray5.length(); j++) {
							list2.add(jsonArray5.get(j).toString());
						}
						cropBean.setList2(list2);
						Logger.d("aaaa", "list2==="+cropBean.getList2().toString());
					}
					
					
					if (null!=jsonArray.getJSONObject(i).getJSONArray("list1")&&jsonArray.getJSONObject(i).getJSONArray("list1").length()>0&&
							null!=jsonArray.getJSONObject(i).getJSONArray("time")&&jsonArray.getJSONObject(i).getJSONArray("time").length()>0) {
						
						System.out.println("lenth===="+i);
						
						cropBeans.add(cropBean);
					}

				
			}
			
			chartInfo.setResult(cropBeans);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return chartInfo;
	}
}
