 
package com.zy.booking.components;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sromku.simple.storage.Storage;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.activitys.imageloader.MuliImgSelectActivity;
import com.zy.booking.fragments.BaseFragment;
import com.zy.booking.util.BaseTools;
import com.zy.booking.util.MyThumbnailUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class SelectImageGirdComponents extends BaseComponents {

    BaseActivity mActivity;

    ViewGroup mViewHolder;

    public interface OnPicSelectedListener {
        public void onPicSelectedListener(String mFilePath);
    }

    OnPicSelectedListener mOnPicSelectedListener;

    public void setOnPicSelectedListener(OnPicSelectedListener mOnPicSelectedListener) {
        this.mOnPicSelectedListener = mOnPicSelectedListener;
    }

    private int maxSelectPics = 4;

    public SelectImageGirdComponents(Context mContext) {
        super(mContext);
        mActivity = (BaseActivity) mContext;
    }

    public void setMaxPicNums(int numpics) {
        this.maxSelectPics = numpics;
    }

    BaseFragment mBaseFragment;

    public SelectImageGirdComponents(Context mContext, ViewGroup mViewHolder) {
        super(mContext);
        mActivity = (BaseActivity) mContext;
        this.mViewHolder = mViewHolder;

        initPopuWidow();
    }

    public SelectImageGirdComponents(BaseFragment mBaseFragment, Context mContext,
            ViewGroup mViewHolder) {
        super(mContext);
        this.mBaseFragment = mBaseFragment;
        mActivity = (BaseActivity) mContext;
        this.mViewHolder = mViewHolder;

        initPopuWidow();
    }

    @Override
    public void initFatherView() {
        initView();
        mFatherView = mGridView.getView();
    }

    private void initView() {
        mGridView = new CategorysGirdComponents(null, mContext);
        mGridView.setVerNumber(4);

        adapter = new GridAdapter(mContext);
        mGridView.setAdapter(adapter);
        adapter.update();

        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {

                if (arg2 == mListBitmapLists.size()) {
                    actionShowPopuWindow();
                }

            }
        });
    }

    public void actionShowPopuWindow() {
        ll_popup.startAnimation(AnimationUtils.loadAnimation(
                mActivity, R.anim.activity_translate_in));
        pop.showAtLocation(mViewHolder, Gravity.BOTTOM, 0, 0);
        BaseTools.closeInputManager(mActivity);

    }

    private void initPopuWidow() {
        pop = new PopupWindow(mActivity);

        View view = LayoutInflater.from(mActivity).inflate(
                R.layout.item_popupwindows, null);

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
                Intent intent = new Intent(
                       mContext,MuliImgSelectActivity.class);
                intent.putExtra(MuliImgSelectActivity.TAG_MAXPICSELECTED, mOnPicSelectedListener==null?AppDefine.MAX_UPLOADPICS:1);
                if (mBaseFragment != null) {
                    mBaseFragment.startActivityForResult(intent, TAKE_FROMLOCAL);
                } else {
                    mActivity.startActivityForResult(intent, TAKE_FROMLOCAL);
                }
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

    }

    private static final int TAKE_PICTURE = 0x000001;
    private static final int TAKE_FROMLOCAL = 0x000003;

    public ArrayList<String> mListFilePath = new ArrayList<String>();
    public ArrayList<Bitmap> mListBitmapLists = new ArrayList<Bitmap>();

    String mCurrentPhotoPath;
    String mCurrentFileName;

    private GridAdapter adapter;

    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private CategorysGirdComponents mGridView;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode !=Activity.RESULT_OK){
            return;
        }
        switch (requestCode) {
            case TAKE_PICTURE:
                if (mListBitmapLists == null)
                    mListBitmapLists = new ArrayList<Bitmap>();
                if (mListBitmapLists.size() < maxSelectPics) {
                    if (mOnPicSelectedListener != null) {
                        mOnPicSelectedListener.onPicSelectedListener(mCurrentPhotoPath);
                        return;
                    }
                    onActivityResultForCamera(mCurrentPhotoPath);
                }
                break;

            case TAKE_FROMLOCAL:
                String [] mFileList = data.getStringArrayExtra(MuliImgSelectActivity.TAG_IMAGESELECTED);
                if(mFileList ==null ||mFileList.length==0){
                    return;
                }
                if (mOnPicSelectedListener != null) {
                    mOnPicSelectedListener.onPicSelectedListener(mFileList[0]);
                    return;
                }
                
                onActivityResult(mFileList);
                break;

        }
    }

    /**
     * Try to return the absolute file path from the given Uri
     * 
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[] {
                        ImageColumns.DATA
                    }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private void onActivityResultForCamera(String filePath) {
        try {
            Bitmap mBitmapThumbnail = MyThumbnailUtils
                    .createImageThumbnail(filePath);
            boolean isCreateThumbFileSuccess = CpApplication.getApplication().mStorage
                    .createFile(AppDefine.APP_GLOBLEFILEPATH, mCurrentFileName,
                            mBitmapThumbnail);
            if (isCreateThumbFileSuccess) {
                mBitmapThumbnail.recycle();
                Bitmap mBitmapThumbShowInPanel = MyThumbnailUtils
                        .createImageThumbnail(mCurrentPhotoPath, 200);
                mListBitmapLists.add(mBitmapThumbShowInPanel);
                mListFilePath.add(mCurrentPhotoPath);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            mActivity.showToastShort("图片处理失败请重新选择");
        }
    }
    
    private void onActivityResultForLocal(String filePath) {
        createCurrentFilePath();
        try {
            Bitmap mBitmapThumbnail = MyThumbnailUtils
                    .createImageThumbnail(filePath);
            boolean isCreateThumbFileSuccess = CpApplication.getApplication().mStorage
                    .createFile(AppDefine.APP_GLOBLEFILEPATH, mCurrentFileName,
                            mBitmapThumbnail);
            if (isCreateThumbFileSuccess) {
                mBitmapThumbnail.recycle();
                Bitmap mBitmapThumbShowInPanel = MyThumbnailUtils
                        .createImageThumbnail(mCurrentPhotoPath, 200);
                mListBitmapLists.add(mBitmapThumbShowInPanel);
                mListFilePath.add(mCurrentPhotoPath);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            mActivity.showToastShort("图片处理失败请重新选择");
        }
    }
    
    
    private void onActivityResult(String[]  mList){
        if(mList==null)return;
        
        for(String mPicSelected:mList){
            onActivityResultForLocal(mPicSelected);
        }
    }

    public void photo() {
        if (mListFilePath == null)
            mListFilePath = new ArrayList<String>();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File mFilePhoto = createCurrentFilePath();
        Uri imageUri = Uri.fromFile(mFilePhoto);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        if (mBaseFragment != null) {
            mBaseFragment.startActivityForResult(intent, TAKE_PICTURE);
        } else {
            mActivity.startActivityForResult(intent, TAKE_PICTURE);
        }

    }

    private File createCurrentFilePath() {
        Storage mStorage = CpApplication.getApplication().mStorage;

        mCurrentFileName = System.currentTimeMillis() + ".jpg";
        File mFilePhoto = mStorage.getFile(AppDefine.APP_GLOBLEFILEPATH,
                mCurrentFileName);

        mCurrentPhotoPath = mFilePhoto.getAbsolutePath();

        return mFilePhoto;
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
            if (mListBitmapLists == null)
                return 0;
            if (mListBitmapLists.size() == maxSelectPics) {
                return maxSelectPics;
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
                        mActivity.getResources(),
                        R.drawable.icon_addpic_unfocused));
                if (position == maxSelectPics) {
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

}
