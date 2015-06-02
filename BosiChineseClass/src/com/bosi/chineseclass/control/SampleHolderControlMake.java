package com.bosi.chineseclass.control;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.activitys.SampleHolderActivity;
import com.bosi.chineseclass.activitys.SampleHolderActivity.ISampleControlInterface;

/**
 * 使用SampleHolderActivity 页面的需要在这里注册
 * 有点组合和代理的意思
 */
public class SampleHolderControlMake implements ISampleControlInterface{
	

	ISampleControlInterface mControl;
	public static final  String mControlName = "controlname";
	
	public SampleHolderControlMake(SampleHolderActivity mActivity,Intent mBundler){
		mControl = new PinYinLearnControl();
	}
	
	@Override
	public ArrayList<Fragment> getFragments() {
		return mControl.getFragments();
	}
	
	

}
