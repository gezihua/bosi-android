package com.zy.booking.activitys;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.dk.view.drop.CoverManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.update.UmengUpdateAgent;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.EmsgManager;
import com.zy.booking.R;
import com.zy.booking.control.LocationCtrol;
import com.zy.booking.control.LocationCtrol.OnLocationChangedListener;
import com.zy.booking.control.UpdateInfoControl;
import com.zy.booking.db.MSGHISTORY;
import com.zy.booking.fragments.ContactsFragment;
import com.zy.booking.fragments.ModelIndexFragment;
import com.zy.booking.fragments.NotifyCenterFragment;
import com.zy.booking.fragments.PersionalFragment;
import com.zy.booking.modle.SeverFragmentPagerAdapter;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.BaseTools;
import com.zy.booking.util.PreferencesUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_index)
public class IndexActivity extends BaseActivity implements OnHttpActionListener {
	@ViewInject(R.id.mViewPager)
	ViewPager mViewPager;

	@ViewInject(R.id.mRadioGroup_content)
	LinearLayout mRadioGroup_content;

	SeverFragmentPagerAdapter mAdapetr;

	private ArrayList<Fragment> fragments;

	private int mScreenWidth;

	/** Item宽度 */
	private int mItemWidth = 0;

	private String[] mArrayMenuData;
	
	
	UpdateInfoControl mCacheInfoControl;
	

	private int[] mArrayMenuBgId = { R.drawable.selector_bottom_booking,
			R.drawable.selector_bottom_activity,
			R.drawable.selector_bottom_mine, R.drawable.selector_bottom_friend };

	private List<OnLocationChangedListener> mRegisterLocationClints;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
        Intent mIntent = new Intent(this,AdvertActivity.class);
		startActivity(mIntent);
		UmengUpdateAgent.update(this);
		
		mCacheInfoControl = new UpdateInfoControl(this);
		mScreenWidth = BaseTools.getWindowsWidth(this);
		mRegisterLocationClints = new ArrayList<OnLocationChangedListener>();

		
		String token = PreferencesUtils.getString(this, "token");
		if (TextUtils.isEmpty(token)) {
			intentToAuth();
			finish();
		} else {
			sendTokenData(token);
		}
		
		mCacheInfoControl.getCacheData();
		initViewPager();
		initMenuData();
		initBottomTab();
		initFragments();
		initWaterDrop();

		initLocationControl();

		registerEmsgMessageReciver();
		
