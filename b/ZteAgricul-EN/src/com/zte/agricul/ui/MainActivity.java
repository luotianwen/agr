package com.zte.agricul.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.mapcore2d.el;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.zte.agricul.zh.R;
import com.zte.agricul.adapter.AgriculListAdapter;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.AgriculListBean;
import com.zte.agricul.bean.AgriculMainInfo;
import com.zte.agricul.bean.AgriculMainPopInfo;
import com.zte.agricul.bean.CityBean;
import com.zte.agricul.bean.CropTypeBean;
import com.zte.agricul.bean.MainPopBean;
import com.zte.agricul.bean.ProvinceBean;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.Util;
import com.zte.agricul.view.ListViewToScrollView;
import com.zte.agricul.view.PopupWindowCityList;
import com.zte.agricul.view.PopupWindowCropsList;

public class MainActivity extends BaseActivity implements OnClickListener{
	private final int REFRESH_MAIN_TEXT = 1;
	private final int REFRESH_HEADER_TEXT = 2;
	private final int NET_ERROR = 3;
	private Context mContext;
	private TextView leftTv, ivTitleName, rightSearch,rightTv;
	private TextView wifi;
	public Gson gson;
	private SharedPreferences sharedPreferences;
	private TextView txt_unit1,txt_unit2,right3, header_t1, header_t2, header_t3, header_t4;
	private ListViewToScrollView mlist;
	private AgriculListAdapter mAdapter ;
	private RelativeLayout  progressView;
	private TextView options1,options2,options3,options4;	
	private ScrollView mScrollView;
	
	private List<List<CityBean >>  areaDatas ;
	private List<MainPopBean>  popStringList;
	private PopupWindowCityList mPop;
	private PopupWindowCropsList mPopCrop;
	private AgriculMainPopInfo     mBean  ;
	private AgriculMainInfo     mMainBean  ;
	private String ProID = "0";
	private String CityID = "0";
	private String CropTypeID = "0";
	private String AnnualOutput = "0";
	private String BaseSize = "0";
	
	
	
	private LinearLayout headerLin ;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case REFRESH_MAIN_TEXT:
				progressView.setVisibility(View.GONE);
				mAdapter = new AgriculListAdapter(getApplicationContext(), mMainBean.getResult().getBaseList(),imageLoader);
				mlist.setAdapter(mAdapter);
				wifi.setVisibility(View.GONE);
				headerLin.setVisibility(View.VISIBLE);
				break;
			case REFRESH_HEADER_TEXT:
//				header_t1.setText(mBean.getResult().getLand_Count());
				setlandData(header_t1, mBean.getResult().getLand_Count(), getResources().getString(R.string.mu));
				setlandData(header_t2, mBean.getResult().getCrops_Count(), getResources().getString(R.string.varieties_of));
				setlandData(header_t3, mBean.getResult().getAnnual_Yield(), getResources().getString(R.string.tun));
//				header_t2.setText(mBean.getResult().getCrops_Count());
//				header_t3.setText(mBean.getResult().getAnnual_Yield());
				break;
			case NET_ERROR:
				progressView.setVisibility(View.GONE);
				wifi.setVisibility(View.VISIBLE);
				Util.showToast(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT);
				break;

			}

		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = MainActivity.this;
		ZteApplication.bus.register(this);
		sharedPreferences = getSharedPreferences(Constants.SHARE_PRE_FILE,
				Context.MODE_PRIVATE);
		gson = new Gson();
		initView();
		initData();
		initPopData();
	}
	/***
	 * userid=2&ProID=0&CityID=0&CropTypeID=1&AnnualOutput=0&BaseSize=0
	 */
	private void initData() {
		// TODO Auto-generated method stub
		progressView.setVisibility(View.VISIBLE);
		wifi.setVisibility(View.GONE);
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair("userid",
							Constants.uid));
					nameValuePair.add(new BasicNameValuePair("ProID",
							ProID));
					nameValuePair.add(new BasicNameValuePair("CityID",
							CityID));
					nameValuePair.add(new BasicNameValuePair("CropTypeID",
							CropTypeID));
					nameValuePair.add(new BasicNameValuePair("AnnualOutput",
							AnnualOutput));
					nameValuePair.add(new BasicNameValuePair("BaseSize",
							BaseSize));
					
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.MAIN_URL, nameValuePair);
					Logger.d("ddd", "sb==" + Constants.MAIN_URL+nameValuePair.toString());
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mMainBean = gson.fromJson(sb.toString(),
								AgriculMainInfo.class);
						if (mMainBean != null && "0".equals(mMainBean.getStatus())) {
							Message msg = handler.obtainMessage(REFRESH_MAIN_TEXT);
							handler.sendMessage(msg);
						} else {
							Message msg = handler.obtainMessage(NET_ERROR,
									getResources().getString(R.string.net_data_error));
							handler.sendMessage(msg);
						}
					} else {
						Logger.d("ddd", "sb==null");

						Message msg = handler.obtainMessage(NET_ERROR, getResources().getString(R.string.net_data_error));
						handler.sendMessage(msg);
					}
				} else {
					Message msg = handler.obtainMessage(NET_ERROR, getResources().getString(R.string.net_data_error));
					handler.sendMessage(msg);
				}
			};
		}.start();
	
		
	}
	private void initPopData() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair("Userid",
							Constants.uid));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.MAIN_HEADER_URL, nameValuePair);
					Logger.d("ddd", Constants.MAIN_HEADER_URL + nameValuePair.toString());
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mBean = gson.fromJson(sb.toString(),
								AgriculMainPopInfo.class);
						if (mBean != null && "0".equals(mBean.getStatus())) {
							Message msg = handler.obtainMessage(REFRESH_HEADER_TEXT);
							handler.sendMessage(msg);
						} 
