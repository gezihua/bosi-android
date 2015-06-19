package com.bosi.chineseclass.fragments.hzcs;

import android.annotation.TargetApi;

import android.os.Build;
import android.view.View;

import com.bosi.chineseclass.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;


//汉字演变
public class HzybDitalFragment extends AbsHzcsFragment{
	
	String mJwArray[];
	String mJgwArray[];
	String mZsArray[];
	String mKsArray[];
	String mlsArray[];
	
	@OnClick(R.id.bt_hzyb_dital_jgw)
	public void actionClickJgw(View mView){
		
	}
	@OnClick(R.id.bt_hzyb_dital_jw)
	public void actionClickJw(View mView){
		
	}
	@OnClick(R.id.bt_hzyb_dital_zs)
	public void actionClickzs(View mView){
		
	}
	@OnClick(R.id.bt_hzyb_dital_ls)
	public void actionClickls(View mView){
		
	}
	@OnClick(R.id.bt_hzyb_dital_ks)
	public void actionClickks(View mView){
		
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void initMenu() {
		mImageLoader.getBitmapFactory().display(mIvDital,
				"assets/hzqy/hzqy.png");
		
		View mMenuView = View.inflate(mActivity, R.layout.layout_hzyb_menu, null);
		mLayoutMenu.addView(mMenuView);
		ViewUtils.inject(this, mMenuView);
		mBtLeft.setVisibility(View.GONE);
		mBtRight.setVisibility(View.GONE);
	}

	@Override
	protected void afterViewInject() {
		super.afterViewInject();
	}
	
	private void initArrays(){
		
	}
	@Override
	public void initWholeArray() {
		
	}
	



}
