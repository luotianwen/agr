package com.zte.agricul.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
/**
 * 移动互联工具类
 * @author luzi
 * @date 2014-2-24
 */
public class NetworkUtil {
	/**
	 * 判断网络连接状态
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context){
		ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
		if(netInfo==null || !netInfo.isAvailable()){
			return false;
		}
		return true;
	}
	/**
	 * 从网络获取字符串响应数据
	 * @param urlStr
	 * @return
	 */
	public static String getStringFromUrl(String urlStr){
		String result = "";
		HttpGet req = new HttpGet(urlStr);
		HttpParams connParam = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(connParam, 5 * 1000);
		HttpConnectionParams.setSoTimeout(connParam, 5 * 1000);
		HttpClient client = new DefaultHttpClient(connParam);
		try{
			HttpResponse resp = client.execute(req);
			if(resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity respEntity = resp.getEntity();
				result = EntityUtils.toString(respEntity, "utf-8");
			}else{
//				Log.e("TAG", "响应失败！");
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
//			Log.e("TAG", "" + e.getMessage());
		}catch(IOException e){
			e.printStackTrace();
//			Log.e("TAG", "" + e.getMessage());
		}
		
		return result;
	}
	
	
	public static String loginByPost(String urls){
		String result="";
		HttpURLConnection conn=null;     
		BufferedReader br=null;    
		try{
			URL url=new URL(urls); 
			conn=(HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(5*1000);
			conn.setReadTimeout(5*1000);

			br=new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));

			StringBuilder str=new StringBuilder();
			String lineStr=null;
			while((lineStr=br.readLine())!=null){
				str.append(lineStr);
			}
			result=str.toString();    	 
		}catch(MalformedURLException e){
			e.printStackTrace();	 	
		}catch(SocketTimeoutException e){
			e.printStackTrace();	 		
		}catch(IOException e){
			e.printStackTrace();	 		
		}	  
		return result;  
	}	  
	
	/**
	 * 从网络获取输入流响应数据
	 * @param urlStr
	 * @return
	 */
	public static InputStream getInputStreamFromUrl(String urlStr){
		InputStream is = null;
		HttpGet req = new HttpGet(urlStr);
		HttpParams connParam = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(connParam, 5 * 1000);
		HttpConnectionParams.setSoTimeout(connParam, 5 * 1000);
		HttpClient client = new DefaultHttpClient(connParam);
		try{
			HttpResponse resp = client.execute(req);
			if(resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity respEntity = resp.getEntity();
				is = respEntity.getContent();
			}else{
				Log.e("TAG", "响应失败！");
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
			Log.e("TAG", "" + e.getMessage());
		}catch(IOException e){
			e.printStackTrace();
			Log.e("TAG", "" + e.getMessage());
		}
		return is;
	}
	/**
	 * 从网络获取图片
	 * @param imageUrl
	 * @return
	 */
	public static Bitmap loadImageFromUrl(String imageUrl){
		Bitmap result = null;
		HttpGet req = new HttpGet(imageUrl);
		HttpParams connParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(connParams, 5 * 1000);
		HttpConnectionParams.setSoTimeout(connParams, 5 * 1000);
		HttpClient client = new DefaultHttpClient(connParams);
		try{
			HttpResponse resp = client.execute(req);
			if(resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity respEntity = resp.getEntity();
				result = BitmapFactory.decodeStream(respEntity.getContent());
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
			Log.e("TAG", "" + e.getMessage());
		}catch(IOException e){
			e.printStackTrace();
			Log.e("TAG", "" + e.getMessage());
		}
		return result;
	}
}
