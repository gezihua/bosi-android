package com.zy.booking.fragments;

import com.zy.booking.control.SalesControl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SalesFragment extends BaseFragment{
	
	SalesControl mSalesControl;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected View getBasedView() {
		mSalesControl = new SalesControl(this.mActivity);
		mSalesControl.getAccessToken();
		return null;
	}

	@Override
	void afterViewInject() {
		
	}
	

	
}
