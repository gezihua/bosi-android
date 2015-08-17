package com.bosi.chineseclass;

import org.json.JSONObject;

public interface OnHttpActionListener {
	public void onHttpSuccess(JSONObject mResult);
	public void onHttpError (Exception e ,String reason);
}
