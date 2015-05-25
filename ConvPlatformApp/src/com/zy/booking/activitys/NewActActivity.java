package com.zy.booking.activitys;

import java.io.File;



import java.util.ArrayList;
import java.util.List;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.components.CategorysGirdComponents;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.control.LocationCtrol;
import com.zy.booking.control.LocationCtrol.OnLocationChangedListener;
import com.zy.booking.db.HistoryDbBean;
import com.zy.booking.json.DecodeResult;
import com.zy.booking.json.IDecodeJson;
import com.zy.booking.json.IspUpLoadJsonData;
import com.zy.booking.modle.ServicesListAdapter;
import com.zy.booking.util.BaseTools;
import com.zy.booking.util.MyThumbnailUtils;
import com.zy.booking.util.PreferencesUtils;
import com.baidu.location.BDLocation;
import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qiniu.utils.FileUri;
import com.sromku.simple.storage.Storage;

@ContentView(R.layout.activity_actnew)
public class NewActActivity extends BaseActivity {

	@ViewInject(R.id.headactionbar)
	View mHeadView;
	HeadLayoutComponents mLayoutComponents;
	@ViewInject(R.id.bt_sendinfo)
	Button mBtSendInfo;

	private PopupWindow pop = null;
	private LinearLayout ll_popup;

	private GridAdapter adapter;

	private CategorysGirdComponents mGridView;

	@ViewInject(R.id.ll_container_images)
	private LinearLayout ll_layoutPicBody;

	private List<File> mListDatas;

	@ViewInject(R.id.et_actnewtitle)
	private EditText mEtTitle;

	@ViewInject(R.id.et_actnewcontact)
	private EditText mEtContact;

	@ViewInject(R.id.et_actnewdiscribe)
	private EditText mEtDiscribe;

	@ViewInject(R.id.et_actnewplace)
	private EditText mPlace;

	@ViewInject(R.id.et_acttag)
	private EditText mTag;

	@ViewInject(R.id.ll_title)
	private View mlltitle;
	@ViewInject(R.id.ll_actperphone)
	private View mllContact;
	@ViewInject(R.id.ll_actinstro)
	private View mllIntro;
	@ViewInject(R.id.ll_actplacename)
	private View mllPlace;

	@ViewInject(R.id.ll_actbiaoqian)
	private View mllTag;

	LocationCtrol mLocationControl;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mLayoutComponents = new HeadLayoutComponents(this, mHeadView);
		mLayoutComponents.setTextMiddle("发布", -1);

		String phone = PreferencesUtils.getString(this, "account");
		mEtContact.setText(phone);

		mLayoutComponents.setDefaultLeftCallBack(true);
		// bug fix 解决页面返回的时候白屏的问题
		getWindow().setBackgroundDrawable(null);
		mListDatas = new ArrayList<File>();
		init();

