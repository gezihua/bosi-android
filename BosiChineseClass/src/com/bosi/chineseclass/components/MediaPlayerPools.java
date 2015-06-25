package com.bosi.chineseclass.components;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;

public class MediaPlayerPools {

	MediaPlayer mMediaPlayer;
	
	Context mContext;
	public MediaPlayerPools(Context mContext ){
		initMediaPlayer();
		this.mContext = mContext;
	}

	public void initMediaPlayer() {
		destroyMediaPlayer();
		mMediaPlayer = new MediaPlayer();
	}

	public void destroyMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
	
	
	//可以播放网络音乐不过可能会卡顿
	public void playMediaFile(String mediaFile) {
		try {
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(mediaFile);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		} catch (IOException e) {
		} catch (Exception e) {
		}
	}

}
