package com.zy.booking.components;

import java.sql.Date;
import java.util.Calendar;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.util.TimeUtils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class TimeBtweenComponents extends BaseComponents implements
		OnDateSetListener, OnClickListener {

	@ViewInject(R.id.bt_fragmentispsearch_startdate)
	Button mButtonStart;
	@ViewInject(R.id.bt_fragmentispsearch_enddate)
	Button mButtonEnd;
	@ViewInject(R.id.bt_fragmentispsearch_search)
	Button mButtonSearch;

	final Calendar calendar = Calendar.getInstance();
	public static final String DATEPICKER_TAG = "datepicker";
	Fragment mFragment;

	public TimeBtweenComponents(Context mContext) {
		super(mContext);

		datePickerDialog = DatePickerDialog.newInstance(this,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), true);
	}

	public TimeBtweenComponents(Context mContext, Fragment mFragment) {
		super(mContext);
		datePickerDialog = DatePickerDialog.newInstance(this,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), true);
		this.mFragment = mFragment;
	}
	
	public void initDate(long start ,long endData,boolean isAction){
		
		long mStart = start==0?System.currentTimeMillis() :start ;
		long mEnd  = endData ==0 ?System.currentTimeMillis()+7*60*60*24*1000:endData;
		
		String mStartDate = TimeUtils.getFormatDateTime(new Date(mStart), TimeUtils.yyyy_MM_dd);
		String mEndDate = TimeUtils.getFormatDateTime(new Date(mEnd), TimeUtils.yyyy_MM_dd);
		
		
		mButtonStart.setText(mStartDate);
		mButtonStart.setTag(mStart);
		mButtonEnd.setText(mEndDate);
		mButtonEnd.setTag(mEnd);
		
		if(isAction)
		filterSearch();
	}

	TimeBtweenCallBack mCallback;

	public void setFatherView(View mBaseView, TimeBtweenCallBack mCallback) {
		this.mFatherView = mBaseView;
		mBaseView.setVisibility(View.VISIBLE);
		ViewUtils.inject(this, mBaseView);

		mButtonEnd.setOnClickListener(this);
		mButtonStart.setOnClickListener(this);
		mButtonSearch.setOnClickListener(this);
		this.mCallback = mCallback;
	}

	//用于点击按钮时的回调
	public interface TimeBtweenCallBack {
		public void onActionButCallBack(long start, long end);
	}

	public boolean filterSearch() {

		if (TextUtils.isEmpty(mButtonStart.getText())) {
			CpApplication.getApplication().playYoYo(mButtonStart);
			return false;
		}

		if (TextUtils.isEmpty(mButtonEnd.getText())) {
			CpApplication.getApplication().playYoYo(mButtonEnd);
			return false;
		}
		long start = (Long) mButtonStart.getTag();
		long end = (Long) mButtonEnd.getTag();
		if (start > end) {
			Toast.makeText(mContext, "开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (mCallback != null) {
			mCallback.onActionButCallBack(start, end);
		}
        return true;
	}

	@Override
	public void initFatherView() {
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, String year,
			String month, String day, Calendar mcalander) {

		int tag = datePickerDialog.getSId();

		String dateResult = TimeUtils.getFormatDateTime(new Date(mcalander.getTimeInMillis()), TimeUtils.yyyy_MM_dd);

		if (tag == 101) {
			mButtonStart.setText(dateResult);
			mButtonStart.setTag(mcalander.getTimeInMillis());
		} else if (tag == 102) {
			mButtonEnd.setText(dateResult);
			mButtonEnd.setTag(mcalander.getTimeInMillis());
		}

	}
	
	public Button getActionButton(){
		return mButtonSearch;
	}

	DatePickerDialog datePickerDialog;

	private void showDateDialog(int Tag) {
		datePickerDialog.setId(Tag);
		datePickerDialog.setYearRange(2014, 2015);

		if (mFragment != null) {
			datePickerDialog.show(mFragment.getFragmentManager(),
					DATEPICKER_TAG);
		} else {
			if (mContext instanceof FragmentActivity) {
				FragmentActivity mFragmentActivity = (FragmentActivity) mContext;
				datePickerDialog.show(
						mFragmentActivity.getSupportFragmentManager(),
						DATEPICKER_TAG);
			}
		}
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

}
