package com.zy.booking.modle.pagefactory;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.zy.booking.R;
import com.zy.booking.components.TabPageIndicatorComponents;
import com.zy.booking.fragments.IspAskdCustromsFragment;
import com.zy.booking.fragments.IspOpenDateFragment;
import com.zy.booking.fragments.TimeManagerFragment;

public class IspManagerPageFactory extends AbsFactory{
	
	
	ViewPager mViewPagger;
	
	@Override
	public void productTopTab(ViewGroup mViewGroupBottom) {
		super.productTopTab(mViewGroupBottom);
		mViewGroupBottom.setVisibility(View.VISIBLE);
		TabPageIndicatorComponents mTabComponents = new TabPageIndicatorComponents(mContext);
		mTabComponents.setData(mContext.getResources().getString(R.string.isp_name_tabs).split("#"));
		mViewGroupBottom.addView(mTabComponents.getView());
		mTabComponents.setViewPagger(mViewPagger);
	}

	public IspManagerPageFactory(Context mContext) {
		super(mContext);
	}
	
	public IspManagerPageFactory(Context mContext,ViewPager mViewPagger){
		super(mContext);
		this.mViewPagger = mViewPagger;
	}

	private String serviceId;
	public void setServiceId(String serviceId){
		this.serviceId =serviceId;
	}

	@Override
	public ArrayList<Fragment> productBody() {
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		IspAskdCustromsFragment mAskedCustromsFragment=new IspAskdCustromsFragment();
		TimeManagerFragment mTimeManagerFragment = new TimeManagerFragment();
		
		mAskedCustromsFragment.setServiceId(serviceId);
		mTimeManagerFragment.setSpId(serviceId);
		
		IspOpenDateFragment mOpenDateFragment = new IspOpenDateFragment();
		
		mOpenDateFragment.setServiceId(serviceId);
		fragments.add(mAskedCustromsFragment);
		
		fragments.add(mOpenDateFragment);
		fragments.add(mTimeManagerFragment);
		return fragments;
	}

	@Override
	public void productMenu(ViewGroup mViewGroupBottom) {
		

	}


}
