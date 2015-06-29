package com.bosi.chineseclass.han.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.activity_zy_object)
public class ZyObjectActivity extends BaseActivity{

    private String mId;

    @ViewInject(R.id.webview_zy_object)
    private WebView mWebView;

    //TODO:webview路径需要写正确
    private final String PATH = "http://www.yuwen100.cn/yuwen100/hzzy/jbzy-clips/objectswf/";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mId = getIntent().getStringExtra(AppDefine.ZYDefine.ZY_OBJECT_ID);
        Log.e("HNX", "mId : " + mId);
        
        initWebView();
    }
    private void initWebView() {
        
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //TODO:webview路径需要写正确
//        PATH + mId;
//        String path = PATH + mId + "./html";
        mWebView.loadUrl("http://manage1.bsccedu.com/temp/1.html");
        
    }

    @OnClick(R.id.bt_close)
    private void CloseActivity(View view){
        this.finish();
    }

}
