package com.bosi.chineseclass.fragments.hzcs;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

import com.bosi.chineseclass.R;
import com.lidroid.xutils.ViewUtils;


//汉字演变
public class HzybDitalFragment extends AbsHzcsFragment{


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

	@Override
	public void downloadimgs() {
		
	}


}
