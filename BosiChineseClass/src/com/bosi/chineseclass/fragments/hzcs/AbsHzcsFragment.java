package com.bosi.chineseclass.fragments.hzcs;

import android.os.Bundle;

import android.view.View;

import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.XutilImageLoader;

//汉字常识的基础功能
public abstract class AbsHzcsFragment extends BaseFragment {
	
	XutilImageLoader mImageLoader;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageLoader = new XutilImageLoader(mActivity);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected View getBasedView() {
		return View.inflate(mActivity, R.layout.layout_hzcs_base, null);
	}

	@Override
	protected void afterViewInject() {
		initViewAndState();
	}
	
	public abstract void actionLeft(View mView);
	public abstract void actionRight(View mView);
	public abstract void initMenu();

	private void initViewAndState(){
		initMenu();
	}
	

}
