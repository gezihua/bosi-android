package com.zy.booking.modle.pagefactory;

import java.util.ArrayList;

import com.zy.booking.fragments.CustromAskdHistroyFragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

public class OrderServiceFactory extends AbsFactory{

	public OrderServiceFactory(Context mContext) {
		super(mContext);
	}

	@Override
	public ArrayList<Fragment> productBody() {
		ArrayList<Fragment> mLists = new ArrayList<Fragment>();
		CustromAskdHistroyFragment mCustromAskedHistory = new CustromAskdHistroyFragment();
		mLists.add(mCustromAskedHistory);
		return mLists;
	}

	@Override
	public void productMenu(ViewGroup mViewGroupBottom) {
		
	}


}
