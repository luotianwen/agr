package com.zte.agricul.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zte.agricul.zh.R;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.Constants;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.bean.GrowthLogParentListBean;
import com.zte.agricul.bean.UploadResultBean;
import com.zte.agricul.ui.ViewImageActivity;
import com.zte.agricul.util.HttpUtil;
import com.zte.agricul.util.Logger;
import com.zte.agricul.util.NetworkUtil;
import com.zte.agricul.view.GridViewToScrollView;

public class GrowthLogListAdapter extends BaseAdapter {
	// private Context mContext;
	private List<GrowthLogParentListBean>  mBeans;
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private ViewHolder viewHolder = null;
	int unreadNum ;
	ImageLoader imageLoader;
	public static final String EXTRA_IMAGE = "extra_image";
	public static final String EXTRA_STR = "extra_str";
	private SharedPreferences sharedPreferences;
	String userPer = "";
	public GrowthLogListAdapter(Context mContext, List<GrowthLogParentListBean>   mBeans,ImageLoader imageLoader) {
		super();
		this.mContext = mContext;
		this.mBeans = mBeans;
		this.imageLoader=imageLoader;
		mLayoutInflater = LayoutInflater.from(mContext);
		sharedPreferences = mContext.getSharedPreferences(Constants.SHARE_PRE_FILE,
				Context.MODE_PRIVATE);
		userPer = sharedPreferences
				.getString(Constants.USER_PERMISSION, "");
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
			viewRoot = mLayoutInflater.inflate(R.layout.item_plot_grow_list, null);
			viewHolder = new ViewHolder();
			viewHolder.time_title1 = (TextView) viewRoot
					.findViewById(R.id.time_title1);
			viewHolder.time_title2 = (TextView) viewRoot
					.findViewById(R.id.time_title2);
			viewHolder.txt_time1 = (TextView) viewRoot
					.findViewById(R.id.txt_time1);
			viewHolder.txt_time2 = (TextView) viewRoot
					.findViewById(R.id.txt_time2);
			viewHolder.txt_content1 = (TextView) viewRoot
					.findViewById(R.id.txt_content1);
			viewHolder.txt_content2 = (TextView) viewRoot
					.findViewById(R.id.txt_content2);
			viewHolder.compare_time = (TextView) viewRoot
					.findViewById(R.id.compare_time);
			viewHolder.layout_grow_item_1=(LinearLayout) viewRoot
					.findViewById(R.id.layout_grow_item_1);
			viewHolder.layout_grow_item_2=(LinearLayout) viewRoot
					.findViewById(R.id.layout_grow_item_2);
			viewHolder.item_grow_img_grid1= (GridViewToScrollView) viewRoot
					.findViewById(R.id.item_grow_img_grid1);
			viewHolder.item_grow_img_grid2= (GridViewToScrollView) viewRoot
					.findViewById(R.id.item_grow_img_grid2);
			viewHolder.time_view1=  (ImageView) viewRoot
					.findViewById(R.id.time_view1);
			viewHolder.time_view2=  (ImageView) viewRoot
					.findViewById(R.id.time_view2);
			viewHolder.grow_type= (TextView) viewRoot
					.findViewById(R.id.grow_type);
			viewHolder.deletebBtn1= (ImageView) viewRoot
					.findViewById(R.id.log_delete1);
			viewHolder.deletebBtn2= (ImageView) viewRoot
					.findViewById(R.id.log_delete2);
			viewRoot.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) viewRoot.getTag();
		}
		if ("".equals(mBeans.get(position).getList().get(0).getTimeTitle())) {
			viewHolder.time_title1.setVisibility(View.GONE);
		}else {
			viewHolder.time_title1.setVisibility(View.VISIBLE);
			viewHolder.time_title1.setText(mBeans.get(position).getList().get(0).getTimeTitle());
		}
		
		if ("".equals(mBeans.get(position).getList().get(0).getTime())) {
			viewHolder.txt_time1.setVisibility(View.GONE);
			viewHolder.time_view1.setVisibility(View.GONE);
		}else {
			viewHolder.txt_time1.setVisibility(View.VISIBLE);
			viewHolder.txt_time1.setText(mBeans.get(position).getList().get(0).getTime());
			viewHolder.time_view1.setVisibility(View.VISIBLE);
		}
		if ("".equals(mBeans.get(position).getList().get(0).getContent())) {
			viewHolder.txt_content1.setVisibility(View.GONE);
		}else {
			viewHolder.txt_content1.setVisibility(View.VISIBLE);
			viewHolder.txt_content1.setText(mBeans.get(position).getList().get(0).getContent());
		}
		
		viewHolder.compare_time.setText(mBeans.get(position).getName());
		GrowImgItemAdapter adapter   = new GrowImgItemAdapter(mContext, mBeans.get(position).getList().get(0).getImgList(), imageLoader);
		viewHolder.item_grow_img_grid1.setAdapter(adapter);
		viewHolder.item_grow_img_grid1.setOnItemClickListener(new lvOnItemClickListener(mBeans.get(position).getList().get(0).getImgList()));
		if (mBeans.get(position).getList().size()>=2) {
			if ("".equals(mBeans.get(position).getList().get(1).getTimeTitle())) {
				viewHolder.time_title2.setVisibility(View.GONE);
			}else {
				viewHolder.time_title2.setVisibility(View.VISIBLE);
				viewHolder.time_title2.setText(mBeans.get(position).getList().get(1).getTimeTitle());
			}
			if ("".equals(mBeans.get(position).getList().get(1).getTime())) {
				viewHolder.txt_time2.setVisibility(View.GONE);
				viewHolder.time_view2.setVisibility(View.GONE);
			}else {
				viewHolder.txt_time2.setVisibility(View.VISIBLE);
				viewHolder.txt_time2.setText(mBeans.get(position).getList().get(1).getTime());
				viewHolder.time_view2.setVisibility(View.VISIBLE);
			}
			if ("".equals(mBeans.get(position).getList().get(1).getContent())) {
				viewHolder.txt_content2.setVisibility(View.GONE);
			}else {
				viewHolder.txt_content2.setVisibility(View.VISIBLE);
				viewHolder.txt_content2.setText(mBeans.get(position).getList().get(1).getContent());
			}
			viewHolder.layout_grow_item_2.setVisibility(View.VISIBLE);
			GrowImgItemAdapter adapter2   = new GrowImgItemAdapter(mContext, mBeans.get(position).getList().get(1).getImgList(), imageLoader);
			viewHolder.item_grow_img_grid2.setAdapter(adapter2);
			viewHolder.item_grow_img_grid2.setOnItemClickListener(new lvOnItemClickListener(mBeans.get(position).getList().get(1).getImgList()));
		}else {
			viewHolder.layout_grow_item_2.setVisibility(View.INVISIBLE);
		}
		
		
		if (userPer.contains("4")) {
			if ("".equals(mBeans.get(position).getList().get(0).getID())) {
				viewHolder.deletebBtn1.setVisibility(View.GONE);
			}else {
				viewHolder.deletebBtn1.setVisibility(View.VISIBLE);
			}
			if (mBeans.get(position).getList().size()>=2) {
				if ("".equals(mBeans.get(position).getList().get(1).getID())) {
					viewHolder.deletebBtn2.setVisibility(View.GONE);
				}else {
					viewHolder.deletebBtn2.setVisibility(View.VISIBLE);
				}
			}else {
				viewHolder.deletebBtn2.setVisibility(View.GONE);
			}
			
		}else {
			viewHolder.deletebBtn1.setVisibility(View.GONE);
			viewHolder.deletebBtn2.setVisibility(View.GONE);
		}
		viewHolder.deletebBtn1.setOnClickListener(new lvButtonListener(0,position));
		viewHolder.deletebBtn2.setOnClickListener(new lvButtonListener(1,position));
		
		if (!"".equals(mBeans.get(position).getList().get(0).getGrowth_Pro_Name())) {
			viewHolder.grow_type.setText(mBeans.get(position).getList().get(0).getGrowth_Pro_Name());
		}else {
			viewHolder.grow_type.setText(mBeans.get(position).getList().get(1).getGrowth_Pro_Name());
		}
		
		return viewRoot;
	}

	private class ViewHolder {
		
		private  TextView time_title1 ,time_title2 ;
		private  TextView txt_time1 ,txt_time2 ;
		private  TextView txt_content1 ,txt_content2;
		private  LinearLayout layout_grow_item_1,layout_grow_item_2;
		private  TextView compare_time;
		private  TextView grow_type ;
		private GridViewToScrollView item_grow_img_grid1,item_grow_img_grid2;
		private ImageView time_view1 ,time_view2 ;
		private ImageView deletebBtn1 ,deletebBtn2;
	}
	
	class lvOnItemClickListener implements OnItemClickListener {
		private List<String>  imgList;
		lvOnItemClickListener(List<String>  imgList) {
			this.imgList = imgList;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			for (int i = 0; i < imgList.size(); i++) {
				 System.out.println("aaaaaaaaaaa===="+imgList.get(i).toString());
			}
			Intent  intent  = new  Intent(mContext , ViewImageActivity.class);
			intent.putStringArrayListExtra(EXTRA_STR, (ArrayList<String>) imgList);
			intent.putExtra(EXTRA_IMAGE, 0);
			mContext.startActivity(intent);
			
		}
		
		
	}
	class lvButtonListener implements OnClickListener {
		private int position;
		private int type;
		lvButtonListener(int type ,int pos) {
			position = pos;
			this.type =type ;
		}
		@Override
		public void onClick(View v) {
			if (type==0) {
				BusEvent.DELETE_LOG_EVENT.data=mBeans.get(position).getList().get(0).getID();
			}else {
				BusEvent.DELETE_LOG_EVENT.data=mBeans.get(position).getList().get(1).getID();
			}
			ZteApplication.bus.post(BusEvent.DELETE_LOG_EVENT);
		}
	}
	

}
