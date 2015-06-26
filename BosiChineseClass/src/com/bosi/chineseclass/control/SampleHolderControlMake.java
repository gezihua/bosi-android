package com.bosi.chineseclass.control;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.activitys.SampleHolderActivity;
import com.bosi.chineseclass.activitys.SampleHolderActivity.ISampleControlInterface;
import com.bosi.chineseclass.utils.ReflectUtils;

/**
 * 使用SampleHolderActivity 页面的需要在这里注册
 * 有点组合和代理的意思
 */
public class SampleHolderControlMake extends IActivityControl implements ISampleControlInterface{
	

	IActivityControl mControl;
	
	public static final  String mControlName = "controlname";
	
	private Activity mActivity;
	public SampleHolderControlMake(SampleHolderActivity mActivity,Intent mBundler){
		mControl = ReflectUtils.getObjectFromPackage("com.bosi.chineseclass.control", mBundler.getStringExtra(mControlName), IActivityControl.class);
		if(mControl ==null){
			mControl = new SampleControl();
		}
		mControl.onCreate(mBundler);
		mControl.setContext(mActivity);
	}
	
	@Override
	public ArrayList<Fragment> getFragments() {
		return mControl.getFragments();
	}

	@Override
	public void onCreate(Intent mBundle) {
		mControl.onCreate(mBundle);
	}

	@Override
	public void onResume() {
		mControl.onResume();
	}

	@Override
	public void onDestroy() {
		
	}

	@Override
	public void setContext(BaseActivity mContext) {
		
	}
	

}
