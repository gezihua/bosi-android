
package com.bosi.chineseclass.activitys;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.activitys.ZiYuanActivity;
import com.bosi.chineseclass.han.db.DbManager;
import com.bosi.chineseclass.su.db.DicOpenHelper;
import com.bosi.chineseclass.su.ui.actvities.DictionaryAcitvity;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

@ContentView(R.layout.main)
public class MainActivity extends BaseActivity {
	
	FeedbackAgent agent;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        UmengUpdateAgent.update(this);
        agent = new FeedbackAgent(this); 
        //agent.sync();
        init();
    }
   //测试友盟反馈
    @OnClick(R.id.btn_feedback)
    public void actionFeedBack(View mView){
    	agent.startFeedbackActivity();
    }

    private void init() {
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 DicOpenHelper helper = new DicOpenHelper(getBaseContext());
                 SQLiteDatabase database = helper.getReadableDatabase();
                 Intent intent = new Intent();
                 intent.setClass(MainActivity.this, DictionaryAcitvity.class);
                 startActivity(intent);

//                Intent mIntent = new Intent(mContext, SampleHolderActivity.class);
//                mIntent.putExtra(SampleHolderControlMake.mControlName, PinYinLearnControl.class);
//                startActivity(mIntent);
            }
        });

        Button bt_jbzy = (Button) findViewById(R.id.bt_zy);
        bt_jbzy.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                DicOpenHelper helper = new DicOpenHelper(getBaseContext());
                SQLiteDatabase database = helper.getReadableDatabase();

                BSApplication.getInstance().mDbManager = new DbManager(mContext);

                Intent ziyuanIntent = new Intent(mContext, ZiYuanActivity.class);
                startActivity(ziyuanIntent);
            }
        });
    }
}
