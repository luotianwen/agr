package com.zte.agricul.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.zte.agricul.R;
import com.zte.agricul.adapter.AgriculListAdapter;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.AgriculListBean;
import com.zte.agricul.bean.AgriculMainInfo;
import com.zte.agricul.bean.AgriculMainPopInfo;
import com.zte.agricul.bean.CityBean;
import com.zte.agricul.bean.ConturyBean;
import com.zte.agricul.bean.MainPopBean;
import com.zte.agricul.bean.PlotAllListInfo;
import com.zte.agricul.bean.PlotBean;
import com.zte.agricul.bean.ProvinceBean;
import com.zte.agricul.bean.UploadResultBean;
import com.zte.agricul.ui.PlotAllListActivity.PlotMainAllListAdapter;
import com.zte.agricul.ui.PlotAllListActivity.PlotMainAllListAdapter.lvButtonListener;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.Options;
import com.zte.agricul.util.Util;
import com.zte.agricul.view.DeleteListView;
import com.zte.agricul.view.GridViewToScrollView;
import com.zte.agricul.view.PopupWindowCityList;
import com.zte.agricul.view.PopupWindowCropsList;

public class ManageMainListActivity extends BaseActivity implements
		OnClickListener {
	private TextView leftTv, ivTitleName, rightTv;
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private DeleteListView mList;

	private PlotMainAllListAdapter mAdapter;
	private TextView net_error;
	private RelativeLayout progressView;
	private AgriculMainInfo mBean;
	private List<AgriculListBean> landListBean;
	private UploadResultBean deleteBean;
	private List<ConturyBean> Conturylist;
	private List<ProvinceBean> ProvinceList;
	private List<CityBean> CityList;
	private List<CityBean> userList;
	private boolean isFinish = false;
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
				landListBean = mBean.getResult().getBaseList();
				mAdapter = new PlotMainAllListAdapter(getApplicationContext());
				mList.setAdapter(mAdapter);
				net_error.setVisibility(View.GONE);
				break;
			case NET_ERROR:
				net_error.setVisibility(View.VISIBLE);
				Util.showToast(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT);
				break;
			case 3:
				net_error.setVisibility(View.GONE);
				landListBean.remove(msg.arg1);
				mAdapter.notifyDataSetChanged();
				mList.turnToNormal();
				ZteApplication.bus.post(BusEvent.REFRESH_DELETE_BASE_LAND_EVENT);
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
		setContentView(R.layout.activity_all_plot_list);
		ZteApplication.bus.register(this);
		initView();
		initData();
	};

	private void initView() {
		// TODO Auto-generated method stub
		leftTv = (TextView) findViewById(R.id.leftTv);
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setOnClickListener(this);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		ivTitleName.setText(getResources().getString(R.string.management_base));
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setOnClickListener(this);
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);
		rightTv.setText(getResources().getString(R.string.new_base));
		rightTv.setVisibility(View.VISIBLE);
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

					if (mBean == null || mBean.getResult() == null
							|| mBean.getResult().getContury() == null
							|| mBean.getResult().getProvince() == null
							|| mBean.getResult().getCity() == null) {
						return;
					}
					AgriculListBean bean = (AgriculListBean)parent.getItemAtPosition(position);
					Intent	intent =new Intent(getApplicationContext(),EditBaseLandActivity.class);
					intent.putExtra("contury", (Serializable) mBean.getResult()
							.getContury());
					intent.putExtra("province", (Serializable) mBean.getResult()
							.getProvince());
					intent.putExtra("city", (Serializable) mBean.getResult()
							.getCity());
					intent.putExtra("user", (Serializable) mBean.getResult()
							.getUsersList());
					intent.putExtra("bean", (Serializable) bean);
					startActivity(intent);
					
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

		case R.id.rightTv:
			
			if (mBean == null || mBean.getResult() == null
					|| mBean.getResult().getContury() == null
					|| mBean.getResult().getProvince() == null
					|| mBean.getResult().getCity() == null) {
				return;
			}
			intent =new Intent(getApplicationContext(),AddBaseLandActivity.class);
			intent.putExtra("contury", (Serializable) mBean.getResult()
					.getContury());
			intent.putExtra("province", (Serializable) mBean.getResult()
					.getProvince());
			intent.putExtra("city", (Serializable) mBean.getResult()
					.getCity());
			intent.putExtra("user", (Serializable) mBean.getResult()
					.getUsersList());
			
			startActivity(intent);
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
					nameValuePair.add(new BasicNameValuePair("userid",
							Constants.uid));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.MAIN_LIST_URL, nameValuePair);
					Logger.d(
							"ddd",
							Constants.MAIN_LIST_URL
									+ nameValuePair.toString());
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mBean = gson.fromJson(sb.toString(),
								AgriculMainInfo.class);
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

	private void deletePlotData(final int position, final String landid) {
		// TODO Auto-generated method stub
		progressView.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair("baseid", landid));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.UPLOAD_DELETE_BASE_LAND_URL,
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

	public class PlotMainAllListAdapter extends BaseAdapter {
		// private Context mContext;
		private LayoutInflater mLayoutInflater;
		private Context mContext;
		private ViewHolder viewHolder = null;
		int unreadNum;

		public PlotMainAllListAdapter(Context mContext) {
			super();
			this.mContext = mContext;
			mLayoutInflater = LayoutInflater.from(mContext);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			// Log.d("TAG", "adapter.size() -" + mNewsListContent.size());
			return landListBean.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return landListBean.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View viewRoot = convertView;
			if (viewRoot == null) {
				viewRoot = mLayoutInflater.inflate(R.layout.item_main_list_manage, null);
				viewHolder = new ViewHolder();
				viewHolder.txt_title = (TextView) viewRoot
						.findViewById(R.id.txt_title);
//				viewHolder.txt_capacity = (TextView) viewRoot
//						.findViewById(R.id.txt_capacity);
				viewHolder.main_list_icon = (ImageView) viewRoot
						.findViewById(R.id.main_list_icon);
				viewHolder.crop_item1 = (TextView) viewRoot
						.findViewById(R.id.crop_item1);
				viewHolder.crop_item2 = (TextView) viewRoot
						.findViewById(R.id.crop_item2);
				viewHolder.crop_item3 = (TextView) viewRoot
						.findViewById(R.id.crop_item3);
				viewHolder.crop_img_item1 = (ImageView) viewRoot
						.findViewById(R.id.crop_img_item1);
				viewHolder.crop_img_item2 = (ImageView) viewRoot
						.findViewById(R.id.crop_img_item2);
				viewHolder.crop_img_item3 = (ImageView) viewRoot
						.findViewById(R.id.crop_img_item3);
				viewRoot.setTag(viewHolder);
			

				viewHolder.delete = (TextView) viewRoot
						.findViewById(R.id.delete);
				viewRoot.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) viewRoot.getTag();
			}
			viewHolder.delete
					.setOnClickListener(new lvButtonListener(position));
			
			
			
			viewHolder.txt_title.setText(landListBean.get(position).getName());
			imageLoader.displayImage(landListBean.get(position).getImagePath(), viewHolder.main_list_icon,Options.bulidOptions(R.drawable.default_img_loading_bg));
			
			if (landListBean.get(position).getCropList().size()==0) {
				viewHolder.crop_item1.setVisibility(View.GONE);
				viewHolder.crop_item2.setVisibility(View.GONE);
				viewHolder.crop_item3.setVisibility(View.GONE);
			}else if (landListBean.get(position).getCropList().size()==1){
				viewHolder.crop_item1.setVisibility(View.VISIBLE);
				viewHolder.crop_item2.setVisibility(View.GONE);
				viewHolder.crop_item3.setVisibility(View.GONE);
				viewHolder.crop_item1.setText(landListBean.get(position).getCropList().get(0).getCropTypeName());
				if ("1".equals(landListBean.get(position).getCropList().get(0).getIsnew())) {
					viewHolder.crop_img_item1.setVisibility(View.VISIBLE);
				}else {
					viewHolder.crop_img_item1.setVisibility(View.GONE);
				}
			
			}else if (landListBean.get(position).getCropList().size()==2){
				viewHolder.crop_item1.setVisibility(View.VISIBLE);
				viewHolder.crop_item2.setVisibility(View.VISIBLE);
				viewHolder.crop_item3.setVisibility(View.GONE);
				viewHolder.crop_item1.setText(landListBean.get(position).getCropList().get(0).getCropTypeName());
				viewHolder.crop_item2.setText(landListBean.get(position).getCropList().get(1).getCropTypeName());
				if ("1".equals(landListBean.get(position).getCropList().get(0).getIsnew())) {
					viewHolder.crop_img_item1.setVisibility(View.VISIBLE);
				}else {
					viewHolder.crop_img_item1.setVisibility(View.GONE);
				}
				if ("1".equals(landListBean.get(position).getCropList().get(1).getIsnew())) {
					viewHolder.crop_img_item2.setVisibility(View.VISIBLE);
				}else {
					viewHolder.crop_img_item2.setVisibility(View.GONE);
				}
			
			}else if (landListBean.get(position).getCropList().size()>=3){
				viewHolder.crop_item1.setVisibility(View.VISIBLE);
				viewHolder.crop_item2.setVisibility(View.VISIBLE);
				viewHolder.crop_item3.setVisibility(View.VISIBLE);
				viewHolder.crop_item1.setText(landListBean.get(position).getCropList().get(0).getCropTypeName());
				viewHolder.crop_item2.setText(landListBean.get(position).getCropList().get(1).getCropTypeName());
				viewHolder.crop_item3.setText(landListBean.get(position).getCropList().get(2).getCropTypeName());
				if ("1".equals(landListBean.get(position).getCropList().get(0).getIsnew())) {
					viewHolder.crop_img_item1.setVisibility(View.VISIBLE);
				}else {
					viewHolder.crop_img_item1.setVisibility(View.GONE);
				}
				if ("1".equals(landListBean.get(position).getCropList().get(1).getIsnew())) {
					viewHolder.crop_img_item2.setVisibility(View.VISIBLE);
				}else {
					viewHolder.crop_img_item2.setVisibility(View.GONE);
				}
				if ("1".equals(landListBean.get(position).getCropList().get(2).getIsnew())) {
					viewHolder.crop_img_item3.setVisibility(View.VISIBLE);
				}else {
					viewHolder.crop_img_item3.setVisibility(View.GONE);
				}
			}
			return viewRoot;
		}

		private class ViewHolder {

			private ImageView main_list_icon;
			private TextView txt_title, txt_capacity, unread_find_number;
			private RelativeLayout fault_msg ;
			private GridViewToScrollView     main_data_list ; 
			
			private TextView crop_item1  ,crop_item2 ,crop_item3 ; 
			private ImageView crop_img_item1  ,crop_img_item2 ,crop_img_item3 ; 
		
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
					deletePlotData(position, landListBean.get(position).getID());
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