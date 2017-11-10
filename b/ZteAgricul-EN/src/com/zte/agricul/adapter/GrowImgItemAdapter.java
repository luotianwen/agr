package com.zte.agricul.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zte.agricul.zh.R;
import com.zte.agricul.bean.GrowthLogParentListBean;
import com.zte.agricul.util.Options;
import com.zte.agricul.view.GridViewToScrollView;

public class GrowImgItemAdapter extends BaseAdapter {
	// private Context mContext;
	private List<String >  mBeans;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private ViewHolder viewHolder = null;
	int unreadNum ;
	ImageLoader imageLoader;
	public GrowImgItemAdapter(Context mContext, List<String >   mBeans,ImageLoader imageLoader) {
		super();
		this.mContext = mContext;
		this.mBeans = mBeans;
		this.imageLoader =imageLoader ; 
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		// Log.d("TAG", "adapter.size() -" + mNewsListContent.size());
		return mBeans.size()>=3?3:mBeans.size();
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
			viewRoot = mLayoutInflater.inflate(R.layout.item_grow_img_grid, null);
			viewHolder = new ViewHolder();
			viewHolder.item_grow_img =  (ImageView) viewRoot
					.findViewById(R.id.item_grow_img);
			viewRoot.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) viewRoot.getTag();
		}
		imageLoader.displayImage(mBeans.get(position).toString(), viewHolder.item_grow_img,Options.bulidOptions(R.drawable.bg_shuffling));
		return viewRoot;
	}

	private class ViewHolder {
		private  ImageView item_grow_img;
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
