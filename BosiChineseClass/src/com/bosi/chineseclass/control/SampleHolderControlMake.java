package com.bosi.chineseclass.control;


import java.util.ArrayList;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bosi.chineseclass.activitys.SampleHolderActivity;
import com.bosi.chineseclass.activitys.SampleHolderActivity.ISampleControlInterface;
import com.bosi.chineseclass.utils.ReflectUtils;

/**
 * 使用SampleHolderActivity 页面的需要在这里注册
 * 有点组合和代理的意思
 */
public class SampleHolderControlMake implements ISampleControlInterface{
	

	IActivityControl mControl;
	
	public static final  String mControlName = "controlname";
	
	public SampleHolderControlMake(SampleHolderActivity mActivity,Intent mBundler){
		mControl = ReflectUtils.getObjectFromPackage("com.bosi.chineseclass.control", mBundler.getStringExtra(mControlName), IActivityControl.class);
		if(mControl ==null){
			mControl = new HzcsDitalContarol();
		}
		mControl.onCreate(mBundler);
		mControl.setContext(mActivity);
	}
	
	@Override
	public ArrayList<Fragment> getFragments() {
		return mControl.getFragments();
	}
	
	

}
