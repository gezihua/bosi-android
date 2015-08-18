package com.bosi.chineseclass.activitys;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;




import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.OnHttpActionListener;
import com.bosi.chineseclass.R;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.layout_authpage)
public class AuthActivity extends BaseActivity implements OnHttpActionListener{

	@ViewInject(R.id.et_account)
	private EditText mEditAccount;
	@ViewInject(R.id.et_password)
	private EditText mEditPassword;
	@ViewInject(R.id.et_phonenum)
	private EditText mEditPhone;

	
	@OnClick(R.id.bt_login)
	public void actionLogin(View mView){
		
		String mAccount = mEditAccount.getText().toString();
		String mPassword = mEditPassword.getText().toString();
		String mEditTextPhone = mEditPhone.getText().toString();
		
		if(TextUtils.isEmpty(mAccount)){
			playYoYo(mEditAccount);
			return;
		}
		
		if(TextUtils.isEmpty(mPassword)){
			playYoYo(mEditPassword);
			return;
		}
		if(TextUtils.isEmpty(mEditTextPhone)){
			playYoYo(mEditPhone);
			return;
		}
		List<NameValuePair> mList = new ArrayList<NameValuePair>();
		mList.add(new BasicNameValuePair("txtLoginName", mAccount));
		mList.add(new BasicNameValuePair("txtLoginPwd", mPassword));
		mList.add(new BasicNameValuePair("txtMobilePhone", mEditTextPhone));
		
		showProgresssDialog();
		
		BSApplication.getInstance().sendData(mList, "http://verify.yuwen100.cn", this, 101);
		
	}
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}

	@Override
	public void onHttpSuccess(JSONObject mResult,int code) {
		
		//{"code":"1","message":""} 0 登陆失败 1成功 2 用户名不存在 3 密码不正确  4 超出使用权限
		dismissProgress(); //登录成功
		
	}

	@Override
	public void onHttpError(Exception e, String reason,int code) {
		dismissProgress();
		
	}


}
