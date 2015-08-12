
package com.bosi.chineseclass.su.ui.actvities;

import android.content.Intent;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.XutilImageLoader;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.han.components.HeadLayoutComponents.SearchableAction;
import com.bosi.chineseclass.su.ui.fragment.FilerPyFragment;
import com.bosi.chineseclass.su.ui.fragment.FilterRadicalFragment;
import com.bosi.chineseclass.su.ui.fragment.FiterStokeFragment;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

public class DictionaryAcitvity extends BaseActivity implements SearchableAction {
    private final static String sTitleList[] = {
            "拼音","部首","起笔"
    };
    View mHeadActionBar;
    HeadLayoutComponents mHeadActionBarComp;

    XutilImageLoader mXutilImageLoader;
    private View mMainView; 

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.dictionary_main_layout);
        init();
    }
    private void initHeadActionBarComp() {
        mHeadActionBarComp = new HeadLayoutComponents(this, mHeadActionBar);

        mHeadActionBarComp.setTextMiddle("字源字典", -1);
        mHeadActionBarComp.setDefaultLeftCallBack(true);
        mHeadActionBarComp.setDefaultRightCallBack(true);
        mHeadActionBarComp.showSearchable();
        mHeadActionBarComp.setSearchableAction(this);
    }

    private void init() {
        mMainView = findViewById(R.id.activity_dictionary_main);
        mXutilImageLoader = new XutilImageLoader(this);
        mXutilImageLoader.getBitmapFactory().display(mMainView, "assets/zyzd/bg_activity_dictionary.png");

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
    @Override
    public void search(String word) {
        Intent intent = new Intent();
        intent.putExtra("word", word);
        intent.setClass(this, WordsDetailActivity.class);
        intent.putExtra(WordsDetailActivity.EXTRA_NAME_WORDS_NAME,getResources().getString(R.string.wrod_dital_name));
        startActivity(intent);        
    }

}
