package com.zte.agricul.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.king.photo.util.AlbumHelper;
import com.king.photo.util.Bimp;
import com.king.photo.util.ImageBucket;
import com.king.photo.util.ImageItem;
import com.king.photo.util.PublicWay;
import com.king.photo.util.Res;
import com.zte.agricul.R;
import com.zte.agricul.adapter.AlbumGridViewAdapter;

/**
 * ����ǽ��������ʾ����ͼƬ�Ľ���
 * 
 * @author king
 * @QQ:595163260
 * @version 2014��10��18�� ����11:47:15
 */
public class AlbumActivity extends Activity {
	// ��ʾ�ֻ��������ͼƬ���б�ؼ�
	private GridView gridView;
	// ���ֻ���û��ͼƬʱ����ʾ�û�û��ͼƬ�Ŀؼ�
	private TextView tv;
	// gridView��adapter
	private AlbumGridViewAdapter gridImageAdapter;
	// ��ɰ�ť
	private Button okButton;
	// ���ذ�ť
	private Button back;
	// ȡ����ť
	private Button cancel;
	private Intent intent;
	// Ԥ����ť
	private Button preview;
	private Context mContext;
	private ArrayList<ImageItem> dataList;
	private AlbumHelper helper;
	public static List<ImageBucket> contentList;
	public static Bitmap bitmap;

