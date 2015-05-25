
package com.zy.booking.fragments;

import android.content.Intent;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emsg.sdk.EmsgCallBack;
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
import com.zy.booking.EmsgManager;
import com.zy.booking.R;
import com.zy.booking.activitys.ChatActivity;
import com.zy.booking.db.DBFRIEND;
import com.zy.booking.db.DBUSER;
import com.zy.booking.db.User;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.PreferencesUtils;

import java.util.List;

public class UserInfoFragment extends BaseFragment implements OnHttpActionListener {

    UserBodyDital mUserBodyDital;

    @Override
    protected View getBasedView() {
        return inflater.inflate(R.layout.fragment_layout_common, null);
    }

    JsonObject mJsonObj; // 包含了所有的模特的信息

    public void setData(JsonObject mJsonObj) {
        this.mJsonObj = mJsonObj;
        mUserBodyDital.updatePanel(false);
    }

    class UserBodyDital {
        public View mViewBody;

        @ViewInject(R.id.et_modelinfo_sex)
        private EditText mEtSex;

        @ViewInject(R.id.et_modelinfo_phone)
        private EditText mEtPhone;

        @ViewInject(R.id.tv_model_name)
        private TextView mTvName;

        @ViewInject(R.id.model_userphoto)
        private ImageView mImageIcon;

        @ViewInject(R.id.bt_addforfriends)
        private View mViewAddForFriends;

        @ViewInject(R.id.bt_enterchat)
        private View mViewEnterToChat;

        @ViewInject(R.id.bt_removefriends)
        private View mViewRemoveFriends;

        @OnClick(R.id.bt_addforfriends)
        public void actionAddFriend(View mView) {
            // 发送添加好友的消息
            String nickName = PreferencesUtils.getString(mActivity, "nickName");
            CpApplication.getApplication().mEmsgManager
                    .sendJoinFriendsMessage(mUserId + EmsgManager.EMSGAREA,
                            nickName, new EmsgCallBack() {

                                @Override
                                public void onSuccess() {
                                    mActivity.showToastShort("验证信息发送成功");
                                }

                                @Override
                                public void onError(TypeError mErrorType) {

                                }
                            });
        }

        @OnClick(R.id.bt_removefriends)
        public void actionRemoveFriends(View mView) {
            deleteUserInterface();
        }

        @OnClick(R.id.bt_enterchat)
        public void actionEnterChat(View mView) {
            Intent mIntent = new Intent(mActivity, ChatActivity.class);
            mIntent.putExtra(ChatActivity.TAG_MESSAGE_TO, mUserId);
            mIntent.putExtra(ChatActivity.TAG_CHAT_NAME, mTvName.getText().toString());
            mActivity.startActivity(mIntent);
        }

        public UserBodyDital() {
            mViewBody = LayoutInflater.from(mActivity).inflate(
                    R.layout.layout_persiondital_foruser, null);
            ViewUtils.inject(this, mViewBody);

            if (isSameUser()) {
                mViewAddForFriends.setVisibility(View.GONE);
                mViewEnterToChat.setVisibility(View.GONE);
                mViewRemoveFriends.setVisibility(View.GONE);
            }
        }

