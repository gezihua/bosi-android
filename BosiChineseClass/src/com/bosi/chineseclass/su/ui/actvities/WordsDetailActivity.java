package com.bosi.chineseclass.su.ui.actvities;


import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.components.BpStasticLayout;
import com.bosi.chineseclass.components.MutilMediaPlayerTools;
import com.bosi.chineseclass.components.MutilMediaPlayerTools.MutilMediaPlayerListener;
import com.bosi.chineseclass.components.WordDitalExpainComponent;
import com.bosi.chineseclass.control.DownLoadResouceControl;
import com.bosi.chineseclass.control.DownLoadResouceControl.DownLoadInterface;
import com.bosi.chineseclass.control.bphzControl.AbsBpStasitcViewControl.OnDataChangedListener;
import com.bosi.chineseclass.db.BPHZ;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.su.db.DbUtils;
import com.bosi.chineseclass.su.db.Word;
import com.bosi.chineseclass.su.utils.MyVolley;
import com.bosi.chineseclass.views.BsVideoViewGroup;
import com.bosi.chineseclass.views.PaintPadWindow;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_container)
public class WordsDetailActivity extends BaseActivity implements
		OnClickListener, DownLoadInterface, MutilMediaPlayerListener {
	View mHeadActionBar;
	private static final String TAG = WordsDetailActivity.class.getSimpleName();
	HeadLayoutComponents mHeadActionBarComp;
	
	@ViewInject(R.id.oracle_img)
	 ImageView mOracleImg ;
	
	@ViewInject(R.id.oracle_word)
	 ImageView mOracleWord;
	
	@ViewInject(R.id.detail_word)
	 TextView mWordTextView ;
	
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
	
	
	private DownLoadResouceControl mDownLoadControl;
	

	private void init() {
		mDownLoadControl = new DownLoadResouceControl(this);
		mDownLoadControl.setModelResourceAbs(false);
		mDownLoadControl.setOnDownLoadCallback(this);
		
		mHeadActionBar = findViewById(R.id.deatail_headactionbar);
		initHeadActionBarComp();
		mSoundContainer.setOnClickListener(this);
		
		
		mPadView = (ImageButton) findViewById(R.id.word_pad);
		mPadView.setOnClickListener(this);
		
		mExpainComponent = new WordDitalExpainComponent(this,mllExpainBody);
		mPaintPadWindow = new PaintPadWindow(mContext);
		
		setUpBpWordsControl();
	}


	private void initHeadActionBarComp() {
		mHeadActionBarComp = new HeadLayoutComponents(this, mHeadActionBar);
		mHeadActionBarComp.setTextMiddle("字源字典", -1);
		mHeadActionBarComp.setDefaultLeftCallBack(true);
		mHeadActionBarComp.setDefaultRightCallBack(true);
	}

	private void loadFromDb(String word) {
		Word detail = DbUtils.getInstance(this).getExplain(word, "");
		showDetail(detail);
		showExplain(detail);
		loadSound(detail);
		loadFromRemote(detail);
	}

	private void loadFromRemote(Word detail) {
		// 这里需要请求四中四种资源
		// 1. 声音 2.动画 3. 字体图片 4 示意图片
		loadImage(detail);
		loadVideoAndSound(detail);
	}

	private void loadImage(Word wordDetail) {
		MyVolley.getInstance(this).loadImage(
				"http://www.yuwen100.cn/yuwen100/zy/zyzd-clips/zxtu/"
						+ wordDetail.refid + ".jpg",
				new ImageLoader.ImageListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {

					}

					@Override
					public void onResponse(
							ImageLoader.ImageContainer imageContainer, boolean b) {
						if (imageContainer.getBitmap() != null) {
							mOracleImg.setImageBitmap(imageContainer
									.getBitmap());
						}
					}
				});
		MyVolley.getInstance(this).loadImage(
				"http://www.yuwen100.cn/yuwen100/zy/zyzd-clips/zytu/"
						+ wordDetail.refid + ".jpg",
				new ImageLoader.ImageListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {

					}

					@Override
					public void onResponse(
							ImageLoader.ImageContainer imageContainer, boolean b) {
						if (imageContainer.getBitmap() != null) {
							mOracleWord.setImageBitmap(imageContainer
									.getBitmap());
						}
					}
				});

	}

	private void loadVideoAndSound(Word detail) {
		String path = "http://www.yuwen100.cn/yuwen100/zy/hanzi-flash/"
				+ detail.refid + ".mp4";
		mVideoView.playVideo(path);
	}

	private final static String CACHES = "/data/data/"
			+ BSApplication.getInstance().getPackageName() + "/caches";
	private final static String SOUNDURL = "http://www.yuwen100.cn/yuwen100/zy/zyzd-clips/pinyinread/";

	// 记录在声音的队列中
	private List<String> sounds = new ArrayList<String>();

	private void loadSound(Word detail) {
		if (!TextUtils.isEmpty(detail.pinyin)) {
			sounds = DbUtils.getInstance(this).getPyList(detail.pinyin);
			if (sounds != null && sounds.size() > 0) {
//				FileUtils.mkdir(CACHES + "/sounds");
//				mDownLoadControl.setOnDownLoadCallback(this);
//				mDownLoadControl.downloadFiles(CACHES + "/sounds",
//						createSoundsUrls());
			}
		}
	}

	private String[] createSoundsUrls() {
		String[] strings = new String[sounds.size()];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = SOUNDURL + sounds.get(i) + ".mp3";
		}
		return strings;
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case R.id.word_pad: {
			showWordPad();
			break;
		}
		case R.id.sound_container: {
			playSound();
			break;
		}

		default:
			break;
		}

	}
	
	public void onBackPressed() {
		mPaintPadWindow.dismissView();
	};

	PaintPadWindow mPaintPadWindow;
	private void showWordPad(){
		mPaintPadWindow.createFloatView();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPaintPadWindow.dismissView();
	}
	private MutilMediaPlayerTools mMutilMediaPlayerTools;

	private void playSound() {
		if (mMutilMediaPlayerTools != null) {
			mMutilMediaPlayerTools.play();
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		init();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mMutilMediaPlayerTools != null) {
			mMutilMediaPlayerTools.onDestory();
		}
	}

	private String onRecieveIntent() {
		Intent intent = getIntent();
		if (intent != null) {
			String word = intent.getExtras().getString("word", "");
			return word;
		}
		return null;
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
			mExplainTextView.setText(detail.shiyi);
		}

	}
	WordDitalExpainComponent mExpainComponent;
	private void showExplain(Word detail) {
		mExpainComponent.setData(new String[]{detail.yanbian,detail.cysy,detail.cy});
	}

	// ------------------------------------------- 添加和统计布局的相关内容
	// --------------------------------------------------------

	BpStasticLayout mBpStasitcLayout;

	@ViewInject(R.id.ll_hzdital_stastic)
	LinearLayout mLayoutStastic;

	public static final String EXTRA_NAME_WORDS_TAG = "tag";

	BPHZ mBphz = new BPHZ();

	private void setUpBpWordsControl() {
		final int TAG = getIntent().getIntExtra(EXTRA_NAME_WORDS_TAG, -1);
		if (TAG != -1) {
			int tagFromBpLv = getIntent().getIntExtra(EXTRA_NAME_WORDS_TAG,
					AppDefine.ZYDefine.BPHZ_TAG_NORMAL);
			mBpStasitcLayout = new BpStasticLayout(mContext);
			mBpStasitcLayout.setViewControl(tagFromBpLv,
					new OnDataChangedListener() {
						@Override
						public void chagePageData(int refid) {
							updateUI(refid + "", "");
						}

						@Override
						public void chagePageData() {
						}
					});
			
			mLayoutStastic.addView(mBpStasitcLayout.getBaseView());
			mLayoutStastic.setVisibility(View.VISIBLE);
		} else {
			String word = onRecieveIntent();
			mWordTextView.setText(word);
			loadFromDb(word);
			mLayoutStastic.setVisibility(View.GONE);
		}
	}

	private void updateUI(String id, String word) {
		Word detail = DbUtils.getInstance(this).getExplain("", id);
		showDetail(detail);
		showExplain(detail);
	}

	// ------------------------------------------------------------下载播放生意-----------------------------------------------
	@Override
	public void onDownLoadCallback(int mCurrentSize, int wholeSize) {
		try {
			if (mCurrentSize == wholeSize) {
				mMutilMediaPlayerTools = new MutilMediaPlayerTools(this,
						createSoudPaths());
				mMutilMediaPlayerTools.setMutilMediaPlayerListener(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String[] createSoudPaths() {
		String[] strings = new String[sounds.size()];
		int i = 0;
		for (String temp : sounds) {
			strings[i] = CACHES + "/sounds/" + temp + ".mp3";
			i++;
		}
		return strings;
	}

	@Override
	public void finished() {
		if (mMutilMediaPlayerTools != null) {
			mMutilMediaPlayerTools.reset();
			Log.e(TAG, "reset");
		}
	}


	@Override
	public String[] getDownLoadUrls() {
		return null;
	}


	@Override
	public String getFolderPath() {
		return null;
	}
}
