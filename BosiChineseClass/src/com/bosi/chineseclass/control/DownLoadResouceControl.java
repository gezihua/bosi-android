package com.bosi.chineseclass.control;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.utils.NetStateUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class DownLoadResouceControl {

	HttpUtils mHttpUtils;

	private List<HttpHandler> mHandlerList;

	boolean isModleResourceAbs = true;
	
	int maxLength = 0;

	public void setModelResourceAbs(boolean isModleResourceAbs) {
		this.isModleResourceAbs = isModleResourceAbs;
	}

	public interface DownLoadInterface {
		public String[] getDownLoadUrls();

		public void onDownLoadCallback(int mCurrentSize, int wholeSize);

		public String getFolderPath();
	}

	DownLoadInterface mDownLoadCallBack;

	boolean isCancleTask = false ;
	public void onDestroy(){
		canclTask();
	}
	public void setOnDownLoadCallback(DownLoadInterface mDownLoadCallBack) {
		this.mDownLoadCallBack = mDownLoadCallBack;
	}

	int loadedData = -1;

	BaseActivity mActivity;
	AtomicBoolean isDownLoadOver = new AtomicBoolean(false) ;

	public DownLoadResouceControl(BaseActivity mActivity) {
		this.mActivity = mActivity;
		onCreate();
	}

	public void onCreate() {
		mHttpUtils = new HttpUtils();
		mHandlerList = new ArrayList<HttpHandler>();
	}

	public String getAbsFilePath() {
		String mCurrentFoderName = mDownLoadCallBack.getFolderPath();
		BSApplication.getInstance().mStorage.createDirectory(mCurrentFoderName);

		return BSApplication.getInstance().mStorage.getFile(mCurrentFoderName)
				.getAbsolutePath() + "/";
	}

	private boolean isCurrentDownLoadSuccess() {
		return loadedData == maxLength;
	}

	public boolean downloadFiles() {
		if(!NetStateUtil.isNetWorkAlive(mActivity)){
			mActivity.showToastShort("没有网络连接 ,请检查手机网络");
			mActivity.mLoadingDialog.dismiss();
			return false;
		}
		isDownLoadOver .set(false);
		String [] urls = mDownLoadCallBack.getDownLoadUrls();
		
		if (mDownLoadCallBack == null
				|| urls == null)
			return false;

		this.maxLength = urls.length;
		// 如果没有文件的话 先创建文件
		final String filePath = getAbsFilePath();

		int files = BSApplication.getInstance().mStorage.getFile(
				mDownLoadCallBack.getFolderPath()).list().length;

		if (urls.length == files && isModleResourceAbs) {
			return true;
		}

		loadedData = -1;
		isCancleTask = false;
		mActivity.mLoadingDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				canclTask();
				// 如果内容没下载完 则销毁页面 条件是 当前页面上要下载的是固定的
				if (!isDownLoadOver.get() && isModleResourceAbs) {
					mActivity.finish();
				}
			}
		});
		updateProgress();
		for (int i = 0; i < urls.length; i++) {
			String url = urls[i];
			String fileName = url.substring(url.lastIndexOf("/"), url.length());
			HttpHandler mHandler = mHttpUtils.download(urls[i], filePath
					+ fileName, new RequestCallBack<File>() {

				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					updateProgress();
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					updateProgress();
				}
			});
			mHandlerList.add(mHandler);
		}
		return false;
	}

	public void canclTask() {
		isCancleTask = true;
		if (mHandlerList != null && mHandlerList.size() > 0) {
			for (HttpHandler mHandler : mHandlerList) {
				mHandler.cancel();
			}
		}
	}

	private synchronized void updateProgress() {
		if(isCancleTask)return;
		loadedData++;
		mActivity.updateProgress(loadedData, maxLength);
		if (isCurrentDownLoadSuccess()) {
			isDownLoadOver .set(true);
			if (mDownLoadCallBack != null)
				mDownLoadCallBack.onDownLoadCallback(loadedData, maxLength);
			mActivity.dismissProgress();
			loadedData = 0;
			
		}
	}

}
