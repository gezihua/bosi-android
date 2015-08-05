package com.bosi.chineseclass.activitys;

import java.util.List;

import android.os.Bundle;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseActivity;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.bean.BpcyDataBean;
import com.bosi.chineseclass.components.BpStasticLayout;
import com.bosi.chineseclass.control.DownLoadResouceControl.DownLoadInterface;
import com.bosi.chineseclass.control.OnDataChangedListener;
import com.bosi.chineseclass.db.BPCY;
import com.bosi.chineseclass.db.dict.BPCYDATA;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.utils.DesUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.layout_bpcydital)
public class BpcyDitalActivity extends BaseActivity implements
DownLoadInterface{
	@ViewInject(R.id.headactionbar)
	View mViewHead;
	HeadLayoutComponents mHeadActionBar;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setUpBpCyControl();
		
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

	BpcyDataBean mBpcyBean  ;

	private void updateUI(String id) {
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
		}else{
			showToastLong("该组成语未开放");
			finish();
		}
	}
	
	@ViewInject(R.id.tv_cyname)
	TextView mTvName;
	@ViewInject(R.id.tv_cypinyin)
	TextView mTvCyPinyin;
	@ViewInject(R.id.iv_hint_bphz_learn)
    ImageView mSampleHintView;
	private void setUpNameAndPinyin() throws Exception{
		mTvName.setText(DesUtils.decode(mBpcyBean.CYCimu));
		mTvCyPinyin.setText(DesUtils.decode(mBpcyBean.CYFayin));
	}
	
	
	@Override
	public String[] getDownLoadUrls() {
		return null;
	}

	@Override
	public void onDownLoadCallback(int mCurrentSize, int wholeSize) {
		
	}

	@Override
	public String getFolderPath() {
		 return AppDefine.FilePathDefine.APP_CYDITALNPATH + mBpcyBean.Cybh+"/";
	}
}
