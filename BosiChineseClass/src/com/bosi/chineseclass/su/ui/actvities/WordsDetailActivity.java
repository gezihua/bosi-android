
package com.bosi.chineseclass.su.ui.actvities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
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
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.components.BpStasticLayout;
import com.bosi.chineseclass.components.MutilMediaPlayerTools;
import com.bosi.chineseclass.components.MutilMediaPlayerTools.MutilMediaPlayerListener;
import com.bosi.chineseclass.control.DownLoadResouceControl;
import com.bosi.chineseclass.control.DownLoadResouceControl.DownloadCallback;
import com.bosi.chineseclass.control.bphzControl.AbsBpStasitcViewControl.OnDataChangedListener;
import com.bosi.chineseclass.db.BPHZ;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.su.db.DbUtils;
import com.bosi.chineseclass.su.db.Word;
import com.bosi.chineseclass.su.ui.fragment.TextViewFragment;
import com.bosi.chineseclass.su.ui.view.WordPadView;
import com.bosi.chineseclass.su.utils.FileUtils;
import com.bosi.chineseclass.su.utils.MyVolley;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_container)
public class WordsDetailActivity extends BaseActivity implements
        OnClickListener, DownloadCallback, MutilMediaPlayerListener {
    View mHeadActionBar;
    private static final String TAG = WordsDetailActivity.class.getSimpleName();
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
    private final static String[] sExplain = {
            "基本释义", "完整释义", "成语典故"
    };
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

    private DownLoadResouceControl mDownLoadControl;
    private View mSoundContainer;

    private void init() {
        mDownLoadControl = new DownLoadResouceControl(this);
        mHeadActionBar = findViewById(R.id.deatail_headactionbar);
        initHeadActionBarComp();
        mSoundContainer = findViewById(R.id.sound_container);
        mSoundContainer.setOnClickListener(this);
        mOracleImg = (ImageView) findViewById(R.id.oracle_img);
        mOracleWord = (ImageView) findViewById(R.id.oracle_word);
        mWordTextView = (TextView) findViewById(R.id.detail_word);
        mPlayButton = (Button) findViewById(R.id.sound_play);
        mExplainTextView = (TextView) findViewById(R.id.word_explain);
        mWordDtail = (ViewPager) findViewById(R.id.word_detail_body);
        mYtTextView = (TextView) findViewById(R.id.ytzi);
        mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
        mIndicator.setVisibility(View.VISIBLE);
        mVideoView = (VideoView) findViewById(R.id.video_pad).findViewById(R.id.dictionary_video);
        mPadView = (Button) findViewById(R.id.word_pad);
        mPadView.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
        String word = onRecieveIntent();
        mWordTextView.setText(word);
        loadFromDb(word);
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
                "http://www.yuwen100.cn/yuwen100/zy/zyzd-clips/zxtu/" + wordDetail.refid + ".jpg",
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
                "http://www.yuwen100.cn/yuwen100/zy/zyzd-clips/zytu/" + wordDetail.refid + ".jpg",
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
        mVideoView.setMediaController(new MediaController(this));
        // TODO:设置正确的专家讲字源路径
        String path = "http://www.yuwen100.cn/yuwen100/zy/hanzi-flash/" + detail.refid + ".mp4";
        playVideo(path);
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
                FileUtils.mkdir(CACHES + "/sounds");
                mDownLoadControl.setOnDownLoadCallback(this);
                mDownLoadControl.downloadFiles(CACHES + "/sounds", createSoundsUrls());
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

    private int mCurrentPlayIndex = 0;

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

    private MutilMediaPlayerTools mMutilMediaPlayerTools;

    private void playSound() {
        if (mMutilMediaPlayerTools != null) {
            mMutilMediaPlayerTools.play();
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fragment_container);
        init();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
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

    private void playVideo(String path) {
        mVideoView.setVideoURI(Uri.parse(path));
        mVideoView.requestFocus();
        mVideoView.start();
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

    private void showExplain(Word detail) {
        final List<TextViewFragment> fragments = new ArrayList<TextViewFragment>();
        fragments.add(new TextViewFragment(detail.yanbian));
        fragments.add(new TextViewFragment(detail.cysy));
        fragments.add(new TextViewFragment(detail.cy));
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
                // TODO Auto-generated method stub
                return sExplain[position];
            }
        });
        mIndicator.setTabNames(sExplain);
        mIndicator.setViewPager(mWordDtail);

    }

    private void showWordPad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.word_pad, null);
        final WordPadView padView = (WordPadView) view.findViewById(R.id.wordpad);
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
            mBpStasitcLayout.setViewControl(tagFromBpLv, new OnDataChangedListener() {
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
                mMutilMediaPlayerTools = new MutilMediaPlayerTools(this, createSoudPaths());
                mMutilMediaPlayerTools.setMutilMediaPlayerListener(this);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
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

}