	private int type ;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_album);
		type = getIntent().getIntExtra("type", 0);
		PublicWay.activityList.add(this);
		mContext = this;
		// ע��һ���㲥������㲥��Ҫ��������GalleryActivity����Ԥ��ʱ����ֹ������ͼƬ��ɾ������ٻص���ҳ��ʱ��ȡ��ѡ�е�ͼƬ�Դ���ѡ��״̬
		IntentFilter filter = new IntentFilter("data.broadcast.action");
		registerReceiver(broadcastReceiver, filter);
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.plugin_camera_no_pictures);
		init();
		initListener();
		// ���������Ҫ��������Ԥ������ɰ�ť��״̬
		isShowOkBt();
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// mContext.unregisterReceiver(this);
			// TODO Auto-generated method stub
			gridImageAdapter.notifyDataSetChanged();
		}
	};

	// Ԥ����ť�ļ���
	private class PreviewListener implements OnClickListener {
		public void onClick(View v) {
			int size = 0 ;
			if (type==1) {
				size = Bimp.tempSelectBitmap1.size()  ;
			}else {
				size = Bimp.tempSelectBitmap2.size()  ;
			}
			
			
			if (size > 0) {
				intent.putExtra("position", "1");
				intent.setClass(AlbumActivity.this, GalleryActivity.class);
				startActivity(intent);
			}
		}

	}

	// ��ɰ�ť�ļ���
	private class AlbumSendListener implements OnClickListener {
		public void onClick(View v) {
			overridePendingTransition(R.anim.activity_translate_in,
					R.anim.activity_translate_out);
//			intent.setClass(mContext, AddGrowProcessActivity.class);
//			startActivity(intent);
			finish();
		}

	}

	// ���ذ�ť����
	private class BackListener implements OnClickListener {
		public void onClick(View v) {
			// intent.setClass(AlbumActivity.this, ImageFile.class);
			// startActivity(intent);
			Bimp.tempSelectBitmap1.clear();
			Bimp.max1 = 0;
			Bimp.tempSelectBitmap2.clear();
			Bimp.max2 = 0;
			finish();
		}
	}

	// ȡ����ť�ļ���
	private class CancelListener implements OnClickListener {
		public void onClick(View v) {
			Bimp.tempSelectBitmap1.clear();
			Bimp.max1 = 0;
			Bimp.tempSelectBitmap2.clear();
			Bimp.max2 = 0;
			finish();
			// intent.setClass(mContext, FaultEditActivity.class);
			// startActivity(intent);
		}
	}

	// ��ʼ������һЩ����ֵ
	private void init() {
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		contentList = helper.getImagesBucketList(false);
		dataList = new ArrayList<ImageItem>();
		for (int i = 0; i < contentList.size(); i++) {
			dataList.addAll(contentList.get(i).imageList);
		}
		Collections.reverse(dataList);
		back = (Button) findViewById(R.id.back);
		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new CancelListener());
		back.setOnClickListener(new BackListener());
		preview = (Button) findViewById(R.id.preview);
		preview.setOnClickListener(new PreviewListener());
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		gridView = (GridView) findViewById(R.id.myGrid);
		if (type==1) {
			gridImageAdapter = new AlbumGridViewAdapter(this, dataList,Bimp.tempSelectBitmap1);
		}else {
			gridImageAdapter = new AlbumGridViewAdapter(this, dataList,Bimp.tempSelectBitmap2);
		}
		
		gridView.setAdapter(gridImageAdapter);
		tv = (TextView) findViewById(R.id.myText);
		gridView.setEmptyView(tv);
		okButton = (Button) findViewById(R.id.ok_button);
		if (type==1) {
			okButton.setText( getResources().getString(R.string.finish)+"("+ Bimp.tempSelectBitmap1.size() + "/" + PublicWay.num + ")");
		}else {
			okButton.setText( getResources().getString(R.string.finish)+"("+ Bimp.tempSelectBitmap2.size() + "/" + PublicWay.num + ")");
		}
		
	}

	private void initListener() {

		gridImageAdapter
				.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(final ToggleButton toggleButton,
							int position, boolean isChecked, Button chooseBt) {
						int size = 0 ;
						if (type==1) {
							size = Bimp.tempSelectBitmap1.size();
						}else {
							size = Bimp.tempSelectBitmap2.size();
						}
						if (size>= PublicWay.num) {
							toggleButton.setChecked(false);
							chooseBt.setVisibility(View.GONE);
							if (!removeOneData(dataList.get(position))) {
								Toast.makeText(AlbumActivity.this,
										getResources().getString(R.string.only_choose_num), 200)
										.show();
							}
							return;
						}
						if (isChecked) {
							chooseBt.setVisibility(View.VISIBLE);
							if (type==1) {
								Bimp.tempSelectBitmap1.add(dataList.get(position));
								okButton.setText(getResources().getString(R.string.finish) + "("
										+ Bimp.tempSelectBitmap1.size() + "/"+ PublicWay.num + ")");
							}else {
								Bimp.tempSelectBitmap2.add(dataList.get(position));
								okButton.setText(getResources().getString(R.string.finish) + "("
										+ Bimp.tempSelectBitmap2.size() + "/"+ PublicWay.num + ")");
							}
							
						} else {
							if (type==1) {
								Bimp.tempSelectBitmap1.remove(dataList.get(position));
								chooseBt.setVisibility(View.GONE);
								okButton.setText(getResources().getString(R.string.finish) + "("
										+ Bimp.tempSelectBitmap1.size() + "/"
										+ PublicWay.num + ")");
							}else {
								Bimp.tempSelectBitmap2.remove(dataList.get(position));
								chooseBt.setVisibility(View.GONE);
								okButton.setText(getResources().getString(R.string.finish) + "("
										+ Bimp.tempSelectBitmap2.size() + "/"
										+ PublicWay.num + ")");
							}
							
						}
						isShowOkBt();
					}
				});

		okButton.setOnClickListener(new AlbumSendListener());

	}

	private boolean removeOneData(ImageItem imageItem) {
		if (type==1) {
			if (Bimp.tempSelectBitmap1.contains(imageItem)) {
				Bimp.tempSelectBitmap1.remove(imageItem);
				okButton.setText(getResources().getString(R.string.finish) + "("
						+ Bimp.tempSelectBitmap1.size() + "/" + PublicWay.num + ")");
				return true;
			}
		}else {
			if (Bimp.tempSelectBitmap2.contains(imageItem)) {
				Bimp.tempSelectBitmap2.remove(imageItem);
				okButton.setText(getResources().getString(R.string.finish) + "("
						+ Bimp.tempSelectBitmap2.size() + "/" + PublicWay.num + ")");
				return true;
			}
		}
		
		

		return false;
	}

	public void isShowOkBt() {
		int size = 0 ;
		if (type==1) {
			size = Bimp.tempSelectBitmap1.size() ;
		}else {
			size = Bimp.tempSelectBitmap2.size() ;
		}
		
		if (size > 0) {
			okButton.setText(getResources().getString(R.string.finish) + "("
					+ size + "/" + PublicWay.num + ")");
			preview.setPressed(true);
			okButton.setPressed(true);
			preview.setClickable(true);
			okButton.setClickable(true);
			okButton.setTextColor(Color.WHITE);
			preview.setTextColor(Color.WHITE);
		} else {
			okButton.setText(getResources().getString(R.string.finish) + "("
					+size + "/" + PublicWay.num + ")");
			preview.setPressed(false);
			preview.setClickable(false);
			okButton.setPressed(false);
			okButton.setClickable(false);
			okButton.setTextColor(Color.parseColor("#E1E0DE"));
			preview.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// intent.setClass(AlbumActivity.this, ImageFile.class);
			// startActivity(intent);
			// Bimp.tempSelectBitmap.clear();
			// Bimp.max = 0 ;
			finish();
		}
		return false;

	}

	@Override
	protected void onRestart() {
		isShowOkBt();
		super.onRestart();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}
}
