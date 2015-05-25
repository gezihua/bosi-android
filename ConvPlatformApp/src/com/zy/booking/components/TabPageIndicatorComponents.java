package com.zy.booking.components;

import com.viewpagerindicator.TabPageIndicator;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup.LayoutParams;

public class TabPageIndicatorComponents extends BaseComponents{
	 TabPageIndicator indicator;
	 
	 private String [] mData;
	
	public TabPageIndicatorComponents(Context mContext) {
		super(mContext);
	}

	@Override
	public void initFatherView() {
		indicator = new TabPageIndicator(mContext);
		indicator.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		mFatherView =  indicator;
		indicator.setBackgroundColor(Color.WHITE);
	}
	
	public void setData(String [] mDataArray){
		this.mData = mDataArray;
		indicator.setTabNames(mData);
	}
	
	public void setViewPagger(ViewPager mViewPager){
		indicator.setViewPager(mViewPager);
	}
	
	

}
