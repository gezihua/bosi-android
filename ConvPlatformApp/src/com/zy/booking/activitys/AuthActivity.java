package com.zy.booking.activitys;

import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.lidroid.xutils.view.annotation.ContentView;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.db.DBFRIEND;
import com.zy.booking.db.MSGHISTORY;
import com.zy.booking.fragments.LoginFragment;
import com.zy.booking.fragments.LoginFragment.OnActionRegitsterListener;
import com.zy.booking.fragments.RegisterFragment;
import com.zy.booking.fragments.RegisterFragment.OnActionLoginListener;
import com.zy.booking.util.PreferencesUtils;

@ContentView(R.layout.activity_auth)
public class AuthActivity extends BaseActivity implements OnActionLoginListener,OnActionRegitsterListener{
	
	LoginFragment mFragmentLogin;
	RegisterFragment mFragmentRegister;
	
	FragmentManager mFragManager;
	

	@Override
	protected void onCreate(Bundle arg0)  {
		super.onCreate(arg0);
		
		if(isLogined()){
			intentToMain();
			finish();
		}
		mFragManager = getSupportFragmentManager();
		FragmentTransaction transaction = mFragManager.beginTransaction();  
		mFragmentLogin = new LoginFragment();  
		mFragmentLogin.setOnActionResgister(this);
        transaction.replace(R.id.fl_authcontent, mFragmentLogin);  
        transaction.commit();  		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	public void rmLoginFragment(){
		FragmentTransaction transaction = mFragManager.beginTransaction();  
		 mFragmentRegister = new RegisterFragment();
		 mFragmentRegister.setOnActionLogin(this);
		 transaction.replace(R.id.fl_authcontent, mFragmentRegister);  
	     transaction.commit();  	
	}
	
	public void rmRegisterFragment(){
		 FragmentTransaction transaction = mFragManager.beginTransaction();  
		 mFragmentLogin = new LoginFragment();
		 mFragmentLogin.setOnActionResgister(this);
		 transaction.replace(R.id.fl_authcontent, mFragmentLogin);  
	     transaction.commit();  	
	}
	
	private void intentToMain(){
	    Intent mIntent = new Intent(this,
                IndexActivity.class);
        startActivity(mIntent);
        finish();
	}
	
	public boolean isLogined(){
		return !TextUtils.isEmpty(PreferencesUtils.getString(mContext, "token"));
	}

	@Override
	public void onRegister() {
		rmLoginFragment();
	}

	@Override
	public void onLogin() {
		rmRegisterFragment();
	}
	
    public void onLoginSuccess(String userName ,String token ,String userId){
    	boolean isChangeUser = false;
    	String account  = PreferencesUtils.getString(this, "account");
    	if(account!=null &&!account.equals(userName)){
    		isChangeUser =true;
    	}
    	
    	if(isChangeUser){
			MSGHISTORY mDbMsg = new MSGHISTORY();
			mDbMsg.clearDbData();
			//判断是否更换用户 ，更换用户删除数据表
			PreferencesUtils.clear(this);
			CpApplication.getApplication().mEmsgManager.getEmsgClient().closeClient();
			
			DBFRIEND mDbFriend = new DBFRIEND();
			mDbFriend.clearDbData();
		}
    	PreferencesUtils.putString(this, "account", userName);
		PreferencesUtils.putString(this, "token", token);
		PreferencesUtils.putString(this, AppDefine.KEY_USERID, userId);
		
		//String nickName = PreferencesUtils.getString(this, "                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  ");
		showToastShort("登陆成功");
	
		intentToMain();
		finish();
	
    }
	
	

}
