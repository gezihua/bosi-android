
package com.zy.booking.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zy.booking.AppDefine;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.activitys.ChatActivity;
import com.zy.booking.activitys.SearchContactActivity;
import com.zy.booking.components.CategorysGirdComponents;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.db.DBFRIEND;
import com.zy.booking.db.DBUSER;
import com.zy.booking.db.FriendTable;
import com.zy.booking.db.User;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.modle.UserIconAdapter;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.PreferencesUtils;

public class FunsGroupFragment extends BaseFragment implements
        OnHttpActionListener {
	
	
    @ViewInject(R.id.ll_container_body)
    LinearLayout mLayoutBody;

    View mViewGroupMember;

    private String mGroupId;

    private String modelId;

    public FunsGroupFragment(String modelId) {
        this.modelId = modelId;
    }

    MyGroupMember mGroupMemberManager;

    @ViewInject(R.id.headactionbar)
    View mViewHead;

    HeadLayoutComponents mHeadLayoutComponents;

    @Override
    protected View getBasedView() {
        return inflater.inflate(R.layout.fragment_layout_common, null);
    }

    @Override
    void afterViewInject() {
        mViewGroupMember = inflater.inflate(R.layout.layout_fungroup, null);
        mLayoutBody.addView(mViewGroupMember);
        mGroupMemberManager = new MyGroupMember();
        if (isShowHead) {
            mHeadLayoutComponents = new HeadLayoutComponents(mActivity, mViewHead);
            mHeadLayoutComponents.setDefaultLeftCallBack(true);
            mHeadLayoutComponents.setTextMiddle("群信息", -1);
            getGroupData();
        }
        mGroupMemberManager.showBtJudge();
        
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(AppDefine.ACTION_REMOVEFUNS);
        
        mActivity.registerReceiver(new MyRemoveFunBroadReciver(), mIntentFilter);

    }

    private boolean isShowHead;

    // 获取该模特的相关群组
    public void setGroupId(String mGroupId, boolean isShowHead) {
        this.mGroupId = mGroupId;
        this.isShowHead = isShowHead;
        if (mGroupMemberManager != null)
            mGroupMemberManager.showBtJudge();
        if (!isShowHead)
            getGroupData();
    }

    private final int GETGROUP = 101;

    private final int JOINGROUP = 102;

    private final int QUITGROUP = 103;
    
    
    class MyRemoveFunBroadReciver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(!mActivity.isFinishing())
            getGroupData();
        }
        
    }

    // 获取分组成员
    private void getGroupData() {
        showProgresssDialog();
        sendData(UserUpLoadJsonData.getGroupNameValuePair(mGroupId),
                AppDefine.URL_GROUP_GETGROUP, this, GETGROUP);
    }

    private void joinGroup() {
        showProgresssDialog();
        sendData(UserUpLoadJsonData.joinGroupNameValuePair(mGroupId),
                AppDefine.URL_USER_JOINGROUP, this, JOINGROUP);
    }

    private void quitGroup() {
        showProgresssDialog();
        sendData(UserUpLoadJsonData.exitGroupNameValuePairs(mGroupId),
                AppDefine.URL_USER_QUITGROUP, this, QUITGROUP);
    }

    class MyGroupMember {
        @ViewInject(R.id.bt_action_joingroup)
        View mViewJoinGroup;
        @ViewInject(R.id.ll_group_members)
        LinearLayout mLayoutMembers;
        UserIconAdapter mUserAdapter;
        JsonArray mJsonArray;

        @ViewInject(R.id.tv_id_groupname)
        TextView mTvGroupName;
        @ViewInject(R.id.tv_groupintro)
        TextView mTvGroupIntro;

        @ViewInject(R.id.bt_action_joingroup)
        private View mViewJoin;
        @ViewInject(R.id.bt_action_quitgroup)
        private Button mViewExit;
        @ViewInject(R.id.iv_group_img)
        private ImageView mImageIcon;

        @ViewInject(R.id.bt_action_entergroup)
        private View mViewEnterGroup;

        public MyGroupMember() {
            ViewUtils.inject(this, mViewGroupMember);
            mGridView = new CategorysGirdComponents(null, mActivity);
            mJsonArray = new JsonArray();

            mUserAdapter = new UserIconAdapter(mActivity, mJsonArray);
            mGridView.setAdapter(mUserAdapter);
            mGridView.setVerNumber(6);
            mLayoutMembers.addView(mGridView.getView());
        }

        @OnClick(R.id.bt_action_joingroup)
        public void actionJoinGroup(View mView) {
            joinGroup();
        }

        @OnClick(R.id.bt_action_entergroup)
        public void actionEnterGroup(View mView) {
            Intent mIntent = new Intent(mActivity, ChatActivity.class);
            mIntent.putExtra(ChatActivity.TAG_ISCHATGROUP, true);
            mIntent.putExtra(ChatActivity.TAG_MESSAGE_TO, mGroupId);
            mIntent.putExtra(ChatActivity.TAG_CHAT_NAME, mGroupMemberManager.mTvGroupName.getText()
                    .toString());
            mActivity.startActivity(mIntent);
        }

        @OnClick(R.id.bt_action_quitgroup)
        private void actionExitGroup(View mView) {
            
            if(mViewExit.getText().equals("管理群组")){
                Intent mIntent = new Intent(mActivity,SearchContactActivity.class);
                mIntent.putExtra("tag", SearchContactActivity.TAG_MODIFYCONTACT);
                mActivity.startActivity(mIntent);
            }else
            quitGroup();
        }

        public void setJsonArray(JsonArray mJsonArray) {
            this.mJsonArray = mJsonArray;
            mUserAdapter.changeDataSource(mJsonArray);

            if (mJsonArray.size() > 0) {
                String mGroupIdMe = PreferencesUtils.getString(mActivity,
                        AppDefine.KEY_MODELGROUPID);
                if (TextUtils.isEmpty(mGroupIdMe) || !mGroupIdMe.equals(mGroupId)) {
                    return;
                }
                for (int i = 0; i < mJsonArray.size(); i++) {
                    JsonObject mJson = mUserAdapter.getItem(i);

                    String userId = JsonUtil.getAsString(mJson, UserIconAdapter.USERID);
                    String phone = JsonUtil.getAsString(mJson, UserIconAdapter.PHONE);
                    String sex = JsonUtil.getAsString(mJson, UserIconAdapter.SEX);
                    String nickName = JsonUtil.getAsString(mJson, UserIconAdapter.NICKNAME);
                    String photo = JsonUtil.getAsString(mJson, UserIconAdapter.PHOTO);
                    User mUser = new User();
                    mUser.nickname = nickName;
                    mUser.photo = photo;
                    mUser.phone = phone;
                    mUser.clumn0 = sex;
                    mUser.userid = userId;
                    mDbUser.insertOrUpdate(mUser, WhereBuilder.b(User.USERID, "=", mUser.userid));
                    
                    
                    FriendTable mFriendTable = new FriendTable();
                    mFriendTable.groupId = mGroupId;
                    mFriendTable.userId =mUser.userid ;
                    mDbfriend.insertOrUpdate(mFriendTable,  WhereBuilder.b(FriendTable.USERID, "=", mFriendTable.userId));
                }
            }
        }

        DBFRIEND mDbfriend = new DBFRIEND();
        public void showBtJudge() {
            boolean isMyGroup = PreferencesUtils.getBoolean(mActivity, mGroupId, false);
            String mGroupId = PreferencesUtils.getString(mActivity, AppDefine.KEY_USERID);
            if (!TextUtils.isEmpty(mGroupMasterId) && !TextUtils.isEmpty(mGroupId)
                    && mGroupId.equals(mGroupMasterId)) {
                mViewExit.setText("管理群组");
                mViewExit.setVisibility(View.VISIBLE);
                mViewEnterGroup.setEnabled(true);
                mViewJoin.setVisibility(View.GONE);
            }
            else if (isMyGroup) {
                mViewExit.setVisibility(View.VISIBLE);
                mViewJoin.setVisibility(View.GONE);
                mViewEnterGroup.setEnabled(true);
            } else {
                mViewExit.setVisibility(View.GONE);
                mViewJoin.setVisibility(View.VISIBLE);
                mViewEnterGroup.setEnabled(false);
            }
        }

        CategorysGirdComponents mGridView;

    }

    @Override
    public void onHttpError(Exception e, String msg, int requestCode) {
        dismissProgressDialog();
    }

    DBUSER mDbUser = new DBUSER();


    String mGroupMasterId = "";

    
    private void masterParse(JsonObject mJsonMaster) {
        if (mJsonMaster == null)
            return;

        mGroupMasterId = JsonUtil.getAsString(mJsonMaster, "id");
        
        mGroupMemberManager.showBtJudge();
    }
    
    private final String MODEL_SHOP_URL = "http://wd.koudai.com/item.html?itemID=1203998093";
    
    class MyTextSpan extends ClickableSpan{
    	String text;
    	
    	public MyTextSpan(String text){
    		this.text = text;
    	}
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();        
			intent.setAction("android.intent.action.VIEW");    
			Uri content_url = Uri.parse(text);   
			intent.setData(content_url);  
			startActivity(intent);
		}
			
		@Override
		public void updateDrawState(TextPaint ds) {
		ds.setColor(ds.linkColor); //设置链接的文本颜色
		ds.setUnderlineText(false); //去掉下划线
		}
    }
    @Override
    public void onDecoded(String reason, boolean isSuccess,
            JsonObject mJsonResult, JsonArray mLists, int resultCode) {
        dismissProgressDialog();
        if (!isSuccess)
            return;
        // "name":"猪乐的群","introduction":null
        if (resultCode == 101) {
            JsonObject mJsonGroup = JsonUtil.getAsJsonObject(mJsonResult, "group");
            String mGroupName = JsonUtil.getAsString(mJsonGroup, "name");
            String mIntro = JsonUtil.getAsString(mJsonGroup, "introduction");
            String photo = JsonUtil.getAsString(mJsonGroup, "photo");
            if (!TextUtils.isEmpty(photo)) {
                CpApplication.getApplication().mBitmapManager.disPlayImage(
                        mGroupMemberManager.mImageIcon, photo);
            }

            // 群组的所有者
            JsonObject mJsonMaster = JsonUtil.getAsJsonObject(mJsonGroup, "master");
            masterParse(mJsonMaster);

            User mUser = new User();
            mUser.nickname = mGroupName;
            mUser.photo = photo;
            mUser.userid = mGroupId;
            mDbUser.insertOrUpdate(mUser, WhereBuilder.b(User.USERID, "=", mUser.userid));
            
            mGroupMemberManager.mTvGroupName.setText(mGroupName);
            SpannableString spStr = new SpannableString(MODEL_SHOP_URL);
            MyTextSpan myTextSpan = new MyTextSpan(MODEL_SHOP_URL);
            spStr.setSpan(myTextSpan, 0, spStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            mGroupMemberManager.mTvGroupIntro.append(spStr);
            mGroupMemberManager.mTvGroupIntro.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接
          
            if (mJsonGroup != null) {
                JsonArray mJsonMemberList = JsonUtil.getAsJsonArray(mJsonGroup, "memberList");
                mGroupMemberManager.setJsonArray(mJsonMemberList);
            }
        }
        if (resultCode == 102) {
            PreferencesUtils.putBoolean(mActivity, mGroupId, true);
            mActivity.showToastShort("加群成功");
            getGroupData();
            mGroupMemberManager.showBtJudge();
        }

        if (resultCode == QUITGROUP) {
            PreferencesUtils.putBoolean(mActivity, mGroupId, false);
            mActivity.showToastShort("退群成功");
            getGroupData();
            mGroupMemberManager.showBtJudge();
        }
    }

}
