package com.zy.booking.activitys;

import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.components.SwipListViewComponents.OnSwipCallBack;
import com.zy.booking.db.HistoryDbBean;
import com.zy.booking.json.IspUpLoadJsonData;
import com.zy.booking.modle.ServicesListAdapter;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.DataTools;

@ContentView(R.layout.fragment_layout_beauarea)
public class MySendedServiceAcitivity extends BaseActivity implements
		OnHttpActionListener {
	@ViewInject(R.id.headactionbar)
	View mHeadView;
	HeadLayoutComponents mLayoutComponents;

	@ViewInject(R.id.ll_container_body)
	LinearLayout mLinearBody;

	SwipListViewComponents mSwipeListView;

	JsonArray mJsonArray = new JsonArray();

	ServicesListAdapter mAdapter;

	public void afterViewInject() {
		mLayoutComponents = new HeadLayoutComponents(this, mHeadView);
		mLayoutComponents.setTextMiddle("我的服务", -1);
		mLayoutComponents.setDefaultLeftCallBack(true);
		mLayoutComponents.setTextRight("", R.drawable.act_writ);
		mLayoutComponents.setRightOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				Intent intent = new Intent(mContext, NewActActivity.class);
				mContext.startActivity(intent);
			}
		});

		mSwipeListView = new SwipListViewComponents(this);
		mSwipeListView.setSwipCallBack(new OnSwipCallBack() {

			@Override
			public void onReflesh() {
				requestIntentData();
			}

			@Override
			public void onLoadMore() {

			}

			@Override
			public void onItemClickListener(int position) {
				JsonObject mjsonData = (JsonObject) mJsonArray.get(position);
				Intent mIntent = new Intent(mContext,
						SampleHolderActivity.class);
				mIntent.putExtra("id", JsonUtil.getAsString(mjsonData, "id"));
				mIntent.putExtra("tag", SampleHolderActivity.TAG_ISPFUNS);
				startActivity(mIntent);
			}
		});
		mLinearBody.addView(mSwipeListView.getView());

		mAdapter = new ServicesListAdapter(this, mJsonArray);
		mSwipeListView.setAdapter(mAdapter);

		
		
		LinearLayout mLinearLayout = new LinearLayout(this);
		mLinearLayout.setGravity(Gravity.CENTER);
		
		Button mButtonSendService = new Button(this);
		mButtonSendService.setText("我 要 发 布");
		mButtonSendService.setTextColor(Color.WHITE);
		mButtonSendService.setBackgroundResource(R.drawable.selector_btn);
		mButtonSendService.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext, NewActActivity.class);
				mContext.startActivity(intent);
			}
		});
		
		LayoutParams mLayoutParams = new LayoutParams(DataTools.dip2px(this, 300),LayoutParams.WRAP_CONTENT);
		mButtonSendService.setLayoutParams(mLayoutParams);
		mLinearLayout.addView(mButtonSendService);
		mSwipeListView.getListView().addFooterView(mLinearLayout);
		
		
	}
	

	// 从网络上刷新数据
	private void requestIntentData() {
		mSwipeListView.loading();
		sendData(IspUpLoadJsonData.getSendedServicesNameValuePair(mJsonArray.size()+"","10"),
				AppDefine.BASE_URL_SEARCHBYISUSER, this, 101);
	}

	@Override
	public void onStart() {
		super.onStart();
		requestIntentData();
		// loadDataFromLoacl();
	}

	// public void loadDataFromLoacl() {
	// mJsonArray = new JsonArray();
	// ArrayList<HistoryDbBean> lists =
	// CpApplication.getApplication().mDbManager
	// .getAskedList();
	// if (lists == null)
	// return;
	// for (HistoryDbBean mdbBean : lists) {
	// mJsonArray.add(JsonUtil.parse(mdbBean.contect));
	// }
	// mAdapter.changeDataSource(mJsonArray);
	//
	// mSwipeListView.onLoadOver();
	// }

	public void insertToHistoryDb(JsonArray mLists) {
		if (mLists == null || mLists.size() == 0)
			return;
		CpApplication.getApplication().mDbManager.deleteAllService();
		for (int i = 0; i < mLists.size(); i++) {
			JsonObject mJson = (JsonObject) mLists.get(i);
			String id = JsonUtil.getAsString(mJson, "id");
			String isAsked = "1";
			HistoryDbBean mDbBean = new HistoryDbBean();
			mDbBean.contect = mJson.toString();
			mDbBean.serviceId = id;
			mDbBean.isAskd = isAsked;
			CpApplication.getApplication().mDbManager
					.insertHistoryData(mDbBean);
		}
	}

	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		afterViewInject();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
		mSwipeListView.onLoadOver();
	}

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		mSwipeListView.onLoadOver();
		if (mLists != null) {
			CpApplication.getApplication().mDbManager.deleteAllService();
			//insertToHistoryDb(mLists);
			mJsonArray = mLists;
			mAdapter.changeDataSource(mJsonArray);
		}
	}

}
