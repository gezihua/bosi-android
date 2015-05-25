package com.zy.booking.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.util.PreferencesUtils;
import com.zy.booking.wedget.CheckSwitchButton;

@ContentView(R.layout.activity_app_preference)
public class PreferenceActivity extends BaseActivity{
	@ViewInject(R.id.prefer_emsg_withshack)
	CheckSwitchButton mViewShakeOpen;
	
	@ViewInject(R.id.prefer_emsg_withvoice)
	CheckSwitchButton mViewVoiceeOpen;
	
	@ViewInject(R.id.headactionbar)
	View mViewHeadAction;
	
	HeadLayoutComponents mLayoutHead;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mLayoutHead= new HeadLayoutComponents(this, mViewHeadAction); 
		
		mLayoutHead.setTextMiddle("系统管理", -1);
		mLayoutHead.setDefaultLeftCallBack(true);
		boolean isShake = PreferencesUtils.getBoolean(this, "isshakeopen");
		mViewShakeOpen.setChecked(isShake);
		
		
		boolean isVoice = PreferencesUtils.getBoolean(this, "isvoiceeopen");
		mViewVoiceeOpen.setChecked(isVoice);
		
		
		mViewShakeOpen.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isCheck) {
				if(isCheck){
					PreferencesUtils.putBoolean(mContext, "isshakeopen", isCheck);
				}
			}
		});
		
		mViewVoiceeOpen.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isCheck) {
				if(isCheck){
					PreferencesUtils.putBoolean(mContext, "isvoiceeopen", isCheck);
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@OnClick(R.id.rl_loginout)
	public void actionLoginOut(View mView){
		showToastLong("正在注销中...");
		PreferencesUtils.putString(mContext, "token", "");
		
		CpApplication.getApplication().destroySystem();
	}
	
	
}
