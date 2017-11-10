package com.zte.agricul.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.zte.agricul.zh.R;

public class ListEditorAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private List<Map<String, String>> mData; // 存储的editTex值
	private Map<String, String> editorValue = new HashMap<String, String>();
	private static HashMap<Integer, Boolean> isSelected;

	public ListEditorAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
	}
	
	public void setData( List<Map<String, String>> data) {
		mData = data;
		init();
	}

	private void init() {
		editorValue.clear();
		isSelected = new HashMap<Integer, Boolean>();
		if (mData != null) {
			for (int i = 0; i < mData.size(); i++) {
				isSelected.put(i, false);
			}
		}
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
						mData.get(position).put("list_item_inputvalue",
								s.toString());
					}
				}
			}
			holder.numEdit.addTextChangedListener(new MyTextWatcher(holder));
			
			
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.numEdit.setTag(position);
		}
		
		
		
		Object value = mData.get(position).get("list_item_inputvalue");
		if (value != null && !"".equals(value)) {
			holder.numEdit.setText(value.toString());
		} else {
			holder.numEdit.setText("0");
		}
		holder.numEdit.clearFocus();
		if (index != -1 && index == position) {
			holder.numEdit.requestFocus();
		}
		return convertView;
	}
	
	public HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(HashMap<Integer,Boolean> isSelected) {
        this.isSelected = isSelected;
    }

	public class ViewHolder {
		TextView textView;
		EditText numEdit;

	}

}
