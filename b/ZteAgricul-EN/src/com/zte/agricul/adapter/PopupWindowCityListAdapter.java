package com.zte.agricul.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zte.agricul.zh.R;
import com.zte.agricul.bean.CityBean;
import com.zte.agricul.bean.ProvinceBean;
import com.zte.agricul.view.GridViewToScrollView;

public class PopupWindowCityListAdapter extends BaseAdapter {
	// private Context mContext;
	private List<List<CityBean>>   cityBeans;
	private List<ProvinceBean> provinceBeans;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private ViewHolder viewHolder = null;
	int unreadNum ;
	private int select =0 ;
	private String selectName ="";
	public PopupWindowCityListAdapter(Context mContext, List<ProvinceBean> provinceBeans,List<List<CityBean>> cityBeans,String selectName) {
		super();
		this.mContext = mContext;
		this.cityBeans = cityBeans;
		this.provinceBeans  = provinceBeans;
		this.selectName =selectName;
		mLayoutInflater = LayoutInflater.from(mContext);
		System.out.println("===="+cityBeans.get(0).size());
	}

	public int getCount() {
		// TODO Auto-generated method stub
		// Log.d("TAG", "adapter.size() -" + mNewsListContent.size());
		return provinceBeans.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return provinceBeans.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View viewRoot = convertView;
		if (viewRoot == null) {
			viewRoot = mLayoutInflater.inflate(R.layout.item_pop_city_list, null);
			viewHolder = new ViewHolder();
			viewHolder.pop_city = (TextView) viewRoot
					.findViewById(R.id.pop_city);
			viewHolder.mGrid = (GridViewToScrollView) viewRoot.findViewById(R.id.pop_grid_list);
			viewRoot.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) viewRoot.getTag();
		}
		viewHolder.pop_city.setText(provinceBeans.get(position).getName());
		viewHolder.pop_city.setOnClickListener(new lvButtonListener(position));
		PopAreaGridListAdapter adapter = new PopAreaGridListAdapter(mContext, cityBeans.get(position),selectName);
		viewHolder.mGrid.setAdapter(adapter);
		if (select==position) {
			viewHolder.mGrid.setVisibility(View.VISIBLE);
		}else {
			viewHolder.mGrid.setVisibility(View.GONE);
		}
		
		return viewRoot;
	}

	private class ViewHolder {
		private TextView pop_city;
		private GridViewToScrollView mGrid;
	}
	
	private  void  setSelected(int select){
		this.select =select;
		notifyDataSetChanged();
	}

	class lvButtonListener implements OnClickListener {
		private int position;

		lvButtonListener(int pos) {
			position = pos;
		}

		@Override
		public void onClick(View v) {
			int vid = v.getId();
			if (vid == viewHolder.pop_city.getId()) {
				setSelected(position);
			}
		}
	}
}
