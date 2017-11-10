package com.zte.agricul.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.otto.Subscribe;
import com.zte.agricul.zh.R;
import com.zte.agricul.adapter.GrowthLogListAdapter;
import com.zte.agricul.adapter.PlotCropAllListAdapter;
import com.zte.agricul.adapter.PlotCropSpinnerAdapter;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.bean.CropIndexBean;
import com.zte.agricul.bean.GrowthLogInfo;
import com.zte.agricul.bean.PlotBean;
import com.zte.agricul.bean.UploadResultBean;
import com.zte.agricul.ui.AddGrowProcessActivity;
import com.zte.agricul.ui.AddOtherDataActivity;
import com.zte.agricul.ui.PlotCropsFragmentActivity;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.Util;

public class PlotCropsGrowFragment extends Fragment implements OnClickListener {
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private View view;
	private RelativeLayout  progressView;
	private TextView net_error ;
	public ImageLoader imageLoader;
	public Gson gson;
	
	private GrowthLogInfo mBean ;
	private GrowthLogListAdapter	mAdapter;
	private ListView mList ;
	private View headerView;
	private TextView  plotName  ;
	
	private TextView plot_additiveid1,plot_additiveid2 ;
	private boolean isFinish = false ; 
	private String id; 
	
//	public static  int landNum;
	public static ArrayList<String >  landList = new ArrayList<String>() ;
	private List<PlotBean >  plotList  = new ArrayList<PlotBean>() ;
	private String landid2= "0";
	private String landName ="" ;
	
	private Spinner plotSpinner;
	private PlotCropSpinnerAdapter  plotAdapter ;
	
