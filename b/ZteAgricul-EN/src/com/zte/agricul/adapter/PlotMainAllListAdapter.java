package com.zte.agricul.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zte.agricul.zh.R;
import com.zte.agricul.bean.PlotBean;
import com.zte.agricul.view.DeleteListView;

public class PlotMainAllListAdapter extends BaseAdapter {
	// private Context mContext;
	private List<PlotBean>  mBeans;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private ViewHolder viewHolder = null;
	int unreadNum ;
	private DeleteListView mListView ;
	public PlotMainAllListAdapter(Context mContext, List<PlotBean> mBeans,DeleteListView mListView) {
		super();
		this.mContext = mContext;
		this.mBeans = mBeans;
		mLayoutInflater = LayoutInflater.from(mContext);
		this.mListView = mListView ;
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
		View viewRoot = convertView;
		if (viewRoot == null) {
			viewRoot = mLayoutInflater.inflate(R.layout.item_plot_main_all_list, null);
			viewHolder = new ViewHolder();
			viewHolder.item_crop_icon= (ImageView) viewRoot
					.findViewById(R.id.item_crop_icon);
			viewHolder.txt_title = (TextView) viewRoot
					.findViewById(R.id.txt_title);
			viewHolder.txt_time = (TextView) viewRoot
					.findViewById(R.id.txt_time);
			viewHolder.land_name = (TextView) viewRoot
					.findViewById(R.id.land_name);
			viewHolder.brand_name = (TextView) viewRoot
					.findViewById(R.id.brand_name);
			
			viewHolder.delete = (TextView) viewRoot.findViewById(R.id.delete);
			viewRoot.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) viewRoot.getTag();
		}
		viewHolder.delete.setOnClickListener(new lvButtonListener(position));
		viewHolder.txt_title.setText(mBeans.get(position).getCropName());
		viewHolder.txt_time.setText(mContext.getResources().getString(R.string.sowing_time)+mBeans.get(position).getSow_Time());
		viewHolder.land_name.setText(mBeans.get(position).getName());
		viewHolder.brand_name.setText(mBeans.get(position).getBrandName());
		switch (Integer.valueOf(mBeans.get(position).getCrop_Type_ID())) {
		case 1:
			viewHolder.item_crop_icon.setImageResource(R.drawable.item_plot_i1);
			break;
		case 2:
			viewHolder.item_crop_icon.setImageResource(R.drawable.item_plot_i2);
			break;
		case 3:
			viewHolder.item_crop_icon.setImageResource(R.drawable.item_plot_i3);
			break;
		}
		return viewRoot;
	}

	private class ViewHolder {
		private TextView txt_title, txt_time, land_name,brand_name;
		private ImageView item_crop_icon ;
		private TextView  delete ;
	}
	
	class lvButtonListener implements OnClickListener {
		private int position;

		lvButtonListener(int pos) {
			position = pos;
		}
		@Override
		public void onClick(View v) {
			int vid = v.getId();
			if (vid == viewHolder.delete.getId()) {
				mBeans.remove(position);
				notifyDataSetChanged();
				mListView.turnToNormal();
			} 
		}
	}
}
