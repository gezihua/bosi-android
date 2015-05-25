package com.zy.booking.fragments;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.baidu.location.BDLocation;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.AppDefine;
import com.zy.booking.R;
import com.zy.booking.activitys.SearchActivity;
import com.zy.booking.components.FlowWithTvComponents;
import com.zy.booking.components.FlowWithTvComponents.OnItemFlowViewClickListener;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.control.LocationCtrol.OnLocationChangedListener;
import com.zy.booking.struct.OnHttpActionListener;


public class SpCategorysFragments extends BaseFragment implements OnHttpActionListener,OnLocationChangedListener{
	
	@ViewInject(R.id.ll_container_body)
	LinearLayout mLayoutBody;

	
	
	@ViewInject(R.id.headactionbar)
	View mViewHead;
	
	HeadLayoutComponents mHeadLayoutComponents;
	
	FlowWithTvComponents mFlowWithTvComponents;
	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_layout_common, null);
	}

	@Override
	void afterViewInject() {
		
		mHeadLayoutComponents = new HeadLayoutComponents(getActivity(), mViewHead);
		mHeadLayoutComponents.setTextMiddle("服务分类", -1);
		
		
		mFlowWithTvComponents = new FlowWithTvComponents(getActivity());
		mFlowWithTvComponents.setOnItemClickListener(new OnItemFlowViewClickListener() {
			
			@Override
			public void onItemClickListener(final String name) {
				Intent mIntent  = new Intent(getActivity(),SearchActivity.class);
				mIntent.putExtra(SearchActivity.TAG_CATEGORYS_NAME, name);
				if(mBdLocation!=null){
					mIntent.putExtra(SearchActivity.TAG_CATEGORYS_PROVICE, mBdLocation.getProvince());
					mIntent.putExtra(SearchActivity.TAG_CATEGORYS_AREA, mBdLocation.getDistrict());
					mIntent.putExtra(SearchActivity.TAG_CATEGORYS_CITY, mBdLocation.getCity());
					mIntent.putExtra(SearchActivity.TAG_CATEGORYS_LNG, mBdLocation.getLongitude()+"");
					mIntent.putExtra(SearchActivity.TAG_CATEGORYS_LAT, mBdLocation.getLatitude()+"");
				}
			    getActivity().startActivity(mIntent);
			}
		});
		
		mLayoutBody.addView(mFlowWithTvComponents.getView());
		//mLists.add(new BasicNameValuePair("city_id", cityId));
		getCategorys();
	}
	
	private void getCategorys(){
		ArrayList<NameValuePair> mLists = new ArrayList<NameValuePair>();
		sendData(mLists, AppDefine.URL_SERACHCATEGORYS, this,
				101);
	}

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
	}
	
	
	JsonArray mJsonArray = new JsonArray();
	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		if(isSuccess){
			JsonArray mJsonArry = mJsonResult.getAsJsonArray("svc_list");
			if (mJsonResult != null && mJsonArry != null) {
				this.mJsonArray = mJsonArry;
				mFlowWithTvComponents.addItems(mJsonArray);
			}
		}
	}
	
	BDLocation mBdLocation;
	@Override
	public void onLocationChanged(BDLocation mBdLocation) {
	   this.mBdLocation = mBdLocation;
		
	}

}
