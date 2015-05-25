package com.zy.booking.fragments;

import java.util.List;



import org.apache.http.NameValuePair;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zy.booking.AppDefine;
import com.zy.booking.R;
import com.zy.booking.activitys.AuthActivity;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.json.IspUpLoadJsonData;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.PreferencesUtils;
import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RegisterFragment extends BaseFragment implements
		OnHttpActionListener {

	@ViewInject(R.id.headactionbar)
	View mHeadActionBar;

	OnActionLoginListener mOnActionLogin;
	HeadLayoutComponents mHeadActionBarComp;

	@ViewInject(R.id.et_regsiger_account)
	EditText mEditAccount;
	@ViewInject(R.id.et_regsiger_password)
	EditText mEditPassword;
	@ViewInject(R.id.et_regsiger_surepassword)
	EditText mEditPasswordSure;

	@ViewInject(R.id.bt_register_actionreg)
	Button mBtRegister;

	public void setOnActionLogin(OnActionLoginListener mListener) {
		this.mOnActionLogin = mListener;
	}

	public void afterViewInject() {
		mHeadActionBarComp = new HeadLayoutComponents(getActivity(),
				mHeadActionBar);

		mHeadActionBarComp.setTextMiddle("注册", -1);
		mHeadActionBarComp.setTextLeft("", R.drawable.icon_back_white);
		mHeadActionBarComp.setLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOnActionLogin.onLogin();
			}
		});

		mBtRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fitleRegister();
			}
		});
	}

	String usename;
	String password;

	private void fitleRegister() {
		usename = mEditAccount.getText().toString().trim();
		password = mEditPassword.getText().toString().trim();
		String mSurePassword = mEditPasswordSure.getText().toString().trim();

		if (TextUtils.isEmpty(usename)) {
			playYoYo(mEditAccount);
			return;
		}
		if (TextUtils.isEmpty(password)) {
			playYoYo(mEditPassword);
			return;
		}
		if (TextUtils.isEmpty(mSurePassword)) {
			playYoYo(mEditPasswordSure);
			return;
		}

		if (!password.equals(mSurePassword)) {
			Toast.makeText(getActivity(), "两次密码输入不一致", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		
		sendRegisterData();
	}


	public interface OnActionLoginListener {
		public void onLogin();
	}

	private void sendRegisterData() {
		showProgresssDialog();
		List<NameValuePair> registerData = IspUpLoadJsonData.getRegisterDataNameValuePair(usename, password,
				true);
		sendData(registerData, AppDefine.BASE_URL_REGISTER, this, 100);
	}

	private void sendLoginData() {
		showProgresssDialog();
		sendData(IspUpLoadJsonData.getLoginDataList(usename, password), AppDefine.BASE_URL_LOGIN, this, 101);
	}

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
           dismissProgressDialog();
	}

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		 dismissProgressDialog();
		if(isSuccess && resultCode ==100){
			Toast.makeText(getActivity(), "注册并登陆成功", Toast.LENGTH_SHORT).show();
			sendLoginData();
		}else if(isSuccess && resultCode ==101){
			if(mJsonResult!=null){
				String token = JsonUtil.getAsString(mJsonResult, "token");
				String userId = JsonUtil.getAsString(mJsonResult, "userId");
				((AuthActivity)getActivity()).onLoginSuccess(usename, token,userId);
			}
		}else{
			Toast.makeText(getActivity(), reason, Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_layout_register, null);
	}
}
