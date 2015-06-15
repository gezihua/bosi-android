package com.bosi.chineseclass.control;


import java.util.ArrayList;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bosi.chineseclass.activitys.SampleHolderActivity;
import com.bosi.chineseclass.activitys.SampleHolderActivity.ISampleControlInterface;

/**
 * 使用SampleHolderActivity 页面的需要在这里注册
 * 有点组合和代理的意思
 */
public class SampleHolderControlMake implements ISampleControlInterface{
	

	IActivityControl mControl;
	
	public static final  String mControlName = "controlname";
	
	public SampleHolderControlMake(SampleHolderActivity mActivity,Intent mBundler){
//		mControl = new PinYinLearnControl();
//		mControl.onCreate(mBundler);
		String className = "com.bosi.chineseclass.control."+ mBundler.getStringExtra(mControlName);
		try {
			Class mLoadingClass = Class.forName(className);
			try {
				mControl =	(IActivityControl) mLoadingClass.newInstance();
				mControl.onCreate(mBundler);
				mControl.setContext(mActivity);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public ArrayList<Fragment> getFragments() {
		return mControl.getFragments();
	}
	
	

}