		holdPowerManager();
		
		
	}
	WakeLock wakeLock;
	
	// hold the lock for the power
	private void holdPowerManager() {
		PowerManager mPowerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		 wakeLock = mPowerManager.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "EMSG");
		if (!wakeLock.isHeld())
			wakeLock.acquire();
	}
	
	public void releasePower(){
		if(wakeLock!=null)wakeLock.release();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	public void requestLocation() {
		mLocationControl.onCreate(null);
	}

	LocationCtrol mLocationControl;

	private void initLocationControl() {
		mLocationControl = new LocationCtrol(this);

		mLocationControl.onCreate(null);
		mLocationControl
				.setLocationChangedListener(new OnLocationChangedListener() {

					@Override
					public void onLocationChanged(BDLocation mBdLocation) {
						mLocationControl.onDestroy();
						notifyLocationServer(mBdLocation);
					}
				});
	}

	private void registerLocationServer(
			OnLocationChangedListener mLocationServer) {
		mRegisterLocationClints.add(mLocationServer);
	}

	private void notifyLocationServer(BDLocation mBdLocation) {
		for (OnLocationChangedListener mLocationServer : mRegisterLocationClints) {
			if (mLocationServer != null) {
				mLocationServer.onLocationChanged(mBdLocation);
			}
		}
	}

	private void initViewPager() {
		mAdapetr = new SeverFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setOffscreenPageLimit(4);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);
	}

	private void initMenuData() {
		mArrayMenuData = getString(R.string.menu_tabs_customer).split("#");
		if (mArrayMenuData.length > 1) {
			mItemWidth = mScreenWidth / mArrayMenuData.length;// 一个Item宽度为屏幕的1/4
		} else {
			mRadioGroup_content.setVisibility(View.GONE);
		}

	}

	private void initFragments() {
		fragments = new ArrayList<Fragment>();

		ModelIndexFragment mIndexFragment = new ModelIndexFragment();

		ContactsFragment mContactFriendsFragment = new ContactsFragment();
		
		//registerLocationServer(mSendInfoFragment);
		
		
		PersionalFragment mPersionalFragmet = new PersionalFragment();
		NotifyCenterFragment mPersionalFragmet12 = new NotifyCenterFragment();
		fragments.add(mIndexFragment);
		fragments.add(mContactFriendsFragment);
		fragments.add(mPersionalFragmet);
		registerLocationServer(mIndexFragment);
		fragments.add(mPersionalFragmet12);
		mAdapetr.appendList(fragments);

	}

	private void initBottomTab() {
		for (int i = 0; i < mArrayMenuData.length; i++) {

			int imgId = mArrayMenuBgId[i];

			View mViewItem = initTabItem(mArrayMenuData[i], i, imgId);

			mRadioGroup_content.addView(mViewItem);
		}

	}

	private final int CODE_REQUESTTOKEN = 101;

	private void sendTokenData(String token) {
		List<NameValuePair> mLists = new ArrayList<NameValuePair>();
		mLists.add(new BasicNameValuePair("token", token));
		sendData(mLists, AppDefine.URL_GETTOKENINFO, this, CODE_REQUESTTOKEN);
	}

	/**
	 * ViewPager切换监听方法
	 */
	public OnPageChangeListener pageListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			mViewPager.setCurrentItem(position);
			selectTab(position);
		}
	};

	/**
	 * 选择的Column里面的Tab
	 */
	private void selectTab(int tab_postion) {
		// 判断是否选中
		for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
				checkView.setBackgroundColor(Color.LTGRAY);
			} else {
				ischeck = false;
				checkView.setBackgroundColor(Color.WHITE);
			}
			checkView.setSelected(ischeck);
		}
	}

	TextView mTextViewDotHot;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private View initTabItem(String text, final int i, int imgId) {
		View mView = LayoutInflater.from(this).inflate(
				R.layout.item_layout_menuitem, null);

		LinearLayout.LayoutParams mLayoutParam = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParam.weight = 1;
		mView.setLayoutParams(mLayoutParam);

		TextView mTextView = (TextView) mView.findViewById(R.id.tv_menu_text);
		mTextView.setText(text);

		if (i == mArrayMenuData.length - 1) {
			TextView mTextViewDothot = (TextView) mView
					.findViewById(R.id.tv_index_menu_dothot);
			this.mTextViewDotHot = mTextViewDothot;
			mTextViewDothot.setVisibility(View.VISIBLE);
			this.mTextViewDotHot = mTextViewDothot;
		}

		ImageView mImageView = (ImageView) mView
				.findViewById(R.id.iv_menu_icon);
		mImageView.setBackgroundResource(imgId);

		mView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(i);
			}
		});

		return mView;
	}

	// 计算器 变量 用于判断当期那点击返回键的次数
	int tempClickBack = 0;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationControl.onDestroy();
		unregisterReceiver(mEmsgBrodCastReciver);

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onBackPressed() {
		tempClickBack++;
		if (tempClickBack == 1) {
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			mViewPager.postDelayed(new Runnable() {

				@Override
				public void run() {
					tempClickBack = 0;
				}
			}, 5000);
			return;
		} else if (tempClickBack == 2) {
			finish();
		}
	}

	private void initWaterDrop() {
		CoverManager.getInstance().init(this);
		CoverManager.getInstance().setMaxDragDistance(350);
		CoverManager.getInstance().setExplosionTime(150);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		CpApplication.getApplication().mEmsgManager.cancleNotify();
		updateUnReadCount();
		
	}

	private void updateUnReadCount(){
	    String unReadMsgNum = mMsgDb
                .getUnReadMsgCount(getString(R.string.select_allunreadcount));
        if (unReadMsgNum.equals("0")) {
            mTextViewDotHot.setVisibility(View.INVISIBLE);
            unReadMsgCount = 0;
        } else {
            unReadMsgCount = Integer.parseInt(unReadMsgNum);
            mTextViewDotHot.setText(unReadMsgNum);
        }
	}
	int unReadMsgCount = 0;
	BroadcastReceiver mEmsgBrodCastReciver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context mContext, Intent mIntent) {
		    if(!isFinishing())
		    updateUnReadCount();
		}

	};

	boolean isFinishByMySelf = false;

	private void intentToAuth() {
		Intent intent = new Intent(this, AuthActivity.class);
		startActivity(intent);
		isFinishByMySelf = true;
		finish();

	}

	private void registerEmsgMessageReciver() {

		IntentFilter mIntentFiter = new IntentFilter();
		// 注册即时消息接收广播
		mIntentFiter.addAction(EmsgManager.ACTION_NEWMSGCOMING);
		// 接收离线消息广播
		// 对应的上下文对象
		mContext.registerReceiver(mEmsgBrodCastReciver, mIntentFiter);

	}

	MSGHISTORY mMsgDb = new MSGHISTORY();

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {

	}

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		if (resultCode == CODE_REQUESTTOKEN) {
			if (isSuccess) {
				CpApplication.getApplication().mEmsgManager.auth();
			} 
		}
	}
}
