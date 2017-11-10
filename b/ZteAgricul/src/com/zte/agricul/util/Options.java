package com.zte.agricul.util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class Options {

	private static DisplayImageOptions options;
	
	
	public static DisplayImageOptions bulidOptions( int drawable){
		
		return options = new DisplayImageOptions.Builder() 
		.showImageOnLoading(drawable)		// ����ͼƬ�����ڼ���ʾ��ͼƬ
		.showImageForEmptyUri(drawable)	// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
		.showImageOnFail(drawable)		// ����ͼƬ���ػ��������з���������ʾ��ͼƬ	
		.cacheInMemory(true)						// �������ص�ͼƬ�Ƿ񻺴����ڴ���
		.cacheOnDisk(true)							// �������ص�ͼƬ�Ƿ񻺴���SD����
		.build();
		
	}
}
