
package com.bosi.chineseclass.su.ui.actvities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.su.ui.fragment.FilerPyFragment;
import com.bosi.chineseclass.su.ui.fragment.FiterStokeFragment;

import java.util.ArrayList;

public class DictionaryAcitvity extends BaseActivity {
    private final static String sTitleList[] = {
        "拼音"
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.dictionary_main_layout);
        init();
    }

    private void init() {
        final ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new FilerPyFragment());
        fragments.add(new FilerPyFragment());
        fragments.add(new FiterStokeFragment());
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

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
                return sTitleList[0];
            }
        };
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
    }

}
