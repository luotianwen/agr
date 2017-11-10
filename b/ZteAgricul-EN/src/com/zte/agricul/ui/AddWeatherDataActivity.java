package com.zte.agricul.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zte.agricul.zh.R;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.UploadResultBean;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.Util;
import com.zte.agricul.view.DateTimePickDialogUtil;

public class AddWeatherDataActivity extends BaseActivity implements
		OnClickListener {
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private RelativeLayout progressView;
	private boolean isFinish = false;
	private TextView leftTv, ivTitleName, rightTv;
	private TextView time_selected;
	private EditText daytime_tem, daytime_weather, night_weather, night_tem;
	private TextView landName1, landName2;
	private UploadResultBean mBean;

	public static boolean isRefresh = false;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isFinish) {
				return;
			}
			progressView.setVisibility(View.GONE);
			switch (msg.what) {
			case REFRESH_TEXT:
				Util.showToast(getApplicationContext(), getResources().getString(R.string.upload_success),
						Toast.LENGTH_SHORT);
				isRefresh = true;
				finish();
				break;

			case NET_ERROR:
				Util.showToast(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT);
				break;

			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_weather_data);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		progressView = (RelativeLayout) findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);

		leftTv = (TextView) findViewById(R.id.leftTv);
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setOnClickListener(this);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		ivTitleName.setText(getResources().getString(R.string.add_weather_data));
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setVisibility(View.VISIBLE);
		rightTv.setOnClickListener(this);
		rightTv.setText(R.string.save);
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);
		
		time_selected = (TextView) findViewById(R.id.time_selected);
		time_selected.setOnClickListener(this);
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = sDateFormat.format(new java.util.Date());
		time_selected.setText(date);

		landName1 = (TextView) findViewById(R.id.landName1);
		landName1.setText(PlotMainFragmentActivity.landName);
		landName2 = (TextView) findViewById(R.id.landName2);
		landName2.setText(getIntent().getStringExtra("landName"));

		daytime_weather = (EditText) findViewById(R.id.daytime_weather);
		daytime_tem = (EditText) findViewById(R.id.daytime_tem);
		night_weather = (EditText) findViewById(R.id.night_weather);
		night_tem = (EditText) findViewById(R.id.night_tem);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.leftTv:
			finish();
			break;
		case R.id.rightTv:
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(rightTv.getWindowToken(), 0); // Ç¿ÖÆÒþ²Ø¼üÅÌ

			if ("".equals(daytime_weather.getText().toString())
					|| "".equals(daytime_tem.getText().toString())
					|| "".equals(night_weather.getText().toString())
					|| "".equals(night_tem.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.please_edit_data),
						Toast.LENGTH_SHORT);
				return;
			}

			uploadCropData(Constants.SUBMIT_WEATHER_DATA_URL);

			break;
			
		case R.id.time_selected:
			DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
					AddWeatherDataActivity.this, "");
			dateTimePicKDialog.dateTimePicKDialog(time_selected);
			break;
		}
	}

	private void uploadCropData(final String url) {
		// TODO Auto-generated method stub
		progressView.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					
					nameValuePair.add(new BasicNameValuePair("userid", Constants.uid));
					nameValuePair.add(new BasicNameValuePair("time", time_selected.getText().toString()));
					nameValuePair.add(new BasicNameValuePair("landid", getIntent().getStringExtra("landid")));
					nameValuePair.add(new BasicNameValuePair("d1",daytime_tem.getText().toString()));
					nameValuePair.add(new BasicNameValuePair("d2",daytime_weather.getText().toString()));
					nameValuePair.add(new BasicNameValuePair("d3",night_tem.getText().toString()));
					nameValuePair.add(new BasicNameValuePair("d4",night_weather.getText().toString()));
					StringBuffer sb = HttpUtil.getDataFromServer(
							url, nameValuePair);
					Logger.d("ddd", url+ nameValuePair.toString());
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mBean = gson
								.fromJson(sb.toString(), UploadResultBean.class);
						if ("0".equals(mBean.getStatus())) {
							Message msg = mHandler.obtainMessage(REFRESH_TEXT,getResources()
									.getString(R.string.upload_success));
							mHandler.sendMessage(msg);
						} else {
							Message msg = mHandler.obtainMessage(2,
									getResources().getString(R.string.upload_error));
							mHandler.sendMessage(msg);
						}
					} else {
						Logger.d("ddd", "sb==null");
						Message msg = mHandler.obtainMessage(2, getResources()
								.getString(R.string.upload_error));
						mHandler.sendMessage(msg);
					}
				} else {
					Message msg = mHandler.obtainMessage(2, getResources()
							.getString(R.string.upload_error));
					mHandler.sendMessage(msg);
				}
			};
		}.start();
	}
}
