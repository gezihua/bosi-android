package com.zy.booking.fragments;

import java.util.List;




import org.apache.http.NameValuePair;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.view.MyLoadingProgressBar;
import com.lidroid.xutils.ViewUtils;

public abstract class BaseFragment extends Fragment {
	LayoutInflater inflater;
	
	View mBaseView;
	
	
	BaseActivity mActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.inflater = inflater;
		View mView =  getBasedView();
		ViewUtils.inject(this, mView);
		afterViewInject();
		return mView;
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	};

	MyLoadingProgressBar myLoadingProgressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mActivity = (BaseActivity) getActivity();
	}

	public void showProgresssDialog() {
		myLoadingProgressBar = new MyLoadingProgressBar(getActivity());
		myLoadingProgressBar.show();
	}

	public void dismissProgressDialog() {
		if (myLoadingProgressBar != null)
			myLoadingProgressBar.dialogDismiss();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	protected void sendData(String data, String url,
			final OnHttpActionListener mTatget, final int requestCode) {
	}
	
	protected void sendData(List<NameValuePair> data, String url,
			final OnHttpActionListener mTatget, final int requestCode) {
		CpApplication.getApplication().sendData(data, url, mTatget, requestCode);
	}
	
	public void playYoYo(View mView){
		((BaseActivity )getActivity()).playYoYo(mView);
	}
	
	protected abstract View getBasedView();
	
	/**
	 * 做一些初始化数据  但是不用在此方法中做相关网络数据 等耗时操作 （防止viewpagger 预加载导致 白屏）
	 * */
	abstract void afterViewInject();
}
