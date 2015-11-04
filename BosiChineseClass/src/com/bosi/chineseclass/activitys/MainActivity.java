package com.bosi.chineseclass.activitys;

import java.util.List;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.XutilImageLoader;
import com.bosi.chineseclass.bean.BpcyDataBean;
import com.bosi.chineseclass.control.SampleControl;
import com.bosi.chineseclass.control.SampleHolderControlMake;
import com.bosi.chineseclass.db.dict.BPCYDATA;
import com.bosi.chineseclass.han.activitys.GameActivity;
import com.bosi.chineseclass.han.activitys.ZiYuanActivity;
import com.bosi.chineseclass.han.db.CheckDbUtils;
import com.bosi.chineseclass.han.db.DbManager;
import com.bosi.chineseclass.han.util.LogUtils;
import com.bosi.chineseclass.su.ui.actvities.DictionaryAcitvity;
import com.bosi.chineseclass.utils.BosiUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

@ContentView(R.layout.main)
public class MainActivity extends BaseActivity {
	@ViewInject(R.id.ll_main_body)
	RelativeLayout mlayoutBody;
	
	XutilImageLoader mXutilImageLoader;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		UmengUpdateAgent.update(this);
		MobclickAgent.updateOnlineConfig(this);
		getDataAsy();
		mXutilImageLoader = new XutilImageLoader(this);
		mXutilImageLoader.getBitmapFactory().display(mlayoutBody, "assets/bosi_index_bg.jpg");
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@OnClick(R.id.btn_pinyinlearn)
	public void actionPyLearn(View mView){
		Intent mIntent = new Intent(this,SampleHolderActivity.class);
		mIntent.putExtra(SampleHolderControlMake.mControlName, "PinYinLearnControl");
		startActivity(mIntent);
		
	}
	@OnClick(R.id.main_btn_hzcs)
	public void actionHzcs(View mView){
		Intent mIntent = new Intent(this,HzcsActivity.class);
		startActivity(mIntent);
	}
	@OnClick(R.id.btn_bphz)
	public void actionBphz(View mView){
		Intent mIntent = new Intent(this,SampleHolderActivity.class);
		mIntent.putExtra(SampleHolderActivity.KEY_NOTALLOWBACKKEY,"notallow");
		mIntent.putExtra(SampleControl.KEY_FRAGMENTNAMES, new String []{"BphzLevFragment"});
		mIntent.putExtra(SampleControl.KEY_PACKAGETNAME ,"com.bosi.chineseclass.fragments");
		startActivity(mIntent);
	}

	@OnClick(R.id.btn_bpcy)
	public void actionBpcy(View mView){
		Intent mIntent = new Intent(this,SampleHolderActivity.class);
		
		mIntent.putExtra(SampleHolderActivity.KEY_NOTALLOWBACKKEY,"notallow");
		mIntent.putExtra(SampleControl.KEY_FRAGMENTNAMES, new String []{"BpcyLevFragment"});
		mIntent.putExtra(SampleControl.KEY_PACKAGETNAME ,"com.bosi.chineseclass.fragments");
		startActivity(mIntent);
	}
	@OnClick(R.id.btn_zjkt)
	public void actionZjkt(View mView){
		Intent mIntent = new Intent(this,SampleHolderActivity.class);
		mIntent.putExtra(SampleControl.KEY_FRAGMENTNAMES, new String []{"ExpertClassFragment"});
		mIntent.putExtra(SampleControl.KEY_PACKAGETNAME ,"com.bosi.chineseclass.fragments");
		startActivity(mIntent);
	}
	@OnClick(R.id.bt_zyyx)
	public void actionZyyx(View mView){
		Intent mIntent = new Intent(this,GameActivity.class);
		mIntent.putExtra("title", "字源游戏");
		startActivity(mIntent);
	}
	
	
	@OnClick(R.id.bt_zy)
	public void actionZy(View mView){
		Intent ziyuanIntent = new Intent(mContext, ZiYuanActivity.class);
		startActivity(ziyuanIntent);
	}
	@OnClick(R.id.btn_zyzd)
	public void actionZyzd(View mView){
		Intent intent = new Intent(MainActivity.this, DictionaryAcitvity.class);
		startActivity(intent);
	}
	
	BPCYDATA mBpcy = new BPCYDATA();
	@OnClick(R.id.encode_bpcy)
	public void actionEncodeBpcy(View mView){
		new Thread(){
			public void run(){
				for(int i=0 ;i<13300 ;i=i+100){
					String sqlSelect = getResources().getString(R.string.select_frombpcy);
					String sqlFormat = String.format(sqlSelect, i+"",(100+i)+"");
					LogUtils.i("zhujohnle", sqlFormat);
					List<BpcyDataBean> mList = mBpcy.selectDataFromDb(sqlFormat);
					try {
						mBpcy.syncEncryptData(mList);
						LogUtils.i("zhujohnle", "success"+i);
					} catch (Exception e) {
						LogUtils.i("zhujohnle", "failed"+i);
					}
				}
			}
		}.start();
	}
	
	private void getDataAsy(){
		updateProgress(1, 2);
		
		AsyTaskBaseThread(new Runnable() {
			
			@Override
			public void run() {
				 boolean isSuccess = CheckDbUtils.checkDb();
				 if(!isSuccess){
					 finish();
				 }
				 BSApplication.getInstance().mDbManager = new DbManager(mContext);
			}
		},new Runnable() {
			
			@Override
			public void run() {
				runOnUiThread( new Runnable() {
					
					@Override
					public void run() {
						updateProgress(2, 2);
					}
				});
			}
		});
	}
	
	
	@Override
	public void onBackPressed() {
		BSApplication.getInstance().exitApp();
	}
	
	@ViewInject(R.id.iv_popu_remote_main)
	ImageView mImageHintView;
	
	@OnClick(R.id.iv_popu_remote_main)
	public void actionShowPopu(View mView){
		showPopuWindow();
	}
	
	PopupWindow mPopuWindow;

	private void showPopuWindow() {
		if (mPopuWindow != null && mPopuWindow.isShowing()) {
			mPopuWindow.dismiss();
			return;
		}
		View mPopView = View.inflate(this, R.layout.popu_main_remote,
				null);
		mPopuWindow = new PopupWindow(mPopView,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		mPopuWindow.setTouchable(true);
		mPopuWindow.setOutsideTouchable(true);
		mPopuWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopuWindow.showAsDropDown(mImageHintView, 0, 0);
		//
		View mFirst = mPopView.findViewById(R.id.main_popu_rl_instrohowtuse);
		mFirst.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				BosiUtils.intentToWebActivity(AppDefine.URLDefine.URL_INSTRO_HELP, "使用说明", MainActivity.this);
				mPopuWindow.dismiss();
			}
		});
		
		View mSecond = mPopView.findViewById(R.id.main_popu_rl_userlimit);
		mSecond.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mPopuWindow.dismiss();
			}
		});
	}

}
