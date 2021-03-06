package com.bosi.chineseclass.control;

import java.util.ArrayList;



import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.activitys.SampleHolderActivity.ISampleControlInterface;
import com.bosi.chineseclass.utils.ReflectUtils;

public class HzcsDitalControl extends IActivityControl implements ISampleControlInterface{

	String mFragmentName;
	public final static String KEY_HZCSFUNCNAME = "key_hzcsfuncname";
	@Override
	public ArrayList<Fragment> getFragments() {
		ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
		mFragments.add(ReflectUtils.getObjectFromPackage("com.bosi.chineseclass.fragments.hzcs", mFragmentName,BaseFragment.class));
		return mFragments;
	}

	@Override
	public void onCreate(Intent mBundle) {
		mFragmentName = mBundle.getStringExtra(KEY_HZCSFUNCNAME);
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
