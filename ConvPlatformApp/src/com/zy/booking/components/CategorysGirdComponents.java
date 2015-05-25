package com.zy.booking.components;



import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.zy.booking.modle.CategorysAdapter;

public class CategorysGirdComponents extends BaseComponents {

	// 禁止滚动的方法在api中未体现

	public CategorysGirdComponents(LayoutInflater mLayoutInflater,
			Context mContext) {
		super(mLayoutInflater, mContext);
	}

	
	public CategorysGirdComponents(Context mContext ,GridView mGirdGridView){
		super(null, mContext);
		this.mGridView =mGirdGridView;
		mFatherView = mGridView;
		mGridView.setCacheColorHint(0);
	}
	protected GridView mGridView;
	List<Integer> mListData;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	
	@Override
	public void initFatherView() {
		mGridView = new MyGirdView(mContext);
		mGridView.setNumColumns(3);
		mGridView.setCacheColorHint(0);
		mFatherView = mGridView;
	}

	public void setAdapter() {
		mGridView.setAdapter(new CategorysAdapter(mContext, mListData));
		mGridView.setScrollbarFadingEnabled(true);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	public void setAdapter(BaseAdapter mAdapter) {
		if (mAdapter == null) {
			setAdapter();
			return;
		}
		mGridView.setAdapter(mAdapter);
	}

	
	public void setPadding(int left,int right,int bottom ,int top){
		mGridView.setPadding(left, top, right, bottom);
	}
	public void setVerNumber(int numColumns) {
		mGridView.setNumColumns(numColumns);
	}

	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
		mGridView.setOnItemClickListener(mOnItemClickListener);
	}
	
	public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClick){
		mGridView.setOnItemLongClickListener(mOnItemLongClick);
	}
	
	

	public void setLayoutParams(int top, int left, int right, int bottom) {
		//
		// LinearLayout.LayoutParams mLayoutParams =
		// (android.widget.LinearLayout.LayoutParams)
		// mGridView.getLayoutParams();
		// mLayoutParams.topMargin = top;
		// mGridView.setLayoutParams(mLayoutParams);
		//
	}

	boolean isGirdCanScroll = false;

	public void setIsGirdCanScroll(boolean isGirdCanScroll) {
		this.isGirdCanScroll = isGirdCanScroll;
	}

	public void setBackGroudResource(int backGround) {
		mGridView.setBackgroundResource(backGround);
	}

	class MyGirdView extends GridView {

		public MyGirdView(Context context) {
			super(context);
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			if (ev.getAction() == MotionEvent.ACTION_MOVE) {
				if (!isGirdCanScroll) {
					return true;
				}
			}
			return super.dispatchTouchEvent(ev);
		}

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

			int expandSpec = MeasureSpec.makeMeasureSpec(
					Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);
		}

	}

}