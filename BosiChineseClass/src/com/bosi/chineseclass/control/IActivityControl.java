package com.bosi.chineseclass.control;

import com.bosi.chineseclass.BaseActivity;

import android.os.Bundle;

public abstract class IActivityControl {
	public abstract void onCreate(Bundle mBundle);
	public abstract void onResume();
	public abstract void onDestroy();
	
	public BaseActivity mContext;
	public abstract void setContext(BaseActivity mContext);
	
}
