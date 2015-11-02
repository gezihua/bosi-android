package com.bosi.chineseclass.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.activitys.ZyObjectActivity;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.han.fragments.ZiyuanBihuaFragment.WebAppShowObjectInterface;
import com.lidroid.xutils.view.annotation.ViewInject;


//专家课堂
public class ExpertClassFragment extends BaseFragment{

	
	HeadLayoutComponents mHeadActionbar;
	
	@ViewInject(R.id.headactionbar)
	View mHeadActionBar;
	@ViewInject(R.id.expertmain_wv_menu)
	WebView mWvExpertMenu;
	
	@ViewInject(R.id.expertmain_wv_instro)
	WebView mWvExpertinstro;
	
	@Override
	protected View getBasedView() {
		return View.inflate(mActivity, R.layout.layout_expertclass_mainpage, null);
	}

	@Override
	protected void afterViewInject() {
		mHeadActionbar = new HeadLayoutComponents(mActivity, mHeadActionBar);
		mHeadActionbar.setTextMiddle("专家课堂", -1);
		initMenuWebData(mWvExpertMenu);
		initMenuWebData(mWvExpertinstro);
		mWvExpertMenu.addJavascriptInterface(new WebAppShowObjectInterface(),"zjkt");
		
		loadMenuData();
		loadInstroData();
	}
	
	 public class WebAppShowObjectInterface {
	        @JavascriptInterface
	        public void showObject(String id) {
//	            Intent intent = new Intent(mActivity, ZyObjectActivity.class);
//	            intent.putExtra(AppDefine.ZYDefine.ZY_OBJECT_ID, id);
//	            mActivity.startActivity(intent);
	        	
	        	
	        }
	 }
	 
	@SuppressLint("SetJavaScriptEnabled")
	private void initMenuWebData(WebView mWebView){
	WebSettings mSetting = 	mWebView.getSettings();
	mSetting.setJavaScriptEnabled(true);
	mSetting.setSupportZoom(true); // 可以缩放
	mSetting.setBuiltInZoomControls(true);
	
	
	}

	private void loadMenuData(){
		mWvExpertMenu.loadUrl("file:///android_asset/zjkt/noicon.html");
	}
	
	private void loadInstroData(){
		mWvExpertinstro.loadUrl("http://www.baidu.com");
	}
}
