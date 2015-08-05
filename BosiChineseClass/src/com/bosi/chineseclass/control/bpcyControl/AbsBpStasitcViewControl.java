package com.bosi.chineseclass.control.bpcyControl;

import android.content.Context;

import android.view.View;
import android.widget.Toast;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.components.BpStasticLayout;
import com.bosi.chineseclass.components.BpStasticLayout.OnBpStasticListener;
import com.bosi.chineseclass.control.OnDataChangedListener;
import com.bosi.chineseclass.db.BPCY;
import com.bosi.chineseclass.db.BpcyHistory;
import com.bosi.chineseclass.db.BphzHistory;
import com.bosi.chineseclass.han.util.PreferencesUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;

//用于对
public abstract class AbsBpStasitcViewControl implements OnBpStasticListener {

	protected BpStasticLayout mBpStasticLayout;

	protected BPCY mBphz = new BPCY();

	OnDataChangedListener mDataChangedListener;

	Context mContext;

	int dictStart = 1;
	int dictEnd = 500;

	public AbsBpStasitcViewControl(BpStasticLayout mBpStasticLayout,
			OnDataChangedListener mDataChangedListener) {
		this.mBpStasticLayout = mBpStasticLayout;
		this.mDataChangedListener = mDataChangedListener;
		mBpStasticLayout.setCallback(this);
		this.mContext = mBpStasticLayout.getBaseView().getContext();
		dictStart = PreferencesUtils.getInt(mContext,
				AppDefine.ZYDefine.EXTRA_DATA_BPCY_SATSTICSTART);
		dictEnd = PreferencesUtils.getInt(mContext,
				AppDefine.ZYDefine.EXTRA_DATA_BPCY_SATSTICEND);

		mBpStasticLayout.mTvNumber.setText(getNumberForStastic() + "");
		// mBpStasticLayout.mBtRember.setText(getRemberNum());
		// mBpStasticLayout.mBtUnRember.setText(getUnRemberNum());
		mDataChangedListener.chagePageData(getInitRefid());
	}

	public void asyLoadData() {
		mBpStasticLayout.mTvNumber.setText(getNumberForStastic());
		mBpStasticLayout.mBtRember.setText(getRemberNum());
		mBpStasticLayout.mBtUnRember.setText(getUnRemberNum());
		mDataChangedListener.chagePageData(getInitRefid());
	}

	public View getStasticView() {
		return mBpStasticLayout.getBaseView();
	}

	public abstract int getNumberForStastic();

	public int getRemberNum() {
		return 0;
	};

	public void showToastRemoteLearnOver() {
		Toast.makeText(
				mContext,
				mContext.getResources().getString(
						R.string.toast_remote_bphz_term_learnover),
				Toast.LENGTH_SHORT).show();
	}

	public int getUnRemberNum() {
		return 0;
	}

	public abstract int getInitRefid();

	protected void updateDb(int isResmber, int mCurrentID) {
		BpcyHistory mBpHistory = new BpcyHistory();
		mBpHistory.dictindex = mCurrentID;
		mBpHistory.isRember = isResmber;
		mBphz.insertOrUpdate(mBpHistory,
				WhereBuilder.b(BphzHistory.DICTINDEX, "=", mCurrentID));
	}


}
