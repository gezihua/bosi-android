package com.zy.booking.control;

import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;

import com.zy.booking.BaseActivity;
import com.zy.booking.struct.OnHttpActionListener;


public abstract class BaseControl extends OnViewContainerLifeCycleListener implements OnEventListener {

	
	protected BaseActivity mActivity;
	
	protected Context mContext;
	
	public BaseControl(BaseActivity mContext){
		this.mActivity = mContext;
	}
	public BaseControl (Context  mContext){
		this.mContext = mContext;
	}
	
	@Override
	public void onClickListner(int id) {
	}

	@Override
	public void onLongClickListener(int id) {
	}
	
	//发送数据
	public void sendData(List<NameValuePair> mList,String url ,OnHttpActionListener mTarget,int code){
		mActivity.sendData(mList, url, mTarget, code);
	}
}
