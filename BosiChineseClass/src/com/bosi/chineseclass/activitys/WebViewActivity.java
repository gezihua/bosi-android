package com.bosi.chineseclass.activitys;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
@ContentView(R.layout.laout_webview)
public class WebViewActivity extends BaseActivity{
	    @ViewInject(R.id.wv_webact)
	    private WebView webview;
	    @ViewInject(R.id.progbar_webview)
	    private ProgressBar mProgressBar;

	    @SuppressLint("SetJavaScriptEnabled")
	    @Override
	    protected void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        WebSettings webSettings = webview.getSettings();
	        webSettings.setJavaScriptEnabled(true);
	        // 设置可以访问文件
	        webSettings.setAllowFileAccess(true);
	        // 设置支持缩放
	        webSettings.setBuiltInZoomControls(true);
	        // 加载需要显示的网页
	        // 这个是不行滴  加载不了滴
	        String url = "http://manage1.bsccedu.com/temp/1.html";
	        // 设置Web视图
	        webview.setWebViewClient(new WebViewClient());
	        //webview.setWebChromeClient(new MyWebChromeClient());
	        webview.loadUrl(url);
	    }

}
