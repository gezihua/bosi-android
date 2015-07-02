package com.bosi.chineseclass.su.ui.actvities;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.components.BpStasticLayout;
import com.bosi.chineseclass.components.BpStasticLayout.OnBpStasticListener;
import com.bosi.chineseclass.control.bphzControl.AbsBpStasitcViewControl.OnDataChangedListener;
import com.bosi.chineseclass.db.BPHZ;
import com.bosi.chineseclass.db.BphzHistory;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.su.db.DbUtils;
import com.bosi.chineseclass.su.db.Word;
import com.bosi.chineseclass.su.ui.fragment.TextViewFragment;
import com.bosi.chineseclass.su.ui.view.WordPadView;
import com.bosi.chineseclass.su.utils.MyVolley;
import com.daimajia.easing.linear.Linear;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

@ContentView(R.layout.fragment_container)
public class WordsDetailActivity extends BaseActivity implements
		OnClickListener {
	View mHeadActionBar;
	HeadLayoutComponents mHeadActionBarComp;
	private ImageView mOracleImg = null;
	private ImageView mOracleWord = null;

	private TextView mWordTextView = null;
	private Button mPlayButton = null;
	private TextView mExplainTextView;
	private ViewPager mWordDtail;
	private TextView mYtTextView;
	private TabPageIndicator mIndicator;
	private FragmentManager mFragManager;
	private VideoView mVideoView;
	private Button mPadView;
	private final static String[] sExplain = { "基本示意", "完整示意", "成语典故" };
	private final static String ORACLE_IMG = "";

	private final static String ORACLE_WORD = "";

	private <T> void asynload(String url, RequestCallBack<T> callBack) {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, url, callBack);
	}

	private void createTabBtn(String word) {

	}

	private TextViewFragment creaTextViewFragment(String word) {
		return new TextViewFragment(word);
	}
	
	private void init() {
		mHeadActionBar = findViewById(R.id.deatail_headactionbar);
		initHeadActionBarComp();
		mOracleImg = (ImageView) findViewById(R.id.oracle_img);
		mOracleWord = (ImageView) findViewById(R.id.oracle_word);
		mWordTextView = (TextView) findViewById(R.id.detail_word);
		mPlayButton = (Button) findViewById(R.id.sound_play);
		mExplainTextView = (TextView) findViewById(R.id.word_explain);
		mWordDtail = (ViewPager) findViewById(R.id.word_detail_body);
		mYtTextView = (TextView) findViewById(R.id.ytzi);
		mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
		mIndicator.setVisibility(View.VISIBLE);
		mVideoView = (VideoView) findViewById(R.id.video_pad).findViewById(
				R.id.dictionary_video);
		mPadView = (Button) findViewById(R.id.word_pad);
		mPadView.setOnClickListener(this);
		
		setUpBpWordsControl();
		
		
	}

	private void initHeadActionBarComp() {
		mHeadActionBarComp = new HeadLayoutComponents(this, mHeadActionBar);

		mHeadActionBarComp.setTextMiddle("字源字典", -1);
		mHeadActionBarComp.setDefaultLeftCallBack(true);
		mHeadActionBarComp.setDefaultRightCallBack(true);
	}

	private void loadFromDb(String word) {
		Word detail = DbUtils.getInstance(this).getExplain(word,"");
		showDetail(detail);
		showExplain(detail);
	}

	private void loadFromRemote() {
		// 这里需要请求四中四种资源
		// 1. 声音 2.动画 3. 字体图片 4 示意图片
		loadImage();
		loadVideoAndSound();
	}

	private void loadImage() {
		MyVolley.getInstance(this)
				.loadImage(
						"http://www.uimaker.com/uploads/allimg/120801/1_120801005405_1.png",
						new ImageLoader.ImageListener() {
							@Override
							public void onErrorResponse(VolleyError volleyError) {

							}

							@Override
							public void onResponse(
									ImageLoader.ImageContainer imageContainer,
									boolean b) {
								if (imageContainer.getBitmap() != null) {
									mOracleImg.setImageBitmap(imageContainer
											.getBitmap());
								}
							}
						});
		MyVolley.getInstance(this)
				.loadImage(
						"http://www.uimaker.com/uploads/allimg/120801/1_120801005405_1.png",
						new ImageLoader.ImageListener() {
							@Override
							public void onErrorResponse(VolleyError volleyError) {

							}

							@Override
							public void onResponse(
									ImageLoader.ImageContainer imageContainer,
									boolean b) {
								if (imageContainer.getBitmap() != null) {
									mOracleWord.setImageBitmap(imageContainer
											.getBitmap());
								}
							}
						});
	}

	private void loadVideoAndSound() {
		mVideoView.setMediaController(new MediaController(this));
		// TODO:设置正确的专家讲字源路径
		String path = "http://www.yuwen100.cn/yuwen100/zy/hanzi-flash/120001.mp4";
		playVideo(path);
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case R.id.word_pad: {
			showWordPad();
			break;
		}

		default:
			break;
		}

	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		init();

	}

	private String onRecieveIntent() {
		Intent intent = getIntent();
		if (intent != null) {
			String word = intent.getExtras().getString("word", "");
			return word;
		}
		return null;
	}

	private void playVideo(String path) {
		mVideoView.setVideoURI(Uri.parse(path));
		mVideoView.requestFocus();
		mVideoView.start();
	}

	private void showDetail(Word detail) {
		String temp = null;
		if (!TextUtils.isEmpty(detail.pinyin)) {
			temp = "(" + detail.pinyin + ")";
		}
		if (!TextUtils.isEmpty(detail.ytzi)) {
			temp += detail.ytzi;
		}
		mYtTextView.setText(temp);
		if (!TextUtils.isEmpty(detail.shiyi)) {
			mExplainTextView.setText(detail.shiyi);
		}

	}

	private void showExplain(Word detail) {
		final List<TextViewFragment> fragments = new ArrayList<TextViewFragment>();
		fragments.add(new TextViewFragment(detail.yanbian));
		fragments.add(new TextViewFragment(detail.cy));
		fragments.add(new TextViewFragment(detail.cysy));
		mWordDtail.setAdapter(new FragmentPagerAdapter(
				getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return 3;
			}

			@Override
			public Fragment getItem(int arg0) {
				return fragments.get(arg0);
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return sExplain[position];
			}
		});
		mIndicator.setTabNames(sExplain);
		mIndicator.setViewPager(mWordDtail);

	}

	private void showWordPad() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = View.inflate(this, R.layout.word_pad, null);
		final WordPadView padView = (WordPadView) view
				.findViewById(R.id.wordpad);
		Button reset = (Button) view.findViewById(R.id.rest);
		reset.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				padView.rest();
			}
		});
		AlertDialog dialog = builder.setView(view).create();
		dialog.show();
	}
	
	//-------------------------------------------   添加和统计布局的相关内容     --------------------------------------------------------
	
	BpStasticLayout mBpStasitcLayout;
	
	@ViewInject(R.id.ll_hzdital_stastic)
	LinearLayout mLayoutStastic;
	
	public static final String  EXTRA_NAME_WORDS_TAG = "tag";
	
	BPHZ mBphz = new BPHZ();
	
	private void setUpBpWordsControl(){
		final int TAG= getIntent().getIntExtra(EXTRA_NAME_WORDS_TAG, -1);
		if(TAG!=-1){
			int tagFromBpLv = getIntent().getIntExtra(EXTRA_NAME_WORDS_TAG,AppDefine.ZYDefine.BPHZ_TAG_NORMAL);
			mBpStasitcLayout = new BpStasticLayout(mContext);
			mBpStasitcLayout.setViewControl(tagFromBpLv, new OnDataChangedListener() {
				@Override
				public void chagePageData(int refid) {
					updateUI(refid+"","");
				}

				@Override
				public void chagePageData() {
					
				}
			});
			mLayoutStastic.addView(mBpStasitcLayout.getBaseView());
			mLayoutStastic.setVisibility(View.VISIBLE);
		}else{
			String word = onRecieveIntent();
			mWordTextView.setText(word);
			loadFromRemote();
			loadFromDb(word);
		}
	}
	
	private void updateUI(String id,String word){
		Word detail = DbUtils.getInstance(this).getExplain("",id);
		showDetail(detail);
		showExplain(detail);
	}

}
