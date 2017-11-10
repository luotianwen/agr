package com.zte.agricul.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zte.agricul.R;
import com.zte.agricul.adapter.AgriculListAdapter;
import com.zte.agricul.app.Constants;
import com.zte.agricul.bean.AgriculMainInfo;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.util.Util;

public class SearchActivity extends Activity implements OnClickListener {
	private TextView leftTv, ivTitleName, rightTv;
	private Button search_btn;
//	private LoadingDialog dialog;
	public Gson gson;
//	private SearchListBean mBean;
	private  EditText search_edit ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
//		dialog = new LoadingDialog(this);
		gson = new Gson();
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		leftTv = (TextView) findViewById(R.id.leftTv);
		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
		rightTv = (TextView) findViewById(R.id.rightTv);
		ivTitleName.setText(R.string.search);
		search_edit= (EditText) findViewById(R.id.search_edit);
		leftTv.setVisibility(View.VISIBLE);
		leftTv.setOnClickListener(this);
		search_btn=  (Button) findViewById(R.id.search_btn);
		search_btn.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_btn:
			if ("".equals(search_edit.getText().toString())) {
				Util.showToast(getApplicationContext(), getResources().getString(R.string.input_search_content), Toast.LENGTH_SHORT);
				return ;
			}
			Intent  intent = new Intent(getApplicationContext(),SearchListActivity.class);
			intent.putExtra("search", search_edit.getText().toString());
			startActivity(intent);
			break;
		case R.id.leftTv:
			finish();
			break;
		default:
			break;
		}
	}
}
