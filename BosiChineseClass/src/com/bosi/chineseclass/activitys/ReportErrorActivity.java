package com.bosi.chineseclass.activitys;


import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

@ContentView(R.layout.activity_report_error)

public class ReportErrorActivity extends BaseActivity {
	
	public final  static String KEY_ERRORMESG = "KEY_ERROR";
	@ViewInject(R.id.tv_errordital)
	private TextView mTvErrorDital;
	String mErrorDital;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mErrorDital = getIntent().getStringExtra(KEY_ERRORMESG);
        mTvErrorDital.setText(mErrorDital);
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    
    @OnClick(R.id.bt_reporterror)
    public void actionReportError(View mView){
    	MobclickAgent.reportError(this, mErrorDital);
    	System.exit(0);
    }

}
