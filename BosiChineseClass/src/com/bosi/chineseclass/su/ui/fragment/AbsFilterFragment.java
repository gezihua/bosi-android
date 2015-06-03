
package com.bosi.chineseclass.su.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.su.ui.actvities.WordsDetailActivity;

public abstract class AbsFilterFragment extends BaseFragment {
    protected LayoutInflater mInflater = null;

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        init();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mInflater = LayoutInflater.from(getActivity());
    }

    @Override
    public void afterViewInject() {
    }

    // 在这里进行findviewbyid 的操作
    abstract void init();

    public void startWordsId(long id) {
        Intent intent = new Intent();
        intent.putExtra("words_id", id);
        intent.setClass(getActivity(), WordsDetailActivity.class);
        startActivity(intent);
    }

}
