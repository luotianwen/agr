package com.zte.agricul.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLongClickListener;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.VisibleRegion;
import com.zte.agricul.zh.R;
import com.zte.agricul.app.BusEvent;
import com.zte.agricul.app.ZteApplication;
import com.zte.agricul.util.Util;

public class MapToLocationActivity extends Activity implements OnMapClickListener,OnClickListener {
private AMap aMap;
private MapView mapView;
private TextView mTapTextView,location;
private TextView leftTv, ivTitleName, rightTv;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_map_to_location);
mapView = (MapView) findViewById(R.id.map);

mapView.onCreate(savedInstanceState);// 此方法必须重写
init();
}

/**
* 初始化AMap对象
*/
private void init() {
if (aMap == null) {
	aMap = mapView.getMap();
	setUpMap();
}
mTapTextView = (TextView) findViewById(R.id.tap_text);
location= (TextView) findViewById(R.id.location);
leftTv = (TextView) findViewById(R.id.leftTv);
leftTv.setVisibility(View.VISIBLE);
leftTv.setOnClickListener(this);
ivTitleName = (TextView) findViewById(R.id.ivTitleName);
ivTitleName.setText(getResources().getString(R.string.land_location));
rightTv = (TextView) findViewById(R.id.rightTv);
rightTv.setOnClickListener(this);
rightTv.setBackgroundResource(R.drawable.round_conner_line_green_bg);
rightTv.setText(getResources().getString(R.string.save));
rightTv.setVisibility(View.VISIBLE);
}

/**
* amap添加一些事件监听器
*/
private void setUpMap() {
aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
}

/**
* 方法必须重写
*/
@Override
protected void onResume() {
super.onResume();
mapView.onResume();
}

/**
* 方法必须重写
*/
@Override
protected void onPause() {
super.onPause();
mapView.onPause();
}

/**
* 方法必须重写
*/
@Override
protected void onSaveInstanceState(Bundle outState) {
super.onSaveInstanceState(outState);
mapView.onSaveInstanceState(outState);
}

/**
* 方法必须重写
*/
@Override
protected void onDestroy() {
super.onDestroy();
mapView.onDestroy();
}

/**
* 对单击地图事件回调
*/
@Override
public void onMapClick(LatLng point) {
aMap.clear();
location.setText(getResources().getString(R.string.land_location)+":" + point.latitude+","+point.longitude);
setUpMap(point);
}
/**
* 往地图上添加一个marker
*/
private void setUpMap(LatLng point) {
// aMap.addMarker(new MarkerOptions().position(Constants.CHENGDU).icon(
// BitmapDescriptorFactory
// .defaultMarker(BitmapDescriptorFactory.HUE_RED)));

MarkerOptions markerOptions = new MarkerOptions();
markerOptions.position(point);
markerOptions.visible(true);
// BitmapDescriptor bitmapDescriptor =
// BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),
// R.drawable.location_marker));
// markerOptions.icon(bitmapDescriptor);
Marker marker=aMap.addMarker(markerOptions);
marker.showInfoWindow();// 设置默认显示一个infowinfow
//changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
//		location, 12, 0, 30)), null);
}

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()) {
	case R.id.leftTv:
		finish();
		break;
	case R.id.rightTv:
		if ((getResources().getString(R.string.land_location)+":").equals(location.getText().toString())) {
			Util.showToast(getApplicationContext(), getResources().getString(R.string.choice_land_location), Toast.LENGTH_SHORT);
			return ;
		}
		BusEvent.REFRESH_LOCATION_EVENT.data=location.getText().toString().replace(getResources().getString(R.string.land_location)+":", "");
		ZteApplication.bus.post(BusEvent.REFRESH_LOCATION_EVENT);
		finish();
		break;
	default:
		break;
	}
}


}
