
package com.bosi.chineseclass.su.ui.actvities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.su.db.DbUtils;
import com.bosi.chineseclass.su.db.Word;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

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

    private void init() {
        mOracleImg = (ImageView) findViewById(R.id.oracle_img);
        mOracleWord = (ImageView) findViewById(R.id.oracle_word);
        mWordTextView = (TextView) findViewById(R.id.detail_word);
        mPlayButton = (Button) findViewById(R.id.sound_play);
        mExplainTextView = (TextView) findViewById(R.id.word_explain);
        mWordDtail = (ViewPager) findViewById(R.id.word_detail_body);
        
        String word = onRecieveIntent();
        mWordTextView.setText(word);
        load();
    }
    private void loadFromDb(String word){
        Word detail=DbUtils.getInstance(this).getExplain(word);
        
    }

    private void load() {
        // 这里需要请求四中四种资源
        // 1. 声音 2.动画 3. 字体图片 4 示意图片
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
