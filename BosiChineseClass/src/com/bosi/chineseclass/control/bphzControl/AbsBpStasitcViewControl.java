package com.bosi.chineseclass.control.bphzControl;

import android.content.Context;
import android.view.View;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.components.BpStasticLayout;
import com.bosi.chineseclass.components.BpStasticLayout.OnBpStasticListener;
import com.bosi.chineseclass.db.BPHZ;
import com.bosi.chineseclass.db.BphzHistory;
import com.bosi.chineseclass.han.util.PreferencesUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;


//用于对
public abstract class AbsBpStasitcViewControl implements OnBpStasticListener {

	protected BpStasticLayout mBpStasticLayout;
	
	protected BPHZ mBphz = new BPHZ();
	
	OnDataChangedListener mDataChangedListener;
	
	Context mContext;
	
	int dictStart = 1;
	int dictEnd  = 50;
	
	public AbsBpStasitcViewControl(BpStasticLayout mBpStasticLayout,OnDataChangedListener mDataChangedListener){
		this.mBpStasticLayout = mBpStasticLayout;
		this.mDataChangedListener = mDataChangedListener;
		mBpStasticLayout.setCallback(this);
		this.mContext = mBpStasticLayout.getBaseView().getContext();
		dictStart = PreferencesUtils.getInt(mContext,AppDefine.ZYDefine.EXTRA_DATA_BPHZ_SATSTICSTART);
		dictEnd = PreferencesUtils.getInt(mContext,  AppDefine.ZYDefine.EXTRA_DATA_BPHZ_SATSTICEND); 
		
     	mBpStasticLayout.mTvNumber.setText(getNumberForStastic()+"");
//		mBpStasticLayout.mBtRember.setText(getRemberNum());
//		mBpStasticLayout.mBtUnRember.setText(getUnRemberNum());
		mDataChangedListener.chagePageData(getInitRefid());
	}
	
	public void asyLoadData(){
		mBpStasticLayout.mTvNumber.setText(getNumberForStastic());
		mBpStasticLayout.mBtRember.setText(getRemberNum());
		mBpStasticLayout.mBtUnRember.setText(getUnRemberNum());
		mDataChangedListener.chagePageData(getInitRefid());
	}
	
	public View getStasticView(){
		return mBpStasticLayout.getBaseView();
	}
	
	public abstract int getNumberForStastic();
	
	public  int getRemberNum()
	{
		return 0;
	}
	;
	
	public  int getUnRemberNum(){
		return 0;
	}
	
	public abstract int getInitRefid();
	
	protected void updateDb(int isResmber,String mCurrentID){
		BphzHistory mBpHistory = new BphzHistory();
		mBpHistory.dictindex = mCurrentID;
		mBpHistory.isRember = isResmber;
		mBphz.insertOrUpdate(mBpHistory, WhereBuilder.b(BphzHistory.DICTINDEX, "=", mCurrentID));
	}
	
	public interface OnDataChangedListener{
		public void chagePageData(int refid);
		public void chagePageData();//如果没有id的话说明还是用当前的id 只是需要将学习的部分 开始介绍一下
	}
	
}
