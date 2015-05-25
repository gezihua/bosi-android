package com.zy.booking.components;

import android.app.Activity;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zy.booking.R;
import com.zy.booking.util.DataTools;

public class HeadLayoutComponents extends BaseComponents {

	public HeadLayoutComponents(Context mContext, View mView) {
		super(mContext, mView);
		mView.setVisibility(View.VISIBLE);
	}

	private TextView mTvLeft;

	private TextView mTvMiddle;

	private TextView mTvRight;

	
	private TextView mTvMiddleDown;
	
	public TextView getMiddleText(){
		mTvMiddleDown = (TextView) mFatherView.findViewById(R.id.headviewmiddledown);
		mTvMiddleDown.setVisibility(View.VISIBLE);
		return mTvMiddleDown;
	}
	@Override
	public void initFatherView() {
		mTvLeft = (TextView) mFatherView.findViewById(R.id.tvLeft);
		mTvMiddle = (TextView) mFatherView.findViewById(R.id.headviewmiddle);
		mTvRight = (TextView) mFatherView.findViewById(R.id.tvright);
	}
	
	public void setDefaultLeftCallBack(boolean isLeftCallBack){
		if(isLeftCallBack){
			this.setTextLeft("", R.drawable.icon_back_white);
			mTvLeft.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Activity mActivity = (Activity) mContext;
					mActivity.finish();
				}
			});
		}
	}

	public void setTextLeft(String text, int resid) {
		mTvLeft.setText(text);
		if (resid != -1)
		mTvLeft.setBackgroundResource(resid);
	}

	public void setTextMiddle(String text, int resid) {
		mTvMiddle.setText(text);
		if (resid != -1)
			mTvMiddle.setBackgroundResource(resid);
	}

	public void setTextRight(String text, int resid) {
		mTvRight.setText(text);
		if (resid != -1){
			mTvRight.setBackgroundResource(resid);
			mTvRight.setLayoutParams(new LinearLayout.LayoutParams(DataTools.dip2px(mContext, 30),DataTools.dip2px(mContext, 30)));
		}
			
	}
	public void setTextRight(String text, int resid,float size) {
		setTextRight(text,resid);
		mTvRight.setTextSize(size);
	}

	public void setLeftOnclickListener(OnClickListener mOnclickListener) {
		mTvLeft.setOnClickListener(mOnclickListener);
	}

	public void setRightOnClickListener(OnClickListener mOnclickListener) {
		mTvRight.setOnClickListener(mOnclickListener);
	}

}
