package com.zy.booking.modle;



import com.zy.booking.R;

import com.zy.booking.util.ResourceUtil;
import com.zy.booking.util.ViewHolder;
import com.emsg.sdk.util.JsonUtil;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class TimeManagerAdapter extends SampleListAdapter {

	public final String AM = "am";
	public final String PM = "pm";
	public final String NIGHT = "night";
	public final String DAYLIST = "dayList";
	public final String WEEKDAY = "weekday";
	public final String DATE = "date";
	
	protected final  String HINTPRICE = "Price";
	protected final String HINTNUM = "Num";

	protected final String BOOKINGNUM = "BookingNum";
	
	protected final String UNIT = "unit";
	
	
	private String unit="";
	
	public void setpriceUnit(String unit){
		this.unit =unit;
	}
	

	public interface OnDateClickListener {
		public void onDateClick(JsonArray time);

		public void onDateClick(String date, String time);
		
		public void onDateClick(JsonArray time,String unit);

	}

	OnDateClickListener onDateClick;

	public void setOnDateClick(OnDateClickListener onDateClick) {
		this.onDateClick = onDateClick;
	}

	public TimeManagerAdapter(Context mContext, JsonArray mData) {
		super(mContext, mData);
	}

	@Override
	public View getView(int posi, View viewTemp, ViewGroup arg2) {

		if (viewTemp == null) {
			viewTemp = LayoutInflater.from(context).inflate(
					R.layout.item_layout_timeavailable, null);
		}
		TextView mTvPm = ViewHolder.get(viewTemp, R.id.tv_item_timeavail_pm);
		final TextView mTvDate = ViewHolder.get(viewTemp,
				R.id.tv_item_timeavail_date);
		TextView mTvAm = ViewHolder.get(viewTemp, R.id.tv_item_timeavail_am);

		TextView mNight = ViewHolder
				.get(viewTemp, R.id.tv_item_timeavail_night);
		TextView mWeek = ViewHolder.get(viewTemp, R.id.tv_item_timeavail_week);

		final JsonObject mJsonObj = (JsonObject) mListData.get(posi);

		final boolean isTvAmAvail = JsonUtil.getAsBoolean(mJsonObj, AM);
		final boolean isTvPmAvail = JsonUtil.getAsBoolean(mJsonObj, PM);
		final boolean isTvNightAvail = JsonUtil.getAsBoolean(mJsonObj, NIGHT);

		mTvAm.setBackgroundResource(isTvAmAvail ? R.drawable.icon_available
				: R.drawable.icon_unavailable);
		mTvPm.setBackgroundResource(isTvPmAvail ? R.drawable.icon_available
				: R.drawable.icon_unavailable);
		mNight.setBackgroundResource(isTvNightAvail ? R.drawable.icon_available
				: R.drawable.icon_unavailable);
		dealDate(mJsonObj, mTvDate, mWeek);
		
		final String showDate = mTvDate.getText().toString();
		dealAm(mJsonObj, mTvAm, showDate, isTvAmAvail);
		dealPm(mJsonObj, mTvPm, showDate, isTvPmAvail);
		dealNight(mJsonObj, mNight, showDate, isTvNightAvail);

		return viewTemp;
	}

	protected void dealDate(JsonObject mJsonObj, TextView mTvDate,TextView mWeek) {
		mWeek.setVisibility(View.GONE);
		final String date = JsonUtil.getAsString(mJsonObj, WEEKDAY);
		mTvDate.setText(date);
	}

	protected void dealAm(final JsonObject mJsonObj, TextView mTvAm,
			final String date, final boolean isTvAmAvail) {
		String price = JsonUtil.getAsString(mJsonObj, AM+HINTPRICE);
		if(!TextUtils.isEmpty(price)){
			mTvAm.setText(price+unit);
		}else{
			mTvAm.setText("未设定");
		}
		mTvAm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				showDialog(isTvAmAvail, date, mJsonObj, AM, mListData);
			}
		});
	}

	protected void dealPm(final JsonObject mJsonObj, TextView mTvPm,
			final String showDate, final boolean isTvPmAvail) {
		
		String price = JsonUtil.getAsString(mJsonObj, PM+HINTPRICE);
		if(!TextUtils.isEmpty(price)){
			mTvPm.setText(price+unit);
		}else{
			mTvPm.setText("未设定");
		}
		mTvPm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				showDialog(isTvPmAvail, showDate, mJsonObj, PM, mListData);
			}
		});

	}

	protected void dealNight(final JsonObject mJsonObj, TextView mNight,
			final String showDate, final boolean isTvNightAvail) {
		String price = JsonUtil.getAsString(mJsonObj, NIGHT+HINTPRICE);
		if(!TextUtils.isEmpty(price)){
			mNight.setText(price+unit);
		}else{
			mNight.setText("未设定");
		}
		mNight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				showDialog(isTvNightAvail, showDate, mJsonObj, NIGHT, mListData);
			}
		});
	}

	// 根据不同版本执行的条件不同
	protected void showDialog(final boolean isAvail, final String date,
			final JsonObject mJsonObject, final String time,
			final JsonArray mJsonArray) {

		String timeRes = context.getResources().getString(
				ResourceUtil.getStringId(time));
		String dialogtitle = context.getResources().getString(
				R.string.dialog_default_title);
		String btn1Tv = context.getResources().getString(
				R.string.dialog_default_bt1);
		String btn2Tv = context.getResources().getString(
				R.string.dialog_default_bt2);

		final NitifyPanelManager mNitifyPanel = new NitifyPanelManager(
				mJsonObject, time, isAvail);

		final NiftyDialogBuilder mBuilder = NiftyDialogBuilder
				.getInstance(context).withButton1Text(btn1Tv)
				.withEffect(Effectstype.Shake)
				.setCustomView(mNitifyPanel.dealNitifyPanelControl(), context)
				.withButton2Text(btn2Tv).withEffect(Effectstype.Shake)
				.withTitle(date + timeRes);
		mBuilder.setButton1Click(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				// mJsonObject.addProperty(time, !isAvail);
				if (onDateClick != null) {
					mNitifyPanel.makeSelectedData();
					onDateClick.onDateClick(mListData,mNitifyPanel.getUnitHint());
				}

				mBuilder.dismiss();
			}
		});

		mBuilder.setButton2Click(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				mBuilder.dismiss();
			}
		});
		try {
			mBuilder.show();
		} catch (Exception e) {
		}
	}

	
	protected void dealVisableOperateAll(View mViewAllState,View mViewAllData){
		
	}
	
    protected void dealPriceHintAvailable(JsonObject mJsonItem , EditText mEditText){
    	mEditText.setEnabled(true);
    	mEditText.setText(unit);
	}
	
	/**
	 * 用于管理对单个弹出框的数据整合
	 * 
	 * 
	 * */
	class NitifyPanelManager {

		boolean isApplyPriceAll = false;
		boolean isApplayOpenStateAll = false;
		boolean isOpenService = false;

		EditText mEtPrice;
		EditText mEtServiceNum;
		EditText mUnitPrice;

		JsonObject mJsonObject;

		String hintTime;

		
		public String getUnitHint(){
			return mUnitPrice.getText().toString();
		}

		public NitifyPanelManager(JsonObject mJsonObject, String hintTime,
				boolean mCurrentStatus) {
			this.hintTime = hintTime;
			this.isOpenService = mCurrentStatus;
			this.mJsonObject = mJsonObject;
		}

		public View dealNitifyPanelControl() {
			View mViewBody = LayoutInflater.from(context).inflate(
					R.layout.layout_sechlude_priceinfo, null);
			// .withMessage(
			// "确定将" + date + "的" + timeRes + "服务时间"
			// + (isAvail ? "停止" : "开放" + "?"))

			final View mViewDataLayout = mViewBody
					.findViewById(R.id.ll_body_data);
			if (!isOpenService)
				mViewDataLayout.setVisibility(View.GONE);
			// 服务状态控制
			RadioGroup mRadioGroupOpenStatus = (RadioGroup) mViewBody
					.findViewById(R.id.rg_apply_openstatus);

			View mHintStatus =  mViewBody
					.findViewById(R.id.ll_hint_apply);
			
			View mHintStatusData = mViewBody
					.findViewById(R.id.ll_hint_apply_data);
			
			if (isOpenService) {
				((RadioButton) mRadioGroupOpenStatus.getChildAt(0))
						.setChecked(true);
			} else {
				((RadioButton) mRadioGroupOpenStatus.getChildAt(1))
						.setChecked(true);
			}
			mRadioGroupOpenStatus
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup arg0,
								int mCheckPosi) {
							
							if (mCheckPosi == R.id.rb_state_available) {
								isOpenService = true;
								mViewDataLayout.setVisibility(View.VISIBLE);
							}
							else {
								isOpenService = false;
								mViewDataLayout.setVisibility(View.GONE);
							}
						}
					});

			// 服务状态应用于控制
			RadioGroup mRadioGroupApplyStatus = (RadioGroup) mViewBody
					.findViewById(R.id.rg_apply_status);
			((RadioButton) mRadioGroupApplyStatus.getChildAt(0))
					.setChecked(true);
			mRadioGroupApplyStatus
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup arg0,
								int mCheckPosi) {
							if (mCheckPosi ==R.id.rb_apply_current)
								isApplayOpenStateAll = false;
							else {
								isApplayOpenStateAll = true;
							}
						}
					});

			// 服务的基础数据控制
			RadioGroup mRadioGroupApplyData = (RadioGroup) mViewBody
					.findViewById(R.id.rg_apply_data);
			((RadioButton) mRadioGroupApplyData.getChildAt(0)).setChecked(true);
			mRadioGroupApplyData
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup arg0,
								int mPosition) {
							if (mPosition == R.id.rb_apply_current_data)
								isApplyPriceAll = false;
							else {
								isApplyPriceAll = true;
							}
						}
					});

			String mJsonPrice = JsonUtil.getAsString(mJsonObject, hintTime
					+ HINTPRICE);
			String mJsonNum = JsonUtil.getAsString(mJsonObject, hintTime
					+ HINTNUM);

			mEtPrice = (EditText) mViewBody
					.findViewById(R.id.et_sechlude_changeprice);
			
			mUnitPrice = (EditText) mViewBody
			.findViewById(R.id.et_sechlude_changeunit);
			

			if (!TextUtils.isEmpty(mJsonPrice)) {
				mEtPrice.setText(mJsonPrice);
				
			}
			
			dealPriceHintAvailable(mJsonObject,mUnitPrice);
			
			mEtServiceNum = (EditText) mViewBody
					.findViewById(R.id.et_sechlude_changenum);

			if (!TextUtils.isEmpty(mJsonNum)) {
				mEtServiceNum.setText(mJsonNum);
			}
			
			dealVisableOperateAll(mHintStatus,mHintStatusData);
			return mViewBody;
		}

		public void makeSelectedData() {
			String price = mEtPrice.getText().toString();
			String num = mEtServiceNum.getText().toString();


			if (!isApplyPriceAll && !isApplayOpenStateAll) {
				mJsonObject.addProperty(hintTime, isOpenService);
				mJsonObject.addProperty(hintTime+HINTPRICE, price == null ? "" : price);
				mJsonObject.addProperty(hintTime+HINTNUM, num == null ? "" : num);
			} else {
				for (int i = 0; i < mListData.size(); i++) {
					JsonObject mJsonObject = (JsonObject) mListData.get(i);
					if (isApplayOpenStateAll)
						mJsonObject.addProperty(hintTime, isOpenService);

					if (isApplyPriceAll) {
						mJsonObject.addProperty(hintTime+HINTPRICE, price == null ? ""
								: price);
						mJsonObject
								.addProperty(hintTime+HINTNUM, num == null ? "" : num);
					}
				}
			}
		}
	}

}
