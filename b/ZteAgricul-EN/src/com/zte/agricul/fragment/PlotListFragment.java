package com.zte.agricul.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.otto.Subscribe;
import com.zte.agricul.zh.R;
import com.zte.agricul.adapter.ConcertPagerAdapter;
import com.zte.agricul.adapter.PlotMainListAdapter;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.bean.PlotBaseInfo;
import com.zte.agricul.bean.PlotBean;
import com.zte.agricul.bean.PlotListInfo;
import com.zte.agricul.ui.PlotCropsFragmentActivity;
import com.zte.agricul.ui.PlotMainFragmentActivity;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.Options;
import com.zte.agricul.util.Util;

public class PlotListFragment extends Fragment implements OnClickListener{
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private View view;
	private ListView mList ;
	
	private View headerView;
	private ViewPager viewPager;
	private LinearLayout llayout;
	private List<ImageView> list;
	private List<String> adsList = new ArrayList<String>();
	private int prePosition = 0;
	private boolean isSwitch = true;
	private boolean isMoveWhenSleep = true;
	private static int TIME = 4000;
	private int curPosition = 0;
	private ConcertPagerAdapter pagerAdapter;
	
	public ImageLoader imageLoader;
	public Gson gson;
	
	private PlotMainListAdapter mAdapter ;
	private String id ="";
	private TextView net_error ;
	private TextView header_land_name ;
	private RelativeLayout  progressView;
	private PlotListInfo mBean ;
	private boolean isFinish = false ;
	
