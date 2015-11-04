package com.bosi.chineseclass.fragments;


import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ExpertClassDitalFragment extends BaseFragment{
	@ViewInject(R.id.video_zjjzy)
	private VideoView mVideoView;

	@ViewInject(R.id.webview_zjjzy_index)
	private WebView mWebView;

	@ViewInject(R.id.pb_videoplaying)
	private ProgressBar mPbBarForVideo;

	
	HeadLayoutComponents mHeadActionBarComp;
	@ViewInject(R.id.headactionbar)
	View mHeadActionBar;
	
	
	public static final String KEY_FATHERID = "key_fatherid";
	String mFatherId ;
	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_layout_zjjzy, null);
	}

	@Override
	protected void afterViewInject() {
		mVideoView.setMediaController(new MediaController(mActivity));

		initWebView();
		//加载目录
		mWebView.loadUrl("file:///android_asset/zjkt/videoindex.html");

		
		mFatherId = mActivity.getIntent().getStringExtra(KEY_FATHERID);
		mHeadActionBarComp = new HeadLayoutComponents(mActivity, mHeadActionBar);
		mHeadActionBarComp.setTextMiddle("专家课堂", -1);
		mHeadActionBarComp.setDefaultLeftCallBack(true);
		mHeadActionBarComp.setDefaultRightCallBack(true);
		
	}

	private void playVideo(String path) {
		mPbBarForVideo.setVisibility(View.VISIBLE);
		mVideoView.setScrollContainer(false);
		mVideoView.setVideoURI(Uri.parse(path));
		mVideoView.requestFocus();
		mVideoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer arg0) {
				mPbBarForVideo.setVisibility(View.GONE);
			}
		});
		mVideoView.start();
	}

	private void initWebView() {

		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new WebAppShowObjectInterface(),
				"zjktdital");

	}

	public class WebAppShowObjectInterface {
		@JavascriptInterface
		public void showObject(final String id) {
			Log.e("HNX", "Zjjzy showObject id  " + id);
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String path = AppDefine.URLDefine.URL_HZJC_ZJJZY_VIDEO + id
							+ ".mp4";
					playVideo(path);
				}
			});
		}
	}

	@Override
	public void onDestroy() {
		mWebView.clearCache(true);
		mWebView.clearHistory();
		mVideoView.stopPlayback();
		super.onDestroy();
	}


	
}
