package com.zy.booking.activitys;

import java.util.Calendar;



import java.util.Date;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zy.booking.BaseActivity;
import com.zy.booking.R;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.components.SwipListViewComponents.OnSwipCallBack;
import com.zy.booking.modle.SpAskedAdapter;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.TimeUtils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.fragment_ispsearch)
public class IspAskedCustromsActivity extends BaseActivity implements
		OnDateSetListener, OnClickListener, OnHttpActionListener {

	HeadLayoutComponents mActionBar;
	@ViewInject(R.id.headactionbar)
	View mActionBarBase;
	@ViewInject(R.id.ll_fragmentispsearchbody)
	LinearLayout mLinearBody;

	SwipListViewComponents mListView;

	DatePickerDialog datePickerDialog;
	@ViewInject(R.id.bt_fragmentispsearch_startdate)
	Button mButtonStart;
	@ViewInject(R.id.bt_fragmentispsearch_enddate)
	Button mButtonEnd;
	@ViewInject(R.id.bt_fragmentispsearch_search)
	Button mButtonSearch;

	final Calendar calendar = Calendar.getInstance();
	public static final String DATEPICKER_TAG = "datepicker";

	SpAskedAdapter mSearchAdapter;
	JsonArray mJsonArray = new JsonArray();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActionBar = new HeadLayoutComponents(this, mActionBarBase);
		mActionBar.setDefaultLeftCallBack(true);
		mActionBar.setTextMiddle("预约查询", -1);

		mButtonEnd.setOnClickListener(this);
		mButtonStart.setOnClickListener(this);
		mButtonSearch.setOnClickListener(this);

		datePickerDialog = DatePickerDialog.newInstance(this,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), true);

		mListView = new SwipListViewComponents(this);
		mSearchAdapter = new SpAskedAdapter(this, mJsonArray);
		mListView.setAdapter(mSearchAdapter);
		mLinearBody.addView(mListView.getView());

		mListView.setSwipCallBack(new OnSwipCallBack() {

			@Override
			public void onReflesh() {
				filterSearch();
			}

			@Override
			public void onLoadMore() {

			}

			@Override
			public void onItemClickListener(int position) {

			}
		});

		
		// for add the default search
		
		long time = getIntent().getExtras().getLong("time");
		if(time<=0)time =System.currentTimeMillis();
		serviceId = getIntent().getExtras().getString("serviceId");
		init(time);

	}
	
	private String serviceId;

	public void init(long time) {
		Date mDateNow = new Date(time);
		String textDateNow = TimeUtils.getFormatDateTime(mDateNow,
				TimeUtils.yyyy_MM_dd);
		mButtonStart.setText(textDateNow);
		mButtonEnd.setText(textDateNow);
		mButtonStart.setTag(time);
		mButtonEnd.setTag(time);
		filterSearch();
	}

	private void filterSearch() {
		long start = (Long) mButtonStart.getTag();
		long end = (Long) mButtonEnd.getTag();
		if (start > end) {
			Toast.makeText(this, "开始时间不能大于结束时间", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		showProgresssDialog();

		searchListData(start, end);
	}

	private void searchListData(long startTime, Long endTime) {
//		sendData(UpLoadJsonData.getRequestUsers(startTime, endTime,serviceId).toString(),
//				AppDefine.BASE_URL_ISPSEARCH, this, 101);
	}

	private void showDateDialog(int Tag) {
		datePickerDialog.setId(Tag);
		datePickerDialog.setYearRange(2014, 2015);
		datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_fragmentispsearch_startdate:
			showDateDialog(101);
			break;
		case R.id.bt_fragmentispsearch_enddate:
			showDateDialog(102);
			break;
		case R.id.bt_fragmentispsearch_search:
			filterSearch();
			break;
		}
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, String year,
			String month, String day, Calendar mcalander) {
		int tag = datePickerDialog.getSId();

		String dateResult = year + "-" + month + "-" + day;

		if (tag == 101) {
			mButtonStart.setText(dateResult);
			mButtonStart.setTag(mcalander.getTimeInMillis());
		} else if (tag == 102) {
			mButtonEnd.setText(dateResult);
			mButtonEnd.setTag(mcalander.getTimeInMillis());
		}

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
		if (isSuccess&&mLists!=null){
			
			mSearchAdapter.changeDataSource(mLists);
		}
			
	}

}
