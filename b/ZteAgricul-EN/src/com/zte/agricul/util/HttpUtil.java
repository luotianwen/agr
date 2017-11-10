package com.zte.agricul.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.zte.agricul.app.Constants;

/**
 * @author wush
 * @date 2012-2-27
 * @version v1.0
 */
public class HttpUtil {

	public static StringBuffer getDataFromServer(String httpUrl,
			List<NameValuePair> nameValuePair) {
		Logger.d("ddd", "url==" + httpUrl+nameValuePair.toString());
		StringBuffer resultStr = null;
		try {
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
			HttpConnectionParams.setSoTimeout(httpParams, 15000);
			HttpClient httpclient = new DefaultHttpClient(httpParams);
			HttpPost httpPost = new HttpPost(httpUrl);
			if (nameValuePair != null && nameValuePair.size() > 0) {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,
						HTTP.UTF_8));
			}
			HttpResponse httpResponse = httpclient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = httpResponse.getEntity();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						httpEntity.getContent(), "UTF-8"));
				String data = null;
				resultStr = new StringBuffer();
				while ((data = br.readLine()) != null) {
					resultStr.append(data);
				}
				br.close();
				// Logger.println("resultStr===="+resultStr);
			}

		} catch (IllegalStateException e) {
			e.printStackTrace();
			Logger.v("httpUtil", "IllegalStateException");
			return resultStr;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Logger.v("httpUtil", "ClientProtocolException");
			return resultStr;
		} catch (IOException e) {
			e.printStackTrace();
			Logger.v("httpUtil", "IOException");
			return resultStr;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.v("httpUtil", "Exception");
			return resultStr;
		}
		return resultStr;

	}

	public static StringBuffer getDataImageFromServer(String httpUrl,
			List<NameValuePair> nameValuePair, List<String> imageUrlList) {
		final String CHARSET = "UTF-8"; // 设置编码
		StringBuffer resultStr = null;
		try {
			String[] filePath = new String[imageUrlList.size()];
			int size = imageUrlList.size();
			for (int i = 0; i < size; i++) {
				filePath[i] = imageUrlList.get(i);
			}

			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
			HttpConnectionParams.setSoTimeout(httpParams, 15000);
			HttpClient httpclient = new DefaultHttpClient(httpParams);
			HttpPost httpPost = new HttpPost(httpUrl);

			MultipartEntity entity = new MultipartEntity();
			if (nameValuePair != null && nameValuePair.size() > 0) {
				for (int i = 0; i < nameValuePair.size(); i++) {
					entity.addPart("text", new StringBody(nameValuePair.get(i)
							.toString(), Charset.forName(CHARSET)));
				}
			}
			// 上传图片
			for (String p : filePath) {
				entity.addPart("fileimg", new FileBody(new File(p), "image/*"));
			}

			httpPost.setEntity(entity);
			HttpResponse httpResponse = httpclient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = httpResponse.getEntity();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						httpEntity.getContent(), "UTF-8"));
				String data = null;
				resultStr = new StringBuffer();
				while ((data = br.readLine()) != null) {
					resultStr.append(data);
				}
				br.close();
				// Logger.println("resultStr===="+resultStr);
			}

		} catch (IllegalStateException e) {
			e.printStackTrace();
			Logger.v("httpUtil", "IllegalStateException");
			return resultStr;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Logger.v("httpUtil", "ClientProtocolException");
			return resultStr;
		} catch (IOException e) {
			e.printStackTrace();
			Logger.v("httpUtil", "IOException");
			return resultStr;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.v("httpUtil", "Exception");
			return resultStr;
		}
		return resultStr;

	}

	public static StringBuffer getDataFormServerByPost(String outXml,
			String httpUrl) {
		StringBuffer resultStr = null;
		try {
			StringEntity stringEntity = new StringEntity(outXml, HTTP.UTF_8);
			stringEntity.setContentType("application/json");
			HttpPost httpPost = new HttpPost(httpUrl);
			httpPost.setEntity(stringEntity);
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
			HttpConnectionParams.setSoTimeout(httpParams, 10000);
			HttpClient httpclient = new DefaultHttpClient(httpParams);
			// httpclient.
			HttpResponse httpResponse = httpclient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						httpResponse.getEntity().getContent(), "UTF-8"));
				String data = null;
				resultStr = new StringBuffer();
				while ((data = br.readLine()) != null) {
					resultStr.append(data);
				}
				br.close();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
			Logger.v("httpUtil", "IllegalStateException");
			return resultStr;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Logger.v("httpUtil", "ClientProtocolException");
			return resultStr;
		} catch (IOException e) {
			e.printStackTrace();
			Logger.v("httpUtil", "IOException");
			return resultStr;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.v("httpUtil", "Exception");
			return resultStr;
		}
		return resultStr;
	}

	public static StringBuffer uploadSubmit(String url, Map<String, String> param,
			List<String> imageUrlList) throws Exception {
		String[] filePath = new String[imageUrlList.size()];
		int size = imageUrlList.size();
		for (int i = 0; i < size; i++) {
			filePath[i] = imageUrlList.get(i);
			System.out.println(imageUrlList.get(i));
		}
		HttpPost post = new HttpPost(url);
		HttpClient httpClient = new DefaultHttpClient();
		MultipartEntity entity = new MultipartEntity();
		if (param != null && !param.isEmpty()) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				if (entry.getValue() != null&& entry.getValue().trim().length() > 0) {
					entity.addPart(entry.getKey(),new StringBody(entry.getValue(),Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));
				}
			}
		}
		// 添加文件参数
		// if (file != null && file.exists()) {
		// entity.addPart("file", new FileBody(file));
		// }
		// 上传图片
		for (String p : filePath) {
			entity.addPart("fileimg", new FileBody(new File(p), "image/*"));
		}
		post.setEntity(entity);
		HttpResponse response = httpClient.execute(post);
		int stateCode = response.getStatusLine().getStatusCode();
		StringBuffer sb = new StringBuffer();
		if (stateCode == HttpStatus.SC_OK) {
			HttpEntity result = response.getEntity();
			if (result != null) {
				InputStream is = result.getContent();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				String tempLine;
				while ((tempLine = br.readLine()) != null) {
					sb.append(tempLine);
				}
			}
		}
		post.abort();
		return sb;
	}
}
