package com.zte.agricul.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.zte.agricul.R;
import com.zte.agricul.adapter.PlotCropAllListAdapter;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.CityBean;
import com.zte.agricul.bean.CropTypeBean;
import com.zte.agricul.bean.PlotAllListInfo;
import com.zte.agricul.bean.ProvinceBean;
import com.zte.agricul.bean.UploadResultBean;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.Util;
import com.zte.agricul.view.DateTimePickDialogUtil;

/***
 * 新建地块
 * @author Administrator
 *
 */
public class AddPlotInfoActivity extends BaseActivity implements
		OnClickListener {
	private final int REFRESH_LIST_TEXT = 3;
	private TextView leftTv, ivTitleName, rightTv;
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private String id = "", landName = "";
	private RelativeLayout progressView;
	private TextView net_error;
	private UploadResultBean mBean;
	private boolean isFinish = false;
	private TextView landNameTxt;
	private Spinner userSpinner;
	private Spinner cropsBrandSpinner,cropsTypeSpinner,additive_spinner;
//	private ArrayAdapter<String> userAdapter;
	private PlotCropAllListAdapter cropsBrandAdapter,CropAdditiveListAdapter;
	private PlotCropAllListAdapter cropsTypeAdapter;
	private PlotCropAllListAdapter  userAdapter ;
	private TextView selectTime ; 
	private EditText editPlotName ,editPlotInfo,editPlotSize,editPlotOutput,editPlotMark,edit_additive;
	
	private List<CropTypeBean>  UsersList ;
	private List<CropTypeBean>  CropTypeList ;
	private List<CropTypeBean>  CropBrandAllList;
	private List<CropTypeBean>  CropBrandSmallList =new ArrayList<CropTypeBean>();
	
	private List<CropTypeBean>  CropAdditiveList;
	private List<List<CropTypeBean>>  CropBrandList= new ArrayList<List<CropTypeBean>>();
	private String cropTypeid ="";
	private String cropBrandid ="";
	private String cropUserid ="";
	private String cropAdditiveid = "";
	private ImageView add_crop_type ,add_crop_brand;
	
	
	private PlotAllListInfo mListBean;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (isFinish) {
				return;
			}
			switch (msg.what) {
			case REFRESH_TEXT:
				progressView.setVisibility(View.GONE);
				finish();
				ZteApplication.bus.post(BusEvent.REFRESH_PLOT_EVENT);
				
				Util.showToast(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT);
				break;
			case NET_ERROR:
				progressView.setVisibility(View.GONE);
				Util.showToast(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT);
				break;
			case REFRESH_LIST_TEXT:
				progressView.setVisibility(View.GONE);
				CropTypeList.clear();
				CropBrandAllList.clear();
				CropTypeList.addAll(mListBean.getResult().getCropTypeList())  ;
				CropBrandAllList.addAll(mListBean.getResult().getCropBrandList())  ;
				getCropBrandList(CropTypeList,CropBrandAllList);
				CropBrandSmallList = CropBrandList.get(0);
				cropsBrandAdapter = new PlotCropAllListAdapter(mContext, CropBrandSmallList);
				cropsBrandSpinner.setAdapter(cropsBrandAdapter);
				
				
				cropTypeid = CropTypeList.get(0).getID();
				cropBrandid = CropBrandAllList.get(0).getID();
				cropUserid = UsersList.get(0).getID();
				cropAdditiveid= CropAdditiveList.get(0).getID();
				cropsTypeAdapter.notifyDataSetChanged();
				break;
			case 4:
				progressView.setVisibility(View.GONE);
				net_error.setVisibility(View.VISIBLE);
				Util.showToast(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT);
				break;
			}
			
			// listView.onRefreshComplete();
		}

	};

	public void onCreate(Bundle savedinstancestate) {
		super.onCreate(savedinstancestate);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_plot_add_edit);
		ZteApplication.bus.register(this);
		landName = getIntent().getStringExtra("landName");
		// initData();
		id=getIntent().getStringExtra("id");
		UsersList = (List<CropTypeBean>) getIntent().getSerializableExtra("userList");
		CropTypeList = (List<CropTypeBean>) getIntent().getSerializableExtra("cropType");
		CropBrandAllList = (List<CropTypeBean>) getIntent().getSerializableExtra("cropBrand");
		CropAdditiveList= (List<CropTypeBean>) getIntent().getSerializableExtra("CropAdditive");
		
		getCropBrandList(CropTypeList,CropBrandAllList);
		CropBrandSmallList =CropBrandList.get(0);
		getListData();
		initView();
	};

	private void getCropBrandList(List<CropTypeBean> CropTypeList ,List<CropTypeBean> CropBrandAllList) {
			// TODO Auto-generated method stub
			List<CropTypeBean> aa = null  ;
			CropBrandList = new ArrayList<List<CropTypeBean>>();
			for (int i = 0; i < CropTypeList.size(); i++) {
				aa = new ArrayList<CropTypeBean>();
				for (int j = 0; j < CropBrandAllList.size(); j++) {
					if (CropBrandAllList.get(j).getCropTypeID().equals(CropTypeList.get(i).getID())) {
						aa.add(CropBrandAllList.get(j));
						System.out.println(i+"==j=="+CropBrandAllList.get(j).getName());
					}
				}
				CropBrandList.add(aa);
			}
	}

	private void getListData() {
		// TODO Auto-generated method stub
		cropTypeid = CropTypeList.get(0).getID();
		cropBrandid = CropBrandAllList.get(0).getID();
		cropUserid = UsersList.get(0).getID();
		cropAdditiveid= CropAdditiveList.get(0).getID();
	}

	private void initView() {
		// TODO Auto-generated method stub
		leftTv = (TextView) findViewById(R.id.leftTv);
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setOnClickListener(this);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		ivTitleName.setText(getResources().getString(R.string.new_plot));
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setOnClickListener(this);
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);
		rightTv.setText(getResources().getString(R.string.save));
		rightTv.setVisibility(View.VISIBLE);
		progressView = (RelativeLayout) findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);
		net_error = (TextView) findViewById(R.id.net_error);
		net_error.setOnClickListener(this);
		editPlotName = (EditText) findViewById(R.id.edit_plot_name);
		editPlotInfo = (EditText) findViewById(R.id.edit_plot_info);
		editPlotSize= (EditText) findViewById(R.id.edit_plot_size);
		editPlotOutput= (EditText) findViewById(R.id.edit_plot_output);
		editPlotMark= (EditText) findViewById(R.id.edit_plot_mark);
		edit_additive= (EditText) findViewById(R.id.edit_additive);
		
		
		landNameTxt = (TextView) findViewById(R.id.land_name_txt);
		landNameTxt.setText(landName);
		userSpinner = (Spinner) findViewById(R.id.user_spinner);
		cropsBrandSpinner= (Spinner) findViewById(R.id.crops_brand_spinner);
		cropsTypeSpinner= (Spinner) findViewById(R.id.crops_type_spinner);
		additive_spinner= (Spinner) findViewById(R.id.additive_spinner);
		userAdapter = new PlotCropAllListAdapter(mContext, UsersList);
		cropsTypeAdapter = new PlotCropAllListAdapter(mContext, CropTypeList);
		cropsBrandAdapter = new PlotCropAllListAdapter(mContext, CropBrandSmallList);
		CropAdditiveListAdapter = new PlotCropAllListAdapter(mContext, CropAdditiveList);
		// 将adapter 添加到spinner中
		userSpinner.setAdapter(userAdapter);
		// 将adapter 添加到spinner中
		cropsTypeSpinner.setAdapter(cropsTypeAdapter);
		// 将adapter 添加到spinner中
		cropsBrandSpinner.setAdapter(cropsBrandAdapter);
		additive_spinner.setAdapter(CropAdditiveListAdapter);
		
		
		// 添加事件Spinner事件监听
		userSpinner.setOnItemSelectedListener(new SpinnerSelectedListener(1));
		cropsTypeSpinner.setOnItemSelectedListener(new SpinnerSelectedListener(2));
		cropsBrandSpinner.setOnItemSelectedListener(new SpinnerSelectedListener(3));
		additive_spinner.setOnItemSelectedListener(new SpinnerSelectedListener(4));
		
		selectTime =(TextView) findViewById(R.id.select_time);
		selectTime.setOnClickListener(this);
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = sDateFormat.format(new java.util.Date());
		selectTime.setText(date);
		
		add_crop_type=(ImageView) findViewById(R.id.add_crop_type);
		add_crop_brand=(ImageView) findViewById(R.id.add_crop_brand);
		add_crop_type.setOnClickListener(this);
		add_crop_brand.setOnClickListener(this);
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
			if (type==1) {
				cropUserid=UsersList.get(position).getID();
				
			}else if (type==2) {
				CropBrandSmallList=	CropBrandList.get(position);
				cropBrandid=CropBrandSmallList.get(0).getID();
				cropTypeid=CropTypeList.get(position).getID();
				cropsBrandAdapter = new PlotCropAllListAdapter(mContext, CropBrandSmallList);
				cropsBrandSpinner.setAdapter(cropsBrandAdapter);
				System.out.println("=="+cropBrandid);
			}else if (type==4) {
				cropAdditiveid= CropAdditiveList.get(position).getID();
			}else {
				cropBrandid=CropBrandSmallList.get(position).getID();
				System.out.println("=="+cropBrandid);
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	/**
	 * 初始化ViewPage
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent ;
		switch (v.getId()) {
		
		case R.id.net_error:
			initData();
			break;
		case R.id.leftTv:
			finish();
			break;
		case R.id.select_time:
			DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
					AddPlotInfoActivity.this, "");
			dateTimePicKDialog.dateTimePicKDialog(selectTime);
			break;
		case R.id.rightTv:
			if ("".equals(editPlotName.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.please_edit_data), Toast.LENGTH_SHORT);
				return ;
			}
			if ("".equals(editPlotInfo.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.please_edit_data), Toast.LENGTH_SHORT);
				return ;
			}
			if ("".equals(editPlotSize.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.please_edit_data), Toast.LENGTH_SHORT);
				return ;
			}
			if ("".equals(editPlotOutput.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.please_edit_data), Toast.LENGTH_SHORT);
				return ;
			}
			if ("".equals(edit_additive.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.please_edit_data), Toast.LENGTH_SHORT);
				return ;
			}
			
			uploadCropData();
			break;
		case R.id.add_crop_type:
			intent = new Intent(getApplicationContext(),AddCropTypeActivity.class);
			intent.putExtra("type", "1");
			startActivity(intent);
			break;
		case R.id.add_crop_brand:
			intent = new Intent(getApplicationContext(),AddCropBrandActivity.class);
			intent.putExtra("cropBrandList", (Serializable) CropTypeList);
			intent.putExtra("type", "2");
			startActivity(intent);
			break;
			
			
		default:
			break;
		}
	}

	private void uploadCropData() {
		// TODO Auto-generated method stub
		progressView.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair("userid",Constants.uid));
					nameValuePair.add(new BasicNameValuePair("baseid", id));
					nameValuePair.add(new BasicNameValuePair("landname",
							editPlotName.getText().toString()));
					nameValuePair.add(new BasicNameValuePair("content", editPlotInfo.getText().toString()));
					nameValuePair.add(new BasicNameValuePair("croptypeid", cropTypeid));
					nameValuePair.add(new BasicNameValuePair("cropbrandid", cropBrandid));
					nameValuePair.add(new BasicNameValuePair("sowuser", cropUserid));
					nameValuePair.add(new BasicNameValuePair("sowtime", selectTime.getText().toString()));
					nameValuePair.add(new BasicNameValuePair("size", editPlotSize.getText().toString()));
					nameValuePair.add(new BasicNameValuePair("annualoutput", editPlotOutput.getText().toString()));
					nameValuePair.add(new BasicNameValuePair("remark", editPlotMark.getText().toString()));
					nameValuePair.add(new BasicNameValuePair("concentration", edit_additive.getText().toString()));
					nameValuePair.add(new BasicNameValuePair("additiveid", cropAdditiveid));
					
					
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.UPLOAD_NEW_LAND_DATA_URL, nameValuePair);
					Logger.d("ddd", Constants.UPLOAD_NEW_LAND_DATA_URL
							+ nameValuePair.toString());
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mBean = gson
								.fromJson(sb.toString(), UploadResultBean.class);
						if ("0".equals(mBean.getStatus())) {
							Message msg = mHandler.obtainMessage(REFRESH_TEXT,getResources()
									.getString(R.string.upload_success));
							mHandler.sendMessage(msg);
						}  else if ("2".equals(mBean.getStatus())) {
							Message msg = mHandler.obtainMessage(
									NET_ERROR,
									getResources().getString(R.string.plot_name_already_exists)
									);
							mHandler.sendMessage(msg);
						} else {
							Message msg = mHandler.obtainMessage(2,
									getResources().getString(R.string.upload_error));
							mHandler.sendMessage(msg);
						}
					} else {
						Logger.d("ddd", "sb==null");
						Message msg = mHandler.obtainMessage(2, getResources()
								.getString(R.string.upload_error));
						mHandler.sendMessage(msg);
					}
				} else {
					Message msg = mHandler.obtainMessage(2, getResources()
							.getString(R.string.upload_error));
					mHandler.sendMessage(msg);
				}
			};
		}.start();
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
						mListBean = gson.fromJson(sb.toString(),
								PlotAllListInfo.class);
						if (mListBean != null && "0".equals(mListBean.getStatus())) {
							Message msg = mHandler.obtainMessage(REFRESH_LIST_TEXT);
							mHandler.sendMessage(msg);
						}else {
							Message msg = mHandler.obtainMessage(
									4,
									getResources().getString(
											R.string.net_data_error));
							mHandler.sendMessage(msg);
						}
					} else {
						Logger.d("ddd", "sb==null");

						Message msg = mHandler.obtainMessage(4, getResources()
								.getString(R.string.net_data_error));
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
		System.out.println("====================");
		if (event == BusEvent.REFRESH_ADD_CROPS_DATA_EVENT) {
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