//						else {
//							Message msg = handler.obtainMessage(NET_ERROR,
//									getResources().getString(R.string.net_data_error));
//							handler.sendMessage(msg);
//						}
					} else {
						Logger.d("ddd", "sb==null");

//						Message msg = handler.obtainMessage(NET_ERROR, getResources().getString(R.string.net_data_error));
//						handler.sendMessage(msg);
					}
				} else {
//					Message msg = handler.obtainMessage(NET_ERROR, getResources().getString(R.string.net_data_error));
//					handler.sendMessage(msg);
				}
			};
		}.start();
	}
	//地区弹窗
	private void initPopWindow() {
		// TODO Auto-generated method stub
		getCityData(mBean.getResult().getProvince(),mBean.getResult().getCity());
		mPop = new PopupWindowCityList(MainActivity.this,mBean.getResult().getProvince(),areaDatas,options1.getText().toString());
	}
	//种类、产量、面积弹窗
	private void initCropsPopWindow(String selectName,int type) {
		// TODO Auto-generated method stub
		if (type==1) {
			mPopCrop = new PopupWindowCropsList(MainActivity.this,mBean.getResult().getCropType(),type,selectName);
		}else if(type==2) {
			mPopCrop = new PopupWindowCropsList(MainActivity.this,mBean.getResult().getAnnualOutput(),type,selectName);
		}else {
			mPopCrop = new PopupWindowCropsList(MainActivity.this,mBean.getResult().getBaseSize(),type,selectName);
		}
		
	}

	private void getCityData(List<ProvinceBean >  proBean ,List<CityBean >  cityBean ) {
		// TODO Auto-generated method stub
		List<CityBean> aa = null  ;
		areaDatas = new ArrayList<List<CityBean>>();
		for (int i = 0; i < proBean.size(); i++) {
			aa = new ArrayList<CityBean>();
			for (int j = 0; j < cityBean.size(); j++) {
				if (cityBean.get(j).getProID().equals(proBean.get(i).getProID())) {
					aa.add(cityBean.get(j));
					System.out.println(j+"==j=="+cityBean.get(j).getName());
				}
			}
			areaDatas.add(aa);
		}
	}
	private void initView() {
		// TODO Auto-generated method stub
		wifi = (TextView) findViewById(R.id.net_error);
		wifi.setOnClickListener(this);
		leftTv = (TextView) findViewById(R.id.leftTv);
		leftTv.requestFocus();
		leftTv.setOnClickListener(this);
		rightSearch = (TextView) findViewById(R.id.right_search);
		rightSearch.setOnClickListener(this);
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setOnClickListener(this);
		rightTv.setText(getResources().getString(R.string.base_management));
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);
		String type = sharedPreferences.getString(Constants.USER_PERMISSION, "");
		System.out.println("type==="+type+"---------"+type.contains("1"));
		if (type.contains("1")) {
			rightTv.setVisibility(View.VISIBLE);
		}else {
			rightTv.setVisibility(View.GONE);
		}
		
		mlist = (ListViewToScrollView) findViewById(R.id.main_data_list);
		progressView =(RelativeLayout) findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);
