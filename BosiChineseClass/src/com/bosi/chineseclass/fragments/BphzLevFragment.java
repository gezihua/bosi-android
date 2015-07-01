package com.bosi.chineseclass.fragments;


import java.util.ArrayList;
import java.util.List;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.bean.BphzBean;
import com.bosi.chineseclass.db.BPHZ;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.model.BphzLevAdapter;
import com.bosi.chineseclass.views.BSGridView;
import com.lidroid.xutils.view.annotation.ViewInject;

public class BphzLevFragment extends BaseFragment{

	@ViewInject(R.id.ll_bphz_body)
	LinearLayout mLayoutBody;
	
	BSGridView mGridView;
	@ViewInject(R.id.headactionbar)
	View mViewHead;
	
	List<BphzBean> mAdapterDataList = new ArrayList<BphzBean>();
	
	@Override
	protected View getBasedView() {
		return View.inflate(mActivity, R.layout.layout_bphz, null);
	}

	BphzLevAdapter mBphzLevAdapter ;
	@Override
	protected void afterViewInject() {
		
		HeadLayoutComponents mHead = new HeadLayoutComponents(mActivity, mViewHead);
		mHead.setTextMiddle("爆破汉字", -1);
		mGridView = new BSGridView(mActivity);
		mGridView.setGravity(Gravity.CENTER_HORIZONTAL);
		mGridView.setNumColumns(5);
		mGridView.setVerticalSpacing(20);
		mGridView.setCacheColorHint(0);
		mLayoutBody.addView(mGridView);
		mGridView.setAdapter(new BphzLevAdapter(mActivity, null));
		
	    mBphzLevAdapter = new BphzLevAdapter(mActivity, mAdapterDataList);
	    mGridView.setAdapter(mBphzLevAdapter);

	    getDataAsy();
	}
	
	
//	模拟一次进度
	private void getDataAsy(){
		mActivity.updateProgress(1, 2);
		
		AsyTaskBaseThread(new Runnable() {
			
			@Override
			public void run() {
				mAdapterDataList = getLists();
			}
		},new Runnable() {
			
			@Override
			public void run() {
				mActivity.runOnUiThread( new Runnable() {
					
					@Override
					public void run() {
						mActivity.updateProgress(2, 2);
						updateUI();
					}
				});
			}
		});
	}
	
	private void updateUI(){
		mBphzLevAdapter.changeDataSource(mAdapterDataList);
	}
	
	
	//放到异步任务中去做
	private List<BphzBean> getLists(){
		BPHZ mBphz = new BPHZ();
		List<BphzBean> mLists = new ArrayList<BphzBean>();
		for(int i = 1 ; i < 15 ;i++){
			BphzBean  mBpHzBean = new BphzBean();
			mBpHzBean.mDictIndex = i-1;
			String startSize = mBpHzBean.mDictIndex *500+1+"";
			String endSize = i *500+"";
			mBpHzBean.mNumberBetween =  startSize+"-"+endSize;
			
			String sqlSelectBphzLvStastic =  getResources().getString(R.string.select_bphz_lev1data);
			String sqlFormat = String.format(sqlSelectBphzLvStastic, "0","1",startSize,endSize);
			mBphz.getListBpHzBeans(sqlFormat,mBpHzBean);
			mLists .add(mBpHzBean);
		}
		
		return mLists;
	}

}
