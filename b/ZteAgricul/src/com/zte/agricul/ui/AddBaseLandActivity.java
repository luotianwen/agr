package com.zte.agricul.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.king.photo.util.Bimp;
import com.squareup.otto.Subscribe;
import com.zte.agricul.R;
import com.zte.agricul.adapter.AddLandCityAdapter;
import com.zte.agricul.adapter.AddLandConturyAdapter;
import com.zte.agricul.adapter.AddLandProvinceAdapter;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.CityBean;
import com.zte.agricul.bean.ConturyBean;
import com.zte.agricul.bean.PlotAllListInfo;
import com.zte.agricul.bean.ProvinceBean;
import com.zte.agricul.bean.UploadResultBean;
import com.zte.agricul.ui.AddGrowProcessActivity.GridAdapter;
import com.zte.agricul.ui.AddGrowProcessActivity.GridAdapter2;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.ResolveJson;
import com.zte.agricul.util.Util;
import com.zte.agricul.view.GridViewToScrollView;

public class AddBaseLandActivity extends BaseActivity implements
		OnClickListener {
	private final int  REFRESH_UPLOAD_TEXT1 = 3 ; 
	private TextView leftTv, ivTitleName, rightTv;
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private RelativeLayout progressView;
	private TextView net_error;
	private UploadResultBean mBean;
	private boolean isFinish = false;
	private Spinner conturySpinner;
	private Spinner provinceSpinner, citySpinner,userSpinner;
	// private ArrayAdapter<String> userAdapter;
	private AddLandCityAdapter cityAdapter,userAdapter;
	private AddLandProvinceAdapter provinceAdapter;
	private AddLandConturyAdapter conturyAdapter;
	private EditText editLandName, editLandAddress,
			editLandPhone,editUserName;
	private TextView selectLocation;
	private EditText editMinday, editMaxday;
	private EditText editTemp, editAnnualPro, editSoilPro, editSoilPh;
	private EditText editNPK, editOMC, editMTE, editPCS;
	private EditText editPCY, editRemark, editSize, editAnnualoOtput;
	private List<ConturyBean> Conturylist;
	private List<ProvinceBean> ProvinceList;
	private List<CityBean> CityList;
	private int pPosition ;
	private List<CityBean> userList;
	private List<List<CityBean>> areaDatas;
	private String conturyid = "1";
	private String provinceid = "";
	private String cityid = "";
	private String districtid = "";
	private String username = "";
	private PlotAllListInfo mListBean;
	private ImageView land_pic ;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private static String localTempImageFileName = "";
	private Intent intent ;
	private static final int FLAG_CHOOSE_IMG = 5;
	private static final int FLAG_CHOOSE_PHONE = 6;
	private static final int FLAG_MODIFY_FINISH = 7;
	public static final File FILE_PIC_SCREENSHOT = new File(
			Constants.SCR_IMAGE_PATH);
	String urlPath = null;
	
	private String location ="";
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
			case REFRESH_UPLOAD_TEXT1:
				progressView.setVisibility(View.GONE);
				Util.showToast(getApplicationContext(), getResources().getString(R.string.upload_success),Toast.LENGTH_SHORT);
				ZteApplication.bus.post(BusEvent.REFRESH_ADD_BASE_LAND_EVENT);
				finish() ;
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
		setContentView(R.layout.activity_add_base_land);
		ZteApplication.bus.register(this);
		// initData();
		Conturylist = (List<ConturyBean>) getIntent().getSerializableExtra(
				"contury");
		ProvinceList = (List<ProvinceBean>) getIntent().getSerializableExtra(
				"province");
		CityList = (List<CityBean>) getIntent().getSerializableExtra("city");
		userList = (List<CityBean>) getIntent().getSerializableExtra("user");
		getCityData(ProvinceList, CityList);
		getListData();
		initView();
		Init();
	};

	private void getListData() {
		// TODO Auto-generated method stub
		provinceid = ProvinceList.get(0).getProID();
		cityid = CityList.get(0).getCityID();
		districtid = CityList.get(0).getDisID();
		username= userList.get(0).getID();
		pPosition = 0 ;
	}

	private void initView() {
		// TODO Auto-generated method stub
		leftTv = (TextView) findViewById(R.id.leftTv);
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setOnClickListener(this);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		ivTitleName.setText(getResources().getString(R.string.new_base));
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setOnClickListener(this);
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);
		rightTv.setText(getResources().getString(R.string.save));
		rightTv.setVisibility(View.VISIBLE);
		progressView = (RelativeLayout) findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);
		net_error = (TextView) findViewById(R.id.net_error);
		net_error.setOnClickListener(this);
		
		land_pic = (ImageView) findViewById(R.id.land_pic);
		land_pic.setOnClickListener(this);
		
		editLandName = (EditText) findViewById(R.id.edit_land_name);
		editLandAddress = (EditText) findViewById(R.id.edit_land_address);
		
		editUserName= (EditText) findViewById(R.id.edit_user_name);
		editLandPhone = (EditText) findViewById(R.id.edit_land_phone);
		selectLocation = (TextView) findViewById(R.id.select_location);
		selectLocation.setOnClickListener(this);
		editMinday = (EditText) findViewById(R.id.edit_land_minday);
		editMaxday = (EditText) findViewById(R.id.edit_land_maxday);
		editTemp = (EditText) findViewById(R.id.edit_land_temp);
		editAnnualPro = (EditText) findViewById(R.id.edit_land_annual_pro);
		editSoilPro = (EditText) findViewById(R.id.edit_land_soil_pro);
		editSoilPh = (EditText) findViewById(R.id.edit_land_soil_ph);
		editNPK = (EditText) findViewById(R.id.edit_land_n_p_k);
		editOMC = (EditText) findViewById(R.id.edit_land_o_m_c);
		editMTE = (EditText) findViewById(R.id.edit_land_m_t_e);
		editPCS = (EditText) findViewById(R.id.edit_land_p_c_s);
		editPCY = (EditText) findViewById(R.id.edit_land_p_c_y);
		editRemark = (EditText) findViewById(R.id.edit_plot_mark);
		editSize = (EditText) findViewById(R.id.edit_land_size);
		editAnnualoOtput = (EditText) findViewById(R.id.edit_land_annualo_otput);
		// editPlotMark = (EditText) findViewById(R.id.edit_plot_mark);

		conturySpinner = (Spinner) findViewById(R.id.contury_spinner);
		provinceSpinner = (Spinner) findViewById(R.id.province_spinner);
		citySpinner = (Spinner) findViewById(R.id.city_spinner);
		userSpinner= (Spinner) findViewById(R.id.user_spinner);
		conturyAdapter = new AddLandConturyAdapter(mContext, Conturylist);
		provinceAdapter = new AddLandProvinceAdapter(mContext, ProvinceList);
		cityAdapter = new AddLandCityAdapter(mContext, areaDatas.get(0));
		userAdapter = new AddLandCityAdapter(mContext, userList);
		userSpinner.setAdapter(userAdapter);
		// 将adapter 添加到spinner中
		conturySpinner.setAdapter(conturyAdapter);
		// 将adapter 添加到spinner中
		provinceSpinner.setAdapter(provinceAdapter);
		// 将adapter 添加到spinner中
		citySpinner.setAdapter(cityAdapter);
		// 添加事件Spinner事件监听
		conturySpinner
				.setOnItemSelectedListener(new SpinnerSelectedListener(1));
		provinceSpinner
				.setOnItemSelectedListener(new SpinnerSelectedListener(2));
		citySpinner.setOnItemSelectedListener(new SpinnerSelectedListener(3));
		userSpinner.setOnItemSelectedListener(new SpinnerSelectedListener(4));

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
			if (type == 2) {
				provinceid = ProvinceList.get(position).getProID();
				cityAdapter = new AddLandCityAdapter(mContext,
						areaDatas.get(position));
				citySpinner.setAdapter(cityAdapter);
				pPosition = position ;
			} else if(type == 4){
				username = userList.get(position).getID();
			}else if (type == 3) {
				cityid = areaDatas.get(pPosition).get(position).getCityID();
				districtid = areaDatas.get(pPosition).get(position).getDisID();
			}

			// view.setText("你的血型是：" + m[arg2]);
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
		Intent intent;
		InputMethodManager imm  ;
		switch (v.getId()) {

		case R.id.leftTv:
			finish();
			break;
		case R.id.land_pic:
			imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
			imm.hideSoftInputFromWindow(land_pic.getWindowToken(), 0); //强制隐藏键盘  
			
			ll_popup.startAnimation(AnimationUtils.loadAnimation(AddBaseLandActivity.this,R.anim.activity_translate_in));
			pop.showAtLocation(land_pic, Gravity.BOTTOM, 0, 0);
			break;	
			
			
		case R.id.rightTv:
			imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
			imm.hideSoftInputFromWindow(land_pic.getWindowToken(), 0); //强制隐藏键盘  
			
			
			if ("".equals(editLandName.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.fill_the_base_name),
						Toast.LENGTH_SHORT);
				return;
			}
			if ("".equals(editLandAddress.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.fill_the_base_address),
						Toast.LENGTH_SHORT);
				return;
			}
			if ("".equals(editUserName.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.fill_the_user_name),
						Toast.LENGTH_SHORT);
				return;
			}
			if ("".equals(editLandPhone.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.fill_the_user_phone),
						Toast.LENGTH_SHORT);
				return;
			}
			if ("".equals(selectLocation.getText().toString())||getResources().getString(R.string.fill_the_base_location).equals(selectLocation.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.fill_the_base_location),
						Toast.LENGTH_SHORT);
				return;
			}
			if ("".equals(editSize.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.fill_the_base_size),
						Toast.LENGTH_SHORT);
				return;
			}
			if ("".equals(editAnnualoOtput.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.fill_the_base_yield),
						Toast.LENGTH_SHORT);
				return;
			}
			List<String> imageUrlList = new ArrayList<String>();
			if (urlPath!=null) {
				imageUrlList.add(urlPath);
			}
			
			if (imageUrlList.size()<1) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.fill_the_base_picture),
						Toast.LENGTH_SHORT);
				return;
			}
			uploadImg(imageUrlList,selectLocation.getText().toString());
			break;

		case R.id.select_location:
			intent = new Intent(getApplicationContext(),MapToLocationActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}


	/***
	 * http://117.146.89.194:8032/SubmitNewBase.aspx?
	 * conturyid=1&provinceid=1&cityid=2&districtid=0&
	 * name=jidiceshi&address=sjkkah&username=zhangsan&
	 * phone=13800138000&lo=139.545464&la=39.671384&
	 * minday=100&maxday=200&temp=1500-2000&annual_pro=2000
	 * &soil_pro=%E6%B2%99%E5%9C%9F&soil_ph=6.5&n_p_k=1
	 * &o_m_c=3&m_t_e=0.01&p_c_s=%E5%A4%A7%E8%B1%86&
	 * p_c_y=%E4%BD%8E%E4%BA%A7&remark=&size=1500&annualoutput=2500 
	 * 添加基地接
	 * 
	 * SubmitNewBase.aspx{conturyid=1,provinceid=1,cityid=1,districtid=1,name=北京1号基地,
	 * address=beijing1, username=45646,phone=4564649,lo=4664,la=6464,minday=4656,maxday=66879, temp=5466,
	 *  annual_pro=1,  soil_pro=100, soil_ph=1000,n_p_k=100,o_m_c=100,m_t_e=1000, p_c_s=1给你,  p_c_y=去医院, 
	 *  remark=擒贼先擒王,size=76676, annualoutput=949
	 *  }

	 */
	private void uploadImg(final List<String> imageUrlList,final String location ) {
		// TODO Auto-generated method stub
		progressView.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				final Map<String, String> map = new HashMap<String, String>();
				map.put("userid",Constants.uid);
				map.put("conturyid",conturyid);
				map.put("provinceid", provinceid);
				map.put("cityid",cityid);
				map.put("districtid",districtid);
				map.put("name",editLandName.getText().toString());
				map.put("address",editLandAddress.getText().toString());
				map.put("username",editUserName.getText().toString());
				map.put("landuserid",username);
				map.put("phone",editLandPhone.getText().toString());
				String lo[] = location.split(",");
				System.out.println("location==="+location);
				map.put("la",lo[0]);
				map.put("lo",lo[1]);
				map.put("minday",editMinday.getText().toString());
				map.put("maxday",editMaxday.getText().toString());
				map.put("temp",editTemp.getText().toString());
				map.put("annual_pro",editAnnualPro.getText().toString());
				map.put("soil_pro",editSoilPro.getText().toString());
				map.put("soil_ph",editSoilPh.getText().toString());
				map.put("n_p_k",editNPK.getText().toString());
				map.put("o_m_c",editOMC.getText().toString());
				map.put("m_t_e",editMTE.getText().toString());
				map.put("p_c_s",editPCS.getText().toString());
				map.put("p_c_y",editPCY.getText().toString());
				map.put("m_t_e",editMTE.getText().toString());
				map.put("remark",editRemark.getText().toString());
				map.put("size",editSize.getText().toString());
				map.put("annualoutput",editAnnualoOtput.getText().toString());
				
				try {
					// 登陆流程时要处理
					StringBuffer sb = HttpUtil.uploadSubmit(
							Constants.ADD_BASE_LAND_URL, map,
							imageUrlList);
					 Logger.d("aaaa", "url==" +Constants.ADD_BASE_LAND_URL+ map.toString());
					if (null != sb) {
						Logger.d("aaaa", "======" + sb.toString());
						String sucess = ResolveJson.uploadResultParse(sb.toString());
						if ("0".equals(sucess)) {
							Message msg ;
								msg = mHandler.obtainMessage(REFRESH_UPLOAD_TEXT1);
							mHandler.sendMessage(msg);
						}else {
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
					e.printStackTrace();
					}
			};
		}.start();
	}

	public void Init() {
		
		pop = new PopupWindow(AddBaseLandActivity.this);
		
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view
				.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
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
				takePhoto();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pickPhoto();
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
		
	}
	
	/**
	 * 相册
	 */
	protected void pickPhoto() {
		// TODO Auto-generated method stub
		intent= new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, FLAG_CHOOSE_IMG);
	}
	/**
	 * 执行拍照
	 */

	public void takePhoto() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				localTempImageFileName = "";
				localTempImageFileName = String.valueOf((new Date()).getTime())
						+ ".png";
				File filePath = FILE_PIC_SCREENSHOT;
				if (!filePath.exists()) {
					filePath.mkdirs();
				}
				intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				File f = new File(filePath, localTempImageFileName);
				// localTempImgDir和localTempImageFileName是自己定义的名字
				Uri u = Uri.fromFile(f);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
				startActivityForResult(intent, FLAG_CHOOSE_PHONE);
			} catch (ActivityNotFoundException e) {
				//
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			return;
		}
		switch (requestCode) {
		case FLAG_CHOOSE_PHONE:
			File f = new File(FILE_PIC_SCREENSHOT, localTempImageFileName);
			intent = new Intent(mContext, CropImageActivity.class);
			intent.putExtra("path", f.getAbsolutePath());
			startActivityForResult(intent, FLAG_MODIFY_FINISH);

			break;
		case FLAG_MODIFY_FINISH:
			if (data != null) {
				urlPath = data.getStringExtra("path");
				Bitmap b = BitmapFactory.decodeFile(urlPath);
				land_pic.setImageBitmap(b);
			}
			break;
		case FLAG_CHOOSE_IMG:

			if (data != null) {
				Uri uri = data.getData();
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,
							new String[] { MediaStore.Images.Media.DATA },
							null, null, null);
					if (null == cursor) {
						Util.showToast(getApplicationContext(),  getResources().getString(R.string.no_picture), Toast.LENGTH_SHORT);
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					cursor.close();
					intent = new Intent(this, CropImageActivity.class);
					intent.putExtra("path", path);
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				} else {
					intent = new Intent(this, CropImageActivity.class);
					intent.putExtra("path", uri.getPath());
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				}
			}
		

			break;

		default:
			break;
		}
	}
	// 获取二级联动城市数据
	private void getCityData(List<ProvinceBean> proBean, List<CityBean> cityBean) {
		// TODO Auto-generated method stub
		List<CityBean> aa = null;
		areaDatas = new ArrayList<List<CityBean>>();
		for (int i = 0; i < proBean.size(); i++) {
			aa = new ArrayList<CityBean>();
			for (int j = 0; j < cityBean.size(); j++) {
				if (cityBean.get(j).getProID().equals(proBean.get(i).getProID())) {
					aa.add(cityBean.get(j));
					System.out.println(i+"");
					System.out.println(j + "==j==" + cityBean.get(j).getName() + cityBean.get(j).getDisID());
				}
			}
			areaDatas.add(aa);
		}
	}

	@Subscribe
	public void onBusEvent(BusEvent event) {
		if (event == BusEvent.REFRESH_LOCATION_EVENT) {
			selectLocation.setText(String.valueOf(event.data));
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