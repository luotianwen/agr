package com.zte.agricul.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zte.agricul.zh.R;
import com.zte.agricul.adapter.PopupWindowCityListAdapter;
import com.zte.agricul.bean.CityBean;
import com.zte.agricul.bean.ProvinceBean;

public class PopupWindowCityList extends PopupWindow {

	private Context context;
	private View mMenuView;
	private ListView popList ;
	private PopupWindowCityListAdapter mAdapter ;
	public PopupWindowCityList(Activity context,List<ProvinceBean > provinceBeans, List<List<CityBean >> cityBeans,String selectName) {
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.pop_window_city_list, null);
		popList = (ListView) mMenuView.findViewById(R.id.pop_list);
		mAdapter = new PopupWindowCityListAdapter(context, provinceBeans, cityBeans,selectName);
		popList.setAdapter(mAdapter);
//		popTitle = (TextView) mMenuView.findViewById(R.id.pop_title);
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		this.setBackgroundDrawable(new BitmapDrawable());

	}

}