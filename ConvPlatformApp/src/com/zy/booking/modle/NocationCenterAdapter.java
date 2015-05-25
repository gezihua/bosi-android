
package com.zy.booking.modle;

import java.util.ArrayList;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dk.view.drop.CoverManager;
import com.dk.view.drop.WaterDrop;
import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.EmsgManager;
import com.zy.booking.R;
import com.zy.booking.activitys.SampleHolderActivity;
import com.zy.booking.db.ChatMsgBean;
import com.zy.booking.db.DBFRIEND;
import com.zy.booking.db.DBUSER;
import com.zy.booking.db.MSGHISTORY;
import com.zy.booking.db.User;
import com.zy.booking.json.DecodeResult;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.EmojiUtils;
import com.zy.booking.util.TimeUtils;
import com.zy.booking.util.ViewHolder;

public class NocationCenterAdapter extends SampleStructAdapter implements
        OnHttpActionListener {

    MSGHISTORY mMsgDb = new MSGHISTORY();

    private final String TV_NONICKNAME = "未命名";

    private ListView mListView;

    BaseActivity mBaseActivity;

    public NocationCenterAdapter(Context mContext,
            List<? extends Object> mList, ListView mListView) {
        super(mContext, mList);
        initWaterDrop();

        this.mListView = mListView;

        mBaseActivity = (BaseActivity) mContext;
    }

    private void initWaterDrop() {
        CoverManager.getInstance().init((Activity) context);
        CoverManager.getInstance().setMaxDragDistance(350);
        CoverManager.getInstance().setExplosionTime(150);
    }

    @Override
    public View getView(int posi, View viewTemp, ViewGroup arg2) {
        if (viewTemp == null) {
            viewTemp = LayoutInflater.from(context).inflate(
                    R.layout.item_layout_frends, null);
        }

        ChatMsgBean mChatBean = (ChatMsgBean) getItem(posi);

        // 当发送发是我的时候也就是说这一个分组 只有我给别人发的没有别人给我发的 这个时候就要置换一下位置
        changePosition(mChatBean);
       

        TextView mLastInfoDate = ViewHolder.get(viewTemp,
                R.id.tv_item_friend_last_infodate);

        if (isAddFriend(mChatBean)) {
            //如果是已经添加过了 则不显示
            mChatBean.updateChatMsgBean(mChatBean.msgFrom, mBaseActivity);
            showForAddFriends(mChatBean, viewTemp);
            showPtPChatData(viewTemp, mChatBean, posi);
        } else if (isGroupMsg(mChatBean)) {
            mChatBean.updateChatMsgBean(mChatBean.gid, mBaseActivity);
            showGroupChatData(viewTemp, mChatBean, posi);
        } else {
            mChatBean.updateChatMsgBean(mChatBean.msgFrom, mBaseActivity);
            showCommonMessage(viewTemp, mChatBean);
            showPtPChatData(viewTemp, mChatBean, posi);
        }
        mLastInfoDate.setText(TimeUtils
                .getDateWeekBylongtimemin(mChatBean.msgTime));

        return viewTemp;
    }

    /**
     * 显示点对点聊天的 通用性 组件 如未读条数,人员头像
     */
    private void showPtPChatData(View viewTemp, final ChatMsgBean mChatBean, int posi) {
        WaterDrop mWaterDrop = ViewHolder.get(viewTemp,
                R.id.tv_friends_info_dothot);
        ImageView mtvFriendicon = ViewHolder.get(viewTemp,
                R.id.item_friend_icon);
        String unReadMsgNum = mMsgDb.getUnReadMsgCount(mBaseActivity
                .getStringFormat(mBaseActivity
                        .getString(R.string.select_singleunreadcount),
                        mChatBean.msgFrom,mChatBean.column1));
        if (unReadMsgNum.equals("0") ||mChatBean.column1.equals(EmsgManager.TYPE_SENDLEAVEMESSAGE))
            mWaterDrop.setVisibility(View.GONE);
        else {
            mWaterDrop.setVisibility(View.VISIBLE);
            mWaterDrop.setText(unReadMsgNum);
        }

        if (!TextUtils.isEmpty(mChatBean.column0)
                && !mChatBean.column0.equals("null")) {
            mBitmapUtils.display(mtvFriendicon, mChatBean.column0, mConfig);
        }
        boolean isEmptyName = isEmpty(mChatBean.name)
                || mChatBean.name.equals(TV_NONICKNAME);
        boolean isNeedGetUserInfo = isEmptyName
                && !mChatBean.msgFrom.startsWith(AppDefine.ADMIN);

        if (isNeedGetUserInfo) {
            getUserInfo(mChatBean, posi);
        }
        
        mtvFriendicon.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                intentToUserInfo(mChatBean.msgFrom,SampleHolderActivity.TAG_SHOWUSERINFO);
            }
        });
    }

    /**
     * 显示群组聊天的相关信息
     */
    private void showGroupChatData(View viewTemp,final  ChatMsgBean mChatBean,
            int posi) {
        WaterDrop mWaterDrop = ViewHolder.get(viewTemp,
                R.id.tv_friends_info_dothot);
        ImageView mtvFriendicon = ViewHolder.get(viewTemp,
                R.id.item_friend_icon);
        String unReadMsgNum = mMsgDb.getUnReadMsgCount(mBaseActivity
                .getStringFormat(mBaseActivity
                        .getString(R.string.select_group_unreadcount),
                        mChatBean.gid));
        if (!unReadMsgNum.equals("0")) {
            mWaterDrop.setVisibility(View.VISIBLE);
            mWaterDrop.setText(unReadMsgNum);
        } else {
            mWaterDrop.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mChatBean.column0))
            CpApplication.getApplication().mBitmapManager.disPlayImage(mtvFriendicon,
                    mChatBean.column0);

        TextView mLastInfoInfo = ViewHolder.get(viewTemp,
                R.id.tv_item_friendslist_lastinfo);
        if (mChatBean.contentType.equals("image")) {
            mLastInfoInfo.setText("[图片]");
        } else if (mChatBean.contentType.equals("audio")) {
            mLastInfoInfo.setText("[语音]");
        } else {
            SpannableStringBuilder sb = EmojiUtils.handler(mLastInfoInfo,
                    mChatBean.msgContent, context);
            mLastInfoInfo.setText(sb);
        }

        TextView mtvFriendsName = ViewHolder.get(viewTemp,
                R.id.tv_item_friend_name);
        mtvFriendsName.setText(TextUtils.isEmpty(mChatBean.name) ? "群消息"
                : mChatBean.name);
        // 设置标签 用于 数据更新完成后的回调
        mtvFriendsName.setTag(mChatBean.gid);

        boolean isEmptyName = isEmpty(mChatBean.name)
                || mChatBean.name.equals(TV_NONICKNAME);
        boolean isNeedGetUserInfo = isEmptyName
                && !mChatBean.msgFrom.startsWith(AppDefine.ADMIN);

        if (isNeedGetUserInfo) {
            getGroupInfo(mChatBean, posi);
        }
        
        mtvFriendicon.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                intentToUserInfo(mChatBean.gid,SampleHolderActivity.TAG_GROUPINFO);
            }
        });
    }

    /**
     * 根据是否又gid 来判断当前聊天是否来自于群聊
     */
    public boolean isGroupMsg(ChatMsgBean mChatBean) {
        return !TextUtils.isEmpty(mChatBean.gid);
    }

    /**
     * 添加好友的消息
     */
    private void showForAddFriends(ChatMsgBean mChatBean, View viewTemp) {

        TextView mLastInfoInfo = ViewHolder.get(viewTemp,
                R.id.tv_item_friendslist_lastinfo);
        TextView mtvFriendsName = ViewHolder.get(viewTemp,
                R.id.tv_item_friend_name);
        mtvFriendsName.setText(mChatBean.column2);
        mLastInfoInfo.setText("想添加您为好友");
        mLastInfoInfo.setTextColor(Color.RED);
    }

    private void showCommonMessage(View viewTemp, ChatMsgBean mChatBean) {
        TextView mLastInfoInfo = ViewHolder.get(viewTemp,
                R.id.tv_item_friendslist_lastinfo);

        TextView mtvFriendsName = ViewHolder.get(viewTemp,
                R.id.tv_item_friend_name);

        mLastInfoInfo.setTextColor(Color.parseColor("#9A9A9A"));
        if (mChatBean.contentType.equals("image")) {
            mLastInfoInfo.setText("[图片]");
        } else if (mChatBean.contentType.equals("audio")) {
            mLastInfoInfo.setText("[语音]");
        } else {
            SpannableStringBuilder sb = EmojiUtils.handler(mLastInfoInfo,
                    mChatBean.msgContent, context);
            mLastInfoInfo.setText(sb);
        }

        // 设置标签 用于 数据更新完成后的回调
        mtvFriendsName.setTag(mChatBean.msgFrom);

        if (mChatBean.msgFrom.startsWith(AppDefine.ADMIN)) {
            mtvFriendsName.setText("系统消息");
            mLastInfoInfo.setText(DecodeResult
                    .decodeResultAdmin(mChatBean.msgContent));
        } else {
            mtvFriendsName.setText(isEmpty(mChatBean.name) ? TV_NONICKNAME
                    : mChatBean.name);
        }

    }

    private boolean isEmpty(String mData) {
        return TextUtils.isEmpty(mData) || mData.equals("null");
    }

    private void changePosition(ChatMsgBean mChatMsgBean) {
        if (!mChatMsgBean.msgFrom.startsWith(CpApplication.getApplication()
                .getUserId())) {
            return;
        }
        String temp = null;
        temp = mChatMsgBean.msgFrom;
        mChatMsgBean.msgFrom = mChatMsgBean.msgTo;
        mChatMsgBean.msgTo = temp;
        if (isEmpty(mChatMsgBean.name) || mChatMsgBean.name.equals("我"))
            mChatMsgBean.name = "未命名";
    }

    public void notifyRemoteSingleItem(ListView mListView, int position) {
        View mFatherView = mListView.getChildAt(position
                - mListView.getFirstVisiblePosition());

        View mView = mFatherView.findViewById(R.id.tv_friends_info_dothot);
        mView.setVisibility(View.GONE);
    }

    /**
     * 更新用户名称 和图片名称
     */
    public void notifyUserNameAndIcon(int position, String id, String iconurl,
            String name) throws Exception {
        ChatMsgBean mChatBean = (ChatMsgBean) getItem(position);
        mChatBean.column0 = iconurl;
        mChatBean.name = name;

        View mFatherView = mListView.getChildAt(position
                - mListView.getFirstVisiblePosition());
        TextView mViewName = (TextView) mFatherView
                .findViewById(R.id.tv_item_friend_name);

        ImageView mtvFriendicon = (ImageView) mFatherView
                .findViewById(R.id.item_friend_icon);

        if (!TextUtils.isEmpty(name)
                && mViewName.getTag().equals(id + EmsgManager.EMSGAREA)) {
            mViewName.setText(name);
            mBitmapUtils.display(mtvFriendicon, iconurl, mConfig);
        }
    }

    /**
     * 更新用户名称 和图片名称
     */
    public void notifyGroupNameAndIcon(int position, String mGroupid,
            String iconurl, String name) throws Exception {
        ChatMsgBean mChatBean = (ChatMsgBean) getItem(position);
        mChatBean.column0 = iconurl;
        mChatBean.name = name;

        View mFatherView = mListView.getChildAt(position
                - mListView.getFirstVisiblePosition());
        TextView mViewName = (TextView) mFatherView
                .findViewById(R.id.tv_item_friend_name);

        ImageView mtvFriendicon = (ImageView) mFatherView
                .findViewById(R.id.item_friend_icon);

        if (!TextUtils.isEmpty(name) && mViewName.getTag().equals(mGroupid)) {
            mViewName.setText(name);
            mBitmapUtils.display(mtvFriendicon, iconurl, mConfig);
        }
    }

    @Override
    public void onHttpError(Exception e, String msg, int requestCode) {

    }

    /**
     * 从网络上读取一遍当前用户的信息
     */
    private void getUserInfo(ChatMsgBean mChatMsgBean, int position) {
        String userId = mChatMsgBean.msgFrom.substring(0,
                mChatMsgBean.msgFrom.lastIndexOf("@"));
        ArrayList<NameValuePair> mList = new ArrayList<NameValuePair>();
        mList.add(new BasicNameValuePair("userId", userId));
        ((BaseActivity) context).sendData(mList, AppDefine.URL_GETUSERINFO,
                this, position);
    }

    private void getGroupInfo(ChatMsgBean mChatMsgBean, int position) {
        String gid = mChatMsgBean.gid;

        // getGroupNameValuePair
        ((BaseActivity) context).sendData(
                UserUpLoadJsonData.getGroupNameValuePair(gid),
                AppDefine.URL_GROUP_GETGROUP, this, position);
    }

    DBUSER mDbUser = new DBUSER();

    public boolean isAddFriend(ChatMsgBean c) {
        if (TextUtils.isEmpty(c.column1)) {
            return false;
        }
        if (c.column1.equals(EmsgManager.TYPE_ADDFRIENDS)) {
            return true;
        }
        return false;
    }

    @Override
    public void onDecoded(String reason, boolean isSuccess,
            JsonObject mJsonResult, JsonArray mLists, int resultCode) {

        if (isSuccess && mJsonResult != null) {
            if (mJsonResult.has("user")) {
                decodeUserInfo(mJsonResult, resultCode);
            } else {
                decoceGroupInfo(mJsonResult, resultCode);
            }
        }
    }

    /**
     * 基础的用户信息解析 并放到库里
     */
    private void decodeUserInfo(JsonObject mJsonResult, int resultCode) {

        JsonObject mJsonObj = JsonUtil.getAsJsonObject(mJsonResult, "user");
        String nickName = JsonUtil.getAsString(mJsonObj, "nickname");
        String iconUrl = JsonUtil.getAsString(mJsonObj, "photo");
        String userId = JsonUtil.getAsString(mJsonObj, "id");
        String phone = JsonUtil.getAsString(mJsonObj, "phone");
        // 更新表 若当前数据类型没变化 则更新界面
        BaseActivity mAcitivity = (BaseActivity) context;
        String sql = mAcitivity
                .getString(R.string.update_chathistory_single_userinfo);
        String sqlFormat = mAcitivity.getStringFormat(sql, nickName, iconUrl,
                userId + EmsgManager.EMSGAREA);
        // 保存到用户表
        User mUser = new User();
        mUser.nickname = nickName;
        mUser.userid = userId;
        mUser.photo = iconUrl;
        mUser.phone = phone;

        boolean isSaveSuccess = mDbUser.saveData(mUser);

        if (!isSaveSuccess) {

            try {
                mDbUser.getDBDbManager().getContentDb()
                        .update(mUser, WhereBuilder.b(User.USERID, "=", mUser.userid));
            } catch (DbException e) {
            }

        }
        mMsgDb.updateDataFromDb(sqlFormat);

        try {
            if (!isEmpty(nickName))
                notifyUserNameAndIcon(resultCode, userId, iconUrl, nickName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void intentToUserInfo(String id,String tag) {
        Intent mIntent = new Intent(context, SampleHolderActivity.class);
        mIntent.putExtra(SampleHolderActivity.TAG_ID, id);
        mIntent.putExtra(SampleHolderActivity.TAG_TAG,tag);
        context.startActivity(mIntent);
    }

    private void decoceGroupInfo(JsonObject mJsonResult, int resultCode) {
        JsonObject mJsonGroup = mJsonResult.getAsJsonObject("group");

        // 保存到用户表
        User mUser = new User();
        mUser.nickname = JsonUtil.getAsString(mJsonGroup, "name");
        mUser.userid = JsonUtil.getAsString(mJsonGroup, "id");
        mUser.photo = "";
        mDbUser.saveData(mUser);

        // 更新表 若当前数据类型没变化 则更新界面
        BaseActivity mAcitivity = (BaseActivity) context;
        String sql = mAcitivity
                .getString(R.string.update_chathistory_group_userinfo);
        String sqlFormat = mAcitivity.getStringFormat(sql, mUser.nickname,
                mUser.photo, mUser.userid);
        mMsgDb.updateDataFromDb(sqlFormat);

        try {
            if (!isEmpty(mUser.nickname))
                notifyGroupNameAndIcon(resultCode, mUser.userid, mUser.photo,
                        mUser.nickname);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
