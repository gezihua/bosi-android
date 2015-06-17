package com.bosi.chineseclass.fragments.hzcs;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.XutilImageLoader;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;

//汉字常识的基础功能
public abstract class AbsHzcsFragment extends BaseFragment  implements OnClickListener{

	XutilImageLoader mImageLoader;

	LinearLayout mLayoutMenu;

	ImageView mIvDital;

	Button mBtLeft;
	Button mBtRight;

	String mCurrentData[];
	int currentPosition = -1;

	View mViewHead;
	
	HeadLayoutComponents mHeadActionBar;
	
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
		mLayoutMenu = (LinearLayout) mBaseView.findViewById(R.id.ll_hzcs_leftmenu);
		
		mIvDital = (ImageView) mBaseView.findViewById(R.id.iv_hzcs_dital);
		
		mBtLeft = (Button) mBaseView.findViewById(R.id.bt_hzcs_dital_left);
		mBtLeft.setOnClickListener(this);
		mBtRight = (Button) mBaseView.findViewById(R.id.bt_hzcs_dital_right);
		mBtRight.setOnClickListener(this);
		
		mViewHead = mBaseView.findViewById(R.id.headactionbar);
		
		mHeadActionBar = new HeadLayoutComponents(mActivity,mViewHead);
		initMenu();
	}

	public abstract void initMenu();

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

	public void actionRight(View mView) {
		currentPosition++;
		if (currentPosition == mCurrentData.length) {
			return;

		}
		if (currentPosition == mCurrentData.length - 1) {
			mBtRight.setVisibility(View.GONE);
			mBtLeft.setVisibility(View.VISIBLE);

		}
		updateDitalPg();
	}
	
	
	protected void updateDitalPg() {
		mImageLoader.getBitmapFactory().display(mIvDital,
				mCurrentData[currentPosition]);
	}

	@Override
	public void onClick(View mView) {
		switch (mView.getId()) {
		case R.id.bt_hzcs_dital_left:
			actionLeft(mView);
			break;
		case R.id.bt_hzcs_dital_right:
			actionRight(mView);
			break;
			
		default:
			break;
		}
	}

	

}
