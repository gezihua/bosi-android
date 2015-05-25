package com.zy.booking.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class DataTools {
	/**
	 * 
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	
	public static String getDeviceId(Context mcontext){
		TelephonyManager tm = (TelephonyManager) mcontext.getSystemService(Context.TELEPHONY_SERVICE);  
		return tm.getDeviceId();
	}
	  
}
