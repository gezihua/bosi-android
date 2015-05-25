package com.zy.booking.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.zy.booking.AppDefine;
import com.zy.booking.R;
import com.zy.booking.CpApplication.USER;
import com.zy.booking.components.CategorysGirdComponents;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.json.IspUpLoadJsonData;
import com.zy.booking.modle.TimeManagerAdapter;
import com.zy.booking.modle.TimeManagerAdapter.OnDateClickListener;
import com.zy.booking.struct.OnHttpActionListener;
import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ViewInject;

//it just for the igc to control the time manager 

public class TimeManagerFragment extends BaseFragment implements
		OnHttpActionListener, OnDateClickListener {

	CategorysGirdComponents mGridComponets;
	TimeManagerAdapter mAdapter;
	JsonArray mJsonArray = new JsonArray();

	@ViewInject(R.id.ll_container_body)
	LinearLayout mLayoutBody;

	String spId;

	public void setSpId(final String spId) {
		this.spId = spId;

	}

	// 从dital 中查出时间管理
	public void sendData(String spId) {
		sendData(IspUpLoadJsonData.getProviderDitalNamePairs(spId),
				AppDefine.BASE_URL_SEARCHDITAL, this, 101);
	}

	public void afterViewInject() {

		mGridComponets = new CategorysGirdComponents(null, getActivity());
		mGridComponets.setVerNumber(1);
		mAdapter = new TimeManagerAdapter(getActivity(), mJsonArray);
		mAdapter.setOnDateClick(this);
		mGridComponets.setAdapter(mAdapter);

		mLayoutBody.addView(mGridComponets.getView());

		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sendData(spId);
	}

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
		dismissProgressDialog();
	}

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		dismissProgressDialog();
		if(!isSuccess)return;
		if (resultCode == 101) {
			if (mJsonResult == null)
				return;
			JsonObject mJsonData = JsonUtil.getAsJsonObject(mJsonResult,
					"schedule");
			
			if (mJsonData == null)
				return;
			JsonArray mJsonDayList = JsonUtil.getAsJsonArray(mJsonData,
					"dayList");
			
			String unit = JsonUtil.getAsString(mJsonData,
					"unit");

			dismissProgressDialog();
			if (isSuccess) {
				mJsonArray = mJsonDayList;
				mAdapter.changeDataSource(mJsonArray);
				mAdapter.setpriceUnit(unit);
			}

		} else if (resultCode == 102) {
			mAdapter.setpriceUnit(changedUnitForPrice);
			mAdapter.notifyDataSetChanged();
		}
	}

	
	private String changedUnitForPrice="";
	@Override
	public void onDateClick(JsonArray mJsonArray) {
		JsonObject mJsonObject = new JsonObject();
		this.mJsonArray = mJsonArray;
		mJsonObject.add("dayList", mJsonArray);
		mJsonObject.addProperty("spId", spId);
		showProgresssDialog();
		sendData(IspUpLoadJsonData.uploadTheTimeManagerList(mJsonObject,"weekSchedule"),
				AppDefine.BASE_URL_CHANGESCHEDULE, this, 102);
	}

	OnTimeManagerCallBack mCallBack;

	public void setCallBack(OnTimeManagerCallBack mCallBack) {
		this.mCallBack = mCallBack;
	}

	public interface OnTimeManagerCallBack {
		public void onDateClick(long time);
	}

	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_layout_common, null);
	}

	@Override
	public void onDateClick(String date, String time) {

	}

	@Override
	public void onDateClick(JsonArray mJsonArray, String unit) {
		this.changedUnitForPrice=unit;
		JsonObject mJsonObject = new JsonObject();
		this.mJsonArray = mJsonArray;
		mJsonObject.add("dayList", mJsonArray);
		mJsonObject.addProperty("spId", spId);
		mJsonObject.addProperty("unit", unit);
		showProgresssDialog();
		sendData(IspUpLoadJsonData.uploadTheTimeManagerList(mJsonObject,"weekSchedule"),
				AppDefine.BASE_URL_CHANGESCHEDULE, this, 102);
	
		
	}

}
