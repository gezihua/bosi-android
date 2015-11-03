package com.bosi.chineseclass.task;

import com.lidroid.xutils.http.HttpHandler;

public interface IBasicTask {

	public void cancleTask();
	public HttpHandler<?> sendDataAsy();
}
