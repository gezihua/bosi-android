package com.bosi.chineseclass.control;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

import com.bosi.chineseclass.BaseActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class DownLoadResouceControl {

	private String[] mCurrentData;

	HttpUtils mHttpUtils;

	private List<HttpHandler> mHandlerList;

	public interface DownloadCallback {
		public void onDownLoadCallback(int mCurrentSize, int wholeSize);
	}

	DownloadCallback mDownLoadCallBack;
	
	public void setOnDownLoadCallback(DownloadCallback mDownLoadCallBack){
		this.mDownLoadCallBack = mDownLoadCallBack;
	}
	int loadedData = -1;

	BaseActivity mActivity;

	String mFilePath;

	public DownLoadResouceControl(BaseActivity mActivity) {
		this.mActivity = mActivity;
		onCreate() ;
	}

	public void onCreate() {
		mHttpUtils = new HttpUtils();
		mHandlerList = new ArrayList<HttpHandler>();
	}

	public void downloadFiles(String filePath,String [] urls) {
		this.mCurrentData = urls;
		if (mCurrentData == null || mCurrentData.length == 0)
			return;
		loadedData = -1;
		mActivity.mLoadingDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				canclTask();
			}
		});
		updateProgress();
		for (int i = 0; i < mCurrentData.length; i++) {
			String url = mCurrentData[i];
			String fileName = url.substring(url.lastIndexOf("/"), url.length());
			HttpHandler mHandler = mHttpUtils.download(mCurrentData[i],
					filePath+fileName, new RequestCallBack<File>() {

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
	}

	public void canclTask() {
		if (mHandlerList != null && mHandlerList.size() > 0) {
			for (HttpHandler mHandler : mHandlerList) {
				mHandler.cancel();
			}
		}
	}

	private synchronized void updateProgress() {
		loadedData++;
		mActivity.updateProgress(loadedData, mCurrentData.length);
		if (loadedData == mCurrentData.length) {
			if(mDownLoadCallBack!=null)
			mDownLoadCallBack.onDownLoadCallback(loadedData, mCurrentData.length);
			mActivity.dismissProgress();
			loadedData = 0;
		}
	}

	
	
}
