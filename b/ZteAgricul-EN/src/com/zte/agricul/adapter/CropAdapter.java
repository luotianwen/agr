package com.zte.agricul.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zte.agricul.zh.R;
import com.zte.agricul.bean.CropBean;
import com.zte.agricul.view.GridViewToScrollView;

public class CropAdapter extends BaseAdapter {
	// private Context mContext;
	private List<CropBean>  mBeans;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private ViewHolder viewHolder = null;
	int unreadNum ;
	public CropAdapter(Context mContext, List<CropBean> mBeans) {
		super();
		this.mContext = mContext;
		this.mBeans = mBeans;
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
		View viewRoot = convertView;
		if (viewRoot == null) {
			viewRoot = mLayoutInflater.inflate(R.layout.item_crops, null);
			viewHolder = new ViewHolder();
			viewHolder.txt_title = (TextView) viewRoot
					.findViewById(R.id.textView2);
			viewRoot.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) viewRoot.getTag();
		}
		viewHolder.txt_title.setText(mBeans.get(position).getCropTypeName());
		return viewRoot;
	}

	private class ViewHolder {
		private ImageView main_list_icon;
		private TextView txt_title, txt_capacity, unread_find_number;
		private RelativeLayout fault_msg ;
		private GridViewToScrollView     main_data_list ; 
	}
	
//	class lvButtonListener implements OnClickListener {
//		private int position;
//
//		lvButtonListener(int pos) {
//			position = pos;
//		}
//		@Override
//		public void onClick(View v) {
//			int vid = v.getId();
//			if (vid == viewHolder.fault_msg.getId()) {
//				Intent intent = new Intent(mContext,FaultListActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.putExtra("id", mBeans.get(position).getID());
//				mContext.startActivity(intent);
//			} 
//		}
//	}
}
