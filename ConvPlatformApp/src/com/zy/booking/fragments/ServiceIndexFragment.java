package com.zy.booking.fragments;

import android.content.Intent;



import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zy.booking.AppDefine;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.activitys.IndexActivity;
import com.zy.booking.activitys.SearchActivity;
import com.zy.booking.activitys.ServiceDetailActivity;
import com.zy.booking.components.SliderImagePageIndicator;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.components.SwipListViewComponents.OnSwipCallBack;
import com.zy.booking.control.LocationCtrol.OnLocationChangedListener;
import com.zy.booking.db.HistoryDbBean;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.modle.ServicesListAdapter;
import com.zy.booking.struct.OnHttpActionListener;

public class ServiceIndexFragment extends BaseFragment implements
		 OnHttpActionListener ,OnLocationChangedListener{

	@ViewInject(R.id.ll_container_categorys)
	public LinearLayout mcategorsdisplay;

	@ViewInject(R.id.ll_container_lists)
	public LinearLayout mSwipDataDisplay;

	@ViewInject(R.id.ll_head_left)
	public LinearLayout mlayoutHeadLeft;

	@ViewInject(R.id.tv_headleft)
	TextView mTvHeadLeft;

	@ViewInject(R.id.main_head_fresh)
	ProgressBar mProgressRefresh;

	@ViewInject(R.id.et_headviewsearch)
	public TextView mEtSearch;

	SliderImagePageIndicator mSliderImagePageIndicator;
	SwipListViewComponents mSwipListViewComponents;

	ServicesListAdapter mListAdapter;

	JsonArray mJsonArray;

	String[] mArrayMenu;

	

	String lng, lat;

	public void init() {
		mArrayMenu = getActivity().getString(R.string.index_menu_names).split(
				"#");
		mEtSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SearchActivity.class);
				
				intent.putExtra(SearchActivity.TAG_CATEGORYS_LNG, lng);
				
				intent.putExtra(SearchActivity.TAG_CATEGORYS_LAT, lat);
				startActivity(intent);
				getActivity().overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
		});
		

	}
	
	@OnClick(R.id.tv_headleft)
	public void actionHeadLeft(View mView){
		mProgressRefresh.setVisibility(View.VISIBLE);
		((IndexActivity)getActivity()).requestLocation();
	}

	public void afterViewInject() {
		init();

		mSliderImagePageIndicator = new SliderImagePageIndicator(inflater,
				getActivity());


		mSwipListViewComponents = new SwipListViewComponents(getActivity());
		mSwipListViewComponents.isLoadBottomAuto(true);
		
		mSwipListViewComponents.getListView().addHeaderView(mSliderImagePageIndicator.getView());
		
		
		mSwipDataDisplay.addView(mSwipListViewComponents.getView());

		mJsonArray = new JsonArray();
		mListAdapter = new ServicesListAdapter(getActivity(), mJsonArray);
		mSwipListViewComponents.setAdapter(mListAdapter);

		mSwipListViewComponents.setSwipCallBack(new OnSwipCallBack() {

			@Override
			public void onReflesh() {
				loadData(true);
			}

			@Override
			public void onLoadMore() {
				if(!isEmptyData)
				loadData(true);
			}

			@Override
			public void onItemClickListener(int position) {
				Intent mIntnet = new Intent(getActivity(),
						ServiceDetailActivity.class);
				JsonObject mJsonObj = (JsonObject) mJsonArray.get(position-1);
				String serviceId = JsonUtil.getAsString(mJsonObj, "id");
				mIntnet.putExtra("id", serviceId);

				HistoryDbBean mHistoryBean = new HistoryDbBean();
				mHistoryBean.contect = mJsonObj.toString();
				mHistoryBean.serviceId = serviceId;
				CpApplication.getApplication().mDbManager
						.insertHistoryData(mHistoryBean);

				getActivity().startActivity(mIntnet);
			}
		});

		loadData(true);
	}

	private void attchToActivity(Intent mIntent) {
		getActivity().startActivity(mIntent);
	}

	public void loadData(final boolean isReflash) {
		mSwipListViewComponents.loading();

		sendData(UserUpLoadJsonData.getDefaultProviderNamePairs(mJsonArray.size(), 10, "", "",
				null, null, null, null, null, null),
				AppDefine.BASE_URL_SEARCHDEFAULT, this, 101);
	}

	

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
		mSwipListViewComponents.onLoadOver();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		
		JsonArray mList = mJsonResult.getAsJsonArray("sp_list");
		if (mList != null &&mList.size()>0){
			isEmptyData =false;
			mJsonArray.addAll(mList);
			mListAdapter.changeDataSource(mJsonArray);
			mSwipListViewComponents.onLoadOver(true);
		}else{
			isEmptyData= true;
			mSwipListViewComponents.onLoadOver(false);
		}

	}

	
	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_layout_index, null);
	}

	boolean isEmptyData =false;
	@Override
	public void onLocationChanged(BDLocation mBdLocation) {


		if (mBdLocation != null) {
			String provice = mBdLocation.getProvince();
			String city = mBdLocation.getDistrict();

			if (provice != null && city != null) {
				if(mTvHeadLeft==null) return;
				mTvHeadLeft.setText(provice+"\n"+city);

				lng = mBdLocation.getLongitude() + "";
				lat = mBdLocation.getLatitude() + "";
				mProgressRefresh.setVisibility(View.GONE);
			}
		}
	
		
	}

}
