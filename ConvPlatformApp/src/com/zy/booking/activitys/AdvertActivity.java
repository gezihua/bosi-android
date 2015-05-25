package com.zy.booking.activitys;


import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.zy.booking.BaseActivity;
import com.zy.booking.R;

public class AdvertActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		ImageView mImageView = new ImageView(this);
		mImageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		mImageView.setScaleType(ScaleType.FIT_XY);
		mImageView.setBackgroundResource(R.drawable.ording);
		setContentView(mImageView);
		
		mImageView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
			    
				finish();
			}
		}, 3000);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
}
