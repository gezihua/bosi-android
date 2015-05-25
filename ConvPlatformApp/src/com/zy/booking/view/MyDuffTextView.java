package com.zy.booking.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyDuffTextView extends TextView{

	public MyDuffTextView(Context context){
		super(context, null);
	}
	public MyDuffTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void initDuffTextView(){
		
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}
}
