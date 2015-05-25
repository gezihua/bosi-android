package com.zy.booking.modle;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.activitys.SampleHolderActivity;
import com.zy.booking.util.ViewHolder;

public class FriendsListAdapter extends SampleListAdapter {
	private final String USERNAME = "username";
	public static final String NICKNAME = "nickname";
	private final String PHOTO = "photo";
	public static final String USRID = "userId";
	
	private final String SEX = "sex";
	
	public boolean isModifyStatus = false;
	
	public void setStatus (boolean isModifyStatus){
	    this.isModifyStatus = isModifyStatus;
	}
	
	public FriendsListAdapter(Context mContext, JsonArray mData) {
		super(mContext, mData);
	}
	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public View getView(int posi, View viewTemp, ViewGroup arg2) {
		if (viewTemp == null) {
			viewTemp = LayoutInflater.from(context).inflate(
					R.layout.child_item_layout, null);
		}
		JsonObject mJsonData = (JsonObject) mListData.get(posi);
		String nickName = JsonUtil.getAsString(mJsonData, NICKNAME);
		String photo = JsonUtil.getAsString(mJsonData, PHOTO);
		final String mId  =  JsonUtil.getAsString(mJsonData, USRID);
		String sex = JsonUtil.getAsString(mJsonData, SEX);
		
		
		ImageView mImageButton = ViewHolder.get(viewTemp, R.id.item_friend_icon);
		TextView mTvName = ViewHolder.get(viewTemp, R.id.tv_item_friend_name);
		TextView mTvIntro = ViewHolder.get(viewTemp, R.id.tv_item_friendslist_intro);
		
		CpApplication.getApplication().mBitmapManager.disPlayImage(mImageButton, photo);
		
		mTvName.setText("昵称:"+nickName);
		
		
		if(!TextUtils.isEmpty(sex)){
		    mTvIntro.setText(sex.equals("0")?"性别:男":"性别：女");
		}
		
		mImageButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Intent mIntent = new Intent(context,SampleHolderActivity.class);
                mIntent.putExtra(SampleHolderActivity.TAG_ID, mId);
                mIntent.putExtra(SampleHolderActivity.TAG_TAG, SampleHolderActivity.TAG_SHOWUSERINFO);
                context.startActivity(mIntent);
            }
        });
		
		return viewTemp;
	}

}
