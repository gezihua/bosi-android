package com.zy.booking.components;

import android.content.Context;


import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.CpApplication;
import com.zy.booking.R;


//搜索框控制 组件
public class SearchLayoutComponents extends BaseComponents implements OnClickListener{

	@ViewInject(R.id.tv_search)
	private TextView mActionView;

	@ViewInject(R.id.et_search)
	private EditText mEditText;

	@ViewInject(R.id.view_searchcomp_close)
	ImageView mIvBack;

	@ViewInject(R.id.iv_layoutinput_del)
	ImageView mIvClear;

	public final String TAG_SEARCH = "search";

	public SearchLayoutComponents(Context mContext) {
		super(mContext);
	}

	public SearchLayoutComponents(Context mContext, View mView) {
		super(mContext, mView);
	}
	
	TextWatcher mTextWath;
	
	public void addTextWatch(TextWatcher mTextWath){
		this.mTextWath = mTextWath;
	}

	@Override
	public void initFatherView() {
		if (mFatherView == null)
			mFatherView = LayoutInflater.from(mContext).inflate(
					R.layout.layout_search_component, null);
		ViewUtils.inject(this, mFatherView);
		
		mActionView.setOnClickListener(this);
		mIvBack.setOnClickListener(this);
		mIvClear.setOnClickListener(this);
		mActionView.setTag(TAG_SEARCH);
		
	}

	public void actionClear(View mView) {
		mEditText.setText("");
	}

	public void actionBack(View mView) {
		actionClear(mView);
		mFatherView.setVisibility(View.GONE);
	}

	public void actionSerach(View mView) {
		if (TextUtils.isEmpty(mEditText.getText().toString()))
			CpApplication.getApplication().playYoYo(mEditText);
		if (mOnComponentsActionListener != null) {
			mOnComponentsActionListener.onAction(mView);
		}
	}

	public EditText getEditText() {
		return mEditText;
	}
	
	public TextView getActionView(){
		return mActionView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_search:
			actionSerach(v);
			break;
		case R.id.view_searchcomp_close:
			actionBack(v);
			break;
		case R.id.iv_layoutinput_del:
			actionClear(v);
			break;
			
		default:
			break;
		}
	}

}
