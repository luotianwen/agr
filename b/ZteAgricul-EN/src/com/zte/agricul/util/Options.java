package com.zte.agricul.util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class Options {

	private static DisplayImageOptions options;
	
	
	public static DisplayImageOptions bulidOptions( int drawable){
		
		return options = new DisplayImageOptions.Builder() 
		.showImageOnLoading(drawable)		// 设置图片下载期间显示的图片
		.showImageForEmptyUri(drawable)	// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(drawable)		// 设置图片加载或解码过程中发生错误显示的图片	
		.cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
		.cacheOnDisk(true)							// 设置下载的图片是否缓存在SD卡中
		.build();
		
	}
}
