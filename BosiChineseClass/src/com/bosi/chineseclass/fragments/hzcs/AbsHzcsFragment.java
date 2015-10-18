package com.bosi.chineseclass.fragments.hzcs;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.XutilImageLoader;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.utils.NetStateUtil;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

//汉字常识的基础功能
public abstract class AbsHzcsFragment extends BaseFragment implements
		OnClickListener {

	XutilImageLoader mImageLoader;

	LinearLayout mLayoutMenu;

	ImageView mIvDital;

	Button mBtLeft;
	Button mBtRight;

	String mCurrentData[];
	String mCurrentDataHtmlData[];
	int currentPosition = -1;

	View mViewHead;

	protected HeadLayoutComponents mHeadActionBar;

	View mViewBody;

	TextView mTvDitalTitle;

	LinearLayout mLayoutWebViewBody;

	WebView mWebView;

	ProgressBar mProgressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null)
			return;
		mImageLoader = new XutilImageLoader(mActivity);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected View getBasedView() {
		return View.inflate(mActivity, R.layout.layout_hzcs_base, null);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void afterViewInject() {
		mLayoutMenu = (LinearLayout) mBaseView
				.findViewById(R.id.ll_hzcs_leftmenu);

		mIvDital = (ImageView) mBaseView.findViewById(R.id.iv_hzcs_dital);

		mBtLeft = (Button) mBaseView.findViewById(R.id.bt_hzcs_dital_left);
		mBtLeft.setOnClickListener(this);
		mBtRight = (Button) mBaseView.findViewById(R.id.bt_hzcs_dital_right);
		mBtRight.setOnClickListener(this);

		mWebView = (WebView) mBaseView.findViewById(R.id.wv_hzcs_dital);
        
        
		mProgressBar = (ProgressBar) mBaseView.findViewById(R.id.pb_forwebview);
		mLayoutWebViewBody = (LinearLayout) mBaseView
				.findViewById(R.id.ll_hzcs_dital);

		mTvDitalTitle = (TextView) mBaseView
				.findViewById(R.id.tv_hzcsdital_title);
		mViewHead = mBaseView.findViewById(R.id.headactionbar);
		mViewBody = mBaseView.findViewById(R.id.rl_hzcs_body);
		displayBgView();
		mHeadActionBar = new HeadLayoutComponents(mActivity, mViewHead);
		initMenu();
		initWebView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		WebSettings webSettings = mWebView.getSettings();
		//webSettings.setUseWideViewPort(true); 自适应屏幕
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true); // 可以缩放
		mWebView.setWebViewClient(new WebViewClient() {

			boolean isError = false;

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				view.stopLoading();
				view.clearFormData();
				view.clearHistory();
				if (isError) {
					displayCurrentPic();
				}
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {

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

	}

	public abstract void initMenu();

	public void actionLeft(View mView) {
		mBtRight.setVisibility(View.VISIBLE);
		currentPosition--;
		if (currentPosition < 0)
			return;

		updateDitalPg();
		if (currentPosition == 0) {
			mBtLeft.setVisibility(View.GONE);
			mBtRight.setVisibility(View.VISIBLE);
		}
	}

	public void actionRight(View mView) {
		mBtLeft.setVisibility(View.VISIBLE);
		currentPosition++;
		if (currentPosition == mCurrentData.length) {
			return;
		}

		updateDitalPg();
		if (currentPosition == mCurrentData.length - 1) {
			mBtRight.setVisibility(View.GONE);
			mBtLeft.setVisibility(View.VISIBLE);

		}
	}

	/**
	 * 如果网络状态不通直接返回 -1
	 * 
	 * */
	private int getRespStatus(String url) {

		int status = -1;

		if (!NetStateUtil.isNetWorkAlive(mActivity)) {
			return status;
		}

		try {

			HttpHead head = new HttpHead(url);

			HttpClient client = new DefaultHttpClient();

			HttpResponse resp = client.execute(head);

			status = resp.getStatusLine().getStatusCode();

		} catch (IOException e) {
		}

		return status;

	}

	int isExistHtmlFile = -1;

	protected synchronized void updateDitalPg() {
		if (mCurrentData == null)
			return;
		if (currentPosition == -1 || currentPosition >= mCurrentData.length) {
			currentPosition = 0;

		}
		// 换成加载网页 否则
		mIvDital.setVisibility(View.GONE);
		mLayoutWebViewBody.setVisibility(View.VISIBLE);
		mWebView.clearHistory();

		// 首先异步检查当前文件是否存在如果不存在直接加载图片
		final String mCurrentJpgPath = mCurrentData[currentPosition];
		final String mUrlPath = mCurrentJpgPath.substring(0,
				mCurrentJpgPath.lastIndexOf("."))
				+ ".html";

		mActivity.showProgresssDialogWithHint("加载中...");
		isExistHtmlFile = -1;
		mActivity.AsyTaskBaseThread(new Runnable() {

			@Override
			public void run() {
				isExistHtmlFile = getRespStatus(mUrlPath);
			}
		}, new Runnable() {

			@Override
			public void run() {
/*				if (isExistHtmlFile != -1 && isExistHtmlFile == 200) {
					mWebView.loadUrl(mUrlPath);
				} else {
					displayCurrentPic();
				}*/
				
				mWebView.loadUrl("file:///android_asset/zjl/qiehuan.html");
				mActivity.dismissProgressDialog();
			}
		});

	}

	private void displayCurrentPic() {
		mLayoutWebViewBody.setVisibility(View.GONE);
		mIvDital.setVisibility(View.VISIBLE);
		// 加载当前的图片
		mImageLoader.getBitmapFactory().display(mIvDital,
				mCurrentData[currentPosition]);
	}

	protected abstract void downLoadImageOverAction();

	@Override
	public void onClick(View mView) {
		switch (mView.getId()) {
		case R.id.bt_hzcs_dital_left:
			actionLeft(mView);
			break;
		case R.id.bt_hzcs_dital_right:
			actionRight(mView);
			break;

		default:
			break;
		}
	}

	protected void displayBgView() {
		mViewBody.setBackgroundResource(R.drawable.hzqy_ditalbg);
	}

	/*---------------添加下载模块----------------*/

	int loadedData = -1;

	public void downloadimgs() {
		if (mCurrentData.length > 1) {
			mBtLeft.setVisibility(View.GONE);
			mBtRight.setVisibility(View.VISIBLE);
		} else {
			mBtLeft.setVisibility(View.GONE);
			mBtRight.setVisibility(View.GONE);
		}
		loadedData = -1;
		updateProgress();
		for (int i = 0; i < mCurrentData.length; i++) {
			mImageLoader.getBitmapFactory().display(mIvDital, mCurrentData[i],
					new BitmapLoadCallBack<View>() {

						@Override
						public void onLoadCompleted(View container, String uri,
								Bitmap bitmap, BitmapDisplayConfig config,
								BitmapLoadFrom from) {
							updateProgress();
						}

						@Override
						public void onLoadFailed(View container, String uri,
								Drawable drawable) {
							updateProgress();
						}

						@Override
						public void onLoading(View container, String uri,
								BitmapDisplayConfig config, long total,
								long current) {
							super.onLoading(container, uri, config, total,
									current);
						}

					});
		}
	}

	private void updateProgress() {
		loadedData++;
		mActivity.updateProgress(loadedData, mCurrentData.length);
		if (loadedData == mCurrentData.length) {
			mActivity.dismissProgress();
			downLoadImageOverAction();
			loadedData = 0;

		}
	}

}
