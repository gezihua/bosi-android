
package com.zy.booking.fragments;

import android.content.Intent;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

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
import com.zy.booking.util.PreferencesUtils;

import java.io.File;
import java.util.ArrayList;

public class ChangeModelInfoFragment extends BaseFragment {

    @ViewInject(R.id.iv_select_icon)
    private ImageView mIvChoiceIcon;

    @ViewInject(R.id.et_personal_nickname)
    private EditText mEditNickName;

    @ViewInject(R.id.et_personal_intro)
    private EditText mEditInstoMySelf;

    @ViewInject(R.id.rb_modelinfo_man)
    private RadioButton mTvSexMan;

    @ViewInject(R.id.rb_modelinfo_women)
    private RadioButton mTvSexFeman;

    @ViewInject(R.id.rg_modelTags)
    private RadioGroup mRadioGroup;
    @ViewInject(R.id.rg_modelinfo_sex)
    private RadioGroup mRadioGroupSex;

    private ArrayList<File> mListFileDatas;

    SelectImageGirdComponents mSelectImageView;
    @ViewInject(R.id.ll_userchangebody)
    LinearLayout mLayoutBody;

    @ViewInject(R.id.et_personal_city)
    private EditText mEditNickCity;

    @Override
    protected View getBasedView() {
        return inflater.inflate(R.layout.layout_changemodelinfo, null);
    }

    @Override
    void afterViewInject() {
        mSelectImageView = new SelectImageGirdComponents(this, mActivity, mLayoutBody);
        mSelectImageView.setMaxPicNums(1);
        mSelectImageView.setOnPicSelectedListener(new OnPicSelectedListener() {

            @Override
            public void onPicSelectedListener(String mFilePath) {
                if (!TextUtils.isEmpty(mFilePath)) {
                    iconUrl = mFilePath;
                    mListFileDatas.add(new File(mFilePath));
                    CpApplication.getApplication().mBitmapManager.disPlayImage(mIvChoiceIcon,
                            mFilePath);
                }
            }
        });
        mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int rbId) {
                if (rbId == R.id.rb_women) {
                    mTag = "woman";
                } else if (rbId == R.id.rb_man) {
                    mTag = "man";
                } else {
                    mTag = "child";
                }
            }
        });

        mRadioGroupSex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int sexId) {
                if (sexId == R.id.rb_modelinfo_man) {
                    isManSex = "0";
                } else {
                    isManSex = "1";
                }
            }
        });
        mListFileDatas = new ArrayList<File>();
        initDataToShow();
    }

    private void filterData() {
        nickName = mEditNickName.getText().toString();
        instro = mEditInstoMySelf.getText().toString();
        mCity = mEditNickCity.getText().toString();
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

        if (TextUtils.isEmpty(mTag)) {
            playYoYo(mRadioGroup);
            return;
        }

        if (TextUtils.isEmpty(mCity)) {
            playYoYo(mRadioGroup);
            return;
        }
        if (mListFileDatas.size() == 0) {
            playYoYo(mIvChoiceIcon);
            return;
        }
        sendData();
    }

    private void initDataToShow() {
        nickName = PreferencesUtils.getString(mActivity, AppDefine.KEY_MODELNAME);
        iconUrl = PreferencesUtils.getString(mActivity, AppDefine.KEY_MODELICONURL);
        instro = PreferencesUtils.getString(mActivity, AppDefine.KEY_MODELINSTRO);
        isManSex = PreferencesUtils.getString(mActivity, AppDefine.KEY_MODELSEX);
        mCity = PreferencesUtils.getString(mActivity, AppDefine.KEY_MODELCITY);
        mTag = PreferencesUtils.getString(mActivity, AppDefine.KEY_MODELTAGS);

        if (!TextUtils.isEmpty(isManSex)) {
            if (isManSex.equals("0"))
                mTvSexMan.setChecked(true);
            else {
                mTvSexFeman.setChecked(true);
            }
        }

        if (!TextUtils.isEmpty(mTag)) {
            if (mTag.equals("man")) {
                RadioButton mRadioButton = (RadioButton) mRadioGroup.getChildAt(0);
                mRadioButton.setChecked(true);
            } else if (mTag.equals("woman")) {
                RadioButton mRadioButton = (RadioButton) mRadioGroup.getChildAt(1);
                mRadioButton.setChecked(true);
            } else {
                RadioButton mRadioButton = (RadioButton) mRadioGroup.getChildAt(2);
                mRadioButton.setChecked(true);
            }
        }

        if (!TextUtils.isEmpty(mCity)) {
            mEditNickCity.setText(mCity);
        }
        if (!TextUtils.isEmpty(nickName)) {
            mEditNickName.setText(nickName);
        }
        if (!TextUtils.isEmpty(iconUrl)) {
            CpApplication.getApplication().mBitmapManager.disPlayImage(mIvChoiceIcon, iconUrl);
        }
        if (!TextUtils.isEmpty(instro)) {
            mEditInstoMySelf.setText(instro);
        }
    }

    private String isManSex = "0";
    private String mCity = "";

    private void sendData() {
        showProgresssDialog();

        String modelId = PreferencesUtils.getString(mActivity, AppDefine.KEY_MODELID);

        CpApplication.getApplication().mHttpPack.sendDataAndImgs(
                ModelUploadJsonData.createModleNameValuePairs(nickName, modelId, isManSex, instro,
                        mTag, mCity),
                AppDefine.URL_MODLE_CREATE, mListFileDatas,
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

    String nickName;
    String mTag;
    String iconUrl;
    String instro = "";

    class DecodeUpLoad implements IDecodeJson {

        @Override
        public void onDecoded(String reason, boolean isSuccess,
                JsonObject mJsonResult, JsonArray mLists) {
            if (!isSuccess) {
                Toast.makeText(mActivity, reason, Toast.LENGTH_SHORT).show();
            } else {
                PreferencesUtils.putString(mActivity, AppDefine.KEY_MODELNAME, nickName);
                PreferencesUtils.putString(mActivity, AppDefine.KEY_MODELICONURL, iconUrl);
                PreferencesUtils.putString(mActivity, AppDefine.KEY_MODELINSTRO, instro);
                PreferencesUtils.putString(mActivity, AppDefine.KEY_MODELSEX, isManSex);
                PreferencesUtils.putString(mActivity, AppDefine.KEY_MODELCITY, mCity);
                PreferencesUtils.putString(mActivity, AppDefine.KEY_MODELTAGS, mTag);

                Toast.makeText(mActivity, "信息修改成功", Toast.LENGTH_SHORT).show();
                mActivity.finish();
            }
        }
    }

    @OnClick(R.id.iv_select_icon)
    public void actionSelectIcon(View mView) {
        mSelectImageView.actionShowPopuWindow();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSelectImageView.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.bt_update)
    public void actionUpdate(View mView) {
        filterData();
    }

}
