package com.bosi.chineseclass.task;

import java.util.ArrayList;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.OnHttpActionListener;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.db.BPCY;
import com.bosi.chineseclass.db.BpcyHistory;
import com.bosi.chineseclass.han.util.PreferencesUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class DownLoadBpcyTask implements OnHttpActionListener ,IBasicTask{
	
	BaseActivity mActivity;
	public DownLoadBpcyTask(BaseActivity mActivity){
		this.mActivity = mActivity;
		BpcyHistory mHistory = new BpcyHistory();
		mHistory.dictindex = 0;
		mHistory.isRember = 2;
		mBpcy.saveData(mHistory);
	}

	@Override
	public void cancleTask() {
		
	}
	@Override
	public HttpHandler<?> sendDataAsy() {
		mActivity.showProgresssDialogWithHint(mActivity.getString(R.string.hint_updateing_studyhistory));
		List<NameValuePair> mList = new ArrayList<NameValuePair>();
		String mUid = PreferencesUtils.getString(mActivity,
				AppDefine.ZYDefine.EXTRA_DATA_USERID);
		HttpHandler mHandler = BSApplication.getInstance().sendData(mList,
				AppDefine.URLDefine.URL_SYNCBOSIIDIOM + "?uid=" + mUid, this,
				101, HttpMethod.GET);
		return mHandler;
	}

	@Override
	public void onHttpSuccess(JSONObject mResult, int code) {
		System.out.println(mResult.toString());
		// {"code":1,"msg":"操作成功
		// "
		// ,"data":{"uid":"3","listLearned":["12006",
		// "12005"],"listNotLearned":["12008","1200
		// 7"]}}
		if (code == 101) {
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
							mArrayNotLearned = mData
									.getJSONArray("listNotLearned");
						}
						// 更新本地数据库
						updateLocalBpcyDb(mArrayLearned, mArrayNotLearned);
					} else {
						mActivity.showToastShort(message);
					}
					mActivity.dismissProgressDialog();
				} catch (JSONException e) {
					mActivity.dismissProgressDialog();
					mActivity.showToastShort("服务异常");
				}
			} else {
				mActivity.dismissProgressDialog();
				mActivity.showToastShort("服务异常");
			}
		}

	}

	BPCY mBpcy = new BPCY();

	private void updateLocalBpcyDb(JSONArray mArrayLearned,
			JSONArray mArrayNotLearned) {
		if (mArrayLearned.length() > 0 || mArrayNotLearned.length() > 0) {
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
					String id = mArrayNotLearned.getString(i);
					String mSqlInsertData = String.format(
							mActivity.getString(R.string.insert_bpcy_history),
							id, "0");
					mBpcy.updateDataFromDb(mSqlInsertData);
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
		mActivity.dismissProgressDialog();
	}

       CallBack mCallBack;
	
	public void setCallBack(CallBack mCallBack){
		this.mCallBack = mCallBack;
	}
	public interface CallBack{
		public void actionCallback();
	}

}
