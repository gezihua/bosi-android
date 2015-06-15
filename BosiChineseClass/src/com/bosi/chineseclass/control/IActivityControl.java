package com.bosi.chineseclass.control;

import com.bosi.chineseclass.BaseActivity;

import com.bosi.chineseclass.activitys.SampleHolderActivity.ISampleControlInterface;
import android.content.Intent;

public abstract class IActivityControl implements ISampleControlInterface{
	
//	public IActivityControl(){
//		
//	}
	public abstract void onCreate(Intent mBundle);
	public abstract void onResume();
	public abstract void onDestroy();
	
	public BaseActivity mContext;
	public abstract void setContext(BaseActivity mContext);
	
}
