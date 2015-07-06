package com.bosi.chineseclass.components;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.components.BaseComponents;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class WordDitalExpainComponent extends BaseComponents {

	@ViewInject(R.id.bt_worddital_wzsy)
	TextView mViewWzsy;
	@ViewInject(R.id.bt_worddital_cydg)
	TextView mViewCydg;
	@ViewInject(R.id.bt_worddital_jbsy)
	TextView mViewJbsy;
	@ViewInject(R.id.tv_worddital_expain)
	TextView mTvDital;
	
	public String [] mData;
	public WordDitalExpainComponent(Context mContext,View mView) {
		super(mContext,mView);
	}
	

	@Override
	public void initFatherView() {
		ViewUtils.inject(this, mFatherView);
	}
	
	public void setData(String [] mData){
		this.mData =  mData;
		actionJbsy(null);
	}
	
	@OnClick(R.id.bt_worddital_wzsy)
	public void actionWzsy(View mView){
		if(mData!=null&&mData.length==3){
			mTvDital.setText(mData[1]);
		}
	}
	@OnClick(R.id.bt_worddital_cydg)
	public void actionCydg(View mView){
		if(mData!=null&&mData.length==3){
			mTvDital.setText(mData[2]);
		}
	}
	
	@OnClick(R.id.bt_worddital_jbsy)
	public void actionJbsy(View mView){
		if(mData!=null&&mData.length==3){
			mTvDital.setText(mData[0]);
		}
		
	}
	
	

}
