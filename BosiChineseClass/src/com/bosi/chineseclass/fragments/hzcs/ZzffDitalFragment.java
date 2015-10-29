package com.bosi.chineseclass.fragments.hzcs;


import android.view.View;

import com.bosi.chineseclass.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

//造字方法
public class ZzffDitalFragment extends AbsHzcsFragment{


	String mDataArrayxx [];
	String mDataArrayxs [];
	String mDataArrayzs [];
	String mDataArrayzz [];
	String mDataArrayjj [];
	String mDataArrayhy [];
	
	private void initDataArray(){
		mDataArrayxx = getResources().getStringArray(R.array.hzcs_zzff_xx_picarray);
		mDataArrayxs = getResources().getStringArray(R.array.hzcs_zzff_xs_picarray);
		mDataArrayzs = getResources().getStringArray(R.array.hzcs_zzff_zs_picarray);
		mDataArrayzz = getResources().getStringArray(R.array.hzcs_zzff_zz_picarray);
		mDataArrayjj = getResources().getStringArray(R.array.hzcs_zzff_jj_picarray);
		mDataArrayhy = getResources().getStringArray(R.array.hzcs_zzff_hy_picarray);
	}
	@OnClick(R.id.bt_zzff_dital_xx)
	public void actionxx(View mView){
		mCurrentData = mDataArrayxx;
		initpanel("象形");
	}
	@OnClick(R.id.bt_zzff_dital_zz)
	public void actionzz(View mView){
		mCurrentData = mDataArrayzz;
		initpanel("转注");
	}
	@OnClick(R.id.bt_zzff_dital_jj)
	public void actionjj(View mView){
		mCurrentData = mDataArrayjj;
		initpanel("假借");
	}
	@OnClick(R.id.bt_zzff_dital_xs)
	public void actionxs(View mView){
		mCurrentData = mDataArrayxs;
		initpanel("形声");
	}
	@OnClick(R.id.bt_zzff_dital_hy)
	public void actionhy(View mView){
		mCurrentData = mDataArrayhy;
		initpanel("会意");
	}
	@OnClick(R.id.bt_zzff_dital_zs)
	public void actionzs(View mView){
		mCurrentData = mDataArrayzs;
		initpanel("指事");
	}
	
	private void initpanel(String text){
		mBtRight.setVisibility(View.VISIBLE);
		mBtLeft.setVisibility(View.GONE);
		mTvDitalTitle.setText(text);
		currentPosition = 0;
		downloadimgs();
	}
	
	@Override
	public void initMenu() {
		
		
		View mMenuView = View.inflate(mActivity, R.layout.layout_zzff_menu, null);
		mLayoutMenu.addView(mMenuView);
		ViewUtils.inject(this, mMenuView);
//		mBtLeft.setVisibility(View.GONE);
//		mBtRight.setVisibility(View.GONE);
	}

	@Override
	protected void afterViewInject() {
		super.afterViewInject();
		mHeadActionBar.setTextMiddle("造字方法", -1);
		mTvDitalTitle.setText("造字方法");
		initDataArray();
		mCurrentData =  mDataArrayzs;
		
		mWebView.loadUrl("http://www.yuwen100.cn/yuwen100/hzzy/Android/zaozifangfa/zx/index.html");
	}
	@Override
	protected void downLoadImageOverAction() {
		updateDitalPg();
	}

	
}
