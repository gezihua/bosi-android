package com.bosi.chineseclass.model;

import java.util.List;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bosi.chineseclass.R;
import com.bosi.chineseclass.bean.BphzBean;
import com.bosi.chineseclass.utils.ViewHolder;


//第一个层级的
public class BphzLevAdapter extends ComListViewAdapter<List<BphzBean>> {

	public BphzLevAdapter(Context mContext,List<BphzBean> mlists) {
		super(mContext, mlists);
	}

	@Override
	public int getCount() {
		return 15;
	}

	@Override
	public View getView(int position, View mView, ViewGroup arg2) {
		if (position == 14) {
			return statisticView(position, mView);
		} else {
			return commonView(position, mView);
		}
	}

	private View statisticView(int position, View mView) {
		if (mView == null) {
			mView = View.inflate(context, R.layout.layout_bphz_item_statistic,
					null);
		}
		return mView;
	}

	private View commonView(int position, View mView) {
		if (mView == null) {
			mView = View.inflate(context, R.layout.layout_bphz_item_checkpoint,
					null);
		}
		
		
		
		
		
		Button mButtonSize = ViewHolder.get(mView, R.id.bt_bphz_item_number);
		
		
		
		
		Button mButtonUnRem = ViewHolder.get(mView,
				R.id.bt_bphz_item_unrenumber);
		
		
		
		Button mButtonRem = ViewHolder.get(mView, R.id.bt_bphz_item_renumber);
		if(mListData.size()>0){
			BphzBean mData = mListData.get(position);
			if(mData!=null){
				mButtonSize.setText(mData.mNumberBetween);
				mButtonUnRem.setText(mData.mUnRemberNum);
				mButtonRem .setText(mData.mRemberNum);
			}
		}
		
		int lev = getLevByPosition(position);
		if (lev == 0) {
			mButtonSize
					.setBackgroundResource(R.drawable.bphz_levitem_number_lvbg);
		} else if (lev == 1) {
			mButtonSize
					.setBackgroundResource(R.drawable.bphz_levitem_number_lvvbg);
		} else if (lev == 2) {
			mButtonSize
					.setBackgroundResource(R.drawable.bphz_levitem_number_lvvvbg);
		} else {
			mButtonSize
					.setBackgroundResource(R.drawable.bphz_levitem_number_lvvvvbg);
		}
		return mView;
	}
	
  //用于区分颜色

	private int getLevByPosition(int position) {
		if (position <= 4) {
			return 0;
		} else if (position > 4 && position <= 6) {
			return 1;
		} else if (position > 6 && position <= 9) {
			return 2;
		}
		return 3;
	}

}
