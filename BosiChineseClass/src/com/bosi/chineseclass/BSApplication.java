package com.bosi.chineseclass;

import com.bosi.chineseclass.han.db.DbManager;
import com.bosi.chineseclass.utils.AppActivityStack;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import android.app.Application;

public class BSApplication extends Application{
	// 文件系统
		public Storage mStorage = null;
		public  AppActivityStack mActivityStack;
		
		// 数据库系统
	    public DbManager mDbManager;
		@Override
		public void onCreate() {
			super.onCreate();
			mApplication = this;
			storageManagerInit();
			
			/*mDbManager = new DbManager(this);*/
			
			mActivityStack = new AppActivityStack();
			
			CrashHandler.getInstance().init(this);
		}
		public static BSApplication mApplication=null;
		
		public  static BSApplication getInstance(){
			return mApplication;
		}
		
		
		private void storageManagerInit() {
			if (SimpleStorage.isExternalStorageWritable()) {
				mStorage = SimpleStorage.getExternalStorage();
			} else {
				mStorage = SimpleStorage.getInternalStorage(this);
			}
			mStorage.createDirectory(AppDefine.FilePathDefine.APP_GLOBLEFILEPATH, false);
		}

}
