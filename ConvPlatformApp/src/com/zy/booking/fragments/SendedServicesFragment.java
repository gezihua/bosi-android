package com.zy.booking.fragments;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.zy.booking.AppDefine;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.activitys.NewActActivity;
import com.zy.booking.activitys.SampleHolderActivity;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.components.SwipListViewComponents.OnSwipCallBack;
import com.zy.booking.db.HistoryDbBean;
import com.zy.booking.json.IspUpLoadJsonData;
import com.zy.booking.modle.ServicesListAdapter;
import com.zy.booking.struct.OnHttpActionListener;
import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SendedServicesFragment extends BaseFragment implements
		OnHttpActionListener {
	@ViewInject(R.id.headactionbar)
	View mHeadView;
	HeadLayoutComponents mLayoutComponents;

	@ViewInject(R.id.ll_container_body)
	LinearLayout mLinearBody;

	SwipListViewComponents mSwipeListView;

	JsonArray mJsonArray = new JsonArray();

	ServicesListAdapter mAdapter;
	
	int begin;
	int size ;

	public void afterViewInject() {
		mLayoutComponents = new HeadLayoutComponents(getActivity(), mHeadView);
		mLayoutComponents.setTextMiddle("发布历史", -1);

		mLayoutComponents.setTextRight("", R.drawable.act_writ);
		mLayoutComponents.setRightOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				Intent intent = new Intent(getActivity(), NewActActivity.class);
				getActivity().startActivity(intent);
			}
		});

		mSwipeListView = new SwipListViewComponents(getActivity());
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
				Intent mIntent = new Intent(getActivity(),
						SampleHolderActivity.class);
				mIntent.putExtra("id", JsonUtil.getAsString(mjsonData, "id"));
				mIntent.putExtra("tag", SampleHolderActivity.TAG_ISPFUNS);
				startActivity(mIntent);
			}
		});
		mLinearBody.addView(mSwipeListView.getView());

		mAdapter = new ServicesListAdapter(getActivity(), mJsonArray);
		mSwipeListView.setAdapter(mAdapter);
		
	}

	// 从网络上刷新数据
	private void requestIntentData() {
		sendData(IspUpLoadJsonData.getSendedServicesNameValuePair(mJsonArray.size()+"",10+""),
				AppDefine.BASE_URL_SEARCHBYISUSER, this, 101);
	}

	@Override
	public void onStart() {
		super.onStart();
		requestIntentData();
		//loadDataFromLoacl();
	}

//	public void loadDataFromLoacl() {
//		mJsonArray = new JsonArray();
//		ArrayList<HistoryDbBean> lists = CpApplication.getApplication().mDbManager
//				.getAskedList();
//		if (lists == null)
//			return;
//		for (HistoryDbBean mdbBean : lists) {
//			mJsonArray.add(JsonUtil.parse(mdbBean.contect));
//		}
//		mAdapter.changeDataSource(mJsonArray);
//
//		mSwipeListView.onLoadOver();
//	}

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
	public void onHttpError(Exception e, String msg, int requestCode) {
		mSwipeListView.onLoadOver();
	}

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		mSwipeListView.onLoadOver();
		if (mLists != null) {
			CpApplication.getApplication().mDbManager.deleteAllService();
			insertToHistoryDb(mLists);
			mJsonArray.addAll(mLists);
			mAdapter.changeDataSource(mJsonArray);
			mSwipeListView.getListView().scrollBy(0,mSwipeListView.getListView().getHeight() );
		}
	}

	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_layout_beauarea, null);
	}

}
