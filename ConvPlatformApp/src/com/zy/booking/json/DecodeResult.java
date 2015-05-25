
package com.zy.booking.json;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zy.booking.CpApplication;
import com.zy.booking.activitys.AuthActivity;
import com.zy.booking.util.PreferencesUtils;

public class DecodeResult {

    // errorCode 3 为 token 已经过期

    public static void decoResult(String result, IDecodeJson mDecoded) {
        JsonObject mJsonObject = JsonUtil.parse(result);

        if (mJsonObject == null) {
            Toast.makeText(CpApplication.getApplication(), result,
                    Toast.LENGTH_LONG).show();
            return;

        }
        boolean isSuccess = JsonUtil.getAsBoolean(mJsonObject, "result");
        String reason = JsonUtil.getAsString(mJsonObject, "reason");

        if (reason != null) {
            int errorCode = -1;
            if (mJsonObject.has("errorCode")) {
                errorCode = JsonUtil.getAsInt(mJsonObject, "errorCode");
            }
            if (errorCode != 3) {
                Toast.makeText(CpApplication.getApplication(), reason,
                        Toast.LENGTH_LONG).show();
            } else {
            	try{
            		 intentToAuth(CpApplication.getApplication().mActivityStack.peek());
                     PreferencesUtils.putString(CpApplication.getApplication(), "token", "");
            	}catch(Exception e){
            		
            	}
            }

        }
        JsonObject mJsonData = JsonUtil.getAsJsonObject(mJsonObject, "data");
        JsonArray mJsonArray = null;
        if (mJsonData != null) {
            if (mJsonData.has("list"))
                mJsonArray = JsonUtil.getAsJsonArray(mJsonData, "list");
        } else {
            mJsonData = new JsonObject();
        }
        mDecoded.onDecoded(reason, isSuccess, mJsonData, mJsonArray);
    }

    private static void intentToAuth(Activity mContext) {
        Intent mIntent = new Intent(mContext, AuthActivity.class);
        mContext.startActivity(mIntent);
        mContext.finish();
    }

    public static String decodeResultAdmin(String mDecode) {
        JsonObject mData = JsonUtil.parse(mDecode);
        if (mData.has("content")) {
            return JsonUtil.getAsString(mData, "content");
        }
        return "";
    }

}
