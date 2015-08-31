package com.bosi.chineseclass.han.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_zy_object)
public class ZyObjectActivity extends BaseActivity{

    private String mId;

    @ViewInject(R.id.webview_zy_object)
    private WebView mWebView;
    
    @ViewInject(R.id.pb_forwebview)
    ProgressBar mPbLoading;

    //TODO:webview路径需要写正确
    private final String PATH = "http://www.yuwen100.cn/yuwen100/hzzy/jbzy-clips/objectswf/";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mId = getIntent().getStringExtra(AppDefine.ZYDefine.ZY_OBJECT_ID);
        Log.e("HNX", "mId : " + mId);
        
        mHeadLayout = new HeadLayoutComponents(this,mViewHead);
        mHeadLayout.setTextMiddle("基本字源", -1);
        initWebView();
        
        mWebView.setWebChromeClient(new MyWebChromeClient());
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

        //TODO:webview路径需要写正确
//        PATH + mId;
         //String path = PATH + mId + "./html";
        // mWebView.loadUrl(path); 
      mWebView.loadUrl("file:///android_asset/bosijiaoyu/index.html");
        
    }

    @ViewInject(R.id.headactionbar)
    View mViewHead;
    
    HeadLayoutComponents mHeadLayout;
    
//    @OnClick(R.id.bt_close)
//    private void CloseActivity(View view){
//        this.finish();
//    }

}
