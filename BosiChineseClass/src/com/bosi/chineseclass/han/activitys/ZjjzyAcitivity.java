package com.bosi.chineseclass.han.activitys;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.fragments.ZjjzyFragment;
import com.lidroid.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_zjjzy)
public class ZjjzyAcitivity extends BaseActivity {

    private ZjjzyFragment mZjjzyFragment;

    private FragmentManager mFragManager;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        mFragManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragManager.beginTransaction();  
        mZjjzyFragment = new ZjjzyFragment();  
        transaction.replace(R.id.fl_zjjzy_content, mZjjzyFragment);  
        transaction.commit();
    }

}
