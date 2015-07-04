
package com.bosi.chineseclass.su.ui.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;

public class TextViewFragment extends BaseFragment {
    private View mRootView;
    private String mWord;

    public TextViewFragment(String mWord) {
        super();
        this.mWord = mWord;
    }

    public TextViewFragment(){
    	
    }
    @Override
    protected View getBasedView() {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.dictionary_fragment_text,
                null, false);
        if (!TextUtils.isEmpty(mWord)) {
            ((TextView) mRootView.findViewById(R.id.fragment_text)).setText(mWord);
        }
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void load(String word) {
        ((TextView) mRootView.findViewById(R.id.fragment_text)).setText(word);
    }

    @Override
    protected void afterViewInject() {

    }

}
