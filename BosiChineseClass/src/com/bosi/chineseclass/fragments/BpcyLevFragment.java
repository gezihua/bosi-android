package com.bosi.chineseclass.fragments;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.OnHttpActionListener;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.bean.BpStasticBean;
import com.bosi.chineseclass.db.BPCY;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.han.util.PreferencesUtils;
import com.bosi.chineseclass.model.BpcyLevAdapter;
import com.bosi.chineseclass.model.BphzLevAdapter;
import com.bosi.chineseclass.views.BSGridView;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

public class BpcyLevFragment extends BaseFragment implements
		OnHttpActionListener {

	@ViewInject(R.id.ll_bphz_body)
	LinearLayout mLayoutBody;

	BSGridView mGridView;
	@ViewInject(R.id.headactionbar)
	View mViewHead;

	List<BpStasticBean> mAdapterDataList = new ArrayList<BpStasticBean>();

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
		mActivity.showProgresssDialogWithHint("正在同步学习记录 ... ");

		mActivity.AsyTaskBaseThread(new Runnable() {

			@Override
			public void run() {
				getBphzHistory();
			}
		}, new Runnable() {

			@Override
			public void run() {
				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						mActivity.dismissProgressDialog();
						updateUI();
					}
				});
			}
		});
	}

	private void updateUI() {
		mBphzLevAdapter.changeDataSource(mAdapterDataList);
	}

	private void sendBpHzHistory() {
		// uid listLearned listNotLearned
		String mUid = PreferencesUtils.getString(mActivity, AppDefine.ZYDefine.EXTRA_DATA_USERID);
		List<NameValuePair> mList = new ArrayList<NameValuePair>();
		mList.add(new BasicNameValuePair("uid", mUid));
		mList.add(new BasicNameValuePair("listLearned", mBpcy.getAllLearnedData(mActivity, "0")));
		mList.add(new BasicNameValuePair("listNotLearned", mBpcy.getAllLearnedData(mActivity, "1")));
	}

	private void getBphzHistory() {
		List<NameValuePair> mList = new ArrayList<NameValuePair>();
		String mUid = PreferencesUtils.getString(mActivity, AppDefine.ZYDefine.EXTRA_DATA_USERID);
		mList.add(new BasicNameValuePair("uid", mUid));
		BSApplication.getInstance().sendData(mList,
				AppDefine.URLDefine.URL_SYNCBOSIIDIOM, this, 101,
				HttpMethod.GET);
	}

	// 放到异步任务中去做
	private List<BpStasticBean> getLists() {
		BPCY mBphz = new BPCY();
		List<BpStasticBean> mLists = new ArrayList<BpStasticBean>();
		for (int i = 1; i <= 15; i++) {
			BpStasticBean mBpHzBean = new BpStasticBean();
			mBpHzBean.mDictIndex = i - 1;
			int startSize = (mBpHzBean.mDictIndex * 1000 + 1);
			int endSize = (i * 1000);

			// 如果是最后一个统计的view 的话 最小值1 最大值 13000
			if (i == 15) {
				mBpHzBean.mNumberBetween = "0-13000";
				mBphz.getListBpHzBeans(mActivity, 1,
						AppDefine.ZYDefine.BPCH_REFID_ADDED + 13000, mBpHzBean);
			} else {
				mBpHzBean.mNumberBetween = startSize + "-" + endSize;
				mBphz.getListBpHzBeans(mActivity,
						AppDefine.ZYDefine.BPCH_REFID_ADDED + startSize,
						AppDefine.ZYDefine.BPCH_REFID_ADDED + endSize,
						mBpHzBean);
			}
			mLists.add(mBpHzBean);
		}

		return mLists;
	}

	@Override
	public void onHttpSuccess(JSONObject mResult, int code) {
		// {"code":1,"msg":"操作成功
		// "
		// ,"data":{"uid":"3","listLearned":["12006",
		// "12005"],"listNotLearned":["12008","1200
		// 7"]}}
		mActivity.dismissProgressDialog(); // 获取数据成功
		JSONArray mArrayLearned = null;
		JSONArray mArrayNotLearned = null;
		if (mResult.has("code")) {
			try {
				String codeResult = mResult.getString("code");
				String message = mResult.getString("message");
				if (codeResult.equals(AppDefine.ZYDefine.CODE_SUCCESS)) {
					JSONObject mData = mResult.getJSONObject("data");
					if (mData.has("listLearned")) {
						mArrayLearned = mData.getJSONArray("listLearned");
					}

					if (mData.has("listNotLearned")) {
						mArrayNotLearned = mData.getJSONArray("listNotLearned");
					}

					//更新本地数据库
					updateLocalBpcyDb(mArrayLearned, mArrayNotLearned);
					
					
				} else {
					mActivity.showToastShort(message);
				}
			} catch (JSONException e) {
				mActivity.showToastShort("服务异常");
			}
		} else {
		}
	}

	BPCY mBpcy = new BPCY();

	private void updateLocalBpcyDb(JSONArray mArrayLearned,
			JSONArray mArrayNotLearned) {
		if (mArrayLearned != null || mArrayLearned != null
				&& mArrayLearned.length() > 0 || mArrayNotLearned.length() > 0) {
			mBpcy.clearDbData();
		}
		if (mArrayLearned != null) {
			for (int i = 0; i < mArrayLearned.length(); i++) {
				try {
					String id = mArrayLearned.getString(i);
					String mSqlInsertData = String.format(
							mActivity.getString(R.string.insert_bpcy_history),
							id, "1");
					mBpcy.updateDataFromDb(mSqlInsertData);
				} catch (Exception e) {
				}
			}
		}

		if (mArrayNotLearned != null) {
			for (int i = 0; i < mArrayNotLearned.length(); i++) {
				try {
					String id = mArrayLearned.getString(i);
					String mSqlInsertData = String.format(
							mActivity.getString(R.string.insert_bpcy_history),
							id, "0");
					mBpcy.updateDataFromDb(mSqlInsertData);
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public void onHttpError(Exception e, String reason, int code) {
		mActivity.showToastShort("网络异常");
	}

}
