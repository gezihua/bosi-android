package com.zy.booking.util;

import android.view.View;
import android.view.View.OnLongClickListener;

public class PopuWindowUtil {
	
	/**
	 * 每个ITEM中more按钮对应的点击动作
	 * */
	public class popAction implements OnLongClickListener {
		int position;
		View view;
		int fromOrTo;

		public popAction(View view, int position, int fromOrTo) {
			this.position = position;
			this.view = view;
			this.fromOrTo = fromOrTo;
		}

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			int[] arrayOfInt = new int[2];
			// 获取点击按钮的坐标
			v.getLocationOnScreen(arrayOfInt);
			int x = arrayOfInt[0];
			int y = arrayOfInt[1];
			// System.out.println("x: " + x + " y:" + y + " w: " +
			// v.getMeasuredWidth() + " h: " + v.getMeasuredHeight() );
			return true;
		}
	}


}
