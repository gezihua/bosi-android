package com.bosi.chineseclass.han.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ZjjzyFragment extends BaseFragment {
    @ViewInject(R.id.video_zjjzy)
    private VideoView mVideoView;

    @ViewInject(R.id.webview_zjjzy_index)
    private WebView mWebView;

    private final String PATH = "http://www.yuwen100.cn/yuwen100/hzzy/jbzy-clips/video/";

    @Override
    protected View getBasedView() {
        return inflater.inflate(R.layout.fragment_layout_zjjzy, null);
    }

    @Override
    protected void afterViewInject() {
        mVideoView.setMediaController(new MediaController(mActivity));
        //TODO:设置正确的专家讲字源路径
        String path = "http://www.yuwen100.cn/yuwen100/zy/hanzi-flash/120001.mp4";
        playVideo(path);

        initWebView();
        mWebView.loadUrl("file:///android_asset/zjjzy/videoindex.html");

    }

    private void playVideo(String path) {
        mVideoView.setVideoURI(Uri.parse(path));
        mVideoView.requestFocus();
        mVideoView.start();
    }

    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    private void initWebView() {

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new WebAppShowObjectInterface(), "zjjzy");

        }

    public class WebAppShowObjectInterface {
        @JavascriptInterface
        public void showObject(final String id) {
            Log.e("HNX", "Zjjzy showObject id  " + id);
            
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  //TODO:设置正确的专家讲字源路径
//                    String path = PATH + id + ".mp4";
                    String path = "http://www.yuwen100.cn/yuwen100/zy/hanzi-flash/120001.mp4";
                    playVideo(path);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        mWebView.clearCache(true);
        mWebView.clearHistory();

        super.onDestroy();
    }

}
