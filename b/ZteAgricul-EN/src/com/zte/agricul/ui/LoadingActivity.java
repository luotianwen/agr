package com.zte.agricul.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zte.agricul.zh.R;
import com.zte.agricul.app.Constants;


public class LoadingActivity extends Activity{
	private ImageView imageView;
	private Animation welcomeAnimation;
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_activity);
//		Constant.isLogin=false ;
		sharedPreferences = getSharedPreferences(Constants.SHARE_PRE_FILE,
				Context.MODE_PRIVATE);
		if (sharedPreferences.getBoolean(Constants.field_is_login, false)) {
			Constants.IS_LOGIN = true;
			Constants.uid = sharedPreferences.getString(Constants.USER_ID, "");
			Constants.userName = sharedPreferences.getString(Constants.USER_NAME, "");
		}
		imageView = (ImageView) findViewById(R.id.splash_ImageView);
		welcomeAnimation = AnimationUtils.loadAnimation(this,
				R.drawable.splash_style);
		imageView.startAnimation(welcomeAnimation);
		welcomeAnimation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				// Tip:动画结束时,利用Intent跳转到下个Activity
				if (Constants.IS_LOGIN ) {
					Intent intent = new Intent(LoadingActivity.this,
							MainActivity.class);
					startActivity(intent);
				}else {
					Intent intent = new Intent(LoadingActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
				finish();
			}
		});
	}
}
