package com.zy.booking.modle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zy.booking.R;
import com.zy.booking.util.ViewHolder;
import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SearchDitalAdapter extends SampleListAdapter {
	SEARCHTAG Tag;
	public SearchDitalAdapter(Context mContext, JsonArray mData) {
		super(mContext, mData);
	}

	
	public void setSearchEnum(SEARCHTAG mTag){
		this.Tag = mTag;
	}
	@Override
	public View getView(int posi, View mView, ViewGroup arg2) {

		if (mView == null) {
			mView = LayoutInflater.from(context).inflate(
					R.layout.item_layout_content, null);
		}

		JsonObject mJsonData = (JsonObject) mListData.get(posi);
		
		ImageView mImageIcon =  ViewHolder.get(mView, R.id.iv_item_intro);
		mBitmapUtils.display(mImageIcon,
				JsonUtil.getAsString(mJsonData, "url"), mConfig);
		
		
		TextView mname = ViewHolder.get(mView, R.id.tv_itemlayout_fname);
		
		TextView mtag = ViewHolder.get(mView, R.id.tv_itemlayout_ftag);
		
		TextView mcontent = ViewHolder.get(mView, R.id.tv_itemlayout_fcomment);
		
		mname.setText(JsonUtil.getAsString(mJsonData, "name"));
		mtag.setText(JsonUtil.getAsString(mJsonData, "tag"));
		mcontent.setText(JsonUtil.getAsString(mJsonData, "place"));
		
		if(Tag == SEARCHTAG.RECOMMEND){
			RatingBar ratBar = ViewHolder.get(mView, R.id.rb_dital);
			ratBar.setVisibility(View.VISIBLE);
			ratBar.setRating(JsonUtil.getAsInt(mJsonData, "rating"));
		}
		else{
			LinearLayout mlayoutLoc =  ViewHolder.get(mView, R.id.ll_itemlayoutcontentplace);
			mlayoutLoc.setVisibility(View.VISIBLE);
			TextView mTvLocNearBy =  ViewHolder.get(mView, R.id.tv_nearby_distance);
			mTvLocNearBy.setText(JsonUtil.getAsString(mJsonData, "distance"));
		}
		return mView;
	}
	
	public enum SEARCHTAG{
		NEARBY, RECOMMEND
	}

}
