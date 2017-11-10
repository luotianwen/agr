package com.zte.agricul.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zte.agricul.R;
import com.zte.agricul.adapter.AgriculListAdapter;
import com.zte.agricul.app.Constants;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.AgriculListBean;
import com.zte.agricul.bean.AgriculMainInfo;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.Util;

public class SearchListActivity extends BaseActivity implements OnClickListener {
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private TextView leftTv, ivTitleName, rightTv;
	private AgriculListAdapter mAdapter;
	private ListView mlist;
	private Button search_btn;
//	private LoadingDialog dialog;
	public Gson gson;
//	private SearchListBean mBean;
	private TextView wifi;
	private RelativeLayout  progressView;
	private AgriculMainInfo     mMainBean  ;
	private String searchText ;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			progressView.setVisibility(View.GONE);
			switch (msg.what) {
			case REFRESH_TEXT:
				mAdapter = new AgriculListAdapter(getApplicationContext(), mMainBean.getResult().getBaseList(),imageLoader);
				mlist.setAdapter(mAdapter);
				wifi.setVisibility(View.GONE);
				break;
			case NET_ERROR:
				wifi.setVisibility(View.VISIBLE);
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
		setContentView(R.layout.search_list_activity);
//		dialog = new LoadingDialog(this);
		gson = new Gson();
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		wifi = (TextView) findViewById(R.id.net_error);
		wifi.setOnClickListener(this);
		leftTv = (TextView) findViewById(R.id.leftTv);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		rightTv = (TextView) findViewById(R.id.rightTv);
		ivTitleName.setText(R.string.search_result);
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setOnClickListener(this);
		
		progressView =(RelativeLayout) findViewById(R.id.layout_loading);
		progressView.setVisibility(View.INVISIBLE);
		progressView.setOnClickListener(this);
		mlist = (ListView) findViewById(R.id.main_data_list);
		mlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				AgriculListBean  aBeans = (AgriculListBean) parent.getItemAtPosition(position);
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
		switch (v.getId()) {
		case R.id.leftTv:
			finish();
			break;
		case R.id.net_error:
			initData();
			break;
			
		default:
			break;
		}
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
							"0"));
					nameValuePair.add(new BasicNameValuePair("CityID",
							"0"));
					nameValuePair.add(new BasicNameValuePair("CropTypeID",
							"0"));
					nameValuePair.add(new BasicNameValuePair("AnnualOutput",
							"0"));
					nameValuePair.add(new BasicNameValuePair("BaseSize",
							"0"));
					nameValuePair.add(new BasicNameValuePair("Search",
							getIntent().getStringExtra("search")));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.SEARCH_URL, nameValuePair);
					Logger.d("ddd", "sb==" + Constants.SEARCH_URL+nameValuePair.toString());
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mMainBean = gson.fromJson(sb.toString(),
								AgriculMainInfo.class);
						if (mMainBean != null && "0".equals(mMainBean.getStatus())) {
							Message msg = handler.obtainMessage(REFRESH_TEXT);
							handler.sendMessage(msg);
						} else {
							Message msg = handler.obtainMessage(2,
									getResources().getString(R.string.net_data_error));
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
}