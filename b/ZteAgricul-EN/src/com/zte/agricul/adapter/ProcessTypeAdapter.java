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
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.bean.ProcessBean;

public class ProcessTypeAdapter extends BaseAdapter {
	// private Context mContext;
	private List<ProcessBean> mBeans;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private ViewHolder viewHolder = null;
	int unreadNum;
	private int select = 0;
	private String selectName;
	int type;

	public ProcessTypeAdapter(Context mContext, List<ProcessBean> mBeans) {
		super();
		this.mContext = mContext;
		this.mBeans = mBeans;
		this.selectName = selectName;
		mLayoutInflater = LayoutInflater.from(mContext);
		this.type = type;
		ZteApplication.bus.register(mContext);
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
			viewRoot = mLayoutInflater.inflate(R.layout.item_process_type,
					null);
			viewHolder = new ViewHolder();
			viewHolder.txt_process_type = (TextView) viewRoot
					.findViewById(R.id.txt_process_type);
			viewRoot.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) viewRoot.getTag();
		}

			
			if (select == position) {
				viewHolder.txt_process_type.setTextColor(mContext.getResources().getColor(R.color.green_tr_5e));
			}else {
				viewHolder.txt_process_type.setTextColor(mContext.getResources().getColor(R.color.grey));
			}
			viewHolder.txt_process_type.setText(mBeans.get(position).getName());
//		viewHolder.txt_process_type.setOnClickListener(new lvButtonListener(
//				position, type));
		return viewRoot;
	}

	private class ViewHolder {
		private TextView txt_process_type;
	}

	public void setSelected(int select) {
		this.select = select;
		notifyDataSetChanged();
	}

	class lvButtonListener implements OnClickListener {
		private int position;
		private int type;

		lvButtonListener(int pos, int type) {
			this.position = pos;
			this.type = type;
		}

		@Override
		public void onClick(View v) {
			int vid = v.getId();
			if (vid == viewHolder.txt_process_type.getId()) {
				setSelected(position);
//				if (type == 1) {
//					BusEvent.CROPS_SELECT_EVENT.data = cropTypeBeans
//							.get(position);
//					ZteApplication.bus.post(BusEvent.CROPS_SELECT_EVENT);
//				} else if (type == 2) {
//					BusEvent.OUTPUT_SELECT_EVENT.data = cropTypeBeans
//							.get(position);
//					ZteApplication.bus.post(BusEvent.OUTPUT_SELECT_EVENT);
//				} else {
//					BusEvent.SIZE_SELECT_EVENT.data = cropTypeBeans
//							.get(position);
//					ZteApplication.bus.post(BusEvent.SIZE_SELECT_EVENT);
//				}

			}
		}
	}
}