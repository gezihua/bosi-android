package com.bosi.chineseclass.fragments;


import android.view.View;
import android.widget.LinearLayout;

import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.model.BphzLevAdapter;
import com.bosi.chineseclass.views.BSGridView;
import com.lidroid.xutils.view.annotation.ViewInject;

public class BphzLevFragment extends BaseFragment{

	@ViewInject(R.id.ll_bphz_body)
	LinearLayout mLayoutBody;
	
	BSGridView mGridView;
	@ViewInject(R.id.headactionbar)
	View mViewHead;
	
	@Override
	protected View getBasedView() {
		return View.inflate(mActivity, R.layout.layout_bphz, null);
	}

	@Override
	protected void afterViewInject() {
		
		HeadLayoutComponents mHead = new HeadLayoutComponents(mActivity, mViewHead);
		mHead.setTextMiddle("爆破汉字", -1);
		mGridView = new BSGridView(mActivity);
		mGridView.setNumColumns(5);
		mGridView.setVerticalSpacing(20);
		mGridView.setCacheColorHint(0);
		mLayoutBody.addView(mGridView);
		mGridView.setAdapter(new BphzLevAdapter(mActivity, null));

	}

}
