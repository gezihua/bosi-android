package com.zy.booking.activitys;

import java.util.ArrayList;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.R;
import com.zy.booking.components.CategorysGirdComponents;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.json.ModelUploadJsonData;
import com.zy.booking.modle.WaterFallAdapter;
import com.zy.booking.modle.WaterFallAdapter.IWaterFallImageClickCallBack;
import com.zy.booking.struct.OnHttpActionListener;

@ContentView(R.layout.fragment_layout_common)
public class WaterFallImagesActivity extends BaseActivity implements OnHttpActionListener{

	@ViewInject(R.id.ll_container_body)
	LinearLayout mLayoutBody;
	
	CategorysGirdComponents mCategorysGirdComponents;
	
	WaterFallAdapter mWaterFallAdapter;
	
	String [] mArrayUrl;
	
	String [] imgIds;
	
	public static final String TAG_URLS= "tag-urls";
	
	public static final String TAG_ALBUMID = "albumId";
	
	public static final String TAG_IDS ="tag-ids";
	
	public static final String TAG_MODELID = "modelId";
	
	
	ArrayList<String> mImageList;
	@ViewInject(R.id.muliti_image_select_drawerlayout)
	DrawerLayout mDrawerLayout;
	
	//当前相册的id
	String albumId ;
	
	String modelId;
	
	@ViewInject(R.id.headactionbar)
	View mViewHead;
	public static final String TAG_ISMODIFY = "isModify" ;
	private boolean isModify =false ;
	HeadLayoutComponents mLayoutComponent;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		isModify = getIntent().getBooleanExtra(TAG_ISMODIFY,false);
		mCategorysGirdComponents = new CategorysGirdComponents(null,this);
		mCategorysGirdComponents.setVerNumber(3);
		mCategorysGirdComponents.setLayoutParams(5, 5, 5, 5);
	  
	    mArrayUrl = getIntent().getStringArrayExtra(TAG_URLS);
	    albumId = getIntent().getStringExtra(TAG_ALBUMID);
	    imgIds  = getIntent().getStringArrayExtra(TAG_IDS);
	    modelId =  getIntent().getStringExtra(TAG_MODELID);
	    mImageList = new ArrayList<String>(Arrays.asList(mArrayUrl));
	    mLayoutBody.addView(mCategorysGirdComponents.getView());
	    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
	    
	    
	    mWaterFallAdapter= new WaterFallAdapter(this,mImageList);
	    mCategorysGirdComponents.setAdapter(mWaterFallAdapter);
	    
	    mWaterFallAdapter.setCallBack(new IWaterFallImageClickCallBack() {
			
			@Override
			public void onDeleteClick(int position) {
				sendDeleteItemData(imgIds[position],position);
			}

			@SuppressLint("NewApi")
			@Override
			public void onClickItem(int position,View mView) {
				 ActivityOptionsCompat options =
						 
						 ActivityOptionsCompat.makeScaleUpAnimation(mView, 0, 0, 0, 0);
				Intent mIntent = new Intent(mContext,ImageDitalActivity.class);
				mIntent.putExtra(ImageDitalActivity.TAG_INTENT_FILEPATH, mArrayUrl);
				mIntent.putExtra(ImageDitalActivity.TAG_POSITION, position);
				mContext.startActivity(mIntent,options.toBundle());
			}
		});
	    
	    mLayoutComponent = new HeadLayoutComponents(this, mViewHead);
	    mLayoutComponent.setDefaultLeftCallBack(true);
	    mLayoutComponent.setTextMiddle("相册明细", -1);
	    if(isModify)
	    mLayoutComponent.setTextRight("添加", -1);
	    mLayoutComponent.setRightOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent mIntent = new Intent(mContext,UpLoadImageToAlbumActivity.class);
				mIntent.putExtra(UpLoadImageToAlbumActivity.TAG_ALBUMID, albumId);
				mContext.startActivity(mIntent);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void sendDeleteItemData(String imageId,int position){
		showProgresssDialog();
		sendData(ModelUploadJsonData.removeAlbumsImageNameValuePairs(albumId, imageId), AppDefine.URL_ALBUM_REMOVEIMG, this, position);
	}

	@Override
	public void onHttpError(Exception e, String msg, int requestCode) {
		dismissProgressDialog();
	}

	@Override
	public void onDecoded(String reason, boolean isSuccess,
			JsonObject mJsonResult, JsonArray mLists, int resultCode) {
		if(isSuccess){
			removeImageDital(resultCode);
		}
	}
	
	private void removeImageDital(int position){
		dismissProgressDialog();
		//Exception List remove andrid 还没有实现该方法 会报异常
		mImageList.remove(position);
		mWaterFallAdapter.changeDataSource(mImageList);
	}
	
	
}
