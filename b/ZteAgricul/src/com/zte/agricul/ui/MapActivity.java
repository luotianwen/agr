package com.zte.agricul.ui;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.CancelableCallback;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMap.OnMarkerDragListener;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.zte.agricul.R;
import com.zte.agricul.app.Constants;

public class MapActivity extends Activity implements OnClickListener,
		CancelableCallback {
	private static final int SCROLL_BY_PX = 100;
	private MapView mapView;
	private AMap aMap;
	private String name="";
	private String Lat = "";
	private String Lon = "";
	private LatLng location ;// �ɶ��о�γ��
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);
		Lat=getIntent().getStringExtra("Lat");
		Lon=getIntent().getStringExtra("Lon");
		name =getIntent().getStringExtra("name");
		location = new LatLng(Float.valueOf(Lat),Float.valueOf(Lon));
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// �˷���������д
		init();
	}

	/**
	 * ��ʼ��AMap����
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		// Button stopAnimation = (Button) findViewById(R.id.stop_animation);
		// stopAnimation.setOnClickListener(this);
		// ToggleButton animate = (ToggleButton) findViewById(R.id.animate);
		// animate.setOnClickListener(this);
		// Button Lujiazui = (Button) findViewById(R.id.Lujiazui);
		// Lujiazui.setOnClickListener(this);
		// Button Zhongguancun = (Button) findViewById(R.id.Zhongguancun);
		// Zhongguancun.setOnClickListener(this);
		// Button scrollLeft = (Button) findViewById(R.id.scroll_left);
		// scrollLeft.setOnClickListener(this);
		// Button scrollRight = (Button) findViewById(R.id.scroll_right);
		// scrollRight.setOnClickListener(this);
		// Button scrollUp = (Button) findViewById(R.id.scroll_up);
		// scrollUp.setOnClickListener(this);
		// Button scrollDown = (Button) findViewById(R.id.scroll_down);
		// scrollDown.setOnClickListener(this);
		// Button zoomIn = (Button) findViewById(R.id.zoom_in);
		// zoomIn.setOnClickListener(this);
		// Button zoomOut = (Button) findViewById(R.id.zoom_out);
		// zoomOut.setOnClickListener(this);
	}

	/**
	 * ����ͼ�����һ��marker
	 */
	private void setUpMap() {
		// aMap.addMarker(new MarkerOptions().position(Constants.CHENGDU).icon(
		// BitmapDescriptorFactory
		// .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(location);
		markerOptions.title(name);
		markerOptions.visible(true);
		// BitmapDescriptor bitmapDescriptor =
		// BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),
		// R.drawable.location_marker));
		// markerOptions.icon(bitmapDescriptor);
		Marker marker=aMap.addMarker(markerOptions);
		marker.showInfoWindow();// ����Ĭ����ʾһ��infowinfow
		changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
				location, 12, 0, 30)), null);
	}

	/**
	 * ���ݶ�����ť״̬�����ú���animateCamera��moveCamera���ı��������
	 */
	private void changeCamera(CameraUpdate update, CancelableCallback callback) {
		// boolean animated = ((CompoundButton) findViewById(R.id.animate))
		// .isChecked();
		// if (animated) {
		aMap.animateCamera(update, 1000, callback);
		// } else {
		// aMap.moveCamera(update);
		// }
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * ����������д
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * ���ݶ�����ť״̬�����ú���animateCamera��moveCamera���ı��������
	 */
	// private void changeCamera(CameraUpdate update, CancelableCallback
	// callback) {
	// boolean animated = ((CompoundButton) findViewById(R.id.animate))
	// .isChecked();
	// if (animated) {
	// aMap.animateCamera(update, 1000, callback);
	// } else {
	// aMap.moveCamera(update);
	// }
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * ���ֹͣ������ť��Ӧ�¼�
		 */
		// case R.id.stop_animation:
		// aMap.stopAnimation();
		// break;
		// /**
		// * �����ȥ�йش塱��ť��Ӧ�¼�
		// */
		// case R.id.Zhongguancun:
		// changeCamera(
		// CameraUpdateFactory.newCameraPosition(new CameraPosition(
		// Constants.ZHONGGUANCUN, 18, 0, 30)), null);
		// break;
		//
		// /**
		// * �����ȥ½���족��ť��Ӧ�¼�
		// */
		// case R.id.Lujiazui:
		// changeCamera(
		// CameraUpdateFactory.newCameraPosition(new CameraPosition(
		// Constants.SHANGHAI, 18, 30, 0)), this);
		// break;
		// /**
		// * ��������ƶ���ť��Ӧ�¼���camera��������ƶ�
		// */
		// case R.id.scroll_left:
		// changeCamera(CameraUpdateFactory.scrollBy(-SCROLL_BY_PX, 0), null);
		// break;
		// /**
		// * ��������ƶ���ť��Ӧ�¼���camera�����ұ��ƶ�
		// */
		// case R.id.scroll_right:
		// changeCamera(CameraUpdateFactory.scrollBy(SCROLL_BY_PX, 0), null);
		// break;
		// /**
		// * ��������ƶ���ť��Ӧ�¼���camera�����ϱ��ƶ�
		// */
		// case R.id.scroll_up:
		// changeCamera(CameraUpdateFactory.scrollBy(0, -SCROLL_BY_PX), null);
		// break;
		// /**
		// * ��������ƶ���ť��Ӧ�¼���camera�����±��ƶ�
		// */
		// case R.id.scroll_down:
		// changeCamera(CameraUpdateFactory.scrollBy(0, SCROLL_BY_PX), null);
		// break;
		// /**
		// * �����ͼ�Ŵ�ť��Ӧ�¼�
		// */
		// case R.id.zoom_in:
		// changeCamera(CameraUpdateFactory.zoomIn(), null);
		// break;
		// /**
		// * �����ͼ��С��ť��Ӧ�¼�
		// */
		// case R.id.zoom_out:
		// changeCamera(CameraUpdateFactory.zoomOut(), null);
		// break;
		default:
			break;
		}
	}

	/**
	 * ��ͼ����Ч����ֹ�ص�����
	 */
	@Override
	public void onCancel() {

		// ToastUtil.show(CameraActivity.this, "Animation to ½���� canceled");
	}

	/**
	 * ��ͼ����Ч����ɻص�����
	 */
	@Override
	public void onFinish() {

		// ToastUtil.show(CameraActivity.this, "Animation to ½���� complete");
	}
}
