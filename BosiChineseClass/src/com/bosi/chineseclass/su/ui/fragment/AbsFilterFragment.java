
package com.bosi.chineseclass.su.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
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

    public void startWord(String word) {
        Intent intent = new Intent();
        intent.putExtra("word", word);
        intent.setClass(getActivity(), WordsDetailActivity.class);
        intent.putExtra(WordsDetailActivity.EXTRA_NAME_WORDS_NAME,getResources().getString(R.string.wrod_dital_name));
        intent.putExtra(WordsDetailActivity.EXTRA_NAME_WORDS_NAME,getResources().getString(R.string.wrod_dital_name));
        startActivity(intent);
    }

    public abstract String getSelectedRstWord(int postion);

    public void setResultOnItemClick(AbsListView absListView) {
        if (absListView != null) {
            absListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String temp = getSelectedRstWord(position);
                    if (TextUtils.isEmpty(temp)) {
                        return;
                    }
                    startWord(temp);
                }
            });
        }
    }

}
