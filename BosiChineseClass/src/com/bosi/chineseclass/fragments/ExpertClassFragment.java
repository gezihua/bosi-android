package com.bosi.chineseclass.fragments;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.activitys.SampleHolderActivity;
import com.bosi.chineseclass.control.SampleControl;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.lidroid.xutils.view.annotation.ViewInject;

//专家课堂
public class ExpertClassFragment extends BaseFragment {

	HeadLayoutComponents mHeadActionbar;

	@ViewInject(R.id.headactionbar)
	View mHeadActionBar;
	@ViewInject(R.id.expertmain_wv_menu)
	WebView mWvExpertMenu;

	@ViewInject(R.id.expertmain_wv_instro)
	WebView mWvExpertinstro;

	@ViewInject(R.id.pb_forwebview)
	ProgressBar mProgressBar;
	
	@ViewInject(R.id.pb_forwebviewmenu)
	ProgressBar mProgressForMenu;

	@Override
	protected View getBasedView() {
		return View.inflate(mActivity, R.layout.layout_expertclass_mainpage,
				null);
	}

	@Override
	protected void afterViewInject() {
		mHeadActionbar = new HeadLayoutComponents(mActivity, mHeadActionBar);
		mHeadActionbar.setTextMiddle("专家课堂", -1);
		initMenuWebData(mWvExpertMenu);
		initMenuWebData(mWvExpertinstro);
		mWvExpertMenu.addJavascriptInterface(new WebAppShowObjectInterface(),
				"zjktd");

		mWvExpertinstro.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
					mProgressForMenu.setVisibility(View.GONE);
					return;
				}
				mProgressForMenu.setVisibility(View.VISIBLE);
				mProgressForMenu.setProgress(newProgress);
			}

		});
		mWvExpertMenu.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
					mProgressBar.setVisibility(View.GONE);
					return;
				}
				mProgressBar.setVisibility(View.VISIBLE);
				mProgressBar.setProgress(newProgress);
			}

		});

		loadMenuData();
		loadInstroData(AppDefine.URLDefine.URL_ZJKT_ZYINSTRO);
	}

	public class WebAppShowObjectInterface {
		@JavascriptInterface
		public void showObject(String id) {
			// System.out.println(id);
			if (id.endsWith("html")) {
				loadInstroData(AppDefine.URLDefine.URL_BASEURL + id);
			} else {
				Intent mIntent = new Intent(mActivity,
						SampleHolderActivity.class);
				mIntent.putExtra(ExpertClassDitalFragment.KEY_FATHERID, id);
				mIntent.putExtra(SampleControl.KEY_FRAGMENTNAMES,
						new String[] { "ExpertClassDitalFragment" });
				mIntent.putExtra(SampleControl.KEY_PACKAGETNAME,
						"com.bosi.chineseclass.fragments");
				startActivity(mIntent);
			}
		}

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initMenuWebData(WebView mWebView) {
		WebSettings mSetting = mWebView.getSettings();
		mSetting.setJavaScriptEnabled(true);
		mSetting.setSupportZoom(true); // 可以缩放

	}

	private void loadMenuData() {
		// TODO FOR CURRECT URL
		mWvExpertMenu.loadUrl(AppDefine.URLDefine.URL_ZJKT_FIRSTPAGEMENU);
	}

	private void loadInstroData(String mUrl) {
		mWvExpertinstro.loadUrl(mUrl);
	}
}
