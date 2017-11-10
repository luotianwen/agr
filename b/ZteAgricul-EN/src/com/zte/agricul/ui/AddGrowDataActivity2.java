package com.zte.agricul.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zte.agricul.zh.R;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.CropIndexBean;
import com.zte.agricul.fragment.PlotCropsGrowFragment;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.ResolveJson;
import com.zte.agricul.util.Util;
import com.zte.agricul.view.DateTimePickDialogUtil;

public class AddGrowDataActivity2 extends BaseActivity implements
		OnClickListener {
	private TextView leftTv, ivTitleName, rightTv;
	private ListView mListView;
	List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
	public HorizontalScrollView mTouchView;
	// 装入所有的HScrollView
	private TextView time_selected, land_name;
	private EditText ievEditView1, ievEditView2, ievEditView3;
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private RelativeLayout progressView;

	private boolean isFinish = false;
	public static boolean isRefresh = false;
	AddGrowDataAdapter mAdapter;
	List<CropIndexBean>  cropIndex  = new ArrayList<CropIndexBean>();
	
	private  List<String>  upLoadData = new ArrayList<String>() ;
	
	private EditText[] editData = new EditText[20]; 
	private LinearLayout[] layout = new LinearLayout[20]; 
	private TextView[] dataName = new TextView[20]; 
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isFinish) {
				return;
			}
			progressView.setVisibility(View.GONE);
			switch (msg.what) {
			case REFRESH_TEXT:
				Util.showToast(getApplicationContext(), getResources().getString(R.string.upload_success),
						Toast.LENGTH_SHORT);
				isRefresh = true;
				finish();
				break;

			case NET_ERROR:
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_grow_data);
		cropIndex.addAll(PlotCropsGrowFragment.cropIndex);
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
		ivTitleName.setText(R.string.add_grow_data);
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setVisibility(View.VISIBLE);
		rightTv.setOnClickListener(this);
		rightTv.setText(R.string.save);
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);

		time_selected = (TextView) findViewById(R.id.time_selected);
		time_selected.setOnClickListener(this);
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = sDateFormat.format(new java.util.Date());
		time_selected.setText(date);
		land_name = (TextView) findViewById(R.id.land_name);
		land_name.setText(getIntent().getStringExtra("landName"));
		
		
		layout[0] = (LinearLayout) findViewById(R.id.layout0); 
		layout[1] = (LinearLayout) findViewById(R.id.layout1); 
		layout[2] = (LinearLayout) findViewById(R.id.layout2); 
		layout[3] = (LinearLayout) findViewById(R.id.layout3); 
		layout[4] = (LinearLayout) findViewById(R.id.layout4);
		layout[5] = (LinearLayout) findViewById(R.id.layout5); 
		layout[6] = (LinearLayout) findViewById(R.id.layout6); 
		layout[7] = (LinearLayout) findViewById(R.id.layout7); 
		layout[8] = (LinearLayout) findViewById(R.id.layout8); 
		layout[9] = (LinearLayout) findViewById(R.id.layout9);
		layout[10] = (LinearLayout) findViewById(R.id.layout10); 
		layout[11] = (LinearLayout) findViewById(R.id.layout11); 
		layout[12] = (LinearLayout) findViewById(R.id.layout12); 
		layout[13] = (LinearLayout) findViewById(R.id.layout13); 
		layout[14] = (LinearLayout) findViewById(R.id.layout14);
		layout[15] = (LinearLayout) findViewById(R.id.layout15); 
		layout[16] = (LinearLayout) findViewById(R.id.layout16); 
		layout[17] = (LinearLayout) findViewById(R.id.layout17); 
		layout[18] = (LinearLayout) findViewById(R.id.layout18); 
		layout[19] = (LinearLayout) findViewById(R.id.layout19);
		
		editData[0] = (EditText) findViewById(R.id.crop_data0); 
		editData[1] = (EditText) findViewById(R.id.crop_data1); 
		editData[2] = (EditText) findViewById(R.id.crop_data2); 
		editData[3] = (EditText) findViewById(R.id.crop_data3); 
		editData[4] = (EditText) findViewById(R.id.crop_data4); 
		editData[5] = (EditText) findViewById(R.id.crop_data5); 
		editData[6] = (EditText) findViewById(R.id.crop_data6); 
		editData[7] = (EditText) findViewById(R.id.crop_data7); 
		editData[8] = (EditText) findViewById(R.id.crop_data8); 
		editData[9] = (EditText) findViewById(R.id.crop_data9); 
		editData[10] = (EditText) findViewById(R.id.crop_data10); 
		editData[11] = (EditText) findViewById(R.id.crop_data11); 
		editData[12] = (EditText) findViewById(R.id.crop_data12); 
		editData[13] = (EditText) findViewById(R.id.crop_data13); 
		editData[14] = (EditText) findViewById(R.id.crop_data14); 
		editData[15] = (EditText) findViewById(R.id.crop_data15); 
		editData[16] = (EditText) findViewById(R.id.crop_data16); 
		editData[17] = (EditText) findViewById(R.id.crop_data17); 
		editData[18] = (EditText) findViewById(R.id.crop_data18); 
		editData[19] = (EditText) findViewById(R.id.crop_data19); 
		
		
		dataName[0] = (TextView) findViewById(R.id.data_name0); 
		dataName[1] = (TextView) findViewById(R.id.data_name1); 
		dataName[2] = (TextView) findViewById(R.id.data_name2); 
		dataName[3] = (TextView) findViewById(R.id.data_name3); 
		dataName[4] = (TextView) findViewById(R.id.data_name4); 
		dataName[5] = (TextView) findViewById(R.id.data_name5); 
		dataName[6] = (TextView) findViewById(R.id.data_name6); 
		dataName[7] = (TextView) findViewById(R.id.data_name7); 
		dataName[8] = (TextView) findViewById(R.id.data_name8); 
		dataName[9] = (TextView) findViewById(R.id.data_name9); 
		dataName[10] = (TextView) findViewById(R.id.data_name10); 
		dataName[11] = (TextView) findViewById(R.id.data_name11); 
		dataName[12] = (TextView) findViewById(R.id.data_name12); 
		dataName[13] = (TextView) findViewById(R.id.data_name13); 
		dataName[14] = (TextView) findViewById(R.id.data_name14); 
		dataName[15] = (TextView) findViewById(R.id.data_name15); 
		dataName[16] = (TextView) findViewById(R.id.data_name16); 
		dataName[17] = (TextView) findViewById(R.id.data_name17); 
		dataName[18] = (TextView) findViewById(R.id.data_name18); 
		dataName[19] = (TextView) findViewById(R.id.data_name19); 
		
		
		for (int i = 0; i < 20; i++) {
			layout[i].setVisibility(View.GONE);
			
		}
		
		for (int i = 0; i < cropIndex.size(); i++) {
			layout[i].setVisibility(View.VISIBLE);
			dataName[i].setText(cropIndex.get(i).getIndexName()+":");
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
			imm.hideSoftInputFromWindow(land_name.getWindowToken(), 0); // 强制隐藏键盘
			
			for (int i = 0; i < cropIndex.size(); i++) {
				if ("".equals(editData[i].getText().toString())) {
					Util.showToast(getApplicationContext(), getResources()
							.getString(R.string.upload_no_data), Toast.LENGTH_SHORT);
					return ;
				}
			}

			
			List<String> imageUrlList = new ArrayList<String>();
			uploadImg(getIntent().getStringExtra("landid"), getIntent()
					.getStringExtra("cropTypeId"), cropIndex, imageUrlList);

			break;
		case R.id.time_selected:
			DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
					AddGrowDataActivity2.this, "");
			dateTimePicKDialog.dateTimePicKDialog(time_selected);
			break;

		default:
			break;
		}
	}

	/***
	 * Fault_Nessage_ID 故障信息id PowerStation_ID 电站id UserID 电站管理人员id Type
	 * 提交数据类型（1-初步诊断意见 2-处理结果） Fault 初步诊断意见/处理结果
	 * http://117.146.89.194:8032/SubmitGrowthData
	 * .aspx?landid=1&croptypeid=1&data1
	 * =1.1&data2=1.2&data3=1.3&userid=2&time=2016-7-30
	 */
	private void uploadImg(final String landid, final String croptypeid,
			final List<CropIndexBean>  dataBeans,
			final List<String> imageUrlList) {
		// TODO Auto-generated method stub
		progressView.setVisibility(View.VISIBLE);
		/*upLoadData = new ArrayList<String>() ;
		for (int i = 0; i < 20; i++) {
			upLoadData.add("data"+(i+1));
		}*/
		new Thread() {
			public void run() {
				List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
//				final Map<String, String> map = new HashMap<String, String>();
				
				
				
				//for (int i = 0; i < upLoadData.size(); i++) {
					for (int j = 0; j < dataBeans.size(); j++) {
						nameValuePair.add(new BasicNameValuePair("data"+(j+1),
								editData[j].getText().toString()));

						//if (upLoadData.get(i).equals(dataBeans.get(j).getDataName())) {
//							map.put(dataBeans.get(j).getDataName(), editData[j].getText().toString());
							
							/*nameValuePair.add(new BasicNameValuePair(dataBeans.get(j).getDataName(),
									editData[j].getText().toString()));*/
							System.out.println(dataBeans.get(j).getDataName()+"==="+editData[j].getText().toString());
							
							//upLoadData.remove(i);
						//}
					}
				//}
				
				/*for (int i = 0; i < upLoadData.size(); i++) {
//					map.put(upLoadData.get(i), "");
					nameValuePair.add(new BasicNameValuePair(upLoadData.get(i),
							""));
				}*/
				nameValuePair.add(new BasicNameValuePair("landid", landid));
				nameValuePair.add(new BasicNameValuePair("croptypeid", croptypeid));
				nameValuePair.add(new BasicNameValuePair("userid", Constants.uid));
				nameValuePair.add(new BasicNameValuePair("time", time_selected.getText().toString()));
//				map.put("landid", landid);
//				map.put("croptypeid", croptypeid);
//				map.put("userid", Constants.uid);
//				map.put("time", time_selected.getText().toString());
				try {
					// 登陆流程时要处理
					StringBuffer sb = HttpUtil
							.getDataFromServer(Constants.UPLOAD_GROWTH_DATA_URL,nameValuePair);
					Logger.d("aaaa", "url==" + Constants.UPLOAD_GROWTH_DATA_URL
							+ nameValuePair.toString());
					if (null != sb) {
						Logger.d("aaaa", "======" + sb.toString());
						String sucess = ResolveJson.uploadResultParse(sb
								.toString());
						if ("0".equals(sucess)) {
							Message msg = mHandler.obtainMessage(REFRESH_TEXT);
							mHandler.sendMessage(msg);
						} else {
							Message msg = mHandler.obtainMessage(2,
									getResources().getString(R.string.upload_error_retry));
							mHandler.sendMessage(msg);
						}
					} else {
						Message msg = mHandler.obtainMessage(2,
								getResources().getString(R.string.upload_error_retry));
						mHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			};
		}.start();
	}

	class AddGrowDataAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private Context mContext;
//		private Map<String, String> editorValue = new HashMap<String, String>();
//		private static HashMap<Integer, Boolean> isSelected;

		public AddGrowDataAdapter(Context context ) {
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (cropIndex != null) {
				return cropIndex.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		private Integer index = -1;

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Log.d("zhang", "position = " + position);
			final ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_add_grow_data, null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView.findViewById(R.id.text_name);
				holder.numEdit = (EditText) convertView.findViewById(R.id.edit_data);
				holder.numEdit.setTag(position);
				holder.numEdit.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_UP) {
							index = (Integer) v.getTag();
						}
						return false;
					}
				});

				class MyTextWatcher implements TextWatcher {

					public MyTextWatcher(ViewHolder holder) {
						mHolder = holder;
					}

					private ViewHolder mHolder;

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						if (s != null && !"".equals(s.toString())) {
							int position = (Integer) mHolder.numEdit.getTag();
							cropIndex.get(position).setData(s.toString());
							// 当EditText数据发生改变的时候存到data变量中
//							mData.get(position).put("list_item_inputvalue",
//									s.toString());
						}
					}
				}
				holder.numEdit.addTextChangedListener(new MyTextWatcher(holder));
				
				
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.numEdit.setTag(position);
			}
			
			holder.textView.setText(cropIndex.get(position).getIndexName()+":");
			
			holder.numEdit.clearFocus();
			if (index != -1 && index == position) {
				holder.numEdit.requestFocus();
			}
			return convertView;
		}

		public class ViewHolder {
			TextView textView;
			EditText numEdit;

		}

	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isFinish = true;
	}
}