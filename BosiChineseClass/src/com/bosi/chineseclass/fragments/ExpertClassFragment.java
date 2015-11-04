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
import com.bosi.chineseclass.activitys.SampleHolderActivity;
import com.bosi.chineseclass.control.SampleControl;
import com.bosi.chineseclass.control.SampleHolderControlMake;
import com.bosi.chineseclass.han.activitys.ZyObjectActivity;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.han.fragments.ZiyuanBihuaFragment.WebAppShowObjectInterface;
import com.bosi.chineseclass.han.util.PreferencesUtils;
import com.bosi.chineseclass.utils.BosiUtils;
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
	        	Intent mIntent = new Intent(mActivity, SampleHolderActivity.class);
	        	mIntent.putExtra(ExpertClassDitalFragment.KEY_FATHERID, id);
	        	mIntent.putExtra(SampleControl.KEY_FRAGMENTNAMES, new String []{"ExpertClassDitalFragment"});
	    		mIntent.putExtra(SampleControl.KEY_PACKAGETNAME ,"com.bosi.chineseclass.fragments");
	    		startActivity(mIntent);
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
