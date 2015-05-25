package com.zy.booking.wedget;



import android.content.Context;

import android.util.AttributeSet;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.zy.booking.control.LocationCtrol;
import com.zy.booking.control.LocationCtrol.OnLocationChangedListener;

public class LocationView extends TextView implements OnLocationChangedListener{
	
	LocationCtrol mLocationControl;
	private Context mContext;
	
	public LocationView(Context context) {
		super(context);
		this.mContext = context;
		mLocationControl = new LocationCtrol(context);
		mLocationControl.setLocationChangedListener(this);
	}
	public LocationView(Context context, AttributeSet attrs) {
		super(context,attrs,0);
		mLocationControl = new LocationCtrol(context);
		mLocationControl.setLocationChangedListener(this);
	}
	
	public LocationView(Context context, AttributeSet attrs, int defStyle) {
		super(context,attrs,defStyle);
		mLocationControl = new LocationCtrol(context);
		mLocationControl.setLocationChangedListener(this);
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mLocationControl.onCreate(null);
	}
	protected void onDetachedFromWindow(){
		super.onDetachedFromWindow();
		mLocationControl.onDestroy();
	}
	@Override
	public void onLocationChanged(BDLocation mBdLocation) {
		//根据经纬度获取当前地址
		//setText();
		setText(mBdLocation.getLatitude()+"|"+mBdLocation.getLongitude());
	}
	
	
	
}
