package com.bosi.chineseclass;

import com.lidroid.xutils.ViewUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {
	
	protected LayoutInflater inflater;
	
	protected View mBaseView;
	
	protected BaseActivity mActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.inflater = inflater;
		mBaseView =  getBasedView();
		ViewUtils.inject(this, mBaseView);
		afterViewInject();
		return mBaseView;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = (BaseActivity) getActivity();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	protected abstract View getBasedView();
	
	/**
	 * 做一些初始化数据  但是不用在此方法中做相关网络数据 等耗时操作 （防止viewpagger 预加载导致 白屏）
	 * */
	protected abstract void afterViewInject();
}
