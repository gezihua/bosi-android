package com.bosi.chineseclass.control;

import java.util.ArrayList;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.utils.ReflectUtils;

public class SampleControl extends IActivityControl{
	Intent mBundle;
	String[] mFragmentName;
	String mPackageName;
	public static final String KEY_FRAGMENTNAMES = "key_fragmentnames";
	public static final String KEY_PACKAGETNAME = "key_packagename";
	@Override
	public ArrayList<Fragment> getFragments() {
		mFragmentName = mBundle.getStringArrayExtra(KEY_FRAGMENTNAMES);
		mPackageName =  mBundle.getStringExtra(KEY_PACKAGETNAME);
		
		ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
		for(int i = 0;i<mFragmentName.length;i++){
			mFragments.add(ReflectUtils.getObjectFromPackage(mPackageName, mFragmentName[i],BaseFragment.class));
		}
		return mFragments;
	}

	@Override
	public void onCreate(Intent mBundle) {
		this.mBundle = mBundle;
	}

	@Override
	public void onResume() {
		
	}

	@Override
	public void onDestroy() {
		
	}

	@Override
	public void setContext(BaseActivity mContext) {
		
	}

}
