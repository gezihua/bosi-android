package com.bosi.chineseclass.components;


import android.app.AlertDialog;
import android.view.View;

import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class PhoneLoginTimeOutDialog {
	public AlertDialog mDialog;
	private BaseActivity mActivity;

	public PhoneLoginTimeOutDialog(BaseActivity mActivity) {
		this.mActivity = mActivity;

		mDialog = new android.app.AlertDialog.Builder(mActivity).create();
		mDialog.show();
		initExitSystemDialog();
	}

	@OnClick(R.id.dialog_phoneusertimeout_exit)
	public void actionExit(View mView) {
		mDialog.dismiss();
		BSApplication.getInstance().destroySystem();
	}
	
	@OnClick(R.id.dialog_phoneusertimeout_learnmore)
	public void actionLearnMore(View mView) {
		mDialog.dismiss();
		//跳转到购买页面
		BSApplication.getInstance().destroySystem();
		
	}
	
	
	private void initExitSystemDialog() {
		View mViewExit = View.inflate(mActivity, R.layout.dialog_layout_phoneusertimeout,
				null);
		ViewUtils.inject(this, mViewExit);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setContentView(mViewExit);
	}
	
	

}
