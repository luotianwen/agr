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
import com.zte.agricul.bean.AgriculListBean;
import com.zte.agricul.util.Options;
import com.zte.agricul.view.GridViewToScrollView;

public class AgriculListAdapter extends BaseAdapter {
	// private Context mContext;
	private List<AgriculListBean>  mBeans;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private ViewHolder viewHolder = null;
	int unreadNum ;
	private ImageLoader imageLoader ;
	public AgriculListAdapter(Context mContext, List<AgriculListBean> mBeans,ImageLoader imageLoader) {
		super();
		this.mContext = mContext;
		this.mBeans = mBeans;
		mLayoutInflater = LayoutInflater.from(mContext);
		this.imageLoader = imageLoader ; 
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
			viewRoot = mLayoutInflater.inflate(R.layout.item_main_list, null);
			viewHolder = new ViewHolder();
			viewHolder.txt_title = (TextView) viewRoot
					.findViewById(R.id.txt_title);
//			viewHolder.txt_capacity = (TextView) viewRoot
//					.findViewById(R.id.txt_capacity);
			viewHolder.main_list_icon = (ImageView) viewRoot
					.findViewById(R.id.main_list_icon);
			viewHolder.crop_item1 = (TextView) viewRoot
					.findViewById(R.id.crop_item1);
			viewHolder.crop_item2 = (TextView) viewRoot
					.findViewById(R.id.crop_item2);
			viewHolder.crop_item3 = (TextView) viewRoot
					.findViewById(R.id.crop_item3);
			viewHolder.crop_img_item1 = (ImageView) viewRoot
					.findViewById(R.id.crop_img_item1);
			viewHolder.crop_img_item2 = (ImageView) viewRoot
					.findViewById(R.id.crop_img_item2);
			viewHolder.crop_img_item3 = (ImageView) viewRoot
					.findViewById(R.id.crop_img_item3);
			viewRoot.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) viewRoot.getTag();
		}
		viewHolder.txt_title.setText(mBeans.get(position).getName());
		imageLoader.displayImage(mBeans.get(position).getImagePath(), viewHolder.main_list_icon,Options.bulidOptions(R.drawable.default_img_loading_bg));
		
		if (mBeans.get(position).getCropList().size()==0) {
			viewHolder.crop_item1.setVisibility(View.GONE);
			viewHolder.crop_item2.setVisibility(View.GONE);
			viewHolder.crop_item3.setVisibility(View.GONE);
		}else if (mBeans.get(position).getCropList().size()==1){
			viewHolder.crop_item1.setVisibility(View.VISIBLE);
			viewHolder.crop_item2.setVisibility(View.GONE);
			viewHolder.crop_item3.setVisibility(View.GONE);
			viewHolder.crop_item1.setText(mBeans.get(position).getCropList().get(0).getCropTypeName());
			if ("1".equals(mBeans.get(position).getCropList().get(0).getIsnew())) {
				viewHolder.crop_img_item1.setVisibility(View.VISIBLE);
			}else {
				viewHolder.crop_img_item1.setVisibility(View.GONE);
			}
		
		}else if (mBeans.get(position).getCropList().size()==2){
			viewHolder.crop_item1.setVisibility(View.VISIBLE);
			viewHolder.crop_item2.setVisibility(View.VISIBLE);
			viewHolder.crop_item3.setVisibility(View.GONE);
			viewHolder.crop_item1.setText(mBeans.get(position).getCropList().get(0).getCropTypeName());
			viewHolder.crop_item2.setText(mBeans.get(position).getCropList().get(1).getCropTypeName());
			if ("1".equals(mBeans.get(position).getCropList().get(0).getIsnew())) {
				viewHolder.crop_img_item1.setVisibility(View.VISIBLE);
			}else {
				viewHolder.crop_img_item1.setVisibility(View.GONE);
			}
			if ("1".equals(mBeans.get(position).getCropList().get(1).getIsnew())) {
				viewHolder.crop_img_item2.setVisibility(View.VISIBLE);
			}else {
				viewHolder.crop_img_item2.setVisibility(View.GONE);
			}
		
		}else if (mBeans.get(position).getCropList().size()>=3){
			viewHolder.crop_item1.setVisibility(View.VISIBLE);
			viewHolder.crop_item2.setVisibility(View.VISIBLE);
			viewHolder.crop_item3.setVisibility(View.VISIBLE);
			viewHolder.crop_item1.setText(mBeans.get(position).getCropList().get(0).getCropTypeName());
			viewHolder.crop_item2.setText(mBeans.get(position).getCropList().get(1).getCropTypeName());
			viewHolder.crop_item3.setText(mBeans.get(position).getCropList().get(2).getCropTypeName());
			if ("1".equals(mBeans.get(position).getCropList().get(0).getIsnew())) {
				viewHolder.crop_img_item1.setVisibility(View.VISIBLE);
			}else {
				viewHolder.crop_img_item1.setVisibility(View.GONE);
			}
			if ("1".equals(mBeans.get(position).getCropList().get(1).getIsnew())) {
				viewHolder.crop_img_item2.setVisibility(View.VISIBLE);
			}else {
				viewHolder.crop_img_item2.setVisibility(View.GONE);
			}
			if ("1".equals(mBeans.get(position).getCropList().get(2).getIsnew())) {
				viewHolder.crop_img_item3.setVisibility(View.VISIBLE);
			}else {
				viewHolder.crop_img_item3.setVisibility(View.GONE);
			}
		}
		
//		viewHolder.txt_capacity.setText(mBeans.get(position).getVolume()+"KW");
//		viewHolder.main_list_icon.setImageUrl(mBeans.get(position).getImagePath());
//		viewHolder.unread_find_number.setText(mBeans.get(position).getWaring());
//		if ("".equals(mBeans.get(position).getWaring())) {
//			unreadNum = 0 ;
//		}else {
//			unreadNum=Integer.valueOf(mBeans.get(position).getWaring());
//		}
//		if (unreadNum <= 0) {
//			viewHolder.unread_find_number.setVisibility(View.GONE);
//		} else {
//			viewHolder.unread_find_number.setVisibility(View.VISIBLE);
//			int num = unreadNum >= 100 ? 99 : unreadNum ;
//			viewHolder.unread_find_number.setText(""+num);
//		}
//		viewHolder.fault_msg.setOnClickListener(new lvButtonListener(position));
		return viewRoot;
	}

	private class ViewHolder {
		private ImageView main_list_icon;
		private TextView txt_title, txt_capacity, unread_find_number;
		private RelativeLayout fault_msg ;
		private GridViewToScrollView     main_data_list ; 
		
		private TextView crop_item1  ,crop_item2 ,crop_item3 ; 
		private ImageView crop_img_item1  ,crop_img_item2 ,crop_img_item3 ; 
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
