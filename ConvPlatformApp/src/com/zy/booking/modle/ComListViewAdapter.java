package com.zy.booking.modle;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
public class ComListViewAdapter <T> extends BaseAdapter{

    protected T mListData;
    protected Context context;
    public ComListViewAdapter(Context mContext, T mData){
        this.context = mContext;
        this.mListData = mData;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int args) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        return null;
    }
    

}
