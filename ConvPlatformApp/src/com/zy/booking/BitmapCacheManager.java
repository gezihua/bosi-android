
package com.zy.booking;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;

public class BitmapCacheManager {

    private final String CACHE = "zy-cache";

    public BitmapUtils mBitmapUtils;

    public BitmapCacheManager(Context mContext) {
        CpApplication.getApplication().mStorage.createDirectory(CACHE);
        mBitmapUtils = new BitmapUtils(mContext, CpApplication.getApplication().mStorage.getFile(
                CACHE).getAbsolutePath());
    }

    public void disPlayImage(ImageView mImage, String filePath) {
        mBitmapUtils.display(mImage, filePath);
    }
    
    public void disPlayWithListener(ImageView mImage, String filePath,
            BitmapLoadCallBack<View> mCallBack) {
        mBitmapUtils.display(mImage, filePath, mCallBack);
    }

    public void clearCache() {
        mBitmapUtils.clearMemoryCache();
        mBitmapUtils.clearDiskCache();
    }

}
