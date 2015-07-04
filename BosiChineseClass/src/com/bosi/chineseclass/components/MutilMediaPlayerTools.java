
package com.bosi.chineseclass.components;

import android.content.Context;
import android.media.MediaPlayer;

public class MutilMediaPlayerTools implements MediaPlayer.OnCompletionListener {
    private int mCurrentPlayIndex = 0;
    // 播放的路径
    private String[] mPaths;
    private MediaPlayerPools mPlayerPools = null;

    public MutilMediaPlayerTools(Context context, String[] paths) {
        mPlayerPools = new MediaPlayerPools(context);
        mPaths = paths;
        mPlayerPools.setCompleteListener(this);
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
