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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.zte.agricul.zh.R;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.ManagerGrowthBean;
import com.zte.agricul.bean.ManagerGrowthInfo;
import com.zte.agricul.bean.UploadResultBean;
import com.zte.agricul.fragment.PlotCropsGrowFragment;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.Util;
import com.zte.agricul.view.DeleteListView;

public class ManagerGrowthActivity extends BaseActivity implements
		OnClickListener {
	private TextView leftTv, ivTitleName, rightTv;
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private DeleteListView mList;

	private ManagerGrowthAdapter mAdapter;
	private TextView net_error ;
	private RelativeLayout progressView;
	private ManagerGrowthInfo mBean;
	private List<ManagerGrowthBean> growthListBean;
	private UploadResultBean deleteBean;
	private boolean isFinish = false;
	private String landid="";
	private String landName = "";
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
				if (growthListBean.size()>0) {
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
				IS_REFUSH  = true ;
//				ZteApplication.bus
//						.post(BusEvent.REFRESH_CHART_DATA_EVENT);
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
		setContentView(R.layout.activity_manager_growth_list);
		ZteApplication.bus.register(this);
		landid = getIntent().getStringExtra("landid");
		landName =  getIntent().getStringExtra("landName");
		initView();
		initData();
	};

	private void initView() {
		// TODO Auto-generated method stub
		leftTv = (TextView) findViewById(R.id.leftTv);
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setOnClickListener(this);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		ivTitleName.setText(getResources().getString(R.string.management_grow_data));
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setOnClickListener(this);
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);
		rightTv.setText(getResources().getString(R.string.new_base));
		rightTv.setVisibility(View.GONE);
		net_error = (TextView) findViewById(R.id.net_error);
		net_error.setOnClickListener(this);
		progressView = (RelativeLayout) findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);
		
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
					nameValuePair.add(new BasicNameValuePair("landid",
							landid));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.GET_MANAGER_GROWTH_DATA_URL, nameValuePair);
					Logger.d("ddd",
							Constants.GET_MANAGER_GROWTH_DATA_URL + nameValuePair.toString());
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
							Constants.DELETE_GROWTH_DATA_URL,
							nameValuePair);
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
						R.layout.item_manager_growth_list, null);
				viewHolder = new ViewHolder();
				viewHolder.plot_name = (TextView) viewRoot
						.findViewById(R.id.plot_name);
				viewHolder.add_time = (TextView) viewRoot
						.findViewById(R.id.add_time);
			
				viewHolder.data_name[0]  = (TextView) viewRoot
						.findViewById(R.id.data_name1);
				viewHolder.data_name[1]  = (TextView) viewRoot
						.findViewById(R.id.data_name2);
				viewHolder.data_name[2]  = (TextView) viewRoot
						.findViewById(R.id.data_name3);
				viewHolder.data_name[3]  = (TextView) viewRoot
						.findViewById(R.id.data_name4);
				viewHolder.data_name[4]  = (TextView) viewRoot
						.findViewById(R.id.data_name5);
				viewHolder.data_name[5]  = (TextView) viewRoot
						.findViewById(R.id.data_name6);
				viewHolder.data_name[6]  = (TextView) viewRoot
						.findViewById(R.id.data_name7);
				viewHolder.data_name[7]  = (TextView) viewRoot
						.findViewById(R.id.data_name8);
				viewHolder.data_name[8]  = (TextView) viewRoot
						.findViewById(R.id.data_name9);
				viewHolder.data_name[9]  = (TextView) viewRoot
						.findViewById(R.id.data_name10);
				viewHolder.data_name[10]  = (TextView) viewRoot
						.findViewById(R.id.data_name11);
				viewHolder.data_name[11]  = (TextView) viewRoot
						.findViewById(R.id.data_name12);
				viewHolder.data_name[12]  = (TextView) viewRoot
						.findViewById(R.id.data_name13);
				viewHolder.data_name[13]  = (TextView) viewRoot
						.findViewById(R.id.data_name14);
				viewHolder.data_name[14]  = (TextView) viewRoot
						.findViewById(R.id.data_name15);
				viewHolder.data_name[15]  = (TextView) viewRoot
						.findViewById(R.id.data_name16);
				viewHolder.data_name[16]  = (TextView) viewRoot
						.findViewById(R.id.data_name17);
				viewHolder.data_name[17]  = (TextView) viewRoot
						.findViewById(R.id.data_name18);
				viewHolder.data_name[18]  = (TextView) viewRoot
						.findViewById(R.id.data_name19);
				viewHolder.data_name[19]  = (TextView) viewRoot
						.findViewById(R.id.data_name20);
				
				
				viewHolder.plot_data[0]  = (TextView) viewRoot
						.findViewById(R.id.plot_data1);
				viewHolder.plot_data[1]  = (TextView) viewRoot
						.findViewById(R.id.plot_data2);
				viewHolder.plot_data[2]  = (TextView) viewRoot
						.findViewById(R.id.plot_data3);
				viewHolder.plot_data[3]  = (TextView) viewRoot
						.findViewById(R.id.plot_data4);
				viewHolder.plot_data[4]  = (TextView) viewRoot
						.findViewById(R.id.plot_data5);
				viewHolder.plot_data[5]  = (TextView) viewRoot
						.findViewById(R.id.plot_data6);
				viewHolder.plot_data[6]  = (TextView) viewRoot
						.findViewById(R.id.plot_data7);
				viewHolder.plot_data[7]  = (TextView) viewRoot
						.findViewById(R.id.plot_data8);
				viewHolder.plot_data[8]  = (TextView) viewRoot
						.findViewById(R.id.plot_data9);
				viewHolder.plot_data[9]  = (TextView) viewRoot
						.findViewById(R.id.plot_data10);
				viewHolder.plot_data[10]  = (TextView) viewRoot
						.findViewById(R.id.plot_data11);
				viewHolder.plot_data[11]  = (TextView) viewRoot
						.findViewById(R.id.plot_data12);
				viewHolder.plot_data[12]  = (TextView) viewRoot
						.findViewById(R.id.plot_data13);
				viewHolder.plot_data[13]  = (TextView) viewRoot
						.findViewById(R.id.plot_data14);
				viewHolder.plot_data[14]  = (TextView) viewRoot
						.findViewById(R.id.plot_data15);
				viewHolder.plot_data[15]  = (TextView) viewRoot
						.findViewById(R.id.plot_data16);
				viewHolder.plot_data[16]  = (TextView) viewRoot
						.findViewById(R.id.plot_data17);
				viewHolder.plot_data[17]  = (TextView) viewRoot
						.findViewById(R.id.plot_data18);
				viewHolder.plot_data[18]  = (TextView) viewRoot
						.findViewById(R.id.plot_data19);
				viewHolder.plot_data[19]  = (TextView) viewRoot
						.findViewById(R.id.plot_data20);
				
				viewHolder.layout[0]= (LinearLayout) viewRoot
						.findViewById(R.id.layout1);
				viewHolder.layout[1]= (LinearLayout) viewRoot
						.findViewById(R.id.layout2);
				viewHolder.layout[2]= (LinearLayout) viewRoot
						.findViewById(R.id.layout3);
				viewHolder.layout[3]= (LinearLayout) viewRoot
						.findViewById(R.id.layout4);
				viewHolder.layout[4]= (LinearLayout) viewRoot
						.findViewById(R.id.layout5);
				viewHolder.layout[5]= (LinearLayout) viewRoot
						.findViewById(R.id.layout6);
				viewHolder.layout[6]= (LinearLayout) viewRoot
						.findViewById(R.id.layout7);
				viewHolder.layout[7]= (LinearLayout) viewRoot
						.findViewById(R.id.layout8);
				viewHolder.layout[8]= (LinearLayout) viewRoot
						.findViewById(R.id.layout9);
				viewHolder.layout[9]= (LinearLayout) viewRoot
						.findViewById(R.id.layout10);
				viewHolder.layout[10]= (LinearLayout) viewRoot
						.findViewById(R.id.layout11);
				viewHolder.layout[11]= (LinearLayout) viewRoot
						.findViewById(R.id.layout12);
				viewHolder.layout[12]= (LinearLayout) viewRoot
						.findViewById(R.id.layout13);
				viewHolder.layout[13]= (LinearLayout) viewRoot
						.findViewById(R.id.layout14);
				viewHolder.layout[14]= (LinearLayout) viewRoot
						.findViewById(R.id.layout15);
				viewHolder.layout[15]= (LinearLayout) viewRoot
						.findViewById(R.id.layout16);
				viewHolder.layout[16]= (LinearLayout) viewRoot
						.findViewById(R.id.layout17);
				viewHolder.layout[17]= (LinearLayout) viewRoot
						.findViewById(R.id.layout18);
				viewHolder.layout[18]= (LinearLayout) viewRoot
						.findViewById(R.id.layout19);
				viewHolder.layout[19]= (LinearLayout) viewRoot
						.findViewById(R.id.layout20);
				

				viewRoot.setTag(viewHolder);

				viewHolder.delete = (TextView) viewRoot
						.findViewById(R.id.delete);
				viewRoot.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) viewRoot.getTag();
			}
			viewHolder.delete
					.setOnClickListener(new lvButtonListener(position));

			viewHolder.plot_name.setText(landName);
			viewHolder.add_time.setText(growthListBean.get(position).getAddTime());
			
			for (int i = 0; i < PlotCropsGrowFragment.cropIndex.size(); i++) {
				viewHolder.layout[i].setVisibility(View.VISIBLE);
				viewHolder.data_name[i].setText(PlotCropsGrowFragment.cropIndex.get(i).getIndexName());
				viewHolder.plot_data[i].setText(getPlotData(position, i));
			}
			
			
			
			return viewRoot;
		}

		private String getPlotData(int position ,int index){
			String data = "";
			switch (index) {
			case 0:
				data=growthListBean.get(position).getData1();
				break;
			case 1:
				data=growthListBean.get(position).getData2();
				break;
			case 2:
				data=growthListBean.get(position).getData3();
				break;
			case 3:
				data=growthListBean.get(position).getData4();
				break;
			case 4:
				data=growthListBean.get(position).getData5();
				break;
			case 5:
				data=growthListBean.get(position).getData6();
				break;
			case 6:
				data=growthListBean.get(position).getData7();
				break;
			case 7:
				data=growthListBean.get(position).getData8();
				break;
			case 8:
				data=growthListBean.get(position).getData9();
				break;
			case 9:
				data=growthListBean.get(position).getData10();
				break;
			case 10:
				data=growthListBean.get(position).getData11();
				break;
			case 11:
				data=growthListBean.get(position).getData12();
				break;
			case 12:
				data=growthListBean.get(position).getData13();
				break;
			case 13:
				data=growthListBean.get(position).getData14();
				break;
			case 14:
				data=growthListBean.get(position).getData15();
				break;
			case 15:
				data=growthListBean.get(position).getData16();
				break;
			case 16:
				data=growthListBean.get(position).getData17();
				break;
			case 17:
				data=growthListBean.get(position).getData18();
				break;
			case 18:
				data=growthListBean.get(position).getData19();
				break;
			case 19:
				data=growthListBean.get(position).getData20();
				break;
				
			}
			
			
			return data;
		}
		private class ViewHolder {

			private TextView plot_name,add_time;
			private TextView[] data_name = new TextView[20]; 
			private TextView[] plot_data = new TextView[20]; 
			private LinearLayout[] layout =new LinearLayout[20];
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
					deletePlotData(position, growthListBean.get(position).getID());
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