
package com.zy.booking.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.zy.booking.CpApplication;
import com.zy.booking.util.PreferencesUtils;

/**
 * 用户版本上传数据格式 仅限于基础用户使用
 */
public class UserUpLoadJsonData {
    private final static String APPID = "booking";

    /**
     * @author zhujohnle
     * @deprecated 用于普通用户获取默认推荐的服务列表
     */
    public static String getDefaultProvider(int start, int end) {
        JsonObject mJsonObject = getHeadData();

        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mJsonObject.addProperty("token", token);
        mJsonObject.addProperty("begin", start);
        mJsonObject.addProperty("size", end);
        return mJsonObject.toString();
    }

    /**
     * @author zhujohnle 用于普通用户获取默认推荐的服务列表
     * @return List<NamePairs>
     * @param start 用于标注起点
     * @url BASE_URL_SEARCHDEFAULT
     * @param end 用于标注终点
     * @param lng 经度
     * @param lat 纬度
     * @param mdata 用于扩展
     */
    public static List<NameValuePair> getDefaultProviderNamePairs(int start,
            int end, String lng, String lat, String provice, String area,
            String city, String tags, String keywrods, Map<String, String> mdata) {

        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mLists.add(new BasicNameValuePair("token", token));

        mLists.add(new BasicNameValuePair("begin", start + ""));
        mLists.add(new BasicNameValuePair("size", end + ""));

        if (!TextUtils.isEmpty(lng))
            mLists.add(new BasicNameValuePair("lng", lng));

        if (!TextUtils.isEmpty(lat))
            mLists.add(new BasicNameValuePair("lat", lat));

        if (!TextUtils.isEmpty(tags))
            mLists.add(new BasicNameValuePair("tags", tags));

        if (!TextUtils.isEmpty(provice))
            mLists.add(new BasicNameValuePair("province", provice));

        if (!TextUtils.isEmpty(area))
            mLists.add(new BasicNameValuePair("area", city));

        if (!TextUtils.isEmpty(city))
            mLists.add(new BasicNameValuePair("city", area));

        if (!TextUtils.isEmpty(keywrods))
            mLists.add(new BasicNameValuePair("keyword", keywrods));
        if (mdata != null) {
            Set<String> mKeySet = mdata.keySet();
            for (String mkey : mKeySet) {
                if (mkey != null)
                    mLists.add(new BasicNameValuePair(mkey, mdata.get(mkey)));
            }
        }
        return mLists;
    }

    public static JsonObject getHeadData() {
        JsonObject mJsonObject = new JsonObject();
        mJsonObject.addProperty("appId", "booking");
        mJsonObject.addProperty("imei", CpApplication.getApplication().imei);
        return mJsonObject;
    }

    /**
     * @author zhujohnle
     * @deprecated 根据服务id 获取该服务的明细
     */
    public static String getProviderDital(String spId) {
        JsonObject mJsonObject = getHeadData();
        mJsonObject.addProperty("username", PreferencesUtils.getString(
                CpApplication.getApplication(), "account"));
        mJsonObject.addProperty("spId", spId);
        return mJsonObject.toString();
    }

    /**
     * @author zhujohnle 根据服务id 获取该服务的明细
     * @return List<NamePairs>
     */
    public static List<NameValuePair> getProviderDitalNamePairs(String spId) {
        List<NameValuePair> mNameValuePair = new ArrayList<NameValuePair>();
        mNameValuePair.add(new BasicNameValuePair("spId", spId));
        mNameValuePair.add(new BasicNameValuePair("appId", APPID));
        return mNameValuePair;
    }

    /**
     * @deprecated 登陆接口数据
     */
    public static String getLoginData(String username, String password) {
        JsonObject mJsonObject = getHeadData();
        mJsonObject.addProperty("username", username);
        mJsonObject.addProperty("password", password);
        return mJsonObject.toString();
    }

    /**
     * 登陆接口数据
     * 
     * @category 使用List<NameValuePair> 作为返回值
     */

