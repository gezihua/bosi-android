package com.bosi.chineseclass.activitys;

import org.json.JSONObject;



import android.os.Bundle;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.OnHttpActionListener;

public class AuthActivity extends BaseActivity implements OnHttpActionListener{


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}

	@Override
	public void onHttpSuccess(JSONObject mResult) {
		dismissProgress(); //登录成功
		
	}

	@Override
	public void onHttpError(Exception e, String reason) {
		dismissProgress();
		
	}


}
