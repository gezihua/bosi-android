package com.bosi.chineseclass.han.activitys;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.fragments.ZyCategoryFragment;
import com.lidroid.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_zy_category)
public class ZyCategoryActivity extends BaseActivity {
    private ZyCategoryFragment mZyCategoryFragment;

    private FragmentManager mFragManager;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        int type = getIntent().getIntExtra(AppDefine.ZYDefine.TYPE_CATEGORY, -1);

        mFragManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragManager.beginTransaction();  
        mZyCategoryFragment = new ZyCategoryFragment();
        mZyCategoryFragment.setCategory(type);
        transaction.replace(R.id.fl_zy_category_content, mZyCategoryFragment);  
        transaction.commit();
    }


}
