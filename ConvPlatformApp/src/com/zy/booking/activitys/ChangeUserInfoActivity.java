
package com.zy.booking.activitys;

import java.io.File;


import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.components.SelectImageGirdComponents;
import com.zy.booking.components.SelectImageGirdComponents.OnPicSelectedListener;
import com.zy.booking.db.DBUSER;
import com.zy.booking.db.User;
import com.zy.booking.json.DecodeResult;
import com.zy.booking.json.IDecodeJson;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.ModelPlatUtils;
import com.zy.booking.util.PreferencesUtils;

@ContentView(R.layout.activity_changeuserinfo)
public class ChangeUserInfoActivity extends BaseActivity implements OnHttpActionListener {

    @ViewInject(R.id.iv_select_icon)
    private ImageView mIvChoiceIcon;

    @ViewInject(R.id.et_personal_nickname)
    private EditText mEditNickName;

    @ViewInject(R.id.et_personal_phone)
    private EditText mEditPhoneNum;

    @ViewInject(R.id.rb_personal_male)
    private RadioButton mTvSexMan;

    @ViewInject(R.id.rb_personal_female)
    private RadioButton mTvSexFeman;

    @ViewInject(R.id.rg_userinfo_sex)
    private RadioGroup mRadioGroupSex ; 
    
    
    @ViewInject(R.id.headactionbar)
    View mViewHeadbar;

    HeadLayoutComponents mHeadLayoutComponents;

    private ArrayList<File> mListFileDatas;

    SelectImageGirdComponents mSelectImageGirdView;

    public static final String TAG_FROM = "FROM";

    @ViewInject(R.id.ll_userchangebody)
    ViewGroup mLayoutBody;

    private String mTagSex = "0";
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mListFileDatas = new ArrayList<File>();
        mSelectImageGirdView = new SelectImageGirdComponents(mContext, mLayoutBody);
        mSelectImageGirdView.setOnPicSelectedListener(new OnPicSelectedListener() {

            @Override
            public void onPicSelectedListener(String mFilePath) {
                if (!TextUtils.isEmpty(mFilePath)) {
                    mIconUrl = mFilePath;
                    mListFileDatas.add(new File(mIconUrl));
                    CpApplication.getApplication().mBitmapManager.disPlayImage(mIvChoiceIcon,
                            mFilePath);
                }
            }
        });

