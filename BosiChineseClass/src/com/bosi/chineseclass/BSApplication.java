package com.bosi.chineseclass;

import com.bosi.chineseclass.utils.AppActivityStack;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import android.app.Application;

public class BSApplication extends Application{
	// 文件系统
		public Storage mStorage = null;
		public  AppActivityStack mActivityStack;
		@Override
		public void onCreate() {
			super.onCreate();
			storageManagerInit();
			mActivityStack = new AppActivityStack();
		}
		public static BSApplication mApplication=null;
		
		public  static BSApplication getInstance(){
			if(mApplication==null){
				mApplication = new BSApplication();
			}
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
