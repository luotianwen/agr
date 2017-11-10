package com.zte.agricul.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zte.agricul.R;
import com.zte.agricul.ui.ViewImageActivity;
import com.zte.agricul.util.Options;

/**
 * @author wush
 * @date 2014-3-20
 * @version v1.0
 */
public class ViewImageDetailFragment extends Fragment {

	private static final String IMAGE_DATA_EXTRA = "resId";
	private int mImageNum;
	private ImageView mImageView;
	private ImageLoader imageLoader;

	// private ImageWorker mImageWorker;

	/**
	 * Factory method to generate a new instance of the fragment given an image
	 * number.
	 * 
	 * @param imageNum
	 *            The image number within the parent adapter to load
	 * @return A new instance of ImageDetailFragment with imageNum extras
	 */
	public static ViewImageDetailFragment newInstance(int imageNum) {
		final ViewImageDetailFragment f = new ViewImageDetailFragment();

		final Bundle args = new Bundle();
		args.putInt(IMAGE_DATA_EXTRA, imageNum);
		f.setArguments(args);

		return f;
	}

	/**
	 * Empty constructor as per the Fragment documentation
	 */
	public ViewImageDetailFragment() {
	}

	/**
	 * Populate image number from extra, use the convenience factory method
	 * {@link ImageDetailFragment#newInstance(int)} to create this fragment.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageNum = getArguments() != null ? getArguments().getInt(
				IMAGE_DATA_EXTRA) : -1;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate and locate the main ImageView
		final View v = inflater.inflate(R.layout.image_detail_fragment,
				container, false);
		imageLoader = ImageLoader.getInstance();
		mImageView = (ImageView) v.findViewById(R.id.imageView);

		imageLoader.displayImage(ViewImageActivity.imageString.get(mImageNum)
				.toString(), mImageView, Options
				.bulidOptions(R.drawable.plugin_camera_no_pictures));
		// Bitmap bm = BitmapFactory.decodeFile(pathName);
		// WebImage webImage = new
		// WebImage(ViewImageActivity.imageString.get(mImageNum));
		// if(null!=webImage.getBitmapFromCache(getActivity())){
		// mImageView.setImageBitmap(webImage.getBitmap(getActivity()));
		// }else {
		// mImageView.setImageUrl(ViewImageActivity.imageString.get(mImageNum).toString(),
		// R.drawable.plugin_camera_no_pictures);
		// }
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (ViewImageActivity.class.isInstance(getActivity())) {
			// mImageWorker = ((ViewImageActivity)
			// getActivity()).getImageWorker();
			// //有问�?
			// mImageWorker.loadBitmap(MachineryInfoActivity.imageUrls[mImageNum],
			// mImageView);
			// mImageView.setImage(ViewImageActivity.imageString[mImageNum]);
			// mImageView.setImageUrl(ViewImageActivity.imageString[mImageNum]);

//			mImageView
//					.setImageUrl(ViewImageActivity.imageString.get(mImageNum));
			imageLoader.displayImage(ViewImageActivity.imageString.get(mImageNum)
					.toString(), mImageView, Options
					.bulidOptions(R.drawable.plugin_camera_no_pictures));
			// mImageView.setImageUrl(ViewImageActivity.imageBigString[mImageNum],
			// R.drawable.icon_t, R.drawable.icon_t);
			// mImageView.setImageUrl(game_Detail.getGicon(), R.drawable.icon_t,
			// R.drawable.icon_t);
			// mImageView.seti
		}

	}

	/**
	 * Cancels the asynchronous work taking place on the ImageView, called by
	 * the adapter backing the ViewPager when the child is destroyed.
	 */
	// public void cancelWork() {
	// // ImageWorker.cancelWork(mImageView);
	// mImageView.setImageDrawable(null);
	// mImageView = null;
	// }
}
