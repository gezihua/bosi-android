package com.bosi.chineseclass.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.components.BpStasticLayout;
import com.bosi.chineseclass.control.bphzControl.AbsBpStasitcViewControl.OnDataChangedListener;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.layout_bpcydital)
public class BpcyDitalActivity extends BaseActivity{
	@ViewInject(R.id.headactionbar)
	View mViewHead;
	HeadLayoutComponents mHeadActionBar;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setUpBpWordsControl();
		
		mHeadActionBar = new HeadLayoutComponents(this,mViewHead);
		mHeadActionBar.setTextMiddle("爆破成语", -1);
		mHeadActionBar.setDefaultLeftCallBack(true);
		mHeadActionBar.setDefaultRightCallBack(true);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	BpStasticLayout mBpStasitcLayout;
	@ViewInject(R.id.ll_bpcy_stastic)
	LinearLayout mLayoutStastic;

	public static final String EXTRA_NAME_WORDS_TAG = "tag";
	
	private void setUpBpWordsControl() {
		updateProgress(0, 1);
		final int TAG = getIntent().getIntExtra(EXTRA_NAME_WORDS_TAG, -1);
		if (TAG != -1) {
			int tagFromBpLv = getIntent().getIntExtra(EXTRA_NAME_WORDS_TAG,
					AppDefine.ZYDefine.BPHZ_TAG_NORMAL);
			mBpStasitcLayout = new BpStasticLayout(mContext);
			mBpStasitcLayout.setViewControl(tagFromBpLv,
					new OnDataChangedListener() {
						@Override
						public void chagePageData(int refid) {
							//updateUI(refid + "", "");

						}

						@Override
						public void chagePageData() {
						}

						@Override
						public void onSampleLoadBefore() {
							
						}
					});

			mLayoutStastic.addView(mBpStasitcLayout.getBaseView());
			mLayoutStastic.setVisibility(View.VISIBLE);

		} else {
			mLayoutStastic.setVisibility(View.INVISIBLE);
		}
		
	}
}
