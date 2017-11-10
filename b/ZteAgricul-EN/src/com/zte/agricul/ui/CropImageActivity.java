package com.zte.agricul.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.king.photo.img.CropImage;
import com.king.photo.img.CropImageView;
import com.zte.agricul.zh.R;
import com.zte.agricul.base.BaseActivity;
import com.zte.agricul.util.Util;



/**
 * �ü�����
 *
 */
public class CropImageActivity extends BaseActivity implements OnClickListener{
	
	private CropImageView mImageView;
	private Bitmap mBitmap;
	
	private CropImage mCrop;
	
	private Button mSave;
	private Button mCancel,rotateLeft,rotateRight;
	private String mPath = "CropImageActivity";
	private String TAG = "";
	public int screenWidth = 0;
	public int screenHeight = 0;
	
	private ProgressBar mProgressBar;
	
	public static final int SHOW_PROGRESS = 2000;

	public static final int REMOVE_PROGRESS = 2001;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SHOW_PROGRESS:
				mProgressBar.setVisibility(View.VISIBLE);
				break;
			case REMOVE_PROGRESS:
				mHandler.removeMessages(SHOW_PROGRESS);
				mProgressBar.setVisibility(View.INVISIBLE);
				break;
			}

		}
	};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gl_modify_avatar);
        init();
    }
    @Override
    protected void onStop(){
    	super.onStop();
    	if(mBitmap!=null){
    		mBitmap=null;
    	}
    }
    
    private void init()
    {
    	getWindowWH();
    	mPath = getIntent().getStringExtra("path");
        mImageView = (CropImageView) findViewById(R.id.gl_modify_avatar_image);
        mSave = (Button) this.findViewById(R.id.gl_modify_avatar_save);
        mCancel = (Button) this.findViewById(R.id.gl_modify_avatar_cancel);
        rotateLeft = (Button) this.findViewById(R.id.gl_modify_avatar_rotate_left);
        rotateRight = (Button) this.findViewById(R.id.gl_modify_avatar_rotate_right);
        mSave.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        rotateLeft.setOnClickListener(this);
        rotateRight.setOnClickListener(this);
        try{
            mBitmap = createBitmap(mPath,screenWidth,screenHeight);
            if(mBitmap==null){
            	Util.showToast(CropImageActivity.this, getResources().getString(R.string.data_no_ps),Toast.LENGTH_SHORT);
    			finish();
            }else{
            	resetImageView(mBitmap);
            }
        }catch (Exception e) {
        	Util.showToast(CropImageActivity.this, getResources().getString(R.string.data_no_ps),Toast.LENGTH_SHORT);
        	finish();
		}
        addProgressbar();       
    }
    /**
     * ��ȡ��Ļ�ĸߺͿ�
     */
    private void getWindowWH(){
		DisplayMetrics dm=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth=dm.widthPixels;
		screenHeight=dm.heightPixels;
	}
    private void resetImageView(Bitmap b){
    	 mImageView.clear();
    	 mImageView.setImageBitmap(b);
         mImageView.setImageBitmapResetBase(b, true);
         if (0==getIntent().getIntExtra("type", 0)) {
        	 mCrop = new CropImage(this, mImageView,mHandler,300,300);
		}else {
			 mCrop = new CropImage(this, mImageView,mHandler,300,500);
		}
         mCrop.crop(b);
    }
    
    public void onClick(View v)
    {
    	switch (v.getId())
    	{
    	case R.id.gl_modify_avatar_cancel:
//    		mCrop.cropCancel();
    		finish();
    		break;
    	case R.id.gl_modify_avatar_save:
    		String path = mCrop.saveToLocal(mCrop.cropAndSave());
    		Intent intent = new Intent();
    		intent.putExtra("path", path);
    		setResult(RESULT_OK, intent);
    		finish();
    		break;
    	case R.id.gl_modify_avatar_rotate_left:
    		mCrop.startRotate(270.f);
    		break;
    	case R.id.gl_modify_avatar_rotate_right:
    		mCrop.startRotate(90.f);
    		break;
    		
    	}
    }
    protected void addProgressbar() {
		mProgressBar = new ProgressBar(this);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		addContentView(mProgressBar, params);
		mProgressBar.setVisibility(View.INVISIBLE);
	}
    
    public Bitmap createBitmap(String path,int w,int h){
    	try{
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// ���������������Ĺؼ���inJustDecodeBounds��Ϊtrueʱ����ΪͼƬ�����ڴ档
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// ��ȡͼƬ��ԭʼ���
			int srcHeight = opts.outHeight;// ��ȡͼƬԭʼ�߶�
			int destWidth = 0;
			int destHeight = 0;
			// ���ŵı���
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// �������������ź��ͼƬ��С��maxLength�ǳ�����������󳤶�
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// ���ŵı����������Ǻ��Ѱ�׼���ı����������ŵģ�Ŀǰ��ֻ����ֻ��ͨ��inSampleSize���������ţ���ֵ�������ŵı�����SDK�н�����ֵ��2��ָ��ֵ
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds��Ϊfalse��ʾ��ͼƬ�����ڴ���
			newOpts.inJustDecodeBounds = false;
			// ���ô�С�����һ���ǲ�׼ȷ�ģ�����inSampleSize��Ϊ׼���������������ȴ��������
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// ��ȡ���ź�ͼƬ
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
    }
   
}