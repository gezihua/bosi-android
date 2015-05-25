package com.zy.booking.modle.pagefactory;

import java.util.ArrayList;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.R;
import com.zy.booking.components.TabPageIndicatorComponents;
import com.zy.booking.fragments.AlbumsFragment;
import com.zy.booking.fragments.FunsGroupFragment;
import com.zy.booking.fragments.ModelInfoFragment;
import com.zy.booking.json.ModelUploadJsonData;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.struct.OnHttpActionListener;

public class ModelInfoPageFactory extends AbsFactory implements
		OnHttpActionListener {

	ViewPager mViewPagger;
	
	@Override
	public void productTopTab(ViewGroup mViewGroupBottom) {
		super.productTopTab(mViewGroupBottom);
		mViewGroupBottom.setVisibility(View.VISIBLE);
		TabPageIndicatorComponents mTabComponents = new TabPageIndicatorComponents(
				mContext);
		mTabComponents.setData(mContext.getResources()
				.getString(R.string.model_info_tabs).split("#"));
		mViewGroupBottom.addView(mTabComponents.getView());
		mTabComponents.setViewPagger(mViewPagger);
	}

	BaseActivity mBaseActivity;

	public ModelInfoPageFactory(Context mContext) {
		super(mContext);

	}

	public ModelInfoPageFactory(Context mContext, ViewPager mViewPagger) {
		super(mContext);
		this.mViewPagger = mViewPagger;
		this.mBaseActivity = (BaseActivity) mContext;
	}

	private String modelId;

	public void setServiceId(String modelId) {
		this.modelId = modelId;
	}

	ModelInfoFragment modelInfoFragment;
	FunsGroupFragment mFunsGroupFragment;
	AlbumsFragment mAlbumsFragment;

	@Override
	public ArrayList<Fragment> productBody() {
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		modelInfoFragment = new ModelInfoFragment(modelId);
		fragments.add(modelInfoFragment);
		mFunsGroupFragment = new FunsGroupFragment(modelId);
		fragments.add(mFunsGroupFragment);
		mAlbumsFragment = new AlbumsFragment(modelId);
		mAlbumsFragment.setIsModify(false);
		fragments.add(mAlbumsFragment);
		return fragments;
	}

	@Override
	public void productMenu(ViewGroup mViewGroupBottom) {
		getModelData();
	}

	
	

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
		mBaseActivity.dismissProgressDialog();
		mBaseActivity.finish();
	}

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		mBaseActivity.dismissProgressDialog();

		if (isSuccess) {
			JsonObject mJsonModel = JsonUtil.getAsJsonObject(mJsonResult,
					"model");
			String groupId = JsonUtil.getAsString(mJsonModel, "groupId");
			mFunsGroupFragment.setGroupId(groupId,false);
			modelInfoFragment.setData(mJsonModel);
			JsonArray mJsonArray = JsonUtil.getAsJsonArray(mJsonModel,
					"albumList");
			if (mJsonArray != null) {
				mAlbumsFragment.setData(mJsonArray);
			}
		} else {
			mBaseActivity.showToastShort("获取模特信息失败");
			mBaseActivity.finish();
		}

	}

	private void getModelData() {
		mBaseActivity.showProgresssDialog();
		mBaseActivity.sendData(
				ModelUploadJsonData.getModelNameValuePairs(modelId),
				AppDefine.URL_MODLE_GETMODEL, this, 101);
	}


}
