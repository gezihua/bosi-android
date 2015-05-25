package com.zy.booking;

import android.app.Activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.emsg.sdk.EmsgClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;
import com.umeng.analytics.AnalyticsConfig;
import com.zy.booking.db.DbManager;
import com.zy.booking.json.DecodeResult;
import com.zy.booking.json.IDecodeJson;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.DataTools;
import com.zy.booking.util.PreferencesUtils;
import com.zy.booking.util.XutilHttpPack;
import com.zy.booking.util.XutilHttpPack.OnHttpActionCallBack;

import org.apache.http.NameValuePair;

import java.util.Iterator;
import java.util.List;

public class CpApplication extends Application {

	// 全局变量
	public String imei;
	// 即时通讯系统
	private EmsgClient mEmsgClient = null;
	public EmsgManager mEmsgManager;
	// 联网系统
	public XutilHttpPack mHttpPack;
	// 数据库系统
	public DbManager mDbManager;
	// 文件系统
	public Storage mStorage = null;
	// 图片系统
	public BitmapCacheManager mBitmapManager;

	// 应用 的activity栈管理
	public AppActivityStack mActivityStack;

	private String mUid;

	public String getUserId() {
		if (mUid == null) {
			mUid = PreferencesUtils.getString(this, "userid");
		}
		return mUid;
	}

	public EmsgClient getEmsgClient() {
		return mEmsgClient;
	}

	private static CpApplication mCpApplication;

	public static CpApplication getApplication() {
		return mCpApplication;
	}

	private void storageManagerInit() {
		if (SimpleStorage.isExternalStorageWritable()) {
			mStorage = SimpleStorage.getExternalStorage();
		} else {
			mStorage = SimpleStorage.getInternalStorage(this);
		}
		mStorage.createDirectory(AppDefine.APP_GLOBLEFILEPATH, false);

		// 初始化百度地图及定位
		SDKInitializer.initialize(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initUtil();
		imei = DataTools.getDeviceId(this);

		mCpApplication = this;

		storageManagerInit();

		mDbManager = new DbManager(this);

		mEmsgClient = EmsgClient.getInstance();
		mEmsgManager = new EmsgManager(mEmsgClient, this);

		mBitmapManager = new BitmapCacheManager(this);

		mActivityStack = new AppActivityStack();
		
		AnalyticsConfig.setAppkey("555abafe67e58ef4030024c5");
		AnalyticsConfig.setChannel("MODEL-PLATFORM");
	}

	private void initUtil() {
		mHttpPack = new XutilHttpPack();
	}

	@Override
	public void onTerminate() {
		try {
			mEmsgClient.closeClient();
		} catch (Exception ex) {
		}

		super.onTerminate();
	}

	public void destroySystem() {
		mHttpHandler.cancel(true);
		mEmsgManager.cancleNotify();
		try {
			isBaiduServiceRunningKill();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Activity mActivity : mActivityStack) {
			if (mActivity != null && !mActivity.isFinishing()) {
				mActivity.finish();
			}
		}
		mEmsgClient.closeClient();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
	}

	// 检查服务运行状态
	private void isBaiduServiceRunningKill() throws Exception {
		android.os.Process.killProcess(getProcessPid(getPackageName()
				+ ":remote"));
	}

	public int getProcessPid(String processName) {
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> procList = null;
		int result = -1;
		procList = activityManager.getRunningAppProcesses();
		for (Iterator<RunningAppProcessInfo> iterator = procList.iterator(); iterator
				.hasNext();) {
			RunningAppProcessInfo procInfo = iterator.next();
			if (procInfo.processName.equals(processName)) {
				result = procInfo.pid;
				break;
			}
		}
		return result;
	}

	HttpHandler<String> mHttpHandler;

	public HttpHandler<String> getHttpHandler() {
		return mHttpHandler;
	}

	public void sendData(List<NameValuePair> mList, String url,
			final OnHttpActionListener mTatget, final int code) {
		mHttpHandler = CpApplication.getApplication().mHttpPack.sendData(mList,
				url, new OnHttpActionCallBack() {

					@Override
					public void onHttpSuccess(String result) {
						DecodeResult.decoResult(result, new IDecodeJson() {

							@Override
							public void onDecoded(String reason,
									boolean isSuccess, JsonObject mJsonResult,
									JsonArray mLists) {
								mTatget.onDecoded(reason, isSuccess,
										mJsonResult, mLists, code);
							}
						});
					}

					@Override
					public void onHttpError(HttpException e, String messge) {
						mTatget.onHttpError(e, messge, code);
					}
				});
	}

	public void playYoYo(View mView) {
		YoYo.with(Techniques.Shake).duration(700).playOn(mView);
	}

	public static enum USER {
		ISP, CUSTROM
	}

}
