package com.bosi.chineseclass.fragments;

import java.util.ArrayList;
import java.util.List;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.bean.BpStasticBean;
import com.bosi.chineseclass.db.BPHZ;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.model.BphzLevAdapter;
import com.bosi.chineseclass.task.DownLoadBphzTask;
import com.bosi.chineseclass.task.DownLoadBphzTask.CallBack;
import com.bosi.chineseclass.views.BSGridView;
import com.lidroid.xutils.view.annotation.ViewInject;

public class BphzLevFragment extends BaseFragment implements CallBack{

	@ViewInject(R.id.ll_bphz_body)
	LinearLayout mLayoutBody;

	BSGridView mGridView;
	@ViewInject(R.id.headactionbar)
	View mViewHead;

	List<BpStasticBean> mAdapterDataList = new ArrayList<BpStasticBean>();
	
	DownLoadBphzTask mDownLoadTask;

	@Override
	protected View getBasedView() {
		return View.inflate(mActivity, R.layout.layout_bphz, null);
	}

	BphzLevAdapter mBphzLevAdapter;
	@Override
	public void onResume() {
		super.onResume();
		updateUI();
	}
	@Override
	protected void afterViewInject() {
		HeadLayoutComponents mHead = new HeadLayoutComponents(mActivity,
				mViewHead);
		
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
		
		if(BSApplication.getInstance().isFirstInBphz){
			BSApplication.getInstance().isFirstInBphz = false;
			downLoadBphzData();
		}
	}

	private void downLoadBphzData(){
		mDownLoadTask = new DownLoadBphzTask(mActivity);
		mDownLoadTask.setCallBack(this);
		mDownLoadTask.sendDataAsy();
	}
	

	private void updateUI() {
		mAdapterDataList = getLists();
		mBphzLevAdapter.changeDataSource(mAdapterDataList);
	}

	// 放到异步任务中去做
	private List<BpStasticBean> getLists() {
		BPHZ mBphz = new BPHZ();
		List<BpStasticBean> mLists = new ArrayList<BpStasticBean>();
		for (int i = 1; i <= 15; i++) {
			BpStasticBean mBpHzBean = new BpStasticBean();
			mBpHzBean.mDictIndex = i - 1;
			int startSize = (mBpHzBean.mDictIndex * 500 + 1);
			int endSize = (i * 500);
			// 如果是最后一个统计的view 的话 最小值1 最大值 7000
			if (i == 15) {
				mBpHzBean.mNumberBetween = 1 + "-" + 7000;
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
	@Override
	public void actionCallback() {
		updateUI();
	}
	
	


	
}
