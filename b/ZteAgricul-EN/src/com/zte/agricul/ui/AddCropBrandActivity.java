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
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.zte.agricul.zh.R;
import com.zte.agricul.adapter.PlotCropAllListAdapter;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.CropTypeBean;
import com.zte.agricul.bean.UploadResultBean;
import com.zte.agricul.ui.AddPlotInfoActivity.SpinnerSelectedListener;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.Util;

public class AddCropBrandActivity extends BaseActivity implements
		OnClickListener {
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;

	private TextView leftTv, ivTitleName, rightTv;

	private String type;
	private EditText edit_plot_name;
	private TextView plot_name;
	private boolean isFinish = false;
	private UploadResultBean mBean;
	private RelativeLayout progressView;
	private List<CropTypeBean> CropBrandList;
	private PlotCropAllListAdapter cropsBrandAdapter;
	private Spinner cropsBrandSpinner;
	private String croptypeid = "";
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_brand_type);
		type = getIntent().getStringExtra("type");
		CropBrandList = (List<CropTypeBean>) getIntent().getSerializableExtra(
				"cropBrandList");
		croptypeid = CropBrandList.get(0).getID();
		initView();
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

		edit_plot_name = (EditText) findViewById(R.id.edit_plot_name);
		plot_name = (TextView) findViewById(R.id.plot_name);
		ivTitleName.setText(getResources().getString(R.string.add_crop_brand));
		edit_plot_name.setHint(getResources().getString(R.string.enter_crop_brand_name));
		plot_name.setText(getResources().getString(R.string.brand_name));
		cropsBrandSpinner = (Spinner) findViewById(R.id.crops_type_spinner);
		cropsBrandAdapter = new PlotCropAllListAdapter(mContext, CropBrandList);
		cropsBrandSpinner.setAdapter(cropsBrandAdapter);
		cropsBrandSpinner
				.setOnItemSelectedListener(new SpinnerSelectedListener(3));
	}

	// 使用数组形式操作
	class SpinnerSelectedListener implements OnItemSelectedListener {
		private int type;

		public SpinnerSelectedListener(int type) {
			// TODO Auto-generated constructor stub
			this.type = type;
		}

		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			// view.setText("你的血型是：" + m[arg2]);
			croptypeid = CropBrandList.get(position).getID();
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.leftTv:
			finish();
			break;
		case R.id.rightTv:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(edit_plot_name.getWindowToken(), 0); // 强制隐藏键盘

			if ("".equals(edit_plot_name.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.please_edit),
						Toast.LENGTH_SHORT);
				return;
			}
			uploadCropData(Constants.UPLOAD_CROPS_BRAND_DATA_URL,
					"cropbrandname",croptypeid);

			break;
		default:
			break;
		}
	}

	private void uploadCropData(final String url, final String baseName,
			final String croptypeid) {
		// TODO Auto-generated method stub
		progressView.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair(baseName,
							edit_plot_name.getText().toString()));

					nameValuePair.add(new BasicNameValuePair("croptypeid",
							croptypeid));

					StringBuffer sb = HttpUtil.getDataFromServer(url,
							nameValuePair);
					Logger.d("ddd", url + nameValuePair.toString());
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mBean = gson.fromJson(sb.toString(),
								UploadResultBean.class);
						if ("0".equals(mBean.getStatus())) {
							Message msg = mHandler.obtainMessage(
									REFRESH_TEXT,
									getResources().getString(
											R.string.upload_success));
							mHandler.sendMessage(msg);
						} else {
							Message msg = mHandler.obtainMessage(
									2,
									getResources().getString(
											R.string.upload_error));
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isFinish = true;
	}
}
