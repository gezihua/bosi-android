package com.zy.booking.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;

import com.zy.booking.CpApplication;
import com.zy.booking.util.PreferencesUtils;
import com.google.gson.JsonObject;

public class IspUpLoadJsonData {
	private final static String APPID = "booking";

	public static JsonObject getHeadData() {
		JsonObject mJsonObject = new JsonObject();
		mJsonObject.addProperty("appId", "booking");
		mJsonObject.addProperty("imei", CpApplication.getApplication().imei);
		return mJsonObject;
	}

	/**
	 * @author zhujohnle
	 * @deprecated 根据服务id 获取该服务的明细
	 * */
	public static String getProviderDital(String spId) {
		JsonObject mJsonObject = getHeadData();
		mJsonObject.addProperty("appId", APPID);
		mJsonObject.addProperty("spId", spId);
		return mJsonObject.toString();
	}

	/**
	 * @author zhujohnle 根据服务id 获取该服务的明细
	 * @return List<NamePairs>
	 * 
	 *         /detail
	 * */
	public static List<NameValuePair> getProviderDitalNamePairs(String spId) {
		List<NameValuePair> mNameValuePair = new ArrayList<NameValuePair>();
		mNameValuePair.add(new BasicNameValuePair("spId", spId));
		mNameValuePair.add(new BasicNameValuePair("appId", APPID));
		return mNameValuePair;
	}

	/**
	 * @author zhujohnle
	 * 
	 *         获取已经发布的服务明细
	 * */
	public static List<NameValuePair> getSendedServicesNameValuePair(String begin ,String size) {
		List<NameValuePair> mNameValuePairs = new ArrayList<NameValuePair>();
		String token = PreferencesUtils.getString(
				CpApplication.getApplication(), "token");
		if (token != null)
			mNameValuePairs.add(new BasicNameValuePair("token", token));

		mNameValuePairs.add(new BasicNameValuePair("appId", APPID));
		mNameValuePairs.add(new BasicNameValuePair("begin", begin));
		mNameValuePairs.add(new BasicNameValuePair("size", size));
		mNameValuePairs.add(new BasicNameValuePair("username", PreferencesUtils
				.getString(CpApplication.getApplication(), "account")));
		return mNameValuePairs;
	}

	/**
	 * @author zhujohnle
	 * @deprecated return List<NameValuePair>
	 * */
	public static String getSendedServices() {
		JsonObject mJsonObject = new JsonObject();
		String token = PreferencesUtils.getString(
				CpApplication.getApplication(), "token");
		if (token != null)
			mJsonObject.addProperty("token", token);
		mJsonObject.addProperty("appId", "booking");
		mJsonObject.addProperty("username", PreferencesUtils.getString(
				CpApplication.getApplication(), "account"));
		return mJsonObject.toString();
	}

	public static JsonObject getRequestUsers(String serviceId) {
		JsonObject mJsonObject = getHeadData();
		String token = PreferencesUtils.getString(
				CpApplication.getApplication(), "token");
		if (token != null)
			mJsonObject.addProperty("token", token);
		if (serviceId != null)
			mJsonObject.addProperty("spId", serviceId);

		return mJsonObject;
	}

	/**
	 * @deprecated 登陆接口数据
	 * */
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
	 * 
	 * */

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
	 * */

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
	 * 
	 * */
	public static List<NameValuePair> getRegisterDataNameValuePair(
			String userName, String password, boolean isIsp) {
		List<NameValuePair> mLists = new ArrayList<NameValuePair>();
		mLists.add(new BasicNameValuePair("appId", "booking"));
		mLists.add(new BasicNameValuePair("imei", CpApplication
				.getApplication().imei));
		mLists.add(new BasicNameValuePair("username", userName));
		mLists.add(new BasicNameValuePair("password", password));
		mLists.add(new BasicNameValuePair("isp", isIsp + ""));
		mLists.add(new BasicNameValuePair("phone",userName));
		
		return mLists;
	}

	/**
	 * @category 发布服务的接口 文本
	 * @deprecated
	 * @return String
	 * 
	 * */
	public static String getUploadData(String title, String contact,
			String dital, String place) {
		JsonObject mJsonObject = getHeadData();

		String token = PreferencesUtils.getString(
				CpApplication.getApplication(), "token");
		if (token != null)
			mJsonObject.addProperty("token", token);
		mJsonObject.addProperty("name", title);
		mJsonObject.addProperty("phone", contact);
		mJsonObject.addProperty("introduction", dital);
		mJsonObject.addProperty("address", place);
		mJsonObject.addProperty("tags", title);
		mJsonObject.addProperty("lng", "");
		mJsonObject.addProperty("lat", "");
		return mJsonObject.toString();
	}

