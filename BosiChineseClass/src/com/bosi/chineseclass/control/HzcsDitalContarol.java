package com.bosi.chineseclass.control;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.activitys.SampleHolderActivity.ISampleControlInterface;
import com.bosi.chineseclass.fragments.hzcs.HzqyDitalFragment;
import com.bosi.chineseclass.fragments.hzcs.HzybDitalFragment;
import com.bosi.chineseclass.fragments.hzcs.QwhzDitalFragment;
import com.bosi.chineseclass.fragments.hzcs.ZzffDitalFragment;

public class HzcsDitalContarol extends IActivityControl implements ISampleControlInterface{

	HzqyDitalFragment mFragmentHzqy;
	HzybDitalFragment mFragmentHzyb;
	QwhzDitalFragment mFragmentQwhz;
	ZzffDitalFragment mFragmentZzff;
	
	@Override
	public ArrayList<Fragment> getFragments() {
		return null;
	}

	@Override
	public void onCreate(Bundle mBundle) {
		
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
