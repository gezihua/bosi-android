package com.bosi.chineseclass.control;

import android.content.Context;
import android.view.View;

import com.bosi.chineseclass.components.BpStasticLayout;
import com.bosi.chineseclass.components.BpStasticLayout.OnBpStasticListener;

public abstract class AbsBpStasitcViewControl implements OnBpStasticListener {

	protected BpStasticLayout mBpStasticLayout;
	
	public AbsBpStasitcViewControl(Context mContext){
		mBpStasticLayout = new BpStasticLayout(mContext);
		mBpStasticLayout.setCallback(this);
	}
	
	public View getStasticView(){
		return mBpStasticLayout.getBaseView();
	}
	
	public abstract int getNumberForStastic();
	
	public abstract int getRemberNum();
	
	public abstract int getUnRemberNum(); 
	
}
