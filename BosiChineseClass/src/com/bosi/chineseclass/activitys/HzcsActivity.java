package com.bosi.chineseclass.activitys;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.control.HzcsDitalContarol;
import com.bosi.chineseclass.control.SampleHolderControlMake;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.activity_layout_hzcs)
public class HzcsActivity extends BaseActivity{
	//汉字起源 趣味汉字 造字方法 汉字演变
	@ViewInject(R.id.headactionbar)
	View mViewHead;
	HeadLayoutComponents mHeadActionBar;
	
	
    @Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mHeadActionBar = new HeadLayoutComponents(this, mViewHead);
	}
	//造字方法
	@OnClick(R.id.iv_hzcs_zzff)
	public void actionZzff(View mView){
		
	}
	@OnClick(R.id.iv_hzcs_qwhz)
	public void actionQwhz(View mView){
		
	}
	@OnClick(R.id.iv_hzcs_hzyb)
	public void actionHzyb(View mView){
		_intentTo_("HzybDitalFragment");
	}
	//汉字起源	
	@OnClick(R.id.iv_hzcs_hzqy)
	public void actionHzqy(View mView){
		_intentTo_("HzqyDitalFragment");
	}
	//汉字演变
	public void actionHzyb(){
		
	}
	//趣味汉字
	public void actionQwhz(){
		
	}

	
	private void _intentTo_(String frgmentName){
		Intent mIntent = new Intent(this,SampleHolderActivity.class);
		mIntent.putExtra(SampleHolderControlMake.mControlName, "HzcsDitalContarol");
		mIntent.putExtra(HzcsDitalContarol.KEY_HZCSFUNCNAME, frgmentName);
		startActivity(mIntent);
	}


}