//		txt_unit1 = (TextView) findViewById(R.id.txt_unit);
//		right3= (TextView) findViewById(R.id.right3);
		header_t1 = (TextView) findViewById(R.id.header_t1);
		header_t2 = (TextView) findViewById(R.id.header_t2);
		header_t3 = (TextView) findViewById(R.id.header_t3);
		
		headerLin =(LinearLayout) findViewById(R.id.layout_main_header);
		headerLin.setVisibility(View.GONE);
		mScrollView =(ScrollView) findViewById(R.id.main_scroll);
		options1 = (TextView) findViewById(R.id.main_select_options1);
		options2 = (TextView) findViewById(R.id.main_select_options2);
		options3 = (TextView) findViewById(R.id.main_select_options3);
		options4 = (TextView) findViewById(R.id.main_select_options4);
		options1.setOnClickListener(this);
		options2.setOnClickListener(this);
		options3.setOnClickListener(this);
		options4.setOnClickListener(this);
		
		mlist.setFocusable(false);
		progressView.setVisibility(View.GONE);
		
		mlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				AgriculListBean  aBeans = (AgriculListBean) arg0.getItemAtPosition(arg2);
				Intent intent = new Intent(getApplicationContext(),PlotMainFragmentActivity.class);
				
				intent.putExtra("id", aBeans.getID());
				intent.putExtra("landName", aBeans.getName());
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.layout_loading:
			System.out.println("111111111111");
			break;
		case R.id.leftTv:
			intent = new Intent(getApplicationContext(), UserInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.right_search:
			intent = new Intent(getApplicationContext(), SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.rightTv:
			intent = new Intent(getApplicationContext(), ManageMainListActivity.class);
			startActivity(intent);
			break;	
		case R.id.net_error:
			initData();
			break;
		case R.id.main_select_options1:
			mScrollView.scrollTo(mScrollView.getScrollY(), 570);
			initPopWindow();
			if (mPop!=null) {
				mPop.showAsDropDown(options1);
			}
			break;
		case R.id.main_select_options2:
			mScrollView.scrollTo(mScrollView.getScrollY(), 570);
			initCropsPopWindow(options2.getText().toString(), 1);
			if (mPopCrop!=null) {
				mPopCrop.showAsDropDown(options1);
			}
			break;
		case R.id.main_select_options3:
			mScrollView.scrollTo(mScrollView.getScrollY(), 570);
			initCropsPopWindow(options3.getText().toString(), 2);
			if (mPopCrop!=null) {
				mPopCrop.showAsDropDown(options1);
			}
			break;
		case R.id.main_select_options4:
			mScrollView.scrollTo(mScrollView.getScrollY(), 570);
			initCropsPopWindow(options4.getText().toString(), 3);
			if (mPopCrop!=null) {
				mPopCrop.showAsDropDown(options1);
			}
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
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
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
	
	// View宽，高
	public int getLocation(View v) {
	    int[] loc = new int[4];
	    int[] location = new int[2];
	    v.getLocationOnScreen(location);
	    loc[0] = location[0];
	    loc[1] = location[1];
	    int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
	    int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
	    v.measure(w, h);

	    loc[2] = v.getMeasuredWidth();
	    loc[3] = v.getMeasuredHeight();

	    //base = computeWH();
	    return loc[3];
	}
	
	
	private   void   setlandData(TextView  textView , String  count,String unit){
		if (null==count||"".equals(count)) {
			count = "0";
		}
		
		 final SpannableStringBuilder sb = new SpannableStringBuilder(count+unit);   
		 System.out.println(count+unit);
		 ColorStateList redColors = ColorStateList.valueOf(getResources().getColor(R.color.green));  
		 ColorStateList redColors2 = ColorStateList.valueOf(getResources().getColor(R.color.black_20));  
		 final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(158, 158, 158)); // Span to set text color to some RGB value   
		 final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold   
		 sb.setSpan(new TextAppearanceSpan(this, R.style.style0), 0, count.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); // Set the text color for first 4 characters   
		 sb.setSpan(new TextAppearanceSpan(this, R.style.style1), count.length(), sb.toString().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make them also bold   
		 textView.setText(sb); 
	}
	
	
	@Subscribe
	public void onBusEvent(BusEvent event) {
		if (event == BusEvent.AREA_SELECT_EVENT) {
			CityBean  mdata=(CityBean) BusEvent.AREA_SELECT_EVENT.data;
			if (!mdata.getName().equals(options1.getText().toString())) {
				options1.setText(mdata.getName());
				ProID =mdata.getProID();
				CityID =mdata.getID();
				initData();
			}
			
			if (mPop!=null) {
				mPop.dismiss() ;
			}
			// System.out.println(addressBean.getId());
		}else if (event == BusEvent.CROPS_SELECT_EVENT) {
			
			

			CropTypeBean  mdata=(CropTypeBean) BusEvent.CROPS_SELECT_EVENT.data;
			if (!mdata.getName().equals(options2.getText().toString())) {
				options2.setText(mdata.getName());
				CropTypeID = mdata.getID();
				initData();
			}
			
			if (mPopCrop!=null) {
				mPopCrop.dismiss() ;
			}
			// System.out.println(addressBean.getId());
		
		}else if (event == BusEvent.OUTPUT_SELECT_EVENT) {
			
			

			CropTypeBean  mdata=(CropTypeBean) BusEvent.OUTPUT_SELECT_EVENT.data;
			if (!mdata.getSize().equals(options3.getText().toString())) {
				options3.setText(mdata.getSize());
				AnnualOutput = mdata.getSize();
				initData();
			}
			
			if (mPopCrop!=null) {
				mPopCrop.dismiss() ;
			}
			// System.out.println(addressBean.getId());
		
		}else if (event == BusEvent.SIZE_SELECT_EVENT) {
			
			

			CropTypeBean  mdata=(CropTypeBean) BusEvent.SIZE_SELECT_EVENT.data;
			if (!mdata.getSize().equals(options4.getText().toString())) {
				options4.setText(mdata.getSize());
				BaseSize = mdata.getSize();
				initData();
			}
			
			if (mPopCrop!=null) {
				mPopCrop.dismiss() ;
			}
			// System.out.println(addressBean.getId());
		
		}else if (event == BusEvent.REFRESH_DELETE_BASE_LAND_EVENT||event == BusEvent.REFRESH_ADD_BASE_LAND_EVENT) {
			
			initData();
			// System.out.println(addressBean.getId());
		
		}else if (event ==BusEvent.EXIT_EVENT) {
			Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
			startActivity(intent);
			finish();
//			System.exit(0);
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ZteApplication.bus.unregister(this);
	}
	/**
	 * 连续按两次返回键就退出
	 */
	private long firstTime;

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - firstTime < 3000) {
			finish();
		} else {
			firstTime = System.currentTimeMillis();
			Util.showToast(getApplicationContext(), getResources().getString(R.string.exit_press_again),
					Toast.LENGTH_SHORT);
		}
	}
}
