package com.bosi.chineseclass.fragments;

import android.view.View;
import android.webkit.WebView;

import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.lidroid.xutils.view.annotation.ViewInject;


//专家课堂
public class ExpertClassFragment extends BaseFragment{

	
	HeadLayoutComponents mHeadActionbar;
	
	@ViewInject(R.id.headactionbar)
	View mHeadActionBar;
	@ViewInject(R.id.expertmain_wv_menu)
	WebView mWvExpertMenu;
	
	@ViewInject(R.id.expertmain_wv_instro)
	WebView mWvExpertinstro;
	
	@Override
	protected View getBasedView() {
		return View.inflate(mActivity, R.layout.layout_expertclass_mainpage, null);
	}

	@Override
	protected void afterViewInject() {
		mHeadActionbar = new HeadLayoutComponents(mActivity, mHeadActionBar);
		mHeadActionbar.setTextMiddle("专家课堂", -1);
	}

	private void loadMenuData(){
	}
	
	private void loadInstroData(){
	}
}
