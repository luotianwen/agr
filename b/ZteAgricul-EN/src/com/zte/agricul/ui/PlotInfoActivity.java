package com.zte.agricul.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.zte.agricul.zh.R;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.bean.PlotBean;

public class PlotInfoActivity extends BaseActivity implements OnClickListener {
	private TextView leftTv, ivTitleName, rightTv;
	
	private TextView plot_name  ,base_land_name,plot_des,plot_crop,plot_brand,plot_user
	,plot_time,plot_size,plot_output,plot_add,plot_add_con,plot_mark;
	
	private PlotBean plotBean ;
	
	private String landName;

	public void onCreate(Bundle savedinstancestate) {
		super.onCreate(savedinstancestate);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.plot_info_activity);
		landName=getIntent().getStringExtra("landName");
		plotBean = (PlotBean) getIntent().getSerializableExtra("plotBean");
		initView();
	};

	private void initView() {
		// TODO Auto-generated method stub
		leftTv = (TextView) findViewById(R.id.leftTv);
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setOnClickListener(this);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		ivTitleName.setText(getResources().getString(R.string.plot_base_info));
		rightTv = (TextView) findViewById(R.id.rightTv);
		rightTv.setOnClickListener(this);
		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);
		rightTv.setText(getResources().getString(R.string.save));
		rightTv.setVisibility(View.GONE);
		base_land_name=(TextView) findViewById(R.id.base_land_name);
		plot_name =(TextView) findViewById(R.id.plot_name);
		plot_des=(TextView) findViewById(R.id.plot_des);
		plot_crop=(TextView) findViewById(R.id.plot_crop);
		plot_brand=(TextView) findViewById(R.id.plot_brand);
		plot_user=(TextView) findViewById(R.id.plot_user);
		plot_time=(TextView) findViewById(R.id.plot_time);
		plot_size=(TextView) findViewById(R.id.plot_size);
		plot_output=(TextView) findViewById(R.id.plot_output);
		plot_add=(TextView) findViewById(R.id.plot_add);
		plot_add_con=(TextView) findViewById(R.id.plot_add_con);
		plot_mark=(TextView) findViewById(R.id.plot_mark);
		base_land_name.setText(landName);
		plot_name.setText(plotBean.getName());
		plot_des.setText(plotBean.getContent());
		plot_crop.setText(plotBean.getCropName());
		plot_brand.setText(plotBean.getBrandName());
		plot_user.setText(plotBean.getSow_UserName());
		plot_time.setText(plotBean.getSow_Time());
		plot_size.setText(plotBean.getSize());
		plot_output.setText(plotBean.getAnnualOutput());
		plot_add.setText(plotBean.getCrop_Additive());
		plot_add_con.setText(plotBean.getConcentration());
		plot_mark.setText(plotBean.getRemark());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.leftTv:
			finish();
			break;

		default:
			break;
		}
	}

}
