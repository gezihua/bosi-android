package com.bosi.chineseclass.control.bphzControl;

import com.bosi.chineseclass.components.BpStasticLayout;


public class StasticSampleControl extends AbsBpStasitcViewControl{

	int mCurrentDisplayWordId;
	public StasticSampleControl(BpStasticLayout mBpStasticLayout,
			OnDataChangedListener mDataChangedListener) {
		super(mBpStasticLayout, mDataChangedListener);
		mCurrentDisplayWordId = dictStart;
	}

	@Override
	public void onRemberListener() {
		// 显示当前本组还有多少未学习
		if(mCurrentDisplayWordId<=dictEnd)
		updateDb(1, mCurrentDisplayWordId);
		if(isLernOver()){
			showToastRemoteLearnOver();
			return;
		}
		mBpStasticLayout.mTvNumber.setText(dictEnd-mCurrentDisplayWordId+"");
		mDataChangedListener.chagePageData(mCurrentDisplayWordId);
	}
	

	boolean isLernOver(){
		mCurrentDisplayWordId++;
		if(mCurrentDisplayWordId>dictEnd){
			return true;
		}
		return false;
	}
	@Override
	public void onUnRemberListener() {
		mDataChangedListener.chagePageData();
		// 显示
		if(mCurrentDisplayWordId<=dictEnd)
		updateDb(0, mCurrentDisplayWordId);
	}

	@Override
	public void onNextListener() {
		if(isLernOver()){
			showToastRemoteLearnOver();
			return;
		}
		mBpStasticLayout.mTvNumber.setText(dictEnd-mCurrentDisplayWordId+"");
		mDataChangedListener.chagePageData(mCurrentDisplayWordId);
	}

	@Override
	public int getNumberForStastic() {
		return dictEnd-dictStart;
	}

	@Override
	public int getRemberNum() {
		return 0;
	}

	@Override
	public int getUnRemberNum() {
		return 0;
	}

	@Override
	public int getInitRefid() {
		return dictStart;
	}

}
