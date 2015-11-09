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
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.utils.BosiUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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

	@ViewInject(R.id.pb_forzjktdital)
	private ProgressBar mPbForZjktDital ;
	
	@ViewInject(R.id.bt_video_fullscreen_togg)
	Button mBtFullScreen;
	
	public static final String KEY_FATHERID = "key_fatherid";
	@ViewInject(R.id.ll_menu_left)
	View mViewLeftMenu;

	String mFatherId;

	int mWholeSize = 1;
	int mCurremtSize = 1;
	
	String mCurrentVideoPath ;
	
	@Override
	public void onPause() {
		super.onPause();
		if(mVideoView.isPlaying())
		mVideoView.pause();
	}

	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_layout_zjktd, null);
	}

	@OnClick(R.id.bt_video_fullscreen_togg)
	public void actionToggFullScreen(View mView){
//		if(mViewLeftMenu.getVisibility()==View.VISIBLE){
//			mViewLeftMenu.setVisibility(View.GONE);
//			mBtFullScreen.setBackgroundResource(R.drawable.icon_canclefullscreen);
//		}else{
//			mViewLeftMenu.setVisibility(View.VISIBLE);
//			mBtFullScreen.setBackgroundResource(R.drawable.icon_fullscreen);
//		}
		BosiUtils.intentToVideoPlay(mCurrentVideoPath, mActivity);
	}
	@Override
	protected void afterViewInject() {
		mBtFullScreen.setVisibility(View.VISIBLE);
		mFatherId = mActivity.getIntent().getStringExtra(KEY_FATHERID);

		if (TextUtils.isEmpty(mFatherId) || !mFatherId.contains("-")) {
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
					mWebView.loadUrl("javascript:selectNode(" + mCurremtSize
							+ ")");
					playVideo(getBasicUrl() + mCurremtSize + ".mp4");
				}
			}
		});
		initWebView();
		// 加载目录 TODO
		 mWebView.loadUrl(AppDefine.URLDefine.URL_ZJKT_BASEURL + "/"
		 + mFatherId.split("-")[0] + "/secondpage/index.html");
		//mWebView.loadUrl("file:///android_asset/zjkt/index.html");

		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
					mPbForZjktDital.setVisibility(View.GONE);
					mWebView.loadUrl("javascript:getAllNodeSize()");
					mWebView.loadUrl("javascript:selectNode(" + mCurremtSize
							+ ")");
					return;
				}
				mPbForZjktDital.setVisibility(View.VISIBLE);
				mPbForZjktDital.setProgress(newProgress);
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
		mBuilder.append(mSpitArray[0] + "/shipin/");
		mBuilder.append(mSpitArray[1] + "/");
		return mBuilder.toString();
	}

	private void playVideo(String path) {
		if(TextUtils.isEmpty(path)){
			return;
		}
		this.mCurrentVideoPath = path;
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
