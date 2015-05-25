package com.zy.booking.modle;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zy.booking.util.TimeUtils;

public class IspOpenAvailDateAdapter extends TimeManagerAdapter{

	
	
	public IspOpenAvailDateAdapter(Context mContext, JsonArray mData) {
		super(mContext, mData);
	}

	@Override
	protected void dealDate(JsonObject mJsonObj, TextView mTvDate,
			TextView mWeek) {
		
		final String date = JsonUtil.getAsString(mJsonObj, DATE);
		mTvDate.setText(date);
		mWeek.setText(TimeUtils.getDateByStringFormat(date));
		
	}

	@Override
	protected void dealAm(JsonObject mJsonObj, TextView mTvAm, String date,
			boolean isTvAmAvail) {
		super.dealAm(mJsonObj, mTvAm, date, isTvAmAvail);
		String amBookNum =  JsonUtil.getAsString(mJsonObj, AM+BOOKINGNUM);
		String totalNum  = JsonUtil.getAsString(mJsonObj, AM+HINTNUM);
		mTvAm.setText(getPanelText(amBookNum,totalNum));
	}

	@Override
	protected void dealPm(JsonObject mJsonObj, TextView mTvPm, String showDate,
			boolean isTvPmAvail) {
		super.dealPm(mJsonObj, mTvPm, showDate, isTvPmAvail);
		String pmBookNum =  JsonUtil.getAsString(mJsonObj, PM+BOOKINGNUM);
		String totalNum  = JsonUtil.getAsString(mJsonObj, PM+HINTNUM);
		mTvPm.setText(getPanelText(pmBookNum,totalNum));
	}

	@Override
	protected void dealNight(JsonObject mJsonObj, TextView mNight,
			String showDate, boolean isTvNightAvail) {
		super.dealNight(mJsonObj, mNight, showDate, isTvNightAvail);
		String nightBookNum =  JsonUtil.getAsString(mJsonObj, NIGHT+BOOKINGNUM);
		String totalNum  = JsonUtil.getAsString(mJsonObj, NIGHT+HINTNUM);
		mNight.setText(getPanelText(nightBookNum,totalNum));
	}
	
	
	private String getPanelText(String bookingNum,String totalNum){
		
		if(TextUtils.isEmpty(totalNum)){
			return "未设定";
		}else {
			if(TextUtils.isEmpty(bookingNum)){
				return "0/"+totalNum;
			}else{
				return bookingNum+"/"+totalNum;
			}
			
		}
	}

	@Override
	protected void dealVisableOperateAll(View mViewAllState, View mViewAllData) {
		super.dealVisableOperateAll(mViewAllState, mViewAllData);
		
		mViewAllState.setVisibility(View.GONE);
		mViewAllData.setVisibility(View.GONE);
	}

	@Override
	protected void dealPriceHintAvailable(JsonObject mJsonItem,
			EditText mEditText) {
		if(mJsonItem.has(UNIT)){
			String unit = JsonUtil.getAsString(mJsonItem, UNIT);
			mEditText.setText(unit);
		}
		mEditText.setEnabled(false);
	}
	

	
	
	
	
	

}
