package com.bosi.chineseclass.components;


import com.bosi.chineseclass.han.components.BaseComponents;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

public class NiftyDialogComponents extends BaseComponents {
	NiftyDialogBuilder mBuilder;

	public NiftyDialogComponents(Context mContext) {
		super(mContext);
	}

	@Override
	public void initFatherView() {

	}

	public void setUpNifty(String mbt, String mbt2, String title,
			String message, View mCustromView) {
		mBuilder = NiftyDialogBuilder.getInstance(mContext)
				.withButton1Text(TextUtils.isEmpty(mbt) ? "确定" : mbt)
				.withMessage(message).withEffect(Effectstype.Shake)
				.withButton2Text(TextUtils.isEmpty(mbt2) ? "取消" : mbt2)
				.withTitle(title);
		if (mCustromView != null)
			mBuilder.setCustomView(mCustromView, mContext);
	}

	public void setUpNifty(String mbt, String mbt2, String title, String message) {
		setUpNifty(mbt, mbt2, title, message, null);
		mBuilder.setButton1Click(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				if (mOnNiftyCallBack != null)
					mOnNiftyCallBack.onBt1Click();
			}
		});

		mBuilder.setButton2Click(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				if (mOnNiftyCallBack != null)
					mOnNiftyCallBack.onBt2Click();
			}
		});
	}

	public OnNiftyCallBack mOnNiftyCallBack;

	public void setNoftyCallBack(OnNiftyCallBack mOnNiftyCallBack) {
		this.mOnNiftyCallBack = mOnNiftyCallBack;
	}

	public interface OnNiftyCallBack {
		public void onBt1Click();

		public void onBt2Click();
	}

	public void showBuilder() {
		try {
			mBuilder.show();
		} catch (Exception e) {
		}
	}

	public void dismissBuilder() {
		try {
			mBuilder.dismiss();
		} catch (Exception e) {
		}
	}

}
