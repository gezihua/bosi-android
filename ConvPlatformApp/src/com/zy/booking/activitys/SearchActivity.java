package com.zy.booking.activitys;

import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.R;
import com.zy.booking.components.BaseComponents.OnComponentsActionListener;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.components.SearchLayoutComponents;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.components.SwipListViewComponents.OnSwipCallBack;
import com.zy.booking.fragments.SearchConditionFragment;
import com.zy.booking.fragments.SearchConditionFragment.OnSearchConditionChoiceCallBack;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.modle.ServicesListAdapter;
import com.zy.booking.struct.OnHttpActionListener;

@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity implements
		OnHttpActionListener {

	public static final String TAG_CATEGORYS_NAME = "categorys";

	public static final String TAG_CATEGORYS_PROVICE = "provice";

	public static final String TAG_CATEGORYS_AREA = "area";

	public static final String TAG_CATEGORYS_CITY = "city";

	public static final String TAG_CATEGORYS_LNG = "lng";

	public static final String TAG_CATEGORYS_LAT = "lat";
	@ViewInject(R.id.headactionbar)
	View mViewHeadBase;
	// 头部文件
	HeadLayoutComponents mHeadLayoutComponents;

	@ViewInject(R.id.layout_frame_container)
	FrameLayout mFrameLayoutSearchContainer;

	@ViewInject(R.id.search_components)
	View mViewSerch;

	SearchLayoutComponents mSearchComponents;
	FragmentManager mFragManager;

	// 用于显示body的布局
	@ViewInject(R.id.ll_search_body)
	LinearLayout mllbody;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		intHeadView();

		initSearchComponents();

		mFragManager = getSupportFragmentManager();
		initSwipListViewAbout();
		getIntentData();

	}

	// 用于处理actionBar
	private void intHeadView() {
		mHeadLayoutComponents = new HeadLayoutComponents(this, mViewHeadBase);

		mtvcategory = mHeadLayoutComponents.getMiddleText();
		mtvcategory.setText("类别");
		mtvcategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				actionCategorys(mtvcategory);
			}
		});
		mHeadLayoutComponents.setDefaultLeftCallBack(true);
		mHeadLayoutComponents.setTextRight("", R.drawable.icon_search_bg);
		mHeadLayoutComponents.setRightOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mViewSerch.setVisibility(View.VISIBLE);
			}
		});
	}

	// 用于处理搜索框
	private void initSearchComponents() {
		mSearchComponents = new SearchLayoutComponents(this, mViewSerch);
		mSearchComponents
				.setOnComponentsAction(new OnComponentsActionListener() {

					@Override
					public void onAction(View mView) {
						if (mView.getTag() == mSearchComponents.TAG_SEARCH) {
							SearchActivity.this.actionSearch();
						}
					}
				});
	}

	@ViewInject(R.id.tv_proviceselect)
	TextView mtvProvice;
	@ViewInject(R.id.tv_areaselect)
	TextView mtvArea; // 用于显示城市

	@ViewInject(R.id.tv_cityselect)
	TextView mtvCity; // 用于显示区域

	TextView mtvcategory;

	public void actionCategorys(TextView mView) {
		showAreaSelectFragment((TextView) mView);
	}

	@OnClick(R.id.tv_proviceselect)
	public void actionProvice(View mView) {
		showAreaSelectFragment((TextView) mView);
	}

	@OnClick(R.id.tv_areaselect)
	public void actionArea(TextView mView) {
		showAreaSelectFragment(mView);
	}

	@OnClick(R.id.tv_cityselect)
	public void actionCity(TextView mView) {
		showAreaSelectFragment(mView);
	}

	SearchConditionFragment mSearchContainFragment;

	private View mTempView;

	private void showAreaSelectFragment(final TextView mView) {
		boolean isSameOneClick = (mTempView != null && mTempView == mView);
		mTempView = mView;
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
				android.R.anim.fade_out);

		if (mSearchContainFragment == null) {
			showContidionFragment(fragmentTransaction, mView);
		} else {
			dissmissContidionFragment(fragmentTransaction);
			if (!isSameOneClick) {
				showContidionFragment(fragmentTransaction, mView);
			}
		}
		fragmentTransaction.commit();
	}

	/**
	 * 用于显示当前的选择项的fragment
	 * 
	 * */
	private void showContidionFragment(
			final FragmentTransaction fragmentTransaction, final TextView mView) {
		mFrameLayoutSearchContainer.setVisibility(View.VISIBLE);
		mSearchContainFragment = SearchConditionFragment.getInstance();
		int requestCode = mSearchContainFragment.CODE_SERARCHPROVICE;
		if (mView == mtvCity) {
			requestCode = mSearchContainFragment.CODE_SEARCHCITY;
		} else if (mView == mtvArea) {
			requestCode = mSearchContainFragment.CODE_SEARCHAREA;
		} else if (mView == mtvcategory) {
			requestCode = mSearchContainFragment.CODE_CATEGORY;
		}

		String tagProvice = mtvProvice.getText().toString();
		String tagArea = mtvArea.getText().toString();
		mSearchContainFragment.setCurrentSerachType(requestCode, tagProvice,
				tagArea);

		mSearchContainFragment
				.setCallBack(new OnSearchConditionChoiceCallBack() {

					@Override
					public void onProviceCallBack(String provice, String id) {
						mView.setText(provice);
						mView.setTag(id);
						playYoYo(mView);
					}

					@Override
					public void actionSearch() {
						SearchActivity.this.actionSearch();
					}

					@Override
					public void actionDissmiss() {
						dissmissContidionFragment(fragmentTransaction);
					}
				});
		fragmentTransaction.replace(R.id.layout_frame_container,
				mSearchContainFragment);
	}

	public void getIntentData() {
		String categorysName = getIntent().getStringExtra(TAG_CATEGORYS_NAME);
		String lat = getIntent().getStringExtra(TAG_CATEGORYS_LAT);
		String lng = getIntent().getStringExtra(TAG_CATEGORYS_LNG);

		if (!TextUtils.isEmpty(lat)) {
			this.mLocLat = lat;
		}

		if (!TextUtils.isEmpty(lng)) {
			this.mLocLng = lng;
		}
		if (!TextUtils.isEmpty(categorysName)) {
			mtvcategory.setText(categorysName);
			mtvcategory.setTag("0");
		}
		String categorysProvice = getIntent().getStringExtra(
				TAG_CATEGORYS_PROVICE);

		if (!TextUtils.isEmpty(categorysProvice)) {
			if (categorysProvice.endsWith("市")
					|| categorysProvice.endsWith("省"))
				categorysProvice = categorysProvice.substring(0,
						categorysProvice.length() - 1);
			mtvProvice.setTag(categorysProvice);
			mtvProvice.setText(categorysProvice);

		}
		String categorysArea = getIntent().getStringExtra(TAG_CATEGORYS_AREA);

		if (!TextUtils.isEmpty(categorysArea)) {
			if (categorysArea.endsWith("区") )
				categorysArea = categorysArea.substring(0,
						categorysArea.length() - 1);
			mtvCity.setTag(categorysArea);
			mtvCity.setText(categorysArea);
		}
		String categorysCity = getIntent().getStringExtra(TAG_CATEGORYS_CITY);

		if (!TextUtils.isEmpty(categorysCity)) {
			if (categorysCity.endsWith("市"))
				categorysCity = categorysCity.substring(0,
						categorysCity.length() - 1);
			mtvArea.setTag(categorysCity);
			mtvArea.setText(categorysCity);

		}

		actionSearch();

	}

	private void dissmissContidionFragment(
			FragmentTransaction fragmentTransaction) {
		mFrameLayoutSearchContainer.setVisibility(View.GONE);
		fragmentTransaction.remove(mSearchContainFragment);
		mSearchContainFragment = null;
	}

	/*
	 * ------------------------------------------------------------用于body显示区域----
	 * ----------------------------* -------------------------------------*
	 * -------------------------* ----------------* --start
	 */
	SwipListViewComponents mSwipListViewComponents;

	ServicesListAdapter mListAdapter;

	JsonArray mJsonArray;

	/**
	 * 初始化swiplayout的相关代码
	 * 
	 * */
	private void initSwipListViewAbout() {
		mJsonArray = new JsonArray();
		mSwipListViewComponents = new SwipListViewComponents(this);
		mllbody.addView(mSwipListViewComponents.getView());
		mSwipListViewComponents.setSwipCallBack(new OnSwipCallBack() {

			@Override
			public void onReflesh() {
				actionSearch();
			}

			@Override
			public void onLoadMore() {

			}

			@Override
			public void onItemClickListener(int position) {
				JsonObject mJsonObj = (JsonObject) mJsonArray.get(position);
				Intent intent = new Intent(mContext, ServiceDetailActivity.class);
				intent.putExtra(ServiceDetailActivity.TAG_ID, JsonUtil
						.getAsString(mJsonObj, "id"));
				startActivity(intent);

			}
		});
		mListAdapter = new ServicesListAdapter(this, mJsonArray);
		mSwipListViewComponents.setAdapter(mListAdapter);
	}

	String mLocLat;
	String mLocLng;

	/**
	 * 执行搜索的相关代码
	 * 
	 * */
	private void actionSearch() {
		String keyWords = mSearchComponents.getEditText().getText().toString();

		String provice = mtvProvice.getText().toString();
		String area = mtvArea.getText().toString();
		String city = mtvCity.getText().toString();

		String tags = mtvcategory.getText().toString();

		mSwipListViewComponents.loading();

		List<NameValuePair> list = UserUpLoadJsonData
				.getDefaultProviderNamePairs(0, 10, mLocLng, mLocLat, provice
						.equals("省份") ? "" : provice, area.equals("城市") ? ""
						: provice, city.equals("区域") ? "" : city, tags
						.equals("类别") ? "" : tags, keyWords, null);
		sendData(list, AppDefine.BASE_URL_SEARCHDEFAULT, this, 101);
	}

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		mSwipListViewComponents.onLoadOver();
		JsonArray mList = mJsonResult.getAsJsonArray("sp_list");
		if (mList != null && mList.size() != 0) {
			mJsonArray = mList;
			mListAdapter.changeDataSource(mJsonArray);
		} else {
			showToastShort("查无结果！！");
		}

	}

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
		mSwipListViewComponents.onLoadOver();
	}

	/*
	 * -------------* --------------------* --------------------------*
	 * ----------
	 * --------------------------------------------------用于body显示区域----
	 * --------------------------- --end
	 */

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onBackPressed() {
		if (mViewSerch.getVisibility() == View.VISIBLE) {
			mViewSerch.setVisibility(View.GONE);
			return;
		}
		super.onBackPressed();
	}

}
