
package com.bosi.chineseclass.su.ui.fragment;

import android.net.Uri;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.lidroid.xutils.view.annotation.ViewInject;

public class VideoFragment extends BaseFragment {
    private String mVideoUrl;
    @ViewInject(R.id.dictionary_video)
    private VideoView mVideoView;

    public VideoFragment(String mVideoUrl) {
        this.mVideoUrl = mVideoUrl;
    }

    @Override
    protected View getBasedView() {
        View view = View.inflate(getActivity(), R.layout.video_fragment, null);
        return view;
    }

    @Override
    protected void afterViewInject() {
        mVideoView.setMediaController(new MediaController(mActivity));
        String path = "http://www.yuwen100.cn/yuwen100/zy/hanzi-flash/120001.mp4";
        playVideo(path);
    }

    private void playVideo(String path) {
        mVideoView.setVideoURI(Uri.parse(path));
        mVideoView.requestFocus();
        mVideoView.start();
    }

}
