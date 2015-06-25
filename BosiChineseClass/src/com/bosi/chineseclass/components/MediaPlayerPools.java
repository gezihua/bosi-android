package com.bosi.chineseclass.components;

import java.io.IOException;

import android.media.MediaPlayer;

public class MediaPlayerPools {

	MediaPlayer mMediaPlayer;
	
	public MediaPlayerPools(){
		initMediaPlayer();
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
