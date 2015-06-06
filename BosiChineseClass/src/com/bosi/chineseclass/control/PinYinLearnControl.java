package com.bosi.chineseclass.control;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.activitys.SampleHolderActivity;
import com.bosi.chineseclass.activitys.SampleHolderActivity.ISampleControlInterface;
import com.bosi.chineseclass.fragments.PyLearnFragment;
import com.bosi.chineseclass.fragments.PyLearnFragment.CategoryPinyin;

/**
 * 
 * 
 * 
 */
public class PinYinLearnControl extends IActivityControl implements ISampleControlInterface{

	ArrayList<Fragment> mFragments  = null;
	
	
	public PinYinLearnControl() {
		mFragments = new ArrayList<Fragment>();
		PyLearnFragment mSmFragment = new PyLearnFragment(CategoryPinyin.SM);
		PyLearnFragment mYmFragment = new PyLearnFragment(CategoryPinyin.YM);
		mFragments.add(mSmFragment);
		mFragments.add(mYmFragment);
	}
	
	public ArrayList<Fragment> getDitalFragments(){
		return mFragments;
	}
  
	public void onCreate(Bundle mBundle) {
		
	}

	public void onResume() {
		
	}

	public void onDestroy() {
		
	}

	public ArrayList<Fragment> getFragments() {
		return mFragments;
	}

	@Override
	public void setContext(BaseActivity mContext) {
		this.mContext = mContext;
	}
	
	

}