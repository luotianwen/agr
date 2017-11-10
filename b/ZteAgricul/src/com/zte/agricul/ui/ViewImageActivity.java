/**
 * 
 */
package com.zte.agricul.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zte.agricul.R;
import com.zte.agricul.adapter.ImagePagerAdapter;

public class ViewImageActivity extends FragmentActivity {

	private ViewImageActivity mContext = this;
	private static final String TAG = "GridViewActivity";
	// private static final String IMAGE_CACHE_DIR = "images";
	public static final String EXTRA_IMAGE = "extra_image";
	public static final String EXTRA_STR = "extra_str";
	// public static final String EXTRA_STRBIG = "extra_bigstr";

	private ImagePagerAdapter mAdapter;

	// private ImageWorker mImageWorker;
	private ViewPager mPager;

	public static ArrayList<String> imageString;
	// public static String[] imageBigString;
	private TextView countTextView;
	private RelativeLayout photo_relativeLayout;
	private ImageView backImageView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_image);
		getWindow().setWindowAnimations(0);
	
		initView();

	}

	private void initView() {
		backImageView = (ImageView) findViewById(R.id.photo_imageview);
		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
		photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
		countTextView = (TextView) findViewById(R.id.photo_count);
		photo_relativeLayout.setBackgroundColor(0x70000000);
		// Fetch screen height and width, to use as our max size when loading
		// images as this
		// activity runs full screen
		// final DisplayMetrics displaymetrics = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		// final int height = displaymetrics.heightPixels;
		// final int width = displaymetrics.widthPixels;

		// ImageCacheParams cacheParams = new ImageCacheParams();
		// cacheParams.reqHeight = height;
		// cacheParams.reqWidth = width;
		// cacheParams.memoryCacheEnabled = false;
		// cacheParams.loadingResId = R.drawable.empty_photo;
		// mImageWorker = ImageWorker.newInstance(this);
		// mImageWorker.addParams(TAG, cacheParams);
		// // mImageWorker.setLoadingImage(R.drawable.empty_photo);
		// // Set up ViewPager and backing adapter

		if (null != getIntent().getStringArrayListExtra(EXTRA_STR)) {
			imageString = getIntent().getStringArrayListExtra(EXTRA_STR);
		}
		// if(null!=getIntent().getStringExtra(EXTRA_STRBIG)){
		// imageBigString = getIntent().getStringExtra(EXTRA_STRBIG).split(",");
		// }

		mAdapter = new ImagePagerAdapter(getSupportFragmentManager(),
				imageString.size());

		mPager = (ViewPager) findViewById(R.id.viewpager);
		mPager.setOffscreenPageLimit(imageString.size());
		mPager.setOnPageChangeListener(pageChangeListener);
		mPager.setAdapter(mAdapter);
		final int extraCurrentItem = getIntent().getIntExtra(EXTRA_IMAGE, -1);
		if (extraCurrentItem != -1) {
			mPager.setCurrentItem(extraCurrentItem);
			countTextView.setText((extraCurrentItem + 1) + "/"
					+ imageString.size());
		}
		mPager.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// countTextView.setText("1/"+imageString.length);

		// Set the current item based on the extra passed in to this activity

	}

	private void initCount(int count) {
		countTextView.setText((count + 1) + "/" + imageString.size());

	}

	private int count;

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {// 页面选择响应函数
			count = arg0;
			initCount(count);

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

		}

		public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

		}
	};

	/**
	 * Called by the ViewPager child fragments to load images via the one
	 * ImageWorker
	 * 
	 * @return
	 */
	// public ImageWorker getImageWorker() {
	// return mImageWorker;
	// }

	public void onResume() {
		super.onResume();
		mAdapter.notifyDataSetChanged();
	}

	public void onPause() {
		super.onPause();
	}
}
