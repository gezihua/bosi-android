package com.bosi.chineseclass.fragments;


import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.views.AutoChangeLineViewGroup;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @author zhujohnle
 * 拼音学习的页面
 * 包括声母和韵母
 * 
 */
public class PyLearnFragment extends BaseFragment {

	String [] marrayForLearn=null;
	
	CategoryPinyin mCategory;
	
	@ViewInject(R.id.ll_pylearn_leftbody)
	LinearLayout mLinearPinyinLeft;
	
	
	AutoChangeLineViewGroup mAutoViewGroup;
	
	@Override
	protected View getBasedView() {
		return View.inflate(mActivity, R.layout.pinyin_layout_bodyview, null);
	}
	
	public PyLearnFragment(CategoryPinyin mCategory){
		setCurrentPinyinCategory(mCategory);
	}
	private void setCurrentPinyinCategory(CategoryPinyin mCategory){
		this.mCategory = mCategory;
	}
	
	private void setup(){
		initComponents();
		initBasicPinYin();
		addPinYinNameDital();
		
	}
	private void initComponents(){
		mAutoViewGroup = new AutoChangeLineViewGroup(mActivity);
		mLinearPinyinLeft.addView(mAutoViewGroup);
	}
	
	private void addPinYinNameDital(){
		for(int i= 0; i<marrayForLearn.length;i++){
			TextView mTextView = new TextView(mActivity);
			
			mTextView.setTextSize(20);
			mTextView.setBackgroundColor(Color.BLUE);
			mTextView.setText(marrayForLearn[i]);
			mTextView.setPadding(20, 0, 20, 0);
			mAutoViewGroup.addView(mTextView);
			
		}
	}
	
	private void initBasicPinYin(){
		
		switch (mCategory) {
		case SM:
			marrayForLearn = getString(R.string.sm_learn_dital).split("#");
			break;
			
		case YM:
			marrayForLearn = getString(R.string.ym_learn_dital).split("#");
			break;
		default:
			break;
		}
	}
	

	@Override
	protected void afterViewInject() {
		setup();
	}
	
	public enum CategoryPinyin{
		SM,YM
	}

}
