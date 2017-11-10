package com.zte.agricul.fragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zte.agricul.zh.R;
import com.zte.agricul.adapter.PlotChartAdapter;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.bean.CropsChartInfo;
import com.zte.agricul.bean.PlotBean;
import com.zte.agricul.bean.WeatherBean;
import com.zte.agricul.ui.AddGrowDataActivity2;
import com.zte.agricul.ui.AddWeatherDataActivity;
import com.zte.agricul.ui.ManagerGrowthActivity;
import com.zte.agricul.ui.ManagerWeatherActivity;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.ResolveJson;
import com.zte.agricul.view.SplineChart03View;

public class PlotCropsChartFragment2 extends Fragment implements OnClickListener {
	private final int REFRESH_TEXT = 1;
	private final int NET_ERROR = 2;
	private View view;
	private TextView net_error;
	private RelativeLayout progressView;
	private ScrollView scrollView1;
	private SplineChart03View crop_chart1, crop_chart2, crop_chart3;
	private RelativeLayout layout1, layout2, layout3;
	private TextView edit_plot1, edit_plot2, edit_plot3;
	private boolean isFinish = false;
	private String id;
	private String landName;
	private CropsChartInfo mBean;
	private Gson gson;

	private LinkedList<String> mLabels = new LinkedList<String>();
	// 标签对应的数据集
	private LinkedList<String> dayLabels = new LinkedList<String>();// 数轴数据
	private LinkedHashMap<Double, Double> linePoint = new LinkedHashMap<Double, Double>();
	private LinkedHashMap<Double, Double> linePoint2 = new LinkedHashMap<Double, Double>();

	private ListView mListView;
	PlotChartAdapter mAdapter ; 
	private List<PlotBean >  plotList;
	private String landid2="";
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isFinish) {
				return;
			}
			mListView.setVisibility(View.GONE);
			progressView.setVisibility(View.GONE);
			switch (msg.what) {
			case REFRESH_TEXT:
				mListView.setVisibility(View.VISIBLE);
				mAdapter = new PlotChartAdapter(getActivity(), mBean.getResult(),id,landName);
				mListView.setAdapter(mAdapter);
//				String verData = "0,20,80";
//				if (mBean.getResult().get(0).getTime() != null
//						&& mBean.getResult().get(0).getTime().size() > 0) {
//					layout1.setVisibility(View.VISIBLE);
//					setDayChartData1(mBean.getResult().get(0).getList1(), mBean
//							.getResult().get(0).getList2(), mBean.getResult()
//							.get(0).getTime(), verData, mBean.getResult()
//							.get(0).getLandname());
//				}
//				if (mBean.getResult().get(1).getTime() != null
//						&& mBean.getResult().get(1).getTime().size() > 0) {
//					layout2.setVisibility(View.VISIBLE);
//					verData = "0,5,20";
//					setDayChartData2(mBean.getResult().get(1).getList1(), mBean
//							.getResult().get(1).getList2(), mBean.getResult()
//							.get(1).getTime(), verData, mBean.getResult()
//							.get(1).getLandname());
//				}
//				if (mBean.getResult().get(2).getTime() != null
//						|| mBean.getResult().get(2).getTime().size() > 0) {
//					layout3.setVisibility(View.VISIBLE);
//					verData = "0,10,40";
//					setDayChartData3(mBean.getResult().get(2).getWeatherList(),
//							mBean.getResult().get(2).getTime(), verData, mBean
//									.getResult().get(2).getLandname());
//				}
				// setDayChartData1(mBean.getResult().get(2).getList1(),mBean.getResult().get(2).getTime(),
				// verData);
				net_error.setVisibility(View.GONE);
//				scrollView1.setVisibility(View.VISIBLE);
				break;
			case NET_ERROR:
				net_error.setVisibility(View.GONE);
				// Util.showToast(getActivity(), (String) msg.obj,
				// Toast.LENGTH_SHORT);
				break;

			}

		}
	};

	public PlotCropsChartFragment2(String landName,String id,List<PlotBean >  plotList) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.plotList=plotList;
		this.landName= landName ;
		if (plotList==null||plotList.size()==0) {
			landid2="0";
		}else {
			landid2=plotList.get(0).getID();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_plot_crops_chart2, container,
				false);
		ZteApplication.bus.register(this);
		gson = new Gson();
		initView();
		initData();
		return view;
	}

	private void initView() {
		// TODO Auto-generated method stub
		net_error = (TextView) view.findViewById(R.id.net_error);
		net_error.setOnClickListener(this);
		progressView = (RelativeLayout) view.findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);
		scrollView1 = (ScrollView) view.findViewById(R.id.scrollView1);

