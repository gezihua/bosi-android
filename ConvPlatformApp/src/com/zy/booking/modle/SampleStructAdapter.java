package com.zy.booking.modle;

import java.util.List;


import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.util.DataTools;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class SampleStructAdapter extends ComListViewAdapter<List<? extends Object>>
{

	BitmapUtils mBitmapUtils;
	BitmapDisplayConfig mConfig;
	public SampleStructAdapter(Context mContext,List<? extends Object> mList) {
		super(mContext, mList);
		initBitmapUtils(mContext);
	}
	
	protected void initBitmapUtils(Context mContext){
		mBitmapUtils = CpApplication.getApplication().mBitmapManager.mBitmapUtils;
		mConfig = new BitmapDisplayConfig();
		mConfig.setBitmapMaxSize(new BitmapSize(DataTools.dip2px(mContext, 60),
				DataTools.dip2px(mContext, 60)));
		mConfig.setLoadFailedDrawable(context.getResources().getDrawable(
				R.drawable.ic_launcher));
	}

	@Override
	public  int getCount() {
		return mListData.size();
	}
	public  void  changeDataSource(List<? extends Object> mList){
		this.mListData = mList;
		this.notifyDataSetChanged();
	}

	@Override
	public  Object getItem(int args) {
		return mListData.get(args);
	}
	
	@Override
	public  View getView(int arg0, View arg1, ViewGroup arg2) {
		return super.getView(arg0, arg1, arg2);
	}
	
	

}