	public static List<CropIndexBean>  cropIndex = new ArrayList<CropIndexBean>();
	private UploadResultBean deleteBean;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (isFinish) {
				return ;
			}
			progressView.setVisibility(View.GONE);
			switch (msg.what) {
			case REFRESH_TEXT:
				if (null!=mBean.getResult().getCropIndex()&&mBean.getResult().getCropIndex().size()>0) {
					cropIndex.clear();
					cropIndex.addAll(mBean.getResult().getCropIndex());
				}
				plot_additiveid1.setText(mBean.getResult().getAdditiveList().get(0));
				plot_additiveid2.setText(mBean.getResult().getAdditiveList().get(1));
				landList.addAll(mBean.getResult().getLandNameList());
				mAdapter = new GrowthLogListAdapter(getActivity(),mBean.getResult().getLogList(),imageLoader);
				mList.setAdapter(mAdapter);
				net_error.setVisibility(View.GONE);
				break;
			case NET_ERROR:
				net_error.setVisibility(View.VISIBLE);
				Util.showToast(getActivity(), (String) msg.obj,
						Toast.LENGTH_SHORT);
				break;
			case 3:
				initData(landid2);
				Util.showToast(getActivity(), getResources().getString(R.string.delete_success),Toast.LENGTH_SHORT);
				break;
			case 4:
				net_error.setVisibility(View.GONE);
				Util.showToast(getActivity(), (String) msg.obj,
						Toast.LENGTH_SHORT);
				break;
			}
//			listView.onRefreshComplete();
		}

	};
	public PlotCropsGrowFragment(String landName ,String id,List<PlotBean >  plotList) {
		// TODO Auto-generated constructor stub
		this.id =id ;
		this.plotList =plotList;
		this.landName = landName ;
		System.out.println("landName==="+landName);
		for (int i = 0; i < plotList.size(); i++) {
			if (id.equals(plotList.get(i).getID())) {
				plotList.remove(i);
			}
		}
		if (plotList==null||plotList.size()==0) {
			return ; 
		}
		landid2 = plotList.get(0).getID();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_plot_grow, container, false);
		ZteApplication.bus.register(this);
		imageLoader = ImageLoader.getInstance();
		gson = new Gson();
		cropIndex = new ArrayList<CropIndexBean>();
		initView(view,inflater);
		initData(landid2);
		return view;
	}

	private void initView(View view,LayoutInflater inflater) {
		// TODO Auto-generated method stub
//		edit1 = (TextView) view.findViewById(R.id.edit1);
//		edit2 = (TextView) view.findViewById(R.id.edit2);
//		edit1.setOnClickListener(this);
//		edit2.setOnClickListener(this);
		
		
		net_error =(TextView) view.findViewById(R.id.net_error);
		net_error.setOnClickListener(this);	
		progressView =(RelativeLayout) view.findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);
		
		mList = (ListView) view.findViewById(R.id.plot_grow_list);
		
		headerView = inflater.inflate(R.layout.item_plot_grow_header, null);
		plotName=(TextView) headerView.findViewById(R.id.plot_name_item1);
		plotSpinner=(Spinner) headerView.findViewById(R.id.plot_spinner);
		
		plot_additiveid1=(TextView) headerView.findViewById(R.id.plot_additiveid1);
		plot_additiveid2=(TextView) headerView.findViewById(R.id.plot_additiveid2);
		plotName.setText(landName);
		mList.addHeaderView(headerView);
		if (plotList!=null||plotList.size()>0) {
			plotSpinner.setVisibility(View.VISIBLE);
			plotAdapter = new PlotCropSpinnerAdapter(getActivity(), plotList);
			plotSpinner.setAdapter(plotAdapter);
			plotSpinner.setOnItemSelectedListener(new SpinnerSelectedListener(3));
		}
		
	}
	// 使用数组形式操作
	class SpinnerSelectedListener implements OnItemSelectedListener {
		private int type ;
		public SpinnerSelectedListener(int type) {
			// TODO Auto-generated constructor stub
			this.type =type ;
		}

		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
//			view.setText("你的血型是：" + m[arg2]);
			if (landid2.equals(plotList.get(position).getID())) {
				return ;
			}
			landid2=plotList.get(position).getID();
			initData(landid2);
			
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent ;
		switch (v.getId()) {
		case R.id.net_error:
			initData(landid2);
			break;
//		case R.id.edit2:
//			intent = new Intent(getActivity(),AddGrowProcessActivity.class);
//			intent.putExtra("type", 1);
//			startActivity(intent);
//			break;
		default:
			break;
		}
	}

	
	private void initData(final String landid2) {
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
					nameValuePair.add(new BasicNameValuePair("landid1",
							id));
					nameValuePair.add(new BasicNameValuePair("landid2",
							landid2));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.GROWTH_LOG_LIST_URL, nameValuePair);
					Logger.d("ddd", Constants.GROWTH_LOG_LIST_URL+ nameValuePair.toString());
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mBean = gson.fromJson(sb.toString(),
								GrowthLogInfo.class);
						if (mBean != null && "0".equals(mBean.getStatus())) {
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
	
	private void deletePlotData(final String logid) {
		// TODO Auto-generated method stub
		progressView.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (NetworkUtil.isNetworkAvailable(getActivity())) {
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair("logid", logid));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.DELETE_LOG_DATA_URL,
							nameValuePair);
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						deleteBean = gson.fromJson(sb.toString(),
								UploadResultBean.class);
						if ("0".equals(deleteBean.getStatus())) {
							Message msg = mHandler.obtainMessage(3);
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
	
	@Subscribe
	public void onBusEvent(BusEvent event) {
		if (event == BusEvent.REFRESH_GROWTH_LOG_EVENT) {
			initData(landid2);
		}else if(event == BusEvent.DELETE_LOG_EVENT){
			System.out.println("1111111111");
			deletePlotData(String.valueOf(BusEvent.DELETE_LOG_EVENT.data));
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (AddGrowProcessActivity.isRefresh) {
			initData(landid2);
			AddGrowProcessActivity.isRefresh= false ;
		}else if (AddOtherDataActivity.isRefresh) {
			initData(landid2);
			AddOtherDataActivity.isRefresh= false ;
		}
//		else if (AddGrowDataActivity2.isRefresh) {
//			initData();
//			AddGrowDataActivity2.isRefresh= false ;
//		}
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		isFinish  = true ;
		ZteApplication.bus.unregister(this);
	}
}
