package com.bosi.chineseclass.utils;


import java.io.File;

import java.util.List;

import org.apache.http.NameValuePair;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;


public class XutilHttpPack {
	
	private final String HTTP_TAG = "ZYO2O-HTTP";
	
	private HttpUtils mHttPUtils;
	
	public interface OnHttpActionCallBack{
		public void onHttpSuccess(String result);
		public void onHttpError(HttpException e,String messge);
	}
	
	public XutilHttpPack(){
		mHttPUtils =  new HttpUtils(true); 
		mHttPUtils.configTimeout(10*1000);
	}
	public HttpUtils getHttpUtils(){
		return mHttPUtils;
	}
	
	
	public HttpHandler<String> sendData(List<NameValuePair> nameValuePairs,String url,final OnHttpActionCallBack httpCallBack ){
		RequestParams mRequest = getSendDataRequestParams(nameValuePairs,null,"");
		
		return sendData(mRequest,url,httpCallBack);
	}
	private HttpHandler<String> sendData(RequestParams mRequest,String url,final OnHttpActionCallBack httpCallBack ){
		HttpHandler<String> mHandler = mHttPUtils.send(HttpMethod.POST, url, mRequest, new  RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				httpCallBack.onHttpSuccess(responseInfo.result);
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				httpCallBack.onHttpError(error, msg);
				
			}
		});
		return mHandler;
	}
	
	
	public HttpHandler<String> sendDataAndImgs(List<NameValuePair> nameValuePairs,String url,List<File> mListFiles ,RequestCallBack<String> mRequestCallBack ){
		RequestParams mRequest = getSendDataRequestParams(nameValuePairs,mListFiles,"img");
		return mHttPUtils.send(HttpMethod.POST, url,mRequest, mRequestCallBack);
	}
	
 	public HttpHandler<String> sendDataAndVoices(List<NameValuePair> nameValuePairs,String url,List<File> mListFiles ,RequestCallBack<String> mRequestCallBack ){
		RequestParams mRequest = getSendSingleParams(nameValuePairs,mListFiles,"voice");
		return mHttPUtils.send(HttpMethod.POST, url,mRequest, mRequestCallBack);
	}
	
	public HttpHandler<String> sendDataAndImgs(String mData,String url,List<File> mListFiles ,RequestCallBack<String> mRequestCallBack ){
		RequestParams mRequest = getSendDataRequestParams(mData,mListFiles);
		return mHttPUtils.send(HttpMethod.POST, url,mRequest, mRequestCallBack);
	}
	
	public RequestParams getSendDataRequestParams(String data,List<File> mListFiles){
		RequestParams mRequest = new RequestParams();
		mRequest.addBodyParameter("body", data);
		if(mListFiles!=null){
			for(int i=0;i<mListFiles.size();i++){
				mRequest.addBodyParameter("img"+i, mListFiles.get(i));
			}
		}
		return mRequest;
	}
	
	/**
	 * just send sigle file data
	 * 
	 * */
	public RequestParams getSendSingleParams(List<NameValuePair> nameValuePairs,List<File> mListFiles ,String mFileName){
		RequestParams mRequest = new RequestParams();
		mRequest.addBodyParameter(nameValuePairs);
		if(mListFiles!=null&&mListFiles.size()>0)
		mRequest.addBodyParameter(mFileName, mListFiles.get(0));
		return mRequest;
	}
	public RequestParams getSendDataRequestParams(List<NameValuePair> nameValuePairs,List<File> mListFiles ,String mFileName){
		RequestParams mRequest = new RequestParams();
		mRequest.addBodyParameter(nameValuePairs);
		if(mListFiles!=null){
			for(int i=0;i<mListFiles.size();i++){
				mRequest.addBodyParameter(mFileName+i, mListFiles.get(i));
			}
		}
		return mRequest;
	}
	public void destroy(){
	}

}
