package com.zy.booking.modle;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.activitys.ImageDitalActivity;
import com.zy.booking.components.CategorysGirdComponents;
import com.zy.booking.db.DBUSER;
import com.zy.booking.db.User;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.DataTools;
import com.zy.booking.util.ViewHolder;

public class CommentTopicAdapter extends SampleSwipListAdapter implements
		OnHttpActionListener {

	BitmapUtils mBitmapCache;
	BitmapDisplayConfig mDisplayConfig;

	BaseActivity mActivity;

	public CommentTopicAdapter(Context mContext, JsonArray mData) {
		super(mContext, mData);
		mBitmapCache = CpApplication.getApplication().mBitmapManager.mBitmapUtils;
		mDisplayConfig = new BitmapDisplayConfig();
		mDisplayConfig.setBitmapMaxSize(new BitmapSize(DataTools.dip2px(
				mContext, 60), DataTools.dip2px(mContext, 60)));
		mDisplayConfig.setLoadFailedDrawable(mContext.getResources()
				.getDrawable(R.drawable.ic_launcher));
		mDisplayConfig.setLoadingDrawable(mContext.getResources().getDrawable(
				R.drawable.default_avatar));
		mActivity = (BaseActivity) mContext;
	}

	@Override
	public JsonObject getItem(int args) {
		return super.getItem(args);
	}

	@Override
	public View getView(int posi, View viewTemp, ViewGroup arg2) {

		if (viewTemp == null) {
			viewTemp = LayoutInflater.from(context).inflate(
					R.layout.item_layout_comment, null);
		}

		JsonObject mJson = getItem(posi);
		showComment(viewTemp, mJson, posi);

		if (mEditTextTemp != null) {
			mEditTextTemp.clearFocus();
			if (index != -1 && index == posi) {
				mEditTextTemp.setSelection(mEditTextTemp.getText().length());
				mEditTextTemp.requestFocus();
			}
		}
		return viewTemp;
	}

	/**
	 * 基础用户信息显示
	 * 
	 * 
	 */
	private void showUserInfo(View mViewBase, JsonObject mJsonData)
			throws Exception {
		ImageView mImageView = ViewHolder.get(mViewBase,
				R.id.item_topic_usericon);
		TextView mTvUserName = ViewHolder.get(mViewBase,
				R.id.tv_comment_topic_username);

		mBitmapCache.display(mImageView,
				JsonUtil.getAsString(mJsonData, "photo"), mDisplayConfig);
		mTvUserName.setText(JsonUtil.getAsString(mJsonData, "nickname"));
	}

	/**
	 * 显示详情评论
	 * 
	 * */
	private void showComment(final View mViewBase, JsonObject mJsonData,
			final int position) {
		TextView mTvSendTime = ViewHolder.get(mViewBase,
				R.id.tv_comment_topic_sendtime);
		TextView mTvTopic = ViewHolder.get(mViewBase, R.id.tv_topic_name);

		LinearLayout mLayoutCommentDital = ViewHolder.get(mViewBase,
				R.id.ll_topic_dital);

		final String id = JsonUtil.getAsString(mJsonData, "id");
		mTvTopic.setText(JsonUtil.getAsString(mJsonData, "content"));
		mTvSendTime.setText(JsonUtil.getAsString(mJsonData, "createTime"));
		try {
			showUserInfo(mViewBase, mJsonData.getAsJsonObject("issuer"));
		} catch (Exception e) {
		}

		mLayoutCommentDital.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showSendCommentLayout(mViewBase, id, "", "回复主题", position);
			}
		});
		addCategorysImages(mLayoutCommentDital,
				mJsonData.getAsJsonArray("imageList"));

		showCommentList(mViewBase, mJsonData.getAsJsonArray("commentList"), id,
				position);
	}

	private View mViewTemp = null;

	/**
	 * 
	 * 显示发布的图片信息
	 * 
	 * */
	private void addCategorysImages(ViewGroup mViewGroup, JsonArray mJsonDate) {
		if (mViewGroup.getChildCount() > 1)
			mViewGroup.removeViewAt(1);

		if (mJsonDate == null || mJsonDate.size() <= 0)
			return;
		CategorysGirdComponents mCateComponents = new CategorysGirdComponents(
				null, context);
		mCateComponents.setAdapter(new MyTopicImageAdapter(context, mJsonDate));
		mViewGroup.addView(mCateComponents.getView());
	}

	/**
	 * 
	 * 发布的图片信息适配器
	 * 
	 * */
	class MyTopicImageAdapter extends SampleSwipListAdapter {

		public MyTopicImageAdapter(Context mContext, JsonArray mData) {
			super(mContext, mData);
		}

		@Override
		public View getView(int posi, View viewTemp, ViewGroup arg2) {
			ImageView mImageView = new ImageView(context);

			mImageView.setLayoutParams(new AbsListView.LayoutParams(DataTools
					.dip2px(context, 80), DataTools.dip2px(context, 60)));
			mImageView.setPadding(0, 0, 2, 2);
			mImageView.setScaleType(ScaleType.FIT_XY);
			JsonElement mJsonObject = mListData.get(posi);
			final String url = mJsonObject.getAsString();
			mBitmapCache.display(mImageView, url, mDisplayConfig);

			mImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent mIntent = new Intent(mActivity,
							ImageDitalActivity.class);
					String [] mArray = new String[]{url};
					mIntent.putExtra(ImageDitalActivity.TAG_INTENT_FILEPATH,
							mArray);
					mActivity.startActivity(mIntent);
				}
			});
			return mImageView;

		}
	}

	DBUSER mdbUser = new DBUSER();

	/**
	 * 
	 * 添加评论列表
	 * 
	 * */
	private void showCommentList(final View mViewBase, JsonArray mJsonDate,
			final String topicId, final int position) {
		LinearLayout mLayoutCommentList = ViewHolder.get(mViewBase,
				R.id.ll_topic_replay);
		mLayoutCommentList.removeAllViews();
		mLayoutCommentList.setVisibility(View.GONE);
		if (mJsonDate == null || mJsonDate.size() <= 0)
			return;
		mLayoutCommentList.setVisibility(View.VISIBLE);
		for (int i = 0; i < mJsonDate.size(); i++) {
			JsonObject mJsonObj = (JsonObject) mJsonDate.get(i);
			String content = JsonUtil.getAsString(mJsonObj, "content");
			final String otherId = JsonUtil.getAsString(mJsonObj, "issuer");
			final String commentId = JsonUtil.getAsString(mJsonObj, "pk");
			TextView mTextView = new TextView(mActivity);

			mTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showSendCommentLayout(mViewBase, commentId, otherId,
							"回复评论", position);
				}
			});

			User mUser = getUserFromDb(otherId);
			if (mUser == null) {
				mTextView.setText("陌生人" + ":" + content);
				getUserInfo(otherId);
			} else {
				mTextView.setText(mUser.nickname + ":" + content);
			}
			mLayoutCommentList.addView(mTextView);

			addRepalyContent(mLayoutCommentList,
					mJsonObj.getAsJsonArray("replyList"));

			if (mLayoutCommentList.getChildCount() > 0) {
				mLayoutCommentList.setVisibility(View.VISIBLE);
			}

		}
	}

	private User getUserFromDb(String userId) {
		String sql = mActivity.getString(R.string.select_userinfo_baseuid);
		String sqlFormat = mActivity.getStringFormat(sql, userId);
		List<?> mList = mdbUser.selectDataFromDb(sqlFormat);

		User mUser = null;
		if (mList != null && mList.size() > 0) {
			mUser = (User) mList.get(0);
		}
		return mUser;
	}

	/**
	 * 
	 * 添加回复部分
	 * 
	 * 
	 * */
	private void addRepalyContent(LinearLayout mLinearLayout,
			JsonArray mJsonData) {

		if (mJsonData == null || mJsonData.size() <= 0)
			return;

		for (int i = 0; i < mJsonData.size(); i++) {
			JsonObject mData = (JsonObject) mJsonData.get(i);
			String content = JsonUtil.getAsString(mData, "content");
			String isSuer = JsonUtil.getAsString(mData, "issuer");
			String userTo = JsonUtil.getAsString(mData, "userTo");

			User mUserFrom = getUserFromDb(isSuer);
			User mUserTo = getUserFromDb(userTo);

			if (mUserFrom == null) {
				getUserInfo(isSuer);
			} else if (mUserTo == null) {
				getUserInfo(userTo);
			}

			TextView mTextView = new TextView(mActivity);

			String msgFromName = mUserFrom == null ? "陌生人" : mUserFrom.nickname;
			String msgToName = mUserTo == null ? "陌生人" : mUserTo.nickname;
			mTextView.setText(msgFromName + "回复" + msgToName + ":" + content);
			mLinearLayout.addView(mTextView);
		}

	}

	private int index = -1;

	private EditText mEditTextTemp;

	/**
	 * 
	 * 显示评论组件
	 * 
	 * 
	 * */
	private void showSendCommentLayout(View mViewBase, final String topicId,
			final String otherId, String hint, final int position) {
		if (mViewTemp != null) {
			mViewTemp.setVisibility(View.GONE);
		}
		View mView = ViewHolder.get(mViewBase, R.id.ll_sendcomment_body);
		mViewTemp = mView;
		mView.setVisibility(View.VISIBLE);

		final EditText mEditText = ViewHolder.get(mViewBase,
				R.id.et_sendcomment);
		mEditText.setFocusable(true);
		mEditText.requestFocus();
		mEditText.setHint(hint);
		mEditText.setText("");
		mEditTextTemp = mEditText;
		mEditText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {

					index = position;
				}
				return false;
			}
		});

		CpApplication.getApplication().playYoYo(mEditText);

		Button mbtSend = ViewHolder.get(mViewBase, R.id.btsend_comment);

		mbtSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String mEdContent = mEditText.getText().toString();
				if (TextUtils.isEmpty(mEdContent)) {
					CpApplication.getApplication().playYoYo(mEditText);
					return;
				}
				mActivity.showProgresssDialog();
				if (TextUtils.isEmpty(otherId)) {
					mActivity.sendData(UserUpLoadJsonData
							.getAddCommentListValuePairs(topicId, mEdContent),
							AppDefine.URL_EVALUATION_ADDCOMMENT,
							CommentTopicAdapter.this, SENDTOPICREPLAY);
				} else {
					mActivity.sendData(UserUpLoadJsonData
							.getReplyListValuePairs(otherId, mEdContent,
									topicId),
							AppDefine.URL_EVALUATION_ADDCOMMENTREPLAY,
							CommentTopicAdapter.this, SENDOTHERTOPICREPLAY);
				}

			}
		});
	}

	private final int SENDTOPICREPLAY = 0;
	private final int SENDOTHERTOPICREPLAY = 1;
	private final int SENDGETUSERINFO = 2;

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
		mActivity.dismissProgressDialog();
	}

	DBUSER mDbUser = new DBUSER();

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		mActivity.dismissProgressDialog();
		if (isSuccess) {
			if (resultCode == SENDTOPICREPLAY
					|| resultCode == SENDOTHERTOPICREPLAY) {
				mActivity.showToastShort("发布成功");
				if (mCallBack != null)
					mCallBack.onCallBack();

				mViewTemp.setVisibility(View.GONE);
			}

			else {

				JsonObject mJsonObj = JsonUtil.getAsJsonObject(mJsonResult,
						"user");
				String nickName = JsonUtil.getAsString(mJsonObj, "nickname");
				String iconUrl = JsonUtil.getAsString(mJsonObj, "photo");
				String userId = JsonUtil.getAsString(mJsonObj, "id");
				String phone = JsonUtil.getAsString(mJsonObj, "phone");

				if (nickName != null) {
					// 保存到用户表
					User mUser = new User();
					mUser.nickname = nickName;
					mUser.userid = userId;
					mUser.photo = iconUrl;
					mUser.phone = phone;
					mDbUser.saveData(mUser);
					if (mCallBack != null)
						mCallBack.onCallBack();

				}

			}

		}
	}

	private void getUserInfo(String userId) {
		ArrayList<NameValuePair> mList = new ArrayList<NameValuePair>();
		mList.add(new BasicNameValuePair("userId", userId));
		((BaseActivity) context).sendData(mList, AppDefine.URL_GETUSERINFO,
				this, SENDGETUSERINFO);
	}

	CallBack mCallBack;

	public void setCallBack(CallBack mCallBack) {
		this.mCallBack = mCallBack;
	}

	public interface CallBack {
		public void onCallBack();
	}
}
