package com.bosi.chineseclass.han.fragments;

import android.content.Intent;
import android.view.View;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.activitys.GameActivity;
import com.bosi.chineseclass.han.activitys.ZjjzyAcitivity;
import com.bosi.chineseclass.han.activitys.ZyCategoryActivity;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ZiyuanSubstanceFragment extends BaseFragment {

    @Override
    protected View getBasedView() {
        return inflater.inflate(R.layout.fragment_layout_ziyuan_substance2, null);
    }

    @Override
    protected void afterViewInject() {
     // TODO Auto-generated method stub
    }

    @OnClick(R.id.ziyuan_icon_game)
    private void IntentToGame(View view){
        Intent intent = new Intent(mActivity, GameActivity.class);
        mActivity.startActivity(intent);
    }

    @OnClick(R.id.bt_zjjzy)
    private void Zjjzy(View view){
        Intent intent = new Intent(mActivity, ZjjzyAcitivity.class);
        startActivity(intent);
    }

    //TODO:将这几个类别封装成一个函数
    @OnClick(R.id.ziyuan_icon_other)
    private void ZyCategoryOther(View view){
        startZyCategory(AppDefine.ZYDefine.CATEGORY_OTHER);
    }

    @OnClick(R.id.ziyuan_icon_ziran)
    private void ZyCategoryZiran(View view){
        startZyCategory(AppDefine.ZYDefine.CATEGORY_ZIRAN);
    }

    @OnClick(R.id.ziyuan_icon_zhiwu)
    private void ZyCategoryZhiwu(View view){
        startZyCategory(AppDefine.ZYDefine.CATEGORY_ZHIWU);
    }

    @OnClick(R.id.ziyuan_icon_ren)
    private void ZyCategoryRen(View view){
        startZyCategory(AppDefine.ZYDefine.CATEGORY_REN);
    }

    @OnClick(R.id.ziyuan_icon_qiwu)
    private void ZyCategoryQiwu(View view){
        startZyCategory(AppDefine.ZYDefine.CATEGORY_QIWU);
    }

    private void startZyCategory(int type) {
        Intent intent = new Intent(mActivity, ZyCategoryActivity.class);
        intent.putExtra(AppDefine.ZYDefine.TYPE_CATEGORY, type);
        startActivity(intent);
    }
}