		mLocationControl = new LocationCtrol(this);
		mLocationControl.onCreate(arg0);
		mLocationControl
				.setLocationChangedListener(new OnLocationChangedListener() {

					@Override
					public void onLocationChanged(BDLocation mBdLocation) {
						if (mBdLocation != null) {
							String address = mBdLocation.getAddrStr();
							mPlace.setText(address);
							lng = mBdLocation.getLongitude() + "";
							lat = mBdLocation.getLatitude() + "";
							mLocationControl.onDestroy();
						}
					}
				});

	}

	@OnClick(R.id.bt_sendinfo)
	public void actionSendInfo(View mView) {

		if (!filterSend())
			return;
		if (mListFilePath == null || mListFilePath.size() < 2) {
			Toast.makeText(this, "请至少选择两张图片", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			for (String mFilePath : mListFilePath) {
				mListDatas.add(new File(mFilePath));
			}
		} catch (Exception e) {
		}

		sendData();
	}

	// 回收相关数据
	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mListBitmapLists != null) {
			for (Bitmap mBitmap : mListBitmapLists) {
				if (mBitmap != null && !mBitmap.isRecycled())
					mBitmap.recycle();
			}

			mListBitmapLists.clear();
			mListFilePath.clear();
		}
		mLocationControl.onDestroy();
		System.gc();
	}

	String title;
	String contact;
	String discribe;
	String place;
	String lat = "";
	String lng = "";
	String tag;

	private boolean filterSend() {
		title = mEtTitle.getText().toString().trim();
		contact = mEtContact.getText().toString().trim();
		discribe = mEtDiscribe.getText().toString().trim();
		place = mPlace.getText().toString().trim();
		tag = mTag.getText().toString().trim();

		if (TextUtils.isEmpty(title)) {
			playYoYo(mlltitle);
			return false;
		}
		if (TextUtils.isEmpty(contact)) {
			playYoYo(mllContact);
			return false;
		}
		if (TextUtils.isEmpty(discribe)) {
			playYoYo(mllIntro);
			return false;
		}
		if (TextUtils.isEmpty(place)) {
			playYoYo(mllPlace);
			return false;
		}

		if (TextUtils.isEmpty(tag)) {
			playYoYo(mllTag);
			return false;
		}

		return true;
	}

	public void sendData() {
		showProgresssDialog();
		CpApplication.getApplication().mHttpPack.sendDataAndImgs(
				IspUpLoadJsonData.getUploadDataNameValuePair(title, contact,
						discribe, place, lng, lat, tag),
				AppDefine.BASE_URL_ADDSERVICE, mListDatas,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						dismissProgressDialog();
						DecodeResult.decoResult(responseInfo.result,
								new DecodeUpLoad());
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(mContext, msg, Toast.LENGTH_SHORT)
								.show();
						dismissProgressDialog();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						super.onLoading(total, current, isUploading);
					}

				});

	}

	private void init() {

		pop = new PopupWindow(this);

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				createCurrentFilePath();
				Intent intent = new Intent();
				/* 开启Pictures画面Type设定为image */
				intent.setType("image/*");
				/* 使用Intent.ACTION_GET_CONTENT这个Action */
				intent.setAction(Intent.ACTION_GET_CONTENT);
				/* 取得相片后返回本画面 */
				startActivityForResult(intent, TAKE_FROMLOCAL);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

		mGridView = new CategorysGirdComponents(null, this);
		mGridView.setVerNumber(4);
		ll_layoutPicBody.addView(mGridView.getView());

		adapter = new GridAdapter(this);
		mGridView.setAdapter(adapter);
		adapter.update();

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (arg2 == mListBitmapLists.size()) {
					ll_popup.startAnimation(AnimationUtils.loadAnimation(
							NewActActivity.this, R.anim.activity_translate_in));
					pop.showAtLocation(ll_layoutPicBody, Gravity.BOTTOM, 0, 0);
					BaseTools.closeInputManager(mContext);
				}

			}
		});
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			adapter.notifyDataSetChanged();
		}

		public int getCount() {
			if (mListBitmapLists.size() == AppDefine.MAX_UPLOADPICS) {
				return AppDefine.MAX_UPLOADPICS;
			}
			return (mListBitmapLists.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == mListBitmapLists.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == AppDefine.MAX_UPLOADPICS) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(mListBitmapLists.get(position));
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	private static final int TAKE_PICTURE = 0x000001;
	private static final int TAKE_ADDRESS = 0x000002;
	private static final int TAKE_FROMLOCAL = 0x000003;

	ArrayList<String> mListFilePath = new ArrayList<String>();
	ArrayList<Bitmap> mListBitmapLists = new ArrayList<Bitmap>();

	String mCurrentPhotoPath;
	String mCurrentFileName;

	public void photo() {
		if (mListFilePath == null)
			mListFilePath = new ArrayList<String>();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File mFilePhoto = createCurrentFilePath();
		Uri imageUri = Uri.fromFile(mFilePhoto);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, TAKE_PICTURE);
	}

	private File createCurrentFilePath() {
		Storage mStorage = CpApplication.getApplication().mStorage;

		mCurrentFileName = System.currentTimeMillis() + ".jpg";
		File mFilePhoto = mStorage.getFile(AppDefine.APP_GLOBLEFILEPATH,
				mCurrentFileName);

		mCurrentPhotoPath = mFilePhoto.getAbsolutePath();

		return mFilePhoto;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case TAKE_PICTURE:
			if (mListBitmapLists == null)
				mListBitmapLists = new ArrayList<Bitmap>();
			if (mListBitmapLists.size() < AppDefine.MAX_UPLOADPICS
					&& resultCode == RESULT_OK) {
				onActivityResult(mCurrentPhotoPath);
			}
			break;

		case TAKE_ADDRESS:
			String address = data.getStringExtra("address");
			lng = data.getStringExtra("lng");
			lat = data.getStringExtra("lat");
			mPlace.setText(address);
			mPlace.setTag(lng + "|" + lat);
			break;

		case TAKE_FROMLOCAL:
			Uri uri = data.getData();
			File mFileFromUri = FileUri.getFile(mContext, uri);

			String filePath = mFileFromUri.getAbsolutePath();
			onActivityResult(filePath);

			break;

		}

	}

	private void onActivityResult(String filePath) {
		try {
			Bitmap mBitmapThumbnail = MyThumbnailUtils
					.createImageThumbnail(filePath);
			boolean isCreateThumbFileSuccess = CpApplication.getApplication().mStorage
					.createFile(AppDefine.APP_GLOBLEFILEPATH, mCurrentFileName,
							mBitmapThumbnail);
			if (isCreateThumbFileSuccess) {
				mBitmapThumbnail.recycle();
				Bitmap mBitmapThumbShowInPanel = MyThumbnailUtils
						.smallThumbnail(mCurrentPhotoPath);
				mListBitmapLists.add(mBitmapThumbShowInPanel);
				mListFilePath.add(mCurrentPhotoPath);
				adapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			showToastShort("图片处理失败请重新选择" + e.getMessage());
		}

	}

	/**
	 * 保存到已经发布的历史数据中
	 * 
	 * */
	public void makeHistoryData(String url, String serviceId) {
		JsonObject mJsonData = new JsonObject();
		mJsonData.addProperty("id", serviceId);
		mJsonData.addProperty(ServicesListAdapter.ADDRESS, place);
		mJsonData.addProperty(ServicesListAdapter.INTRODUCTION, discribe);
		mJsonData.addProperty(ServicesListAdapter.NAME, title);
		mJsonData.addProperty(ServicesListAdapter.TAGS, title);
		mJsonData.addProperty(ServicesListAdapter.IMG0, url);
		HistoryDbBean mHistoryBean = new HistoryDbBean();
		mHistoryBean.isAskd = "1";
		mHistoryBean.contect = mJsonData.toString();
		mHistoryBean.serviceId = serviceId;
		CpApplication.getApplication().mDbManager
				.insertHistoryData(mHistoryBean);
	}

	class DecodeUpLoad implements IDecodeJson {

		@Override
		public void onDecoded(String reason, boolean isSuccess,
				JsonObject mJsonResult, JsonArray mLists) {
			if (!isSuccess) {
				Toast.makeText(mContext, reason, Toast.LENGTH_SHORT).show();
			} else {
				String url = JsonUtil.getAsString(mJsonResult,
						ServicesListAdapter.IMG0);
				String spid = JsonUtil.getAsString(mJsonResult,
						ServicesListAdapter.SPID);
				makeHistoryData(url, spid);
				Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
				finish();
			}

		}
	}

	@OnClick(R.id.iv_hint_mapselectplace)
	public void actionMapSelected(View view) {
		// 地图选点功能
		Intent mIntent = new Intent(this, SampleHolderActivity.class);
		mIntent.putExtra("tag", SampleHolderActivity.TAG_MAPSEARCH);
		startActivityForResult(mIntent, TAKE_ADDRESS);
	}

}