        mHeadLayoutComponents = new HeadLayoutComponents(this, mViewHeadbar);
        mHeadLayoutComponents.setTextRight("更改", -1);
        mHeadLayoutComponents.setTextMiddle("信息修改", -1);
        mHeadLayoutComponents.setDefaultLeftCallBack(true);
        mHeadLayoutComponents.setRightOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramView) {
                filterData();
            }
        });

        
        initDataToShow(false);
        
        mRadioGroupSex.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(RadioGroup arg0, int mRadioBtId) {
                if(mRadioBtId == R.id.rb_personal_male ){
                    mTagSex ="0";
                }else {
                    mTagSex ="1";
                }
            }
        });
        
        getUserInfo();
    }

    String nickName;
    String phoneName;


    private void getUserInfo() {
        showProgresssDialog();
        String mUid = PreferencesUtils.getString(this, AppDefine.KEY_USERID);
        sendData(UserUpLoadJsonData.getUserInfoNameValuePairs(mUid),
                AppDefine.URL_USER_GETUSERINFO, this, 101);

    }

    private void filterData() {

        nickName = mEditNickName.getText().toString();
        phoneName = mEditPhoneNum.getText().toString();

        if (mListFileDatas.size() == 0 && mIconUrl != null) {
            File mFileIcon = CpApplication.getApplication().mBitmapManager.mBitmapUtils
                    .getBitmapFileFromDiskCache(mIconUrl);
            if (mFileIcon != null)
                mListFileDatas.add(mFileIcon);
        }

        if (TextUtils.isEmpty(nickName)) {
            playYoYo(mEditNickName);
            return;
        }

        if (TextUtils.isEmpty(phoneName)) {
            playYoYo(mEditPhoneNum);
            return;
        }

        if (mListFileDatas.size() == 0) {
            playYoYo(mIvChoiceIcon);
            return;
        }
        sendData();
    }

    String mIconUrl;

    private void initDataToShow(boolean isFromDecode) {
        String nickName = PreferencesUtils.getString(this, "nickName");
        String iconUrl = PreferencesUtils.getString(this, "iconUrl");
        String account = PreferencesUtils.getString(this, "account");
        
        String sex = PreferencesUtils.getString(this, AppDefine.KEY_SEX);
        
        if(TextUtils.isEmpty(sex)||sex.equals("0")){
            mTvSexMan.setChecked(true);
        }else{
            mTvSexFeman.setChecked(true);
        }
        if (!TextUtils.isEmpty(nickName)) {
            mEditNickName.setText(nickName);
        }
        if (!TextUtils.isEmpty(account)) {
            mEditPhoneNum.setText(account);
        }
        if (!TextUtils.isEmpty(iconUrl)) {
            mIconUrl = iconUrl;
            CpApplication.getApplication().mBitmapManager.disPlayImage(mIvChoiceIcon, iconUrl);
        }
    }

    private void sendData() {
        showProgresssDialog();
        String userId = CpApplication.getApplication().getUserId();
        CpApplication.getApplication().mHttpPack
                .sendDataAndImgs(
                        UserUpLoadJsonData.updateUserInfoNameValuePairs(nickName, userId, mTagSex,
                                phoneName),
                        AppDefine.URL_UPDATEUSERINFO, mListFileDatas,
                        new RequestCallBack<String>() {

                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                dismissProgressDialog();
                                DecodeResult.decoResult(responseInfo.result,
                                        new DecodeUpLoad());

                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {
                                dismissProgressDialog();
                            }
                        });
    }

    private void intentToMain() {
        Intent intent = new Intent(this, IndexActivity.class);
        startActivity(intent);
        finish();
    }

    class DecodeUpLoad implements IDecodeJson {

        @Override
        public void onDecoded(String reason, boolean isSuccess,
                JsonObject mJsonResult, JsonArray mLists) {
            if (!isSuccess) {
                Toast.makeText(mContext, reason, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();

                PreferencesUtils.putString(mContext, "iconUrl", mIconUrl);
                PreferencesUtils.putString(mContext, AppDefine.KEY_SEX, mTagSex);
                PreferencesUtils
                        .putString(mContext, "nickName", mEditNickName.getText().toString());

                String mUid = PreferencesUtils.getString(mContext, AppDefine.KEY_USERID);
                User mUser = new User();
                mUser.nickname = nickName;
                mUser.userid = mUid;
                mUser.phone = phoneName;
                mUser.photo = mIconUrl;
                boolean isInsertSuccess = mDbUser.saveData(mUser);

                if (!isInsertSuccess) {
                    try {
                        mDbUser.getDBDbManager().getContentDb()
                                .update(mUser, WhereBuilder.b(User.USERID, "=", mUid));
                    } catch (DbException e) {
                    }
                }
                finish();
            }

        }
    }

    DBUSER mDbUser = new DBUSER();

    private void startCamera() {
        mSelectImageGirdView.actionShowPopuWindow();
    }

    @OnClick(R.id.iv_select_icon)
    public void actionSelectIcon(View mView) {
        startCamera();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSelectImageGirdView.onActivityResult(requestCode, resultCode, data);
    }

    @ViewInject(R.id.ll_modelchangebody)
    View mViewModelLayout;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onHttpError(Exception e, String msg, int requestCode) {
        dismissProgressDialog();
    }

    @Override
    public void onDecoded(String reason, boolean isSuccess, JsonObject mJsonResult,
            JsonArray mLists, int resultCode) {
        dismissProgressDialog();
        if (isSuccess && mJsonResult != null) {
            JsonObject mJsonUser = JsonUtil.getAsJsonObject(mJsonResult, "user");
            ModelPlatUtils.insertUserInfo(mContext, mJsonUser);
            initDataToShow(true);
        }

    }

}
