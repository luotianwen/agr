package com.zte.agricul.view;

import com.zte.agricul.zh.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


public class PopupWindowEditPlot extends PopupWindow {

	private Context context;
	private View mMenuView;
	private ImageView addOtherData ,addGrowData,addGrowGongxu;
	public PopupWindowEditPlot(Activity context, OnClickListener mOnClickListener) {
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.pop_window_add_plot, null);
		addGrowGongxu= (ImageView) mMenuView.findViewById(R.id.pop_add_grow_gongxu);
		addGrowData= (ImageView) mMenuView.findViewById(R.id.pop_add_grow_data);
		addOtherData = (ImageView) mMenuView.findViewById(R.id.pop_add_other_data);
		addOtherData.setOnClickListener(mOnClickListener);
		addGrowData.setOnClickListener(mOnClickListener);
		addGrowGongxu.setOnClickListener(mOnClickListener);
//		popTitle = (TextView) mMenuView.findViewById(R.id.pop_title);
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		this.setBackgroundDrawable(new BitmapDrawable());

	}

}