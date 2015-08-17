package com.bosi.chineseclass.views;

import java.io.File;

import u.aly.cp;
import android.annotation.SuppressLint;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
		playVideo();
		
	}
	
	public void resetVideoView(){
		mVideoView.setVisibility(View.INVISIBLE);
	}
	OnVideoRestartListener mOnVideoRestartListener;
	
	public void setOnVideoRestartListener(OnVideoRestartListener mOnVideoRestartListener){
		this.mOnVideoRestartListener= mOnVideoRestartListener;
	}
	
	private void playVideo(){
		if(TextUtils.isEmpty(mVideoPath))return;
		mVideoView.setVisibility(View.VISIBLE);
		if(mOnVideoRestartListener!=null){
			mOnVideoRestartListener.OnVideoRestarted();
		}
		mVideoView.setVideoPath(mVideoPath);
	}
	
	
	private String mVideoPath ;
	public void playVideo(String filePath){
		if(TextUtils.isEmpty(filePath)) return;
		File mFile = new File(filePath);
		if(mFile==null ||!mFile.exists()){
			Toast.makeText(getContext(), "此资源没有动画", Toast.LENGTH_SHORT).show();
			return;
		}
		this.mVideoPath = filePath;
		playVideo();
	}
	public BsVideoViewGroup(Context context) {
		super(context);
		
		
	}
	
	
	public void onDestroy(){
		if(mVideoView.isPlaying()){
			mVideoView.stopPlayback();
		}
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
		
		mVideoView.setOnCompletionListener(new OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer arg0) {
				mVideoView.stopPlayback();
			}
 	});
	}
	
	@SuppressLint("NewApi")
	public void setVideoBackGround(int resource){
		mVideoView.setBackgroundResource(resource);
	}

	public interface OnVideoRestartListener{
		public void OnVideoRestarted();
	}
}
