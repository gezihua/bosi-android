package com.bosi.chineseclass.activitys;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.OnHttpActionListener;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.AppDefine.URLDefine;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity implements  OnHttpActionListener{
	
	HeadLayoutComponents mHeadLayout;
	
	@ViewInject(R.id.headactionbar)
	View mViewHead;
	
	@ViewInject(R.id.et_account)
	EditText mEtPhone;
	@ViewInject(R.id.et_password)
	EditText mEtPassword;
	@ViewInject(R.id.et_sms)
	EditText mEmsCode;
	
	@ViewInject(R.id.tv_sendems)
	public TextView mTvSendEms;
	
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		
		mHeadLayout = new HeadLayoutComponents(mContext, mViewHead);
		mHeadLayout.setTextMiddle("新用户注册", -1);
		mHeadLayout.setTextRight("注册", -1);
		mHeadLayout.setRightOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actionSendRegister();
			}
		});
	}
	
	
	private final int CODE_SENDEMS = 101;
	private final int BUNDPHONE_CODE  = 102;
	
	private final int REGISTERE_CODE = 103;
	private void sendEmsAcitonHttp(String phone){
		List<NameValuePair> mList = new ArrayList<NameValuePair>();
		mList.add(new BasicNameValuePair("mobilephone", phone));
		mList.add(new BasicNameValuePair("timeout", EFFECT_TIMEOUT+""));
		mList.add(new BasicNameValuePair("length", "6"));
		showProgresssDialogWithHint("正在请求验证码...  ");
		BSApplication.getInstance().sendData(mList, URLDefine.URL_SENDEMS,
				this, CODE_SENDEMS,HttpMethod.POST);
	}
	
	
	private void registerUserAccount(){														
		
		String mPhoneNum = mEtPhone.getText().toString(); 
		String mPassword = mEtPassword.getText().toString();
		List<NameValuePair> mList = new ArrayList<NameValuePair>();
		mList.add(new BasicNameValuePair("account", "bscc-"+mPhoneNum));
		mList.add(new BasicNameValuePair("password", mPassword));
		mList.add(new BasicNameValuePair("product", "6"));
		mList.add(new BasicNameValuePair("ei", BSApplication.getInstance().getImei()));
		showProgresssDialogWithHint("正在注册...  ");
		BSApplication.getInstance().sendData(mList, URLDefine.URL_USEREGISTER,
				this, REGISTERE_CODE,HttpMethod.POST);
	}
	
	
	//绑定手机号
	private void bundPhoneNum(){
		String mPhoneNum = mEtPhone.getText().toString(); 
		String mPassword = mEmsCode.getText().toString();
		List<NameValuePair> mList = new ArrayList<NameValuePair>();
		mList.add(new BasicNameValuePair("mobilephone", mPhoneNum));
		mList.add(new BasicNameValuePair("securityCode", mPassword));
		mList.add(new BasicNameValuePair("uid", mId));
		mList.add(new BasicNameValuePair("token", mToken));
		BSApplication.getInstance().sendData(mList, URLDefine.URL_BUNDPHONE,
				this, BUNDPHONE_CODE,HttpMethod.POST);
	}
	
	final int EFFECT_TIMEOUT = 3;
	
	final int EFFECT_SENDEMSBUT_TIMEOUT = 3*60;
	int tempCurrectWaitTimeToUserSendEmsBt =0 ;

	@OnClick(R.id.tv_sendems)
	public void actionSendEms(View mView){
		String mPhoneNum = mEtPhone.getText().toString().trim();
		if(TextUtils.isEmpty(mPhoneNum)){
			playYoYo(mEtPhone);
			return ;
		}
		sendEmsAcitonHttp(mPhoneNum);
		tempCurrectWaitTimeToUserSendEmsBt =EFFECT_SENDEMSBUT_TIMEOUT ;
		mTvSendEms.setEnabled(false);
		mTvSendEms.post(mRunable);
		
	}
	
	Runnable mRunable = new MyTimeOutRunable();
	class MyTimeOutRunable implements Runnable{

		@Override
		public void run() {
			if(tempCurrectWaitTimeToUserSendEmsBt ==0){
				mTvSendEms.setEnabled(true);
				mTvSendEms.setText("发送验证码");
				return;
			}
			
			tempCurrectWaitTimeToUserSendEmsBt --;
			mTvSendEms.setText(tempCurrectWaitTimeToUserSendEmsBt+"s");
			mTvSendEms.postDelayed(mRunable, 1000);
			
		}
		
	}
	

	public void actionSendRegister(){
		String mPhoneNum = mEtPhone.getText().toString();
		if(TextUtils.isEmpty(mPhoneNum)){
			playYoYo(mEtPhone);
			return ;
		}
		
		String mPassword = mEtPassword.getText().toString();
		
		if(TextUtils.isEmpty(mPassword)){
			playYoYo(mEtPassword);
			return ;
		}
		String mEms = mEmsCode.getText().toString();
		
		if(TextUtils.isEmpty(mEms)){
			playYoYo(mEmsCode);
			return ;
		}
		
		//首先验证短信验证码
		//checkems(mPhoneNum,mEms);
		
		registerUserAccount();
	}
	
	
	public  static final String RESULT_PHONE = "mPhone";
	
	public static final String RESULT_PASSWORD ="password";

	private void actionResult(){
		String mPhoneNum = mEtPhone.getText().toString(); 
		String mPassword = mEtPassword.getText().toString();
		
		Intent mIntent = new Intent(this,AuthActivity.class);

		mIntent.putExtra(RESULT_PHONE, mPhoneNum);
		mIntent.putExtra(RESULT_PASSWORD, mPassword);
		setResult(Activity.RESULT_OK);
		finish();
	}
	
	String mId ="";
	String mToken = "";
	@Override
	public void onHttpSuccess(JSONObject mResult, int code) {
		// {"code":"1","message":"","data":"{}"} 0 登陆失败 1成功 2 用户名不存在 3 密码不正确 4 超出使用权限
		dismissProgressDialog(); // 登录成功
		String message =null;
		if (mResult.has("code")) {
			try {
				String codeResult = mResult.getString("code");
				if(mResult.has("msg")){
					 message = mResult.getString("msg");
				}

				if (codeResult.equals(AppDefine.ZYDefine.CODE_SUCCESS)) {
					if(code == CODE_SENDEMS){
						showToastShort("验证码以发送，请注意查收");
					}else if(code ==  BUNDPHONE_CODE){
						actionResult();
						
					}else if(code ==REGISTERE_CODE){
						//注册成功 执行登陆
						JSONObject mObj = mResult.getJSONObject("data");
						mId = mObj.getString("id");
						mToken = mObj.getString("token");
						bundPhoneNum();
					}
				} else {
					if (!TextUtils.isEmpty(message))
						showToastShort(message);
					else {
						showToastShort("服务异常");
					}
				}

			} catch (JSONException e) {
				showToastShort("服务异常");
			}
		} else {
			showToastShort("服务异常");
		}

	}



	@Override
	public void onHttpError(Exception e, String reason, int code) {
		if(!TextUtils.isEmpty(reason)){
			showToastShort(reason);
		}
		dismissProgressDialog();
			
		
	}

}
