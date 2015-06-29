package com.bosi.chineseclass.fragments.hzcs;

import android.view.View;


import com.bosi.chineseclass.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;


//趣味汉字
public class QwhzDitalFragment extends AbsHzcsFragment{
	
	private String [] zimiguanDataArray;
	private String [] qwhzDataArray;
	private String [] xygDataArray;
	private String [] cbzqhDataArray;
	private String [] sjxhzDataArray;
	
	
	private void initarray(){
		zimiguanDataArray = getResources().getStringArray(R.array.hzcs_qwhz_zmg_picarray);
		qwhzDataArray = getResources().getStringArray(R.array.hzcs_qwhz_qwhz_picarray);
		xygDataArray = getResources().getStringArray(R.array.hzcs_qwhz_xyg_picarray);
		cbzqhDataArray = getResources().getStringArray(R.array.hzcs_qwhz_cbzqu_picarray);
		sjxhzDataArray = getResources().getStringArray(R.array.hzcs_qwhz_sjxhz_picarray);
	}
	@OnClick(R.id.bt_quhz_dital_zmg)
	public void actionZmg(View mView){
		mTvDitalTitle.setText("字谜关");
		mCurrentData = zimiguanDataArray;
		downloadimgs();
	}
	@OnClick(R.id.bt_qwhz_dital_qhz)
	public void actionQwhz(View mView){
		mTvDitalTitle.setText("趣味汉子");
		mCurrentData = qwhzDataArray;
		downloadimgs();
	}
	@OnClick(R.id.bt_qwhz_dital_xyg)
	public void actionXyg(View mView){
		mTvDitalTitle.setText("谐音关");
		mCurrentData = xygDataArray;
		downloadimgs();
	}
	@OnClick(R.id.bt_qwhz_dital_cbz)
	public void actionCbz(View mView){
		mTvDitalTitle.setText("错别字趣话");
		mCurrentData = cbzqhDataArray;
		downloadimgs();
	}
	@OnClick(R.id.bt_qwhz_dital_sjx)
	public void actionSjxhz(View mView){
		mTvDitalTitle.setText("三角形汉子");
		mCurrentData = sjxhzDataArray;
		downloadimgs();
	}
	

	@Override
	public void initMenu() {

		mImageLoader.getBitmapFactory().display(mIvDital,
				"assets/hzqy/cjzz.png");
		View mMenuView = View.inflate(mActivity, R.layout.layout_qwhz_menu, null);
		mLayoutMenu.addView(mMenuView);
		ViewUtils.inject(this, mMenuView);
		mBtLeft.setVisibility(View.GONE);
		mBtRight.setVisibility(View.GONE);
	
	}



	@Override
	protected void afterViewInject() {
		super.afterViewInject();
		mTvDitalTitle.setText("趣味汉字");
		mHeadActionBar.setTextMiddle("趣味汉字", -1);
		initarray();
		mCurrentData = qwhzDataArray; 
		downloadimgs();
	}


	@Override
	protected void displayBgView() {
		mViewBody.setBackgroundResource(R.drawable.gd);
	}

	@Override
	protected void downLoadImageOverAction() {
		updateDitalPg();
	}
	

}
