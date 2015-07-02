package com.bosi.chineseclass.control.bphzControl;

import com.bosi.chineseclass.R;
import com.bosi.chineseclass.components.BpStasticLayout;


public class StasticSampleControl extends AbsBpStasitcViewControl{


	public StasticSampleControl(BpStasticLayout mBpStasticLayout,
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
