
package com.bosi.chineseclass.components;

import java.io.File;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;

public class MutilMediaPlayerTools implements MediaPlayer.OnCompletionListener {
    private int mCurrentPlayIndex = 0;
    // 播放的路径
    private String[] mPaths;
    private MediaPlayerPools mPlayerPools = null;

    public MutilMediaPlayerTools(Context context) {
        mPlayerPools = new MediaPlayerPools(context);
        mPlayerPools.setCompleteListener(this);
    }
    
    public void setCurrentFilePath(String[] paths){
    	this.mPaths = paths;
    }

    public void play() {
        if (mPaths == null) {
            return;
        }
        if (mPaths.length == 0) {
            return;
        }
        if (mPaths.length == 0) {
            mPlayerPools.playMediaFile(mPaths[0]);
            return;
        }
        
        if (mCurrentPlayIndex < mPaths.length) {
        	
        	if(!TextUtils.isEmpty(mPaths[mCurrentPlayIndex])){
        		File mFile = new File(mPaths[mCurrentPlayIndex]);
        		if(mFile==null||!mFile.exists()){
        			onCompletion(mPlayerPools.getMediaPlayer());
            		return;
        		}
        	}
            mPlayerPools.playMediaFile(mPaths[mCurrentPlayIndex]);
        }
        if (mMutilMediaPlayerListener != null && mCurrentPlayIndex == mPaths.length) {
            mMutilMediaPlayerListener.finished();
        }
    }

    private MutilMediaPlayerListener mMutilMediaPlayerListener;

    public void setMutilMediaPlayerListener(MutilMediaPlayerListener mMutilMediaPlayerListener) {
        this.mMutilMediaPlayerListener = mMutilMediaPlayerListener;
    }

    public static interface MutilMediaPlayerListener {
        public void finished();
    }

    public void reset() {
        mCurrentPlayIndex = 0;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mCurrentPlayIndex++;
        play();
    }

    public void onDestory() {
        if (mPlayerPools != null) {
            mPlayerPools.destroyMediaPlayer();
        }
    }

}
