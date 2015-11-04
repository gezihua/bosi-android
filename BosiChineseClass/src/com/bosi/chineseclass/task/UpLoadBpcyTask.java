package com.bosi.chineseclass.task;

import java.util.ArrayList;


import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.OnHttpActionListener;
import com.bosi.chineseclass.db.BPCY;
import com.bosi.chineseclass.han.util.PreferencesUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class UpLoadBpcyTask implements OnHttpActionListener ,IBasicTask{

	HttpHandler<?> mHandler;
	BaseActivity mContext ;
	public UpLoadBpcyTask(BaseActivity mContext){
		this.mContext = mContext;
	}
	@Override
	public void onHttpSuccess(JSONObject mResult, int code) {
		mContext.sendBroadcast(new Intent(AppDefine.ZYDefine.ACTION_BROADCAST_UPBPCYOVER));
		if(mResult ==null)return ;
		if(mResult.has("code")){
			String mCode;
			try {
				mCode = mResult.getString("code");
			} catch (JSONException e) {
				mContext.showToastShort("服务异常");
			}
		}
	}

	@Override
	public void onHttpError(Exception e, String reason, int code) {
		mContext.sendBroadcast(new Intent(AppDefine.ZYDefine.ACTION_BROADCAST_UPBPCYOVER));
		mContext.showToastShort("网络异常");
	}

	@Override
	public void cancleTask() {
		if(mHandler!=null){
			mHandler.cancel();
		}
	}
	BPCY mBpcy = new BPCY();
	@Override
	public HttpHandler<?> sendDataAsy() {
		
		String mUid = PreferencesUtils.getString(mContext,
				AppDefine.ZYDefine.EXTRA_DATA_USERID);
		List<NameValuePair> mList = new ArrayList<NameValuePair>();
		mList.add(new BasicNameValuePair("uid", mUid));
		mList.add(new BasicNameValuePair("listLearned", mBpcy
				.getAllLearnedData(mContext, "1")));
		mList.add(new BasicNameValuePair("listNotLearned", mBpcy
				.getAllLearnedData(mContext, "0")));
		BSApplication.getInstance().sendData(mList,
				AppDefine.URLDefine.URL_SYNCBOSIIDIOM, this, 102,
				HttpMethod.POST);
		mHandler = BSApplication.getInstance().sendData(mList,AppDefine.URLDefine.URL_SYNCBOSIIDIOM, this, 101, HttpMethod.POST);
		return mHandler;
	}


}
