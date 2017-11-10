package com.zte.agricul.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zte.agricul.zh.R;
import com.zte.agricul.app.Constants;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.ResolveJson;
import com.zte.agricul.util.Util;

public class LoginActivity extends Activity  implements OnClickListener{
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private Button login;
	// private LoadingDialog dialog;
	public Gson gson;
	private SharedPreferences sharedPreferences;
	private EditText userName, password;
	private CheckBox isCheck ;
	private RelativeLayout  progressView;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			progressView.setVisibility(View.GONE);
			switch (msg.what) {
			case REFRESH_TEXT:
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
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
		setContentView(R.layout.login_activity);
		// dialog = new LoadingDialog(this);
		gson = new Gson();
		sharedPreferences = getSharedPreferences(Constants.SHARE_PRE_FILE,
				Context.MODE_PRIVATE);
		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub
		login = (Button) findViewById(R.id.login);
		userName = (EditText) findViewById(R.id.login_UserName);
		password = (EditText) findViewById(R.id.login_Password);
		isCheck= (CheckBox) findViewById(R.id.is_check);
		progressView =(RelativeLayout) findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);
		progressView.setVisibility(View.GONE);
		login.setOnClickListener(this);
		password.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_ENTER) {

					progressView.setVisibility(View.VISIBLE);

					initLoginData();

					return true;
				}
				return false;
			}
		});
		if (sharedPreferences.getBoolean(Constants.IS_CHECK, false)) {
			userName.setText(sharedPreferences.getString(Constants.LOGIN_NAME, ""));
			System.out.println("=1="+userName.getText().toString());
			password.setText(sharedPreferences.getString(Constants.LOGIN_PASSWORD, ""));
			isCheck.setChecked(sharedPreferences.getBoolean(Constants.IS_CHECK, false));
		}
	}

	private void initLoginData() {
		// TODO Auto-generated method stub
		progressView.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair("username",
							userName.getText().toString()));
					nameValuePair.add(new BasicNameValuePair("password",
							password.getText().toString()));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.LOGIN_URL, nameValuePair);
					if (null != sb) {
						Logger.d("dddd", "sb==" + sb.toString());
						String sucess = ResolveJson.unUidParse(
								getApplicationContext(), sb.toString());
						Logger.d("dddd", "sucess==" + sucess);
						if ("0".equals(sucess)) {
							sharedPreferences.edit()
									.putBoolean(Constants.field_is_login, true)
									.commit();
							sharedPreferences.edit()
							.putString(Constants.LOGIN_NAME,userName.getText().toString())
							.commit();
							if (isCheck.isChecked()) {
								sharedPreferences.edit()
								.putBoolean(Constants.IS_CHECK, true)
								.commit();
								System.out.println("=="+userName.getText().toString());
								sharedPreferences.edit()
								.putString(Constants.LOGIN_PASSWORD,password.getText().toString())
								.commit();
							}
							Constants.IS_LOGIN = true;
							Message msg = handler.obtainMessage(REFRESH_TEXT,
									getResources().getString(R.string.login_succeed));
							handler.sendMessage(msg);
						} else if ("2".equals(sucess)) {
							Message msg = handler.obtainMessage(NET_ERROR,
									getResources().getString(R.string.user_name_error));
							handler.sendMessage(msg);

						} else {
							Message msg = handler.obtainMessage(NET_ERROR,
									getResources().getString(R.string.user_name_error));
							handler.sendMessage(msg);
						}
					} else {
						Logger.d("ddd", "sb==null");

						Message msg = handler.obtainMessage(2, getResources().getString(R.string.net_data_error));
						handler.sendMessage(msg);
					}
				} else {
					Message msg = handler.obtainMessage(2, getResources().getString(R.string.net_data_error));
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	private boolean isZh() {
		Locale locale = getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		System.out.println("=="+language);
		if (language.endsWith("zh"))
			return true;
		else
			return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login:
			 if ("".equals(userName.getText().toString())) {
			 Util.showToast(getApplicationContext(), getResources().getString(R.string.username_not_null),
			 Toast.LENGTH_SHORT);
			 return ;
			 }
			 if("".equals(password.getText().toString())){
			 Util.showToast(getApplicationContext(), getResources().getString(R.string.pwd_not_null),
			 Toast.LENGTH_SHORT);
			 return ;
			 }
			progressView.setVisibility(View.GONE);
			initLoginData();
		
			break;

		default:
			break;
		}
	}
}
