package com.bosi.chineseclass.fragments;

import java.util.ArrayList;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.BosiChineseService;
import com.bosi.chineseclass.OnHttpActionListener;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.bean.BpStasticBean;
import com.bosi.chineseclass.db.BPHZ;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.han.util.PreferencesUtils;
import com.bosi.chineseclass.model.BphzLevAdapter;
import com.bosi.chineseclass.views.BSGridView;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class BphzLevFragment extends BaseFragment implements OnHttpActionListener{

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

		getDataAsy();
	}

	// 模拟一次进度
	protected void getDataAsy() {
		mActivity.showProgresssDialogWithHint("正在同步爆破汉字学习记录 ... ");
		getBphzHistory();
	}

	private void updateUI() {
		mAdapterDataList = getLists();
		mBphzLevAdapter.changeDataSource(mAdapterDataList);
	}

	@Override
	public void onDestroy() {
		Intent mIntent = new Intent(mActivity , BosiChineseService.class);
		mIntent.putExtra(BosiChineseService.TASKNAME, BosiChineseService.TASK_UPLOADBPHZ);
		mActivity.startService(mIntent);
		
		super.onDestroy();
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
	
	
	BPHZ mBphz = new BPHZ();
	private void getBphzHistory() {
		List<NameValuePair> mList = new ArrayList<NameValuePair>();
		String mUid = PreferencesUtils.getString(mActivity, AppDefine.ZYDefine.EXTRA_DATA_USERID);
		mList.add(new BasicNameValuePair("uid", mUid));
		BSApplication.getInstance().sendData(mList,
				AppDefine.URLDefine.URL_SYNCBOSICHARDATA +"?uid="+mUid, this, 101,
				HttpMethod.GET);
	}
	
	
	@Override
	public void onHttpSuccess(JSONObject mResult, int code) {
		mActivity.dismissProgressDialog(); // 获取数据成功
		JSONArray mArrayLearned = null;
		JSONArray mArrayNotLearned = null;
		if (mResult.has("code")) {
			try {
				String codeResult = mResult.getString("code");
				String message = mResult.getString("msg");
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
					
					updateUI();
				} else {
					mActivity.showToastShort(message);
				}
				mActivity.dismissProgress();
			} catch (JSONException e) {
				mActivity.dismissProgress();
				mActivity.showToastShort("服务异常");
			}
		} else{
			mActivity.dismissProgressDialog();
			mActivity.showToastShort("服务异常");
		}
	}


	private void updateLocalBpcyDb(JSONArray mArrayLearned,
			JSONArray mArrayNotLearned) {
		if (mArrayLearned != null || mArrayLearned != null
				&& mArrayLearned.length() > 0 || mArrayNotLearned.length() > 0) {
			mBphz.clearDbData();
		}
		if (mArrayLearned != null) {
			for (int i = 0; i < mArrayLearned.length(); i++) {
				try {
					String id = mArrayLearned.getString(i);
					String mSqlInsertData = String.format(
							mActivity.getString(R.string.insert_bphz_history),
							id, "1");
					mBphz.updateDataFromDb(mSqlInsertData);
				} catch (Exception e) {
				}
			}
		}

		if (mArrayNotLearned != null) {
			for (int i = 0; i < mArrayNotLearned.length(); i++) {
				try {
					String id = mArrayNotLearned.getString(i);
					String mSqlInsertData = String.format(
							mActivity.getString(R.string.insert_bphz_history),
							id, "0");
					mBphz.updateDataFromDb(mSqlInsertData);
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public void onHttpError(Exception e, String reason, int code) {
		mActivity.showToastShort("网络异常");
		mActivity.dismissProgress();
	}

}
