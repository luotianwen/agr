package com.zte.agricul.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.squareup.otto.Subscribe;
import com.zte.agricul.zh.R;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.ManagerGrowthBean;
import com.zte.agricul.bean.ManagerGrowthInfo;
import com.zte.agricul.bean.UploadResultBean;
import com.zte.agricul.ui.ManagerGrowthActivity.ManagerGrowthAdapter;
import com.zte.agricul.ui.ManagerGrowthActivity.ManagerGrowthAdapter.lvButtonListener;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.Util;
import com.zte.agricul.view.DeleteListView;

public class ManagerWeatherActivity extends BaseActivity implements
		OnClickListener {
	private TextView leftTv, ivTitleName, rightTv;
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private DeleteListView mList;

	private ManagerGrowthAdapter mAdapter;
	private TextView net_error;
	private RelativeLayout progressView;
	private ManagerGrowthInfo mBean;
	private List<ManagerGrowthBean> growthListBean;
	private UploadResultBean deleteBean;
	private boolean isFinish = false;
	private String landid = "";
	private LinearLayout layout_header;
	public static boolean IS_REFUSH =false ;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (isFinish) {
				return;
			}
			progressView.setVisibility(View.GONE);
			switch (msg.what) {
			case REFRESH_TEXT:
				growthListBean = mBean.getResult();
				if (growthListBean.size() > 0) {
					layout_header.setVisibility(View.VISIBLE);
					mAdapter = new ManagerGrowthAdapter(getApplicationContext());
					mList.setAdapter(mAdapter);
				}
				net_error.setVisibility(View.GONE);
				break;
			case NET_ERROR:
				net_error.setVisibility(View.VISIBLE);
				Util.showToast(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT);
				break;
			case 3:
				net_error.setVisibility(View.GONE);
				growthListBean.remove(msg.arg1);
				mAdapter.notifyDataSetChanged();
				mList.turnToNormal();
//				ZteApplication.bus.post(BusEvent.REFRESH_CHART_DATA_EVENT);
				IS_REFUSH = true ;
				Util.showToast(getApplicationContext(), getResources().getString(R.string.delete_success),
						Toast.LENGTH_SHORT);
				break;
			case 4:
				net_error.setVisibility(View.GONE);
				Util.showToast(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT);
				break;

			}
			// listView.onRefreshComplete();
		}

	};

	public void onCreate(Bundle savedinstancestate) {
		super.onCreate(savedinstancestate);
		setContentView(R.layout.activity_manager_weather_list);
		ZteApplication.bus.register(this);
		landid = getIntent().getStringExtra("landid");
		initView();
		initData();
	};

	private void initView() {
		// TODO Auto-generated method stub
		leftTv = (TextView) findViewById(R.id.leftTv);
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setOnClickListener(this);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		ivTitleName.setText(getResources().getString(R.string.management_weather_data));
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setOnClickListener(this);
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);
		rightTv.setText(getResources().getString(R.string.new_base));
		rightTv.setVisibility(View.GONE);
		net_error = (TextView) findViewById(R.id.net_error);
		net_error.setOnClickListener(this);
		progressView = (RelativeLayout) findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);
		layout_header = (LinearLayout) findViewById(R.id.layout_header);
		mList = (DeleteListView) findViewById(R.id.main_data_list);
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (mList.canClick()) {
					// PlotBean mBean = (PlotBean)
					// parent.getItemAtPosition(position);
					// Intent intent = new Intent(getApplicationContext(),
					// PlotCropsFragmentActivity.class);
					// intent.putExtra("baseid", mBean.getID());
					// intent.putExtra("cropTypeId", mBean.getCrop_Type_ID());
					// startActivity(intent);
					// Util.showToast(getApplicationContext(), "==="+position,
					// Toast.LENGTH_SHORT);
				}

			}
		});
	}

	/**
	 * ≥ı ºªØViewPage
	 * 
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.net_error:
			initData();
			break;
		case R.id.leftTv:
			finish();
			break;

		default:
			break;
		}
	}

	private void initData() {
		// TODO Auto-generated method stub
		progressView.setVisibility(View.VISIBLE);
		net_error.setVisibility(View.GONE);
		new Thread() {
			public void run() {
				if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair("landid", landid));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.GET_MANAGER_WEATHER_DATA_URL,
							nameValuePair);
					Logger.d("ddd", Constants.GET_MANAGER_WEATHER_DATA_URL
							+ nameValuePair.toString());
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mBean = gson.fromJson(sb.toString(),
								ManagerGrowthInfo.class);
						if (mBean != null && "0".equals(mBean.getStatus())) {
							Message msg = mHandler.obtainMessage(REFRESH_TEXT);
							mHandler.sendMessage(msg);
						} else {
							Message msg = mHandler.obtainMessage(
									2,
									getResources().getString(
											R.string.net_data_error));
							mHandler.sendMessage(msg);
						}
					} else {
						Logger.d("ddd", "sb==null");

						Message msg = mHandler.obtainMessage(2, getResources()
								.getString(R.string.net_data_error));
						mHandler.sendMessage(msg);
					}
				} else {
					Message msg = mHandler.obtainMessage(2, getResources()
							.getString(R.string.net_data_error));
					mHandler.sendMessage(msg);
				}
			};
		}.start();
	}

	private void deletePlotData(final int position, final String dataid) {
		// TODO Auto-generated method stub
		progressView.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair("dataid", dataid));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.DELETE_WEATHER_DATA_URL, nameValuePair);
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						deleteBean = gson.fromJson(sb.toString(),
								UploadResultBean.class);
						if ("0".equals(deleteBean.getStatus())) {
							Message msg = mHandler.obtainMessage(3);
							msg.arg1 = position;
							mHandler.sendMessage(msg);
						} else {
							Message msg = mHandler.obtainMessage(
									4,
									getResources().getString(
											R.string.upload_error));
							mHandler.sendMessage(msg);
						}
					} else {
						Logger.d("ddd", "sb==null");

						Message msg = mHandler.obtainMessage(4, getResources()
								.getString(R.string.upload_error));
						mHandler.sendMessage(msg);
					}
				} else {
					Message msg = mHandler.obtainMessage(4, getResources()
							.getString(R.string.net_data_error));
					mHandler.sendMessage(msg);
				}
			};
		}.start();
	}

	public class ManagerGrowthAdapter extends BaseAdapter {
		// private Context mContext;
		private LayoutInflater mLayoutInflater;
		private Context mContext;
		private ViewHolder viewHolder = null;
		int unreadNum;

		public ManagerGrowthAdapter(Context mContext) {
			super();
			this.mContext = mContext;
			mLayoutInflater = LayoutInflater.from(mContext);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			// Log.d("TAG", "adapter.size() -" + mNewsListContent.size());
			return growthListBean.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return growthListBean.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View viewRoot = convertView;
			if (viewRoot == null) {
				viewRoot = mLayoutInflater.inflate(
						R.layout.item_manager_weather_list, null);
				viewHolder = new ViewHolder();
				viewHolder.item_data1 = (TextView) viewRoot
						.findViewById(R.id.item_data1);
				viewHolder.item_data2 = (TextView) viewRoot
						.findViewById(R.id.item_data2);
				viewHolder.item_data3 = (TextView) viewRoot
						.findViewById(R.id.item_data3);
				viewHolder.item_data4 = (TextView) viewRoot
						.findViewById(R.id.item_data4);
				viewHolder.item_time = (TextView) viewRoot
						.findViewById(R.id.item_time);
				viewRoot.setTag(viewHolder);

				viewHolder.delete = (TextView) viewRoot
						.findViewById(R.id.delete);
				viewRoot.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) viewRoot.getTag();
			}
			viewHolder.delete
					.setOnClickListener(new lvButtonListener(position));

			viewHolder.item_data1.setText(growthListBean.get(position)
					.getData1());
			viewHolder.item_data2.setText(growthListBean.get(position)
					.getData2());
			viewHolder.item_data3.setText(growthListBean.get(position)
					.getData3());
			viewHolder.item_data4.setText(growthListBean.get(position)
					.getData4());
			viewHolder.item_time.setText(growthListBean.get(position)
					.getAddTime());
			return viewRoot;
		}

		private class ViewHolder {

			private TextView item_time, item_data1,item_data2,item_data3,item_data4;

			private TextView delete;
		}

		class lvButtonListener implements OnClickListener {
			private int position;

			lvButtonListener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(View v) {
				int vid = v.getId();
				if (vid == viewHolder.delete.getId()) {
					deletePlotData(position, growthListBean.get(position)
							.getID());
				}
			}
		}
	}

	@Subscribe
	public void onBusEvent(BusEvent event) {
		System.out.println("====================");
		if (event == BusEvent.REFRESH_ADD_BASE_LAND_EVENT
				|| event == BusEvent.REFRESH_DELETE_BASE_LAND_EVENT) {
			initData();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isFinish = true;
		ZteApplication.bus.unregister(this);
	}
}