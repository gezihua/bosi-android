package com.bosi.chineseclass.model;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bosi.chineseclass.bean.BphzBean;

//第二个层级
public class BphzLevvAdapter extends BphzLevAdapter{

	public BphzLevvAdapter(Context mContext, List<BphzBean> mlists) {
		super(mContext, mlists);
	}

	
	@Override
	public View getView(int position, View mView, ViewGroup arg2) {
		return super.getView(position, mView, arg2);
	}
	
	
}
