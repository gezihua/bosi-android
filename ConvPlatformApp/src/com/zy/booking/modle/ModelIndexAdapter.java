package com.zy.booking.modle;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.util.ViewHolder;

public class ModelIndexAdapter extends SampleListAdapter{
	public ModelIndexAdapter(Context mContext, JsonArray mData) {
		super(mContext, mData);
	}

	public final static String MODELID = "modelId";
	private final static String NAME = "name";
	private final static String PHOTO = "photo";
	private final static String AGE ="age";
	private final static String TAGS = "tags";


	public void initBitmapUtils(Context mContext){
		mBitmapUtils = CpApplication.getApplication().mBitmapManager.mBitmapUtils;
		mConfig = new BitmapDisplayConfig();
		mConfig.setLoadFailedDrawable(context.getResources().getDrawable(
				R.drawable.ic_launcher));
		mConfig.setLoadingDrawable(context.getResources().getDrawable(
				R.drawable.model));
	}
	



	@Override
	public int getCount() {
		int count  = super.getCount();
		return count;
	}




	@Override
	public View getView(int position, View viewTemp, ViewGroup arg2) {
		
		if(viewTemp ==null){
			viewTemp = LayoutInflater.from(context).inflate(R.layout.layout_item_modelidex, null);
		}
		JsonObject mJsonObject = getItem(position);
		String url = JsonUtil.getAsString(mJsonObject, PHOTO);
		ImageView mInageView = ViewHolder.get(viewTemp, R.id.iv_model_index);
		mBitmapUtils.display(mInageView, url);
		
		TextView mTags = ViewHolder.get(viewTemp, R.id.modelName);
		mTags.setText(JsonUtil.getAsString(mJsonObject, NAME));
		
		return viewTemp;
	}
	
	

}
