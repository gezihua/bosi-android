package com.zy.booking.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class IntentUtils {

	
	public static void intentToTel(String phoneNum,Context mContext){
		 Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNum));
		 mContext.startActivity(intent);
	}
}
