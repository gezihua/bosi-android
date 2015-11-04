package com.bosi.chineseclass.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.OnHttpActionListener;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.db.BPHZ;
import com.bosi.chineseclass.db.BphzHistory;
import com.bosi.chineseclass.han.util.PreferencesUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class DownLoadBphzTask implements OnHttpActionListener,IBasicTask{
	BaseActivity mActivity;
	public DownLoadBphzTask(BaseActivity mActivitiy){
		this.mActivity = mActivitiy;
		
		BphzHistory mHistory = new BphzHistory();
		mHistory.dictindex=0;
		mHistory.isRember =2;
		mBphz.saveData(mHistory);
	}
	BPHZ mBphz = new BPHZ();
	@Override
	public void onHttpSuccess(JSONObject mResult, int code) {
		JSONArray mArrayLearned = null;
		JSONArray mArrayNotLearned = null;
		System.out.println(mResult.toString());
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
				} else {
					mActivity.showToastShort(message);
				}
				mActivity.dismissProgressDialog();
			} catch (JSONException e) {
				mActivity.dismissProgressDialog();
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
		if(mCallBack!=null){
			mCallBack.actionCallback();
		}
	}

	@Override
	public void onHttpError(Exception e, String reason, int code) {
		mActivity.showToastShort("网络异常");
		mActivity.dismissProgress();
	}

	@Override
	public void cancleTask() {
	}

	@Override
	public HttpHandler<?> sendDataAsy() {
		mActivity.showProgresssDialogWithHint(mActivity.getString(R.string.hint_updateing_studyhistory));
		List<NameValuePair> mList = new ArrayList<NameValuePair>();
		String mUid = PreferencesUtils.getString(mActivity, AppDefine.ZYDefine.EXTRA_DATA_USERID);
		mList.add(new BasicNameValuePair("uid", mUid));
		HttpHandler mHandler = BSApplication.getInstance().sendData(mList,
				AppDefine.URLDefine.URL_SYNCBOSICHARDATA +"?uid="+mUid, this, 101,
				HttpMethod.GET);
		return mHandler;
	}
	CallBack mCallBack;
	
	public void setCallBack(CallBack mCallBack){
		this.mCallBack = mCallBack;
	}
	public interface CallBack{
		public void actionCallback();
	}


}
