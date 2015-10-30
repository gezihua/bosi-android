package com.bosi.chineseclass.activitys;


import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
@ContentView(R.layout.laout_webview)
public class WebViewActivity extends BaseActivity {


	@ViewInject(R.id.wv_webact)
	private WebView mWebView;

	@ViewInject(R.id.webactivity_pb)
	ProgressBar mPbLoading;


	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
        String mTitleString = getIntent().getStringExtra(KEY_URL_TITLE_STRING);
	    mHeadLayout = new HeadLayoutComponents(this, mViewHead);
		mHeadLayout.setTextMiddle(mTitleString, -1);
	    initWebView();

	}

	private class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			if (newProgress == 100) {
				mPbLoading.setVisibility(View.GONE);
				return;
			}
			mPbLoading.setVisibility(View.VISIBLE);
			mPbLoading.setProgress(newProgress);
		}
	}

	private void initWebView() {
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAppCacheEnabled(true);
		
		mWebView.setWebChromeClient(new MyWebChromeClient());
		
		// TODO:webview路径需要写正确
		// PATH + mId;
		String mUrlString = getIntent().getStringExtra(KEY_URL);
		// mWebView.loadUrl(path);
		mWebView.loadUrl(mUrlString);

	}

	@ViewInject(R.id.headactionbar)
	View mViewHead;

	HeadLayoutComponents mHeadLayout;

	// @OnClick(R.id.bt_close)
	// private void CloseActivity(View view){
	// this.finish();
	// }
	    
	    public final static String KEY_URL ="KEY_URL";
	    public final static String KEY_URL_TITLE_STRING ="KEY_TITLE";
	    

}
