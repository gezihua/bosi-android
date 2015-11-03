package com.bosi.chineseclass.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.OnHttpActionListener;
import com.bosi.chineseclass.db.BPHZ;
import com.bosi.chineseclass.han.util.PreferencesUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class UpLoadBphzTask implements OnHttpActionListener ,IBasicTask{

	HttpHandler<?> mHandler;
	Context mContext ;
	public UpLoadBphzTask(Context mContext){
		this.mContext = mContext;
	}
	@Override
	public void onHttpSuccess(JSONObject mResult, int code) {
		if(mResult.has("code")){
			
		}
		mContext.sendBroadcast(new Intent(AppDefine.ZYDefine.ACTION_BRPADCAST_UPBPHZOVER));
	}

	@Override
	public void onHttpError(Exception e, String reason, int code) {
		mContext.sendBroadcast(new Intent(AppDefine.ZYDefine.ACTION_BRPADCAST_UPBPHZOVER));
	}

	@Override
	public void cancleTask() {
		mContext.sendBroadcast(new Intent(AppDefine.ZYDefine.ACTION_BRPADCAST_UPBPHZOVER));
		if(mHandler!=null){
			mHandler.cancel();
		}
	}
	BPHZ mBpcy = new BPHZ();
	@Override
	public HttpHandler<?> sendDataAsy() {
		mContext.sendBroadcast(new Intent(AppDefine.ZYDefine.ACTION_BRPADCAST_UPBPHZBGTIN));
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
		mHandler = BSApplication.getInstance().sendData(mList,AppDefine.URLDefine.URL_SYNCBOSICHARDATA, this, 101, HttpMethod.POST);
		return mHandler;
	}


}
