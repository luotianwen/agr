package com.zte.agricul.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zte.agricul.zh.R;
import com.zte.agricul.adapter.ListEditorAdapter;
import com.zte.agricul.adapter.ListEditorAdapter.ViewHolder;
import com.zte.agricul.bean.CropIndexBean;
import com.zte.agricul.fragment.PlotCropsGrowFragment;

public class aaaa extends Activity implements OnClickListener {
	private TextView leftTv, ivTitleName, rightTv;
	private RelativeLayout progressView;
	private TextView time_selected, land_name;
	private ListView listView;
	private ListEditorAdapter mAdapter;

	
	
	List<CropIndexBean>  cropIndex  = new ArrayList<CropIndexBean>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		
		cropIndex.addAll(PlotCropsGrowFragment.cropIndex);		
		initView();
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		progressView = (RelativeLayout) findViewById(R.id.layout_loading);
		progressView.setOnClickListener(this);

		leftTv = (TextView) findViewById(R.id.leftTv);
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setOnClickListener(this);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		ivTitleName.setText(R.string.add_grow_data);
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setVisibility(View.VISIBLE);
		rightTv.setOnClickListener(this);
		rightTv.setText(R.string.save);
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);

		time_selected = (TextView) findViewById(R.id.time_selected);
		time_selected.setOnClickListener(this);
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = sDateFormat.format(new java.util.Date());
		time_selected.setText(date);
		land_name = (TextView) findViewById(R.id.land_name);
		land_name.setText(getIntent().getStringExtra("landName"));
		
		
		listView = (ListView) findViewById(R.id.list);
		mAdapter = new ListEditorAdapter(this);
		listView.setAdapter(mAdapter);
		mAdapter.setData(cropIndex);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		}
	}
	class ListEditorAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private Context mContext;
		private List<CropIndexBean> mData; // 存储的editTex值

		public ListEditorAdapter(Context context) {
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(context);
		}
		
		public void setData( List<CropIndexBean> data) {
			mData = data;
		}


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (mData != null) {
				return mData.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		private Integer index = -1;

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Log.d("zhang", "position = " + position);
			final ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_add_grow_data, null);
				holder = new ViewHolder();
				holder.textView = (TextView) convertView.findViewById(R.id.text_name);
				holder.numEdit = (EditText) convertView.findViewById(R.id.edit_data);
				holder.numEdit.setTag(position);
				holder.numEdit.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_UP) {
							index = (Integer) v.getTag();
						}
						return false;
					}
				});

				class MyTextWatcher implements TextWatcher {

					public MyTextWatcher(ViewHolder holder) {
						mHolder = holder;
					}

					private ViewHolder mHolder;

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						if (s != null && !"".equals(s.toString())) {
							int position = (Integer) mHolder.numEdit.getTag();
							// 当EditText数据发生改变的时候存到data变量中
							mData.get(position).setData(s.toString());
						}
					}
				}
				holder.numEdit.addTextChangedListener(new MyTextWatcher(holder));
				
				
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.numEdit.setTag(position);
			}
			
			
			
			Object value = mData.get(position).getData();
			if (value != null && !"".equals(value)) {
				holder.numEdit.setHint("aaaaaa");
			} else {
				holder.numEdit.setHint("aaaaaa");
			}
			holder.numEdit.clearFocus();
			if (index != -1 && index == position) {
				holder.numEdit.requestFocus();
			}
			return convertView;
		}
		

		public class ViewHolder {
			TextView textView;
			EditText numEdit;

		}

	}

}
