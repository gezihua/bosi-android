package com.bosi.chineseclass;

import com.bosi.chineseclass.task.IBasicTask;
import com.bosi.chineseclass.task.UpLoadBpcyTask;
import com.bosi.chineseclass.task.UpLoadBphzTask;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public class BosiChineseService extends Service {
	MyUploadThread mUploadThread;
	private final static String TAG = "BosiChineseService";
	public static final String TASKNAME = "TASKNAME";
	public static final int TASK_UPLOADBPCY = 0;
	public static final int TASK_UPLOADBPHZ = 1;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mUploadThread = new MyUploadThread(TAG);
		mUploadThread.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 根据传递过来的信息 执行相应的任务
		if (intent != null) {
			int mTaskName = intent.getIntExtra(TASKNAME, TASK_UPLOADBPCY);
			Message msg = Message.obtain();
			msg.arg1 = mTaskName;
			mUploadThread.mHandler.sendMessage(msg);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mUploadThread.quit();
		if (mTask != null)
			mTask.cancleTask();
	}

	IBasicTask mTask;

	class MyUploadThread extends HandlerThread {

		public Handler mHandler;

		public synchronized void start() {
			super.start();
			mHandler = new Handler(getLooper()) {

				@Override
				public void handleMessage(Message msg) {
					if (msg.arg1 == TASK_UPLOADBPCY) {
						mTask = new UpLoadBpcyTask(BosiChineseService.this);
						mTask.sendDataAsy();
					} else if (msg.arg1 == TASK_UPLOADBPHZ) {
						mTask = new UpLoadBphzTask(BosiChineseService.this);
						mTask.sendDataAsy();
					}
					super.handleMessage(msg);
				}
			};
		};

		public MyUploadThread(String name) {
			super(name);
		}

		@Override
		public Looper getLooper() {
			return super.getLooper();
		}

	}
}
