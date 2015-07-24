package com.bosi.chineseclass.utils;

import java.util.ArrayList;

import com.bosi.chineseclass.bean.SpanData;

import android.text.TextUtils;

public class BosiUtils {
	
	//对于需要换行的操作
	public static SpanData getInsertRelineData(String data){
		
		if(TextUtils.isEmpty(data)){
			return null;
		}
		SpanData mData = new SpanData();
		ArrayList<Integer []> mList = new ArrayList<Integer[]>();
		char [] mDataArray  = data.toCharArray();
		StringBuilder mStringBuilder = new StringBuilder();
		Integer[] mIntegerTemp =null;
		for(int i=0;i<mDataArray.length ;i++){
			
			
			if(mDataArray[i]=='[' ||mDataArray[i] =='【' ){
				if(i!=0)
				mStringBuilder.append("\n\n");
				mIntegerTemp= new Integer[2];
				mIntegerTemp[0]= i;
			}else if(mDataArray[i]==']' ||mDataArray[i] =='】'){
				if(mIntegerTemp!=null){
					mIntegerTemp[1]= i+1;
					mList.add(mIntegerTemp);
				}
				mIntegerTemp = null;
			}
			
			
			mStringBuilder.append(mDataArray[i]);
		}
		mData.mResouce = mStringBuilder.toString();
		mData.mSpaList = mList;
		return mData;
	}

	
	//对于需要执加颜色的设置
}
