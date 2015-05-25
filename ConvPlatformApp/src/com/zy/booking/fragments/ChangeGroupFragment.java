
package com.zy.booking.fragments;

import android.content.Intent;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zy.booking.AppDefine;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.components.SelectImageGirdComponents;
import com.zy.booking.components.SelectImageGirdComponents.OnPicSelectedListener;
import com.zy.booking.json.DecodeResult;
import com.zy.booking.json.IDecodeJson;
import com.zy.booking.json.ModelUploadJsonData;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.ModelPlatUtils;
import com.zy.booking.util.PreferencesUtils;

import java.io.File;
import java.util.ArrayList;

public class ChangeGroupFragment extends BaseFragment implements OnHttpActionListener{

    @ViewInject(R.id.iv_select_icon)
    private ImageView mIvChoiceIcon;

    @ViewInject(R.id.et_personal_nickname)
    private EditText mEditNickName;

    @ViewInject(R.id.et_personal_intro)
    private EditText mEditInstoMySelf;

    SelectImageGirdComponents mSelectImagesView;

    private ArrayList<File> mListFileDatas;

    @ViewInject(R.id.ll_userchangebody)
    LinearLayout mLayoutBody;

    @Override
    protected View getBasedView() {
        return inflater.inflate(R.layout.layout_change_group, null);
    }

    private final int GETGROUP = 101;
    // 获取分组成员
    private void getGroupData() {
        showProgresssDialog();
        sendData(UserUpLoadJsonData.getGroupNameValuePair(mGroupId),
                AppDefine.URL_GROUP_GETGROUP, this, GETGROUP);
    }
    @Override
    void afterViewInject() {
        mSelectImagesView = new SelectImageGirdComponents(this, mActivity, mLayoutBody);
        mSelectImagesView.setOnPicSelectedListener(new OnPicSelectedListener() {

            @Override
            public void onPicSelectedListener(String mFilePath) {
                if (!TextUtils.isEmpty(mFilePath)) {
                    iconUrl = mFilePath;
                    mListFileDatas.add(new File(mFilePath));
                    CpApplication.getApplication().mBitmapManager.disPlayImage(mIvChoiceIcon,
                            iconUrl);
                }

            }
        });
        mListFileDatas = new ArrayList<File>();
        initDataToShow();
    }

    String nickName;
    String instro = "";

    private void filterData() {
        nickName = mEditNickName.getText().toString();
        instro = mEditInstoMySelf.getText().toString();

        if (mListFileDatas.size() == 0 && iconUrl != null) {
            File mFileIcon = CpApplication.getApplication().mBitmapManager.mBitmapUtils
                    .getBitmapFileFromDiskCache(iconUrl);
            if (mFileIcon != null)
                mListFileDatas.add(mFileIcon);
        }
        if (TextUtils.isEmpty(nickName)) {
            playYoYo(mEditNickName);
            return;
        }
        if (TextUtils.isEmpty(instro)) {
            playYoYo(mEditInstoMySelf);
            return;
        }

        if (mListFileDatas.size() == 0) {
            playYoYo(mIvChoiceIcon);
            return;
        }
        sendData();
    }

    String iconUrl;
    String mGroupId ;

    private void initDataToShow() {
        String nickName = PreferencesUtils.getString(mActivity, AppDefine.KEY_GROUPNAME);
        String iconUrl = PreferencesUtils.getString(mActivity, AppDefine.KEY_GROUPICON);
        String intro = PreferencesUtils.getString(mActivity, AppDefine.KEY_GROUPINTRO);
        
        mGroupId = PreferencesUtils.getString(mActivity, AppDefine.KEY_MODELGROUPID);
        
        if(TextUtils.isEmpty(mGroupId)){
            mActivity.finish();
            mActivity.showToastShort("本地未找到群id");
        }
        
        
        if (!TextUtils.isEmpty(nickName)) {
            mEditNickName.setText(nickName);
        }else{
            getGroupData();
        }
        
        if (!TextUtils.isEmpty(iconUrl)) {
            this.iconUrl = iconUrl;
            CpApplication.getApplication().mBitmapManager.disPlayImage(mIvChoiceIcon, iconUrl);
        }
        if (!TextUtils.isEmpty(intro)) {
            mEditInstoMySelf.setText(intro);
        }
        
    }

    private void sendData() {
        showProgresssDialog();
        
        CpApplication.getApplication().mHttpPack.sendDataAndImgs(
                ModelUploadJsonData.updatGroupNameValuePairs(mGroupId, instro, nickName),
                AppDefine.URL_MODEL_UPDATEGROUP, mListFileDatas,
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

    class DecodeUpLoad implements IDecodeJson {

        @Override
        public void onDecoded(String reason, boolean isSuccess,
                JsonObject mJsonResult, JsonArray mLists) {
            if (!isSuccess) {
                Toast.makeText(mActivity, reason, Toast.LENGTH_SHORT).show();
            } else {
                // 保存群组信息
                PreferencesUtils.putString(mActivity, AppDefine.KEY_MODELGROUPID, mGroupId);
                PreferencesUtils.putString(mActivity, AppDefine.KEY_GROUPNAME, nickName);
                PreferencesUtils.putString(mActivity, AppDefine.KEY_GROUPICON, iconUrl);
                PreferencesUtils.putString(mActivity, AppDefine.KEY_GROUPINTRO, instro);
                Toast.makeText(mActivity, "群组信息修改成功", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void startCamera() {
        mSelectImagesView.actionShowPopuWindow();
    }

    @OnClick(R.id.iv_select_icon)
    public void actionSelectIcon(View mView) {
        startCamera();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSelectImagesView.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.bt_update)
    public void actionUpdate(View mView) {
        filterData();
    }
    @Override
    public void onHttpError(Exception e, String msg, int requestCode) {
        
    }
    @Override
    public void onDecoded(String reason, boolean isSuccess, JsonObject mJsonResult,
            JsonArray mLists, int resultCode) {
        dismissProgressDialog();
        if (resultCode == GETGROUP) {
            JsonObject mJsonGroup = JsonUtil.getAsJsonObject(mJsonResult, "group");
            
            ModelPlatUtils.insertGroupInfo(mActivity, mJsonGroup);
            initDataToShow();
        }
    }

}
