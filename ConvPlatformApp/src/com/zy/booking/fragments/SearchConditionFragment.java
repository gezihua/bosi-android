package com.zy.booking.fragments;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zy.booking.AppDefine;
import com.zy.booking.R;
import com.zy.booking.components.CategorysGirdComponents;
import com.zy.booking.modle.SampleListAdapter;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.ViewHolder;

public class SearchConditionFragment extends BaseFragment implements
		OnHttpActionListener, OnItemClickListener {

	public static SearchConditionFragment mSearchConditionFragment;
	public final int CODE_SERARCHPROVICE = 101;
	public final int CODE_SEARCHAREA = 102;
	public final int CODE_SEARCHCITY = 103;
	public final int CODE_CATEGORY  =104;

	CategorysGirdComponents mCategoryGirdComponents;

	JsonArray mJsonArray = new JsonArray();

	@ViewInject(R.id.ll_searchconditaion_body)
	LinearLayout mLinearLayoutBody;

	@ViewInject(R.id.bt_search_back)
	Button mButtonRefsh;

	@ViewInject(R.id.bt_search_search)
	Button mButtonSearch;

	int currentCode;

	String proviceId;

	String cityId;

	String mJsonArrayKey;

	public static SearchConditionFragment getInstance() {
		if (mSearchConditionFragment == null) {
			mSearchConditionFragment = new SearchConditionFragment();
		}
		return mSearchConditionFragment;
	}

	public void setCurrentSerachType(int code, String proviceId, String cityId) {
		this.currentCode = code;
		this.proviceId = proviceId;
		this.cityId = cityId;
	}

	@OnClick(R.id.bt_search_back)
	public void actionReflash(View mView) {
		if (mCallBack != null) {
			mCallBack.actionDissmiss();
		}
	}

	@OnClick(R.id.bt_search_search)
	public void actionSearch(View mView) {
		if (mCallBack != null) {
			mCallBack.actionSearch();
		}
	}

	@Override
	public void onDestroy() {
		mSearchConditionFragment = null;
		System.gc();
		super.onDestroy();
	}

	private void actionSearch() {
		showProgresssDialog();
		if (currentCode == CODE_SERARCHPROVICE) {
			mJsonArrayKey = JSONARRAYKEYP_PROVICE;
			sendData(new ArrayList<NameValuePair>(),
					AppDefine.URL_SEARCHAVAILPROVICE, this, CODE_SERARCHPROVICE);
			return;
		}

		if (currentCode == CODE_SEARCHAREA) {
			mJsonArrayKey = JSONARRAYKEYP_AREA;
			ArrayList<NameValuePair> mLists = new ArrayList<NameValuePair>();
			mLists.add(new BasicNameValuePair("province_name", proviceId));
			sendData(mLists, AppDefine.URL_SEARCHAVAILAREA, this,
					CODE_SERARCHPROVICE);
			return;
		}

		if (currentCode == CODE_SEARCHCITY) {
			mJsonArrayKey = JSONARRAYKEYP_CITY;
			ArrayList<NameValuePair> mLists = new ArrayList<NameValuePair>();
			mLists.add(new BasicNameValuePair("city_name", cityId));
			sendData(mLists, AppDefine.URL_SEARCHCITY, this,
					CODE_SERARCHPROVICE);
			return;
		}
		
		if(currentCode == CODE_CATEGORY){
			mJsonArrayKey = JSONARRAYKEYP_SVC;
			ArrayList<NameValuePair> mLists = new ArrayList<NameValuePair>();
			//mLists.add(new BasicNameValuePair("city_id", cityId));
			sendData(mLists, AppDefine.URL_SERACHCATEGORYS, this,
					CODE_CATEGORY);
			return;
			
		}

	}

	public class SearchCateGoryAdapter extends SampleListAdapter {

		public SearchCateGoryAdapter(Context mContext, JsonArray mData) {
			super(mContext, mData);
		}

		@Override
		public View getView(int posi, View viewTemp, ViewGroup arg2) {
			if (viewTemp == null) {
				viewTemp = LayoutInflater.from(context).inflate(
						R.layout.item_search_concadition, null);
			}
			TextView mTextView = ViewHolder.get(viewTemp,
					R.id.tv_serachcon_item);
			final JsonObject mJsonObj = (JsonObject) mListData.get(posi);
			String name = JsonUtil.getAsString(mJsonObj, "name");
			if (name != null) {
				mTextView.setText(name);
			}

			mTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
						String name = JsonUtil.getAsString(mJsonObj, "name");
						String id = JsonUtil.getAsString(mJsonObj, "id");
						mCallBack.onProviceCallBack(name, id);

				}
			});
			// viewTemp.set
			return viewTemp;
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void playYoYo(View mView) {
		super.playYoYo(mView);
	}

	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_layout_searchcondition, null);
	}

	SearchCateGoryAdapter mAdapterComment;

	@Override
	void afterViewInject() {
		// sendData(new ArrayList<NameValuePair>(), AppDefine.BASE_URL, this,
		// CODE_SERARCHPROVICE);

		mCategoryGirdComponents = new CategorysGirdComponents(
				getActivity(),new GridView(getActivity()));
		mCategoryGirdComponents.setVerNumber(4);
		mCategoryGirdComponents.setIsGirdCanScroll(true);
		mLinearLayoutBody.addView(mCategoryGirdComponents.getView());

		actionSearch();
	}

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {

	}

	private final String JSONARRAYKEYP_PROVICE = "province_list";
	private final String JSONARRAYKEYP_AREA = "city_list";
	private final String JSONARRAYKEYP_CITY = "county_list";
	private final String JSONARRAYKEYP_SVC = "svc_list";
	

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		dismissProgressDialog();

		JsonArray mJsonArry = mJsonResult.getAsJsonArray(mJsonArrayKey);
		if (mJsonResult != null && mJsonArry != null) {
			this.mJsonArray = mJsonArry;
			mAdapterComment = new SearchCateGoryAdapter(getActivity(),
					mJsonArray);
			mCategoryGirdComponents.setAdapter(mAdapterComment);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int posi, long arg3) {
		if (mCallBack == null)
			return;

		JsonObject mJsonObj = (JsonObject) mJsonArray.get(posi);

	}

	OnSearchConditionChoiceCallBack mCallBack;

	public void setCallBack(OnSearchConditionChoiceCallBack mCallBack) {
		this.mCallBack = mCallBack;
	}

	public interface OnSearchConditionChoiceCallBack {
		public void onProviceCallBack(String provice, String id);

		public void actionSearch();

		public void actionDissmiss();
	}
	//
	

}
