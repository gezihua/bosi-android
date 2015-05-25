package com.zy.booking.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.CpApplication;
import com.zy.booking.EmsgManager;
import com.zy.booking.R;
import com.zy.booking.activitys.ChatActivity;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.db.ChatMsgBean;
import com.zy.booking.modle.NocationCenterAdapter;
import com.zy.booking.util.FriendSystemUtils;

import java.util.List;

public class MySendLeaMsgFragment extends NotifyCenterFragment{

    @ViewInject(R.id.ll_body)
    LinearLayout mLayoutBody;
    
    @ViewInject(R.id.headactionbar)
    View nHeadView;

    @ViewInject(R.id.tv_remote_nounreadmsg)
    TextView mTextViewRemote;

    @Override
    protected void modifyHeadActionBar() {
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
    @Override
    protected void onItemClickCallBack(int position) {


        ChatMsgBean mChatBean = mList.get(position);
        if(!FriendSystemUtils.isMyFriend(mChatBean.msgTo, mActivity)){
            mActivity.showToastShort("请先添加"+mChatBean.name+"为好友");
            return;
        }
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        String msgFrom = mList.get(position).msgFrom;
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
        getActivity().startActivity(intent);
        mAdapter.notifyRemoteSingleItem(
                mSwipListViewComponents.getListView(), position);
    
    
        
    }

    @Override
    protected void selectFromLocalDb() {
        
        String myEmgsAccount = CpApplication.getApplication().getUserId()
                + EmsgManager.EMSGAREA;

        String sqlSendByMe = mActivity.getStringFormat(
                getString(R.string.select_leavmsgsendbymelist),
                myEmgsAccount);
        // 查询我发出的信息 根据发送给的人分组的集合
        List<ChatMsgBean> mlocalSendByMyself = mDbMsgHistory
                .selectDataFromDb(sqlSendByMe);
        
        if (mlocalSendByMyself != null && mlocalSendByMyself.size() > 0) {
            this.mList = mlocalSendByMyself;
            mAdapter.changeDataSource(mList);
            mTextViewRemote.setVisibility(View.GONE);
        }
        mSwipListViewComponents.onLoadOver();
    }
    
    

}
