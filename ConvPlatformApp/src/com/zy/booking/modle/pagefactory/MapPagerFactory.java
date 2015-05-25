package com.zy.booking.modle.pagefactory;

import java.util.ArrayList;

import com.zy.booking.fragments.CustromAskdHistroyFragment;
import com.zy.booking.fragments.MapLocFragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

public class MapPagerFactory extends AbsFactory{

	public MapPagerFactory(Context mContext) {
		super(mContext);
	}

	@Override
	public ArrayList<Fragment> productBody() {
		ArrayList<Fragment> mLists = new ArrayList<Fragment>();
		MapLocFragment mMapLocFragment = new MapLocFragment();
		mLists.add(mMapLocFragment);
		return mLists;
	}

	@Override
	public void productMenu(ViewGroup mViewGroupBottom) {
		
	}


}
