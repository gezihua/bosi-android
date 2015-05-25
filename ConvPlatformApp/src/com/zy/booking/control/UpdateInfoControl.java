
package com.zy.booking.control;

import android.text.TextUtils;

import com.emsg.sdk.util.JsonUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.json.ModelUploadJsonData;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.ModelPlatUtils;
import com.zy.booking.util.PreferencesUtils;

public class UpdateInfoControl implements OnHttpActionListener {

    // URL_USER_GETCACHEDATA
    BaseActivity mActivity;

    public UpdateInfoControl(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }

    private final int CODE_GETCACHE = 101;
    private final int CODE_GETUSER = 102;
    private final int CODE_GETMODEL = 103;
    private final int CODE_GETGROUP = 104;

    long mUserLastUpdateTime;
    long mModelLastUpdateTime;
    long mGroupLastUpdateTime;

    public void getCacheData() {
        mActivity.sendData(UserUpLoadJsonData.getCacheInfoNameValuePairs(),
                AppDefine.URL_USER_GETCACHEDATA, this, CODE_GETCACHE);
    }

    private void getUserInfo() {
        String mUid = PreferencesUtils.getString(mActivity, AppDefine.KEY_USERID);
        mActivity.sendData(UserUpLoadJsonData.getUserInfoNameValuePairs(mUid),
                AppDefine.URL_USER_GETUSERINFO, this, CODE_GETUSER);
    }

    private void getGroupInfo() {
        String mGroupId = PreferencesUtils.getString(mActivity, AppDefine.KEY_MODELGROUPID);
        if(TextUtils.isEmpty(mGroupId))return;
        mActivity.sendData(UserUpLoadJsonData.getGroupNameValuePair(mGroupId),
                AppDefine.URL_GROUP_GETGROUP, this, CODE_GETGROUP);
    }

    private void getModelInfo() {
        String modelId = PreferencesUtils.getString(mActivity, AppDefine.KEY_USERID);
        mActivity.sendData(
                ModelUploadJsonData.getModelNameValuePairs(modelId),
                AppDefine.URL_MODLE_GETMODEL, this, CODE_GETMODEL);
    }

    @Override
    public void onHttpError(Exception e, String msg, int requestCode) {
        mActivity.dismissProgressDialog();
        if (requestCode == CODE_GETCACHE) {
            mActivity.showToastShort("获取用户信息失败" + msg);
        } else if (requestCode == CODE_GETUSER) {
            mActivity.showToastShort("获取用户信息失败" + msg);
        } else if (requestCode == CODE_GETGROUP) {
            mActivity.showToastShort("获取群组信息失败" + msg);
        } else if (requestCode == CODE_GETMODEL) {
            mActivity.showToastShort("获取模特信息失败" + msg);
        }
    }

    @Override
    public void onDecoded(String reason, boolean isSuccess, JsonObject mJsonResult,
            JsonArray mLists, int resultCode) {
        mActivity.dismissProgressDialog();
        if (mJsonResult == null) {
            return;
        }
        if (resultCode == CODE_GETCACHE) {
            JsonObject mCacheData = JsonUtil.getAsJsonObject(mJsonResult, "cachedData");
            if (mCacheData != null) {
                JsonArray mCacheList = JsonUtil.getAsJsonArray(mCacheData, "itemList");
                updateLocalInfo(mCacheList);
            }
        }
        if (resultCode == CODE_GETUSER) {
            JsonObject mJsonData = JsonUtil.getAsJsonObject(mJsonResult, USER);
            ModelPlatUtils.insertUserInfo(mActivity, mJsonData);
        }
        if (resultCode == CODE_GETGROUP) {
            JsonObject mJsonData = JsonUtil.getAsJsonObject(mJsonResult, GROUP);
            ModelPlatUtils.insertGroupInfo(mActivity, mJsonData);
        }
        if (resultCode == CODE_GETMODEL) {
            JsonObject mJsonData = JsonUtil.getAsJsonObject(mJsonResult, MODEL);
            ModelPlatUtils.insertModelInfo(mActivity, mJsonData);
        }
    }

