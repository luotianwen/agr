package com.zte.agricul.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zte.agricul.zh.R;
import com.zte.agricul.adapter.PopAreaGridListAdapter.lvButtonListener;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.bean.CityBean;
import com.zte.agricul.bean.CropTypeBean;

public class PopupWindowCropsListAdapter extends BaseAdapter {
	// private Context mContext;
	private List<CropTypeBean>  cropTypeBeans ;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private ViewHolder viewHolder = null;
	int unreadNum ;
	private int select = 0 ;
	private String selectName;
	int type  ;
	public PopupWindowCropsListAdapter(Context mContext,List<CropTypeBean>  cropTypeBeans,int type ,String selectName) {
		super();
		this.mContext = mContext;
		this.cropTypeBeans = cropTypeBeans;
		this.selectName =selectName;
		mLayoutInflater = LayoutInflater.from(mContext);
		this.type = type ;
		ZteApplication.bus.register(mContext);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		// Log.d("TAG", "adapter.size() -" + mNewsListContent.size());
		return cropTypeBeans.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cropTypeBeans.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View viewRoot = convertView;
		if (viewRoot == null) {
			viewRoot = mLayoutInflater.inflate(R.layout.item_pop_crops_list, null);
			viewHolder = new ViewHolder();
			viewHolder.item_pop_type = (TextView) viewRoot
					.findViewById(R.id.item_pop_type);
			viewRoot.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) viewRoot.getTag();
		}
//		System.out.println(selectName+"==="+cityBeans.get(position).getName());
//		if (selectName.equals("ÇøÓò")&&"È«²¿".equals(cityBeans.get(position))) {
//			viewHolder.pop_area.setBackgroundResource(R.drawable.pop_area_bg_selected);
//			viewHolder.pop_area.setTextColor(mContext.getResources().getColor(R.color.white));
//		}else if (selectName.equals(cityBeans.get(position).getName())) {
//			viewHolder.pop_area.setBackgroundResource(R.drawable.pop_area_bg_selected);
//			viewHolder.pop_area.setTextColor(mContext.getResources().getColor(R.color.white));
//		}else {
//			viewHolder.pop_area.setBackgroundResource(R.drawable.pop_area_bg);
//			viewHolder.pop_area.setTextColor(mContext.getResources().getColor(R.color.grey_7f));
//		}
		if (type==1) {
			
			if (selectName.equals(cropTypeBeans.get(position).getName())) {
				viewHolder.item_pop_type.setBackgroundResource(R.drawable.pop_area_bg_selected);
				viewHolder.item_pop_type.setTextColor(mContext.getResources().getColor(R.color.white));
			}else {
				viewHolder.item_pop_type.setBackgroundResource(R.drawable.pop_area_bg);
				viewHolder.item_pop_type.setTextColor(mContext.getResources().getColor(R.color.grey_7f));
			}
			
			viewHolder.item_pop_type.setText(cropTypeBeans.get(position).getName());
		}else  {
			viewHolder.item_pop_type.setText(cropTypeBeans.get(position).getSize());
			if (selectName.equals(cropTypeBeans.get(position).getSize())) {
				viewHolder.item_pop_type.setBackgroundResource(R.drawable.pop_area_bg_selected);
				viewHolder.item_pop_type.setTextColor(mContext.getResources().getColor(R.color.white));
			}else {
				viewHolder.item_pop_type.setBackgroundResource(R.drawable.pop_area_bg);
				viewHolder.item_pop_type.setTextColor(mContext.getResources().getColor(R.color.grey_7f));
			}
		}
		
		viewHolder.item_pop_type.setOnClickListener(new lvButtonListener(position,type));
		return viewRoot;
	}

	private class ViewHolder {
		private TextView item_pop_type;
	}

	private  void  setSelected(int select){
		this.select =select;
		notifyDataSetChanged();
	}
	
	class lvButtonListener implements OnClickListener {
		private int position;
		private int type ;

		lvButtonListener(int pos,int type) {
			this.position = pos;
			this.type =type;
		}

		@Override
		public void onClick(View v) {
			int vid = v.getId();
			if (vid == viewHolder.item_pop_type.getId()) {
				setSelected(position);
				if (type==1) {
					BusEvent.CROPS_SELECT_EVENT.data = cropTypeBeans.get(position);
					ZteApplication.bus.post(BusEvent.CROPS_SELECT_EVENT);
				}else if (type==2) {
					BusEvent.OUTPUT_SELECT_EVENT.data = cropTypeBeans.get(position);
					ZteApplication.bus.post(BusEvent.OUTPUT_SELECT_EVENT);
				}else {
					BusEvent.SIZE_SELECT_EVENT.data = cropTypeBeans.get(position);
					ZteApplication.bus.post(BusEvent.SIZE_SELECT_EVENT);
				}
				
			}
		}
	}
}