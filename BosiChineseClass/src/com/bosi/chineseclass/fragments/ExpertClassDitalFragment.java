package com.bosi.chineseclass.fragments;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
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

public class ExpertClassDitalFragment extends BaseFragment {
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

	String mFatherId;

	int mWholeSize = 1;
	int mCurremtSize = 1;

	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_layout_zjjzy, null);
	}

	@Override
	protected void afterViewInject() {

		mFatherId = mActivity.getIntent().getStringExtra(KEY_FATHERID);

		if (TextUtils.isEmpty(mFatherId)) {
			mActivity.showToastShort("数据异常");
			mActivity.finish();
			return;
		}
		mVideoView.setMediaController(new MediaController(mActivity));
		mVideoView.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				if (mCurremtSize < mWholeSize) {
					mCurremtSize++;
					playVideo(getBasicUrl() + mCurremtSize + ".mp4");
				}
			}
		});
		initWebView();
		// 加载目录
		mWebView.loadUrl("file:///android_asset/zjkt/index.html");
		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
					mWebView.loadUrl("javascript:getAllNodeSize()");
					return;
				}
			}
		});
		playVideo(getBasicUrl() + mCurremtSize + ".mp4");

		mHeadActionBarComp = new HeadLayoutComponents(mActivity, mHeadActionBar);
		mHeadActionBarComp.setTextMiddle("专家课堂", -1);
		mHeadActionBarComp.setDefaultLeftCallBack(true);
		mHeadActionBarComp.setDefaultRightCallBack(true);

	}

	private String getBasicUrl() {
		String mSpitArray[] = mFatherId.split("-");
		if (mSpitArray.length == 0) {
			return "";
		}
		StringBuilder mBuilder = new StringBuilder();
		mBuilder.append(AppDefine.URLDefine.URL_BASEURL);
		mBuilder.append("/zhuanjia/");
		mBuilder.append(mSpitArray[0] + "/");
		mBuilder.append(mSpitArray[1] + "/");
		return mBuilder.toString();
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
		mWebView.addJavascriptInterface(new WebAppShowObjectInterface(), "zjkt");
	}

	public class WebAppShowObjectInterface {
		@JavascriptInterface
		public void showObject(final String id) {
			Log.e("HNX", "Zjjzy showObject id  " + id);
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (TextUtils.isEmpty(id))
						return;
					mCurremtSize = Integer.parseInt(id);
					playVideo(getBasicUrl() + id + ".mp4");
				}
			});
		}

		public void getAllNodeSize(String size) {
			mWholeSize = Integer.parseInt(size);
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