	/**
	 * @category 发布服务的接口 文本
	 * 
	 * @return List<NameValuePair>
	 * 
	 *         /create
	 * 
	 * */
	public static List<NameValuePair> getUploadDataNameValuePair(String title,
			String contact, String dital, String place,String lng ,String lat,String tag) {

		List<NameValuePair> mLists = new ArrayList<NameValuePair>();

		String token = PreferencesUtils.getString(
				CpApplication.getApplication(), "token");
		if (token != null)
			mLists.add(new BasicNameValuePair("token", token));
		mLists.add(new BasicNameValuePair("name", title));
		mLists.add(new BasicNameValuePair("phone", contact));
		mLists.add(new BasicNameValuePair("introduction", dital));
		mLists.add(new BasicNameValuePair("address", place));
		mLists.add(new BasicNameValuePair("tags", tag));
		if(!TextUtils.isEmpty(lng))
		mLists.add(new BasicNameValuePair("lng", lng));
		if(!TextUtils.isEmpty(lat))
		mLists.add(new BasicNameValuePair("lat", lat));
		mLists.add(new BasicNameValuePair("appId", APPID));
		return mLists;
	}

	/**
	 * 上传时间管理
	 * 
	 * @deprecated
	 * @return String
	 * 
	 *         /changeSchedule
	 * 
	 * */
	public static String uploadTheTimeManager(JsonObject mJsonObj) {
		JsonObject mJsonObject = new JsonObject();

		String token = PreferencesUtils.getString(
				CpApplication.getApplication(), "token");
		if (token != null)
			mJsonObject.addProperty("token", token);

		mJsonObject.add("weekSchedule", mJsonObj);
		return mJsonObject.toString();
	}

	/**
	 * 上传时间管理
	 * 
	 * @return List<NameValuePair>
	 * */
	public static List<NameValuePair> uploadTheTimeManagerList(
			JsonObject mJsonObj,String mkeyForArray) {

		List<NameValuePair> mLists = new ArrayList<NameValuePair>();
		String token = PreferencesUtils.getString(
				CpApplication.getApplication(), "token");
		if (token != null)
			mLists.add(new BasicNameValuePair("token", token));
		mLists.add(new BasicNameValuePair(mkeyForArray, mJsonObj.toString()));
		return mLists;
	}

	
	//Isp 开放预约时间管理
	public static List<NameValuePair> getOpenDateListPairs(String beginTime,
			String endTime,String spId) {
		List<NameValuePair> mLists = new ArrayList<NameValuePair>();
		mLists.add(new BasicNameValuePair("beginTime", beginTime));
		mLists.add(new BasicNameValuePair("endTime", endTime));
		mLists.add(new BasicNameValuePair("spId", spId));
		String token = PreferencesUtils.getString(
				CpApplication.getApplication(), "token");
		mLists.add(new BasicNameValuePair("token", token));
		return mLists;
	}
	
	//Isp 开放预约时间管理
	public static List<NameValuePair> getOpenedDateListPairs(String spId) {
		List<NameValuePair> mLists = new ArrayList<NameValuePair>();
		mLists.add(new BasicNameValuePair("spId", spId));
		String token = PreferencesUtils.getString(
				CpApplication.getApplication(), "token");
		mLists.add(new BasicNameValuePair("token", token));
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

	// 搜索已经预定的服务
	public static String getRequestServiceData(String time, String msgId) {
		JsonObject mJsonObject = getHeadData();
		String token = PreferencesUtils.getString(
				CpApplication.getApplication(), "token");
		if (token != null)
			mJsonObject.addProperty("token", token);
		mJsonObject.addProperty("time", time);
		mJsonObject.addProperty("username", PreferencesUtils.getString(
				CpApplication.getApplication(), "account"));
		mJsonObject.addProperty("spId", msgId);
		return mJsonObject.toString();
	}

	/**
	 * queryBooking 搜索已经预定的用户
	 * 
	 * /queryBooking
	 * 
	 * @param beginTime 开始时间
	 * @param  endTime   结束时间
	 * 
	 * @param began  搜索内容 条数开始
	 * @param end    搜索内容条数
	 *
	 * */
	public static List<NameValuePair> getBookingUsersNameValuePairs(
			String beginTime, String endTime, String began, String size,String spId) {
		List<NameValuePair> mLists = new ArrayList<NameValuePair>();
		mLists.add(new BasicNameValuePair("spId", spId));
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

}
