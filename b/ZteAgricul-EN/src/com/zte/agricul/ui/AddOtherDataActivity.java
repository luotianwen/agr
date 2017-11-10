package com.zte.agricul.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.king.photo.util.Bimp;
import com.king.photo.util.FileUtils;
import com.king.photo.util.ImageItem;
import com.king.photo.util.PublicWay;
import com.zte.agricul.zh.R;
import com.zte.agricul.adapter.ProcessTypeAdapter;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.ProcessInfo;
import com.zte.agricul.ui.AddOtherDataActivity.GridAdapter;
import com.zte.agricul.ui.AddOtherDataActivity.GridAdapter2;
import com.zte.agricul.ui.AddOtherDataActivity.GridAdapter2.ViewHolder;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.ResolveJson;
import com.zte.agricul.util.Util;
import com.zte.agricul.view.DateTimePickDialogUtil;
import com.zte.agricul.view.GridViewToScrollView;
import com.zte.agricul.view.HorizontalListView;

public class AddOtherDataActivity extends BaseActivity implements
		OnClickListener {
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private final int REFRESH_UPLOAD_TEXT1 = 3;
	private final int REFRESH_UPLOAD_TEXT2 = 4;
	private TextView leftTv, ivTitleName, rightTv;
	private TextView time_selected;

	private TextView net_error;
	private RelativeLayout progressView;
	private ProcessInfo mBean;
	private boolean isFinish = false;
	private HorizontalListView list_process_type;
	private ProcessTypeAdapter madapter;

	private GridViewToScrollView noScrollgridview, noScrollgridview2;
	private GridAdapter adapter;
	private GridAdapter2 adapter2;
	public static Bitmap bimap;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private LinearLayout layout2;
	private int type = 1;

	private TextView saveBtn1, saveBtn2;
	private EditText growth_edit1, growth_edit2;
	private TextView land_name1, land_name2;
	private String growthproid = "";
	private String timetitle = "";
	private int  photoSelect = 1 ; 
	public static boolean isRefresh = false;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isFinish) {
				return;
			}
			progressView.setVisibility(View.GONE);
			switch (msg.what) {
			case REFRESH_TEXT:
				madapter = new ProcessTypeAdapter(getApplicationContext(),
						mBean.getResult());
				growthproid = mBean.getResult().get(0).getID();
				timetitle = mBean.getResult().get(0).getName() + getResources().getString(R.string.date);
				list_process_type.setAdapter(madapter);
				net_error.setVisibility(View.GONE);

				break;
			case NET_ERROR:
				net_error.setVisibility(View.VISIBLE);
				Util.showToast(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT);
				break;

			}

		}
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isFinish) {
				return;
			}
			progressView.setVisibility(View.GONE);
			switch (msg.what) {
			case REFRESH_UPLOAD_TEXT1:
				Util.showToast(getApplicationContext(), getResources().getString(R.string.upload_success),
						Toast.LENGTH_SHORT);
				saveBtn1.setClickable(false);
				saveBtn1.setBackgroundResource(R.drawable.round_conner_line_grey_bg);
				noScrollgridview.setOnItemClickListener(null);
				growth_edit1.setEnabled(false);
				// BusEvent.REFRESH_GROWTH_LOG_EVENT.data = "refresh";
				// ZteApplication.bus.post(BusEvent.REFRESH_GROWTH_LOG_EVENT);
				isRefresh = true;
				finish();
				break;
			case REFRESH_UPLOAD_TEXT2:
				Util.showToast(getApplicationContext(), getResources().getString(R.string.upload_success),
						Toast.LENGTH_SHORT);
				saveBtn2.setClickable(false);
				saveBtn2.setBackgroundResource(R.drawable.round_conner_line_grey_bg);
				noScrollgridview2.setOnItemClickListener(null);
				growth_edit2.setEnabled(false);
				// BusEvent.REFRESH_GROWTH_LOG_EVENT.data = "refresh";
				// ZteApplication.bus.post(BusEvent.REFRESH_GROWTH_LOG_EVENT);
				isRefresh = true;
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
		setContentView(R.layout.activity_add_grow_process);
		ZteApplication.bus.register(getApplicationContext());
		initView();
		initViewData();
		initData();
		Init();
	}

	private void initViewData() {
		// TODO Auto-generated method stub
		saveBtn1 = (TextView) findViewById(R.id.saveBtn1);
		saveBtn2 = (TextView) findViewById(R.id.saveBtn2);
		saveBtn1.setOnClickListener(this);
		saveBtn2.setOnClickListener(this);
		land_name1 = (TextView) findViewById(R.id.land_name1);
		land_name2 = (TextView) findViewById(R.id.land_name2);
		growth_edit1 = (EditText) findViewById(R.id.growth_edit1);
		growth_edit2 = (EditText) findViewById(R.id.growth_edit2);
		land_name1.setText(getIntent().getStringExtra("landName"));
	}

	private void initView() {
		net_error = (TextView) findViewById(R.id.net_error);
		net_error.setOnClickListener(this);
		progressView = (RelativeLayout) findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);

		leftTv = (TextView) findViewById(R.id.leftTv);
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setOnClickListener(this);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		if (1 == getIntent().getIntExtra("type", 0)) {
			ivTitleName.setText(R.string.edit_other_data);
		} else {
			ivTitleName.setText(R.string.add_other_data);
		}
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setVisibility(View.GONE);
		rightTv.setOnClickListener(this);
		rightTv.setText(R.string.save);
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);

		time_selected = (TextView) findViewById(R.id.time_selected);
		time_selected.setOnClickListener(this);
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = sDateFormat.format(new java.util.Date());
		time_selected.setText(date);

		list_process_type = (HorizontalListView) findViewById(R.id.list_process_type);
		// adapter = new ProcessTypeAdapter(getApplicationContext(),
		// getHListData());
		// list_process_type.setAdapter(adapter);
		list_process_type.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				growthproid = mBean.getResult().get(position).getID();
				timetitle = mBean.getResult().get(position).getName() + getResources().getString(R.string.date);
				madapter.setSelected(position);
			}
		});

	}

	public void Init() {

		pop = new PopupWindow(AddOtherDataActivity.this);

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo(photoSelect);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(AddOtherDataActivity.this,
						AlbumActivity.class);
				intent.putExtra("type", type);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in,
						R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

		noScrollgridview = (GridViewToScrollView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.hideSoftInputFromWindow(growth_edit1.getWindowToken(), 0); //强制隐藏键盘  
				type = 1;
				photoSelect =1 ;
				if (arg2 == Bimp.tempSelectBitmap1.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(AnimationUtils.loadAnimation(
							AddOtherDataActivity.this,
							R.anim.activity_translate_in));
					pop.showAtLocation(noScrollgridview, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(AddOtherDataActivity.this,
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					intent.putExtra("type", type);
					startActivity(intent);
				}
			}
		});

		noScrollgridview2 = (GridViewToScrollView) findViewById(R.id.noScrollgridview2);
		noScrollgridview2.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter2 = new GridAdapter2(this);
		adapter2.update();
		noScrollgridview2.setAdapter(adapter2);
		noScrollgridview2.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.hideSoftInputFromWindow(growth_edit1.getWindowToken(), 0); //强制隐藏键盘  
				type = 2;
				photoSelect =2 ;
				if (arg2 == Bimp.tempSelectBitmap2.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(AnimationUtils.loadAnimation(
							AddOtherDataActivity.this,
							R.anim.activity_translate_in));
					pop.showAtLocation(noScrollgridview2, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(AddOtherDataActivity.this,
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					intent.putExtra("type", type);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.leftTv:
			finish();
			break;
		case R.id.rightTv:
			Util.showToast(getApplicationContext(),
					getResources().getString(R.string.save), Toast.LENGTH_SHORT);
			break;

		case R.id.time_selected:
			DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
					AddOtherDataActivity.this, "");
			dateTimePicKDialog.dateTimePicKDialog(time_selected);
			break;
		case R.id.saveBtn1:
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
			im.hideSoftInputFromWindow(growth_edit1.getWindowToken(), 0); //强制隐藏键盘  
			if ("".equals(growth_edit1.getText().toString())
					&& Bimp.tempSelectBitmap1.size() == 0) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.please_edit_data),Toast.LENGTH_SHORT);
				return;
			}
			List<String> imageUrlList = new ArrayList<String>();
			for (int i = 0; i < Bimp.tempSelectBitmap1.size(); i++) {
				imageUrlList.add(Bimp.tempSelectBitmap1.get(i).getImagePath());
			}
			uploadImg(1, growth_edit1.getText().toString(), getIntent().getStringExtra("landid"), imageUrlList);
			break;
		case R.id.saveBtn2:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
			imm.hideSoftInputFromWindow(growth_edit2.getWindowToken(), 0); //强制隐藏键盘  
			if ("".equals(growth_edit2.getText().toString())
					&& Bimp.tempSelectBitmap2.size() == 0) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.please_edit_data),Toast.LENGTH_SHORT);
				return;
			}
			List<String> imageUrlList2 = new ArrayList<String>();
			for (int i = 0; i < Bimp.tempSelectBitmap2.size(); i++) {
				imageUrlList2.add(Bimp.tempSelectBitmap2.get(i).getImagePath());
			}
			uploadImg(2, growth_edit2.getText().toString(), getIntent().getStringExtra("landid"), imageUrlList2);
			break;

		default:
			break;
		}
	}

	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap1.size() == PublicWay.num) {
				return PublicWay.num;
			}
			return (Bimp.tempSelectBitmap1.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.tempSelectBitmap1.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap1
						.get(position).getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler1 = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max1 == Bimp.tempSelectBitmap1.size()) {
							Message message = new Message();
							message.what = 1;
							handler1.sendMessage(message);
							break;
						} else {
							Bimp.max1 += 1;
							Message message = new Message();
							message.what = 1;
							handler1.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	public class GridAdapter2 extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter2(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap2.size() == PublicWay.num) {
				return PublicWay.num;
			}
			return (Bimp.tempSelectBitmap2.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.tempSelectBitmap2.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap2
						.get(position).getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler2 = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter2.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max2 == Bimp.tempSelectBitmap2.size()) {
							Message message = new Message();
							message.what = 1;
							handler2.sendMessage(message);
							break;
						} else {
							Bimp.max2 += 1;
							Message message = new Message();
							message.what = 1;
							handler2.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	private static final int TAKE_PICTURE1 = 0x000001;
	private static final int TAKE_PICTURE2 = 0x000002;
	public void photo(int selectNum) {
		if (selectNum ==1) {
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(openCameraIntent, TAKE_PICTURE1);
		}else {
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(openCameraIntent, TAKE_PICTURE2);
		}
		
		
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE1:
			if (Bimp.tempSelectBitmap1.size() < 9 && resultCode == RESULT_OK) {
				
				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);
				
				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				takePhoto.setImagePath(FileUtils.SDPATH+fileName+".jpg");
				Bimp.tempSelectBitmap1.add(takePhoto);
			}
			break;
		case TAKE_PICTURE2:
			if (Bimp.tempSelectBitmap2.size() < 9 && resultCode == RESULT_OK) {
				
				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);
				
				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				takePhoto.setImagePath(FileUtils.SDPATH+fileName+".jpg");
				Bimp.tempSelectBitmap2.add(takePhoto);
			}
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
					nameValuePair.add(new BasicNameValuePair("t", "1"));
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.GROWTH_PROCESS_DATA_URL, nameValuePair);
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mBean = gson.fromJson(sb.toString(), ProcessInfo.class);
						if (mBean != null && "0".equals(mBean.getStatus())) {
							Message msg = handler.obtainMessage(REFRESH_TEXT);
							handler.sendMessage(msg);
						} else {
							Message msg = handler.obtainMessage(
									2,
									getResources().getString(
											R.string.net_data_error));
							handler.sendMessage(msg);
						}
					} else {
						Logger.d("ddd", "sb==null");

						Message msg = handler.obtainMessage(2, getResources()
								.getString(R.string.net_data_error));
						handler.sendMessage(msg);
					}
				} else {
					Message msg = handler.obtainMessage(2, getResources()
							.getString(R.string.net_data_error));
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	/***
	 * Fault_Nessage_ID 故障信息id PowerStation_ID 电站id UserID 电站管理人员id Type
	 * 提交数据类型（1-初步诊断意见 2-处理结果） Fault 初步诊断意见/处理结果
	 * http://117.146.89.194:8032/SubmitGrowthLog
	 * .aspx?landid=1&growthproid=1&content
	 * =test&timetitle=%E7%81%8C%E6%BA%89%E6%
	 * 97%A5%E6%9C%9F&userid=2&time=2016-7-30
	 */
	private void uploadImg(final int type, final String content,
			final String landid, final List<String> imageUrlList) {
		// TODO Auto-generated method stub
		progressView.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				final Map<String, String> map = new HashMap<String, String>();
				map.put("landid", landid);
				map.put("growthproid", growthproid);
				map.put("content", content);
				map.put("timetitle", timetitle);
				map.put("time", time_selected.getText().toString());
				map.put("userid", Constants.uid);
				try {
					// 登陆流程时要处理
					StringBuffer sb = HttpUtil.uploadSubmit(
							Constants.UPLOAD_GROWTH_LOG_DATA_URL, map,
							imageUrlList);
					Logger.d("aaaa",
							"url==" + Constants.UPLOAD_GROWTH_LOG_DATA_URL
									+ map.toString());
					if (null != sb) {
						Logger.d("aaaa", "======" + sb.toString());
						String sucess = ResolveJson.uploadResultParse(sb
								.toString());
						if ("0".equals(sucess)) {
							Message msg;
							if (type == 1) {
								msg = mHandler
										.obtainMessage(REFRESH_UPLOAD_TEXT1);
							} else {
								msg = mHandler
										.obtainMessage(REFRESH_UPLOAD_TEXT2);
							}
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

	protected void onRestart() {
		adapter.update();
		adapter2.update();
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isFinish = true;
		Bimp.tempSelectBitmap1.clear();
		Bimp.max1 = 0;
		Bimp.tempSelectBitmap2.clear();
		Bimp.max2 = 0;
		ZteApplication.bus.unregister(mContext);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for (int i = 0; i < PublicWay.activityList.size(); i++) {
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}
			finish();
		}
		return true;
	}
}
