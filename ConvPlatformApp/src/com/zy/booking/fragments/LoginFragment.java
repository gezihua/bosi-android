package com.zy.booking.fragments;

import android.content.Intent;


import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.zy.booking.AppDefine;
import com.zy.booking.R;
import com.zy.booking.activitys.AuthActivity;
import com.zy.booking.activitys.IndexActivity;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.json.IspUpLoadJsonData;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.PreferencesUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LoginFragment extends BaseFragment implements OnHttpActionListener{

	@ViewInject(R.id.headactionbar)
	View mViewHead;
	
	HeadLayoutComponents mHeadActionBar;
	
	OnActionRegitsterListener mActionRegListener;
	@ViewInject(R.id.et_useraccount)
	EditText mEditUserAccount;
	@ViewInject(R.id.et_password)
	EditText mEditPassword;
	
	@ViewInject(R.id.bt_login)
	Button mBtLogin;
	
	public void setOnActionResgister(OnActionRegitsterListener mListener){
		this.mActionRegListener = mListener;
	}
	
	
	
	public void initJudge(){
		
		isLogined();
//		if(isLogined()){
//			intentToMain();
//		}
	}
	private void intentToMain(){
		Intent intent = new Intent(getActivity(),IndexActivity.class);
		startActivity(intent);
		getActivity().finish();
	}
	private void isLogined(){
		String loginAccount = PreferencesUtils.getString(getActivity(), "account");
		if(!TextUtils.isEmpty(loginAccount))
		mEditUserAccount.setText(loginAccount);
	}
	public void afterViewInject(){
		initJudge();
		
		mHeadActionBar= new HeadLayoutComponents(getActivity(), mViewHead);
		mHeadActionBar.setTextMiddle("登陆", -1);
		mHeadActionBar.setTextRight("注册", -1);
		mHeadActionBar.setRightOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mActionRegListener.onRegister();
			}
		});
		
		mBtLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 filterLogin();
			}
		});
	}
	String uName;
	String uPassword;
	private void filterLogin(){
		String mTextAccount =uName= mEditUserAccount.getText().toString().trim();
		String mTextPassword =uPassword= mEditPassword.getText().toString().trim();
		if(TextUtils.isEmpty(mTextAccount)){
			 YoYo.with(Techniques.Shake)
             .duration(700)
             .playOn(mEditUserAccount);
			 return;
		}
	   if(TextUtils.isEmpty(mTextPassword)){
			 YoYo.with(Techniques.Shake)
             .duration(700)
             .playOn(mEditPassword);
			 return;
		}
	   showProgresssDialog();
	    sendData(IspUpLoadJsonData.getLoginDataList(uName,uPassword), AppDefine.BASE_URL_LOGIN, this, 101);
	}

	public interface OnActionRegitsterListener{
		public void onRegister();
	}

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
		dismissProgressDialog();
	}

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		dismissProgressDialog();
		if(isSuccess){
			if(mJsonResult!=null){
				String token = JsonUtil.getAsString(mJsonResult, "token");
				String userId =  JsonUtil.getAsString(mJsonResult, "userId");
				((AuthActivity)getActivity()).onLoginSuccess(uName, token,userId);
			}
		}
	}

	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_layout_login, null);
	}
}