    public static List<NameValuePair> getLoginDataList(String username,
            String password) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("username", username));
        mLists.add(new BasicNameValuePair("password", password));
        mLists.add(new BasicNameValuePair("appId", "booking"));
        mLists.add(new BasicNameValuePair("imei", CpApplication
                .getApplication().imei));
        return mLists;
    }

    /**
     * @deprecated
     * @category 注册接口数据
     * @return 使用String 作为返回值
     */

    public static String getRegisterData(String userName, String password,
            boolean isIsp) {
        JsonObject mJsonObject = getHeadData();
        mJsonObject.addProperty("username", userName);
        mJsonObject.addProperty("password", password);
        mJsonObject.addProperty("isp", isIsp);
        return mJsonObject.toString();
    }

    /**
     * @category 注册接口数据
     * @category 使用List<NameValuePair> 作为返回值
     */
    public static List<NameValuePair> getRegisterDataNameValuePair(
            String userName, String password, boolean isIsp) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("imei", CpApplication
                .getApplication().imei));
        mLists.add(new BasicNameValuePair("username", userName));
        mLists.add(new BasicNameValuePair("password", password));
        mLists.add(new BasicNameValuePair("isp", isIsp + ""));
        return mLists;
    }

    // 发表评论
    public static String uploadComponent(String components, String spId) {
        JsonObject mJsonObject = getHeadData();
        mJsonObject.addProperty("username", PreferencesUtils.getString(
                CpApplication.getApplication(), "account"));
        mJsonObject.addProperty("component", components);
        mJsonObject.addProperty("spId", spId);

        return mJsonObject.toString();
    }

    /**
     * 查询特定服务商发布的明细
     * 
     * @url /search_issuer
     */
    public static String getRequestServiceData(String ispId) {
        JsonObject mJsonObject = getHeadData();
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mJsonObject.addProperty("token", token);

        mJsonObject.addProperty("ispId", ispId);

        return mJsonObject.toString();
    }

    /**
     * 预约某一项服务
     */
    public static List<NameValuePair> getUpLoadBookingNameValuePairs(
            String date, String spId, String am) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("date", date));
        mLists.add(new BasicNameValuePair("spId", spId));
        mLists.add(new BasicNameValuePair("period", am));
        mLists.add(new BasicNameValuePair("token", PreferencesUtils.getString(
                CpApplication.getApplication(), "token")));
        return mLists;
    }

    /**
     * queryBooking 搜索已经预定的服务 /queryBooking
     * 
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param began 搜索内容 条数开始
     * @param end 搜索内容条数
     */
    public static List<NameValuePair> getBookingServiceNameValuePairs(
            String beginTime, String endTime, String began, String size) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("beginTime", beginTime));
        mLists.add(new BasicNameValuePair("endTime", endTime));
        mLists.add(new BasicNameValuePair("begin", began));
        mLists.add(new BasicNameValuePair("size", size));
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mLists.add(new BasicNameValuePair("token", token));
        return mLists;
    }

    /**
     * 更新用户信息
     */
    public static List<NameValuePair> updateUserInfoNameValuePairs(
            String nickName, String userId, String sex, String phone) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("nickName", nickName));
        mLists.add(new BasicNameValuePair("sex", sex));
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mLists.add(new BasicNameValuePair("token", token));
        mLists.add(new BasicNameValuePair("phone", phone));
        return mLists;
    }

    /**
     * evaluation sp_add_comment 添加对某个主题的评论
     */
    public static List<NameValuePair> getAddCommentListValuePairs(
            String topicId, String commnet) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("topic_id", topicId));
        mLists.add(new BasicNameValuePair("issuer_id", CpApplication
                .getApplication().getUserId()));
        mLists.add(new BasicNameValuePair("comment", commnet));
        return mLists;
    }

    /**
     * evaluation /sp_add_reply 添加对某个主题的评论
     */
    public static List<NameValuePair> getReplyListValuePairs(String others_id,
            String commnet, String commentId) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("others_id", others_id));
        mLists.add(new BasicNameValuePair("issuer_id", CpApplication
                .getApplication().getUserId()));
        mLists.add(new BasicNameValuePair("comment", commnet));
        mLists.add(new BasicNameValuePair("comment_id", commentId));
        return mLists;
    }

    /**
     * evaluation /sp_add_topic 添加对某个主题的评论
     */

    public static List<NameValuePair> getAddTopicValueParis(String spId,
            String content) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("spId", spId));
        mLists.add(new BasicNameValuePair("issuer_id", CpApplication
                .getApplication().getUserId()));
        mLists.add(new BasicNameValuePair("content", content));
        return mLists;
    }

    /**
     * evaluation /sp_get_topics 获取所有评论的主题
     */

    public static List<NameValuePair> getTopicsNameValuePairs(String spId,
            int pageNo, int pageSize) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("sp_id", spId));
        mLists.add(new BasicNameValuePair("page_no", pageNo + ""));
        mLists.add(new BasicNameValuePair("page_size", pageSize + ""));
        return mLists;
    }

    /**
     * group
     */
    // getGroup for group
    public static List<NameValuePair> getGroupNameValuePair(String groupId) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("groupId", groupId));
        return mLists;
    }

    // getGroup for user
    public static List<NameValuePair> getGroupsForUserNameValuePair() {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        mLists.add(new BasicNameValuePair("token", token));
        return mLists;
    }

    // joinGroup
    public static List<NameValuePair> joinGroupNameValuePair(String groupId) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("groupId", groupId));
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mLists.add(new BasicNameValuePair("token", token));
        return mLists;
    }

    // getFriends
    public static List<NameValuePair> getFriendsForUserNameValuePair() {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        mLists.add(new BasicNameValuePair("token", token));
        return mLists;
    }

    // addFriend
    public static List<NameValuePair> addFriendsForUserNameValuePair(
            String friendId) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        mLists.add(new BasicNameValuePair("token", token));
        mLists.add(new BasicNameValuePair("friendId", friendId));

        return mLists;
    }

    // addFriend
    public static List<NameValuePair> getFrindsByKeyWordNameValuePair(
            String keyword, int begin, int size) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("keyword", keyword));
        mLists.add(new BasicNameValuePair("begin", begin + ""));
        mLists.add(new BasicNameValuePair("size", 10 + ""));
        return mLists;
    }

    // becomeModel
    public static List<NameValuePair> becomeModelNameValueParis() {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        mLists.add(new BasicNameValuePair("token", token));
        return mLists;
    }
    
    //quitGroup
    public static List<NameValuePair> exitGroupNameValuePairs(String mGroupId) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("groupId", mGroupId));
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mLists.add(new BasicNameValuePair("token", token));
        return mLists;
    }
    
    //getUserInfo
    public static List<NameValuePair> getUserInfoNameValuePairs(String mUserId) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("userId", mUserId));
        return mLists;
    }
    
    public static List<NameValuePair> removeFrinedValuePairs(String mFriendUid){
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("friendId", mFriendUid));
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mLists.add(new BasicNameValuePair("token", token));
        return mLists;
    }
    
    
    public static List<NameValuePair> getCacheInfoNameValuePairs() {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mLists.add(new BasicNameValuePair("token", token));
        return mLists;
    }
    
    public static List<NameValuePair> removeFriendNameValuePairs(String frinedId) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        
        mLists.add(new BasicNameValuePair("userId", frinedId));
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mLists.add(new BasicNameValuePair("token", token));
        return mLists;
    }
    
}
