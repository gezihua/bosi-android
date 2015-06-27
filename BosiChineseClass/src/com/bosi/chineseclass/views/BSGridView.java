package com.bosi.chineseclass.views;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.GridView;

public class BSGridView extends GridView{

	boolean isGirdCanScroll;
	public BSGridView(Context context) {
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
