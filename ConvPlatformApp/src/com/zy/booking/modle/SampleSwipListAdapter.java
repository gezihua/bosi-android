package com.zy.booking.modle;


import android.content.Context;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zy.booking.R;
import com.zy.booking.util.DataTools;
import com.zy.booking.util.ViewHolder;
import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;

public class SampleSwipListAdapter extends ComListViewAdapter<JsonArray> {

	BitmapUtils mBitmapUtils;
	BitmapDisplayConfig mConfig;

	public SampleSwipListAdapter(Context mContext, JsonArray mData) {
		super(mContext, mData);
		mBitmapUtils = new BitmapUtils(mContext);
		mBitmapUtils.configDefaultBitmapMaxSize(DataTools.dip2px(mContext, 60),
				DataTools.dip2px(mContext, 60));
		mBitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);
		mConfig = new BitmapDisplayConfig();
		mConfig.setLoadFailedDrawable(context.getResources().getDrawable(
				R.drawable.icon));
	}
	

	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public JsonObject getItem(int args) {
		return (JsonObject) mListData.get(args);
	}
	
	public void  changeDataSource(JsonArray mData){
		this.mListData = mData;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int posi, View viewTemp, ViewGroup arg2) {
		if (viewTemp == null) {
			viewTemp = LayoutInflater.from(context).inflate(
					R.layout.item_layout_intro, null);
		}
		ImageView mIvintro = ViewHolder.get(viewTemp, R.id.iv_item_intro);
		JsonObject mJsonObject = getItem(posi);
		mBitmapUtils.display(mIvintro,
				JsonUtil.getAsString(mJsonObject, "url"), mConfig);
		TextView mTvIntro = ViewHolder.get(viewTemp, R.id.tv_itemlayout_intro);
		mTvIntro.setText(JsonUtil.getAsString(mJsonObject, "content"));

		return viewTemp;
	}

}
