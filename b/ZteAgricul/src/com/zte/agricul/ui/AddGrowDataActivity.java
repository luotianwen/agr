package com.zte.agricul.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zte.agricul.R;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.util.Util;
import com.zte.agricul.view.DateTimePickDialogUtil;

//public class AddGrowDataActivity extends BaseActivity implements OnClickListener {
//	private TextView leftTv, ivTitleName, rightTv;
//	private ListView mListView;
//	List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
//	public HorizontalScrollView mTouchView;
//	// 装入所有的HScrollView
//	protected List<CHTableScrollView> mHScrollViews = new ArrayList<CHTableScrollView>();
//	private TextView time_selected ;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_add_grow_data);
//		initView();
//	}
//
//	private void initView() {
//		// TODO Auto-generated method stub
//
//		leftTv = (TextView) findViewById(R.id.leftTv);
//		leftTv.setVisibility(View.VISIBLE);
//		leftTv.setOnClickListener(this);
//		ivTitleName = (TextView) findViewById(R.id.ivTitleName);
//		ivTitleName.setText(R.string.add_grow_data);
//		rightTv = (TextView) findViewById(R.id.rightTv);
//		rightTv.setVisibility(View.VISIBLE);
//		rightTv.setOnClickListener(this);
//		rightTv.setText(R.string.save);
//		rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);
//
//		time_selected = (TextView) findViewById(R.id.time_selected);
//		time_selected.setOnClickListener(this);
//		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
//		String date = sDateFormat.format(new java.util.Date()); 
//		time_selected.setText(date);
//		
//		// Column
//		String[] cols = { "data_1", "data_2", "data_3", "data_4", "data_5",
//				"data_6", "data_7", "data_8", "data_9", };
//
//		
//		Map<String, String> data = null;
//		mListView = (ListView) findViewById(R.id.scroll_list);
//		for (int i = 0; i < 10; i++) {
//			data = new HashMap<String, String>();
//			if (i == 0) {
//				data.put(cols[0], "地块");
//				data.put(cols[1], "株高ｃｍ");
//				data.put(cols[2], "茎粗");
//				data.put(cols[3], "单株荚数个");
//				data.put(cols[4], "直径cm" + 4 + "_" + i);
//				data.put(cols[5], "叶长cm" + 5 + "_" + i);
//				data.put(cols[6], "果实成长" + 6 + "_" + i);
//				data.put(cols[7], "施肥" + 7 + "_" + i);
//				data.put(cols[8], "浇水" + 8 + "_" + i);
//			} else {
//				data.put(cols[0], "地块" + i);
//				data.put(cols[1], "Date_" + 1 + "_" + i);
//				data.put(cols[2], "Date_" + 2 + "_" + i);
//				data.put(cols[3], "Date_" + 3 + "_" + i);
//				data.put(cols[4], "Date_" + 4 + "_" + i);
//				data.put(cols[5], "Date_" + 5 + "_" + i);
//				data.put(cols[6], "Date_" + 6 + "_" + i);
//				data.put(cols[7], "Date_" + 7 + "_" + i);
//				data.put(cols[8], "Date_" + 8 + "_" + i);
//			}
//
//			datas.add(data);
//		}
//
//		// mColumnControls.clear();
//		// for(int i=0;i<cols.length;i++){
//		// //预留第一列
//		// if(i!=0){
//		// EditText etItem1 = new EditText(HTableActivity.this);
//		// etItem1.setWidth(50);
//		// etItem1.setTextColor(Color.DKGRAY);
//		// etItem1.setGravity(Gravity.CENTER);
//		// //
//		// mColumnControls.put(cols[i], etItem1);
//		// }
//		// }
//
//		BaseAdapter adapter = new ScrollAdapter2(this,
//				R.layout.row_item_edit, cols);
//		mListView.setAdapter(adapter);
//
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.leftTv:
//			finish();
//			break;
//		case R.id.rightTv:
//			Util.showToast(getApplicationContext(), getResources().getString(R.string.save),
//					Toast.LENGTH_SHORT);
//			break;
//		case R.id.time_selected:
//			DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
//					AddGrowDataActivity.this, "");
//			dateTimePicKDialog.dateTimePicKDialog(time_selected);
//			break;
//		default:
//			break;
//		}
//	}
//
//	public void addHViews(final CHTableScrollView hScrollView) {
//		if (!mHScrollViews.isEmpty()) {
//			int size = mHScrollViews.size();
//			CHTableScrollView scrollView = mHScrollViews.get(size - 1);
//			final int scrollX = scrollView.getScrollX();
//			// 第一次满屏后，向下滑动，有一条数据在开始时未加入
//			if (scrollX != 0) {
//				mListView.post(new Runnable() {
//					@Override
//					public void run() {
//						// 当listView刷新完成之后，把该条移动到最终位置
//						hScrollView.scrollTo(scrollX, 0);
//					}
//				});
//			}
//		}
//		mHScrollViews.add(hScrollView);
//	}
//
//	public void onScrollChanged(int l, int t, int oldl, int oldt) {
//		for (CHTableScrollView scrollView : mHScrollViews) {
//			// 防止重复滑动
//			if (mTouchView != scrollView)
//				scrollView.smoothScrollTo(l, t);
//		}
//	}
//
//	class ScrollAdapter2 extends BaseAdapter {
//		private int res;
//		private String[] from;
//		private Context context;
//
//		public ScrollAdapter2(Context context, int resource, String[] from) {
//			this.context = context;
//			this.res = resource;
//			this.from = from;
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return datas.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return datas.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			View v = convertView;
//			if (v == null) {
//				v = LayoutInflater.from(context).inflate(res, null);
//				View chsv = v.findViewById(R.id.item_scroll);
//				LinearLayout ll = (LinearLayout) chsv
//						.findViewById(R.id.item_scroll_layout);
//				View[] views = new View[from.length];
//
//				for (int i = 0; i < from.length; i++) {
//
//					View linearLay = newView(R.layout.row_item_edit_view,
//							from[i]);
//
//					/***
//					 * position =0 表示横排数据 i =0 表示 竖排数据
//					 */
//					EditText td = (EditText) linearLay.findViewById(R.id.ievEditView);
//
//					if (i == 0 || position == 0) {
//						td.setEnabled(false);
//					} else {
//						td.setEnabled(true);
//					}
//					td.setOnClickListener(clickListener);
//					ll.addView(linearLay);
//					views[i] = td;
//				}
//				v.setTag(views);
//
//				addHViews((CHTableScrollView) chsv);
//			}
//
//			View[] holders = (View[]) v.getTag();
//			int len = holders.length;
//			for (int i = 0; i < len; i++) {
//				((EditText) holders[i]).setText(datas.get(position)
//						.get(from[i]).toString());
//				((EditText) holders[i])
//						.addTextChangedListener(new textChangListener(position,i,from));
//			}
//
//			return v;
//		}
//	}
//
//	private View newView(int res_id, String tag_name) {
//
//		View itemView = LayoutInflater.from(AddGrowDataActivity.this).inflate(
//				res_id, null);
//		itemView.setTag(tag_name);
//
//		return itemView;
//	}
//
//	class textChangListener implements TextWatcher {
//		private int position;
//		private int i ;
//		private String[] from;
//		textChangListener(int pos,int i ,String[] from) {
//			this.position = pos;
//			this.i = i ; 
//			this.from = from ;
//		}
//
//		@Override
//		public void afterTextChanged(Editable arg0) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//				int arg3) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//				int arg3) {
//			// TODO Auto-generated method stub
//			datas.get(position).put(from[i], arg0.toString());
//			System.out.println(datas.get(position).get(from[i]).toString());
//		}
//
//	}
//
//	class ScrollAdapter extends SimpleAdapter {
//
//		private List<? extends Map<String, ?>> datas;
//		private int res;
//		private String[] from;
//		private int[] to;
//		private Context context;
//
//		public ScrollAdapter(Context context,
//				List<? extends Map<String, ?>> data, int resource,
//				String[] from, int[] to) {
//			super(context, data, resource, from, to);
//			this.context = context;
//			this.datas = data;
//			this.res = resource;
//			this.from = from;
//			this.to = to;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			View v = convertView;
//			if (v == null) {
//				v = LayoutInflater.from(context).inflate(res, null);
//				// 第一次初始化的时候装进来
//				addHViews((CHTableScrollView) v.findViewById(R.id.item_scroll));
//				View[] views = new View[to.length];
//				//
//				for (int i = 0; i < to.length; i++) {
//					View tv = v.findViewById(to[i]);
//					tv.setOnClickListener(clickListener);
//					views[i] = tv;
//				}
//				//
//				v.setTag(views);
//			}
//			View[] holders = (View[]) v.getTag();
//			int len = holders.length;
//			for (int i = 0; i < len; i++) {
//				((EditText) holders[i]).setText(this.datas.get(position)
//						.get(from[i]).toString());
//			}
//			return v;
//		}
//	}
//
//	// 测试点击的事件
//	protected View.OnClickListener clickListener = new View.OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			Toast.makeText(AddGrowDataActivity.this, ((EditText) v).getText(),
//					Toast.LENGTH_SHORT).show();
//		}
//	};
//}
