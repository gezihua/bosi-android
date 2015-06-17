package com.bosi.chineseclass.fragments.hzcs;

import android.annotation.TargetApi;

import android.os.Build;
import android.view.View;
import com.bosi.chineseclass.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

//汉字起源
public class HzqyDitalFragment extends AbsHzcsFragment {

	
	@OnClick(R.id.bt_hzcs_dital_jsjs)
	public void actionClickJsJs(View mView){
		mCurrentData = new String[1];
		mCurrentData[0] = "assets/hzqy/jsjs.png";
		mBtRight.setVisibility(View.GONE);
		mBtLeft.setVisibility(View.GONE);
		currentPosition = 0;
		updateDitalPg();
	}
	
	@OnClick(R.id.bt_hzcs_dital_cjzz)
	public void actionClickCjzz(View mView){

		mCurrentData = new String[2];
		mCurrentData[0] = "assets/hzqy/cjzz0.png";
		mCurrentData[1] = "assets/hzqy/cjzz1.png";
		mBtRight.setVisibility(View.VISIBLE);
		mBtLeft.setVisibility(View.GONE);
		currentPosition = 0;
		updateDitalPg();
	
	}
	
	@OnClick(R.id.bt_hzcs_dital_fxbg)
	public void actionClickFxbg(View mView){
		mCurrentData = new String[1];
		mCurrentData[0] = "assets/hzqy/fxbg.png";
		mBtRight.setVisibility(View.GONE);
		mBtLeft.setVisibility(View.GONE);
		currentPosition = 0;
		updateDitalPg();
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void initMenu() {
		mImageLoader.getBitmapFactory().display(mIvDital,
				"assets/hzqy/hzqy.png");
		
		View mMenuView = View.inflate(mActivity, R.layout.layout_hzqy_menu, mLayoutMenu);
		
		ViewUtils.inject(this, mMenuView);
		mBtLeft.setVisibility(View.GONE);
		mBtRight.setVisibility(View.GONE);
	}


}
