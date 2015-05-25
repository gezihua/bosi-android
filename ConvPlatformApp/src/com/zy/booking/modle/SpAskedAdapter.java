package com.zy.booking.modle;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zy.booking.R;
import com.zy.booking.activitys.ChatActivity;
import com.zy.booking.activitys.ServiceDetailActivity;
import com.zy.booking.util.CpTextUtils;
import com.zy.booking.util.IntentUtils;
import com.zy.booking.util.ViewHolder;
import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SpAskedAdapter extends SampleListAdapter {

	private final String COMMENT = "comment";
	private final String USERID = "userId";

	private final String BOOINGITEM = "bookingItem";
	private final String USERSUMMARY = "userSummary";
	private final String SPUSERSUMMARY = "spUserSummary";
	private final String SPSUMMARY = "spSummary";

	private final String NICKNAME = "nickname";

	private final String PHONE = "phone";

	public SpAskedAdapter(Context mContext, JsonArray mData) {
		super(mContext, mData);
	}

	@Override
	public View getView(int posi, View viewTemp, ViewGroup arg2) {

		if (viewTemp == null) {
			viewTemp = LayoutInflater.from(context).inflate(
					R.layout.item_layout_ispdital, null);
		}
		try {
			showView(viewTemp, posi);
		} catch (Exception e) {

		}
		return viewTemp;
	}

	private void showView(View viewTemp, int position) throws Exception {
		JsonObject mJsonObj = getItem(position);

		JsonObject mJsonBooking = mJsonObj.getAsJsonObject(BOOINGITEM);
		try {
			
			//如果有 SPSUMMARY 这个字段且不为空 则 我可以直接点进去查看我预约的服务内容
			JsonObject mSpSummary = mJsonObj.getAsJsonObject(SPSUMMARY);
			final String id = JsonUtil.getAsString(mSpSummary, "id");
			viewTemp.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent mIntent = new Intent(context,
							ServiceDetailActivity.class);
					mIntent.putExtra(ServiceDetailActivity.TAG_ID, id);

					context.startActivity(mIntent);
				}
			});

		} catch (Exception e) {
		}

		JsonObject mUserSummer = null;
		try {
			mUserSummer = mJsonObj.getAsJsonObject(USERSUMMARY);
		} catch (Exception e) {
			mUserSummer = mJsonObj.getAsJsonObject(SPUSERSUMMARY);
		}

		final String userId = JsonUtil.getAsString(mUserSummer, USERID);
		final String userName = JsonUtil.getAsString(mUserSummer, NICKNAME);
		final String uname = context.getString(R.string.custromname, userName);

		String utime = context.getString(R.string.custromdital,
				JsonUtil.getAsString(mJsonBooking, COMMENT));
		final String phoneNum = JsonUtil.getAsString(mUserSummer, PHONE);
		String textContact = CpTextUtils.getHtmlTelString(phoneNum);
		String ucontact = context.getString(R.string.custromcontact,
				textContact);

		setData(viewTemp, userId, ucontact, uname, utime);
	}

	private void setData(View viewTemp, final String userId,
			final String phoneNum, String userName, String dital) {
		TextView mTvUname = ViewHolder.get(viewTemp, R.id.tv_itemlayout_uname);
		TextView mTvAtime = ViewHolder
				.get(viewTemp, R.id.tv_itemlayout_asktime);
		TextView mTvAtContact = ViewHolder.get(viewTemp,
				R.id.tv_itemlayout_fcomment);
		ImageView mImageView = ViewHolder
				.get(viewTemp, R.id.iv_itemlayout_chat);

		mTvUname.setText(userName);

		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				Intent intent = new Intent(context, ChatActivity.class);
				intent.putExtra(ChatActivity.TAG_MESSAGE_TO, userId);
				context.startActivity(intent);
			}
		});

		mTvAtime.setText(dital);

		mTvAtContact.setText(Html.fromHtml(phoneNum));
		mTvAtContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				IntentUtils.intentToTel(phoneNum, context);
			}
		});

	}

}
