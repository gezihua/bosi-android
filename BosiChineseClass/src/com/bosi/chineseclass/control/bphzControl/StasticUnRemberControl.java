package com.bosi.chineseclass.control.bphzControl;

import com.bosi.chineseclass.components.BpStasticLayout;


public class StasticUnRemberControl extends AbsBpStasitcViewControl{


	public StasticUnRemberControl(BpStasticLayout mBpStasticLayout,
			OnDataChangedListener mDataChangedListener) {
		super(mBpStasticLayout, mDataChangedListener);
	}

	@Override
	public void onRemberListener() {
		
	}

	@Override
	public void onUnRemberListener() {
		
	}

	@Override
	public void onNextListener() {
		
	}

	@Override
	public int getNumberForStastic() {
		return 0;
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
		// TODO Auto-generated method stub
		return 0;
	}

}
