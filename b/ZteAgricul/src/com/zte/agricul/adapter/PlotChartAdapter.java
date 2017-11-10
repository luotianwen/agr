package com.zte.agricul.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zte.agricul.R;
import com.zte.agricul.app.Constants;
import com.zte.agricul.bean.CropsChartBean;
import com.zte.agricul.bean.WeatherBean;
import com.zte.agricul.fragment.PlotCropsGrowFragment;
import com.zte.agricul.ui.ManagerGrowthActivity;
import com.zte.agricul.ui.ManagerWeatherActivity;
import com.zte.agricul.ui.ViewImageActivity;
import com.zte.agricul.util.Logger;
import com.zte.agricul.view.SplineChart03View;

public class PlotChartAdapter extends BaseAdapter {
	// private Context mContext;
	private List<CropsChartBean>  mBeans;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private ViewHolder viewHolder = null;
	int unreadNum ;
	public static final String EXTRA_IMAGE = "extra_image";
	public static final String EXTRA_STR = "extra_str";
	
	// 标签对应的数据集
	private LinkedList<String> dayLabels = new LinkedList<String>();// 数轴数据
	private LinkedHashMap<Double, Double> linePoint = new LinkedHashMap<Double, Double>();
	private LinkedHashMap<Double, Double> linePoint2 = new LinkedHashMap<Double, Double>();
	
	private String landid = "";
	private String landName = "";
	String userPer = "";
	private SharedPreferences sharedPreferences;
	public PlotChartAdapter(Context mContext, List<CropsChartBean>  mBeans,String landid ,String landName) {
		super();
		sharedPreferences = mContext.getSharedPreferences(Constants.SHARE_PRE_FILE,
				Context.MODE_PRIVATE);
		userPer = sharedPreferences
				.getString(Constants.USER_PERMISSION, "");
		this.mContext = mContext;
		this.mBeans = mBeans;
		this.landid =landid ;
		this.landName=landName;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		// Log.d("TAG", "adapter.size() -" + mNewsListContent.size());
		return mBeans.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mBeans.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
//		View viewRoot = convertView;
//		if (viewRoot == null) {
//			viewRoot = mLayoutInflater.inflate(R.layout.item_crops_chart_list, null);
//			viewHolder = new ViewHolder();
//			viewHolder.spline_chart1 = (SplineChart03View) viewRoot
//					.findViewById(R.id.spline_chart1);
//			viewHolder.edit_plot1 = (TextView) viewRoot
//					.findViewById(R.id.edit_plot1);
////			viewRoot.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) viewRoot.getTag();
//		}
//		
//		String verData = "0,5,20";
////		if ("天气数据".equals(mBeans.get(position).getName())) {
////			
////		}else {
//		
//		if (position==0) {
//			setDayChartData2(viewHolder.spline_chart1,mBeans.get(position).getList1(), mBeans
//					.get(position).getList2(), mBeans
//					.get(position).getTime(), verData, mBeans.get(position).getLandname(),mBeans.get(position).getName());
//		}
////		}
//		
//		
//		return viewRoot;
		
		 convertView = mLayoutInflater.inflate(R.layout.item_crops_chart_list, null);
		 
		 SplineChart03View  spline_chart1 = (SplineChart03View) convertView
					.findViewById(R.id.spline_chart1);
		 TextView edit_plot1 = (TextView) convertView
					.findViewById(R.id.edit_plot1);
		
		int type =1 ;
		if (mContext.getResources().getString(R.string.weather_data).equals(mBeans.get(position).getName())) {
			setDayChartData3(spline_chart1,mBeans.get(position).getWeatherList(),
			mBeans.get(position).getTime(), mBeans
			.get(position).getVerData(), mBeans
					.get(position).getLandname(),mBeans.get(position).getName());
			type =2;
			
			if (userPer.contains("6")) {
				edit_plot1.setVisibility(View.VISIBLE);
			}else {
				edit_plot1.setVisibility(View.GONE);
			}
		}else {
			setDayChartData2(spline_chart1,mBeans.get(position).getList1(), mBeans
					.get(position).getList2(), mBeans
					.get(position).getTime(), mBeans
					.get(position).getVerData(), mBeans.get(position).getLandname(),mBeans.get(position).getName());
			type=1;
			
			if (userPer.contains("5")) {
				edit_plot1.setVisibility(View.VISIBLE);
			}else {
				edit_plot1.setVisibility(View.GONE);
			}
		}
		edit_plot1.setOnClickListener(new lvButtonListener(type));
//		}
		return convertView;
	}

