package com.zy.booking.fragments;

import java.util.Date;

import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zy.booking.AppDefine;
import com.zy.booking.R;
import com.zy.booking.components.CategorysGirdComponents;
import com.zy.booking.components.TimeBtweenComponents;
import com.zy.booking.components.TimeBtweenComponents.TimeBtweenCallBack;
import com.zy.booking.json.IspUpLoadJsonData;
import com.zy.booking.modle.IspOpenAvailDateAdapter;
import com.zy.booking.modle.TimeManagerAdapter.OnDateClickListener;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.TimeUtils;
import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ViewInject;

public class IspOpenDateFragment extends BaseFragment implements
		OnHttpActionListener, TimeBtweenCallBack, OnDateClickListener {


	@ViewInject(R.id.component_betweenTime)
	View mViewTimeSerach;
	TimeBtweenComponents mTimeSerachCompoents;

	@ViewInject(R.id.ll_fragmentispsearchbody)
	LinearLayout mLayoutBody;

	private String mServiceId;

	public void setServiceId(String serviceId) {
		this.mServiceId = serviceId;
	}

	private final int CODE_REQUEST_OPENDATE = 101;

	private final int CODE_REQUEST_GETOPENEDDATE = 102;

	private final int CODE_REQUEST_CHANGOPENEDATE = 103;

	private void filterSearch() {

		searchListData(TimeUtils.getFormatDateTime(new Date(start),
				TimeUtils.yyyy_MM_dd), TimeUtils.getFormatDateTime(
				new Date(end), TimeUtils.yyyy_MM_dd));
	}

	private void searchListData(String startTime, String endTime) {
		showProgresssDialog();
		sendData(IspUpLoadJsonData.getOpenDateListPairs(startTime, endTime,
				mServiceId), AppDefine.BASE_URL_OPENDATE, this,
				CODE_REQUEST_OPENDATE);
	}

	private void getOpenDate() {
		showProgresssDialog();
		sendData(IspUpLoadJsonData.getOpenedDateListPairs(mServiceId),
				AppDefine.URL_GETDATE_OPENED, this, CODE_REQUEST_GETOPENEDDATE);
	}

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
		dismissProgressDialog();
	}

	
	private String mPriceHine = "";
	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
         dismissProgressDialog();
		if (!isSuccess)
			return;
		if (resultCode == CODE_REQUEST_OPENDATE) {
			Toast.makeText(getActivity(), "服务时间开放成功", Toast.LENGTH_SHORT)
					.show();
			getOpenDate();
		} else if (resultCode == CODE_REQUEST_GETOPENEDDATE) {

			if (mJsonResult == null)
				return;
			JsonObject mJsonData = JsonUtil.getAsJsonObject(mJsonResult,
					"date_open");

			String unit = JsonUtil.getAsString(mJsonData,
					"unit");
			
			if (mJsonData == null)
				return;
			JsonArray mJsonDayList = JsonUtil.getAsJsonArray(mJsonData,
					"dateList");

			dismissProgressDialog();
			if (isSuccess) {
				mJsonArray = mJsonDayList;
				mAdapter.setpriceUnit(unit);
				mAdapter.changeDataSource(mJsonArray);
			}
		}else if(resultCode == CODE_REQUEST_CHANGOPENEDATE){
			mAdapter.setpriceUnit(mPriceHine);
			mAdapter.notifyDataSetChanged();
		}

		dismissProgressDialog();
	}

	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_ispsearch, null);
	}

	CategorysGirdComponents mGridComponets;
	IspOpenAvailDateAdapter mAdapter;
	JsonArray mJsonArray = new JsonArray();

	@Override
	void afterViewInject() {

		mTimeSerachCompoents = new TimeBtweenComponents(getActivity(), this);
		mTimeSerachCompoents.setFatherView(mViewTimeSerach, this);

		mTimeSerachCompoents.initDate(0, 0, false);
		mTimeSerachCompoents.getActionButton().setText("开 放 ");
		initCategorysGird();

		getOpenDate();
	}

	private void initCategorysGird() {
		mGridComponets = new CategorysGirdComponents( getActivity(),new GridView(getActivity()));
		mGridComponets.setVerNumber(1);
		mAdapter = new IspOpenAvailDateAdapter(getActivity(), mJsonArray);
		mAdapter.setOnDateClick(this);
		mGridComponets.setAdapter(mAdapter);

		mLayoutBody.addView(mGridComponets.getView());
	}

	long start, end;

	@Override
	public void onActionButCallBack(long start, long end) {
		this.start = start;
		this.end = end;
		filterSearch();
	}

	@Override
	public void onDateClick(JsonArray mJsonArray) {
		JsonObject mJsonObject = new JsonObject();
		this.mJsonArray = mJsonArray;
		mJsonObject.add("dateList", mJsonArray);
		mJsonObject.addProperty("spId", mServiceId);
		showProgresssDialog();
		sendData(IspUpLoadJsonData.uploadTheTimeManagerList(mJsonObject,"openDate"),
				AppDefine.URL_CHANGE_OPENDATE, this,
				CODE_REQUEST_CHANGOPENEDATE);
	}

	@Override
	public void onDateClick(String date, String time) {

	}

	@Override
	public void onDateClick(JsonArray mJsonArray, String unit) {
		this.mPriceHine = unit;
		JsonObject mJsonObject = new JsonObject();
		this.mJsonArray = mJsonArray;
		mJsonObject.add("dateList", mJsonArray);
		mJsonObject.addProperty("spId", mServiceId);
		showProgresssDialog();
		sendData(IspUpLoadJsonData.uploadTheTimeManagerList(mJsonObject,"openDate"),
				AppDefine.URL_CHANGE_OPENDATE, this,
				CODE_REQUEST_CHANGOPENEDATE);
	
	}

}
