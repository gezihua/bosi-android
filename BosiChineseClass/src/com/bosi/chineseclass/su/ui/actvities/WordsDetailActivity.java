package com.bosi.chineseclass.su.ui.actvities;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.XutilImageLoader;
import com.bosi.chineseclass.components.BpStasticLayout;
import com.bosi.chineseclass.components.MutilMediaPlayerTools;
import com.bosi.chineseclass.components.MutilMediaPlayerTools.MutilMediaPlayerListener;
import com.bosi.chineseclass.components.WordDitalExpainComponent;
import com.bosi.chineseclass.control.DownLoadResouceControl;
import com.bosi.chineseclass.control.DownLoadResouceControl.DownLoadInterface;
import com.bosi.chineseclass.db.BPHZ;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.su.db.DbUtils;
import com.bosi.chineseclass.su.db.Word;
import com.bosi.chineseclass.utils.BosiUtils;
import com.bosi.chineseclass.views.BsVideoViewGroup;
import com.bosi.chineseclass.views.PaintPadWindow;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_container)
public class WordsDetailActivity extends BaseActivity implements
		DownLoadInterface, MutilMediaPlayerListener {
	@ViewInject(R.id.deatail_headactionbar)
	View mHeadActionBar;
	HeadLayoutComponents mHeadActionBarComp;

	@ViewInject(R.id.iv_jfall)
	ImageView mjfImg;

	@ViewInject(R.id.oracle_word)
	ImageView mOracleWord;

	@ViewInject(R.id.detail_word)
	TextView mWordTextView;

	@ViewInject(R.id.word_explain)
	TextView mExplainTextView;

	@ViewInject(R.id.ytzi)
	TextView mYtTextView;

	@ViewInject(R.id.video_pad)
	BsVideoViewGroup mVideoView;
	@ViewInject(R.id.word_pad)
	ImageButton mPadView;

	@ViewInject(R.id.sound_container)
	private View mSoundContainer;
	@ViewInject(R.id.ll_word_dital)
	View mllExpainBody;

	Word mCurrentWord;
	
	XutilImageLoader mImageLoader;

	private DownLoadResouceControl mDownLoadControl;

	private void init() {
		mDownLoadControl = new DownLoadResouceControl(this);
		mDownLoadControl.setOnDownLoadCallback(this);

		mHeadActionBar = findViewById(R.id.deatail_headactionbar);
		initHeadActionBarComp();

		mExpainComponent = new WordDitalExpainComponent(this, mllExpainBody);
		mPaintPadWindow = new PaintPadWindow(mContext);
		mImageLoader = new XutilImageLoader(mContext);
		setUpBpWordsControl();
		
	}

	/** 初始化头部 */
	private void initHeadActionBarComp() {
		
		String mTitleName = getIntent().getStringExtra(EXTRA_NAME_WORDS_NAME);
		mHeadActionBarComp = new HeadLayoutComponents(this, mHeadActionBar);
		mHeadActionBarComp.setTextMiddle(mTitleName, -1);
		mHeadActionBarComp.setDefaultLeftCallBack(true);
		mHeadActionBarComp.setDefaultRightCallBack(true);
	}

	// 记录在声音的队列中
	private List<String> sounds = new ArrayList<String>();

	@OnClick(R.id.word_pad)
	public void actionWordPad(View mView) {
		showWordPad();
	}

	@OnClick(R.id.sound_container)
	public void actionSoundPad(View mView) {
		playSound();
	}

	public void onBackPressed() {
		if(mPaintPadWindow!=null)
		mPaintPadWindow.dismissView();
	};

	PaintPadWindow mPaintPadWindow;

	private void showWordPad() {
		mPaintPadWindow.createFloatView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPaintPadWindow.onDestroy();
		mPaintPadWindow.dismissView();
	}

	private MutilMediaPlayerTools mMutilMediaPlayerTools;

	private void playSound() {
		if (mMutilMediaPlayerTools != null) {
			mMutilMediaPlayerTools.setCurrentFilePath(getLocalSoundsPath());
			mMutilMediaPlayerTools.play();
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		init();
		mMutilMediaPlayerTools = new MutilMediaPlayerTools(this);
		mMutilMediaPlayerTools.setMutilMediaPlayerListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mMutilMediaPlayerTools != null) {
			mMutilMediaPlayerTools.onDestory();
		}
	}

	private void showDetail(Word detail) {
		String temp = null;
		if (!TextUtils.isEmpty(detail.pinyin)) {
			temp = detail.pinyin;
		}
		if (!TextUtils.isEmpty(detail.ytzi)) {
			temp += "(" + detail.ytzi + ")";
		}
		mYtTextView.setText(temp);
		if (!TextUtils.isEmpty(detail.shiyi)) {
			BosiUtils.loadTransfDataBaseSquare(mExplainTextView,detail.shiyi);
		}
	}
	
	
	/** 用于展示文字的具体示意 */
	private void showExplain(Word detail) {
		mExpainComponent.setData(new String[] { detail.yanbian, detail.cysy,
				detail.cy });
		mWordTextView.setText(detail.zitou);
	}

	WordDitalExpainComponent mExpainComponent;
	// ------------------------------------------- 添加和统计布局的相关内容

	BpStasticLayout mBpStasitcLayout;
	@ViewInject(R.id.ll_hzdital_stastic)
	LinearLayout mLayoutStastic;

	public static final String EXTRA_NAME_WORDS_TAG = "tag";  //for 修改为当前的控制器
	public static final String EXTRA_NAME_WORDS_NAME = "name"; //当前标题名
 
	BPHZ mBphz = new BPHZ();
	@ViewInject(R.id.iv_hint_bphz_learn)
    ImageView mSampleHintView;
	
	@OnClick(R.id.iv_hint_bphz_learn)
	public void onClick(View mView){
		mllExpainBody.setVisibility(View.VISIBLE);
		mExplainTextView.setVisibility(View.VISIBLE);
		mSampleHintView.setVisibility(View.GONE);
		
	}
	private void setUpBpWordsControl() {
		updateProgress(0, 1);
		final int TAG = getIntent().getIntExtra(EXTRA_NAME_WORDS_TAG, -1);
		if (TAG != -1) {
			int tagFromBpLv = getIntent().getIntExtra(EXTRA_NAME_WORDS_TAG,
					AppDefine.ZYDefine.BPHZ_TAG_NORMAL);
			mBpStasitcLayout = new BpStasticLayout(mContext);
			mBpStasitcLayout.setViewControl(tagFromBpLv,
					new com.bosi.chineseclass.control.OnDataChangedListener() {
						@Override
						public void chagePageData(int refid) {
							updateUI(refid + "", "");

						}

						@Override
						public void chagePageData() {
							onClick(null);
						}

						@Override
						public void onSampleLoadBefore() {
							mImageLoader.getBitmapFactory().display(mSampleHintView, "assets/hint_bphz_learnbg.jpg");
							mllExpainBody.setVisibility(View.INVISIBLE);
							mExplainTextView.setVisibility(View.INVISIBLE);
							mSampleHintView.setVisibility(View.VISIBLE);
							
						}
					});

			mLayoutStastic.addView(mBpStasitcLayout.getBaseView());
			mLayoutStastic.setVisibility(View.VISIBLE);

		} else {
			mLayoutStastic.setVisibility(View.INVISIBLE);
			String word = getIntent().getStringExtra("word");
			updateUI("", word);
		}
		
	}

	private void updateUI(String id, String word) {
		mCurrentWord = DbUtils.getInstance(this).getExplain(word, id);
		if(mCurrentWord ==null ||TextUtils.isEmpty(mCurrentWord.zitou)){
			showToastLong("未找到相关字源");
			mContext.finish();
		}
		showDetail(mCurrentWord);
		showExplain(mCurrentWord);
		boolean isDownLoadSuccess = mDownLoadControl.downloadFiles();
		if(isDownLoadSuccess){
			actionSuccess();
		}
	}

	// ------------------------------------------------------------下载播放生意-----------------------------------------------
	@Override
	public void onDownLoadCallback(int mCurrentSize, int wholeSize) {
		if(mCurrentSize == wholeSize){
			actionSuccess();
		}
	}

	@Override
	public void finished() {
		if (mMutilMediaPlayerTools != null) {
			mMutilMediaPlayerTools.reset();
		}
	}
	
	
	public String[] getLocalSoundsPath(){
		String [] mFilePaths = null;
		if(sounds!=null &&sounds.size()>0){
			mFilePaths = new String[sounds.size()];
			for(int i=0;i<sounds.size();i++){
				mFilePaths[i] = mDownLoadControl.getAbsFilePath()+sounds.get(i)
						+ ".mp3";
			}
		}
		return mFilePaths;
	}

	@Override
	public String[] getDownLoadUrls() {
		String[] urls = null;
		if (!TextUtils.isEmpty(mCurrentWord.pinyin)) {
			sounds = DbUtils.getInstance(this).getPyList(mCurrentWord.pinyin);
			urls =  new String[sounds.size()+2];
			if (sounds != null && sounds.size() > 0) {
				for (int i = 0; i < sounds.size(); i++) {
					urls[i] = AppDefine.URLDefine.URL_PINREADER + sounds.get(i)
							+ ".mp3";
				}
			}
		}else{
			urls =  new String[2];
		}
		
		String path = "http://www.yuwen100.cn/yuwen100/zy/hanzi-flash/"
				+ mCurrentWord.refid + ".mp4";
		String pathZxtuPath = "http://www.yuwen100.cn/yuwen100/zy/zyzd-clips/zxtu/"
				+ mCurrentWord.refid + ".jpg";
		
		
		urls[sounds.size()]=path;
		urls[sounds.size()+1]=pathZxtuPath;
		
		String pathZytu = "http://www.yuwen100.cn/yuwen100/zy/zyzd-clips/zytu/"
				+ mCurrentWord.refid + ".jpg";
		mImageLoader.getBitmapFactory().display(mOracleWord, pathZytu);
		return urls;
	}

	@Override
	public String getFolderPath() {
		return AppDefine.FilePathDefine.APP_DICTDITALNPATH + mCurrentWord.refid+"/";
	}
	
	private void actionSuccess(){
		loadImage() ;
		playVideo();
		dismissProgress();
		//playSound();
	}

	private void loadImage() {
		String fileAbs = mDownLoadControl.getAbsFilePath();
		//从本地读取图片
		mImageLoader.getBitmapFactory().display(mjfImg, fileAbs+mCurrentWord.refid + ".jpg");
	}
	//420588z  420588zjl
	private void playVideo(){
		//从本地读取视频
		String fileAbs = mDownLoadControl.getAbsFilePath();
		mVideoView.playVideo(fileAbs+mCurrentWord.refid + ".mp4");
	}

}
