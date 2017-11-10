package com.zte.agricul.app;

import com.amap.api.maps2d.model.LatLng;

import android.os.Environment;

public class Constants {
	public static boolean LOG = true;
	// references��Ϣ
	public static final String SHARE_PRE_FILE = "energy_preferences";
	public static final String CACHE_PATH = Environment
			.getExternalStorageDirectory() + "/zte/agricul/cache/";
	/**
	 * �û�ͷ�� ���ղü�ͼƬ�ı���·��
	 */
	public static final String SCR_IMAGE_PATH = Environment
			.getExternalStorageDirectory() + "/zte/screenshots/";
	/***
	 * ���ݽӿ�
	 */
	public static String BASE_URL = "http://1.180.83.230:8032";
	/**
	 * ��¼�ӿ�
	 */
	public static String LOGIN_URL = BASE_URL + "/Login.aspx";
	/***
	 * ��ҳͷ���ݺ�pop����
	 */
	public static String MAIN_HEADER_URL = BASE_URL + "/IndexData.aspx";
	/***
	 * ��ҳ����
	 */
	public static String MAIN_URL = BASE_URL + "/GetBaseList.aspx";
	/***
	 * http://117.146.89.194:8032/GetManagerBase.aspx?userid=2
		��ȡ���ع����б�
	 * 
	 */
	public static String MAIN_LIST_URL = BASE_URL + "/GetManagerBase.aspx";
	
	/***
	 * �����ӿ�
	 */
	public static String SEARCH_URL = BASE_URL + "/Search.aspx";
	
	/***
	 * �ؿ���ҳ������Ϣ�ӿ�
	 */
	public static String PLOT_BASE_URL = BASE_URL + "/GetBase.aspx";
	/***
	 * �ؿ���ҳ������Ϣ�ӿ�
	 */
	public static String GROWTH_DATA_URL = BASE_URL + "/GetGrowthData.aspx";
	/***
	 * �ؿ���ҳ�б���Ϣ�ӿ�
	 */
	public static String PLOT_LAND_LIST_URL = BASE_URL + "/GetLand.aspx";
	
	/***
	 * �ؿ���ҳ�б���Ϣ�ӿ�
	 */
	public static String PLOT_ALL_LIST_URL = BASE_URL + "/GetManagerLand.aspx";
	
	/***
	 * ������־ �б�ӿ�
	 */
	public static String GROWTH_LOG_LIST_URL = BASE_URL + "/GetGrowthLog.aspx";
	
	/***
	 * ������־ �б�ӿ�
	 */
	public static String GROWTH_PROCESS_DATA_URL = BASE_URL + "/GetGrowthProcess.aspx";
	
	/***
	 * �ϴ�������¼
	 */
	public static String UPLOAD_GROWTH_LOG_DATA_URL = BASE_URL + "/SubmitGrowthLog.aspx";
	
	/***
	 * �ϴ���������
	 */
	public static String UPLOAD_GROWTH_DATA_URL = BASE_URL + "/SubmitGrowthData.aspx";
	
	/***
	 * 
	 * http://117.146.89.194:8032/SubmitNewLnad.aspx?baseid=1&landname=3%E5%8F%B7%E5%9D%97%E5%9C%B0&content=&croptypeid=1&cropbrandid=1&sowuser=2&sowtime=2016-8-1&size=500&annualoutput=2000&remark=&userid=2
     *�ϴ��½��ؿ����ݽӿ�
	 */
	public static String UPLOAD_NEW_LAND_DATA_URL = BASE_URL + "/SubmitNewLnad.aspx";
	
	/***
	 * 
	 * http://117.146.89.194:8032/SubmitNewLnad.aspx?baseid=1&landname=3%E5%8F%B7%E5%9D%97%E5%9C%B0&content=&croptypeid=1&cropbrandid=1&sowuser=2&sowtime=2016-8-1&size=500&annualoutput=2000&remark=&userid=2
     *�༭�ؿ���Ϣ
	 */
	public static String EDIT_LAND_DATA_URL = BASE_URL + "/UpdateLandInfo.aspx";
	
	
	/***
	 * 
	 * 	http://117.146.89.194:8032/SubmitNewCrops.aspx?cropname=%E5%9C%9F%E8%B1%86
		���ũ����ӿ�
	 */
	public static String UPLOAD_NEW_CROPS_DATA_URL = BASE_URL + "/SubmitNewCrops.aspx";
	/***
	 * 
	 * http://117.146.89.194:8032/SubmitNewCropsBrand.aspx?cropbrandname=%E5%A4%A7%E7%95%AA%E8%96%AF%E4%B8%80%E5%8F%B7
			���ũ����Ʒ��
	 */
	public static String UPLOAD_CROPS_BRAND_DATA_URL = BASE_URL + "/SubmitNewCropsBrand.aspx";

