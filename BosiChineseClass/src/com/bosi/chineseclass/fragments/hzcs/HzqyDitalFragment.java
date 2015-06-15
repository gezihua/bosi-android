package com.bosi.chineseclass.fragments.hzcs;

import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.os.Build;
import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
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

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void initMenu() {
		// 结绳记事
		ImageView mIvJsjs = new ImageView(mActivity);
		mIvJsjs.setBackground(getResources().getDrawable(R.drawable.icon_jsjs));
		mIvJsjs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mCurrentData = new String[1];
				mCurrentData[0] = "assets/hzqy/jsjs.png";
				mBtRight.setVisibility(View.GONE);
				mBtLeft.setVisibility(View.GONE);
				currentPosition = 0;
				updateDitalPg();
			}
		});

		// 仓颉照字
		ImageView mIvCjzz = new ImageView(mActivity);
		mIvCjzz.setBackground(getResources().getDrawable(R.drawable.icon_cjzz));
		mIvCjzz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mCurrentData = new String[2];
				mCurrentData[0] = "assets/hzqy/cjzz0.png";
				mCurrentData[1] = "assets/hzqy/cjzz1.png";
				mBtRight.setVisibility(View.VISIBLE);
				mBtLeft.setVisibility(View.GONE);
				currentPosition = 0;
				updateDitalPg();
			}
		});

		// 伏羲八卦
		ImageView mIvFxbg = new ImageView(mActivity);
		LayoutParams mLayoutParmas = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		mLayoutParmas.setMargins(20, 20, 0, 0);
		mIvFxbg.setLayoutParams(mLayoutParmas);
		mIvFxbg.setBackground(getResources().getDrawable(R.drawable.icon_fxbg));

		mIvFxbg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mCurrentData = new String[1];
				mCurrentData[0] = "assets/hzqy/fxbg.png";
				mBtRight.setVisibility(View.GONE);
				mBtLeft.setVisibility(View.GONE);
				currentPosition = 0;
				updateDitalPg();
			}
		});
		mLayoutMenu.addView(mIvJsjs);
		mLayoutMenu.addView(mIvCjzz);

		mLayoutMenu.addView(mIvFxbg);
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
