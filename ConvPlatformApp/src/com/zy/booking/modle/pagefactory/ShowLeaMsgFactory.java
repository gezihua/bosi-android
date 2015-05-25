package com.zy.booking.modle.pagefactory;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.zy.booking.R;
import com.zy.booking.components.TabPageIndicatorComponents;
import com.zy.booking.fragments.LeaMsgFragment;
import com.zy.booking.fragments.MySendLeaMsgFragment;

import java.util.ArrayList;

public class ShowLeaMsgFactory extends AbsFactory{
    
    
    ViewPager mViewPagger;
    
    @Override
    public void productTopTab(ViewGroup mViewGroupBottom) {
        super.productTopTab(mViewGroupBottom);
        mViewGroupBottom.setVisibility(View.VISIBLE);
        TabPageIndicatorComponents mTabComponents = new TabPageIndicatorComponents(mContext);
        mTabComponents.setData(mContext.getResources().getString(R.string.showleamsg_name_tabs).split("#"));
        mViewGroupBottom.addView(mTabComponents.getView());
        mTabComponents.setViewPagger(mViewPagger);
    }

    public ShowLeaMsgFactory(Context mContext) {
        super(mContext);
    }
    
    public ShowLeaMsgFactory(Context mContext,ViewPager mViewPagger){
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
        MySendLeaMsgFragment mSendLeaMsgFragment = new MySendLeaMsgFragment(); 
        LeaMsgFragment mLeaMsgFragment = new LeaMsgFragment();
        fragments.add(mSendLeaMsgFragment);
        fragments.add(mLeaMsgFragment);
        return fragments;
    }

    @Override
    public void productMenu(ViewGroup mViewGroupBottom) {
        

    }


}
