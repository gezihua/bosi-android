package com.bosi.chineseclass.activitys;

import android.content.Intent;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.XutilImageLoader;
import com.bosi.chineseclass.control.SampleControl;
import com.bosi.chineseclass.control.SampleHolderControlMake;
import com.bosi.chineseclass.han.activitys.ZiYuanActivity;
import com.bosi.chineseclass.han.db.CheckDbUtils;
import com.bosi.chineseclass.han.db.DbManager;
import com.bosi.chineseclass.su.ui.actvities.DictionaryAcitvity;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.update.UmengUpdateAgent;

@ContentView(R.layout.main)
public class MainActivity extends BaseActivity {
	@ViewInject(R.id.ll_main_body)
	LinearLayout mlayoutBody;
	
	XutilImageLoader mXutilImageLoader;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		UmengUpdateAgent.update(this);
		getDataAsy();
		mXutilImageLoader = new XutilImageLoader(this);
		mXutilImageLoader.getBitmapFactory().display(mlayoutBody, "assets/bosi_index_bg.jpg");
		
	}
	@OnClick(R.id.btn_pinyinlearn)
	public void actionPyLearn(View mView){
		Intent mIntent = new Intent(this,SampleHolderActivity.class);
		mIntent.putExtra(SampleHolderControlMake.mControlName, "PinYinLearnControl");
		startActivity(mIntent);
		
	}
	@OnClick(R.id.main_btn_hzcs)
	public void actionHzcs(View mView){
		Intent mIntent = new Intent(this,HzcsActivity.class);
		startActivity(mIntent);
	}
	@OnClick(R.id.btn_bphz)
	public void actionBphz(View mView){
		Intent mIntent = new Intent(this,SampleHolderActivity.class);
		mIntent.putExtra(SampleControl.KEY_FRAGMENTNAMES, new String []{"BphzLevFragment"});
		mIntent.putExtra(SampleControl.KEY_PACKAGETNAME ,"com.bosi.chineseclass.fragments");
		startActivity(mIntent);
	}

	@OnClick(R.id.bt_zy)
	public void actionZy(View mView){
		Intent ziyuanIntent = new Intent(mContext, ZiYuanActivity.class);
		startActivity(ziyuanIntent);
	}
	@OnClick(R.id.btn_zyzd)
	public void actionZyzd(View mView){
		
		Intent intent = new Intent(MainActivity.this, DictionaryAcitvity.class);
		startActivity(intent);
	}
	private void getDataAsy(){
		updateProgress(1, 2);
		
		AsyTaskBaseThread(new Runnable() {
			
			@Override
			public void run() {
				 boolean isSuccess = CheckDbUtils.checkDb();
				 if(!isSuccess){
					 finish();
				 }
				 BSApplication.getInstance().mDbManager = new DbManager(mContext);
			}
		},new Runnable() {
			
			@Override
			public void run() {
				runOnUiThread( new Runnable() {
					
					@Override
					public void run() {
						updateProgress(2, 2);
					}
				});
			}
		});
	}
	
	
	@Override
	public void onBackPressed() {
		BSApplication.getInstance().exitApp();
	}

}