//		crop_chart1 = (SplineChart03View) view.findViewById(R.id.spline_chart1);
//
//		crop_chart2 = (SplineChart03View) view.findViewById(R.id.spline_chart2);
//
//		crop_chart3 = (SplineChart03View) view.findViewById(R.id.spline_chart3);
//
//		layout1 = (RelativeLayout) view.findViewById(R.id.layout_plot_chart1);
//		layout2 = (RelativeLayout) view.findViewById(R.id.layout_plot_chart2);
//		layout3 = (RelativeLayout) view.findViewById(R.id.layout_plot_chart3);
//
//		edit_plot1 = (TextView) view.findViewById(R.id.edit_plot1);
//		edit_plot2 = (TextView) view.findViewById(R.id.edit_plot2);
//		edit_plot3 = (TextView) view.findViewById(R.id.edit_plot3);
//		edit_plot1.setOnClickListener(this);
//		edit_plot2.setOnClickListener(this);
//		edit_plot3.setOnClickListener(this);
		
		mListView =(ListView) view.findViewById(R.id.main_data_list);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent ; 
		switch (v.getId()) {
		case R.id.net_error:
			initData();
			break;
		case R.id.edit_plot1:
			intent = new Intent(getActivity(),ManagerGrowthActivity.class);
			intent.putExtra("landid", id);
			startActivity(intent);
			break;
		case R.id.edit_plot2:
			intent = new Intent(getActivity(),ManagerGrowthActivity.class);
			intent.putExtra("landid", id);
			startActivity(intent);
			break;
		case R.id.edit_plot3:
			intent = new Intent(getActivity(),ManagerWeatherActivity.class);
			intent.putExtra("landid", id);
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

					nameValuePair.add(new BasicNameValuePair("baseid", id));
					nameValuePair.add(new BasicNameValuePair("landid1", id));
					nameValuePair.add(new BasicNameValuePair("landid2", landid2));
					
					StringBuffer sb = HttpUtil.getDataFromServer(
							Constants.GROWTH_DATA_URL, nameValuePair);
					Logger.d("ddd", "url==" + Constants.GROWTH_DATA_URL
							+ nameValuePair.toString());
					if (null != sb) {
						Logger.d("ddd", "sb==" + sb.toString());
						mBean = ResolveJson.cropsChartParse2(sb.toString());
						if (mBean != null && "0".equals(mBean.getStatus())) {
							Message msg = handler.obtainMessage(REFRESH_TEXT);
							handler.sendMessage(msg);
						} else {
							Message msg = handler.obtainMessage(
								2,getResources().getString(R.string.net_data_error));
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

	// //日发电量
	private void setDayChartData1(List<String> line1, List<String> line2,
			List<String> time, String verData, List<String> landName) {
		// TODO Auto-generated method stub
		if (null == time || time.size() == 0) {
			return;
		}
		linePoint = new LinkedHashMap<Double, Double>();
		linePoint2 = new LinkedHashMap<Double, Double>();
		dayLabels = new LinkedList<String>();// 数轴数据
		// 设置数轴数据
		// 设置横轴数据 和颜色
		double aaaaaa = Double.valueOf(100) / time.size();
		Logger.d("aaaa", "aaaaaa===" + aaaaaa);
		for (int i = 0; i < time.size(); i++) {
			if ("".equals(line1.get(i))) {
				linePoint.put(Double.valueOf(i), Double.valueOf(0));
				linePoint2.put(Double.valueOf(i), Double.valueOf(0));
			} else {
				linePoint.put(Double.valueOf(i), Double.valueOf(line1.get(i)));
				linePoint2.put(Double.valueOf(i), Double.valueOf(line2.get(i)));
			}
			dayLabels.add(time.get(i));
			Logger.d("aaaa", "bbb===" + aaaaaa * i);
		}

		if (!"".equals(verData) && verData != null) {
			String[] aa = verData.split(",");
			crop_chart1
					.setVerData(Integer.valueOf(aa[aa.length - 1]),
							Integer.valueOf(aa[0]), Integer.valueOf(aa[1]),
							time.size());// 设置竖轴数据
		}
		crop_chart1.getSplineChart().setTitle("  "+getResources().getString(R.string.plant_height));
		crop_chart1.setChartDataSet(linePoint, linePoint2, landName);
		crop_chart1.setChartLabels(dayLabels); // 设置横轴数据
		crop_chart1.initData();

	}

	/***
	 * 
	 * @param point1
	 *            线1
	 * @param point2
	 *            线2
	 * @param time
	 *            时间轴数据
	 * @param verData
	 *            数轴 最大，最小，间距等
	 * @param landName
	 *            线1和线2的名字
	 */
	private void setDayChartData2(List<String> line1, List<String> line2,
			List<String> time, String verData, List<String> landName) {
		// TODO Auto-generated method stub
		if (null == time || time.size() == 0) {
			return;
		}
		linePoint = new LinkedHashMap<Double, Double>();
		linePoint2 = new LinkedHashMap<Double, Double>();
		dayLabels = new LinkedList<String>();// 数轴数据
		// 设置数轴数据
		// 设置横轴数据 和颜色
		double aaaaaa = Double.valueOf(100) / time.size();
		Logger.d("aaaa", "aaaaaa===" + aaaaaa);
		for (int i = 0; i < time.size(); i++) {
			if ("".equals(line1.get(i))) {
				linePoint.put(Double.valueOf(i), Double.valueOf(0));
				linePoint2.put(Double.valueOf(i), Double.valueOf(0));
			} else {
				linePoint.put(Double.valueOf(i), Double.valueOf(line1.get(i)));
				linePoint2.put(Double.valueOf(i), Double.valueOf(line2.get(i)));
			}
			dayLabels.add(time.get(i));
			Logger.d("aaaa", "bbb===" + aaaaaa * i);
		}

		if (!"".equals(verData) && verData != null) {
			String[] aa = verData.split(",");
			crop_chart2
					.setVerData(Integer.valueOf(aa[aa.length - 1]),
							Integer.valueOf(aa[0]), Integer.valueOf(aa[1]),
							time.size());// 设置竖轴数据
		}
		crop_chart2.getSplineChart().setTitle("  "+getResources().getString(R.string.stem_diameter));
		crop_chart2.setChartDataSet(linePoint, linePoint2, landName);
		crop_chart2.setChartLabels(dayLabels); // 设置横轴数据
		crop_chart2.initData();

	}

	/***
	 * 
	 * @param point1
	 *            线1
	 * @param point2
	 *            线2
	 * @param time
	 *            时间轴数据
	 * @param verData
	 *            数轴 最大，最小，间距等
	 * @param landName
	 *            线1和线2的名字
	 */
	private void setDayChartData3(List<WeatherBean> weatherList,
			List<String> time, String verData, List<String> landName) {
		// TODO Auto-generated method stub
		if (null == time || time.size() == 0) {
			return;
		}
		linePoint = new LinkedHashMap<Double, Double>();
		linePoint2 = new LinkedHashMap<Double, Double>();
		dayLabels = new LinkedList<String>();// 数轴数据
		// 设置数轴数据
		// 设置横轴数据 和颜色
		double aaaaaa = Double.valueOf(100) / time.size();
		Logger.d("aaaa", "aaaaaa===" + aaaaaa);
		for (int i = 0; i < time.size(); i++) {
			if ("".equals(weatherList.get(i).getC())) {
				linePoint.put(Double.valueOf(i), Double.valueOf(0));
			} else {
				linePoint.put(Double.valueOf(i),
						Double.valueOf(weatherList.get(i).getC()));
			}
			if ("".equals(weatherList.get(i).getW_night())) {
				linePoint2.put(Double.valueOf(i), Double.valueOf(0));
			} else {
				linePoint2.put(Double.valueOf(i),
						Double.valueOf(weatherList.get(i).getW_night()));
			}
			dayLabels.add(time.get(i));
			Logger.d("aaaa", "bbb===" + aaaaaa * i);
		}

		if (!"".equals(verData) && verData != null) {
			String[] aa = verData.split(",");
			crop_chart3
					.setVerData(Integer.valueOf(aa[aa.length - 1]),
							Integer.valueOf(aa[0]), Integer.valueOf(aa[1]),
							time.size());// 设置竖轴数据
		}
		crop_chart3.getSplineChart().setTitle("  "+getResources().getString(R.string.temperature));
		crop_chart3.setChartDataSet(linePoint, linePoint2, landName);
		crop_chart3.setChartLabels(dayLabels); // 设置横轴数据
		crop_chart3.initData();

	}

	public void onBusEvent(BusEvent event) {
		System.out.println("xxxxxxxxxxxxxxxxxx");
		if (event == BusEvent.REFRESH_CHART_DATA_EVENT) {
			initData();
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (ManagerWeatherActivity.IS_REFUSH) {
			initData() ;
			ManagerWeatherActivity.IS_REFUSH = false ;
		}
		if (ManagerGrowthActivity.IS_REFUSH) {
			initData() ;
			ManagerGrowthActivity.IS_REFUSH = false ;
		}
		if (AddWeatherDataActivity.isRefresh) {
			initData() ;
			AddWeatherDataActivity.isRefresh = false ;
		}
		if (AddGrowDataActivity2.isRefresh) {
			initData() ;
			AddGrowDataActivity2.isRefresh = false ;
		}
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		isFinish = true;
		ZteApplication.bus.unregister(this);
	}
}
