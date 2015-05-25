package com.zy.booking.control;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonObject;
import com.lidroid.xutils.HttpUtils;


import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.util.PreferencesUtils;

import android.os.Bundle;
import android.text.TextUtils;

public class SalesControl extends BaseControl{
	
	private final String APPKEY = "623387";
	private final String ACCESSTOKEN = "accessToken";
	HttpUtils mhHttpUtils ;
	public SalesControl(BaseActivity mContext) {
		super(mContext);
		
		mhHttpUtils = CpApplication.getApplication().mHttpPack.getHttpUtils();
	}

	@Override
	public void onCreate(Bundle mBundle) {
		
	}

	@Override
	public void onResume() {
		
	}

	@Override
	public void onDestroy() {
		
	}
	
	
	//获取accesstoken
	public void getAccessToken (){
	String accessToken  = PreferencesUtils.getString(mActivity, ACCESSTOKEN);
	if(!TextUtils.isEmpty(accessToken))return;
	String mUrl = String.format(AppDefine.URL_SALE_GETASSESSTOKEN,APPKEY,"5404159999ff428e16164e7e580940be" );
	mhHttpUtils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			String result = responseInfo.result;
		    JsonObject mJsonObject = JsonUtil.parse(result);
		    JsonObject mJsonResult = mJsonObject.getAsJsonObject("result");
		    String accesstoken = JsonUtil.getAsString(mJsonResult, "access_token");
		    if(!TextUtils.isEmpty(accesstoken)){
		    	PreferencesUtils.putString(mActivity, ACCESSTOKEN, accesstoken);
		    }
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			System.out.println(msg);
		}
	} )	;
		
		
		
		
	}
}
