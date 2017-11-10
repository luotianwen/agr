package com.zte.agricul.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zte.agricul.R;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.bean.CityBean;

public class PopAreaGridListAdapter extends BaseAdapter {
	// private Context mContext;
	private List<CityBean> cityBeans;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private ViewHolder viewHolder = null;
	int unreadNum ;
	private int select = 0 ;
	private String selectName;
	
	public PopAreaGridListAdapter(Context mContext, List<CityBean> cityBeans,String selectName) {
		super();
		this.mContext = mContext;
		this.cityBeans = cityBeans;
		this.selectName =selectName;
		mLayoutInflater = LayoutInflater.from(mContext);
		ZteApplication.bus.register(mContext);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		// Log.d("TAG", "adapter.size() -" + mNewsListContent.size());
		return cityBeans.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cityBeans.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View viewRoot = convertView;
		if (viewRoot == null) {
			viewRoot = mLayoutInflater.inflate(R.layout.item_pop_area_list, null);
			viewHolder = new ViewHolder();
			viewHolder.pop_area = (TextView) viewRoot
					.findViewById(R.id.pop_area);
			viewRoot.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) viewRoot.getTag();
		}
		System.out.println(selectName+"==="+cityBeans.get(position).getName());
		if (selectName.equals(mContext.getResources().getString(R.string.province)
				)&&mContext.getResources().getString(R.string.all).equals(cityBeans.get(position))) {
			viewHolder.pop_area.setBackgroundResource(R.drawable.pop_area_bg_selected);
			viewHolder.pop_area.setTextColor(mContext.getResources().getColor(R.color.white));
		}else if (selectName.equals(cityBeans.get(position).getName())) {
			viewHolder.pop_area.setBackgroundResource(R.drawable.pop_area_bg_selected);
			viewHolder.pop_area.setTextColor(mContext.getResources().getColor(R.color.white));
		}else {
			viewHolder.pop_area.setBackgroundResource(R.drawable.pop_area_bg);
			viewHolder.pop_area.setTextColor(mContext.getResources().getColor(R.color.grey_7f));
		}
		viewHolder.pop_area.setText(cityBeans.get(position).getName());
		viewHolder.pop_area.setOnClickListener(new lvButtonListener(position));
		return viewRoot;
	}

	private class ViewHolder {
		private TextView pop_area;
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
			if (vid == viewHolder.pop_area.getId()) {
				setSelected(position);
				BusEvent.AREA_SELECT_EVENT.data = cityBeans.get(position);
				ZteApplication.bus.post(BusEvent.AREA_SELECT_EVENT);
			}
		}
	}
}