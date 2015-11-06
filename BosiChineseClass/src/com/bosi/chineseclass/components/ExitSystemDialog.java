package com.bosi.chineseclass.components;

import java.util.HashMap;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.util.Utils;
import com.bosi.chineseclass.task.UpLoadBpcyTask;
import com.bosi.chineseclass.task.UpLoadBphzTask;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

public class ExitSystemDialog {
	FeedbackAgent mFbagent;
	public AlertDialog mDialog;
	private BaseActivity mActivity;
	UpLoadBphzTask mUpLoadBpHzTask;
	UpLoadBpcyTask mUploadBpcyTask;
	MyReciver mReviver;

	public ExitSystemDialog(BaseActivity mActivity) {
		this.mActivity = mActivity;
		mUpLoadBpHzTask = new UpLoadBphzTask(mActivity);
		mUploadBpcyTask = new UpLoadBpcyTask(mActivity);

		mReviver = new MyReciver();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(AppDefine.ZYDefine.ACTION_BROADCAST_UPBPCYOVER);
		mFilter.addAction(AppDefine.ZYDefine.ACTION_BRPADCAST_UPBPHZOVER);
		mActivity.registerReceiver(mReviver, mFilter);

		mDialog = new android.app.AlertDialog.Builder(mActivity).create();
		mDialog.show();
		initExitSystemDialog();
		// mFbagent.sync();
	}

	@OnClick(R.id.dialog_exit_bt_good)
	public void actionGood(View mView) {
		if (!Utils.hasNetwork(mActivity)) {
			mActivity.showToastLong("因需要同步学习记录请联网后退出系统 ..");
			if (mDialog != null)
				mDialog.dismiss();
			return;
		}
		HashMap<String, String> mHashData = new HashMap<String, String>();
		mHashData.put("actName", mActivity.getClass().getName());
		MobclickAgent.onEvent(mActivity, "ID_ZAN", mHashData);
		mDialog.dismiss();
		cleanDataAsy();
	}

	private void cleanDataAsy() {
		mActivity.showProgresssDialogWithHint("正在准备退出系统...");
		mActivity.AsyTaskBaseThread(new Runnable() {

			@Override
			public void run() {
				MobclickAgent.onKillProcess(mActivity);
				BSApplication.getInstance().mStorage
						.deleteDirectory(AppDefine.FilePathDefine.APP_DICTDITALNPATH);
				BSApplication.getInstance().mStorage
						.deleteDirectory(AppDefine.FilePathDefine.APP_PINYINLEARNPATH);
				BSApplication.getInstance().mStorage
						.deleteDirectory(AppDefine.FilePathDefine.APP_CYDITALNPATH);
				mUpLoadBpHzTask.sendDataAsy();
				mUploadBpcyTask.sendDataAsy();
			}
		}, new Runnable() {

			@Override
			public void run() {
			}
		});
	}

	boolean isUpLoadBphzOver = false;
	boolean isUploadBpcyOver = false;

	class MyReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent mIntent) {

			if (mIntent.getAction().equals(
					AppDefine.ZYDefine.ACTION_BROADCAST_UPBPCYOVER)) {
				isUploadBpcyOver = true;
			} else if (mIntent.getAction().equals(
					AppDefine.ZYDefine.ACTION_BRPADCAST_UPBPHZOVER)) {
				isUpLoadBphzOver = true;
			}
			if (isUploadBpcyOver && isUpLoadBphzOver) {
				actionExist();
			}
		}
	}

	private void actionExist() {
		mActivity.dismissProgressDialog();
		BSApplication.getInstance().destroySystem();
	}

	@OnClick(R.id.dialog_exit_bt_normal)
	public void actionNormal(View mView) {
		if (!Utils.hasNetwork(mActivity)) {
			mActivity.showToastLong("因需要同步学习记录请联网后退出系统 ..");
			if (mDialog != null)
				mDialog.dismiss();
			return;
		}
		HashMap<String, String> mHashData = new HashMap<String, String>();
		mHashData.put("actName", mActivity.getClass().getName());
		MobclickAgent.onEvent(mActivity, "ID_NORMAL", mHashData);
		mDialog.dismiss();
		cleanDataAsy();
	}

	@OnClick(R.id.dialog_exit_bt_feedback)
	public void actionFeedBack(View mView) {
		mDialog.dismiss();
		mFbagent.startFeedbackActivity();
	}

	private void initExitSystemDialog() {
		mFbagent = new FeedbackAgent(mActivity);
		View mViewExit = View.inflate(mActivity, R.layout.dialog_layout_exit,
				null);
		ViewUtils.inject(this, mViewExit);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setContentView(mViewExit);
	}

}
