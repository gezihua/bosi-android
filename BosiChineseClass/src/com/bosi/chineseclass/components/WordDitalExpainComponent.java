package com.bosi.chineseclass.components;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bosi.chineseclass.R;
import com.bosi.chineseclass.bean.SpanData;
import com.bosi.chineseclass.han.components.BaseComponents;
import com.bosi.chineseclass.utils.BosiUtils;

public class WordDitalExpainComponent extends BaseComponents implements
		OnClickListener {

	TextView mViewWzsy;
	TextView mViewCydg;
	TextView mViewJbsy;
	TextView mTvDital;

	public String[] mData;

	public WordDitalExpainComponent(Context mContext, View mView) {
		super(mContext, mView);
	}

	@Override
	public void initFatherView() {
		mViewWzsy = (TextView) mFatherView.findViewById(R.id.bt_worddital_wzsy);
		mViewWzsy.setOnClickListener(this);
		mViewCydg = (TextView) mFatherView.findViewById(R.id.bt_worddital_cydg);
		mViewCydg.setOnClickListener(this);
		mTvDital = (TextView) mFatherView
				.findViewById(R.id.tv_worddital_expain);
		mViewJbsy = (TextView) mFatherView.findViewById(R.id.bt_worddital_jbsy);
		mViewJbsy.setOnClickListener(this);
	}

	public void setData(String[] mData) {
		this.mData = mData;
		actionJbsy(null);
	}

	public void actionWzsy(View mView) {
		if (mData != null && mData.length == 3) {
			SpanData mSpanData = BosiUtils.getInsertRelineData(mData[1]);
			if (!TextUtils.isEmpty(mSpanData.mResouce))
				mTvDital.setText(mSpanData.mResouce);
		}
		mViewWzsy.setBackgroundResource(R.drawable.tab_word_detail_normal);
		mViewCydg.setBackgroundResource(R.drawable.tab_word_detail_selected);
		mViewJbsy.setBackgroundResource(R.drawable.tab_word_detail_selected);
	}

	public void actionCydg(View mView) {
		if (mData != null && mData.length == 3) {

			SpanData mSpanData = BosiUtils.getInsertRelineData(mData[2]);
			if (mSpanData != null) {
				if (!TextUtils.isEmpty(mSpanData.mResouce)) {
					if (mSpanData.mSpaList != null
							&& mSpanData.mSpaList.size() > 0) {
						SpannableStringBuilder builder = new SpannableStringBuilder(
								mSpanData.mResouce);
						
						for (int i = 0; i < mSpanData.mSpaList.size(); i++) {
							Integer[] mIntegerArray = mSpanData.mSpaList.get(i);
						
							if (mIntegerArray[0] != null
									&& mIntegerArray[1] != null){
								ForegroundColorSpan redSpan = new ForegroundColorSpan(
										Color.RED);
								builder.setSpan(redSpan, mIntegerArray[0],
										mIntegerArray[1],
										Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							}
								
						}
						mTvDital.setText(builder);
					}

				}

			}

		}
		mViewWzsy.setBackgroundResource(R.drawable.tab_word_detail_selected);
		mViewCydg.setBackgroundResource(R.drawable.tab_word_detail_normal);
		mViewJbsy.setBackgroundResource(R.drawable.tab_word_detail_selected);
	}

	public void actionJbsy(View mView) {
		if (mData != null && mData.length == 3) {
			SpanData mSpanData = BosiUtils.getInsertRelineData(mData[0]);
			if (!TextUtils.isEmpty(mSpanData.mResouce))
				mTvDital.setText(mSpanData.mResouce);
		}
		mViewWzsy.setBackgroundResource(R.drawable.tab_word_detail_selected);
		mViewCydg.setBackgroundResource(R.drawable.tab_word_detail_selected);
		mViewJbsy.setBackgroundResource(R.drawable.tab_word_detail_normal);

	}

	@Override
	public void onClick(View mView) {
		switch (mView.getId()) {
		case R.id.bt_worddital_wzsy:
			actionWzsy(mView);

			break;
		case R.id.bt_worddital_cydg:
			actionCydg(mView);
			break;
		case R.id.bt_worddital_jbsy:
			actionJbsy(mView);

			break;

		default:
			break;
		}
	}

}
