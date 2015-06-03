package com.bosi.chineseclass.activitys;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.control.SampleHolderControlMake;
import com.bosi.chineseclass.model.SeverFragmentPagerAdapter;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 设置基础的页面布局 用于控制
 * @author zhujohnle
 * @date 2015-06-02
 * @modify 
 * {@link com.bosi.chineseclass.control.SampleHolderControlMake}
 */


@ContentView(R.layout.bosi_layout_sampleholder)
public class SampleHolderActivity extends BaseActivity{
	@ViewInject(R.id.vp_sh_body)
	ViewPager mViewPager;
	
	SampleHolderControlMake mControlMaker;
	
	@Override
	protected void onCreate(Bundle mBundler) {
		super.onCreate(mBundler);
		
		mControlMaker = new SampleHolderControlMake(this, getIntent());
		setupViewPagger();
	}
	
	/**
	 * for set up the viewpager common used
	 */
	private void setupViewPagger(){
		SeverFragmentPagerAdapter  mAdapetr = new SeverFragmentPagerAdapter(getSupportFragmentManager());
		mAdapetr.appendList(mControlMaker.getFragments());
		mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(mPageChangeListener);
        
	}
	
	OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	//static 接口才能被外面识别	我就告诉外面 我需要哪些东西 不需要知道他是怎么给我的 因为我就负责显示
	public static interface ISampleControlInterface{
		public ArrayList<Fragment> getFragments();
	}
}
