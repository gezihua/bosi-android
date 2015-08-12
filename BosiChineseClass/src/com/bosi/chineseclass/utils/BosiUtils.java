package com.bosi.chineseclass.utils;

import java.util.ArrayList;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.bean.SpanData;

public class BosiUtils {

	public static void loadTransfDataBaseSquare(TextView mTextView, String mData) {
		SpanData mSpanData = BosiUtils.getInsertRelineData(mData);
		if (mSpanData != null) {
			if (!TextUtils.isEmpty(mSpanData.mResouce)) {
				if (mSpanData.mSpaList != null && mSpanData.mSpaList.size() > 0) {
					SpannableStringBuilder builder = new SpannableStringBuilder(
							mSpanData.mResouce);

					for (int i = 0; i < mSpanData.mSpaList.size(); i++) {
						Integer[] mIntegerArray = mSpanData.mSpaList.get(i);

						if (mIntegerArray[0] != null
								&& mIntegerArray[1] != null) {
							ForegroundColorSpan redSpan = new ForegroundColorSpan(
									Color.RED);

							builder.setSpan(redSpan, mIntegerArray[0],
									mIntegerArray[1],
									Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							String mSpanDatas = mSpanData.mResouce.substring(
									mIntegerArray[0], mIntegerArray[1]);
							System.out.println(mSpanDatas);
						}

					}
					mTextView.setText(builder);
				}else{
					mTextView.setText(mSpanData.mResouce);
				}
			}else{
				mTextView.setText("");
			}
		} else {
			mTextView.setText("");
		}
	}

	// 对于需要换行的操作 [] 转成红色 并且换行
	public static SpanData getInsertRelineData(String data) {

		if (TextUtils.isEmpty(data)) {
			return null;
		}
		SpanData mData = new SpanData();
		ArrayList<Integer[]> mList = new ArrayList<Integer[]>();
		char[] mDataArray = data.toCharArray();
		StringBuilder mStringBuilder = new StringBuilder();
		Integer[] mIntegerTemp = null;
		int tempCurrent = -1; // 用于做游标的 。因为一次 遍历以后如果 有特殊字符 需要加换行符此时换行符也占了位
		for (int i = 0; i < mDataArray.length; i++) {
			tempCurrent++;
			if (mDataArray[i] == '[' || mDataArray[i] == '【') {
				if (i != 0) {
					mStringBuilder.append("\n\n");
					tempCurrent += 2;
				}

				mIntegerTemp = new Integer[2];
				mIntegerTemp[0] = tempCurrent;
			} else if (mDataArray[i] == ']' || mDataArray[i] == '】') {
				if (mIntegerTemp != null) {
					mIntegerTemp[1] = tempCurrent + 1;
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

	// 对于需要执加颜色的设置

	public static SpanData getSpanFilterDataFromFilterArray(String data) {
		String filter = BSApplication.getInstance().getResources()
				.getString(R.string.bphz_dital_reline_filter);

		if (TextUtils.isEmpty(data)) {
			return null;
		}
		SpanData mData = new SpanData();
		char[] mDataArray = data.toCharArray();
		StringBuilder mStringBuilder = new StringBuilder();
		for (int i = 0; i < mDataArray.length; i++) {
			
			if (filter.indexOf(mDataArray[i])> -1) {
				if (i != 0) {
					mStringBuilder.append("\n\n");
				}
			}
			mStringBuilder.append(mDataArray[i]);
		}
		mData.mResouce = mStringBuilder.toString();
		return mData;

	}
}
