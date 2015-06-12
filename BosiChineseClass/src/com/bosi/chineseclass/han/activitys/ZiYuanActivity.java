package com.bosi.chineseclass.han.activitys;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.han.fragments.ZiyuanBihuaFragment;
import com.bosi.chineseclass.han.fragments.ZiyuanSubstanceFragment;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_ziyuan)
public class ZiYuanActivity extends BaseActivity{

    @ViewInject(R.id.headactionbar)
    View mHeadActionBar;
    
    HeadLayoutComponents mHeadActionBarComp;

    private FragmentManager mFragManager;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        initHeadActionBarComp();


        mFragManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragManager.beginTransaction();
        ZiyuanBihuaFragment bihuaFragment2 = new ZiyuanBihuaFragment();
        transaction.replace(R.id.layout_frame_ziyuan_bishu, bihuaFragment2);
        ZiyuanSubstanceFragment categoryFragment = new ZiyuanSubstanceFragment();
        transaction.replace(R.id.layout_frame_ziyuan_category, categoryFragment);
        transaction.commit();
    }

    private void initHeadActionBarComp() {
        mHeadActionBarComp = new HeadLayoutComponents(this, mHeadActionBar);

        mHeadActionBarComp.setTextMiddle("基本资源", -1);
        mHeadActionBarComp.setDefaultLeftCallBack(true);
        mHeadActionBarComp.setDefaultRightCallBack(true);
    }

}
