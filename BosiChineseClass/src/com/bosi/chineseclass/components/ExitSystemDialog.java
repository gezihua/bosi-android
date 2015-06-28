package com.bosi.chineseclass.components;

import java.util.HashMap;

import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

import android.app.AlertDialog;
import android.os.Handler;
import android.view.View;

public class ExitSystemDialog  {
	FeedbackAgent mFbagent;
	public AlertDialog mDialog;
	private BaseActivity mActivity;
	
	public ExitSystemDialog(BaseActivity mActivity){
		this.mActivity =  mActivity;
		mDialog = new  android.app.AlertDialog.Builder(mActivity).create();
		mDialog.show();
		initExitSystemDialog();
		//mFbagent.sync();
	}
	
	@OnClick(R.id.dialog_exit_bt_good)
	public void actionGood(View mView){
		HashMap< String , String> mHashData = new HashMap<String, String>();
		mHashData.put("actName", mActivity.getClass().getName());
		MobclickAgent.onEvent(mActivity, "ID_ZAN",mHashData);
		mDialog.dismiss();
		BSApplication.getInstance().destroySystem();
	}
	@OnClick(R.id.dialog_exit_bt_normal)
    public void actionNormal(View mView){
		HashMap< String , String> mHashData = new HashMap<String, String>();
		mHashData.put("actName", mActivity.getClass().getName());
		MobclickAgent.onEvent(mActivity, "ID_NORMAL",mHashData);
		mDialog.dismiss();
		BSApplication.getInstance().destroySystem();
	}
	@OnClick(R.id.dialog_exit_bt_feedback)
    public void actionFeedBack(View mView){
		mDialog.dismiss();
		mFbagent.startFeedbackActivity();
    }
	
	private void initExitSystemDialog(){
		mFbagent = new FeedbackAgent(mActivity);
		View mViewExit = View.inflate(mActivity, R.layout.dialog_layout_exit, null);
		ViewUtils.inject(this, mViewExit);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setContentView(mViewExit);
	}

}
