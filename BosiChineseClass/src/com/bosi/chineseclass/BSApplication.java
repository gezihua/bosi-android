package com.bosi.chineseclass;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;

import com.bosi.chineseclass.components.ExitSystemDialog;
import com.bosi.chineseclass.db.BosiDbManager;
import com.bosi.chineseclass.han.db.DbManager;
import com.bosi.chineseclass.utils.AppActivityStack;
import com.lidroid.xutils.HttpUtils;
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

	public HttpUtils mHttpUtils;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		storageManagerInit();
		mDbBosiClass = new BosiDbManager(this);//项目逻辑
		mActivityStack = new AppActivityStack();
		mHttpUtils = new HttpUtils();
		// CrashHandler.getInstance().init(this);
		MobclickAgent.setDebugMode(true);
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
