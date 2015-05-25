package com.zy.booking.fragments;

import java.util.Date;
import android.view.View;
import android.widget.LinearLayout;
import com.zy.booking.AppDefine;
import com.zy.booking.R;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.components.SwipListViewComponents.OnSwipCallBack;
import com.zy.booking.components.TimeBtweenComponents;
import com.zy.booking.components.TimeBtweenComponents.TimeBtweenCallBack;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.modle.SpAskedAdapter;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.TimeUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ViewInject;

public class CustromAskdHistroyFragment extends BaseFragment implements
		OnHttpActionListener,TimeBtweenCallBack {
	@ViewInject(R.id.headactionbar)
	View mHeadView;
	HeadLayoutComponents mLayoutComponents;

	@ViewInject(R.id.ll_container_body)
	LinearLayout mLinearBody;

	SwipListViewComponents mSwipeListView;

	JsonArray mJsonArray = new JsonArray();

	SpAskedAdapter mAdapter;
	
	@ViewInject(R.id.component_betweenTime)
	View mViewTimeBase;
	TimeBtweenComponents mTimeBtweenComponents;

	public void afterViewInject() {
		
		mTimeBtweenComponents = new TimeBtweenComponents(getActivity(), this);

		mTimeBtweenComponents.setFatherView(mViewTimeBase, this);
		mTimeBtweenComponents.initDate(System.currentTimeMillis(), System.currentTimeMillis(), true);
		mLayoutComponents = new HeadLayoutComponents(getActivity(), mHeadView);
		mLayoutComponents.setDefaultLeftCallBack(true);
		mLayoutComponents.setTextMiddle("历史", -1);

		mSwipeListView = new SwipListViewComponents(getActivity());
		mSwipeListView.setSwipCallBack(new OnSwipCallBack() {

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
		mLinearBody.addView(mSwipeListView.getView());

		mAdapter = new SpAskedAdapter(getActivity(), mJsonArray);
		mSwipeListView.setAdapter(mAdapter);

	}

	// 从网络上刷新数据
	private void requestIntentData() {
		showProgresssDialog();
		String dateStart = TimeUtils.getFormatDateTime(new Date(start), TimeUtils.yyyy_MM_dd);
		String dateEnd = TimeUtils.getFormatDateTime(new Date(end), TimeUtils.yyyy_MM_dd);
		sendData(UserUpLoadJsonData.getBookingServiceNameValuePairs(
				dateStart,dateEnd, "0", "10"),
				AppDefine.BASE_URL_QUERYBOOKING, this, 101);
	}
	

	@Override
	public void onStart() {
		super.onStart();
	}


	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
		mSwipeListView.onLoadOver();
	}

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {

		mSwipeListView.onLoadOver();
		dismissProgressDialog();
		if (mJsonResult != null) {
			JsonArray mArrayList = mJsonResult.getAsJsonArray("booking_list");
			if (mArrayList != null && mArrayList.size() > 0) {
				
				mJsonArray = mArrayList;
				mAdapter.changeDataSource(mJsonArray);
			}
		}
	}

	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_layout_beauarea, null);
	}

	long start,end;
	@Override
	public void onActionButCallBack(long start, long end) {
		this.start =start;
		this.end =end;
		requestIntentData() ;
	}

}
