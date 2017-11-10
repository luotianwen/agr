package com.zte.agricul.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zte.agricul.zh.R;
import com.zte.agricul.adapter.MyFragmentPagerAdapter;
import com.zte.agricul.app.Constants;
import com.zte.agricul.fragment.PlotInfoFragment;
import com.zte.agricul.fragment.PlotListFragment;

/***
 * 地块首页
 * @author lxl
 *
 */
public class PlotMainFragmentActivity extends FragmentActivity implements OnClickListener{
	private ViewPager mPager;
	private List<Fragment> fragmentsList;
//	private ImageView bottomLineImageView;
	private TextView  musicTextView, radioTextView;
	private int offset = 0;
	private static final int tabCount = 2;
	private static int lineWidth = 0;
	private int currentIndex = 0;
	TextView[] titles = new TextView[2];
	private TextView leftTv, ivTitleName, rightTv;
	public static String landName = "";
	private String id="";
	private SharedPreferences sharedPreferences;
	String type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plot_main);
		sharedPreferences = getSharedPreferences(Constants.SHARE_PRE_FILE,
				Context.MODE_PRIVATE);
		id = getIntent().getStringExtra("id");
		landName = getIntent().getStringExtra("landName");
		initView();
//		initImageView();
		initTextView();
		initViewPager(id);
	
	}

	private void initTextView() {
		musicTextView = (TextView) findViewById(R.id.say_music_new);
		radioTextView = (TextView) findViewById(R.id.say_music_dj);
		titles[0] = musicTextView;
		titles[1] = radioTextView;
		musicTextView.setOnClickListener(new TextViewOnClickListener(0));
		radioTextView.setOnClickListener(new TextViewOnClickListener(1));

	}

//	private void initImageView() {
//		bottomLineImageView = (ImageView) findViewById(R.id.say_music_bottom_line);
//		// 获取图片宽度
//		lineWidth = bottomLineImageView.getLayoutParams().width;
//		// BitmapFactory.decodeResource(getResources(),R.drawable.line).getWidth();
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		// 获取屏幕宽度
//		int screenWidth = dm.widthPixels;
////		Matrix matrix = new Matrix();
//		offset = (int) ((screenWidth / (float) tabCount - lineWidth) / 2);
////		matrix.postTranslate(offset, 0);
//		// 设置初始位置
////		bottomLineImageView.setImageMatrix(matrix);
//		bottomLineImageView.setTranslationX(offset);
//		
//	}

	private void initViewPager(String id) {
		mPager = (ViewPager) findViewById(R.id.say_music_viewPager);
//		mPager.setOffscreenPageLimit(2);
		fragmentsList = new ArrayList<Fragment>();
		fragmentsList.add(new PlotListFragment(id,landName));
		fragmentsList.add(new PlotInfoFragment(id));
		mPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(),  fragmentsList));
		mPager.setCurrentItem(0);
		musicTextView.setTextColor(getResources().getColor(R.color.green_5e));
		mPager.setOnPageChangeListener(new mOnPageChangeListener());
	}

	private void initView() {
		leftTv = (TextView) findViewById(R.id.leftTv);
		leftTv.setOnClickListener(this);
		leftTv.setVisibility(View.VISIBLE);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		ivTitleName.setText(R.string.plot_main_home);
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);
		rightTv.setText(getResources().getString(R.string.management_plot));
		rightTv.setOnClickListener(this);
		
		type = sharedPreferences.getString(Constants.USER_PERMISSION, "");
		if (type.contains("2")) {
			rightTv.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent();
		switch (view.getId()) {
		case R.id.leftTv:
			finish();
			break;
		case R.id.rightTv:
			intent  =new Intent(getApplicationContext(),PlotAllListActivity.class);
			intent.putExtra("id", id);
			intent.putExtra("landName", landName);
			startActivity(intent);
			break;

			
		default:
			break;
		}

	}

	public class mOnPageChangeListener implements OnPageChangeListener {
		int one = offset * 2 + lineWidth;// 页卡1 -> 页卡2 偏移量

		@Override
		public void onPageSelected(int index)// 设置标题的颜色以及下划线的移动效果
		{
			
			
			if (type.contains("2")) {
				if (index ==0 ) {
					rightTv.setVisibility(View.VISIBLE);
				}else {
					rightTv.setVisibility(View.GONE);
				}
			}else {
				rightTv.setVisibility(View.GONE);
			}
			
			
			Animation animation = new TranslateAnimation(one * currentIndex,
					one * index, 0, 0);
			animation.setFillAfter(true);
			animation.setDuration(300);
			titles[currentIndex].setTextColor(getResources().getColor(
					R.color.grey_set));
			titles[index].setTextColor(getResources().getColor(R.color.green_5e));
			
			if (index==0) {
				Resources res = getResources();
				Drawable myImage = res.getDrawable(R.drawable.plot_main_home_pressed);
				titles[index].setCompoundDrawablesWithIntrinsicBounds(null,
						myImage, null, null);
				myImage = res.getDrawable(R.drawable.plot_main_info);
				titles[currentIndex].setCompoundDrawablesWithIntrinsicBounds(null,
						myImage, null, null);
			}else {
				Resources res = getResources();
				Drawable myImage = res.getDrawable(R.drawable.plot_main_info_pressed);
				titles[index].setCompoundDrawablesWithIntrinsicBounds(null,
						myImage, null, null);
				myImage = res.getDrawable(R.drawable.plot_main_home);
				titles[currentIndex].setCompoundDrawablesWithIntrinsicBounds(null,
						myImage, null, null);
			}
			currentIndex = index;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}
	}

	public class TextViewOnClickListener implements View.OnClickListener {
		private int index = 0;

		public TextViewOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};
}
