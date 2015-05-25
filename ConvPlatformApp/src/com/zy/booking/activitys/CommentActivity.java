package com.zy.booking.activitys;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.R;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.components.SwipListViewComponents.OnSwipCallBack;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.modle.CommentTopicAdapter;
import com.zy.booking.modle.CommentTopicAdapter.CallBack;
import com.zy.booking.struct.OnHttpActionListener;

@ContentView(R.layout.layout_common_withoutscroll)
public class CommentActivity extends BaseActivity implements
		OnHttpActionListener,CallBack {

	@ViewInject(R.id.headactionbar)
	private View mViewHead;

	private HeadLayoutComponents mLayoutComponents;

	private SwipListViewComponents mListViewComponents;

	@ViewInject(R.id.ll_container_body)
	private LinearLayout mLayoutBody;

	public static final String TAG_ID = "tag-id";

	
	CommentTopicAdapter mTopicAdapter;
	
	JsonArray mJsonData = new JsonArray();
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		mListViewComponents = new SwipListViewComponents(this);
		mLayoutComponents = new HeadLayoutComponents(this, mViewHead);
		mLayoutComponents.setTextMiddle("评论详情", -1);
		mLayoutComponents.setTextRight("发布", -1);
		mLayoutComponents.setRightOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent mIntent = new Intent(mContext,SendCommentActivity.class);
				mIntent.putExtra(TAG_ID, getIntent().getStringExtra(TAG_ID));
				startActivity(mIntent);
			}
		});
		mLayoutComponents.setDefaultLeftCallBack(true);
		mListViewComponents.setSwipCallBack(new OnSwipCallBack() {

			@Override
			public void onReflesh() {
				getTopicData();
			}

			@Override
			public void onLoadMore() {

			}

			@Override
			public void onItemClickListener(int position) {

			}
		});
		mLayoutBody.addView(mListViewComponents.getView());
		mTopicAdapter = new CommentTopicAdapter(this, mJsonData);
		mTopicAdapter.setCallBack(this);
		mListViewComponents.setAdapter(mTopicAdapter);
		getTopicData();
	}

	private final int CODE_GETTOPICS = 101;

	private void getTopicData() {
		showProgresssDialog();
		String id = getIntent().getStringExtra(TAG_ID);
		sendData(UserUpLoadJsonData.getTopicsNameValuePairs(id, 0, 10),
				AppDefine.URL_EVALUATION_GETTOPICS, this, CODE_GETTOPICS);
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
		//
		mListViewComponents.onLoadOver();
	}

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		dismissProgressDialog();
		mListViewComponents.onLoadOver();
		
		if(mJsonResult.has("topic_list")){
			JsonArray mJsonData = mJsonResult.getAsJsonArray("topic_list");
			this.mJsonData = mJsonData;
			if(mJsonData!=null&&mJsonData.size()>0){
				mTopicAdapter.changeDataSource(this.mJsonData);
			}
		}
	}


	@Override
	public void onCallBack() {
		getTopicData();
		
	}

}
