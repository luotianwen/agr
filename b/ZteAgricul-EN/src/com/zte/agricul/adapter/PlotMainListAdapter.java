package com.zte.agricul.adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zte.agricul.zh.R;
import com.zte.agricul.bean.PlotBean;
import com.zte.agricul.ui.PlotInfoActivity;
import com.zte.agricul.util.Options;

public class PlotMainListAdapter extends BaseAdapter {
	// private Context mContext;
	private List<PlotBean>  mBeans;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private ViewHolder viewHolder = null;
	int unreadNum ;
	ImageLoader imageLoader;
	private String landName;
	public PlotMainListAdapter(Context mContext, List<PlotBean> mBeans,ImageLoader imageLoader,String landName) {
		super();
		this.mContext = mContext;
		this.mBeans = mBeans;
		this.imageLoader=imageLoader;
		mLayoutInflater = LayoutInflater.from(mContext);
		this.landName =landName ;
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
			viewRoot = mLayoutInflater.inflate(R.layout.item_plot_main_list, null);
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
			viewHolder.plot_info_btn= (TextView) viewRoot
					.findViewById(R.id.plot_info_btn);
			viewRoot.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) viewRoot.getTag();
		}
		viewHolder.txt_title.setText(mBeans.get(position).getCropName());
		viewHolder.txt_time.setText(mContext.getResources().getString(R.string.sowing_time)+mBeans.get(position).getSow_Time());
		viewHolder.land_name.setText(mBeans.get(position).getName());
		viewHolder.brand_name.setText(mBeans.get(position).getBrandName());
		imageLoader.displayImage(mBeans.get(position).getCropImage(), viewHolder.item_crop_icon,Options.bulidOptions(R.drawable.bg_shuffling));
		viewHolder.plot_info_btn.setOnClickListener(new lvButtonListener(position));
		
		
		return viewRoot;
	}

	private class ViewHolder {
		private TextView txt_title, txt_time, land_name,brand_name;
		private ImageView item_crop_icon ;
		
		private TextView plot_info_btn ;
	}
	
	class lvButtonListener implements OnClickListener {
		private int position;

		lvButtonListener(int pos) {
			position = pos;
		}
		@Override
		public void onClick(View v) {
			int vid = v.getId();
			if (vid == viewHolder.plot_info_btn.getId()) {
				Intent intent = new Intent(mContext,PlotInfoActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				intent.putExtra("landName", landName);
				intent.putExtra("plotBean", (Serializable)mBeans.get(position));
				mContext.startActivity(intent);
			} 
		}
	}
}
