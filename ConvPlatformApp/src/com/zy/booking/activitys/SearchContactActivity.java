
package com.zy.booking.activitys;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.emsg.sdk.EmsgCallBack;
import com.emsg.sdk.util.JsonUtil;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.EmsgManager;
import com.zy.booking.R;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.components.SwipListViewComponents.OnSwipCallBack;
import com.zy.booking.db.DBFRIEND;
import com.zy.booking.db.DBUSER;
import com.zy.booking.db.User;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.modle.FriendsListAdapter;
import com.zy.booking.modle.ModifyFriendAdapter;
import com.zy.booking.modle.ModifyFriendAdapter.OnRemoveFriendCallBack;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.PreferencesUtils;


import java.util.List;

@ContentView(R.layout.layout_common_withoutscroll)
public class SearchContactActivity extends BaseActivity implements
        OnHttpActionListener,OnRemoveFriendCallBack {

    @ViewInject(R.id.ll_container_body)
    LinearLayout mlayoutBody;

    SwipListViewComponents mSwipList;

    FriendsListAdapter mFriendsAdapter;

    Button mBtSearch;
    EditText mEditText;

    JsonArray mJsonArray = new JsonArray();

    String mCurrentKeyWords;

    private boolean isLoadMore = false;

    @ViewInject(R.id.headactionbar)
    View mViewHead;

    HeadLayoutComponents mHeadLayout;

    public static final String TAG_MODIFYCONTACT = "modify"; // 从本地的user表中查询
                                                             // 然后有删除的功能
    public static final String TAG_SEARCHANDJOIN = "searchJoin";

    private String tagSearch = "";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        View mView = View.inflate(this,  R.layout.layout_search_friends, null);
        mlayoutBody.addView(mView);
        
        tagSearch = getIntent().getStringExtra("tag");
        

        mBtSearch = (Button) findViewById(R.id.btn_search);
        mEditText = (EditText) findViewById(R.id.et_find_name);
        mBtSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String keyword = mEditText.getText().toString();
                if (TextUtils.isEmpty(keyword)) {
                    playYoYo(mEditText);
                    return;
                }
                isLoadMore = false;

                actionSearchByKeyWords(keyword);
            }
        });
        mSwipList = new SwipListViewComponents(mContext);
        mSwipList.isLoadBottomAuto(true);
        mSwipList.setSwipCallBack(new OnSwipCallBack() {

            @Override
            public void onReflesh() {
                mSwipList.onLoadOver();
            }

            @Override
            public void onLoadMore() {
                isLoadMore = true;
                actionSearchByKeyWords(mCurrentKeyWords);
            }

            @Override
            public void onItemClickListener(int position) {
                if(isModifyStatus()){
                    return;
                }
                JsonObject mJsonObj = mFriendsAdapter.getItem(position);
                String name = JsonUtil.getAsString(mJsonObj,
                        FriendsListAdapter.NICKNAME);
                String id = JsonUtil.getAsString(mJsonObj, "id");
                showDialog(id, name);
            }
        });
        mlayoutBody.addView(mSwipList.getView());
       

        mHeadLayout = new HeadLayoutComponents(mContext, mViewHead);

        mHeadLayout.setDefaultLeftCallBack(true);
        mHeadLayout.setTextMiddle("好友搜索", -1);
        
        if(isModifyStatus()){

            mFriendsAdapter = new ModifyFriendAdapter(this, mJsonArray);
            ((ModifyFriendAdapter) mFriendsAdapter).setRemoveFriendCallBack(this);
            mFriendsAdapter.setStatus(true);
        }else{
            mFriendsAdapter = new FriendsListAdapter(this, mJsonArray);
        }
        mSwipList.setAdapter(mFriendsAdapter);
    }

    
    private boolean isModifyStatus(){
        return tagSearch.equals(TAG_MODIFYCONTACT);
                
    }
    private final int CODE_REQUEST_SEARCHFRIEND = 101;

    int mCurrentBegin = 0;

    private void actionSearchByKeyWords(String keyWord) {
        if (!isModifyStatus()) {
            actionSearchFriendsFromCloud(keyWord);
        } else {
            actionSearchFriendFromLocal(keyWord);
        }
    }

    private void actionSearchFriendsFromCloud(String keywords) {
        // ambiguously
        mCurrentKeyWords = keywords;
        showProgresssDialog();

        // 用于添加好友
        sendData(UserUpLoadJsonData.getFrindsByKeyWordNameValuePair(keywords,
                mJsonArray.size(), 10), AppDefine.URL_USER_SEARCHFRIENDS, this,
                CODE_REQUEST_SEARCHFRIEND);
    }

    private void actionSearchFriendFromLocal(String keyword) {
        if (TextUtils.isEmpty(keyword))
            keyword = "";
        String mGroupId = PreferencesUtils.getString(mContext, AppDefine.KEY_MODELGROUPID);
        String keyWordSql = String.format(
                getResources().getString(R.string.select_userinfo_baselikenickname), "%" + keyword
                        + "%" ,CpApplication.getApplication().getUserId(),mGroupId);

        getUserFromLocalDb(keyWordSql);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onHttpError(Exception e, String msg, int requestCode) {
        dismissProgressDialog();
    }

    @Override
    public void onDecoded(String reason, boolean isSuccess,
            JsonObject mJsonResult, JsonArray mLists, int resultCode) {
        dismissProgressDialog();
        JsonArray mUserList = JsonUtil.getAsJsonArray(mJsonResult, "uesr_list");
        if (mUserList != null && mUserList.size() > 0) {
            if (isLoadMore) {
                this.mJsonArray.addAll(mUserList);
            } else {
                this.mJsonArray = mUserList;
            }
            mFriendsAdapter.changeDataSource(this.mJsonArray);
        }
    }

    //
    DBUSER mDbUser = new DBUSER();

    private void getUserFromLocalDb(String sql) {
        List<User> mUsers = mDbUser.selectDataFromDb(sql);
        Gson mGson = new Gson();
        JsonArray mJsonArray = mGson.toJsonTree(mUsers).getAsJsonArray();

        if (mJsonArray != null && mJsonArray.size() > 0) {

            this.mJsonArray = mJsonArray;
            mFriendsAdapter.changeDataSource(this.mJsonArray);
        }
    }

    // 根据不同版本执行的条件不同
    protected void showDialog(final String mUserId, String userName) {
        String mUid = PreferencesUtils.getString(mContext, AppDefine.KEY_USERID);
        if (mUserId.equals(mUid)) {
            showToastShort("不能加自己为好友！");
            return;
        }

        String dialogtitle = getResources().getString(
                R.string.dialog_default_title);
        String btn1Tv = getResources().getString(R.string.dialog_default_bt1);
        String btn2Tv = getResources().getString(R.string.dialog_default_bt2);
        String notifyMessage = "需要添加" + userName + "为好友?";

        final NiftyDialogBuilder mBuilder = NiftyDialogBuilder
                .getInstance(this).withButton1Text(btn1Tv)
                .withEffect(Effectstype.Shake).withButton2Text(btn2Tv)
                .withEffect(Effectstype.Shake).withTitle(dialogtitle)
                .withMessage(notifyMessage);

        mBuilder.setButton1Click(new OnClickListener() {

            @Override
            public void onClick(View paramView) {
                mBuilder.dismiss();
                // 发送添加好友的消息
                String nickName = PreferencesUtils.getString(mContext, "nickName");
                CpApplication.getApplication().mEmsgManager
                        .sendJoinFriendsMessage(mUserId + EmsgManager.EMSGAREA,
                                nickName, new EmsgCallBack() {

                                    @Override
                                    public void onSuccess() {
                                        showToastShort("验证信息发送成功");
                                    }

                                    @Override
                                    public void onError(TypeError mErrorType) {

                                    }
                                });
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
    DBFRIEND mDbfriend = new DBFRIEND();

    @Override
    public void removeFriendCallBack(String friendId) {
        showToastShort("移除粉丝成功");
        String sql = getResources().getString(R.string.delete_friend_baseuserid);
        String mSqlFormat = String.format(sql, friendId);
        mDbfriend.deleteDataFromDb(mSqlFormat);
        sendBroadcast(new Intent(AppDefine.ACTION_REMOVEFUNS));
        
        finish();
    
    }
    

}
