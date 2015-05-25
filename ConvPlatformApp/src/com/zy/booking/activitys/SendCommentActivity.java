package com.zy.booking.activitys;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.components.SelectImageGirdComponents;
import com.zy.booking.json.DecodeResult;
import com.zy.booking.json.IDecodeJson;
import com.zy.booking.json.UserUpLoadJsonData;

@ContentView(R.layout.layout_sendcomment)
public class SendCommentActivity extends BaseActivity {

	@ViewInject(R.id.headactionbar)
	View mViewHead;
	HeadLayoutComponents mHeadLayoutComponents;

	@ViewInject(R.id.et_sendcomment)
	EditText mEditComment;

	@ViewInject(R.id.ll_sendcomment)
	LinearLayout mllBodyDital;

	SelectImageGirdComponents mSelectImageGirdView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mHeadLayoutComponents = new HeadLayoutComponents(this, mViewHead);
		mHeadLayoutComponents.setDefaultLeftCallBack(true);
		mHeadLayoutComponents.setTextMiddle("发表评论", -1);
		mHeadLayoutComponents.setTextRight("发表", -1);
		mHeadLayoutComponents.setRightOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sendTopicData();
			}
		});

		mSelectImageGirdView = new SelectImageGirdComponents(this, mllBodyDital);
		mllBodyDital.addView(mSelectImageGirdView.getView());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mSelectImageGirdView.mListBitmapLists != null) {
			for (Bitmap mBitmap : mSelectImageGirdView.mListBitmapLists) {
				if (mBitmap != null && !mBitmap.isRecycled())
					mBitmap.recycle();
			}

			mSelectImageGirdView.mListBitmapLists.clear();
			mSelectImageGirdView.mListFilePath.clear();
		}
		System.gc();
	}

	private String content = null;

	private void sendTopicData() {
		content = mEditComment.getText().toString();
		if (TextUtils.isEmpty(content)) {
			playYoYo(mEditComment);
			return;
		}
		showProgresssDialog();
		String spId = getIntent().getStringExtra(CommentActivity.TAG_ID);

		ArrayList<File> mFiles = new ArrayList<File>();
		for (String filePath : mSelectImageGirdView.mListFilePath) {
			mFiles.add(new File(filePath));
		}
		CpApplication.getApplication().mHttpPack.sendDataAndImgs(
				UserUpLoadJsonData.getAddTopicValueParis(spId, content),
				AppDefine.URL_EVALUATION_ADDTOPICS, mFiles,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						DecodeResult.decoResult(responseInfo.result,
								new IDecodeJson() {

									@Override
									public void onDecoded(String reason,
											boolean isSuccess,
											JsonObject mJsonResult,
											JsonArray mLists) {
										if (isSuccess) {
											showToastShort("发布成功");
											finish();
										}
									}
								});
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						showToastShort("网络异常,发布失败");
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mSelectImageGirdView.onActivityResult(requestCode, resultCode, data);
	}

}
