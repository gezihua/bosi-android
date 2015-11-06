package com.bosi.chineseclass.fragments.hzcs;



import android.graphics.Bitmap;
import android.view.View;

import com.bosi.chineseclass.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.core.BitmapDecoder;
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
		mCurrentData = mJgwArray;
		initpanel("甲骨文");
	}
	@OnClick(R.id.bt_hzyb_dital_jw)
	public void actionClickJw(View mView){
		mCurrentData = mJwArray;
		initpanel("金文");
	}
	@OnClick(R.id.bt_hzyb_dital_zs)
	public void actionClickzs(View mView){
		mCurrentData = mZsArray;
		initpanel("纂书");
	}
	@OnClick(R.id.bt_hzyb_dital_ls)
	public void actionClickls(View mView){
		mCurrentData = mlsArray;
		initpanel("隶书");
	}
	@OnClick(R.id.bt_hzyb_dital_ks)
	public void actionClickks(View mView){
		mCurrentData = mKsArray;
		initpanel("楷书");
	}
	
	private void initpanel(String text){
//		mBtRight.setVisibility(View.VISIBLE);
//		mBtLeft.setVisibility(View.GONE);
		mTvDitalTitle.setText(text);
		currentPosition = 0;
		downloadimgs();
	}
	Bitmap mTempBitmap ; 
	@Override
	public void initMenu() {
		mWebView.loadUrl("http://www.yuwen100.cn/yuwen100/hzzy/Android/hanziyanbian/zx/index.html");
		
		View mMenuView = View.inflate(mActivity, R.layout.layout_hzyb_menu, null);
		mLayoutMenu.addView(mMenuView);
		ViewUtils.inject(this, mMenuView);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mTempBitmap!=null&&!mTempBitmap.isRecycled()){
			mTempBitmap.recycle();
		}
	}

	@Override
	protected void afterViewInject() {
		super.afterViewInject();
		mHeadActionBar.setTextMiddle("汉字演变", -1);
		mTvDitalTitle.setText("汉字演变");
		initArrays();
	}
	
	private void initArrays(){
		mJgwArray = getResources().getStringArray(R.array.hzcs_hzyb_jgw_picarray);
		mJwArray  = getResources().getStringArray(R.array.hzcs_hzyb_jw_picarray);
		mZsArray  = getResources().getStringArray(R.array.hzcs_hzyb_zs_picarray);
		mKsArray  = getResources().getStringArray(R.array.hzcs_hzyb_ks_picarray);
		mlsArray  = getResources().getStringArray(R.array.hzcs_hzyb_ls_picarray);
	}
	@Override
	protected void downLoadImageOverAction() {
		updateDitalPg();
	}


}
