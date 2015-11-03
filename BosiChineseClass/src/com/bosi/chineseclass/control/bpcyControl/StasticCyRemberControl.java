package com.bosi.chineseclass.control.bpcyControl;

import java.util.List;

import com.bosi.chineseclass.components.BpStasticLayout;
import com.bosi.chineseclass.control.OnDataChangedListener;

public class StasticCyRemberControl extends AbsBpStasitcViewControl{
	List<Integer> listData = null;
    int mCurrent = 0;
	public StasticCyRemberControl(BpStasticLayout mBpStasticLayout,
			OnDataChangedListener mDataChangedListener) {
		super(mBpStasticLayout, mDataChangedListener);
	}

	@Override
	public void onRemberListener() {
		if(mCurrent<getListData().size())
		updateDb(1, listData.get(mCurrent));
		if(isReachBottom()){
			showToastRemoteLearnOver();
			return;
		}
		mBpStasticLayout.mTvNumber.setText(getNumberForStastic()-mCurrent+"");
		mDataChangedListener.chagePageData(listData.get(mCurrent));
	}

	
	public List<Integer>  getListData(){
		if(listData ==null){
			listData = mBphz.selectDictListBaseTag(mContext, 1, dictStart, dictEnd);
		}
		return listData;
	}
	@Override
	public void onUnRemberListener() {
		if(mCurrent<getListData().size())
			updateDb(0, getListData().get(mCurrent));
		
		mDataChangedListener.chagePageData();
	}

	@Override
	public void onNextListener() {
		if(isReachBottom()){
			showToastRemoteLearnOver();
			return;
		}
		mBpStasticLayout.mTvNumber.setText(getNumberForStastic()-mCurrent+"");
		mDataChangedListener.chagePageData(getListData().get(mCurrent));
	}
	
	private boolean isReachBottom(){
		mCurrent++;
		if(mCurrent > getNumberForStastic()){
			return true;
		}
		return false;
	}

	@Override
	public int getNumberForStastic() {
		/*select_dictindexgroup*/
		listData = getListData();
		
		return listData.size()-1;
	}

	@Override
	public int getInitRefid() {
		int mCurrentId  = 220001;
		try{
			mCurrentId = listData.get(mCurrent);
		}catch(ArrayIndexOutOfBoundsException e){
			//异常的话自动归类
		}
		
		return mCurrentId;
	}

}
