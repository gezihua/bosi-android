
package com.zy.booking.fragments;

import java.util.ArrayList;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.AppDefine;
import com.zy.booking.CpApplication;
import com.zy.booking.EmsgManager;
import com.zy.booking.R;
import com.zy.booking.activitys.ChatActivity;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.components.SwipListViewComponents.OnSwipCallBack;
import com.zy.booking.db.ChatMsgBean;
import com.zy.booking.db.DBFRIEND;
import com.zy.booking.db.MSGHISTORY;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.modle.NocationCenterAdapter;
import com.zy.booking.struct.OnHttpActionListener;

public class NotifyCenterFragment extends BaseFragment implements
        OnHttpActionListener, OnSwipCallBack {

   
    
    SwipListViewComponents mSwipListViewComponents;

    HeadLayoutComponents mHeadLayout;
    
    @ViewInject(R.id.ll_body)
    LinearLayout mLayoutBody;
    
    @ViewInject(R.id.headactionbar)
    View nHeadView;

    @ViewInject(R.id.tv_remote_nounreadmsg)
    TextView mTextViewRemote;
    NocationCenterAdapter mAdapter;

   

    List<ChatMsgBean> mList = new ArrayList<ChatMsgBean>();

    @Override
    protected View getBasedView() {
        return inflater.inflate(R.layout.fragment_layout_notifycenter, null);
    }

    @Override
    protected void afterViewInject() {
        mSwipListViewComponents = new SwipListViewComponents(getActivity()
                );
        mLayoutBody.addView(mSwipListViewComponents.getView());
        modifyHeadActionBar();

        mAdapter = new NocationCenterAdapter(getActivity(), mList,
                mSwipListViewComponents.getListView());
        mSwipListViewComponents.setAdapter(mAdapter);

        mSwipListViewComponents.setSwipCallBack(this);

        registerEmsgMessageReciver();
    }
    
   protected void modifyHeadActionBar(){
       mHeadLayout = new HeadLayoutComponents(getActivity(), nHeadView);
       mHeadLayout.setTextMiddle("消息中心", -1);
   }

    /**
     * 注册list监控事件
     */
    protected void onItemClickCallBack(int position) {
        ChatMsgBean mChatBean = mList.get(position);
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        String msgFrom = mList.get(position).msgFrom;
        // 如果是加好友 点击以后 直接清空已读 并弹出对话框
        if (mAdapter.isAddFriend(mChatBean)) {

            DBFRIEND mDbFrined = new DBFRIEND();
            String mSqlSelectFrineds = mActivity.
                    getString(R.string.select_friend_baseuserid);
            List<?> mLists = mDbFrined.selectDataFromDb(String.format(mSqlSelectFrineds,
                    mChatBean.msgFrom.replaceAll(EmsgManager.EMSGAREA, "")));

            if (mLists != null && mLists.size() > 0) {
                // 已经添加过好友了
                mActivity.showToastShort("该好友已添加");
                return;
            }
            updateIsReadState(msgFrom);
            showAddFriendsNotify(mChatBean.column2, mChatBean.msgFrom
                    .replaceAll(EmsgManager.EMSGAREA, ""));
            mAdapter.changeDataSource(mList);
            return;
        }

        if (msgFrom
                .contains(CpApplication.getApplication().getUserId())) {
            msgFrom = mChatBean.msgTo;
        }

        if (mAdapter.isGroupMsg(mChatBean)) {
            msgFrom = mChatBean.gid;
            intent.putExtra(ChatActivity.TAG_ISCHATGROUP, true);
        }
        intent.putExtra(ChatActivity.TAG_CHAT_NAME, mChatBean.name);
        intent.putExtra(ChatActivity.TAG_MESSAGE_TO, msgFrom);
        intent.putExtra(ChatActivity.TAG_FROM_SERVER,
                msgFrom.startsWith(AppDefine.ADMIN));
        getActivity().startActivity(intent);
        mAdapter.notifyRemoteSingleItem(
                mSwipListViewComponents.getListView(), position);
    }

    MSGHISTORY mDbMsgHistory = new MSGHISTORY();

    // 查询的是所有单对单发布信息的归类
    protected void selectFromLocalDb() {

        List<ChatMsgBean> mChatOrigin = new ArrayList<ChatMsgBean>();

        List<ChatMsgBean> pointChatData = selectSingleChat();
        List<ChatMsgBean> groupChatData = selectGroupDataFromLocalDb();

        mChatOrigin.addAll(pointChatData);
        mChatOrigin.addAll(groupChatData);
        if (mChatOrigin != null && mChatOrigin.size() > 0) {

            this.mList = mChatOrigin;
            mAdapter.changeDataSource(mList);
            mTextViewRemote.setVisibility(View.GONE);
        }
        mSwipListViewComponents.onLoadOver();
    }
    

    private List<ChatMsgBean> selectSingleChat() {

        String myEmgsAccount = CpApplication.getApplication().getUserId()
                + EmsgManager.EMSGAREA;

        String sqlSendByMe = mActivity.getStringFormat(
                getString(R.string.select_msghistorysendbymelist),
                myEmgsAccount);
        String sqlSendByOther = mActivity.getStringFormat(
                getString(R.string.select_msghistorysendbyotherlist),
                myEmgsAccount);

        // 查询我发出的信息 根据发送给的人分组的集合
        List<ChatMsgBean> mlocalSendByMyself = mDbMsgHistory
                .selectDataFromDb(sqlSendByMe);

        // 查询发信息给我的人的分组信息
        List<ChatMsgBean> mlocalDataSendByOther = mDbMsgHistory
                .selectDataFromDb(sqlSendByOther);

        List<ChatMsgBean> mTempListData = new ArrayList<ChatMsgBean>();

        // 过滤掉已经发送给我的人和发送信息给我的人 两个分组的重合
        if (mlocalSendByMyself.size() == 0) {
            mTempListData = mlocalDataSendByOther;
        } else if (mlocalDataSendByOther.size() == 0) {
            mTempListData = mlocalSendByMyself;
        } else {
            for (ChatMsgBean mChatBean : mlocalSendByMyself) {
                String msgTo = mChatBean.msgTo;
                boolean isMsgFromTimeBigThanMsgTo = false;
                boolean isSameMsg = false;
                ChatMsgBean mChatBeanOtherTemp = null;
                for (ChatMsgBean mChatBeanOther : mlocalDataSendByOther) {

                    if (msgTo.equals(mChatBeanOther.msgFrom)) {
                        isSameMsg = true;
                        if (mChatBeanOther.msgTime < mChatBean.msgTime) {
                            isMsgFromTimeBigThanMsgTo = true;
                            mChatBeanOtherTemp = mChatBeanOther;
                        }
                        break;
                    }
                }
                if (isSameMsg) {
                    if (isMsgFromTimeBigThanMsgTo) {
                        mlocalDataSendByOther.remove(mChatBeanOtherTemp);
                        mTempListData.add(mChatBean);
                    }
                } else {
                    mTempListData.add(mChatBean);
                }

            }
            // 把过滤掉的所有发送给我的信息 添加到集合
            mTempListData.addAll(mlocalDataSendByOther);
            mlocalDataSendByOther.clear();

        }
        return mTempListData;

    }

    private List<ChatMsgBean> selectGroupDataFromLocalDb() {
        String mSelectGroupSql = mActivity.getString(R.string.select_chatgroup);
        // 查询发信息给我的人的分组信息
        List<ChatMsgBean> mlocalDataGroup = mDbMsgHistory
                .selectDataFromDb(mSelectGroupSql);
        return mlocalDataGroup;
    }

    BroadcastReceiver mBrodCastNewsReciver;

    protected void registerEmsgMessageReciver() {

        IntentFilter mIntentFiter = new IntentFilter();

        mIntentFiter.addAction(EmsgManager.ACTION_NEWMSGCOMING);

        mBrodCastNewsReciver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context mContext, Intent mIntent) {
                selectFromLocalDb();
            }

        };
        // 对应的上下文对象
        getActivity().registerReceiver(mBrodCastNewsReciver, mIntentFiter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBrodCastNewsReciver);
    }

    private void showAddFriendsNotify(String userName, final String userId) {

        String dialogtitle = getResources().getString(
                R.string.dialog_default_title);
        String btn1Tv = getResources().getString(R.string.dialog_default_bt1);
        String btn2Tv = getResources().getString(R.string.dialog_default_bt2);
        String notifyMessage = "确认添加" + userName + "为好友?";

        final NiftyDialogBuilder mBuilder = NiftyDialogBuilder
                .getInstance(getActivity()).withButton1Text(btn1Tv)
                .withEffect(Effectstype.Shake).withButton2Text(btn2Tv)
                .withEffect(Effectstype.Shake).withTitle(dialogtitle)
                .withMessage(notifyMessage);

        mBuilder.setButton1Click(new OnClickListener() {

            @Override
            public void onClick(View paramView) {
                mBuilder.dismiss();
                sendAddFriendMsg(userId);
            }
        });

        mBuilder.setButton2Click(new OnClickListener() {

            @Override
            public void onClick(View paramView) {
                mBuilder.dismiss();
            }
        });
        try {
            mBuilder.show();
        } catch (Exception e) {
        }

    }

    private void sendAddFriendMsg(String friendsId) {
        showProgresssDialog();
        sendData(UserUpLoadJsonData.addFriendsForUserNameValuePair(friendsId),
                AppDefine.URL_USER_ADDFRIEND, this, 101);
    }

    @Override
    public void onResume() {
        super.onResume();
        selectFromLocalDb();
    }

    @Override
    public void onHttpError(Exception e, String msg, int requestCode) {
        dismissProgressDialog();
    }

    @Override
    public void onDecoded(String reason, boolean isSuccess,
            JsonObject mJsonResult, JsonArray mLists, int resultCode) {
        dismissProgressDialog();
        // isSuccess 如果添加成功则 在我的好友的列表中显示相关信息
        if (isSuccess && resultCode == 101) {
            mActivity.showToastShort("添加好友成功");
            mActivity.sendBroadcast(new Intent(EmsgManager.ACTION_ADDFRIENDCALLBACK));
        }
    }

    private void updateIsReadState(String msgFrom) {
        String sql = getString(R.string.update_isread_bymsgfrom);
        String updateSqlFormat = String.format(sql, msgFrom, EmsgManager.TYPE_ADDFRIENDS);
        mDbMsgHistory.updateDataFromDb(updateSqlFormat);
    }

    @Override
    public void onReflesh() {
        selectFromLocalDb();
    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public void onItemClickListener(int position) {
        onItemClickCallBack(position);
    }

}
