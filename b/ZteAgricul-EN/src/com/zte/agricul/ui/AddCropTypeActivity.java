package com.zte.agricul.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zte.agricul.zh.R;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.PlotAllListInfo;
import com.zte.agricul.bean.UploadResultBean;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.Util;

public class AddCropTypeActivity extends BaseActivity implements
			OnClickListener {
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;

	private TextView leftTv, ivTitleName, rightTv;
	
	private String type;
	private EditText  edit_plot_name,edit_plot_brand_name; 
	private TextView plot_name ;
	private boolean isFinish = false;
	private UploadResultBean mBean ;
	private RelativeLayout progressView;

	private EditText[] editData = new EditText[20]; 
	private LinearLayout[] layout = new LinearLayout[20]; 
	private ImageView[] hideBrand = new ImageView[20]; 
	
	private int showNum = 1 ;
	
	private LinearLayout addView;
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
				ZteApplication.bus.post(BusEvent.REFRESH_ADD_CROPS_DATA_EVENT);
				
				Util.showToast(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT);
				break;
			case NET_ERROR:
				progressView.setVisibility(View.GONE);
				Util.showToast(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT);
				break;
			}
			// listView.onRefreshComplete();
		}

	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_crop_type);
		type = getIntent().getStringExtra("type");
		initView();
		showView(showNum);
	}
	private void initView() {
		// TODO Auto-generated method stub
		progressView = (RelativeLayout) findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);
		
		leftTv = (TextView) findViewById(R.id.leftTv);
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setOnClickListener(this);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		
		
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setOnClickListener(this);
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);
		rightTv.setText(getResources().getString(R.string.save));
		rightTv.setVisibility(View.VISIBLE);
		
		
		edit_plot_name =(EditText) findViewById(R.id.edit_plot_name);
		plot_name =(TextView) findViewById(R.id.plot_name);
		edit_plot_brand_name=(EditText) findViewById(R.id.edit_plot_brand_name);
		if ("1".equals(type)) {
			ivTitleName.setText(getResources().getString(R.string.add_crop));
			edit_plot_name.setHint(getResources().getString(R.string.enter_crop_name));
			plot_name.setText(getResources().getString(R.string.crop_name));
		}else {
			ivTitleName.setText(getResources().getString(R.string.add_crop_brand));
			edit_plot_name.setHint(getResources().getString(R.string.enter_crop_brand_name));
			plot_name.setText(getResources().getString(R.string.brand_name));
		}
		
		addView =(LinearLayout) findViewById(R.id.add_view);
		addView.setOnClickListener(this);
		editData[0] = (EditText) findViewById(R.id.edit_plot_data1); 
		editData[1] = (EditText) findViewById(R.id.edit_plot_data2); 
		editData[2] = (EditText) findViewById(R.id.edit_plot_data3); 
		editData[3] = (EditText) findViewById(R.id.edit_plot_data4); 
		editData[4] = (EditText) findViewById(R.id.edit_plot_data5); 
		editData[5] = (EditText) findViewById(R.id.edit_plot_data6); 
		editData[6] = (EditText) findViewById(R.id.edit_plot_data7); 
		editData[7] = (EditText) findViewById(R.id.edit_plot_data8); 
		editData[8] = (EditText) findViewById(R.id.edit_plot_data9); 
		editData[9] = (EditText) findViewById(R.id.edit_plot_data10); 
		editData[10] = (EditText) findViewById(R.id.edit_plot_data11); 
		editData[11] = (EditText) findViewById(R.id.edit_plot_data12); 
		editData[12] = (EditText) findViewById(R.id.edit_plot_data13); 
		editData[13] = (EditText) findViewById(R.id.edit_plot_data14); 
		editData[14] = (EditText) findViewById(R.id.edit_plot_data15); 
		editData[15] = (EditText) findViewById(R.id.edit_plot_data16); 
		editData[16] = (EditText) findViewById(R.id.edit_plot_data17); 
		editData[17] = (EditText) findViewById(R.id.edit_plot_data18); 
		editData[18] = (EditText) findViewById(R.id.edit_plot_data19); 
		editData[19] = (EditText) findViewById(R.id.edit_plot_data20); 
		
		layout[0] = (LinearLayout) findViewById(R.id.layout1); 
		layout[1] = (LinearLayout) findViewById(R.id.layout2); 
		layout[2] = (LinearLayout) findViewById(R.id.layout3); 
		layout[3] = (LinearLayout) findViewById(R.id.layout4); 
		layout[4] = (LinearLayout) findViewById(R.id.layout5);
		layout[5] = (LinearLayout) findViewById(R.id.layout6); 
		layout[6] = (LinearLayout) findViewById(R.id.layout7); 
		layout[7] = (LinearLayout) findViewById(R.id.layout8); 
		layout[8] = (LinearLayout) findViewById(R.id.layout9); 
		layout[9] = (LinearLayout) findViewById(R.id.layout10);
		layout[10] = (LinearLayout) findViewById(R.id.layout11); 
		layout[11] = (LinearLayout) findViewById(R.id.layout12); 
		layout[12] = (LinearLayout) findViewById(R.id.layout13); 
		layout[13] = (LinearLayout) findViewById(R.id.layout14); 
		layout[14] = (LinearLayout) findViewById(R.id.layout15);
		layout[15] = (LinearLayout) findViewById(R.id.layout16); 
		layout[16] = (LinearLayout) findViewById(R.id.layout17); 
		layout[17] = (LinearLayout) findViewById(R.id.layout18); 
		layout[18] = (LinearLayout) findViewById(R.id.layout19); 
		layout[19] = (LinearLayout) findViewById(R.id.layout20);
		
		hideBrand[0] =(ImageView) findViewById(R.id.hide_crop_brand1);
		hideBrand[1] =(ImageView) findViewById(R.id.hide_crop_brand2);
		hideBrand[2] =(ImageView) findViewById(R.id.hide_crop_brand3);
		hideBrand[3] =(ImageView) findViewById(R.id.hide_crop_brand4);
		hideBrand[4] =(ImageView) findViewById(R.id.hide_crop_brand5);
		hideBrand[5] =(ImageView) findViewById(R.id.hide_crop_brand6);
		hideBrand[6] =(ImageView) findViewById(R.id.hide_crop_brand7);
		hideBrand[7] =(ImageView) findViewById(R.id.hide_crop_brand8);
		hideBrand[8] =(ImageView) findViewById(R.id.hide_crop_brand9);
		hideBrand[9] =(ImageView) findViewById(R.id.hide_crop_brand10);
		hideBrand[10] =(ImageView) findViewById(R.id.hide_crop_brand11);
		hideBrand[11] =(ImageView) findViewById(R.id.hide_crop_brand12);
		hideBrand[12] =(ImageView) findViewById(R.id.hide_crop_brand13);
		hideBrand[13] =(ImageView) findViewById(R.id.hide_crop_brand14);
		hideBrand[14] =(ImageView) findViewById(R.id.hide_crop_brand15);
		hideBrand[15] =(ImageView) findViewById(R.id.hide_crop_brand16);
		hideBrand[16] =(ImageView) findViewById(R.id.hide_crop_brand17);
		hideBrand[17] =(ImageView) findViewById(R.id.hide_crop_brand18);
		hideBrand[18] =(ImageView) findViewById(R.id.hide_crop_brand19);
		hideBrand[19] =(ImageView) findViewById(R.id.hide_crop_brand20);
		for (int i = 0; i < hideBrand.length; i++) {
			hideBrand[i].setOnClickListener(new monClickListener(i));
		}
	}
	
	class monClickListener implements OnClickListener {
		private int position;

		monClickListener(int pos) {
			position = pos;
		}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			showNum--;
			showView(showNum);
		}
	};
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.leftTv:
			finish();
			break;
		case R.id.rightTv:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
			imm.hideSoftInputFromWindow(edit_plot_name.getWindowToken(), 0); //Ç¿ÖÆÒþ²Ø¼üÅÌ
			
			if ("".equals(edit_plot_name.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.please_edit_name), Toast.LENGTH_SHORT);
				return ;
			}
			if ("".equals(edit_plot_brand_name.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.please_edit_brand), Toast.LENGTH_SHORT);
				return ;
			}
			
			if ("".equals(editData[0].getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.please_edit_index_name), Toast.LENGTH_SHORT);
				return ;
			}
				uploadCropData(Constants.UPLOAD_NEW_CROPS_DATA_URL,"cropname");
			
			break;
		case R.id.add_view:
			showNum++;
			showView(showNum);
			break;
			
		default:
			break;
		}
	}
	private void uploadCropData(final String url,final String baseName) {
		// TODO Auto-generated method stub
		progressView.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair(baseName,edit_plot_name.getText().toString()));
					nameValuePair.add(new BasicNameValuePair("cropbrandname",edit_plot_brand_name.getText().toString()));
					
					nameValuePair.add(new BasicNameValuePair("data1",editData[0].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data2",editData[1].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data3",editData[2].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data4",editData[3].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data5",editData[4].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data6",editData[5].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data7",editData[6].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data8",editData[7].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data9",editData[8].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data10",editData[9].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data11",editData[10].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data12",editData[11].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data13",editData[12].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data14",editData[13].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data15",editData[14].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data16",editData[15].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data17",editData[16].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data18",editData[17].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data19",editData[18].getText().toString()));
					nameValuePair.add(new BasicNameValuePair("data20",editData[19].getText().toString()));
					StringBuffer sb = HttpUtil.getDataFromServer(
							url, nameValuePair);
					Logger.d("ddd", url+ nameValuePair.toString());
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mBean = gson
								.fromJson(sb.toString(), UploadResultBean.class);
						if ("0".equals(mBean.getStatus())) {
							Message msg = mHandler.obtainMessage(REFRESH_TEXT,getResources()
									.getString(R.string.upload_success));
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
	
	private void showView(int num){
		for (int i = 0; i < 20; i++) {
			if (i<=(num-1)) {
				layout[i].setVisibility(View.VISIBLE);
			}else {
				layout[i].setVisibility(View.GONE);
			}
			
		}
		
		for (int i = 0; i < 20; i++) {
			hideBrand[i].setVisibility(View.INVISIBLE);
		}
		
		
		for (int i = 0; i < num; i++) {
			if(i==(num-1)){
				System.out.println("==="+num);
				hideBrand[i].setVisibility(View.VISIBLE);
			}else {
				hideBrand[i].setVisibility(View.INVISIBLE);
			}
			if (i==0) {
				hideBrand[i].setVisibility(View.INVISIBLE);
			}
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isFinish = true;
	}
}
