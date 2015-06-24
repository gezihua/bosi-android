package com.bosi.chineseclass.fragments;

import java.io.IOException;
import java.util.Properties;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.VideoView;

import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.views.AutoChangeLineViewGroup;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @author zhujohnle 拼音学习的页面 包括声母和韵母
 * 
 */
public class PyLearnFragment extends BaseFragment {

	String[] marrayForLearn = null;


	@ViewInject(R.id.ll_pylearn_leftbody)
	LinearLayout mLinearPinyinLeft;
	@ViewInject(R.id.ll_pinyin_sym_item_dital)
	LinearLayout mBody;
	AutoChangeLineViewGroup mAutoViewGroup;

	@ViewInject(R.id.vv_pinyinlearn_reader)
	VideoView mVideoViewRead;

	@ViewInject(R.id.pinyinlearn_dital)
	LinearLayout mLayoutDital;

	Properties mPorperties;

	String mPressedZm = "b";
	@ViewInject(R.id.iv_pyxx_sm)
	ImageView mImageViewSm;

	@ViewInject(R.id.iv_pyxx_ym)
	ImageView mImageViewYm;
	
	@ViewInject(R.id.headactionbar)
	View mViewHeadbar;
	
	private final String SUFFIX_FYYL = "_f";
	private final String SUFFIX_PYP = "_p";
	private final String SUFFIX_DYD = "_d";
	private final String SUFFIX_DYDD = "_dd";

	@OnClick(R.id.iv_pyxx_ym)
	public void acitonYm(View mView) {
		mImageViewYm.bringToFront();
		mPressedZm = "a";
		initBasicPinYin(CategoryPinyin.YM);
		addPinYinNameDital();
	}

	@OnClick(R.id.iv_pyxx_sm)
	public void acitonSm(View mView) {
		mPressedZm = "b";
		mImageViewSm.bringToFront();
		initBasicPinYin(CategoryPinyin.SM);
		addPinYinNameDital();
	}

	@Override
	protected View getBasedView() {
		return View.inflate(mActivity, R.layout.pinyin_layout_bodyview, null);
	}

	public PyLearnFragment(CategoryPinyin mCategory) {
	}


	private void setup() {
		initComponents();
		initBasicPinYin(CategoryPinyin.SM);
		addPinYinNameDital();
	}

	private void initComponents() {
		mAutoViewGroup = new AutoChangeLineViewGroup(mActivity);
		mLinearPinyinLeft.addView(mAutoViewGroup);
	}

	private void addPinYinNameDital() {
		mAutoViewGroup.removeAllViews();
		for (int i = 0; i < marrayForLearn.length; i++) {
			final TextView mTextView = new TextView(mActivity);
			mTextView.setTextSize(20);
			mTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					mPressedZm = mTextView.getText().toString();
					showPresentPressedZmFyyl(getPropertiesFromKey(mPressedZm
							+ SUFFIX_FYYL));
				}
			});
			mTextView.setBackgroundColor(Color.YELLOW);
			mTextView.setText(marrayForLearn[i]);
			mTextView.setGravity(Gravity.CENTER);
			mAutoViewGroup.addView(mTextView);
		}
	}

	private String getPropertiesFromKey(String key) {
		String value = mPorperties.getProperty(key);

		try {
			return new String(value.getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
		}
		return "";
	}

	@OnClick(R.id.bt_pingyinlearn_fyyl)
	public void actionFyyl(View mView) {
		showPresentPressedZmFyyl(getPropertiesFromKey(mPressedZm + SUFFIX_FYYL));
	}

	@OnClick(R.id.bt_pingyinlearn_pyp)
	public void actionPyp(View mView) {
		showPypDital(getPropertiesFromKey(mPressedZm + SUFFIX_PYP).split("#"));
	}

	@OnClick(R.id.bt_pingyinlearn_dyd)
	public void actionDyd(View mView) {
		showdydDital(getPropertiesFromKey(mPressedZm + SUFFIX_DYD).split("#"));
	}

	private void showPresentPressedZmFyyl(String msg) {
		mLayoutDital.removeAllViews();
		TextView mTextView = new TextView(mActivity);
		mTextView.setText(msg);
		mTextView.setTextSize(20);
		mLayoutDital.addView(mTextView);
	}

	/**
	 * 更换拼一拼的数据内容
	 * 
	 * */
	private void showPypDital(String[] mArrays) {
		if (mArrays == null)
			return;
		mLayoutDital.removeAllViews();

		for (int i = 0; i < mArrays.length; i++) {
			TextView mTextView = new TextView(mActivity);
			mTextView.setTextSize(20);
			mTextView.setText(mArrays[i]);
			LinearLayout.LayoutParams mLayoutParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
			mLayoutParams.setMargins(10, 0, 0, 0);
			mTextView.setLayoutParams(mLayoutParams);
			mLayoutDital.addView(mTextView);
		}

	}

	/**
	 * 更换读一读的数据内容
	 * 
	 * 
	 * */
	private void showdydDital(String[] mArrays) {
		if (mArrays == null||mArrays.length==0)
			return;
		mLayoutDital.removeAllViews();
		AutoChangeLineViewGroup mAutoChangeLineView = new AutoChangeLineViewGroup(
				mActivity);
		for (int i = 0; i < mArrays.length; i++) {
			View mView = View.inflate(mActivity, R.layout.item_pinyinlearn_pyp,
					null);
			TextView mTextView = (TextView) mView
					.findViewById(R.id.tv_pinyinlearn_item_dyd);
			mTextView.setTextSize(20);
			mTextView.setText(mArrays[i]);
			mAutoChangeLineView.addView(mView);

		}
		mLayoutDital.addView(mAutoChangeLineView);
	}

	private void initBasicPinYin(CategoryPinyin mCategory) {

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
		HeadLayoutComponents mHeadActionbar = new HeadLayoutComponents(mActivity, mViewHeadbar);
		mHeadActionbar.setTextMiddle("拼音学习", -1);
		setup();
		mPorperties = new Properties();
		try {
			mPorperties.load(getResources().getAssets().open("pinyin"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mPorperties.clear();
		mPorperties = null;

	}

	public enum CategoryPinyin {
		SM, YM
	}

}
