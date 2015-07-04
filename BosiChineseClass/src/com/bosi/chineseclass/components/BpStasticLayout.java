package com.bosi.chineseclass.components;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.control.bphzControl.AbsBpStasitcViewControl;
import com.bosi.chineseclass.control.bphzControl.StasticRemberControl;
import com.bosi.chineseclass.control.bphzControl.AbsBpStasitcViewControl.OnDataChangedListener;
import com.bosi.chineseclass.control.bphzControl.StasticSampleControl;
import com.bosi.chineseclass.control.bphzControl.StasticUnRemberControl;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
//爆破统计相关组件 用于统计当前学习的信息 是否已经记住
public class BpStasticLayout {
	
	private View mViewBase;
	@ViewInject(R.id.tv_bp_statistic_totalnum)
	public TextView mTvNumber;
	@ViewInject(R.id.bt_bphz_statistic_renumber)
	public Button mBtRember;
	@ViewInject(R.id.bt_bphz_statistic_unrenumber)
	public Button mBtUnRember;
	@ViewInject(R.id.bt_bphz_statistic_next)
	private Button mBtNext;

	
	AbsBpStasitcViewControl mViewControl;
	
	public interface OnBpStasticListener {
		public void onRemberListener();

		public void onUnRemberListener();

		public void onNextListener();
	}
	@OnClick(R.id.bt_bphz_statistic_renumber)
	public void actionRember(View mView){
		mOnBpStasticListener.onRemberListener();
	}
	
	public void setViewControl(int tag,OnDataChangedListener mDataChangedListener){
		switch (tag) {
		case AppDefine.ZYDefine.BPHZ_TAG_REMBER:
			mViewControl = new StasticRemberControl(this, mDataChangedListener);
			break;
		case AppDefine.ZYDefine.BPHZ_TAG_UNREMBER:
			mViewControl = new StasticUnRemberControl(this, mDataChangedListener);
			break;
		case AppDefine.ZYDefine.BPHZ_TAG_NORMAL:
			mViewControl = new StasticSampleControl(this, mDataChangedListener);
			break;
		}
	}
	
	@OnClick(R.id.bt_bphz_statistic_unrenumber)
	public void actionUnRember(View mView){
		mOnBpStasticListener.onUnRemberListener();
		mBtNext.setVisibility(View.VISIBLE);
		mBtUnRember.setVisibility(View.GONE);
	}
	@OnClick(R.id.bt_bphz_statistic_next)
	public void actionNext(View mView){
		mOnBpStasticListener.onNextListener();
		mBtNext.setVisibility(View.GONE);
		mBtUnRember.setVisibility(View.VISIBLE);
	}
	Context mContext;
	
	OnBpStasticListener mOnBpStasticListener;

	public void setCallback(OnBpStasticListener mOnBpStasticListener) {
		this.mOnBpStasticListener = mOnBpStasticListener;
	}

	public BpStasticLayout(Context mContext) {
		this.mContext = mContext;
		
	    mViewBase = makeView();
		
		ViewUtils.inject(this,mViewBase);
	}
	
	public View getBaseView(){
		return mViewBase;
	}

	public View makeView() {
		return View.inflate(mContext, R.layout.layout_bp_statistics, null);
	}

}
