
package com.zy.booking.modle;

import com.emsg.sdk.util.JsonUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zy.booking.R;
import com.zy.booking.activitys.SampleHolderActivity;
import com.zy.booking.util.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class UserIconAdapter extends SampleListAdapter {

    public static final String USERID = "userId";

    public static final String NICKNAME = "nickname";

    public static final String PHOTO = "photo";

    public static final String PHONE = "phone";

    public static final String SEX = "sex";

    public UserIconAdapter(Context mContext, JsonArray mData) {
        super(mContext, mData);
    }

    @Override
    public View getView(int posi, View viewTemp, ViewGroup arg2) {
        if (viewTemp == null) {
            viewTemp = LayoutInflater.from(context).inflate(R.layout.layout_icon_display, null);
        }

        JsonObject mJsonObjct = getItem(posi);

        ImageView mImageView = ViewHolder.get(viewTemp, R.id.iv_circleicon);
        TextView mTextView = ViewHolder.get(viewTemp, R.id.tv_temp_text);

        String url = JsonUtil.getAsString(mJsonObjct, PHOTO);
        mBitmapUtils.display(mImageView, url);

        String nickName = JsonUtil.getAsString(mJsonObjct, NICKNAME);
        mTextView.setText(nickName);

        final String mUid = JsonUtil.getAsString(mJsonObjct, USERID);
        viewTemp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                intentToUserInfo(mUid);
            }
        });

        return viewTemp;

    }

    private void intentToUserInfo(String id) {
        Intent mIntent = new Intent(context, SampleHolderActivity.class);
        mIntent.putExtra(SampleHolderActivity.TAG_ID, id);
        mIntent.putExtra(SampleHolderActivity.TAG_TAG, SampleHolderActivity.TAG_SHOWUSERINFO);
        context.startActivity(mIntent);
    }

    // {"userId":"356422a54fb541cfb38d5ff058f853e5",
    // "username":"13520746671","nickname":"超人",
    // "phone":"13520746670",
    // "photo":"http://202.85.221.165:8080/app-img/356422a54fb541cfb38d5ff058f853e5/8332de3ca11f4ef683bcf7c02ecc48c7.jpeg"
    // ,"sex":"man"}

}
