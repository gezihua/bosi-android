package com.zy.booking.fragments;


import android.view.View;
import android.widget.LinearLayout;
import com.zy.booking.R;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.components.SwipListViewComponents.OnSwipCallBack;
import com.zy.booking.modle.ServicesListAdapter;
import com.google.gson.JsonArray;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RequestServiceFragment extends BaseFragment {

	@ViewInject(R.id.ll_container_body)
	LinearLayout mLayoutBody;

	SwipListViewComponents mSwipListViewComponets;

	ServicesListAdapter mListAdapter;
	JsonArray mJsonData = new JsonArray();

	public void afterViewInject() {
		mSwipListViewComponets = new SwipListViewComponents(
				getActivity());
		mLayoutBody.addView(mSwipListViewComponets.getView());
		mListAdapter = new ServicesListAdapter(getActivity(), mJsonData);
		mSwipListViewComponets.setAdapter(mListAdapter);
		loadData();
		mSwipListViewComponets.setSwipCallBack(new OnSwipCallBack() {

			@Override
			public void onReflesh() {
				loadData();
			}

			@Override
			public void onLoadMore() {
			}

			@Override
			public void onItemClickListener(int position) {

			}
		});
	}

	private void loadData() {/*
		mSwipListViewComponets.loading();
		CpApplication.getApplication().mHttpPack.sendData(UpLoadJsonData
				.getHeadData().toString(), AppDefine.BASE_URL_ACTAVAIL,
				new OnHttpActionCallBack() {

					@Override
					public void onHttpSuccess(String result) {
						mSwipListViewComponets.onLoadOver();
						DecodeResult.decoResult(result, new IDecodeJson() {

							@Override
							public void onDecoded(String reason,
									boolean isSuccess, JsonObject mJsonResult,
									JsonArray mLists) {
								mListAdapter.changeDataSource(mLists);
							}
						});

					}

					@Override
					public void onHttpError(HttpException e, String messge) {
						mSwipListViewComponets.onLoadOver();
					}

				});
	*/}

	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_layout_requestservice, null);
	}

}
