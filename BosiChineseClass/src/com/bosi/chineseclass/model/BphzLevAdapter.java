package com.bosi.chineseclass.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.activitys.SampleHolderActivity;
import com.bosi.chineseclass.bean.BphzBean;
import com.bosi.chineseclass.components.NiftyDialogComponents;
import com.bosi.chineseclass.components.NiftyDialogComponents.OnNiftyCallBack;
import com.bosi.chineseclass.control.SampleControl;
import com.bosi.chineseclass.db.BPHZ;
import com.bosi.chineseclass.han.util.PreferencesUtils;
import com.bosi.chineseclass.su.ui.actvities.WordsDetailActivity;
import com.bosi.chineseclass.utils.ViewHolder;

//第一个层级的
public class BphzLevAdapter extends ComListViewAdapter<List<BphzBean>> {

	BPHZ mBphz = new BPHZ();

	public BphzLevAdapter(Context mContext, List<BphzBean> mlists) {
		super(mContext, mlists);
		mNiftyDialog = new NiftyDialogComponents(mContext);
	}

	@Override
	public int getCount() {
		return 15;
	}

	@Override
	public View getView(int position, View mView, ViewGroup arg2) {
		if (position == 14) {
			return statisticView(position, mView);
		} else {
			return commonView(position, mView);
		}
	}

	protected void intentToWordDital(int start, int end, int tag) {
		PreferencesUtils.putInt(context,
				AppDefine.ZYDefine.EXTRA_DATA_BPHZ_SATSTICSTART, start);
		PreferencesUtils.putInt(context,
				AppDefine.ZYDefine.EXTRA_DATA_BPHZ_SATSTICEND, end);
		Intent mIntent = new Intent(context, WordsDetailActivity.class);
		mIntent.putExtra(WordsDetailActivity.EXTRA_NAME_WORDS_TAG, tag);
		context.startActivity(mIntent);

	}

	NiftyDialogComponents mNiftyDialog;

	private void showNotifyDialog() {
		String dataRemote = context.getResources().getString(
				R.string.dialog_clearbphz_levdata);
		mNiftyDialog.setUpNifty("确认", "取消", "提示", dataRemote);
		mNiftyDialog.setNoftyCallBack(new OnNiftyCallBack() {

			@Override
			public void onBt2Click() {

				mNiftyDialog.dismissBuilder();

			}

			@Override
			public void onBt1Click() {
				// 弹出一个提示框 用于确认是否删除数据
				mNiftyDialog.dismissBuilder();
				mBphz.deleteDbBaseBetweenSE(context, 1,
						AppDefine.ZYDefine.BPHZ_REFID_ADDED
								+ AppDefine.ZYDefine.BPHZ_TOTALNUM);
				changeDataSource(null);
			}
		});
		mNiftyDialog.showBuilder();
	}

	protected void sampleView(final int position, View mView) {
		final Button mButtonUnRem = ViewHolder.get(mView,
				R.id.bt_bphz_item_unrenumber);
		mButtonUnRem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String numUnRem = mButtonUnRem.getText().toString();
				onClickItemCommon(position,
						AppDefine.ZYDefine.BPHZ_TAG_UNREMBER, numUnRem);
			}
		});
		final Button mButtonRem = ViewHolder.get(mView,
				R.id.bt_bphz_item_renumber);
		mButtonRem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String numRem = mButtonRem.getText().toString();
				onClickItemCommon(position, AppDefine.ZYDefine.BPHZ_TAG_REMBER,
						numRem);
			}
		});

		mButtonRem.setText("0");
		mButtonUnRem.setText("0");

		if (mListData != null && mListData.size() > 0) {
			BphzBean mData = mListData.get(position);
			if (mData != null) {
				mButtonUnRem.setText(mData.mUnRemberNum);
				mButtonRem.setText(mData.mRemberNum);
			}
		}
	}

	protected View statisticView(final int position, View mView) {
		if (mView == null) {
			mView = View.inflate(context, R.layout.layout_bphz_item_statistic,
					null);
		}

		sampleView(position, mView);
		TextView mTvClearAllData = ViewHolder.get(mView,
				R.id.tv_bphz_clearalldata);

		mTvClearAllData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showNotifyDialog();
			}
		});

		return mView;
	}

	protected void onClickItemCommon(int position, int tag, String num) {
		if (num.equals("0")) {
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.toast_remote_nothingtolearn),
					Toast.LENGTH_SHORT).show();
			return;
		}
		BphzBean mData = mListData.get(position);
		String array[] = mData.mNumberBetween.split("-");
		String numStart = array[0];
		String numEnd = array[1];
		intentToWordDital(
				AppDefine.ZYDefine.BPHZ_REFID_ADDED
						+ Integer.parseInt(numStart),
				AppDefine.ZYDefine.BPHZ_REFID_ADDED + Integer.parseInt(numEnd),
				tag);
	}

	protected void onClickItemNumsBetween(int position) {
		Intent mIntent = new Intent(context, SampleHolderActivity.class);
		PreferencesUtils.putInt(context, "position", position);
		mIntent.putExtra(SampleControl.KEY_FRAGMENTNAMES,
				new String[] { "BphzLevvFragment" });
		mIntent.putExtra(SampleControl.KEY_PACKAGETNAME,
				"com.bosi.chineseclass.fragments");
		context.startActivity(mIntent);
	}

	protected void setItemViewBg(View mView) {
		mView.setBackgroundResource(R.drawable.bphz_levitem_bg);
	}

	protected View commonView(final int position, View mView) {
		if (mView == null) {
			mView = View.inflate(context, R.layout.layout_bphz_item_checkpoint,
					null);
		}

		LinearLayout mLayoutBody = ViewHolder
				.get(mView, R.id.rl_bphz_item_body);
		setItemViewBg(mLayoutBody);

		Button mButtonSize = ViewHolder.get(mView, R.id.bt_bphz_item_number);
		mButtonSize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onClickItemNumsBetween(position);
			}
		});

		sampleView(position, mView);

		if (mListData != null && mListData.size() > 0) {
			BphzBean mData = mListData.get(position);
			if (mData != null) {
				mButtonSize.setText(mData.mNumberBetween);
			}
		}

		int lev = getLevByPosition(position);
		if (lev == 0) {
			mButtonSize
					.setBackgroundResource(R.drawable.bphz_levitem_number_lvbg);
		} else if (lev == 1) {
			mButtonSize
					.setBackgroundResource(R.drawable.bphz_levitem_number_lvvbg);
		} else if (lev == 2) {
			mButtonSize
					.setBackgroundResource(R.drawable.bphz_levitem_number_lvvvbg);
		} else {
			mButtonSize
					.setBackgroundResource(R.drawable.bphz_levitem_number_lvvvvbg);
		}
		return mView;
	}

	// 用于区分颜色
	protected int getLevByPosition(int position) {
		if (position <= 4) {
			return 0;
		} else if (position > 4 && position <= 6) {
			return 1;
		} else if (position > 6 && position <= 9) {
			return 2;
		}
		return 3;
	}

}
