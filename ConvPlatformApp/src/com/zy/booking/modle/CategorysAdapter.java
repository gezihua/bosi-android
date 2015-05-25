package com.zy.booking.modle;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;

import com.zy.booking.util.DataTools;

public class CategorysAdapter extends ComListViewAdapter<List<? extends Object>>{

	public CategorysAdapter(Context mContext, List<? extends Object> mData) {
		super(mContext, mData);
		
	}

	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public Integer getItem(int args) {
		return (Integer) mListData.get(args);
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		ImageView mImageView = new ImageView(context);
		LayoutParams mParams = new LayoutParams(DataTools.dip2px(context, 80),DataTools.dip2px(context, 80));
		mImageView.setLayoutParams(mParams);
		mImageView.setBackgroundResource(getItem(position));
		return mImageView;
	}
	
	

}
