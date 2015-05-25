
package com.zy.booking.modle.pagefactory;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.R;
import com.zy.booking.components.TabPageIndicatorComponents;
import com.zy.booking.fragments.AlbumsFragment;
import com.zy.booking.fragments.ChangeGroupFragment;
import com.zy.booking.fragments.ChangeModelInfoFragment;
import com.zy.booking.util.PreferencesUtils;

import java.util.ArrayList;

public class ChangeModelInfoFactory extends AbsFactory  {

    ViewPager mViewPagger;

    @Override
    public void productTopTab(ViewGroup mViewGroupBottom) {
        super.productTopTab(mViewGroupBottom);
        mViewGroupBottom.setVisibility(View.VISIBLE);
        TabPageIndicatorComponents mTabComponents = new TabPageIndicatorComponents(
                mContext);
        mTabComponents.setData(mContext.getResources()
                .getString(R.string.model_changeinfo_tabs).split("#"));
        mViewGroupBottom.addView(mTabComponents.getView());
        mTabComponents.setViewPagger(mViewPagger);
    }

    BaseActivity mBaseActivity;

    public ChangeModelInfoFactory(Context mContext) {
        super(mContext);

    }

    public ChangeModelInfoFactory(Context mContext, ViewPager mViewPagger) {
        super(mContext);
        this.mViewPagger = mViewPagger;
        this.mBaseActivity = (BaseActivity) mContext;
    }

    private String modelId;

    public void setServiceId(String modelId) {
        this.modelId = modelId;
    }

    ChangeModelInfoFragment mChangeModelInfoFragment;
    ChangeGroupFragment mChangeGroupFragment;
    AlbumsFragment mAlbumsFragment;
  

    @Override
    public ArrayList<Fragment> productBody() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        mChangeModelInfoFragment = new ChangeModelInfoFragment();
        
        
        fragments.add(mChangeModelInfoFragment);
        mChangeGroupFragment = new ChangeGroupFragment();
        fragments.add(mChangeGroupFragment);
        
        
        String modelId = PreferencesUtils.getString(mContext, AppDefine.KEY_MODELID);
        mAlbumsFragment = new AlbumsFragment(modelId);
        mAlbumsFragment.setIsModify(true);
        
        fragments.add(mAlbumsFragment);
        return fragments;
    }

    @Override
    public void productMenu(ViewGroup mViewGroupBottom) {
    }

    

   
   

}
