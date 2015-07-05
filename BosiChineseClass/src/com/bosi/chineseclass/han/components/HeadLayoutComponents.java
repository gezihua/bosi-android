
package com.bosi.chineseclass.han.components;

import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.R;

import android.app.Activity;

import android.content.Context;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class HeadLayoutComponents extends BaseComponents {

    public HeadLayoutComponents(Context mContext, View mView) {
        super(mContext, mView);
        mView.setVisibility(View.VISIBLE);
    }

    private TextView mTvLeft;

    private TextView mTvMiddle;

    private TextView mTvRight;

    private TextView mTvMiddleDown;

    public TextView getMiddleText() {
        mTvMiddleDown = (TextView) mFatherView.findViewById(R.id.common_head_middle);
        mTvMiddleDown.setVisibility(View.VISIBLE);
        return mTvMiddleDown;
    }

    @Override
    public void initFatherView() {
        mTvLeft = (TextView) mFatherView.findViewById(R.id.common_head_left);
        mTvMiddle = (TextView) mFatherView.findViewById(R.id.common_head_middle);
        mTvRight = (TextView) mFatherView.findViewById(R.id.common_head_right);
        setDefaultLeftCallBack(true);
        setDefaultRightCallBack(true);
        /*
         * added for search
         * */
        initSearchable();
    }

    public void setDefaultLeftCallBack(boolean isLeftCallBack) {
        if (isLeftCallBack) {
            mTvLeft.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Activity mActivity = (Activity) mContext;
                    mActivity.finish();
                }
            });
        }
    }

    public void setDefaultRightCallBack(boolean isLeftCallBack) {
        if (isLeftCallBack) {
            mTvRight.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    BSApplication.getInstance().exitApp();
                }
            });
        }
    }

    public void setTextLeft(String text, int resid) {
        mTvLeft.setText(text);
        if (resid != -1)
            mTvLeft.setBackgroundResource(resid);
    }

    public void setTextMiddle(String text, int resid) {
        mTvMiddle.setText(text);
        if (resid != -1)
            mTvMiddle.setBackgroundResource(resid);
    }

    public void setTextRight(String text, int resid) {
        mTvRight.setText(text);
        if (resid != -1) {
            mTvRight.setBackgroundResource(resid);
            /*
             * mTvRight.setLayoutParams(new LinearLayout.LayoutParams(DataTools
             * .dip2px(mContext, 30), DataTools.dip2px(mContext, 30)));
             */
        }

    }

    public void setTextRight(String text, int resid, float size) {
        setTextRight(text, resid);
        mTvRight.setTextSize(size);
    }

    public void setLeftOnclickListener(OnClickListener mOnclickListener) {
        mTvLeft.setOnClickListener(mOnclickListener);
    }

    public void setRightOnClickListener(OnClickListener mOnclickListener) {
        mTvRight.setOnClickListener(mOnclickListener);
    }

    /*************** -------------------------------这里添加搜索字母的操作------------------------ ***********************/

    private View mSearchableView;
    private View mSearchConfirm;
    private EditText mSearch;

    public void initSearchable() {
        mSearchableView = mFatherView.findViewById(R.id.search);
        mSearch = (EditText) mSearchableView.findViewById(R.id.seachable);
        mSearchConfirm = mSearchableView.findViewById(R.id.search_confirm);
        mSearchConfirm.setOnClickListener(new SearableClickListener());
    }
    private class SearableClickListener implements View.OnClickListener{

        @Override
        public void onClick(View arg0) {
            String text = mSearch.getText().toString();
            if(!TextUtils.isEmpty(text)){
                if (mSearchableAction!=null) {
                    mSearchableAction.search(text);
                }
                
            }
        }
        
    }

    public static interface SearchableAction {
        public void search(String text);
    }
    public void showSearchable(){
        mSearchableView.setVisibility(View.VISIBLE);
    }
    private SearchableAction mSearchableAction;

    public void setSearchableAction(SearchableAction mSearchableAction) {
        this.mSearchableAction = mSearchableAction;
    }
    

}
