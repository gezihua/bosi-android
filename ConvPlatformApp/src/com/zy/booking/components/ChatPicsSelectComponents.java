package com.zy.booking.components;


import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sromku.simple.storage.Storage;
import com.zy.booking.AppDefine;
import com.zy.booking.CpApplication;
import com.zy.booking.R;

public class ChatPicsSelectComponents extends BaseComponents{

	
    public static int REQUEST_CODE_CAMERA = 101;
    
    public static int REQUEST_CODE_LOCAL =102;
    
	
	CategorysGirdComponents mCategoryComponents;
	
	public ChatPicsSelectComponents(Context mContext) {
		super(mContext);
	}

	
	View mViewCamera;
	
	View mViewChoiceLocal ;
	@Override
	public void initFatherView() {
		
		mFatherView = LayoutInflater.from(mContext).inflate(R.layout.layout_container_chat_selectimg, null);
		
	    mViewCamera = mFatherView.findViewById(R.id.view_chat_choiceimg_camera);
	    mViewChoiceLocal = mFatherView.findViewById(R.id.view_chat_choiceimg_local);
	    actionCamerea();
	    actionLocal();
		ImageView mImageCamera = (ImageView) mViewCamera.findViewById(R.id.iv_itemivt);
		mImageCamera.setBackgroundResource(R.drawable.icon_chat_image_camera);
		
		TextView mTvCamera = (TextView) mViewCamera.findViewById(R.id.tv_itemivt);
		mTvCamera.setText("拍照");
		
		
		TextView mTvlocal = (TextView) mViewChoiceLocal.findViewById(R.id.tv_itemivt);
		mTvlocal.setText("相册");
		
		ImageView mImageLocal = (ImageView) mViewChoiceLocal.findViewById(R.id.iv_itemivt);
		mImageLocal.setBackgroundResource(R.drawable.icon_chat_image_pics);
	}
	
	public void actionCamerea(){
		mViewCamera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				photo();
			}
		});
	}
	
	public void actionLocal(){
		mViewChoiceLocal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				takePicFromLocal();
			}
		});
	}
	
	public String mCurrentPhotoPath;
	public String mCurrentFileName;
	
	public void photo() {
		Activity mActivity = (Activity) mContext;
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File mFilePhoto = createCurrentFilePath();
		Uri imageUri = Uri.fromFile(mFilePhoto);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		mActivity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
	}
	

	private File createCurrentFilePath() {
		Storage mStorage = CpApplication.getApplication().mStorage;

		mCurrentFileName = System.currentTimeMillis() + ".jpg";
		File mFilePhoto = mStorage.getFile(AppDefine.APP_GLOBLEFILEPATH,
				mCurrentFileName);

		mCurrentPhotoPath = mFilePhoto.getAbsolutePath();

		return mFilePhoto;
	}
	
	private void takePicFromLocal(){
		
		Activity mActivity = (Activity) mContext;
		createCurrentFilePath();
		 Intent intent = new Intent(
                 Intent.ACTION_PICK,
                 android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		/* 取得相片后返回本画面 */
		mActivity.startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}
	

}
