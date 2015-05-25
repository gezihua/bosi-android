package com.zy.booking.fragments;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapDoubleClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.R;
import com.zy.booking.components.BaseComponents.OnComponentsActionListener;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.components.SearchLayoutComponents;
import com.zy.booking.control.LocationCtrol;
import com.zy.booking.control.LocationCtrol.OnLocationChangedListener;

/**
 * 地图选点功能
 * 
 * @author zhujohnle
 * */
public class MapLocFragment extends BaseFragment implements
		OnGetGeoCoderResultListener {

	@ViewInject(R.id.bmapView)
	MapView mMapView;

	BaiduMap mBaiduMap;

	LatLng mLatLngCurrent;

	GeoCoder mSearch = null;

	@ViewInject(R.id.headactionbar)
	View mViewHeadAction;
	HeadLayoutComponents mHeadLayoutComponents;

	@ViewInject(R.id.ll_search_body)
	LinearLayout mllBody;

	SearchLayoutComponents mSearchComponets;

	LocationCtrol mLocationCtrol;

	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_map_searchloc, null);
	}

	@Override
	void afterViewInject() {
		mBaiduMap = mMapView.getMap();

		initLocation();

		initHeadComponents();
		
		mSearchComponets = new SearchLayoutComponents(getActivity());
		mllBody.addView(mSearchComponets.getView());
		mSearchComponets
				.setOnComponentsAction(new OnComponentsActionListener() {

					@Override
					public void onAction(View mView) {
						String text = mSearchComponets.getEditText().getText()
								.toString();
						mtempAddress = text;
						searchLngLatByAddress("", text);
					}
				});

		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				LocationMode.COMPASS, true, null));
		mBaiduMap.setMyLocationEnabled(true);
		mLocationCtrol = new LocationCtrol(getActivity());
		mLocationCtrol
				.setLocationChangedListener(new OnLocationChangedListener() {

					@Override
					public void onLocationChanged(BDLocation mBdLocation) {
						if (mBdLocation == null || mMapView == null)
							return;

						MyLocationData locData = new MyLocationData.Builder()
								.accuracy(mBdLocation.getRadius())
								// 此处设置开发者获取到的方向信息，顺时针0-360
								.direction(100)
								.latitude(mBdLocation.getLatitude())
								.longitude(mBdLocation.getLongitude()).build();
						mBaiduMap.setMyLocationData(locData);
						if (isFirstLoc) {
							isFirstLoc = false;
							LatLng ll = new LatLng(mBdLocation.getLatitude(),
									mBdLocation.getLongitude());
							MapStatusUpdate u = MapStatusUpdateFactory
									.newLatLng(ll);
							mBaiduMap.animateMapStatus(u);
							mLocationCtrol.onDestroy();
						}
					}
				});
		mLocationCtrol.onCreate(null);
	}

	
	private void initHeadComponents(){
		mHeadLayoutComponents = new HeadLayoutComponents(getActivity(),
				mViewHeadAction);
		mHeadLayoutComponents.setDefaultLeftCallBack(true);
		mHeadLayoutComponents.setTextMiddle("地区搜索", -1);
		mHeadLayoutComponents.setTextRight("", R.drawable.icon_search_bg);
		mHeadLayoutComponents.setRightOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mSearchComponets.setVisiable(View.VISIBLE);
			}
		});
	}
	boolean isFirstLoc = true;

	private void initMapZoomStatus() {
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomBy(70);
		mBaiduMap.animateMapStatus(mapStatusUpdate);
	}

	/**
	 * 注册屏幕mapview 的单机和双击操作
	 * 
	 * */
	private void initLocation() {
		// 实例化 搜索组件
		mSearch = GeoCoder.newInstance();
		
		mSearch.setOnGetGeoCodeResultListener(this);

		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(LatLng point) {
				mLatLngCurrent = point;
				upDateLocation();
			}

			public boolean onMapPoiClick(MapPoi poi) {
				return false;
			}
		});
		mBaiduMap.setOnMapDoubleClickListener(new OnMapDoubleClickListener() {
			public void onMapDoubleClick(LatLng point) {
				mLatLngCurrent = point;
				upDateLocation();
			}
		});

		initMapZoomStatus();
	}

	private void upDateLocation() {
		searchAddressByLocation(mLatLngCurrent.latitude,
				mLatLngCurrent.longitude);
	}

	// 根据经纬度查地址
	private void searchAddressByLocation(double lat, double lng) {
		showProgresssDialog();
		LatLng ptCenter = new LatLng(lat, lng);
		// 反Geo搜索
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
	}

	// 根据地址反查经纬度
	private void searchLngLatByAddress(String city, String address) {
		showProgresssDialog();

		mSearch.geocode(new GeoCodeOption().city(city).address(address));
	}

	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		mSearch = null;
		mLocationCtrol.onDestroy();
		super.onDestroy();
	}

	/**
	 * 根据地理位置查询结果的回调
	 * 
	 * */

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		dismissProgressDialog();
		if(result==null||result.getLocation()==null){
			Toast.makeText(getActivity(), "查无结果", Toast.LENGTH_SHORT).show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.pin_progress_unpinned)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		this.mLatLngCurrent  = result
				.getLocation();
		this.mSelectAddress = mtempAddress;
		
		showHeadLeft();
	}

	/**
	 * 根据经纬度查询的结果回调
	 * 
	 * */
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		dismissProgressDialog();
		if (result == null) {
			Toast.makeText(getActivity(), "查无结果", Toast.LENGTH_SHORT).show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.pin_progress_unpinned)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));

		try {
			mSelectAddress = result.getAddress();
			this.mLatLngCurrent = result.getLocation();
			mSearchComponets.getEditText().setText(mSelectAddress);
			showHeadLeft();
		} catch (Exception e) {
		}

	}

	String mSelectAddress;
	String mtempAddress;

	private void showHeadLeft() {
		mHeadLayoutComponents.setTextLeft("使用", -1);
		mHeadLayoutComponents.setLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity mAcitivity = getActivity();
				Intent mIntent = new Intent();
				mIntent.putExtra("address", mSelectAddress);
				mIntent.putExtra("lng", mLatLngCurrent.longitude + "");
				mIntent.putExtra("lat", mLatLngCurrent.latitude + "");
				mAcitivity.setResult(Activity.RESULT_OK, mIntent);
				getActivity().finish();
			}
		});
	}

}
