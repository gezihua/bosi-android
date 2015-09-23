package com.bosi.chineseclass.activitys;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.bosi.chineseclass.AppDefine.URLDefine;
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.OnHttpActionListener;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.util.PreferencesUtils;
import com.bosi.chineseclass.utils.NetStateUtil;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.layout_authpage)
public class AuthActivity extends BaseActivity implements OnHttpActionListener {

	@ViewInject(R.id.et_account)
	private EditText mEditAccount;
	@ViewInject(R.id.et_password)
	private EditText mEditPassword;
	@ViewInject(R.id.et_phonenum)
	private EditText mEditPhone;

	@OnClick(R.id.bt_login)
	public void actionLogin(View mView) {

		if (!NetStateUtil.isNetWorkAlive(this)) {
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
		mList.add(new BasicNameValuePair("imei", BSApplication.getInstance().getImei()));
		showProgresssDialogWithHint("登录中...  ");
		BSApplication.getInstance().sendData(mList, URLDefine.URL_AUTH, this,
				101);

	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		String account = PreferencesUtils.getString(this, "account");
		String password = PreferencesUtils.getString(this, "password");
		String phone = PreferencesUtils.getString(this, "phone");
		if (!TextUtils.isEmpty(account)) {
			mEditAccount.setText(account);
		}
		if (!TextUtils.isEmpty(password)) {
			mEditPassword.setText(password);
		}
		if (!TextUtils.isEmpty(phone)) {
			mEditPhone.setText(phone);
		}
	}

	interface RESULTCODE {
		public static String CODE_SUCCESS = "1"; // 登陆成功
		public static String CODE_FAILED = "0"; // 登陆失败
		public static String CODE_NOEXISTUSER = "2"; // 用户名不存在
		public static String CODE_INCURETPASWORD = "3"; // 密码不正确
		public static String CODE_OUTOFPROMISSION = "4"; // 超出使用权限
	}

	@Override
	public void onHttpSuccess(JSONObject mResult, int code) {

		// {"code":"1","message":""} 0 登陆失败 1成功 2 用户名不存在 3 密码不正确 4 超出使用权限
		dismissProgressDialog(); // 登录成功
		if (mResult.has("code")) {
			try {
				String codeResult = mResult.getString("code");
				String message = mResult.getString("message");

				if (codeResult.equals(RESULTCODE.CODE_SUCCESS)) {
					showToastShort("登陆成功");
					storeUserData();
					intentToSystem();
				} else {
					if (!TextUtils.isEmpty(message))
						showToastShort(message);
					else {
						showToastShort("登陆失败");
					}
				}

			} catch (JSONException e) {
			}
		} else {
			showToastShort("服务异常");
		}

	}

	private void storeUserData() {
		PreferencesUtils.putString(this, "account", mEditAccount.getText()
				.toString().trim());
		PreferencesUtils.putString(this, "password", mEditPassword.getText()
				.toString().trim());
		PreferencesUtils.putString(this, "phone", mEditPhone.getText()
				.toString().trim());
	}

	@Override
	public void onHttpError(Exception e, String reason, int code) {
		dismissProgressDialog();
		showToastShort("服务异常，请检查网络后重试");
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
}
