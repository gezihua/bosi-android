package com.zy.booking.activitys;

import java.io.File;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

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
import com.zy.booking.json.ModelUploadJsonData;

@ContentView(R.layout.layout_common_withoutscroll)
public class UpLoadImageToAlbumActivity extends BaseActivity {

	public static final String TAG_ALBUMID = "albumid";

	SelectImageGirdComponents mSelectImageGirdView;

	@ViewInject(R.id.headactionbar)
	View mViewHead;

	@ViewInject(R.id.ll_container_body)
	LinearLayout mLinearLayout;

	HeadLayoutComponents mHeadActionBar;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		LayoutParams mLayoutParams = (LayoutParams) mLinearLayout
				.getLayoutParams();
		mLayoutParams.setMargins(0, 10, 0, 0);

		mHeadActionBar = new HeadLayoutComponents(mContext, mViewHead);
		mHeadActionBar.setDefaultLeftCallBack(true);
		mHeadActionBar.setTextMiddle("上传图片", -1);
		mHeadActionBar.setTextRight("上传", -1);
		mHeadActionBar.setRightOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				uploadImageData();
			}
		});

		mSelectImageGirdView = new SelectImageGirdComponents(mContext,
				mLinearLayout);
		mLinearLayout.addView(mSelectImageGirdView.getView());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mSelectImageGirdView.onActivityResult(requestCode, resultCode, data);
	}

	private void uploadImageData() {

		String albumsId = getIntent().getStringExtra(TAG_ALBUMID);

		if (mSelectImageGirdView.mListFilePath.size() == 0)
			return;
		ArrayList<File> mFiles = new ArrayList<File>();
		for (String filePath : mSelectImageGirdView.mListFilePath) {
			mFiles.add(new File(filePath));
		}
		showProgresssDialog();
		CpApplication.getApplication().mHttpPack.sendDataAndImgs(
				ModelUploadJsonData.addAlbumsImageNameValuePairs(albumsId,
						"test"), AppDefine.URL_ALBUM_ADDIMAGE, mFiles,
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
										dismissProgressDialog();
										if (isSuccess) {
											showToastShort("图片上传成功");
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

}
