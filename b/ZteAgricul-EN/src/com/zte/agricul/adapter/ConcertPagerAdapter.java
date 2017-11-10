package com.zte.agricul.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ConcertPagerAdapter extends PagerAdapter {
	List<ImageView> list;
	public ConcertPagerAdapter() {
		
	}
	

	public ConcertPagerAdapter(List<ImageView> list) {
		super();
		this.list = list;
	}


	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		
		container.removeView(list.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(list.get(position));
		return list.get(position);
	}
	

}
