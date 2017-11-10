package com.zte.agricul.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zte.agricul.fragment.ViewImageDetailFragment;



/**
 * @author wush   
 * @date 2014-3-20
 * @version v1.0  
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {
	private final int mSize;

	public ImagePagerAdapter(FragmentManager fm, int size) {
		super(fm);
		mSize = size;
	}

	@Override
	public int getCount() {
		return mSize;
	}

	@Override
	public Fragment getItem(int position) {
		return ViewImageDetailFragment.newInstance(position);
	}

//	@Override
//	public void destroyItem(ViewGroup container, int position, Object object) {
//		final ViewImageDetailFragment fragment = (ViewImageDetailFragment) object;
//		// As the item gets destroyed we try and cancel any existing work.
////		fragment.cancelWork();
//		super.destroyItem(container, position, object);
//	}
}