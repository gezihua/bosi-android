package com.zy.booking.modle;

import android.content.Context;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zy.booking.R;
import com.zy.booking.util.TimeUtils;
import com.zy.booking.util.ViewHolder;

public class TimeAvialableAdapter extends TimeManagerAdapter {

	private final String NIGHT = "night";

	public TimeAvialableAdapter(Context mContext, JsonArray mData) {
		super(mContext, mData);
	}

	@Override
	public View getView(int posi, View viewTemp, ViewGroup arg2) {
		if (viewTemp == null) {
			viewTemp = LayoutInflater.from(context).inflate(
					R.layout.item_layout_timeavailable, null);
		}
		JsonObject mJsonObject = (JsonObject) mListData.get(posi);
		String date = JsonUtil.getAsString(mJsonObject, DATE);

		TextView mTvDate = ViewHolder
				.get(viewTemp, R.id.tv_item_timeavail_date);
		TextView mAm = ViewHolder.get(viewTemp, R.id.tv_item_timeavail_am);
		TextView mPm = ViewHolder.get(viewTemp, R.id.tv_item_timeavail_pm);
		TextView mNight = ViewHolder
				.get(viewTemp, R.id.tv_item_timeavail_night);
		TextView mWeek = ViewHolder.get(viewTemp, R.id.tv_item_timeavail_week);
		if (date != null) {
			mWeek.setText(TimeUtils.getDateByStringFormat(date));
			mTvDate.setText(date);
		}

		View mView = ViewHolder.get(viewTemp, R.id.view_timeavail_hint);

		final boolean isTvAmAvail = JsonUtil.getAsBoolean(mJsonObject, AM);
		mAm.setBackgroundResource(isTvAmAvail ? R.drawable.icon_available
				: R.drawable.icon_unavailable);
		registerCallBack(mAm, isTvAmAvail, date, AM);

		final boolean isTvPMAvail = JsonUtil.getAsBoolean(mJsonObject, PM);
		mPm.setBackgroundResource(isTvPMAvail ? R.drawable.icon_available
				: R.drawable.icon_unavailable);
		registerCallBack(mPm, isTvPMAvail, date, PM);

		final boolean isTvNightAvail = JsonUtil
				.getAsBoolean(mJsonObject, NIGHT);
		mNight.setBackgroundResource(isTvNightAvail ? R.drawable.icon_available
				: R.drawable.icon_unavailable);
		registerCallBack(mNight, isTvNightAvail, date, NIGHT);

		dealAm(mJsonObject, mAm, date, isTvAmAvail);
		dealPm(mJsonObject, mPm, date, isTvPMAvail);
		dealPm(mJsonObject, mNight, date, isTvNightAvail);

		if (posi == mListData.size() - 1) {
			mView.setVisibility(View.GONE);
		}

		return viewTemp;

	}

	private void registerCallBack(View mView, final boolean isAvail,
			final String date, final String time) {
		mView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				onDateClick(isAvail, date, time);
			}
		});
	}

	public void onDateClick(boolean isAvail, String date, String time) {
		if (onDateClick != null) {
			if (isAvail) {
				onDateClick.onDateClick(date, time);
			} else {
				Toast.makeText(context, "该时间不提供服务", Toast.LENGTH_SHORT).show();
			}

		}
	}

	@Override
	protected void showDialog(boolean isAvail, String date,
			JsonObject mJsonObject, String time, JsonArray mJsonArray) {
		if (onDateClick != null) {
			if (isAvail)
				onDateClick.onDateClick(date, time);
		}
		return;
	}

	protected void dealAm(JsonObject mJsonObj, TextView mTvAm, String date,
			boolean isTvAmAvail) {

		String price = JsonUtil.getAsString(mJsonObj, AM + HINTPRICE);
		String unit = JsonUtil.getAsString(mJsonObj, UNIT);
		if (!TextUtils.isEmpty(price)) {
			mTvAm.setText(price + unit);
		} else {
			mTvAm.setText("未设定");
		}
	}

	@Override
	protected void dealPm(JsonObject mJsonObj, TextView mTvPm, String showDate,
			boolean isTvPmAvail) {
		String price = JsonUtil.getAsString(mJsonObj, PM + HINTPRICE);
		String unit = JsonUtil.getAsString(mJsonObj, UNIT);
		if (!TextUtils.isEmpty(price)) {
			mTvPm.setText(price + unit);
		} else {
			mTvPm.setText("未设定");
		}
	}

	@Override
	protected void dealNight(JsonObject mJsonObj, TextView mNight,
			String showDate, boolean isTvNightAvail) {
		String price = JsonUtil.getAsString(mJsonObj, NIGHT + HINTPRICE);
		String unit = JsonUtil.getAsString(mJsonObj, UNIT);
		if (!TextUtils.isEmpty(price)) {
			mNight.setText(price + unit);
		} else {
			mNight.setText("未设定");
		}
	}

	private String getPanelText(String bookingNum, String totalNum) {

		if (TextUtils.isEmpty(totalNum)) {
			return "未设置";
		} else {
			if (TextUtils.isEmpty(bookingNum)) {
				return "0/" + totalNum;
			} else {
				return bookingNum + "/" + totalNum;
			}

		}
	}

}
