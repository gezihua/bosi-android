package com.bosi.chineseclass.activitys;

import java.util.ArrayList;


import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.AppDefine.URLDefine;
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.OnHttpActionListener;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.db.BPCY;
import com.bosi.chineseclass.db.BPHZ;
import com.bosi.chineseclass.han.util.PreferencesUtils;
import com.bosi.chineseclass.utils.BosiUtils;
import com.bosi.chineseclass.utils.NetStateUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.layout_authpage)
public class AuthActivity extends BaseActivity {

	CardUserLoginManager mCardUserManager;
	PhoneUserManager  mPhoneUserManager;
	@ViewInject(R.id.fragment_login_carduser)
	View mViewCardUser;
	
	@ViewInject(R.id.fragment_login_phoneuser)
	View mViewPhoneUser;
	
	
	
	private final int ROLE_USER_CARD = 0;
	private final int ROLE_USER_PHONE = 1;
	private  int ROLE_TEMP_CURRENT = -1;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		ROLE_TEMP_CURRENT = ROLE_USER_CARD;
		mCardUserManager = new CardUserLoginManager();
		mPhoneUserManager = new PhoneUserManager();
		initPanelBaseRole();
	}



	private void initPanelBaseRole(){
		if(ROLE_TEMP_CURRENT == ROLE_USER_CARD){
			mCardUserManager.initCardUserPanel();
		}else{
			mPhoneUserManager.initViewPanel();
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
		super.onActivityResult(resultCode, resultCode, mIntent);
		
		if(resultCode == Activity.RESULT_OK){
			
			if(mIntent!=null){
				
				String mphone = mIntent.getStringExtra(RegisterActivity
						.RESULT_PHONE);
				
				String mPassword = mIntent.getStringExtra(RegisterActivity
						.RESULT_PASSWORD);
				
				
				if(mPhoneUserManager!=null){
					mPhoneUserManager.actionRegisterActivityResult(mphone, mPassword);
				}
			}
		}
		
		
	}
	/**
	 * 手机用户登录 需要获取验证码
	 * 
	 * */
	class PhoneUserManager implements OnHttpActionListener{
		
		public final static int mPhoneUserRequest = 111;
		@ViewInject(R.id.login_et_phoneuser_phonenum)
		EditText mEtPhoenNum;
		
		@ViewInject(R.id.login_et_snscode)
		EditText mEtSnsCode;
		@ViewInject(R.id.register_tv_phonelogin)
		View mViewRegister;
		
		
		
		public PhoneUserManager(){
			ViewUtils.inject(this, mViewPhoneUser);
		}
		@OnClick(R.id.login_tv_showcardpanel)
		public void actionShowCardInfoPanel(View mView){
			mViewPhoneUser.setVisibility(View.GONE);
			mViewCardUser.setVisibility(View.VISIBLE);
			ROLE_TEMP_CURRENT = ROLE_USER_CARD;
		}
		
		@OnClick(R.id.register_tv_phonelogin)
		public void actionRegister(View mView){
			Intent mIntent = new Intent(mContext,RegisterActivity.class);
			mContext.startActivityForResult(mIntent, mPhoneUserRequest);
		}
		private final int CODE_CHECKENABLETIME = 102;
		private final int CODE_PHONEREGISTER = 101;
		
		//查询是否超时
		private void actionCheckOutOfTimeUsed(String uid){
			//手机号登陆
			List<NameValuePair> mList = new ArrayList<NameValuePair>();
			showProgresssDialogWithHint("检查用户权限中...  ");
			
			
			
			JSONObject mObj = new JSONObject();
			try {
				mObj.put("product", "6");
				mObj.put("uid", uid);
			} catch (JSONException e) {
				
			}
			mList.add(new BasicNameValuePair("body", mObj.toString()));
			
			BSApplication.getInstance().sendData(mList, URLDefine.URL_CHECKPHONEUSETIME,
					this, CODE_CHECKENABLETIME,HttpMethod.GET);
		}
		
	    public void actionRegisterActivityResult(String mPhone ,String password){
	    	mEtPhoenNum.setText(mPhone);
	    	mEtSnsCode.setText(password);
	    	sendPhoneLogin(null);
	    }
	    
		
	    @OnClick(R.id.bt_login)
		public void sendPhoneLogin(View mView){
	    	
	    	String mPhone = mEtPhoenNum.getText().toString();
	    	String mPassword = mEtSnsCode.getText().toString();
	    	if(TextUtils.isEmpty(mPhone)){
	    		((BaseActivity)mContext).playYoYo(mEtPhoenNum);
	    		return ;
	    	}
	    	
	    	if(TextUtils.isEmpty(mPassword)){
	    		((BaseActivity)mContext).playYoYo(mEtSnsCode);
	    		return ;
	    	}
	    	
			//手机号登陆
			List<NameValuePair> mList = new ArrayList<NameValuePair>();
			mList.add(new BasicNameValuePair("account", mPhone));
			mList.add(new BasicNameValuePair("password", mPassword));
			mList.add(new BasicNameValuePair("product", "6"));
			mList.add(new BasicNameValuePair("ei", BSApplication
					.getInstance().getImei()));
			showProgresssDialogWithHint("登录中...  ");
			BSApplication.getInstance().sendData(mList, URLDefine.URL_PHONELOGIN,
					this, CODE_PHONEREGISTER,HttpMethod.POST);
	    	
		}
		
		@Override
		public void onHttpSuccess(JSONObject mResult, int code) {
			dismissProgressDialog(); // 登录成功
			String message =null;
			if (mResult.has("code")) {
				try {
					String codeResult = mResult.getString("code");
					if(mResult.has("msg")){
						 message = mResult.getString("msg");
					}
					
					if (codeResult.equals(AppDefine.ZYDefine.CODE_SUCCESS)) {
						if(code ==CODE_PHONEREGISTER){
							JSONObject mJson = mResult.getJSONObject("data");
							String uid = mJson.getString("id");
							//todo
							PreferencesUtils.putString(mContext,
									AppDefine.ZYDefine.EXTRA_DATA_USERID,uid);
							actionCheckOutOfTimeUsed(uid);
						}
						else if(code ==CODE_CHECKENABLETIME){
							JSONObject mJson = mResult.getJSONObject("data");
							String uid = mJson.getString("id");
							PreferencesUtils.putString(mContext,
									AppDefine.ZYDefine.EXTRA_DATA_USERID,uid);
							actionPhoneLoginSuccess();
							intentToSystem();
						}
					
					} else {
						if (!TextUtils.isEmpty(message))
							showToastShort(message);
						else {
							showToastShort("登陆失败");
						}
					}
		
				} catch (JSONException e) {
					showToastShort("后台数据异常");
				}
			} else {
				showToastShort("服务异常");
			}
		}

		@Override
		public void onHttpError(Exception e, String reason, int code) {
			showToastShort("登陆失败");
			dismissProgressDialog(); 
			
		}
		
		public void initViewPanel(){
			mViewPhoneUser.setVisibility(View.VISIBLE);
		}
		
		//手机号登陆成功 清空学习记录 ，并且
		BPCY mBpcy = new BPCY();
		BPHZ mBphz = new BPHZ();
		
		private void actionPhoneLoginSuccess(){
			
			mBpcy.clearDbData();
			mBphz.clearDbData();
			BSApplication.getInstance().mCurrentLoginRole = ROLE_USER_PHONE;
			BSApplication.getInstance().mTimeminPhoneUserLoginTime = System.currentTimeMillis();
		}
		
	}

	/**
	 * 用户卡用户管理
	 * 
	 * */
	class CardUserLoginManager implements OnHttpActionListener {
		@OnClick(R.id.login_tv_showphoneuserpanel)
		public void actionShowPhoneUserPanel(View mView){
			mViewPhoneUser.setVisibility(View.VISIBLE);
			mViewCardUser.setVisibility(View.GONE);
			ROLE_TEMP_CURRENT = ROLE_USER_PHONE;
		}
		
		public CardUserLoginManager(){
			ViewUtils.inject(this, mViewCardUser);
		}
		@ViewInject(R.id.et_account)
		private EditText mEditAccount;
		@ViewInject(R.id.et_password)
		private EditText mEditPassword;
		@ViewInject(R.id.et_phonenum)
		private EditText mEditPhone;
		
		/**
		 * 通讯交互成功
		 * 
		 * */
		@Override
		public void onHttpSuccess(JSONObject mResult, int code) {

			// {"code":"1","message":"","data":"{}"} 0 登陆失败 1成功 2 用户名不存在 3 密码不正确 4 超出使用权限
			dismissProgressDialog(); // 登录成功
			String message =null;
			if (mResult.has("code")) {
				try {
					String codeResult = mResult.getString("code");
					if(mResult.has("message")){
						 message = mResult.getString("message");
					}

					if (codeResult.equals(AppDefine.ZYDefine.CODE_SUCCESS)) {
						showToastShort("登陆成功");
						JSONObject mData = mResult.getJSONObject("data");
						String id = mData.getString("id");
						BSApplication.getInstance().mCurrentLoginRole = ROLE_USER_CARD;
						
						storeUserData(id);
						intentToSystem();
					} else {
						if (!TextUtils.isEmpty(message))
							showToastShort(message);
						else {
							showToastShort("登陆失败");
						}
					}

				} catch (JSONException e) {
					showToastShort("后台数据异常");
				}
			} else {
				showToastShort("服务异常");
			}

		}

		/**
		 * 保存了当前的登录的用户数据
		 * 保存用户卡相关信息
		 * */
		private void storeUserData(String userId) {
			PreferencesUtils.putString(mContext, "account", mEditAccount
					.getText().toString().trim());
			PreferencesUtils.putString(mContext, "password", mEditPassword
					.getText().toString().trim());
			PreferencesUtils.putString(mContext, "phone", mEditPhone.getText()
					.toString().trim());
			PreferencesUtils.putString(mContext, AppDefine.ZYDefine.EXTRA_DATA_USERID, userId);
		}

		@Override
		public void onHttpError(Exception e, String reason, int code) {
			dismissProgressDialog();
			showToastShort("服务异常，请检查网络后重试" +e.getMessage());
		}

		@OnClick(R.id.bt_login)
		public void actionLogin(View mView) {

			if (!NetStateUtil.isNetWorkAlive(mContext)) {
				showToastShort("网络异常请检查网络!");
				return;
			}

			String mAccount = mEditAccount.getText().toString().trim();
			String mPassword = mEditPassword.getText().toString().trim();
			String mEditTextPhone = mEditPhone.getText().toString().trim();

			if (TextUtils.isEmpty(mAccount)) {
				playYoYo(mEditAccount);
				return;
			}

			if (TextUtils.isEmpty(mPassword)) {
				playYoYo(mEditPassword);
				return;
			}
			if (TextUtils.isEmpty(mEditTextPhone)) {
				playYoYo(mEditPhone);
				return;
			}

			List<NameValuePair> mList = new ArrayList<NameValuePair>();
			mList.add(new BasicNameValuePair("txtLoginName", mAccount));
			mList.add(new BasicNameValuePair("txtLoginPwd", mPassword));
			mList.add(new BasicNameValuePair("txtMobilePhone", mEditTextPhone));
			mList.add(new BasicNameValuePair("hidVerifyCode", BSApplication
					.getInstance().getImei()));
			showProgresssDialogWithHint("登录中...  ");
			BSApplication.getInstance().sendData(mList, URLDefine.URL_AUTH,
					this, 101,HttpMethod.POST);

		}
		public void initCardUserPanel() {
			mViewCardUser.setVisibility(View.VISIBLE);
			String account = PreferencesUtils.getString(mContext, "account");
			String password = PreferencesUtils.getString(mContext, "password");
			String phone = PreferencesUtils.getString(mContext, "phone");
			if (!TextUtils.isEmpty(account)) {
				mEditAccount.setText(account);
			}
			if (!TextUtils.isEmpty(password)) {
				mEditPassword.setText(password);
			}
			if (!TextUtils.isEmpty(phone)) {
				mEditPhone.setText(phone);
			}

			 actionLogin(null);
		}

	}

	private void intentToSystem() {
		Intent mIntent = new Intent(this, MainActivity.class);
		startActivity(mIntent);
		finish();
	}

	@OnClick(R.id.iv_login_exit)
	public void existSystem(View mView) {
		BSApplication.getInstance().destroySystem();
	}

	@ViewInject(R.id.iv_popu_remote_login)
	ImageView mViewHintRemote;

	@OnClick(R.id.iv_popu_remote_login)
	public void actionShowRemote(View mView) {
		showPopuWindow();
	}

	// 右上角的一个弹出框
	PopupWindow mPopuWindow;

	private void showPopuWindow() {
		if (mPopuWindow != null && mPopuWindow.isShowing()) {
			mPopuWindow.dismiss();
			return;
		}
		View mPopView = View.inflate(this, R.layout.popu_login_remote, null);
		mPopuWindow = new PopupWindow(mPopView,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		mPopuWindow.setTouchable(true);
		mPopuWindow.setOutsideTouchable(true);
		mPopuWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopuWindow.showAsDropDown(mViewHintRemote, 0, 0);

		//
		View mFirst = mPopView.findViewById(R.id.login_popu_rl_aboutbs);
		mFirst.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mPopuWindow.dismiss();
				BosiUtils.intentToVideoPlay(AppDefine.URLDefine.URL_LOGIN_REMOTE_ABOUTBOSI, mContext);
			}
		});
		View mSecond = mPopView.findViewById(R.id.login_popu_rl_register);
		mSecond.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mPopuWindow.dismiss();
			}
		});
	}
}
