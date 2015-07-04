package com.bosi.chineseclass;



//创建一个异步任务管理器 用于对于页面上的异步任务做相关的处理
public class AsyTaskManager {
	
	Thread mThread;
	public void startTask(Runnable mRunable){
		
		if(mThread == null ||!mThread.isAlive()){
			mThread =  new Thread(mRunable);
			mThread.start();
		}
	}

}
