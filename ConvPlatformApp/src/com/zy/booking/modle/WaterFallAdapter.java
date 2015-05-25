package com.zy.booking.modle;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.zy.booking.R;
import com.zy.booking.util.ViewHolder;

public class WaterFallAdapter extends SampleStructAdapter {

	public WaterFallAdapter(Context mContext, List<? extends Object> mList) {
		super(mContext, mList);
		
		mConfig = new BitmapDisplayConfig();
		mConfig.setLoadingDrawable(context.getResources().getDrawable(
				R.drawable.waterfall_default));
	}

	@Override
	public Object getItem(int args) {
		return super.getItem(args);
	}

	@Override
	public  View getView(final int posi, View viewTemp, ViewGroup arg2) {
		
		if(viewTemp ==null){
			viewTemp = LayoutInflater.from(context).inflate(R.layout.layout_waterfallimage, null);
		}
		String itemUrl = (String) getItem(posi);
		
		final ImageView mImageView = ViewHolder.get(viewTemp, R.id.pv_imgscal);
		final ProgressBar mProgressBar = ViewHolder.get(viewTemp, R.id.pb_img_loading);
		final  TextView mTextView = ViewHolder.get(viewTemp, R.id.tv_delete);
		mTextView.setVisibility(View.GONE);
		
		
		mTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// 执行的是删除单个
				if(mCallBack!=null){ 
					mCallBack.onDeleteClick(posi);
					mTextView.setVisibility(View.GONE);
				}
			}
		});
		
		mImageView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				//显示删除按钮
				mTextView.setVisibility(View.VISIBLE);
				return true;
			}
		});
		
		mImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(mCallBack!=null){ 
					mCallBack.onClickItem(posi,view);
				}
			}
		});
		mBitmapUtils.display(mImageView, itemUrl, new BitmapLoadCallBack<View>() {

			@Override
			public void onLoadCompleted(View container, String uri,
					Bitmap bitmap, BitmapDisplayConfig config,
					BitmapLoadFrom from) {
				mImageView.setImageBitmap(bitmap);
				mProgressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadFailed(View container, String uri,
					Drawable drawable) {
				mProgressBar.setVisibility(View.GONE);
			}
		});
		return viewTemp;
	}
	
	IWaterFallImageClickCallBack mCallBack;
	
	public void setCallBack(IWaterFallImageClickCallBack mCallBack){
		this.mCallBack = mCallBack;
	}
	
	public interface IWaterFallImageClickCallBack{
		public void onDeleteClick(int position);
		public void onClickItem(int position,View mView );
	}

}
