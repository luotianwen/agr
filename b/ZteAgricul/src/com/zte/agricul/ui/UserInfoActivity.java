package com.zte.agricul.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zte.agricul.R;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.PlotAllListInfo;
import com.zte.agricul.bean.UserInfoBean;

public class UserInfoActivity extends BaseActivity implements OnClickListener{
	private TextView leftTv, ivTitleName, rightTv;
	
	private TextView  user_email,user_phone,user_area,user_duty,user_name;
	private SharedPreferences sharedPreferences;
	private Button exit_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
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
		ivTitleName.setText(getResources().getString(R.string.user_info));
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setOnClickListener(this);
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);
		rightTv.setText(getResources().getString(R.string.change_pwd));
		rightTv.setVisibility(View.VISIBLE);
		
		exit_btn =(Button) findViewById(R.id.exit_btn);
		exit_btn.setOnClickListener(this);
		user_name=(TextView) findViewById(R.id.user_name);
		user_duty=(TextView) findViewById(R.id.user_duty);
		user_area=(TextView) findViewById(R.id.user_area);
		user_phone=(TextView) findViewById(R.id.user_phone);
		user_email=(TextView) findViewById(R.id.user_email);
		
		
		String info =sharedPreferences.getString(Constants.USER_INFO, "");
		
		UserInfoBean	mBean = gson.fromJson(info,
				UserInfoBean.class);
		
		user_name.setText(getResources().getString(R.string.user_name)+mBean.getUserName());
		user_duty.setText(getResources().getString(R.string.duty)+mBean.getUserRole());
//		user_area.setText("所属区域："+mBean.getSex());
		user_area.setVisibility(View.GONE);
		user_phone.setText(getResources().getString(R.string.phone)+mBean.getPhone());
		user_email.setText(getResources().getString(R.string.email)+mBean.getEmail());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.leftTv:
			finish();
			break;
		case R.id.rightTv:
			Intent intent = new Intent(getApplicationContext(),ChangePasswordActivity.class);
			startActivity(intent);
			break;
			
		case R.id.exit_btn:
			showClearCacheDialog();
			break;	
			
		default:
			break;
		}
	}
	
	private void showClearCacheDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.is_exit);
//		builder.setMessage("共"+cacheSize+"缓存文件");
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Constants.IS_LOGIN = false;
				sharedPreferences.edit()
						.putBoolean(Constants.field_is_login, false).commit();
				ZteApplication.bus.post(BusEvent.EXIT_EVENT);
				finish();
				
			}
		});
		builder.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.show();
	}
}