        // //{"id":"5b5db57ff9ae47fb9f1280bcdd93e82f",
        // "name":"猪乐","photo":"http://202.85.221.165:8080/app-img/5b5db57ff9ae47fb9f1280bcdd93e82f/2ed59aab71cc4dde883f4be365623dd6.jpeg",
        // "introduction":"我的青春我做主","groupId":"7512616f263a4b3986bc9223a1f15586","tags":"woman","albumList":[]}
        public void updatePanel(boolean isRemoveFriend) {
            // {"id":"09d3c60c3dc74bf3af3ca4780de84d4a","username":"18310665041","password":""
            // ,"nickname":"精致的小妹妹","photo":"http://202.85.221.165:8080/app-img/09d3c60c3dc74bf3af3ca4780de84d4a/c7bc07d531fd4aa7b5a82be088863569.jpeg",
            // "sex":"0","phone":"18310665041","sp":"true"
            if (mJsonObj == null)
                return;
            String mUserName = JsonUtil.getAsString(mJsonObj, "nickname");
            String phone = JsonUtil.getAsString(mJsonObj, "phone");
            String photo = JsonUtil.getAsString(mJsonObj, "photo");
            String sex = JsonUtil.getAsString(mJsonObj, "sex");
            String id = JsonUtil.getAsString(mJsonObj, "id");
            mTvName.setText(mUserName);
            mEtPhone.setText(phone);
            mEtSex.setText(TextUtils.isEmpty(sex) || sex.equals("0") ? "男" : "女");

            CpApplication.getApplication().mBitmapManager.disPlayImage(mImageIcon, photo);

            String mLoginUid = PreferencesUtils.getString(mActivity, AppDefine.KEY_USERID);

            if (mLoginUid.equals(mUserId)) {
                mViewEnterToChat.setVisibility(View.GONE);
                mViewAddForFriends.setVisibility(View.GONE);
                mViewRemoveFriends.setVisibility(View.GONE);
            } else {
                String mSqlSelectFrineds = mActivity
                        .getResourceFromId(R.string.select_friend_baseuserid);
                List<?> mLists = mDbFrined.selectDataFromDb(String.format(mSqlSelectFrineds,
                        mUserId));
                if (mLists != null && mLists.size() > 0) {
                    mViewEnterToChat.setVisibility(View.VISIBLE);
                    mViewAddForFriends.setVisibility(View.GONE);
                    mViewRemoveFriends.setVisibility(View.VISIBLE);
                } else {
                    mViewEnterToChat.setVisibility(View.GONE);
                    mViewAddForFriends.setVisibility(View.VISIBLE);
                    mViewRemoveFriends.setVisibility(View.GONE);
                }
            }

            if (isRemoveFriend) {
                return;
            }

            User mUser = new User();
            mUser.nickname = mUserName;
            mUser.userid = id;
            mUser.phone = phone;
            mUser.photo = photo;
            boolean isInsertSuccess = mDbUser.saveData(mUser);

            if (!isInsertSuccess) {
                try {
                    mDbUser.getDBDbManager().getContentDb()
                            .update(mUser, WhereBuilder.b(User.USERID, "=", id));
                } catch (DbException e) {
                }
            }
        }

    }

    DBUSER mDbUser = new DBUSER();

    private boolean isSameUser() {
        String myUid = PreferencesUtils.getString(mActivity, AppDefine.KEY_USERID);
        if (TextUtils.isEmpty(mUserId)) {
            return true;
        }
        return mUserId.equals(myUid);
    }

    private final int TAG_REMOFRIENDS = 102;
    private final int TAG_GETUSERDATA = 101;

    String mUserId;

    DBFRIEND mDbFrined = new DBFRIEND();

    public UserInfoFragment(String userId) {
        if (userId.contains("@")) {
            userId = userId.split("@")[0];
        }
        this.mUserId = userId;
    }

    @ViewInject(R.id.ll_container_body)
    LinearLayout mLayoutBody;

    @Override
    void afterViewInject() {
        mUserBodyDital = new UserBodyDital();
        mLayoutBody.addView(mUserBodyDital.mViewBody);

        getUserInfoData();
    }

    /**
     * 获取用户信息
     */
    private void getUserInfoData() {
        if (TextUtils.isEmpty(mUserId))
            return;
        mActivity.sendData(UserUpLoadJsonData.getUserInfoNameValuePairs(mUserId),
                AppDefine.URL_USER_GETUSERINFO, this, TAG_GETUSERDATA);
    }

    /**
     * 删除好友接口
     */

    private void deleteUserInterface() {
        if (TextUtils.isEmpty(mUserId))
            return;
        showProgresssDialog();
        mActivity.sendData(UserUpLoadJsonData.removeFrinedValuePairs(mUserId),
                AppDefine.URL_USER_REMOVEUSER, this, TAG_REMOFRIENDS);
    }

    @Override
    public void onHttpError(Exception e, String msg, int requestCode) {
        dismissProgressDialog();
    }

    @Override
    public void onDecoded(String reason, boolean isSuccess, JsonObject mJsonResult,
            JsonArray mLists, int resultCode) {
        dismissProgressDialog();
        if (mJsonResult != null && resultCode == TAG_GETUSERDATA) {
            mJsonObj = mJsonResult.getAsJsonObject("user");
            mUserBodyDital.updatePanel(false);
        }
        else if (isSuccess && resultCode == TAG_REMOFRIENDS) {
            mDbFrined.deleteDataFromDb(String.format(getString(R.string.delete_friend_baseuserid),
                    mUserId));
            mUserBodyDital.updatePanel(true);
        }
    }

}
