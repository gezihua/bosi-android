
package com.zy.booking.fragments;

import android.content.Context;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.AppDefine;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.activitys.ChatActivity;
import com.zy.booking.activitys.SampleHolderActivity;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.components.SwipListViewComponents.OnSwipCallBack;
import com.zy.booking.db.DBUSER;
import com.zy.booking.db.MSGHISTORY;
import com.zy.booking.db.User;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.modle.SampleListAdapter;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.PreferencesUtils;
import com.zy.booking.util.ViewHolder;

public class ContactGroupFragment extends BaseFragment implements
        OnHttpActionListener {
    SwipListViewComponents mSwipListView;

    @ViewInject(R.id.ll_container_body)
    LinearLayout mLayoutBody;

    @Override
    protected View getBasedView() {
        return inflater.from(mActivity).inflate(
                R.layout.layout_common_withoutscroll, null);
    }

    MyGroupAdapter mAdapter;

    @Override
    void afterViewInject() {
        mSwipListView = new SwipListViewComponents(mActivity);

        mAdapter = new MyGroupAdapter(mActivity, mJsonGroupData);
        mLayoutBody.addView(mSwipListView.getView());
        mSwipListView.setAdapter(mAdapter);

        mSwipListView.setSwipCallBack(new OnSwipCallBack() {

            @Override
            public void onReflesh() {
                getUserGroupData();
            }

            @Override
            public void onLoadMore() {

            }

            @Override
            public void onItemClickListener(int position) {
                Intent mIntent = new Intent(mActivity, ChatActivity.class);
                JsonObject mJsonObject = mAdapter.getItem(position);
                String groupId = JsonUtil.getAsString(mJsonObject,
                        mAdapter.GROUPID);
                String groupName = JsonUtil.getAsString(mJsonObject,
                        mAdapter.NAME);
                mIntent.putExtra(ChatActivity.TAG_ISCHATGROUP, true);
                mIntent.putExtra(ChatActivity.TAG_MESSAGE_TO, groupId);
                mIntent.putExtra(ChatActivity.TAG_CHAT_NAME, groupName);
                mActivity.startActivity(mIntent);
            }
        });

        getUserGroupData();
    }

    JsonArray mJsonGroupData = new JsonArray();

    class MyGroupAdapter extends SampleListAdapter {
        private final String NAME = "name";
        public final String GROUPID = "groupId";
        public final String PHOTO = "photo";
        
        public final String INTRODUCTION = "introduction";
        public final String ID = "id";

        public MyGroupAdapter(Context mContext, JsonArray mData) {
            super(mContext, mData);
        }

        @Override
        public View getView(int posi, View viewTemp, ViewGroup arg2) {
            if (viewTemp == null) {
                viewTemp = inflater.inflate(R.layout.child_item_layout, null);
            }
            JsonObject mJson = getItem(posi);
            ImageView mImageButton = ViewHolder.get(viewTemp, R.id.item_friend_icon);
            TextView mTvName = ViewHolder.get(viewTemp, R.id.tv_item_friend_name);
            TextView mTvIntro = ViewHolder.get(viewTemp, R.id.tv_item_friendslist_intro);
            
            String mGroupName = JsonUtil.getAsString(mJson, NAME);
            mTvName.setText(mGroupName);
            
            String mIconUrl = JsonUtil.getAsString(mJson, PHOTO);
            CpApplication.getApplication().mBitmapManager.disPlayImage(mImageButton, mIconUrl);
            
            String intro = JsonUtil.getAsString(mJson, INTRODUCTION);
            mTvIntro.setText(intro);
            
            final String mGroupId = JsonUtil.getAsString(mJson, GROUPID);
            
            mImageButton.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View arg0) {
                    Intent mIntent = new Intent(context,SampleHolderActivity.class);
                    mIntent.putExtra(SampleHolderActivity.TAG_ID,mGroupId);
                    mIntent.putExtra(SampleHolderActivity.TAG_TAG, SampleHolderActivity.TAG_GROUPINFO);
                    context.startActivity(mIntent);
                }
            });
//            TextView mTextView = ViewHolder.get(viewTemp,
//                    R.id.tv_item_groupname);
//            JsonObject mJson = getItem(posi);
//            String groupName = JsonUtil.getAsString(mJson, NAME);
//            mTextView.setText(groupName);

            return viewTemp;
        }

    }

    private void getUserGroupData() {
        showProgresssDialog();
        sendData(UserUpLoadJsonData.getGroupsForUserNameValuePair(),
                AppDefine.URL_USER_GETGROUPS, this, 101);

    }

    @Override
    public void onHttpError(Exception e, String msg, int requestCode) {
        dismissProgressDialog();
        mSwipListView.onLoadOver();
    }

    @Override
    public void onDecoded(String reason, boolean isSuccess,
            JsonObject mJsonResult, JsonArray mLists, int resultCode) {
        dismissProgressDialog();
        mSwipListView.onLoadOver();
        if(mJsonResult==null ||!mJsonResult.has("group_list"))return;
        JsonArray mJsonArry = JsonUtil
                .getAsJsonArray(mJsonResult, "group_list");

        mAdapter.changeDataSource(mJsonArry);

        if (mJsonArry.size() > 0) {
            for (int i = 0; i < mJsonArry.size(); i++) {
                decoceGroupInfo((JsonObject) mJsonArry.get(i));
            }
        }
    }

    DBUSER mDbUser = new DBUSER();

    private void decoceGroupInfo(JsonObject mJsonGroup) {

        // 保存到用户表
        User mUser = new User();
        mUser.nickname = JsonUtil.getAsString(mJsonGroup, "name");
        mUser.userid = JsonUtil.getAsString(mJsonGroup, "groupId");
        
        PreferencesUtils.putBoolean(mActivity, mUser.userid, true);
        mUser.photo =  JsonUtil.getAsString(mJsonGroup, "photo");
        boolean isSaveSuccess = mDbUser.saveData(mUser);
        
        if(!isSaveSuccess){
            try {
                mDbUser.getDBDbManager().getContentDb()
                        .update(mUser, WhereBuilder.b(User.USERID, "=", mUser.userid));
            } catch (DbException e) {
            }
        }
    }

}
