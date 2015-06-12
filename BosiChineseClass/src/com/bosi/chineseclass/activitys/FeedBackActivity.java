package com.bosi.chineseclass.activitys;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
/**
 * 用于自定义 反馈界面
 * 
 * */
@ContentView(R.layout.activity_feedback)
public class FeedBackActivity extends BaseActivity{

	@ViewInject(R.id.ll_feedbackcontainer)
	LinearLayout mLayoutFeedBack;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

}





























