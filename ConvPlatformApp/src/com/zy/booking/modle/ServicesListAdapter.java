package com.zy.booking.modle;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zy.booking.R;
import com.zy.booking.util.ViewHolder;
import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ServicesListAdapter extends SampleListAdapter {
	
	public static final String NAME = "name";
	public static final String INTRODUCTION = "introduction";
	public static final String TAGS = "tags";
	public static final String ADDRESS = "address";
	public static final String IMG0 = "img0";
	public static final String SPID = "spId";

	public ServicesListAdapter(Context mContext, JsonArray mData) {
		super(mContext, mData);
	}

	@Override
	public View getView(int posi, View mView, ViewGroup arg2) {

		if (mView == null) {
			mView = LayoutInflater.from(context).inflate(
					R.layout.item_layout_reqservice, null);
		}
		try {
			JsonObject mJsonData = (JsonObject) mListData.get(posi);
			TextView mTvName = ViewHolder.get(mView, R.id.tv_itemlayout_fname);
			TextView mTvComment = ViewHolder.get(mView,
					R.id.tv_itemlayout_fcomment);
			TextView mTvTime = ViewHolder
					.get(mView, R.id.tv_itemlayout_comtime);
			TextView mTvPlace = ViewHolder
					.get(mView, R.id.tv_itemlayout_cplace);
			TextView tv_availablenum = ViewHolder.get(mView,
					R.id.tv_availablenum);


			mTvName.setText(JsonUtil.getAsString(mJsonData, NAME));
			mTvComment.setText(JsonUtil.getAsString(mJsonData, INTRODUCTION));
			mTvTime.setText(JsonUtil.getAsString(mJsonData,TAGS));
			mTvPlace.setText(JsonUtil.getAsString(mJsonData, ADDRESS));

			// tv_availablenum.setText(JsonUtil.getAsString(mJsonData,
			// "availnum"));

			String img0 = JsonUtil.getAsString(mJsonData, IMG0);
			ImageView mImageView = ViewHolder.get(mView, R.id.iv_item_intro);

			mBitmapUtils.display(mImageView, img0, mConfig);
		} catch (Exception e) {
		}

		return mView;
	}

}
