package com.zte.agricul.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zte.agricul.R;
import com.zte.agricul.app.ZteApplication;

public class BaseActivity extends Activity {


	public ImageLoader imageLoader;
//	public SharePrefrenceUtils mSharePrefrenceUtils;
	// public LoadDataFromNet loadDataFromNet;
	// public BitmapUtils bitmapUtils;
	public ZteApplication mApplication;
	public Gson gson;
	public Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mApplication = (ZteApplication) getApplication();
		mContext = this;
		// loadDataFromNet = LoadDataFromNet.getInstance();
		// bitmapUtils = mApplication.getBitmapUtils();

//		if (!mApplication.isNetworkConnected()) {
//			showErr();
//		}
//		mSharePrefrenceUtils = SharePrefrenceUtils.getInstance(this);


		imageLoader = ImageLoader.getInstance();
		gson = new Gson();
	}

	/**
	 * 淡入淡出界面
	 * 
	 * @param intent
	 */
	public void startFadeActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

//	/**
//	 * finish activity 任务，右侧滑出，左侧滑进
//	 */
//	public void finishWithSlide() {
//		super.finish();
//		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
//	}
//
//	/**
//	 * 从左滑入进入界面
//	 * 
//	 * @param intent
//	 */
//	public void startSlideActivity(Intent intent) {
//		super.startActivity(intent);
//		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//	}

//	@Override
//	public void startActivityForResult(Intent intent, int requestCode) {
//		super.startActivityForResult(intent, requestCode);
//		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//	}

//	@Override
//	public void onResume() {
//		super.onResume();
//		MobclickAgent.onResume(this);
//		JPushInterface.onResume(this);
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		MobclickAgent.onPause(this);
//		JPushInterface.onPause(this);
//	}
//
//	/**
//	 * 网络未连接
//	 */
//	public void showErr() {
//		ToastUtils.showShort(this, R.string.net_error);
//	}
//	/**
//	 * 服务器返回数据有问题
//	 */
//	public void showJsonErr() {
//		ToastUtils.showShort(this, R.string.click_err);
//	}

//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		super.onBackPressed();
//		finishWithSlide();
//	}
}
