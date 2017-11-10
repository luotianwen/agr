package com.zte.agricul.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zte.agricul.R;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.ChangePwdBean;
import com.zte.agricul.bean.PlotAllListInfo;
import com.zte.agricul.ui.PlotAllListActivity.PlotMainAllListAdapter;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.Util;

public class ChangePasswordActivity extends BaseActivity implements
		OnClickListener {
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private TextView leftTv, ivTitleName, rightTv;

	private EditText edit_pw, edit_new_pw, edit_re_new_pw;

	private RelativeLayout progressView;
	private SharedPreferences sharedPreferences;
	
	private ChangePwdBean  mBean ;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			progressView.setVisibility(View.GONE);
			switch (msg.what) {
			case REFRESH_TEXT:
				Util.showToast(getApplicationContext(), getResources().getString(R.string.change_pwd_success),
						Toast.LENGTH_SHORT);
				finish();
				break;
			case NET_ERROR:
				Util.showToast(getApplicationContext(),  getResources().getString(R.string.change_pwd_fail),
						Toast.LENGTH_SHORT);
				break;
				
				
			}
			// listView.onRefreshComplete();
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password_activity);
		sharedPreferences = getSharedPreferences(Constants.SHARE_PRE_FILE,
				Context.MODE_PRIVATE);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		leftTv = (TextView) findViewById(R.id.leftTv);
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setOnClickListener(this);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		ivTitleName.setText(getResources().getString(R.string.change_pwd));
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setOnClickListener(this);
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);
		rightTv.setText(getResources().getString(R.string.save));
		rightTv.setVisibility(View.VISIBLE);

		edit_pw = (EditText) findViewById(R.id.edit_pw);
		edit_new_pw = (EditText) findViewById(R.id.edit_new_pw);
		edit_re_new_pw = (EditText) findViewById(R.id.edit_re_new_pw);
		
		progressView = (RelativeLayout) findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.leftTv:
			finish();
			break;
		case R.id.rightTv:
			if ("".equals(edit_pw.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.pwd_not_null),
						Toast.LENGTH_SHORT);
				return;

			}
			if ("".equals(edit_new_pw.getText().toString())) {
				Util.showToast(getApplicationContext(),  getResources().getString(R.string.new_pwd_not_null),
						Toast.LENGTH_SHORT);
				return;
			}
			if (!edit_re_new_pw.getText().toString()
					.equals(edit_new_pw.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.enter_the_same_pwd),
						Toast.LENGTH_SHORT);
				return;
			}
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
			imm.hideSoftInputFromWindow(edit_new_pw.getWindowToken(), 0); //Ç¿ÖÆÒþ²Ø¼üÅÌ 
			
			
			initData() ;
			break;

		default:
			break;
		}
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		progressView.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair("oldpwd",
							edit_pw.getText().toString()));
					nameValuePair.add(new BasicNameValuePair("newpwd",
							edit_new_pw.getText().toString()));
					nameValuePair.add(new BasicNameValuePair("username", sharedPreferences.getString(Constants.LOGIN_NAME, "")));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.UPDATE_PWD_URL, nameValuePair);
					Logger.d(
							"ddd",
							Constants.UPDATE_PWD_URL
									+ nameValuePair.toString());
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mBean = gson.fromJson(sb.toString(),
								ChangePwdBean.class);
						if ("0".equals(mBean.getStatus())) {
							Message msg = mHandler.obtainMessage(REFRESH_TEXT);
							mHandler.sendMessage(msg);
						} else {
							Message msg = mHandler.obtainMessage(
									2,
									getResources().getString(
											R.string.net_data_error));
							mHandler.sendMessage(msg);
						}
					} else {
						Logger.d("ddd", "sb==null");

						Message msg = mHandler.obtainMessage(2, getResources()
								.getString(R.string.net_data_error));
						mHandler.sendMessage(msg);
					}
				} else {
					Message msg = mHandler.obtainMessage(2, getResources()
							.getString(R.string.net_data_error));
					mHandler.sendMessage(msg);
				}
			};
		}.start();
	}
}
