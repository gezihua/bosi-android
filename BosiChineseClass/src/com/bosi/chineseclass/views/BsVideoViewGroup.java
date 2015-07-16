package com.bosi.chineseclass.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bosi.chineseclass.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


//VideoGroup
public class BsVideoViewGroup extends LinearLayout{

	@ViewInject(R.id.vv_bsvideo)
	public VideoView mVideoView;
	@ViewInject(R.id.bt_bsvideo_replay)
	public Button mBtReplay;
	
	@OnClick(R.id.bt_bsvideo_replay)
	public void actionReplay(View mView){
		mVideoView.setVisibility(View.VISIBLE);
		mVideoView.start();
		if(mOnVideoRestartListener!=null){
			mOnVideoRestartListener.OnVideoRestarted();
		}
	}
	
	public void resetVideoView(){
		mVideoView.setVisibility(View.INVISIBLE);
	}
	OnVideoRestartListener mOnVideoRestartListener;
	
	public void setOnVideoRestartListener(OnVideoRestartListener mOnVideoRestartListener){
		this.mOnVideoRestartListener= mOnVideoRestartListener;
	}
	public void playVideo(String filePath){
		if(TextUtils.isEmpty(filePath)) return;
		mVideoView.setVideoPath(filePath);
		actionReplay(null);
	}
	public BsVideoViewGroup(Context context) {
		super(context);
	
	}
	public BsVideoViewGroup(Context context,AttributeSet mAttributSet) { 
		super(context,mAttributSet);
		addVideoView();
		resetVideoView();
	}
	private void addVideoView(){
		View mView = View.inflate(getContext(), R.layout.layout_videogroup, null);
		ViewUtils.inject(this, mView);
		addView(mView);
	}
	
	@SuppressLint("NewApi")
	public void setVideoBackGround(int resource){
		mVideoView.setBackground(getContext().getResources().getDrawable(resource));
	}

	public interface OnVideoRestartListener{
		public void OnVideoRestarted();
	}
}
