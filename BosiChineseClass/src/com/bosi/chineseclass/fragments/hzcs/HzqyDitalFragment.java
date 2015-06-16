package com.bosi.chineseclass.fragments.hzcs;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bosi.chineseclass.R;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

//汉字起源
public class HzqyDitalFragment extends AbsHzcsFragment {

	@ViewInject(R.id.ll_hzcs_leftmenu)
	LinearLayout mLayoutMenu;

	@ViewInject(R.id.iv_hzcs_dital)
	ImageView mIvDital;

	@ViewInject(R.id.bt_hzcs_dital_left)
	Button mBtLeft;
	@ViewInject(R.id.bt_hzcs_dital_right)
	Button mBtRight;

	String mCurrentData[];
	int currentPosition = -1;
	

	// 仓颉照字 复习变话
	@Override
	@OnClick(R.id.bt_hzcs_dital_left)
	public void actionLeft(View mView) {
		currentPosition--;
		if (currentPosition < 0)
			return;
		
		if (currentPosition == 0) {
			mBtLeft.setVisibility(View.GONE);
			mBtRight.setVisibility(View.VISIBLE);
		}
		updateDitalPg();
	}

	@Override
	@OnClick(R.id.bt_hzcs_dital_right)
	public void actionRight(View mView) {
		currentPosition++;
		if (currentPosition == mCurrentData.length) {
			return;
			
		}
		if (currentPosition == mCurrentData.length-1) {
			mBtRight.setVisibility(View.GONE);
			mBtLeft.setVisibility(View.VISIBLE);
			
		}
		updateDitalPg();
		
		
	}
	
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
		mBtLeft.setVisibility(View.GONE);
		mBtRight.setVisibility(View.GONE);
	}

	private void updateDitalPg() {
		mImageLoader.getBitmapFactory().display(mIvDital,
				mCurrentData[currentPosition]);
	}

}
