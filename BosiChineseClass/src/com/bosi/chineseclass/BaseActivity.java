package com.bosi.chineseclass;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.bosi.chineseclass.components.LoadingDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends FragmentActivity {

	protected BitmapUtils mBitmapUtils;

	protected Activity mContext;

	public LoadingDialog mLoadingDialog;
	
	private final Object mSyObject = new Object();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.mContext = this;
		ViewUtils.inject(this);
		BSApplication.getInstance().mActivityStack.push(this);
		mLoadingDialog = new LoadingDialog(this);
	}

	public String getResourceFromId(int id) {
		return getResources().getString(id);
	}

	public void showLoadingDialog() {
		synchronized (mSyObject) {
			mLoadingDialog.show();
		}
	}

	public void updateProgress(int progress, int max) {
		if (mLoadingDialog.mDialog==null) {
			showLoadingDialog();
		}
		if(progress == max){
			dismissProgress();
		}
		mLoadingDialog.updateProgress(progress, max);
	}

	public void dismissProgress() {
		mLoadingDialog.dismiss();
	}

	public String getStringFormat(String format, String... args) {
		return format.format(format, args);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BSApplication.getInstance().mActivityStack.remove(this);
		System.gc();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	HttpHandler<String> mHttpHandler;

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	public void showToastLong(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	public void showToastShort(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public void playYoYo(View mView) {
		YoYo.with(Techniques.Shake).duration(700).playOn(mView);
	}

	//通过开一个线程去快速的实现一个异步的任务
		public void AsyTaskBaseThread(final Runnable mTask,final Runnable mResult){
			new Thread(){
					public void run(){
						mTask.run();
						mResult.run();
					}
			}.start();
		}
}
