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
	@OnClick(R.id.activity_hzcs_iv_zzff)
	public void actionZzff(View mView){
		
	}
	//汉字起源	
	@OnClick(R.id.iv_activity_hzcs_hzqy)
	public void actionHzqy(View mView){
		Intent mIntent = new Intent(this,SampleHolderActivity.class);
		mIntent.putExtra(SampleHolderControlMake.mControlName, "HzcsDitalContarol");
		mIntent.putExtra(HzcsDitalContarol.KEY_HZCSFUNCNAME, "HzqyDitalFragment");
		startActivity(mIntent);
	}
	//汉字演变
	public void actionHzyb(){
		
	}
	//趣味汉字
	public void actionQwhz(){
		
	}



}
