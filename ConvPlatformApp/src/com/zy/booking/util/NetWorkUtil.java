package com.zy.booking.util;



import com.zy.booking.CpApplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetWorkUtil {

    public static boolean isAnyNetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null) {
            String typeName = info.getTypeName().toLowerCase(); // WIFI/MOBILE
            boolean isXXXNet = false;
            if (typeName.equals("wifi")) {
                isXXXNet = true;
            }else {
                // 3gnet/3gwap/uninet/uniwap/cmnet/cmwap/ctnet/ctwap
                String apnName = info.getExtraInfo().toLowerCase();
                if (apnName.contains("net")) {
                    isXXXNet = true;
                }
            }
            if (isXXXNet && info.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }


    }
    
    
    public static boolean isNetworkAvailable(Context context) {  
        ConnectivityManager connectivity = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (connectivity == null) {  
            return false;
        } else {//获取所有网络连接信息  
            NetworkInfo[] info = null;
			try {
				info = connectivity.getAllNetworkInfo();
			} catch (Exception e) {
			}  
            if (info != null) {//逐一查找状态为已连接的网络  
                for (int i = 0; i < info.length; i++) {  
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {  
                        return true;  
                    }  
                }  
            }  
        }  
        return false;  
    }  
    /** 
     * Returns whether the network is roaming 
     */  
    public static boolean isNetworkRoaming(Context context) {  
        ConnectivityManager connectivity =  
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        TelephonyManager ts = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (connectivity == null) {  
           
        } else {  
            NetworkInfo info = connectivity.getActiveNetworkInfo();  
            if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {  
                if (ts.isNetworkRoaming()) {  
                    return true;  
                } 
            } 
        }  
        return false;  
    }  
    
    
    /**
     * 检查当前wifi是否可用
     * 考虑wifi已经连接并且可用
     * 
     * */
    public static boolean isWifiAvailable(){
    	 ConnectivityManager cm = (ConnectivityManager) CpApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo info = cm.getActiveNetworkInfo();
         String typeName;
         try{
        	 typeName=info.getTypeName();
         }catch (Exception e) {
        	 typeName="";
		 }
         if(info.getTypeName().equalsIgnoreCase("WIFI")){
        	 return true;
         }
    	 return false;
    }
}
