package com.zy.booking.activitys;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.R;

@ContentView(R.layout.layout_pic_dital)
public class ImageDitalActivity extends BaseActivity {

	@ViewInject(R.id.ll_container_body)
	LinearLayout mLinearLayout;
	
	@ViewInject(R.id.ll_container_indicator)
	LinearLayout mLayoutIndicator;

	private PhotoViewAttacher mAttacher;

	public static final String TAG_INTENT_FILEPATH = "filepath";
	
	public static final String TAG_POSITION = "position";

	Bitmap mBitmapDital;
	
	
	private ViewPager mViewPager;
	
	
	private int mIndexPosition ;
	CirclePageIndicator mPageIndicator;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		// mHeadLayoutComponents = new HeadLayoutComponents(this,
		// mViewActionBar);
		// mHeadLayoutComponents.setTextMiddle("图片明细", -1);

		mLinearLayout.setBackgroundColor(Color.BLACK);
		imagePaths = getIntent().getStringArrayExtra(TAG_INTENT_FILEPATH);
		mIndexPosition = getIntent().getIntExtra(TAG_POSITION, 0);
		mViewPager = new MyViewPagger(this);
		mViewPager.setAdapter(new SamplePagerAdapter());
		mViewPager.setCurrentItem(mIndexPosition);
		mLinearLayout.addView(mViewPager);
		
		
		// mAttacher.setScaleType(scaleType)
		mPageIndicator= new CirclePageIndicator(mContext);
		mPageIndicator.setViewPager(mViewPager);
		
		mLayoutIndicator.addView(mPageIndicator);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBitmapDital != null && mBitmapDital.isRecycled()) {
			mBitmapDital.recycle();
			System.gc();
		}
	}

	class MyViewPagger extends ViewPager {

		public MyViewPagger(Context context) {
			super(context);
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent ev) {
			try {
				return super.onInterceptTouchEvent(ev);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	 String [] imagePaths;
	
	 class SamplePagerAdapter extends PagerAdapter {


		@Override
		public int getCount() {
			return imagePaths.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View mViewBase = LayoutInflater.from(mContext).inflate(R.layout.layout_loadingimg, null);
			
			final PhotoView photoView = (PhotoView) mViewBase.findViewById(R.id.pv_imgscal);
			final ProgressBar mProgressBar = (ProgressBar) mViewBase.findViewById(R.id.pb_img_loading);
			CpApplication.getApplication().mBitmapManager.mBitmapUtils.display(photoView, imagePaths[position],new BitmapLoadCallBack<View>() {

				@Override
				public void onLoadCompleted(View container, String uri,
						Bitmap bitmap, BitmapDisplayConfig config,
						BitmapLoadFrom from) {
					mProgressBar.setVisibility(View.GONE);
					photoView.setImageBitmap(bitmap);
				}

				@Override
				public void onLoadFailed(View container, String uri,
						Drawable drawable) {
					mProgressBar.setVisibility(View.GONE);
					photoView.setBackgroundResource(R.drawable.abaose);
				}

				@Override
				public void onLoading(View container, String uri,
						BitmapDisplayConfig config, long total, long current) {
					super.onLoading(container, uri, config, total, current);
				}
				
			});
			
			container.addView(mViewBase, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return mViewBase;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

}
