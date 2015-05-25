package com.zy.booking.activitys;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emsg.sdk.util.JsonUtil;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.CpApplication.USER;
import com.zy.booking.R;
import com.zy.booking.components.CategorysGirdComponents;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.components.NiftyDialogComponents;
import com.zy.booking.components.NiftyDialogComponents.OnNiftyCallBack;
import com.zy.booking.components.SliderImagePageIndicator;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.modle.TimeAvialableAdapter;
import com.zy.booking.modle.TimeManagerAdapter.OnDateClickListener;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.CpTextUtils;
import com.zy.booking.util.IntentUtils;

@ContentView(R.layout.activity_dital)
public class ServiceDetailActivity extends BaseActivity implements
		OnHttpActionListener {

	@ViewInject(R.id.ll_container_dital)
	LinearLayout mLinearContainerDital;

	@ViewInject(R.id.ll_container_compent)
	LinearLayout mLinearContainerComp;

	SliderImagePageIndicator mSliderImgPageIndicator;


	String mCurrentId;

	@ViewInject(R.id.headactionbar)
	View mBaseView;

	@ViewInject(R.id.iv_phone)
	private View callView;

	HeadLayoutComponents mHeadActionBar;

	String phoneNum;

	DitalInfoActFrag mPlaneDital;

	public static final String TAG_ID = "id";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSliderImgPageIndicator = new SliderImagePageIndicator(
				getLayoutInflater(), this);
		mLinearContainerDital.addView(mSliderImgPageIndicator.getView());

		mCurrentId = getIntent().getStringExtra(TAG_ID);
		showProgresssDialog();
		mPlaneDital = new DitalInfoActFrag();
		// mCommentPanel = new CommentPanel();

		mHeadActionBar = new HeadLayoutComponents(this, mBaseView);
		mHeadActionBar.setTextMiddle("服务明细", -1);
		mHeadActionBar.setTextRight("发表评论", -1,14);
		mHeadActionBar.setRightOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent mIntent = new Intent(mContext,CommentActivity.class);
				mIntent.putExtra(CommentActivity.TAG_ID, mCurrentId);
				startActivity(mIntent);
			}
		});
		mHeadActionBar.setDefaultLeftCallBack(true);
		showNotifyDialog();

	}

	private void intentChat() {
		Intent intent = new Intent(this, ChatActivity.class);
		intent.putExtra("content", mLeaverMessage);
		intent.putExtra(ChatActivity.TAG_CHAT_NAME, "店家");
		intent.putExtra(ChatActivity.TAG_MESSAGE_TO, mIspId);
		startActivity(intent);
	}

	String mIspId;

	@OnClick(R.id.iv_phone)
	public void actionPhone(View mView) {
		if (!TextUtils.isEmpty(phoneNum))
			IntentUtils.intentToTel(phoneNum, mContext);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 控制详情面板
	 * */
	class DitalInfoActFrag {
		@ViewInject(R.id.tv_title)
		private TextView mTtitle;
		@ViewInject(R.id.tv_comment)
		private TextView mTcontent;
		@ViewInject(R.id.tv_test01)
		private TextView mTTest01;
		@ViewInject(R.id.tv_test02)
		private TextView mTest02;
		@ViewInject(R.id.tv_test03)
		private TextView mTest03;
		@ViewInject(R.id.rb_dital)
		private RatingBar mRatingBar;

		@ViewInject(R.id.ll_timeavailable)
		private LinearLayout mLinearTimeAvailable;

		@ViewInject(R.id.iv_dital_chat)
		private View chatView;
		@ViewInject(R.id.wv_showprice)
		private WebView mWebView;

		// 添加一个时间查看的相关grid
		CategorysGirdComponents mCateGorysGirdComponents;
		TimeAvialableAdapter mCategoryAdapter;
		JsonArray mArray = new JsonArray();

		public DitalInfoActFrag() {
			View mView = getLayoutInflater().inflate(
					R.layout.layout_contentdital, null);
			mLinearContainerDital.addView(mView);

			ViewUtils.inject(this, mView);

			initCategorys();
			mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
			getIntentData();

		}

		@OnClick(R.id.iv_dital_chat)
		public void actionChat(View mView) {
			intentChat();
		}

		/**
		 * 
		 */
		private void initCategorys() {
			mCateGorysGirdComponents = new CategorysGirdComponents(null,
					mContext);
			mLinearTimeAvailable.addView(mCateGorysGirdComponents.getView());
			mCateGorysGirdComponents.setVerNumber(1);
			mCateGorysGirdComponents.setBackGroudResource(R.drawable.xukuang);
		}

		private void getIntentData() {
			sendData(UserUpLoadJsonData.getProviderDitalNamePairs(mCurrentId),
					AppDefine.BASE_URL_SEARCHDITAL, ServiceDetailActivity.this,
					101);
		}

		public void addAdapter(JsonArray mTimeJson) {
			mCategoryAdapter = new TimeAvialableAdapter(mContext, mTimeJson);
			mCategoryAdapter.setOnDateClick(new OnDateClickListener() {

				@Override
				public void onDateClick(final String date, final String time) {
					final NiftyDialogComponents mNiftyDialog = new NiftyDialogComponents(
							mContext);
					mNiftyDialog.setNoftyCallBack(new OnNiftyCallBack() {

						@Override
						public void onBt2Click() {
							mNiftyDialog.dismissBuilder();
						}

						@Override
						public void onBt1Click() {
							askForService(date, time);
						}
					});
					String message = getResourceFromId(R.string.dialog_askserver_msg);
					String title = getResourceFromId(R.string.dialog_default_title);
					String bt1 = getResourceFromId(R.string.dialog_default_bt1);
					String bt2 = getResourceFromId(R.string.dialog_default_bt2);

					mNiftyDialog.setUpNifty(bt1, bt2, title, message);
					mNiftyDialog.showBuilder();
				}

				@Override
				public void onDateClick(JsonArray time) {

				}

				@Override
				public void onDateClick(JsonArray time, String unit) {
					
				}
			});
			mCateGorysGirdComponents.setAdapter(mCategoryAdapter);
		}

		public void setText(JsonObject mJsonDital, boolean canServiceIdea) {
			dismissProgressDialog();
			try {

				mIspId = JsonUtil.getAsString(mJsonDital, "userId");
				// 标题
				mTtitle.setText(JsonUtil.getAsString(mJsonDital, "name"));

				// 介绍
				String instroduction = JsonUtil.getAsString(mJsonDital,
						"introduction");
				mTcontent.setText(getStringFormat(
						getResourceFromId(R.string.spdital), instroduction));

				mTTest01.setText(getResourceFromId(R.string.dital_place)
						+ JsonUtil.getAsString(mJsonDital, "address"));
				phoneNum = JsonUtil.getAsString(mJsonDital, "phone");

				String textContact = CpTextUtils.getHtmlTelString(phoneNum);
				String ucontact = getResourceFromId(R.string.dital_phone)
						+ textContact;
				mTest02.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View paramView) {
						IntentUtils.intentToTel(phoneNum, mContext);
					}
				});

				mTest02.setText(Html.fromHtml(ucontact));
				mLeaverMessage = JsonUtil
						.getAsString(mJsonDital, "leavmessage");
				// mRatingBar.setRating(JsonUtil.getAsInt(mJsonDital,
				// "numrating"));

				// 预览图片
				Map<String, String> mPicsMap = new HashMap<String, String>();
				for (int i = 0; i < 3; i++) {
					String url = JsonUtil.getAsString(mJsonDital, "img" + i);
					if (url != null) {
						mPicsMap.put("img" + i, url);
					}
				}
				mSliderImgPageIndicator.initData(mPicsMap);

				// 营业时间
				String uopenTime = getResourceFromId(R.string.dital_worktime);
				if (!canServiceIdea) {
					uopenTime = uopenTime + "服务暂时未开放";
				}
				mTest03.setText(uopenTime);

				// 价格显示
				String jsonData = JsonUtil.getAsString(mJsonDital,
						"priceDescription");
				if (jsonData != null)
					mWebView.loadData(jsonData, "text/html; charset=UTF-8",
							null);
			} catch (Exception e) {
			}
			playYoYo(chatView);
			// mCommentPanel.loadData();
		}
	}

	/**
	 * 
	 * 评论详情面板
	 */

	String mLeaverMessage = null;

	private void showNotifyDialog() {
		mLinearContainerComp.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (TextUtils.isEmpty(mLeaverMessage))
					return;
				final NiftyDialogBuilder mBuilder = NiftyDialogBuilder
						.getInstance(mContext).withButton1Text("联系店家")
						.withMessage(mLeaverMessage)
						.withEffect(Effectstype.Shake).withButton2Text("不了谢谢")
						.withTitle("温馨提示");
				mBuilder.setButton1Click(new OnClickListener() {

					@Override
					public void onClick(View paramView) {
						intentChat();
						mBuilder.dismiss();
					}
				});
				mBuilder.setButton2Click(new OnClickListener() {

					@Override
					public void onClick(View paramView) {
						mBuilder.dismiss();
					}
				});
				try {
					mBuilder.show();
				} catch (Exception e) {
				}

			}
		}, 5000);
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	private void askForService(String date, String time) {
		String tempTime = "1";
		if (time.equals("am")) {
			tempTime = "1";
		} else if (time.equals("pm")) {
			tempTime = "2";
		} else {
			tempTime = "3";
		}
		List<NameValuePair> reqData = UserUpLoadJsonData
				.getUpLoadBookingNameValuePairs(date, mCurrentId, tempTime);
		showProgresssDialog();

		sendData(reqData, AppDefine.BASE_URL_BOOKING, this, 102);

		CpApplication.getApplication().mDbManager.askTheService(mCurrentId);
	}

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
		dismissProgressDialog();
	}

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {

		dismissProgressDialog();

		if (resultCode == 101) {
			JsonObject mJsonBooking = JsonUtil.getAsJsonObject(mJsonResult,
					"booking_open");
			boolean isServiceOpen = false;
			if (mJsonBooking != null) {
				JsonArray mJsonArray = mJsonBooking.getAsJsonArray("dateList");

				if (mJsonBooking != null && mJsonArray != null
						&& mJsonArray.size() > 0) {
					isServiceOpen = true;
					mPlaneDital.addAdapter(mJsonArray);
				}
			}

			mPlaneDital.setText(JsonUtil.getAsJsonObject(mJsonResult, "sp"),
					isServiceOpen);
		} else {
			if (isSuccess) {
				Toast.makeText(this, "预定成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

}
