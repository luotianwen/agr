package com.zte.agricul.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.R.integer;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.otto.Subscribe;
import com.zte.agricul.R;
import com.zte.agricul.adapter.PlotMainAllListAdapter;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.PlotAllListInfo;
import com.zte.agricul.bean.PlotBean;
import com.zte.agricul.bean.UploadResultBean;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.Options;
import com.zte.agricul.util.Util;
import com.zte.agricul.view.DeleteListView;

/***
 * 管理地块
 * 
 * @author lxl
 * 
 */
public class PlotAllListActivity extends BaseActivity implements
		OnClickListener {
	private TextView leftTv, ivTitleName, rightTv;
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private DeleteListView mList;

	private PlotMainAllListAdapter mAdapter;
	private String id = "", landName = "";
	private TextView net_error;
	private RelativeLayout progressView;
	private PlotAllListInfo mBean;
	private List<PlotBean> landListBean;
	private UploadResultBean deleteBean;
	private boolean isFinish = false;
	public ImageLoader imageLoader;
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
				landListBean = mBean.getResult().getLandList();
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
				ZteApplication.bus.post(BusEvent.REFRESH_DELETE_PLOT_EVENT);
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
		imageLoader = ImageLoader.getInstance();
		id = getIntent().getStringExtra("id");
		landName = getIntent().getStringExtra("landName");
		initView();
		initData();
	};

	private void initView() {
		// TODO Auto-generated method stub
		leftTv = (TextView) findViewById(R.id.leftTv);
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setOnClickListener(this);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		ivTitleName.setText(getResources().getString(R.string.management_plot));
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setOnClickListener(this);
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);
		rightTv.setText(getResources().getString(R.string.new_plot));
		rightTv.setVisibility(View.VISIBLE);
		net_error = (TextView) findViewById(R.id.net_error);
		net_error.setOnClickListener(this);
		progressView = (RelativeLayout) findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);
		mList = (DeleteListView) findViewById(R.id.main_data_list);
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long lo) {
				// TODO Auto-generated method stub
				if (mList.canClick()) {
					if (mBean == null || mBean.getResult() == null
							|| mBean.getResult().getCropTypeList() == null
							|| mBean.getResult().getCropBrandList() == null
							|| mBean.getResult().getUsersList() == null) {
						return;
					}
					PlotBean bean = (PlotBean) parent.getItemAtPosition(position);
					Intent intent = new Intent(getApplicationContext(),
							EditPlotInfoActivity.class);
					intent.putExtra("cropType", (Serializable) mBean
							.getResult().getCropTypeList());
					intent.putExtra("cropBrand", (Serializable) mBean
							.getResult().getCropBrandList());
					intent.putExtra("userList", (Serializable) mBean
							.getResult().getUsersList());
					intent.putExtra("CropAdditive", (Serializable) mBean
							.getResult().getCropAdditiveList());
					intent.putExtra("bean", (Serializable) bean);
					intent.putExtra("id", id);
					intent.putExtra("landName", landName);
					startActivity(intent);
				}

			}
		});
	}

	/**
	 * 初始化ViewPage
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
					|| mBean.getResult().getCropTypeList() == null
					|| mBean.getResult().getCropBrandList() == null
					|| mBean.getResult().getUsersList() == null) {
				return;
			}
			intent = new Intent(getApplicationContext(),
					AddPlotInfoActivity.class);
			intent.putExtra("cropType", (Serializable) mBean.getResult()
					.getCropTypeList());
			intent.putExtra("cropBrand", (Serializable) mBean.getResult()
					.getCropBrandList());
			intent.putExtra("userList", (Serializable) mBean.getResult()
					.getUsersList());
			intent.putExtra("CropAdditive", (Serializable) mBean.getResult()
					.getCropAdditiveList());
			intent.putExtra("id", id);
			intent.putExtra("landName", landName);
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
					nameValuePair.add(new BasicNameValuePair("baseid", id));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.PLOT_ALL_LIST_URL, nameValuePair);
					Logger.d(
							"ddd",
							Constants.PLOT_ALL_LIST_URL
									+ nameValuePair.toString());
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mBean = gson.fromJson(sb.toString(),
								PlotAllListInfo.class);
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
					nameValuePair.add(new BasicNameValuePair("landid", landid));
					nameValuePair.add(new BasicNameValuePair("baseid", id));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.UPLOAD_DELETE_PLOT_DATA_URL,
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
				viewRoot = mLayoutInflater.inflate(
						R.layout.item_plot_main_all_list, null);
				viewHolder = new ViewHolder();
				viewHolder.item_crop_icon = (ImageView) viewRoot
						.findViewById(R.id.item_crop_icon);
				viewHolder.txt_title = (TextView) viewRoot
						.findViewById(R.id.txt_title);
				viewHolder.txt_time = (TextView) viewRoot
						.findViewById(R.id.txt_time);
				viewHolder.land_name = (TextView) viewRoot
						.findViewById(R.id.land_name);
				viewHolder.brand_name = (TextView) viewRoot
						.findViewById(R.id.brand_name);

				viewHolder.delete = (TextView) viewRoot
						.findViewById(R.id.delete);
				viewRoot.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) viewRoot.getTag();
			}
			viewHolder.delete
					.setOnClickListener(new lvButtonListener(position));
			viewHolder.txt_title.setText(landListBean.get(position)
					.getCropName());
			viewHolder.txt_time.setText(getResources().getString(R.string.date_of_sowing)+"："
					+ landListBean.get(position).getSow_Time());
			viewHolder.land_name.setText(landListBean.get(position).getName());
			viewHolder.brand_name.setText(landListBean.get(position)
					.getBrandName());
			imageLoader.displayImage(landListBean.get(position).getCropImage(),
					viewHolder.item_crop_icon,
					Options.bulidOptions(R.drawable.bg_shuffling));
			return viewRoot;
		}

		private class ViewHolder {
			private TextView txt_title, txt_time, land_name, brand_name;
			private ImageView item_crop_icon;
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
		if (event == BusEvent.REFRESH_PLOT_EVENT
				|| event == BusEvent.REFRESH_ADD_CROPS_DATA_EVENT) {
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