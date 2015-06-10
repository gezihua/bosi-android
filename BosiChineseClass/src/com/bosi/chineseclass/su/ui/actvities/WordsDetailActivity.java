
package com.bosi.chineseclass.su.ui.actvities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.su.db.DbUtils;
import com.bosi.chineseclass.su.db.Word;
import com.bosi.chineseclass.su.ui.fragment.TextViewFragment;
import com.bosi.chineseclass.su.utils.MyVolley;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class WordsDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fragment_container);
        init();
    }

    private ImageView mOracleImg = null;
    private ImageView mOracleWord = null;
    private TextView mWordTextView = null;
    private Button mPlayButton = null;
    private TextView mExplainTextView;
    private ViewPager mWordDtail;
    private TabWidget mWordDetailTab;
    private TextView mYtTextView;

    private void init() {
        mOracleImg = (ImageView) findViewById(R.id.oracle_img);
        mOracleWord = (ImageView) findViewById(R.id.oracle_word);
        mWordTextView = (TextView) findViewById(R.id.detail_word);
        mPlayButton = (Button) findViewById(R.id.sound_play);
        mExplainTextView = (TextView) findViewById(R.id.word_explain);
        mWordDtail = (ViewPager) findViewById(R.id.word_detail_body);
        mWordDetailTab = (TabWidget) findViewById(R.id.word_detail_tab);
        mYtTextView = (TextView) findViewById(R.id.ytzi);
        String word = onRecieveIntent();
        mWordTextView.setText(word);
        loadFromRemote();
        loadFromDb(word);
    }

    private void loadFromDb(String word) {
        Word detail = DbUtils.getInstance(this).getExplain(word);
        showDetail(detail);
        showExplain(detail);
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

    private final static String[] sExplain = {
            "基本示意", "完整示意", "成语典故"
    };

    private void showExplain(Word detail) {
        final List<TextViewFragment> fragments = new ArrayList<TextViewFragment>();
        fragments.add(new TextViewFragment(detail.yanbian));
        fragments.add(new TextViewFragment(detail.cy));
        fragments.add(new TextViewFragment(detail.cysy));
        mWordDtail.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

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

    }

    private void createTabBtn(String word) {

    }

    private TextViewFragment creaTextViewFragment(String word) {
        return new TextViewFragment(word);
    }

    private void loadFromRemote() {
        // 这里需要请求四中四种资源
        // 1. 声音 2.动画 3. 字体图片 4 示意图片
        MyVolley.getInstance(this)
                .loadImage(
                        "http://developer.android.com/images/training/system-ui.png",
                        new ImageLoader.ImageListener() {
                            @Override
                            public void onResponse(ImageLoader.ImageContainer imageContainer,
                                    boolean b) {
                                if (imageContainer.getBitmap() != null) {
                                    mOracleImg.setImageBitmap(imageContainer.getBitmap());
                                }
                            }

                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        });
        MyVolley.getInstance(this)
                .loadImage(
                        "http://developer.android.com/images/training/system-ui.png",
                        new ImageLoader.ImageListener() {
                            @Override
                            public void onResponse(ImageLoader.ImageContainer imageContainer,
                                    boolean b) {
                                if (imageContainer.getBitmap() != null) {
                                    mOracleWord.setImageBitmap(imageContainer.getBitmap());
                                }
                            }

                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        });
    }

    private final static String ORACLE_IMG = "";
    private final static String ORACLE_WORD = "";

    private String onRecieveIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String word = intent.getExtras().getString("word", "");
            return word;
        }
        return null;
    }

    private <T> void asynload(String url, RequestCallBack<T> callBack) {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, url, callBack);
    }

}
