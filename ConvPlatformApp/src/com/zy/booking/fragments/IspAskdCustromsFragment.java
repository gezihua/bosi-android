package com.zy.booking.fragments;

import java.util.Date;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zy.booking.AppDefine;
import com.zy.booking.R;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.components.SwipListViewComponents.OnSwipCallBack;
import com.zy.booking.components.TimeBtweenComponents;
import com.zy.booking.components.TimeBtweenComponents.TimeBtweenCallBack;
import com.zy.booking.json.IspUpLoadJsonData;
import com.zy.booking.modle.SpAskedAdapter;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.TimeUtils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ViewInject;

public class IspAskdCustromsFragment extends BaseFragment implements
		OnHttpActionListener, TimeBtweenCallBack {

	@ViewInject(R.id.ll_fragmentispsearchbody)
	LinearLayout mLinearBody;

	SwipListViewComponents mListView;

	DatePickerDialog datePickerDialog;

	SpAskedAdapter mSearchAdapter;
	JsonArray mJsonArray = new JsonArray();

	@ViewInject(R.id.component_betweenTime)
	View mViewComponent;
	TimeBtweenComponents mTimeBtweenComponents;
	private String serviceId;

	public void setServiceId(String mServiceId) {
		this.serviceId = mServiceId;
	}

	private void filterSearch() {
		showProgresssDialog();
		searchListData(TimeUtils.getFormatDateTime(new Date(start),
				TimeUtils.yyyy_MM_dd),

		TimeUtils.getFormatDateTime(new Date(end), TimeUtils.yyyy_MM_dd));
	}

	private void searchListData(String startTime, String endTime) {
		sendData(IspUpLoadJsonData.getBookingUsersNameValuePairs(startTime,
				endTime, "0", "10", serviceId), AppDefine.BASE_URL_ISPSEARCH,
				this, 101);
	}
	
	
	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
		dismissProgressDialog();
		mListView.onLoadOver();
	}

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		dismissProgressDialog();
		mListView.onLoadOver();
		JsonArray mListsData = mJsonResult.getAsJsonArray("booking_list");
		if (isSuccess && mListsData != null&&mListsData.size()>0) {
			mSearchAdapter.changeDataSource(mListsData);
		}else{
			Toast.makeText(getActivity(), "未查询到预约客户！", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_ispsearch, null);
	}

	@Override
	void afterViewInject() {

		mListView = new SwipListViewComponents(getActivity());
		mSearchAdapter = new SpAskedAdapter(getActivity(), mJsonArray);
		mListView.setAdapter(mSearchAdapter);
		mLinearBody.addView(mListView.getView());

		mTimeBtweenComponents = new TimeBtweenComponents(getActivity(), this);
		mTimeBtweenComponents.setFatherView(mViewComponent, this);
		
		
		mTimeBtweenComponents.initDate(0, 0,true);
		mListView.setSwipCallBack(new OnSwipCallBack() {

			@Override
			public void onReflesh() {
				mTimeBtweenComponents.filterSearch();
			}

			@Override
			public void onLoadMore() {

			}

			@Override
			public void onItemClickListener(int position) {

			}
		});

	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	long start, end;
	@Override
	public void onActionButCallBack(long start, long end) {
		this.start = start;
		this.end = end;
		filterSearch();
	}

}
