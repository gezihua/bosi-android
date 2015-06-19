package com.bosi.chineseclass.fragments.hzcs;

import android.view.View;

import com.bosi.chineseclass.R;
import com.lidroid.xutils.ViewUtils;


//趣味汉字
public class QwhzDitalFragment extends AbsHzcsFragment{
	

	@Override
	public void initMenu() {

		mImageLoader.getBitmapFactory().display(mIvDital,
				"assets/hzqy/cjzz.png");
		View mMenuView = View.inflate(mActivity, R.layout.layout_qwhz_menu, null);
		mLayoutMenu.addView(mMenuView);
		ViewUtils.inject(this, mMenuView);
		mBtLeft.setVisibility(View.GONE);
		mBtRight.setVisibility(View.GONE);
	
	}

	@Override
	public void downloadimgs() {
		
	}

	@Override
	public void initWholeArray() {
		
	}

	@Override
	protected void displayBgView() {
		mViewBody.setBackgroundResource(R.drawable.quhz_bg);
	}
	

}
