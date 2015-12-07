package com.bosi.chineseclass;

import java.util.Iterator;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.bosi.chineseclass.components.ExitSystemDialog;
import com.bosi.chineseclass.db.BosiDbManager;
import com.bosi.chineseclass.han.db.DbManager;
import com.bosi.chineseclass.utils.AppActivityStack;
import com.bosi.chineseclass.utils.XutilHttpPack;
import com.bosi.chineseclass.utils.XutilHttpPack.OnHttpActionCallBack;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;
import com.umeng.analytics.MobclickAgent;

public class BSApplication extends Application {
	// 文件系统
	public Storage mStorage = null;
	public AppActivityStack mActivityStack;
	// 数据库系统
	public DbManager mDbManager;
	
	public BosiDbManager mDbBosiClass;
	
	public boolean isFirstInBphz = true;
	
	public boolean isFirstInBpcy = true;
	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		
		mHttpPack = new XutilHttpPack();
		storageManagerInit();
		mDbBosiClass = new BosiDbManager(this);//项目逻辑
		mActivityStack = new AppActivityStack();
		// CrashHandler.getInstance().init(this);
		MobclickAgent.setDebugMode(false);
		
	}

	XutilHttpPack mHttpPack;
	public HttpHandler sendData(List<NameValuePair> mList, String url,
			final OnHttpActionListener mTatget, final int code,HttpMethod method) {
		
//		if(!NetWorkListenerControl.isNetWorkAvailable){
//			Toast.makeText(this, "网络连接异常", Toast.LENGTH_SHORT).show();
//			return null;
//		}
		
		HttpHandler mHttpHandler = mHttpPack.sendData(mList, url,
				new OnHttpActionCallBack() {

					@Override
					public void onHttpSuccess(String result) {
						try {
							JSONObject mObj = new JSONObject(result);
							mTatget.onHttpSuccess(mObj,code);
						} catch (JSONException e) {
						}
					}

					@Override
					public void onHttpError(HttpException e, String messge) {
						mTatget.onHttpError(e, messge,code);
					}
				},method);
		return mHttpHandler;
	}
	
	
	public static BSApplication mApplication = null;

	public static BSApplication getInstance() {
		return mApplication;
	}

	private void storageManagerInit() {
		if (SimpleStorage.isExternalStorageWritable()) {
			mStorage = SimpleStorage.getExternalStorage();
		} else {
			mStorage = SimpleStorage.getInternalStorage(this);
		}
		mStorage.createDirectory(AppDefine.FilePathDefine.APP_GLOBLEFILEPATH,
				false);
	}

	// 退出系统要点赞
	public void exitApp() {
		ExitSystemDialog mDialog = new ExitSystemDialog(
				(BaseActivity) mActivityStack.peek());
		mDialog.mDialog.show();
	}
	
	public String getImei(){
		TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		String imei=telephonyManager.getDeviceId();
		
		if(!TextUtils.isEmpty(imei)){
			return imei;
		}else{
			return "1234567890";
		}
	}

	public void destroySystem() {
		
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
		List<ActivityManager.RunningAppProcessInfo> procList = null;
		int result = -1;
		procList = activityManager.getRunningAppProcesses();
		for (Iterator<ActivityManager.RunningAppProcessInfo> iterator = procList
				.iterator(); iterator.hasNext();) {
			ActivityManager.RunningAppProcessInfo procInfo = iterator.next();
			if (procInfo.processName.equals(processName)) {
				result = procInfo.pid;
				break;
			}
		}
		return result;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
