package com.zy.booking;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.view.MyLoadingProgressBar;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.HttpHandler;

public class BaseActivity extends FragmentActivity {
	
	protected BitmapUtils mBitmapUtils;

	MyLoadingProgressBar myLoadingProgressBar;
	protected Activity mContext;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.mContext = this;
		ViewUtils.inject(this);
		CpApplication.getApplication().mActivityStack.push(this);
		
	}

	public String getResourceFromId(int id) {
		return getResources().getString(id);
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

	public void showProgresssDialog() {
		myLoadingProgressBar = new MyLoadingProgressBar(this);
		myLoadingProgressBar.show();
	}
	public void showProgresssDialogWithHint(String hint) {
        myLoadingProgressBar = new MyLoadingProgressBar(this);
        myLoadingProgressBar.showWithHint(hint);
    }

	public void dismissProgressDialog() {
		if(myLoadingProgressBar!=null &&myLoadingProgressBar.isProgressShowing())
		myLoadingProgressBar.dialogDismiss();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CpApplication.getApplication().mActivityStack.remove(this);
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

	public void sendData(String data, String url,
			final OnHttpActionListener mTatget, final int code) {}
	
	
	public void sendData(List<NameValuePair> mList, String url,
			final OnHttpActionListener mTatget, final int code) {
		CpApplication.getApplication().sendData(mList, url, mTatget, code);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		mHttpHandler = CpApplication.getApplication().getHttpHandler();
		if(mHttpHandler!=null){
			mHttpHandler.cancel(true);
		}
	}
	
    public void showToastLong(String text){
    	Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
    
    public void showToastShort(String text){
    	Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
	public void playYoYo(View mView) {
		YoYo.with(Techniques.Shake).duration(700).playOn(mView);
	}
	
	
}
