
package com.zy.booking.fragments;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emsg.sdk.EmsgCallBack;
import com.emsg.sdk.util.JsonUtil;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zy.booking.AppDefine;
import com.zy.booking.CpApplication;
import com.zy.booking.EmsgManager;
import com.zy.booking.R;
import com.zy.booking.activitys.SampleHolderActivity;
import com.zy.booking.json.ModelUploadJsonData;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.PreferencesUtils;

public class ModelInfoFragment extends BaseFragment implements OnHttpActionListener {

    ModelBodyDital mModelBodyDital;

    @Override
    protected View getBasedView() {
        return inflater.inflate(R.layout.fragment_layout_common, null);
    }

    JsonObject mJsonObj; // 包含了所有的模特的信息

    public void setData(JsonObject mJsonObj) {
        this.mJsonObj = mJsonObj;
        mModelBodyDital.updatePanel();
    }

    class ModelBodyDital {
        public View mViewBody;

        @ViewInject(R.id.et_modelinfo_work)
        private EditText mEtWork;

        @ViewInject(R.id.et_modelinfo_city)
        private EditText mEtCity;

        @ViewInject(R.id.et_modelinfo_sex)
        private EditText mEtSex;

        @ViewInject(R.id.et_modelinfo_intro)
        private EditText mEtIntro;

        @ViewInject(R.id.tv_model_name)
        private TextView mTvName;

        @ViewInject(R.id.model_userphoto)
        private ImageView mImageIcon;
        @ViewInject(R.id.bt_show_mymsg)
        private Button mBtShowMsg;

        @ViewInject(R.id.bt_send_message)
        private Button mBtSendMsg;

        @OnClick(R.id.model_userphoto)
        public void actionUserInfo(View mView) {
            Intent mIntent = new Intent(mActivity, SampleHolderActivity.class);
            mIntent.putExtra(SampleHolderActivity.TAG_ID, modelId);
            mIntent.putExtra(SampleHolderActivity.TAG_TAG, SampleHolderActivity.TAG_SHOWUSERINFO);
            mActivity.startActivity(mIntent);
        }
        @OnClick(R.id.bt_send_message)
        public void actionSendMsg(View mView){
            showLeaveMsgDialog();
        }
        
        @OnClick(R.id.bt_show_mymsg)
        public void actionShowMsg(View mView){
            Intent mIntent = new Intent(mActivity,SampleHolderActivity.class);
            mIntent.putExtra(SampleHolderActivity.TAG_TAG, SampleHolderActivity.TAG_SHOWLEAVEMSG);
            mActivity.startActivity(mIntent);
        }
        public ModelBodyDital() {
            mViewBody = LayoutInflater.from(mActivity).inflate(
                    R.layout.layout_persiondital_formodel, null);
            ViewUtils.inject(this, mViewBody);
        }

        // //{"id":"5b5db57ff9ae47fb9f1280bcdd93e82f",
        // "name":"猪乐","photo":"http://202.85.221.165:8080/app-img/5b5db57ff9ae47fb9f1280bcdd93e82f/2ed59aab71cc4dde883f4be365623dd6.jpeg",
        // "introduction":"我的青春我做主","groupId":"7512616f263a4b3986bc9223a1f15586","tags":"woman","albumList":[]}
        public void updatePanel() {
            String userName = JsonUtil.getAsString(mJsonObj, "name");
            String iconUrl = JsonUtil.getAsString(mJsonObj, "photo");

            String mSex = JsonUtil.getAsString(mJsonObj, "sex");
            String mCity = JsonUtil.getAsString(mJsonObj, "city");

            String introduction = JsonUtil.getAsString(mJsonObj, "introduction");
            CpApplication.getApplication().mBitmapManager.disPlayImage(mImageIcon, iconUrl);
            mEtIntro.setText(introduction);
            mTvName.setText(userName);

            String modelIdLocal = PreferencesUtils.getString(mActivity, AppDefine.KEY_MODELID);

            if (!TextUtils.isEmpty(modelIdLocal) && modelIdLocal.equals(modelId)) {
                mBtShowMsg.setVisibility(View.VISIBLE);
                mBtSendMsg.setVisibility(View.GONE);
            } else {
                mBtShowMsg.setVisibility(View.GONE);
                mBtSendMsg.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(mCity)) {
                mEtCity.setText(mCity);
            }

            if (!TextUtils.isEmpty(mSex)) {
                mEtSex.setText(mSex.equals("0") ? "男" : "女");
            }
        }

    }

    String modelId;

    public ModelInfoFragment(String modelId) {
        this.modelId = modelId;
    }

    @ViewInject(R.id.ll_container_body)
    LinearLayout mLayoutBody;

    @Override
    void afterViewInject() {
        mModelBodyDital = new ModelBodyDital();
        mLayoutBody.addView(mModelBodyDital.mViewBody);

    }

    private void getModelData() {
        if (TextUtils.isEmpty(modelId))
            return;
        mActivity.showProgresssDialog();
        mActivity.sendData(
                ModelUploadJsonData.getModelNameValuePairs(modelId),
                AppDefine.URL_MODLE_GETMODEL, this, 101);
    }

    @Override
    public void onHttpError(Exception e, String msg, int requestCode) {

    }

    @Override
    public void onDecoded(String reason, boolean isSuccess, JsonObject mJsonResult,
            JsonArray mLists, int resultCode) {

        if (mJsonResult != null) {
            mJsonObj = mJsonResult.getAsJsonObject("model");
            mModelBodyDital.updatePanel();
        }
    }

    NiftyDialogBuilder mBuilder;

    private void showLeaveMsgDialog() {
        View mViewAddAlbumLayout = LayoutInflater.from(mActivity).inflate(
                R.layout.layout_dialog_leavemsg, null);
        final EditText mEtMsgContent = (EditText) mViewAddAlbumLayout
                .findViewById(R.id.et_leave_msg);

        mBuilder = NiftyDialogBuilder
                .getInstance(mActivity).withButton1Text("确定")
                .withEffect(Effectstype.Shake)
                .setCustomView(mViewAddAlbumLayout, mActivity).withButton2Text("取消")
                .withEffect(Effectstype.Shake).withTitle("留言板");
        mBuilder.setButton1Click(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showProgresssDialog();
                String mNameData = mEtMsgContent.getText().toString();
                if (TextUtils.isEmpty(mNameData)) {
                    playYoYo(mEtMsgContent);
                    return;
                }
                CpApplication.getApplication().mEmsgManager.sendLeaveMessage(modelId
                        + EmsgManager.EMSGAREA, mNameData, new EmsgCallBack() {

                    @Override
                    public void onSuccess() {
                        dismissProgressDialog();
                        mActivity.showToastShort("私密留言成功");
                        mBuilder.dismiss();
                    }
                    @Override
                    public void onError(TypeError mErrorType) {
                        dismissProgressDialog();
                        mActivity.showToastShort("私密留言失败");
                    }

                });
            }
        });

        mBuilder.setButton2Click(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mBuilder.dismiss();
            }
        });
        try {
            mBuilder.show();
        } catch (Exception e) {
        }

    }

}
