package com.bosi.chineseclass.model;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.bean.BphzBean;
import com.bosi.chineseclass.han.util.PreferencesUtils;

//第二个层级
public class BphzLevvAdapter extends BphzLevAdapter {

	public BphzLevvAdapter(Context mContext, List<BphzBean> mlists) {
		super(mContext, mlists);
	}

	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public View getView(int position, View mView, ViewGroup arg2) {
		return commonView(position, mView);
	}

	@Override
	protected View statisticView(int position, View mView) {
		return View.inflate(context, R.layout.bphz_levv_remote, null);
	}

	@Override
	protected int getLevByPosition(int position) {
		return super.getLevByPosition(PreferencesUtils.getInt(context,
				"position"));
	}

	@Override
	protected void onClickItemNumsBetween(int position) {
		// Intent mIntent = new Intent( ); //进入 字典详情页
		BphzBean mData = mListData.get(position);
		String array [] =mData.mNumberBetween.split("-");
	    String numStart =array[0];
	    String numEnd = array[1];
		intentToWordDital(120000+Integer.parseInt(numStart),120000+Integer.parseInt(numEnd),AppDefine.ZYDefine.BPHZ_TAG_NORMAL);
	}

	@Override
	protected void setItemViewBg(View mView) {
		mView.setBackgroundResource(R.drawable.bphz_levv_item_bg);
	}

}
