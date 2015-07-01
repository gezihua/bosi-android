package com.bosi.chineseclass.fragments;

import java.io.IOException;
import java.util.Properties;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.VideoView;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.XutilImageLoader;
import com.bosi.chineseclass.components.MediaPlayerPools;
import com.bosi.chineseclass.control.DownLoadResouceControl;
import com.bosi.chineseclass.control.DownLoadResouceControl.DownloadCallback;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.utils.BubbleImageHelper;
import com.bosi.chineseclass.views.AutoChangeLineViewGroup;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @author zhujohnle 拼音学习的页面 包括声母和韵母
 * 
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class PyLearnFragment extends BaseFragment implements DownloadCallback {

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
	
	@ViewInject(R.id.iv_pinyinlearn_reader)
	
	ImageView mImageReader;
	
	
	MediaPlayerPools mMediaPlayerPools;
	
	XutilImageLoader mImageLoader;

	private final String SUFFIX_FYYL = "_f";
	private final String SUFFIX_PYP = "_p";
	private final String SUFFIX_DYD = "_d";
	private final String SUFFIX_DYDD = "_dd";

	@OnClick(R.id.iv_pyxx_ym)
	public void acitonYm(View mView) {
		mImageViewYm.bringToFront();
		mPressedZm = "y";
		initBasicPinYin(CategoryPinyin.YM);
		addPinYinNameDital();
		downLoadFilesBaseCurrentZiMu();
		showPresentPressedZmFyyl(getPropertiesFromKey(mPressedZm + SUFFIX_FYYL));
	}

	@OnClick(R.id.iv_pyxx_sm)
	public void acitonSm(View mView) {
		mPressedZm = "a";
		mImageViewSm.bringToFront();
		initBasicPinYin(CategoryPinyin.SM);
		addPinYinNameDital();
		downLoadFilesBaseCurrentZiMu();
		showPresentPressedZmFyyl(getPropertiesFromKey(mPressedZm + SUFFIX_FYYL));
	}

	@Override
	protected View getBasedView() {
		return View.inflate(mActivity, R.layout.pinyin_layout_bodyview, null);
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

	/**
	 * 
	 * 当前文件夹目录
	 * 
	 * */
	private String getFolderPath(){
		
		return AppDefine.FilePathDefine.APP_PINYINLEARNPATH
				+ mPressedZm + "/";
	}
	
	private String getAbsoultFilePath(){
		return BSApplication.getInstance().mStorage
		.getFile(getFolderPath()).getAbsolutePath()+"/";
	}
	private void addPinYinNameDital() {
		mAutoViewGroup.removeAllViews();
		for (int i = 0; i < marrayForLearn.length; i++) {
			final TextView mTextView = new TextView(mActivity);
			mTextView.setTextSize(20);
			mTextView.setGravity(Gravity.CENTER);
			mTextView.setPadding(5, 3, 5, 3);
			mTextView.setBackground(getResources().getDrawable(
					R.drawable.pingying_learn_zibg));
			mTextView.setGravity(Gravity.CENTER);
			mTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// 检查固定的目录是否有下载数据 如果没有则需要创建下载
					mPressedZm = mTextView.getText().toString();
					downLoadFilesBaseCurrentZiMu();
					showPresentPressedZmFyyl(getPropertiesFromKey(mPressedZm
							+ SUFFIX_FYYL));
				}
			});
			mTextView.setText(marrayForLearn[i]);
			mAutoViewGroup.addView(mTextView);
		}
	}

	private void downLoadFilesBaseCurrentZiMu(){
		String mCurrentFoderName =getFolderPath();
		BSApplication.getInstance().mStorage
				.createDirectory(mCurrentFoderName);
		String filePath = getAbsoultFilePath();

		String[] mFilePath = getDownLoadUrlsBaseCurrentSouce();
		if (mFilePath != null && mFilePath.length > 0) {
			int files = BSApplication.getInstance().mStorage
					.getFile(mCurrentFoderName).list().length;
			if (mFilePath.length != files)
				mDownLoadControl.downloadFiles(filePath,
						mFilePath);
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
			mTextView.setGravity(Gravity.CENTER);
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
	 * */
	private void showdydDital(String[] mArrays) {
		if (mArrays == null || mArrays.length == 0)
			return;
		mLayoutDital.removeAllViews();
		AutoChangeLineViewGroup mAutoChangeLineView = new AutoChangeLineViewGroup(
				mActivity);
		for (int i = 0; i < mArrays.length; i++) {
			final int tagPosition = i;
			View mView = View.inflate(mActivity, R.layout.item_pinyinlearn_pyp,
					null);
			TextView mTextView = (TextView) mView
					.findViewById(R.id.tv_pinyinlearn_item_dyd);
			final View mViewVoice = (TextView) mView
					.findViewById(R.id.bt_pinyinlearn_item_dyd);
			mViewVoice.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
				String [] 	mVideoNames = getPropertiesFromKey(mPressedZm + SUFFIX_DYDD)
						.split("#");
				String fileName = mVideoNames[tagPosition]+AppDefine.STUFFDEFICE.STUFF_VOICE;
				playVoice(getAbsoultFilePath()+fileName);
				}
			});
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

	DownLoadResouceControl mDownLoadControl;

	@Override
	protected void afterViewInject() {

		mDownLoadControl = new DownLoadResouceControl(mActivity);
		mDownLoadControl.setOnDownLoadCallback(this);
		
		HeadLayoutComponents mHeadActionbar = new HeadLayoutComponents(
				mActivity, mViewHeadbar);
		mHeadActionbar.setTextMiddle("拼音学习", -1);
		mMediaPlayerPools = new MediaPlayerPools(mActivity);
		setup();
		mPorperties = new Properties();
		try {
			mPorperties.load(getResources().getAssets().open("pinyin"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		mImageLoader = new XutilImageLoader(mActivity);
		showPresentPressedZmFyyl(getPropertiesFromKey(mPressedZm + SUFFIX_FYYL));
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

	private void playVoice(String fileName){
		mMediaPlayerPools.playMediaFile(fileName);
	}
	
	private void playVideoRead(){
		
	}
	private void playVideoWrite(){
		
	}
	private void displayCurrentZmbg(){
		mImageLoader.getBitmapFactory().display(mImageReader, getAbsoultFilePath()+mPressedZm+".jpg");
	}
	private String[] getDownLoadUrlsBaseCurrentSouce() {

		String[] mVoiceUrls = getPropertiesFromKey(mPressedZm + SUFFIX_DYDD)
				.split("#");
		String[] mUrlSoruce = new String[mVoiceUrls.length+3];
		// 组拼
		for (int i = 0; i < mVoiceUrls.length; i++) {
			String voiceSouceValue = mVoiceUrls[i];
			String urlForVoice = AppDefine.URLDefine.URL_PINYINVOICE
					+ mPressedZm + "/" + voiceSouceValue + ".mp3";
			mUrlSoruce[i] = urlForVoice;
		}
		mUrlSoruce[mVoiceUrls.length] =  AppDefine.URLDefine.URL_PINYINVOICE
				+ mPressedZm + "/" + mPressedZm+".mp4";
		
		mUrlSoruce[mVoiceUrls.length+1] =  AppDefine.URLDefine.URL_PINYINVOICE
				+ mPressedZm + "/" + mPressedZm+"-1.mp4";
		
		mUrlSoruce[mVoiceUrls.length+2] =  AppDefine.URLDefine.URL_PINYINVOICE
				+ mPressedZm + "/" + mPressedZm+".jpg";
		return mUrlSoruce;
	}

	@Override
	public void onDownLoadCallback(int mCurrentSize, int wholeSize) {
		if(mCurrentSize ==wholeSize){
			//该播放mp4的播放mp4 该显示图片的显示图片
			displayCurrentZmbg();
		}
	}

}
