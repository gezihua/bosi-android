package com.bosi.chineseclass;

import android.content.Context;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;

/**
 * Created by youyou on 2015/6/10.
 */
public class XutilImageLoader {
/**
*用于 imageloader 的异步加载 刚开始我们先配置好默认的显示数据
* */

    private final int DRAWABLE_LOADFILE = R.drawable.ic_launcher;
    private final int DRAWABLE_LOADING  = R.drawable.ic_launcher;
    BitmapUtils mBitmapUtils;
    BitmapDisplayConfig mConfig;
    HttpUtils mHttpUtils;
    Context mContext ;
    public XutilImageLoader(Context mContext){
    	this.mContext = mContext ;
        init();
    }
    
    private void init(){
    String folderPath = AppDefine.FilePathDefine.APP_GLOBLEFILEPATH+"/"+"cache";
    BSApplication.getInstance().mStorage.createDirectory(folderPath,false);
    mBitmapUtils = new BitmapUtils(mContext,BSApplication.getInstance().mStorage.getFile(folderPath).getAbsolutePath());
    mBitmapUtils.configDiskCacheEnabled(true);
    mBitmapUtils.configDefaultLoadFailedImage(DRAWABLE_LOADFILE);
    mBitmapUtils.configDefaultLoadingImage( DRAWABLE_LOADING);
    }
    
    public BitmapUtils getBitmapFactory(){
    	return mBitmapUtils;
    }




}