	private class ViewHolder {
		
//		private  TextView time_title1 ,time_title2 ;
//		private  TextView txt_time1 ,txt_time2 ;
//		private  TextView txt_content1 ,txt_content2;
//		private  LinearLayout layout_grow_item_1,layout_grow_item_2;
//		private  TextView compare_time;
//		private  TextView grow_type ;
//		private GridViewToScrollView item_grow_img_grid1,item_grow_img_grid2;
		
		private SplineChart03View spline_chart1 ;
		private TextView edit_plot1 ;
	}
	
	class lvButtonListener implements OnClickListener {
		private int position;

		lvButtonListener(int pos) {
			position = pos;
		}
		@Override
		public void onClick(View v) {
			if (position==1) {
				if (PlotCropsGrowFragment.cropIndex.size()==0) {
					return ;
				}
				Intent intent = new Intent(mContext,ManagerGrowthActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("landid", landid);
				intent.putExtra("landName", landName);
				mContext.startActivity(intent);
			}else {
				Intent intent = new Intent(mContext,ManagerWeatherActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("landid", landid);
				mContext.startActivity(intent);
			}
				
		}
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
	private void setDayChartData2(SplineChart03View spline_chart1,List<String> line1, List<String> line2,
			List<String> time, String verData, List<String> landName,String name) {
		System.out.println("1111111111111111111111111");
		// TODO Auto-generated method stub
		if ("0,0,0".endsWith(verData)) {
			verData = "0,5,20";
		}
		if (null == time || time.size() == 0) {
			return;
		}
		if (time.size()==1) {
			time.add(time.get(0));
		}
		linePoint = new LinkedHashMap<Double, Double>();
		linePoint2 = new LinkedHashMap<Double, Double>();
		dayLabels = new LinkedList<String>();// 数轴数据
		// 设置数轴数据
		for (int i = 0; i < time.size(); i++) {
			dayLabels.add(time.get(i));
		}

		if (line1!=null&&line1.size()>0) {
			for (int i = 0; i < line1.size(); i++) {
				
				if ("".equals(line1.get(i))) {
					linePoint.put(Double.valueOf(i), Double.valueOf(0));
				}else {
					linePoint.put(Double.valueOf(i), Double.valueOf(line1.get(i)));
				}
			}
		}
		if (line2!=null&&line2.size()>0) {
		for (int i = 0; i < line2.size(); i++) {
			if ("".equals(line2.get(i))) {
				linePoint2.put(Double.valueOf(i), Double.valueOf(0));
			}else {
				linePoint2.put(Double.valueOf(i), Double.valueOf(line2.get(i)));
			}
		}
		}
		int aaa =0;
		if (line1.size()>line2.size()) {
			aaa = line1.size();
		}else {
			aaa=line2.size();
		}
		
		for (int i = 0; i < (aaa-time.size()+5); i++) {
			dayLabels.add("");
		}
		
		if (!"".equals(verData) && verData != null) {
			String[] aa = verData.split(",");
			spline_chart1
					.setVerData(Integer.valueOf(aa[aa.length - 1]),
							Integer.valueOf(aa[0]), Integer.valueOf(aa[1]),
							(aaa+5));// 设置竖轴数据
		}
		spline_chart1.getSplineChart().setTitle("  "+name);
		spline_chart1.setChartDataSet(linePoint, linePoint2, landName);
		spline_chart1.setChartLabels(dayLabels); // 设置横轴数据
		spline_chart1.initData();

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
	private void setDayChartData3(SplineChart03View spline_chart1,List<WeatherBean> weatherList,
			List<String> time, String verData, List<String> landName,String name) {
		if ("0,0,0".endsWith(verData)) {
			verData = "0,5,20";
		}
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
			if (time.size()==1) {
				dayLabels.add(time.get(i));
			}
			dayLabels.add(time.get(i));
			Logger.d("aaaa", "bbb===" + aaaaaa * i);
		}

		if (!"".equals(verData) && verData != null) {
			String[] aa = verData.split(",");
			System.out.println(aa[0]+"=="+Integer.valueOf(aa[1])+"=="+aa[aa.length - 1]);
			spline_chart1
					.setVerData(Integer.valueOf(aa[aa.length - 1]),
							Integer.valueOf(aa[0]), Integer.valueOf(aa[1]),
							time.size());// 设置竖轴数据
		}
		spline_chart1.getSplineChart().setTitle("  "+name);
		spline_chart1.setChartDataSet(linePoint, linePoint2, landName);
		spline_chart1.setChartLabels(dayLabels); // 设置横轴数据
		spline_chart1.initData();

	}
}
