
package com.bosi.chineseclass.su.ui.actvities;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.su.ui.fragment.FilerPyFragment;
import com.bosi.chineseclass.su.ui.fragment.FilterRadicalFragment;
import com.bosi.chineseclass.su.ui.fragment.FiterStokeFragment;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

public class DictionaryAcitvity extends BaseActivity {
    private final static String sTitleList[] = {
            "拼音","部首","起笔"
    };
    View mHeadActionBar;
    HeadLayoutComponents mHeadActionBarComp;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.dictionary_main_layout);
        init();
    }
    private void initHeadActionBarComp() {
        mHeadActionBarComp = new HeadLayoutComponents(this, mHeadActionBar);

        mHeadActionBarComp.setTextMiddle("资源字典", -1);
        mHeadActionBarComp.setDefaultLeftCallBack(true);
        mHeadActionBarComp.setDefaultRightCallBack(true);
    }

    private void init() {
        mHeadActionBar = findViewById(R.id.dic_headactionbar);
        initHeadActionBarComp();
        final ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new FilerPyFragment());
        fragments.add(new FilterRadicalFragment());
        fragments.add(new FiterStokeFragment());
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(
                getSupportFragmentManager()) {

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                // TODO Auto-generated method stub
                return fragments.get(arg0);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                // TODO Auto-generated method stub
                return sTitleList[position%sTitleList.length];
            }
        };
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
//                viewPager.setCurrentItem(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        indicator.setTabNames(sTitleList);
        indicator.setViewPager(viewPager);
       
    }

}