    // {"user":{"id":"5b5db57ff9ae47fb9f1280bcdd93e82f","username":"18310665040","password":"","nickname":"猪乐","photo":"http://202.85.221.165:8080/app-img/5b5db57ff9ae47fb9f1280bcdd93e82f/2f295ac2abd3487b9b94a477372c24b7.jpeg","sex":"0","phone":"123456","sp":"true"}}
    // {"model":{"id":"5b5db57ff9ae47fb9f1280bcdd93e82f","name":"猪乐","photo":"http://202.85.221.165:8080/app-img/5b5db57ff9ae47fb9f1280bcdd93e82f/d3c38302f37d43bc855815a81af996dc.jpeg","introduction":"特斯拉","groupId":"7512616f263a4b3986bc9223a1f15586","tags":"特斯拉","albumList":[{"id":"023404dfde1d4583953116650098b35c","name":null,"introduction":null,"referId":null,"referType":null,"userId":null,"imageList":[]}]}}
    // {"group":{"id":"7512616f263a4b3986bc9223a1f15586","name":"猪乐的群","introduction":"萝莉控","photo":"http://202.85.221.165:8080/app-img/5b5db57ff9ae47fb9f1280bcdd93e82f/6f9629c25c924f3d90e69181d4255c9a.jpeg","master":{"id":"5b5db57ff9ae47fb9f1280bcdd93e82f","username":"18310665040","password":"E10ADC3949BA59ABBE56E057F20F883E","nickname":"猪乐","photo":"http://202.85.221.165:8080/app-img/5b5db57ff9ae47fb9f1280bcdd93e82f/2f295ac2abd3487b9b94a477372c24b7.jpeg","sex":"0","phone":"123456","sp":"true"},"memberList":[{"userId":"5b5db57ff9ae47fb9f1280bcdd93e82f","username":"18310665040","nickname":"猪乐","phone":"123456","photo":"http://202.85.221.165:8080/app-img/5b5db57ff9ae47fb9f1280bcdd93e82f/2f295ac2abd3487b9b94a477372c24b7.jpeg","sex":"0"},{"userId":"e79740817cfb40d5a6b2b5ae03f02367","username":"123456","nickname":"123456","phone":"123456","photo":"http://202.85.221.165:8080/app-img/e79740817cfb40d5a6b2b5ae03f02367/c5d6061ada5d401595e09b053ca65ac6.jpeg","sex":"0"}]}}

    private final String USER = "user";
    private final String GROUP = "group";
    private final String MODEL = "model";

    private void updateLocalInfo(JsonArray mJsonArray) {
        if (mJsonArray == null || mJsonArray.size() == 0)
            return;
        for (int i = 0; i < mJsonArray.size(); i++) {
            JsonObject mJsonObject = (JsonObject) mJsonArray.get(i);
            String type = JsonUtil.getAsString(mJsonObject, "type");
            String timsTamp = JsonUtil.getAsString(mJsonObject, "timestamp");
            if (type.equals(USER)) {
                try {
                    dealUserInfo(timsTamp);
                    PreferencesUtils.putLong(mActivity, AppDefine.KEY_USERUPDATETIME,
                            mUserLastUpdateTime);
                } catch (Exception e) {
                }
            } else if (type.equals(GROUP)) {
                try {
                    dealGroupInfo(timsTamp);
                    PreferencesUtils.putLong(mActivity, AppDefine.KEY_GROUPDATETIME,
                            mGroupLastUpdateTime);
                } catch (Exception e) {
                }
            } else if (type.equals(MODEL)) {
                try {
                    dealModelInfo(timsTamp);
                    PreferencesUtils.putLong(mActivity, AppDefine.KEY_MODELUPDATETIME,
                            mModelLastUpdateTime);
                } catch (Exception e) {
                }
            }
        }
    }

    private void dealUserInfo(String timeMini) throws Exception {
        if (TextUtils.isEmpty(timeMini))
            return;
        long timeMiniLong = Long.parseLong(timeMini);
        long localTimeMini = PreferencesUtils.getLong(mActivity, AppDefine.KEY_USERUPDATETIME);
        mUserLastUpdateTime = timeMiniLong;
        if (isNeedUpdate(timeMiniLong, localTimeMini)) {
            getUserInfo();
        }

    }

    private void dealModelInfo(String timeMini) throws Exception {
        if (TextUtils.isEmpty(timeMini))
            return;
        long timeMiniLong = Long.parseLong(timeMini);
        long localTimeMini = PreferencesUtils.getLong(mActivity, AppDefine.KEY_MODELUPDATETIME);
        mModelLastUpdateTime = timeMiniLong;
        if (isNeedUpdate(timeMiniLong, localTimeMini)) {
            getModelInfo();
        }
    }

    private void dealGroupInfo(String timeMini) throws Exception {
        if (TextUtils.isEmpty(timeMini))
            return;
        long timeMiniLong = Long.parseLong(timeMini);
        long localTimeMini = PreferencesUtils.getLong(mActivity, AppDefine.KEY_GROUPDATETIME);
        mGroupLastUpdateTime = timeMiniLong;
        if (isNeedUpdate(timeMiniLong, localTimeMini)) {
            getGroupInfo();
        }
    }

    private boolean isNeedUpdate(long mServerTime, long mLocalTime) {
        if (mServerTime > mLocalTime)
            return true;
        return false;
    }

}