	private List<PlotBean >  plotList = new ArrayList<PlotBean>();
	private List<PlotBean >  plotSelectedList = new ArrayList<PlotBean>();
	private String landName ;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (isFinish) {
				return ;
			}
			switch (msg.what) {
			case REFRESH_TEXT:
				progressView.setVisibility(View.GONE);
				adsList.addAll(mBean.getResult().getImageList());
			
				mAdapter = new PlotMainListAdapter(getActivity(),mBean.getResult().getBaseList(),imageLoader,landName);
				mList.setAdapter(mAdapter);
				for (int i = 0; i < mBean.getResult().getBaseList().size(); i++) {
					plotList.add(mBean.getResult().getBaseList().get(i));
				}
				net_error.setVisibility(View.GONE);
				initViewPager();
				break;
			case NET_ERROR:
				progressView.setVisibility(View.GONE);
				net_error.setVisibility(View.VISIBLE);
				Util.showToast(getActivity(), (String) msg.obj,
						Toast.LENGTH_SHORT);
				break;
			default:
				if (isSwitch) {
					if (viewPager.getAdapter() != null
							&& viewPager.getAdapter().getCount() != 0) {
						if (curPosition == viewPager.getAdapter().getCount()) {
							curPosition = 0;
						}
						viewPager.setCurrentItem(curPosition);// 切换当前显示的图片
					}
				}
				break;
			}
//			listView.onRefreshComplete();
		}

	};
	
	public PlotListFragment(String id,String landName) {
		// TODO Auto-generated constructor stub
		this.id = id ;
		this.landName=landName;
	}
	public void onCreate(Bundle savedinstancestate) {
		super.onCreate(savedinstancestate);
		imageLoader = ImageLoader.getInstance();
		new Thread(new ScrollTask()).start();
		
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_plot_list, container,
				false);
		ZteApplication.bus.register(this);
		gson = new Gson();
		initView(view,inflater);
		initData();
		
		// viewPager监听器
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				llayout.getChildAt(position).setBackgroundResource(
						R.drawable.icon_normal);
				llayout.getChildAt(prePosition).setBackgroundResource(
						R.drawable.icon_check);

				prePosition = position;

				curPosition = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		viewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					break;
				case MotionEvent.ACTION_MOVE:
					isSwitch = false;
					isMoveWhenSleep = true;
					break;
				case MotionEvent.ACTION_UP:
					isSwitch = true;
					break;

				default:
					isSwitch = true;
					break;
				}
				return false;
			}
		});
		return view;
	}
	private void initView(View view,LayoutInflater inflater) {
		// TODO Auto-generated method stub
		net_error =(TextView) view.findViewById(R.id.net_error);
		net_error.setOnClickListener(this);	
		progressView =(RelativeLayout) view.findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);
		
		
		mList = (ListView) view.findViewById(R.id.main_data_list);
		headerView = inflater.inflate(R.layout.header_plot_main, null);
		header_land_name=(TextView) headerView.findViewById(R.id.header_land_name);
		header_land_name.setText(PlotMainFragmentActivity.landName);
		mList.addHeaderView(headerView);
		headerView.setVisibility(View.VISIBLE);
		viewPager = (ViewPager) headerView.findViewById(R.id.show_pager);
		llayout = (LinearLayout) headerView.findViewById(R.id.show_layout);
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				
				PlotBean mBean = (PlotBean) parent.getItemAtPosition(position);
				plotSelectedList = new ArrayList<PlotBean>();
				for (int i = 0; i < plotList.size(); i++) {
					System.out.println(plotList.get(i).getCrop_Type_ID());
					System.out.println(mBean.getCrop_Type_ID());
					if (plotList.get(i).getCrop_Type_ID().equals(mBean.getCrop_Type_ID())) {
						plotSelectedList.add(plotList.get(i));
					}
				}
				Intent intent = new Intent(getActivity(),PlotCropsFragmentActivity.class);
				intent.putExtra("baseid", mBean.getID());
				intent.putExtra("landName", mBean.getName());
				intent.putExtra("cropTypeId", mBean.getCrop_Type_ID());
				intent.putExtra("plotList", (Serializable)plotSelectedList);
				startActivity(intent);
			}
		});
	}
	
	
	
	/**
	 * 初始化ViewPage
	 * 
	 */
	@SuppressLint("NewApi")
	private void initViewPager() {

		if (adsList.size()<=0) {
			return ;
		}
		list = new ArrayList<ImageView>();
		// if (isRefresh) {
		llayout.removeAllViews();
		// }
		System.out.println(adsList.size());
		for (int i = 0; i < adsList.size(); i++) {
			// for (int i = 0; i < topList.size(); i++) {
			ImageView imageView = new ImageView(getActivity());

			imageLoader.displayImage( adsList.get(i).toString(), imageView,
					Options.bulidOptions(R.drawable.bg_shuffling));

			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setOnClickListener(this);
			list.add(imageView);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
//					Intent adIntent = AdUtils.StartPage(
//							adsList.get(prePosition), mActivity);
//					startSlideActivity(adIntent);
				}
			});

			View view = new View(getActivity());
			view.setBackgroundResource(R.drawable.icon_check);
			if (i == 0) {
				view.setBackgroundResource(R.drawable.icon_normal);
			}
			LayoutParams params = new LayoutParams(25, 10);
			params.leftMargin = 40;

			view.setLayoutParams(params);

			llayout.addView(view);

		}
		pagerAdapter = new ConcertPagerAdapter(list);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOffscreenPageLimit(1);
		// if (topList.size() > 0) {

		llayout.getChildAt(0).setBackgroundResource(R.drawable.icon_normal);
		prePosition = 0;
		// changeTopConcertContent(0);
		// }

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.net_error:
			initData();
			break;

		default:
			break;
		}
	}
	
	private class ScrollTask implements Runnable {

		@Override
		public void run() {
			while (true) {
				if (isSwitch) {
					try {
						isMoveWhenSleep = false;
						Thread.sleep(TIME);
						// 睡眠过程中有滑动就不切换了
						if (isMoveWhenSleep) {
							continue;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					curPosition++;
					mHandler.obtainMessage().sendToTarget();
				}
			}
		}
	}
	
	
	
	private void initData() {
		// TODO Auto-generated method stub
		progressView.setVisibility(View.VISIBLE);
		net_error.setVisibility(View.GONE);
		new Thread() {
			public void run() {
				if (NetworkUtil.isNetworkAvailable(getActivity())) {
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair("userid",
							Constants.uid));
					
					nameValuePair.add(new BasicNameValuePair("baseid",
							id));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.PLOT_LAND_LIST_URL, nameValuePair);
					Logger.d("ddd", Constants.PLOT_LAND_LIST_URL+ nameValuePair.toString());
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mBean = gson.fromJson(sb.toString(),
								PlotListInfo.class);
						if (mBean != null&& "0".equals(mBean.getStatus())) {
							Message msg = mHandler.obtainMessage(REFRESH_TEXT);
							mHandler.sendMessage(msg);
						} else {
							Message msg = mHandler.obtainMessage(2,
									getResources().getString(R.string.net_data_error));
							mHandler.sendMessage(msg);
						}
					} else {
						Logger.d("ddd", "sb==null");

						Message msg = mHandler.obtainMessage(2, getResources().getString(R.string.net_data_error));
						mHandler.sendMessage(msg);
					}
				} else {
					Message msg = mHandler.obtainMessage(2, getResources().getString(R.string.net_data_error));
					mHandler.sendMessage(msg);
				}
			};
		}.start();
	}
	
	@Subscribe
	public void onBusEvent(BusEvent event) {
		if (event == BusEvent.REFRESH_PLOT_EVENT||event ==BusEvent.REFRESH_DELETE_PLOT_EVENT) {
			initData();
		}
		
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		isFinish =true;
		ZteApplication.bus.unregister(this);
	}
}
