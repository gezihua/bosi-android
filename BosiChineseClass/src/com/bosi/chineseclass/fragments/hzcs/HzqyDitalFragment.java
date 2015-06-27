package com.bosi.chineseclass.fragments.hzcs;

import android.annotation.TargetApi;



import android.os.Build;
import android.view.View;
import com.bosi.chineseclass.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

//汉字起源
public class HzqyDitalFragment extends AbsHzcsFragment {

	
	private void initDataArray(){
		mDataArrayJsjs = getResources().getStringArray(R.array.hzcs_hzqy_jsjs_picarray);
		
		mDataArrayCjzz = getResources().getStringArray(R.array.hzcs_hzqy_cjzz_picarray);
		
		mDataArrayFxbg = getResources().getStringArray(R.array.hzcs_hzqy_fxbg_picarray);
	}
	@OnClick(R.id.bt_hzcs_dital_jsjs)
	public void actionClickJsJs(View mView){
		mCurrentData = mDataArrayJsjs;
		mBtRight.setVisibility(View.GONE);
		mBtLeft.setVisibility(View.GONE);
		mTvDitalTitle.setText("结绳记事");
		currentPosition = 0;
		downloadimgs();
	}
	
	@OnClick(R.id.bt_hzcs_dital_cjzz)
	public void actionClickCjzz(View mView){
		mCurrentData = mDataArrayCjzz;
		mBtRight.setVisibility(View.VISIBLE);
		mBtLeft.setVisibility(View.GONE);
		mTvDitalTitle.setText("仓颉造字");
		currentPosition = 0;
		downloadimgs();
	}
	
	@OnClick(R.id.bt_hzcs_dital_fxbg)
	public void actionClickFxbg(View mView){
		mCurrentData = mDataArrayFxbg;
		mBtRight.setVisibility(View.GONE);
		mBtLeft.setVisibility(View.GONE);
		mTvDitalTitle.setText("伏羲八卦");
		currentPosition = 0;
		downloadimgs();
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void initMenu() {
		mImageLoader.getBitmapFactory().display(mIvDital,
				"assets/hzqy/cjzz.png");
		mTvDitalTitle.setText("仓颉造字");
		View mMenuView = View.inflate(mActivity, R.layout.layout_hzqy_menu, null);
		mLayoutMenu.addView(mMenuView);
		ViewUtils.inject(this, mMenuView);
		mBtLeft.setVisibility(View.GONE);
		mBtRight.setVisibility(View.GONE);
	}

	@Override
	protected void afterViewInject() {
		super.afterViewInject();
		mHeadActionBar.setTextMiddle("汉字起源", -1);
		mTvDitalTitle.setText("汉字起源");
		initDataArray();
	}

	String mDataArrayJsjs [] ;
	String mDataArrayCjzz [];
	String mDataArrayFxbg [];
	@Override
	protected void downLoadImageOverAction() {
		updateDitalPg();
	}
	

}
