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
import android.webkit.WebView.FindListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zte.agricul.zh.R;
import com.zte.agricul.adapter.AgriculListAdapter;
import com.zte.agricul.app.Constants;
import com.zte.agricul.bean.AgriculMainPopInfo;
import com.zte.agricul.bean.PlotBaseInfo;
import com.zte.agricul.ui.MapActivity;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.Util;

public class PlotInfoFragment extends Fragment implements OnClickListener{
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private View view;
	private String id ; 
	private Gson gson;
	private PlotBaseInfo  mBean;
	private TextView net_error ;
	private RelativeLayout  progressView;
	private ScrollView scrollView1 ;
	private TextView  info_address ,info_user_name,info_phone,info_location,
	info_no_frost,info_temp,info_rainfall,info_properties,info_ph,info_npk,
	info_organic_content,info_element,info_crop_species,info_crop_yield;
	private boolean isFinish  = false;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isFinish) {
				return ;
			}
			progressView.setVisibility(View.GONE);
			switch (msg.what) {
			case REFRESH_TEXT:
				
				info_address.setText(mBean.getResult().getAddress());
				info_user_name.setText(mBean.getResult().getUserName());
				info_phone.setText(mBean.getResult().getPhone());
				info_location.setText(mBean.getResult().getLatitude()+","+mBean.getResult().getLongitude());
				info_no_frost.setText(mBean.getResult().getFrost_Free_Period_MinDay()+"~"+mBean.getResult().getFrost_Free_Period_MaxDay());
				info_temp.setText(mBean.getResult().getEffective_Accumulated_Temp());
				info_rainfall.setText(mBean.getResult().getAnnual_Arecipitation());
				info_properties.setText(mBean.getResult().getSoil_Properties());
				info_ph.setText("PH"+mBean.getResult().getSoil_pH());
				info_npk.setText(mBean.getResult().getN_P_K_Content()+getResources().getString(R.string.unit_npk));
				info_organic_content.setText(mBean.getResult().getOrganic_Matter_Content()+getResources().getString(R.string.unit_organic));
				info_element.setText(mBean.getResult().getMedium_Trace_Element()+getResources().getString(R.string.unit_element));
				info_crop_species.setText(mBean.getResult().getPrevious_Crop_Species());
				info_crop_yield.setText(mBean.getResult().getPrevious_Crop_Yield());
				
				net_error.setVisibility(View.GONE);
				
				scrollView1.setVisibility(View.VISIBLE);
				break;
			case NET_ERROR:
				net_error.setVisibility(View.VISIBLE);
				Util.showToast(getActivity(), (String) msg.obj,
						Toast.LENGTH_SHORT);
				break;

			}

		}
	};
	
	public PlotInfoFragment(String id) {
		// TODO Auto-generated constructor stub
		this.id =id ;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_plot_info, container,
				false);
		gson = new Gson();
		initView(view);
		initData();
		return view;
	}
	private void initView(View view2) {
		// TODO Auto-generated method stub
		net_error =(TextView) view2.findViewById(R.id.net_error);
		net_error.setOnClickListener(this);	
		progressView =(RelativeLayout) view2.findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);
		scrollView1 =(ScrollView) view2.findViewById(R.id.scrollView1);
		
		info_address =(TextView) view2.findViewById(R.id.info_address);
		info_address.setOnClickListener(this);
		info_user_name=(TextView) view2.findViewById(R.id.info_user_name);
		info_phone=(TextView) view2.findViewById(R.id.info_phone);
		info_location=(TextView) view2.findViewById(R.id.info_location);
		info_location.setOnClickListener(this);
		info_no_frost=(TextView) view2.findViewById(R.id.info_no_frost);
		info_temp=(TextView) view2.findViewById(R.id.info_temp);
		info_rainfall=(TextView) view2.findViewById(R.id.info_rainfall);
		info_properties=(TextView) view2.findViewById(R.id.info_properties);
		info_ph=(TextView) view2.findViewById(R.id.info_ph);

		info_npk=(TextView) view2.findViewById(R.id.info_npk);
		info_organic_content=(TextView) view2.findViewById(R.id.info_organic_content);
		info_element=(TextView) view2.findViewById(R.id.info_element);
		info_crop_species=(TextView) view2.findViewById(R.id.info_crop_species);
		info_crop_yield=(TextView) view2.findViewById(R.id.info_crop_yield);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent  ;
		switch (v.getId()) {
		case R.id.net_error:
			initData();
			break;
		case R.id.info_location:
			if (mBean.getResult().getLatitude()==null||"".equals(mBean.getResult().getLatitude())||mBean.getResult().getLongitude()==null||"".equals(mBean.getResult().getLongitude())) {
				Util.showToast(getActivity(), getResources().getString(R.string.address_error),Toast.LENGTH_SHORT);
				return ;
			}
		    intent = new Intent(getActivity(),MapActivity.class);
			intent.putExtra("Lat", mBean.getResult().getLatitude());
			intent.putExtra("Lon", mBean.getResult().getLongitude());
			intent.putExtra("name", info_address.getText().toString());
			startActivity(intent);
			break;
		case R.id.info_address:
			if (mBean.getResult().getLatitude()==null||"".equals(mBean.getResult().getLatitude())||mBean.getResult().getLongitude()==null||"".equals(mBean.getResult().getLongitude())) {
				Util.showToast(getActivity(), getResources().getString(R.string.address_error),Toast.LENGTH_SHORT);
				return ;
			}
			intent = new Intent(getActivity(),MapActivity.class);
			intent.putExtra("Lat", mBean.getResult().getLatitude());
			intent.putExtra("Lon", mBean.getResult().getLongitude());
			intent.putExtra("name", info_address.getText().toString());
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
				if (NetworkUtil.isNetworkAvailable(getActivity())) {
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair("userid",
							Constants.uid));
					nameValuePair.add(new BasicNameValuePair("baseid",
							id));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.PLOT_BASE_URL, nameValuePair);
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mBean = gson.fromJson(sb.toString(),
								PlotBaseInfo.class);
						if (mBean != null && "0".equals(mBean.getStatus())) {
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
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeMessages(REFRESH_TEXT);
		handler.removeMessages(NET_ERROR);
		isFinish = true ;
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		isFinish =true;
	}
}
