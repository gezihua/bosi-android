package com.zy.booking.fragments;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.R;

import android.view.View;
import android.widget.LinearLayout;

public class FriendGroupFragment extends BaseFragment{
	
	@ViewInject(R.id.ll_container_body)
	LinearLayout mBodyLayout;
	
	
	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.layout_common_withoutscroll, null);
	}

	
	@Override
	void afterViewInject() {
		
	}

}
