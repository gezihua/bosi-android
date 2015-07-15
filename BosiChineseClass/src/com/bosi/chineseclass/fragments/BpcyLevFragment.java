package com.bosi.chineseclass.fragments;

import java.util.ArrayList;
import java.util.List;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.bean.BphzBean;
import com.bosi.chineseclass.db.BPCY;
import com.bosi.chineseclass.db.BPHZ;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.model.BpcyLevAdapter;
import com.bosi.chineseclass.model.BphzLevAdapter;
import com.bosi.chineseclass.views.BSGridView;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;


public class BpcyLevFragment extends BaseFragment{

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

	BpcyLevAdapter mBphzLevAdapter;

	@Override
	public void onResume() {
		super.onResume();
		getDataAsy();
	}
	@Override
	protected void afterViewInject() {

		HeadLayoutComponents mHead = new HeadLayoutComponents(mActivity,
				mViewHead);
		mHead.setTextMiddle("爆破成语", -1);
		mGridView = new BSGridView(mActivity);
		mGridView.setGravity(Gravity.CENTER_HORIZONTAL);
		mGridView.setNumColumns(5);
		mGridView.setVerticalSpacing(20);
		mGridView.setCacheColorHint(0);
		mLayoutBody.addView(mGridView);
		mGridView.setAdapter(new BphzLevAdapter(mActivity, null));

		mBphzLevAdapter = new BpcyLevAdapter(mActivity, mAdapterDataList);
		mGridView.setAdapter(mBphzLevAdapter);

		getDataAsy();
	}

	// 模拟一次进度
	protected void getDataAsy() {
		mActivity.updateProgress(1, 2);

		mActivity.AsyTaskBaseThread(new Runnable() {

			@Override
			public void run() {
				mAdapterDataList = getLists();
			}
		}, new Runnable() {

			@Override
			public void run() {
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						mActivity.updateProgress(2, 2);
						updateUI();
					}
				});
			}
		});
	}

	private void updateUI() {
		mBphzLevAdapter.changeDataSource(mAdapterDataList);
	}

	// 放到异步任务中去做
	private List<BphzBean> getLists() {
		BPCY mBphz = new BPCY();
		List<BphzBean> mLists = new ArrayList<BphzBean>();
		for (int i = 1; i <= 15; i++) {
			BphzBean mBpHzBean = new BphzBean();
			mBpHzBean.mDictIndex = i - 1;
			int startSize = (mBpHzBean.mDictIndex * 1000 + 1);
			int endSize = (i * 1000);
			
			// 如果是最后一个统计的view 的话 最小值1 最大值 7000
			if (i == 15) {
				mBpHzBean.mNumberBetween = "13000";
				mBphz.getListBpHzBeans(mActivity,1,
						AppDefine.ZYDefine.BPHZ_REFID_ADDED + 7000, mBpHzBean);
			} else {
				mBpHzBean.mNumberBetween = startSize + "-" + endSize;
				mBphz.getListBpHzBeans(mActivity,AppDefine.ZYDefine.BPHZ_REFID_ADDED + startSize,
						AppDefine.ZYDefine.BPHZ_REFID_ADDED + endSize, mBpHzBean);
			}
			mLists.add(mBpHzBean);
		}

		return mLists;
	}

}
