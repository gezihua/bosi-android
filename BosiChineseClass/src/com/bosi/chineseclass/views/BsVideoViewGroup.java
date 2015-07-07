package com.bosi.chineseclass.views;

import android.annotation.SuppressLint;
import android.content.Context;
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
		mVideoView.start();
	}
	
	public void playVideo(String filePath){
		mVideoView.setVideoPath(filePath);
		actionReplay(null);
	}
	public BsVideoViewGroup(Context context) {
		super(context);
	
	}
	public BsVideoViewGroup(Context context,AttributeSet mAttributSet) { 
		super(context,mAttributSet);
		addVideoView();
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

}