	/***
	  * 
	  *	http://117.146.89.194:8032/DeleteLand.aspx?landid=5&baseid=1
		ɾ���ؿ�ӿ�
	  */
	public static String UPLOAD_DELETE_PLOT_DATA_URL = BASE_URL + "/DeleteLand.aspx";
	
	
	/***
	  * 
	  *	http://117.146.89.194:8032/DeleteBase.aspx?baseid=5
		ɾ�����ؽӿ�
	  */
	public static String UPLOAD_DELETE_BASE_LAND_URL = BASE_URL + "/DeleteBase.aspx";
	
	/***
	  * 
	  *	http://117.146.89.194:8032/DeleteBase.aspx?baseid=5
		��ӻ��ؽӿ�
	  */
	public static String ADD_BASE_LAND_URL = BASE_URL + "/SubmitNewBase.aspx";
	/***
	  * 
	  *	http://117.146.89.194:8032/DeleteBase.aspx?baseid=5
		�޸Ļ��ؽӿ�
	  */
	public static String EDIT_BASE_LAND_URL = BASE_URL + "/UpdateBaseInfo.aspx";
	/***
	  * 
	  *	 ��ȡ���������б�
		http://117.146.89.194:8032/GetManagerGrowthData.aspx?landid=4
	  */
	public static String GET_MANAGER_GROWTH_DATA_URL = BASE_URL + "/GetManagerGrowthData.aspx";
	
	/***
	  * ɾ����������
http://117.146.89.194:8032/DeleteGrowthData.aspx?dataid=13
	  */
	public static String DELETE_GROWTH_DATA_URL = BASE_URL + "/DeleteGrowthData.aspx";
	
	/***
	 * 
��ȡ��������
http://117.146.89.194:8032/GetManagerWeatherData.aspx?landid=1
	  */
	public static String GET_MANAGER_WEATHER_DATA_URL = BASE_URL + "/GetManagerWeatherData.aspx";
	
	/***
	  * 
	  *	ɾ����������
	  *  http://117.146.89.194:8032/DeleteWeatherData.aspx?dataid=100
	  */
	public static String DELETE_WEATHER_DATA_URL = BASE_URL + "/DeleteWeatherData.aspx";
	
	/***
	  * 
	  *	�����������
	  *  http://117.146.89.194:8032/DeleteWeatherData.aspx?dataid=100
	  */
	public static String SUBMIT_WEATHER_DATA_URL = BASE_URL + "/SubmitWeatherData.aspx";
	
	
	/***
	  * 
	  *	�����������
	  *  http://117.146.89.194:8032/DeleteWeatherData.aspx?dataid=100
	  */
	public static String DELETE_LOG_DATA_URL = BASE_URL + "/DeleteLog.aspx";
	
	/***
	  * 
	  *	�޸�����
	  *  http://117.146.89.194:8033/UpdatePWD.aspx?username=wangwu&oldpwd=123456&newpwd=123456
	  */
	public static String UPDATE_PWD_URL = BASE_URL + "/UpdatePWD.aspx";
	
	// ��¼
	public static final String field_is_login = "is_login";
	public static boolean IS_LOGIN = false;

	
	//�Ƿ��ס�ʺ�
	public static final String IS_CHECK="is_check";
	//�ʺ��ֶ�
	public static final String LOGIN_NAME="login_name";
	//�����ֶ�
	public static final String LOGIN_PASSWORD="login_password";
	
	/**
	 * �û���Ϣ
	 */
	public static final String USER_ID = "uid";
	public static final String USER_NAME = "userName";
	public static final String USER_INFO = "userInfo";
//	public static final String USER_TYPE = "userType";
	public static String uid = "-1";
	public static String userName = "";
	
	public static final String USER_PERMISSION="user_per";

}
