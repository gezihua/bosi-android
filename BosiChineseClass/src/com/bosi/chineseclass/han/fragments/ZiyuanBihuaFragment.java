package com.bosi.chineseclass.han.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.activitys.ZyObjectActivity;
import com.bosi.chineseclass.views.AutoChangeLineViewGroup;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ZiyuanBihuaFragment extends BaseFragment {
    private int mFocusedIndex = 0;
    private String mId_bihua = "2";
    private static final String PATH = "file:///android_asset/bsjs/";

    private String [] mBihuaArray=null;
    @ViewInject(R.id.ll_bihua)
    private LinearLayout mLL_bihua;

    private AutoChangeLineViewGroup mAutoViewGroup;
    
    @ViewInject(R.id.webview_bihua)
    private WebView mWebView;

    @Override
    protected View getBasedView() {
        return inflater.inflate(R.layout.fragment_layout_ziyuan_bihua, null);
    }

    @Override
    protected void afterViewInject() {
        initWebView();

        intiComponentView();
        initBihuaArray();
        addBihua();

    }

    private void intiComponentView() {
        mAutoViewGroup = new AutoChangeLineViewGroup(mActivity);
        mAutoViewGroup.setHorizontalSpacing(2);
        mLL_bihua.addView(mAutoViewGroup);
    }

    private void addBihua() {
        for(int i= 0; i<mBihuaArray.length;i++){
            final int index = i;
            final TextView mTextView = new TextView(mActivity);
            
            mTextView.setTextSize(17);
            mTextView.setText(mBihuaArray[i]);
            if (mBihuaArray[i].length() < 2) {
                mTextView.setPadding(14, 0, 14, 0);
            } else {
                mTextView.setPadding(2, 0, 2, 0);
            }

            mTextView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    mFocusedIndex = index;
                    refreshBihuaColor();

                    String textContext = mTextView.getText().toString();
                    mId_bihua = textContext.length() < 2 ? textContext.substring(0, 1) : textContext.substring(0, 2);
                    String path = PATH + mId_bihua + ".html";
                    mWebView.loadUrl(path);
                }
            });

            mAutoViewGroup.addView(mTextView);
        }

        refreshBihuaColor();
    }
    
    private void refreshBihuaColor(){
        for (int i = 0; i < mBihuaArray.length; i++ ) {
            initButtonColor(i);
        }
    }

    private void initButtonColor(int btIndex) {
        //TODO:颜色值
        TextView tv = (TextView) mAutoViewGroup.getChildAt(btIndex);
        if (btIndex == mFocusedIndex) {
            tv.setBackgroundResource(R.drawable.bihua_bg_pressed);
        } else {
            tv.setBackgroundResource(R.drawable.bihua_bg_normal);
        }
    }

    private void initBihuaArray() {
        mBihuaArray = getString(R.string.jbzy_bihua_array).split("#");
        
    }

    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    private void initWebView() {

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        mWebView.setBackgroundColor(getResources().getColor(R.color.bihua_webview_color));

        mWebView.addJavascriptInterface(new WebAppShowObjectInterface(), "zyobject");

        String path = PATH + mId_bihua + ".html";
        mWebView.loadUrl(path);
    }

    @Override
    public void onDestroy() {
        mWebView.clearCache(true);
        mWebView.clearHistory();

        super.onDestroy();
    }

    public class WebAppShowObjectInterface {
        @JavascriptInterface
        public void showObject(String id) {
            Intent intent = new Intent(mActivity, ZyObjectActivity.class);
            intent.putExtra(AppDefine.ZYDefine.ZY_OBJECT_ID, id);
            mActivity.startActivity(intent);
        }
    }
}
