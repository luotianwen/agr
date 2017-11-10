package com.zte.agricul.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zte.agricul.zh.R;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.bean.ConturyBean;

public class AddLandConturyAdapter extends BaseAdapter {
	// private Context mContext;
	private List<ConturyBean> cityBeans;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private ViewHolder viewHolder = null;
	int unreadNum ;
	
	public AddLandConturyAdapter(Context mContext, List<ConturyBean> cityBeans) {
		super();
		this.mContext = mContext;
		this.cityBeans = cityBeans;
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
			viewRoot = mLayoutInflater.inflate(R.layout.item_crop_all_list, null);
			viewHolder = new ViewHolder();
			viewHolder.pop_area = (TextView) viewRoot
					.findViewById(R.id.pop_area);
			viewRoot.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) viewRoot.getTag();
		}
		viewHolder.pop_area.setText(cityBeans.get(position).getName());
		return viewRoot;
	}

	private class ViewHolder {
		private TextView pop_area;
	}

	
}