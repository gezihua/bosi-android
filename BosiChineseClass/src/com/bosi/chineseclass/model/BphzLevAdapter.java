package com.bosi.chineseclass.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bosi.chineseclass.R;
import com.bosi.chineseclass.db.BphzHistory;
import com.bosi.chineseclass.utils.ViewHolder;

public class BphzLevAdapter extends ComListViewAdapter<BphzHistory> {

	public BphzLevAdapter(Context mContext, BphzHistory mData) {
		super(mContext, mData);
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
