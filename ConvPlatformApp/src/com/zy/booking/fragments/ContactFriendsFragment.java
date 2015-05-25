package com.zy.booking.fragments;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.LinearLayout;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.EmsgManager;
import com.zy.booking.R;
import com.zy.booking.activitys.ChatActivity;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.components.SwipListViewComponents.OnSwipCallBack;
import com.zy.booking.db.DBFRIEND;
import com.zy.booking.db.DBUSER;
import com.zy.booking.db.FriendTable;
import com.zy.booking.db.MSGHISTORY;
import com.zy.booking.db.User;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.modle.FriendsListAdapter;
import com.zy.booking.struct.OnHttpActionListener;

public class ContactFriendsFragment extends BaseFragment implements
		OnHttpActionListener {
	@ViewInject(R.id.ll_container_body)
	LinearLayout mLayoutBody;

	SwipListViewComponents mSwipListView;

	BaseActivity mContext;

	FriendsListAdapter mFriendsAdapter;

	JsonArray mJsonArray;

	@Override
	protected View getBasedView() {
		return inflater.from(getActivity()).inflate(
				R.layout.layout_common_withoutscroll, null);
	}

	@Override
	void afterViewInject() {
		mContext = (BaseActivity) getActivity();
		IntentFilter mInterFilter = new IntentFilter();
		mInterFilter.addAction(EmsgManager.ACTION_ADDFRIENDCALLBACK);
		mContext.registerReceiver(new NewFriendsComesBrodCastReciver(),
				mInterFilter);

		mJsonArray = new JsonArray();

		mFriendsAdapter = new FriendsListAdapter(mContext, mJsonArray);
		mSwipListView = new SwipListViewComponents(getActivity());
		mSwipListView.setSwipCallBack(new OnSwipCallBack() {

			@Override
			public void onReflesh() {
				getAllFriendsData();
			}

			@Override
			public void onLoadMore() {

			}

			@Override
			public void onItemClickListener(int position) {
				String mId = JsonUtil.getAsString(
						mFriendsAdapter.getItem(position),
						FriendsListAdapter.USRID);
				String uName = JsonUtil.getAsString(
						mFriendsAdapter.getItem(position),
						FriendsListAdapter.NICKNAME);
				Intent mIntent = new Intent(mContext, ChatActivity.class);
				mIntent.putExtra(ChatActivity.TAG_MESSAGE_TO, mId);
				mIntent.putExtra(ChatActivity.TAG_CHAT_NAME, uName);
				mContext.startActivity(mIntent);
			}
		});
		mSwipListView.setAdapter(mFriendsAdapter);
		mLayoutBody.addView(mSwipListView.getView());

		getAllFriendsData();
	}

	private final int REQUESTCODE_GETFRIEND = 101;

	// getFriends(所有的好友)
	private void getAllFriendsData() {
		showProgresssDialog();
		sendData(UserUpLoadJsonData.getFriendsForUserNameValuePair(),
				AppDefine.URL_USER_GETFRIENDS, this, REQUESTCODE_GETFRIEND);
	}

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
		mSwipListView.onLoadOver();
		dismissProgressDialog();
	}

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		dismissProgressDialog();
		mSwipListView.onLoadOver();
		JsonArray mJsonList = null;
		try {
			mJsonList = JsonUtil.getAsJsonArray(mJsonResult, "friend_list");
		} catch (Exception e) {
			return;
		}
		if (mJsonList != null) {
			this.mJsonArray = mJsonList;
			mFriendsAdapter.changeDataSource(mJsonArray);

			for (int i = 0; i < mJsonList.size(); i++) {
				JsonObject mJsonObject = (JsonObject) mJsonList.get(i);
				updateUserAndIcon(mJsonObject);
			}
		}
		// 更新用户信息
	}

	DBUSER mDbUser = new DBUSER();
	MSGHISTORY mMsgDb = new MSGHISTORY();
	DBFRIEND mDbFriend = new DBFRIEND();

	private void updateUserAndIcon(JsonObject mJsonObj) {
		String nickName = JsonUtil.getAsString(mJsonObj, "nickname");
		String iconUrl = JsonUtil.getAsString(mJsonObj, "photo");
		String userId = JsonUtil.getAsString(mJsonObj, "userId");
		String phone = JsonUtil.getAsString(mJsonObj, "phone");
		// 更新表 若当前数据类型没变化 则更新界面
		String sql = mActivity
				.getString(R.string.update_chathistory_single_userinfo);
		String sqlFormat = mActivity.getStringFormat(sql, nickName, iconUrl,
				userId + EmsgManager.EMSGAREA);
		// 保存到用户表
		User mUser = new User();
		mUser.nickname = nickName;
		mUser.userid = userId;
		mUser.photo = iconUrl;
		mUser.phone = phone;
		mDbUser.saveData(mUser);

		FriendTable mFriendTable = new FriendTable();
		mFriendTable.userId = userId;
		mDbFriend.saveData(mFriendTable);

		mMsgDb.updateDataFromDb(sqlFormat);
	}

	class NewFriendsComesBrodCastReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if (!mActivity.isFinishing())
				getAllFriendsData();
		}

	}

}
