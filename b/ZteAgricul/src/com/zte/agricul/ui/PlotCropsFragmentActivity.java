package com.zte.agricul.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zte.agricul.R;
import com.zte.agricul.adapter.MyFragmentPagerAdapter;
import com.zte.agricul.app.Constants;
import com.zte.agricul.bean.PlotBean;
import com.zte.agricul.fragment.PlotCropsChartFragment2;
import com.zte.agricul.fragment.PlotCropsGrowFragment;
import com.zte.agricul.util.Util;
import com.zte.agricul.view.PopupWindowEditPlot;

//生长日志
public class PlotCropsFragmentActivity extends FragmentActivity implements
		OnClickListener {
	private ViewPager mPager;
	private List<Fragment> fragmentsList;
	// private ImageView bottomLineImageView;
	private ImageView musicTextView, radioTextView;
	private int offset = 0;
	private static final int tabCount = 2;
	private static int lineWidth = 0;
	private int currentIndex = 0;
	ImageView[] titles = new ImageView[2];
	private TextView leftTv, ivTitleName;
	private ImageView rightImg;
	private PopupWindowEditPlot mPopupWindow;

	private SharedPreferences sharedPreferences;
	private List<PlotBean> plotList = new ArrayList<PlotBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plot_crops);
		sharedPreferences = getSharedPreferences(Constants.SHARE_PRE_FILE,
				Context.MODE_PRIVATE);
		PlotCropsGrowFragment.cropIndex.clear();
		plotList = (List<PlotBean>) getIntent()
				.getSerializableExtra("plotList");
		initView();
		// initImageView();
		initTextView();
		initViewPager();
	}

	private void initTextView() {
		musicTextView = (ImageView) findViewById(R.id.say_music_new);
		radioTextView = (ImageView) findViewById(R.id.say_music_dj);
		titles[0] = musicTextView;
		titles[1] = radioTextView;
		musicTextView.setOnClickListener(new TextViewOnClickListener(0));
		radioTextView.setOnClickListener(new TextViewOnClickListener(1));

	}

	// private void initImageView() {
	// bottomLineImageView = (ImageView)
	// findViewById(R.id.say_music_bottom_line);
	// // 获取图片宽度
	// lineWidth = bottomLineImageView.getLayoutParams().width;
	// //
	// BitmapFactory.decodeResource(getResources(),R.drawable.line).getWidth();
	// DisplayMetrics dm = new DisplayMetrics();
	// getWindowManager().getDefaultDisplay().getMetrics(dm);
	// // 获取屏幕宽度
	// int screenWidth = dm.widthPixels;
	// // Matrix matrix = new Matrix();
	// offset = (int) ((screenWidth / (float) tabCount - lineWidth) / 2);
	// // matrix.postTranslate(offset, 0);
	// // 设置初始位置
	// // bottomLineImageView.setImageMatrix(matrix);
	// bottomLineImageView.setTranslationX(offset);
	//
	// }

	private void initViewPager() {
		mPager = (ViewPager) findViewById(R.id.say_music_viewPager);
		// mPager.setOffscreenPageLimit(2);
		fragmentsList = new ArrayList<Fragment>();
		fragmentsList.add(new PlotCropsGrowFragment(getIntent().getStringExtra(
				"landName"), getIntent().getStringExtra("baseid"), plotList));
		fragmentsList.add(new PlotCropsChartFragment2(getIntent()
				.getStringExtra("landName"), getIntent().getStringExtra(
				"baseid"), plotList));
		mPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentsList));
		mPager.setCurrentItem(0);
		// musicTextView.setTextColor(getResources().getColor(R.color.green_5e));
		mPager.setOnPageChangeListener(new mOnPageChangeListener());
	}

	private void initView() {
		leftTv = (TextView) findViewById(R.id.leftTv);
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setOnClickListener(this);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		ivTitleName.setText(R.string.plot_main_home);
		rightImg = (ImageView) findViewById(R.id.rightImg);
		rightImg.setVisibility(View.VISIBLE);
		rightImg.setImageResource(R.drawable.add_grow_other_data);
		rightImg.setOnClickListener(this);

		String type = sharedPreferences
				.getString(Constants.USER_PERMISSION, "");
		if (type.contains("4")) {
			rightImg.setVisibility(View.VISIBLE);
		}else {
			rightImg.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent();
		switch (view.getId()) {
		case R.id.leftTv:
			finish();
			break;

		case R.id.rightImg:
			if (null == mPopupWindow) {
				initPop();
			}
			mPopupWindow.showAsDropDown(rightImg);
			break;
		default:
			break;
		}

	}

	private void initPop() {
		mPopupWindow = new PopupWindowEditPlot(PlotCropsFragmentActivity.this,
				mOnClickListener);
	}

	public class mOnPageChangeListener implements OnPageChangeListener {
		int one = offset * 2 + lineWidth;// 页卡1 -> 页卡2 偏移量

		@Override
		public void onPageSelected(int index)// 设置标题的颜色以及下划线的移动效果
		{
			Animation animation = new TranslateAnimation(one * currentIndex,
					one * index, 0, 0);
			animation.setFillAfter(true);
			animation.setDuration(300);
			// titles[currentIndex].setTextColor(getResources().getColor(
			// R.color.grey_set));
			// titles[index].setTextColor(getResources()
			// .getColor(R.color.green_5e));

			if (index == 0) {
				titles[index].setImageResource(R.drawable.grow_log_bg_selected);
				titles[currentIndex]
						.setImageResource(R.drawable.chart_analysis_bg);
			} else {
				titles[index]
						.setImageResource(R.drawable.chart_analysis_bg_selected);
				titles[currentIndex].setImageResource(R.drawable.grow_log_bg);
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

	OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			Intent intent;
			switch (view.getId()) {
			case R.id.pop_add_grow_gongxu:
				intent = new Intent(getApplicationContext(),
						AddGrowProcessActivity.class);
				intent.putStringArrayListExtra("landList",
						PlotCropsGrowFragment.landList);
				intent.putExtra("landid", getIntent().getStringExtra("baseid"));
				intent.putExtra("landName",
						getIntent().getStringExtra("landName"));

				startActivity(intent);
				break;
			case R.id.pop_add_grow_data:
				if (null == PlotCropsGrowFragment.cropIndex
						|| PlotCropsGrowFragment.cropIndex.size() == 0) {
					Util.showToast(getApplicationContext(), getResources().getString(R.string.net_data_error),
							Toast.LENGTH_SHORT);
					return;
				}

				intent = new Intent(getApplicationContext(),
						AddGrowDataActivity2.class);
				intent.putExtra("landid", getIntent().getStringExtra("baseid"));
				intent.putExtra("cropTypeId",
						getIntent().getStringExtra("cropTypeId"));
				intent.putExtra("landName",
						getIntent().getStringExtra("landName"));
				startActivity(intent);
				break;
			case R.id.pop_add_other_data:
				intent = new Intent(getApplicationContext(),
						AddWeatherDataActivity.class);
				intent.putStringArrayListExtra("landList",
						PlotCropsGrowFragment.landList);
				intent.putExtra("landid", getIntent().getStringExtra("baseid"));
				intent.putExtra("landName",
						getIntent().getStringExtra("landName"));
				startActivity(intent);
				break;
			default:
				break;
			}

			if (mPopupWindow != null && mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
		}
	};
}
