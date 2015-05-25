
package com.zy.booking.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dk.view.drop.WaterDrop;
import com.emsg.sdk.util.JsonUtil;
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
import com.zy.booking.activitys.ChangeUserInfoActivity;
import com.zy.booking.activitys.MySendedServiceAcitivity;
import com.zy.booking.activitys.PreferenceActivity;
import com.zy.booking.activitys.SampleHolderActivity;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.db.MSGHISTORY;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.ModelPlatUtils;
import com.zy.booking.util.PreferencesUtils;
import com.zy.booking.view.PullScrollView;

public class PersionalFragment extends BaseFragment implements
        PullScrollView.OnTurnListener, OnHttpActionListener {
    HeadLayoutComponents mLayoutComponents;
    @ViewInject(R.id.headactionbar)
    View mViewBase;

    @ViewInject(R.id.user_name)
    TextView mTvUserName;

    @ViewInject(R.id.ll_container_body)
    LinearLayout mLayoutBody;

    @ViewInject(R.id.user_avatar)
    ImageView mIvicon;

    SwipListViewComponents mListViewComonents;

    @ViewInject(R.id.cimg_unreadcount)
    WaterDrop mUnReadWaterCount;

    MyPersionalBody mPersionBody;

    String iconPath;

    Bitmap mBitmapIcon;

    public void afterViewInject() {

        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(EmsgManager.ACTION_NEWMSGCOMING);

        mActivity.registerReceiver(mBroadCastReciver, mIntentFilter);

        mLayoutComponents = new HeadLayoutComponents(getActivity(), mViewBase);
        mLayoutComponents.setTextMiddle("我的", -1);

        mListViewComonents = new SwipListViewComponents(getActivity());

        View mViewPersion = inflater.inflate(R.layout.layout_persional_body,
                null);
        mLayoutBody.addView(mViewPersion);

        mPersionBody = new MyPersionalBody(mViewPersion);

        // String mUserId = PreferencesUtils.getString(getActivity(), "userid");
        // String mNickName = PreferencesUtils
        // .getString(getActivity(), "nickName");
        //
        // if (TextUtils.isEmpty(mNickName)) {
        // ArrayList<NameValuePair> mList = new ArrayList<NameValuePair>();
        // mList.add(new BasicNameValuePair("userId", mUserId));
        // sendData(mList, AppDefine.URL_GETUSERINFO, this, TAG_SEARCHUSERINFO);
        // }
    }

    private final int TAG_SEARCHUSERINFO = 101;
    private final int TAG_TRANSTOMODEL = 102;

    private void displayImage(String path) {
        CpApplication.getApplication().mBitmapManager.disPlayImage(mIvicon,
                path);
    }

    @Override
    public void onResume() {
        super.onResume();
        getBaseDataFromSb();
        updateUnReadCount();
        String modelId = PreferencesUtils.getString(mActivity, AppDefine.KEY_MODELID);
        if (!TextUtils.isEmpty(modelId)) {
            mPersionBody.mPersionInfo.setVisibility(View.VISIBLE);
            mViewTransModel.setVisibility(View.GONE);
        }

    }

    private void getBaseDataFromSb() {
        mTvUserName.setText(PreferencesUtils.getString(getActivity(),
                "nickName"));
        String currentUrl = PreferencesUtils
                .getString(getActivity(), "iconUrl");

        // 读nickname
        if (!TextUtils.isEmpty(currentUrl)) {
            displayImage(currentUrl);
        }
    }

    @ViewInject(R.id.scroll_view)
    ScrollView mScrollView;

    @Override
    public void onTurn() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBitmapIcon != null && !mBitmapIcon.isRecycled())
            mBitmapIcon.recycle();
        try {
            mActivity.unregisterReceiver(mBroadCastReciver);
        } catch (Exception e) {

        }

    }

    @Override
    protected View getBasedView() {

        return inflater.inflate(R.layout.fragment_layout_persional, null);
    }

    @OnClick(R.id.user_avatar)
    public void actionPersionDital(View mView) {
        Intent mIntent = new Intent(getActivity(), SampleHolderActivity.class);
        mIntent.putExtra("tag", SampleHolderActivity.TAG_MODELINFO);
        mIntent.putExtra("id", PreferencesUtils.getString(
                getActivity(), AppDefine.KEY_MODELID));
        getActivity().startActivity(mIntent);
    }

    private boolean isModel() {
        String modleId = PreferencesUtils.getString(getActivity(),
                AppDefine.KEY_MODELID);
        return !TextUtils.isEmpty(modleId);
    }

    class MyPersionalBody {
        private View mBaseView;
        @ViewInject(R.id.ll_persional_editmyinfodital)
        public View mPersionInfo;
        @ViewInject(R.id.ll_persional_systemsetting)
        View mSystemSetting;

        @ViewInject(R.id.ll_persional_mybooking)
        View mOrderList;

        public MyPersionalBody(View mView) {
            this.mBaseView = mView;
            ViewUtils.inject(this, mBaseView);
            if (isModel()) {
                mPersionInfo.setVisibility(View.VISIBLE);
                mViewTransModel.setVisibility(View.GONE);
            }
        }

        @OnClick(R.id.ll_persional_systemsetting)
        public void actionSystemSetting(View mView) {
            Intent mIntent = new Intent(getActivity(), PreferenceActivity.class);
            startActivity(mIntent);
        }

        @OnClick(R.id.ll_persional_editmyinfodital)
        public void actionPersionInfo(View mView) {
            Intent mIntent = new Intent(getActivity(),
                    SampleHolderActivity.class);
            mIntent.putExtra("tag", SampleHolderActivity.TAG_CHANGEMODELINFO);
            startActivity(mIntent);
        }

        @OnClick(R.id.ll_persional_system)
        public void actionSystemPersionInfo(View mView) {
            Intent mIntent = new Intent(getActivity(),
                    ChangeUserInfoActivity.class);
            mIntent.putExtra(ChangeUserInfoActivity.TAG_FROM, false);
            startActivity(mIntent);

        }

        @OnClick(R.id.ll_persional_mybooking)
        public void actionMyBooking(View mView) {
            Intent mIntent = new Intent(getActivity(),
                    SampleHolderActivity.class);
            mIntent.putExtra("tag", SampleHolderActivity.TAG_CUSTORMORDER);
            startActivity(mIntent);
        }

        @OnClick(R.id.ll_persional_mysended)
        public void actionMySendedService(View mView) {
            Intent mIntent = new Intent(getActivity(),
                    MySendedServiceAcitivity.class);
            mIntent.putExtra("tag", SampleHolderActivity.TAG_CUSTORMORDER);
            startActivity(mIntent);
        }
    }

    // 修改用户信息
    class ChangeInfoPanel {

    }

    @Override
    public void onHttpError(Exception e, String msg, int requestCode) {

    }

    @Override
    public void onDecoded(String reason, boolean isSuccess,
            JsonObject mJsonResult, JsonArray mLists, int resultCode) {
        dismissProgressDialog();

        if (resultCode == TAG_TRANSTOMODEL) {
            JsonObject mJsonModel = JsonUtil.getAsJsonObject(mJsonResult, "model");

            ModelPlatUtils.insertModelInfo(mActivity, mJsonModel);
            mPersionBody.mPersionInfo.setVisibility(View.VISIBLE);
            mViewTransModel.setVisibility(View.GONE);
            return;
        }
        // if (isSuccess && mJsonResult != null) {
        // if (resultCode == TAG_SEARCHUSERINFO) {
        // JsonObject mJsonObj = JsonUtil.getAsJsonObject(mJsonResult,
        // "user");
        // String nickName = JsonUtil.getAsString(mJsonObj, "nickname");
        // String iconUrl = JsonUtil.getAsString(mJsonObj, "photo");
        // PreferencesUtils.putString(getActivity(), "nickName", nickName);
        // PreferencesUtils.putString(getActivity(), "iconUrl", iconUrl);
        // getBaseDataFromSb();
        //
        // if (mJsonResult.has("model")) {
        // JsonObject mJsonModel = JsonUtil.getAsJsonObject(mJsonResult,
        // "model");
        // mPersionBody.mPersionInfo.setVisibility(View.VISIBLE);
        // mViewTransModel.setVisibility(View.GONE);
        // ModelPlatUtils.insertModelInfo(mActivity, mJsonModel);
        // }
        // }
        // }
    }

    NiftyDialogBuilder mBuilder;
    @ViewInject(R.id.view_transf_model)
    View mViewTransModel;

    @OnClick(R.id.view_transf_model)
    public void showTransfModel(View mView) {
        showProgresssDialog();
        sendData(UserUpLoadJsonData.becomeModelNameValueParis(),
                AppDefine.URL_USER_BECOMEMODEL, this, TAG_TRANSTOMODEL);
    }

    MSGHISTORY mMsgDb = new MSGHISTORY();

    private void updateUnReadCount() {
        String unReadMsgNum = mMsgDb
                .getUnReadMsgCount(getString(R.string.select_unreadcount_levmsg));
        if (unReadMsgNum.equals("0")) {
            mUnReadWaterCount.setVisibility(View.INVISIBLE);
            unReadMsgCount = 0;
        } else {
            unReadMsgCount = Integer.parseInt(unReadMsgNum);
            mUnReadWaterCount.setText(unReadMsgNum);
            mUnReadWaterCount.setVisibility(View.VISIBLE);
        }
    }

    int unReadMsgCount = 0;

    BroadcastReceiver mBroadCastReciver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            // 查询未读数量 并更新界面
            if (!mActivity.isFinishing()) {
                updateUnReadCount();
            }

        }

    };

}
