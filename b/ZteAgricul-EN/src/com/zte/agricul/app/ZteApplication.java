package com.zte.agricul.app;

import java.io.File;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.otto.Bus;

public class ZteApplication extends Application{
	private static Context mContext;
	public static Bus bus = new Bus();
	@Override
	public void onCreate() {
		super.onCreate();

		mContext = getApplicationContext();
		initImageLoader(mContext);
	}
	
	
	
	public static void initImageLoader(Context context) {
		// System.out.println("初始化ImageLoader");
		// File cacheDir = StorageUtils.getOwnCacheDirectory(context,
		// Constants.CACHE_PATH);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 2)

				// .diskCache(new UnlimitedDiskCache(cacheDir))
				.diskCache(
						new UnlimitedDiskCache(new File(Constants.CACHE_PATH)))
				.diskCacheSize(50 * 1024 * 1024)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 缓存一百张图片
				.diskCacheFileCount(150).writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}
}
