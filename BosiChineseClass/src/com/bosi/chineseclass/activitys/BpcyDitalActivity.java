package com.bosi.chineseclass.activitys;

import java.io.File;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.XutilImageLoader;
import com.bosi.chineseclass.bean.BpcyDataBean;
import com.bosi.chineseclass.components.BpStasticLayout;
import com.bosi.chineseclass.components.MediaPlayerPools;
import com.bosi.chineseclass.control.DownLoadResouceControl.DownLoadInterface;
import com.bosi.chineseclass.control.DownLoadResouceControl;
import com.bosi.chineseclass.control.OnDataChangedListener;
import com.bosi.chineseclass.db.BPCY;
import com.bosi.chineseclass.db.dict.BPCYDATA;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.utils.BosiUtils;
import com.bosi.chineseclass.utils.DesUtils;
import com.bosi.chineseclass.views.PaintPadWindow;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.layout_bpcydital)
public class BpcyDitalActivity extends BaseActivity implements
DownLoadInterface{
	@ViewInject(R.id.headactionbar)
	View mViewHead;
	HeadLayoutComponents mHeadActionBar;
	
	XutilImageLoader mUtilImageLoader;
	private DownLoadResouceControl mDownLoadControl;
	
	MediaPlayerPools mMediaPlayerPools;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mHeadActionBar = new HeadLayoutComponents(this,mViewHead);
		mHeadActionBar.setTextMiddle("爆破成语", -1);
		mHeadActionBar.setDefaultLeftCallBack(true);
		mHeadActionBar.setDefaultRightCallBack(true);
		
		mUtilImageLoader = new XutilImageLoader(this);
		
		mDownLoadControl = new DownLoadResouceControl(this);
		mDownLoadControl.setOnDownLoadCallback(this);

	    mPaintPadWindow = new PaintPadWindow(this);
		mMediaPlayerPools = new MediaPlayerPools(this);
		
		
		setUpBpCyControl();
	
	}

	
	public void onBackPressed() {
		if(mPaintPadWindow!=null)
		mPaintPadWindow.dismissView();
	};

	PaintPadWindow mPaintPadWindow;

	@OnClick(R.id.bt_word_pad)
	public  void showWordPad(View mView) {
		mPaintPadWindow.createFloatView();
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		mMediaPlayerPools.destroyMediaPlayer();
	}

	
	BPCY mBpHistory = new BPCY();
	BPCYDATA mBpcyData = new BPCYDATA();
	
	
	BpStasticLayout mBpStasitcLayout;
	@ViewInject(R.id.ll_bpcy_stastic)
	LinearLayout mLayoutStastic;

	
	public static final String EXTRA_NAME_WORDS_TAG = "tag";
	private void setUpBpCyControl() {
		updateProgress(0, 1);
		final int TAG = getIntent().getIntExtra(EXTRA_NAME_WORDS_TAG, -1);
		if (TAG != -1) {
			int tagFromBpLv = getIntent().getIntExtra(EXTRA_NAME_WORDS_TAG,
					AppDefine.ZYDefine.BPHZ_TAG_NORMAL);
			mBpStasitcLayout = new BpStasticLayout(mContext);
			mBpStasitcLayout.setViewBpcyControl(tagFromBpLv,
					new OnDataChangedListener() {
						@Override
						public void chagePageData(int refid) {
							
							updateUI(refid+"") ;

						}

						@Override
						public void chagePageData() {
							mSampleHintView.setVisibility(View.GONE);
						}

						@Override
						public void onSampleLoadBefore() {
							mSampleHintView.setVisibility(View.VISIBLE);
							mUtilImageLoader.getBitmapFactory().display(mSampleHintView, "assets/hint_bphz_learnbg.jpg");
						}
					});

			mLayoutStastic.addView(mBpStasitcLayout.getBaseView());
			mLayoutStastic.setVisibility(View.VISIBLE);

		} else {
			mLayoutStastic.setVisibility(View.INVISIBLE);
		}
		
	}
	BpcyDataBean mBpcyBean  ;

	private void updateUI(String id) {
		mIvDispCy.setImageDrawable(getResources().getDrawable(R.drawable.background_1));
		
		String mSql = getResources().getString(R.string.select_frombpcybaseid);
		String mSqlFormat = String.format(mSql, id);
		List<BpcyDataBean>  mLists= mBpcyData.selectDataFromDb(mSqlFormat);
		if(mLists!=null &&mLists.size()>0){
			mBpcyBean =mLists.get(0);
		}
		if(mBpcyBean!=null){
			try {
				setUpNameAndPinyin();
			} catch (Exception e) {
			}
			boolean isDownLoadSuccess = mDownLoadControl.downloadFiles();
			if(isDownLoadSuccess){
				actionDownLoadSuccess();
			}
		}else{
			showToastLong("该组成语未开放");
			finish();
		}
	}
	
	private String getExplanDataText() throws Exception{
		StringBuilder msb = new StringBuilder();
		msb.append("[成语释义]");
		msb.append(mBpcyBean.CYShiyi);
		msb.append("[成语出处]");
		msb.append(DesUtils.decode(mBpcyBean.CYChuchu));
		msb.append("[成语示例]");
		msb.append(mBpcyBean.CYShili);
		msb.append("[近义词]");
		msb.append(DesUtils.decode(mBpcyBean.CYJinyi));
		msb.append("[反义词]");
		msb.append(DesUtils.decode(mBpcyBean.CYFanyi));
		
		String mAppenedData = msb.toString();
		BosiUtils.loadTransfDataBaseSquare(mTvExplain, mAppenedData);
		
		return null;
	}
	
	@ViewInject(R.id.tv_cyname)
	TextView mTvName;
	@ViewInject(R.id.tv_cypinyin)
	TextView mTvCyPinyin;
	@ViewInject(R.id.iv_hint_bphz_learn)
    ImageView mSampleHintView;
	@ViewInject(R.id.tv_bpcy)
	TextView mTvExplain;
	
	@OnClick(R.id.iv_hint_bphz_learn)
	public void actionDismissHintView(View mView){
		mSampleHintView.setVisibility(View.GONE);
	}
	
	private void setUpNameAndPinyin() throws Exception{
		mTvName.setText(DesUtils.decode(mBpcyBean.CYCimu));
		mTvCyPinyin.setText(DesUtils.decode(mBpcyBean.CYFayin));
		getExplanDataText();
	}
	
	@ViewInject(R.id.iv_bpcy_dital_imgs)
	ImageView mIvDispCy;
	@Override
	public String[] getDownLoadUrls() {
		
	/*	http://www.yuwen100.cn/yuwen100/hzzy/zyzd-clips/cytu/220001.jpg
	 * http://www.yuwen100.cn/yuwen100/hzzy/zyzd-clips/cyflv/220001.MP4
	 * http://www.yuwen100.cn/yuwen100/hzzy/zyzd-clips/cyread/220001.mp3
	 * 
*/		
		String mUrls [] = new String[3];
		mUrls[0] = "http://www.yuwen100.cn/yuwen100/hzzy/zyzd-clips/cytu/"+mBpcyBean.Cybh+".jpg";
		mUrls[1] = "http://www.yuwen100.cn/yuwen100/hzzy/zyzd-clips/cyflv/"+mBpcyBean.Cybh+".MP4";
		mUrls[2] = "http://www.yuwen100.cn/yuwen100/hzzy/zyzd-clips/cyread/"+mBpcyBean.Cybh+".mp3";
		return mUrls;
	}
	
	private void actionDownLoadSuccess(){
		dismissProgress();
		mUtilImageLoader.getBitmapFactory().display(mIvDispCy, mDownLoadControl.getAbsFilePath()+mBpcyBean.Cybh+".jpg");
		//actionReadCyPy(null);
	}

	@Override
	public void onDownLoadCallback(int mCurrentSize, int wholeSize) {
		if(mCurrentSize == wholeSize){
			actionDownLoadSuccess();
		}
	}

	@Override
	public String getFolderPath() {
		 return AppDefine.FilePathDefine.APP_CYDITALNPATH + mBpcyBean.Cybh+"/";
	}
	
	@OnClick(R.id.iv_cyspeak)
	public void actionReadCyPy(View mView){
		if(mMediaPlayerPools==null) return;
		mMediaPlayerPools.playMediaFile(mDownLoadControl.getAbsFilePath()+mBpcyBean.Cybh+".mp3");
	}
	
	@OnClick(R.id.tv_showcy_dgdh)
	private void showCyDgVideo(View mView){
		
		String mAbsFilePath = mDownLoadControl.getAbsFilePath()+mBpcyBean.Cybh+".MP4";
		File mFile = new File(mAbsFilePath);
		if(mFile ==null ||!mFile.exists()){
			showToastShort("未找到该典故相关视频");
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		String type = "video/mp4";
		Uri uri = Uri.parse(mAbsFilePath);
		intent.setDataAndType(uri, type);
		startActivity(intent);
	}
}
