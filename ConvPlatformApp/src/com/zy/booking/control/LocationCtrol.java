package com.zy.booking.control;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

//用于监控一个定位的管理类
public class LocationCtrol extends BaseControl {

	LocationClient mLocationClient;

	LocationClientOption mClientOption;

	public BDLocation mBdLocation;
	
	private final Object mObj = new Object();

	public interface OnLocationChangedListener {
		public void onLocationChanged(BDLocation mBdLocation);
	}

	OnLocationChangedListener mListener;

	public void setLocationChangedListener(OnLocationChangedListener mListener) {
		this.mListener = mListener;
	}

	public LocationCtrol(Context mContext) {
		super(mContext);

		mLocationClient = new LocationClient(mContext);
		mClientOption = new LocationClientOption();
		mClientOption.setCoorType("bd09ll");
		mClientOption.setScanSpan(5000);
		mLocationClient.setLocOption(mClientOption);
		
		mClientOption.setAddrType("all");
		mClientOption.setPriority(LocationClientOption.NetWorkFirst); 
		mClientOption.setPoiNumber(10);
		mLocationClient.registerLocationListener(new LocationListener());
		
	}

	class LocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation mLocation) {
			if (mLocation != null) {
				synchronized (mObj) {
					if (mListener != null)
						mListener.onLocationChanged(mLocation);
				}
			}
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			
		}


	}

	@Override
	public void onCreate(Bundle mBundle) {
		mLocationClient.start();
	}

	@Override
	public void onResume() {

	}

	@Override
	public void onDestroy() {
		mLocationClient.stop();
	}

}
