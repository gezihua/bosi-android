package com.bosi.chineseclass.components;

import android.app.AlertDialog;
import android.content.DialogInterface.OnDismissListener;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LoadingDialog {

	public AlertDialog mDialog;

	BaseActivity mActivity;

	@ViewInject(R.id.dialog_loading_pb)
	ProgressBar mProgressBar;
	
	@ViewInject(R.id.dialog_loading_title_tv)
	TextView mTvDownloadintTitle;
	
	public LoadingDialog(BaseActivity mActivity) {
		this.mActivity = mActivity;
	}
	OnDismissListener mOnDismissListener;
	public void setOnDismissListener(OnDismissListener mOnDismissListener){
		this.mOnDismissListener = mOnDismissListener;
	}
	public void show(){
		if(mDialog!=null &&mDialog.isShowing()||mActivity.isFinishing())return;
		mDialog = new android.app.AlertDialog.Builder(mActivity).create();
		mDialog.show();
		
		mDialog.setOnDismissListener(mOnDismissListener);
		initExitSystemDialog();
	}
	
	public void dismiss(){
		mDialog.dismiss();
	}
	private void initExitSystemDialog() {
		View mViewExit = View.inflate(mActivity,
				R.layout.dialog_layout_loading, null);
		ViewUtils.inject(this, mViewExit);
		mDialog.setContentView(mViewExit);
		mDialog.setCanceledOnTouchOutside(false);
	}
	
	public void updateProgress(int progress,int max){
		mProgressBar.setMax(max);
		if(progress>0)
		mProgressBar.setProgress(progress);
	}
	
	public void updateLoadingTitle(String title ){
		mTvDownloadintTitle .setText(title);
	}
	

}
