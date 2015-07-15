package com.bosi.chineseclass.fragments;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.bean.BphzBean;
import com.bosi.chineseclass.components.NiftyDialogComponents;
import com.bosi.chineseclass.components.NiftyDialogComponents.OnNiftyCallBack;
import com.bosi.chineseclass.db.BPCY;
import com.bosi.chineseclass.db.BPHZ;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.han.util.PreferencesUtils;
import com.bosi.chineseclass.model.BpcyLevvAdapter;
import com.bosi.chineseclass.model.BphzLevAdapter;
import com.bosi.chineseclass.model.BphzLevvAdapter;
import com.bosi.chineseclass.views.BSGridView;
import com.lidroid.xutils.view.annotation.ViewInject;

public class BpcyLevvFragment extends BaseFragment {

	@ViewInject(R.id.ll_bphz_body)
	LinearLayout mLayoutBody;

	BSGridView mGridView;
	@ViewInject(R.id.headactionbar)
	View mViewHead;

	List<BphzBean> mAdapterDataList = new ArrayList<BphzBean>();

	@ViewInject(R.id.bphz_remote_body)
	LinearLayout mLayoutRemoteBody;

	@Override
	protected View getBasedView() {
		return View.inflate(mActivity, R.layout.layout_bphz, null);
	}

	BpcyLevvAdapter mBphzLevAdapter;

	int mCurrentXY;

	@Override
	protected void afterViewInject() {
		mCurrentXY = PreferencesUtils.getInt(mActivity, "position");
		HeadLayoutComponents mHead = new HeadLayoutComponents(mActivity,
				mViewHead);
		mHead.setTextMiddle("爆破成语", -1);
		mGridView = new BSGridView(mActivity);
		mGridView.setNumColumns(4);
		mGridView.setVerticalSpacing(20);
		mGridView.setCacheColorHint(0);
		mLayoutBody.addView(mGridView);

		mBphzLevAdapter = new BpcyLevvAdapter(mActivity, mAdapterDataList);
		mGridView.setAdapter(mBphzLevAdapter);
		mLayoutRemoteBody.setVisibility(View.VISIBLE);

		initRemoteView();

	}

	@Override
	public void onResume() {
		super.onResume();
		getDataAsy();
	}

	// 模拟一次进度
	private void getDataAsy() {
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

	BPCY mbphz = new BPCY();
	NiftyDialogComponents mNiftyDialog;

	private void showNotifyDialog() {
		String msgForRemote = getResources().getString(
				R.string.dialog_clearbphz_levvdata);
		mNiftyDialog.setUpNifty("确认", "取消", "提示", msgForRemote);
		mNiftyDialog.setNoftyCallBack(new OnNiftyCallBack() {

			@Override
			public void onBt2Click() {
				mNiftyDialog.dismissBuilder();
			}

			@Override
			public void onBt1Click() {
				// 弹出一个提示框 用于确认是否删除数据
				int mCurrentStartSize = mCurrentXY * 1000 + 1
						+ AppDefine.ZYDefine.BPHZ_REFID_ADDED;
				int mCurrentEndSize = mCurrentXY * 1000 + 1000
						+ AppDefine.ZYDefine.BPHZ_REFID_ADDED;
				mbphz.deleteDbBaseBetweenSE(mActivity, mCurrentStartSize,
						mCurrentEndSize);

				mNiftyDialog.dismissBuilder();
				getDataAsy();
			}
		});
		mNiftyDialog.showBuilder();
	}

	private void initRemoteView() {
		// delete_bphz_basedictindexbetween
		mNiftyDialog = new NiftyDialogComponents(mActivity);

		View mViewRemote = View.inflate(mActivity, R.layout.bphz_levv_remote,
				null);

		Button mButtonClear = (Button) mViewRemote
				.findViewById(R.id.bt_bphzlvv_clear_logdata);
		mButtonClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showNotifyDialog();
			}
		});

		mLayoutRemoteBody.addView(mViewRemote);
	}

	// 放到异步任务中去做
	private List<BphzBean> getLists() {
		BPHZ mBphz = new BPHZ();
		List<BphzBean> mLists = new ArrayList<BphzBean>();
		int mCurrentBegan = mCurrentXY * 1000;
		for (int i = 1; i < 11; i++) {
			BphzBean mBpHzBean = new BphzBean();
			mBpHzBean.mDictIndex = i - 1;
			int startSize = mBpHzBean.mDictIndex * 100 + 1 + mCurrentBegan;
			int endSize = i * 100 + mCurrentBegan;
			mBpHzBean.mNumberBetween = startSize + "-" + endSize;
			mBphz.getListBpHzBeans(mActivity,
					AppDefine.ZYDefine.BPHZ_REFID_ADDED + startSize,
					AppDefine.ZYDefine.BPHZ_REFID_ADDED + endSize, mBpHzBean);
			mLists.add(mBpHzBean);
		}

		return mLists;
	}

}
