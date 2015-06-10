
package com.bosi.chineseclass.su.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

public class MyVolley {
    private static MyVolley sMyVolley;
    private Context mContext;
    RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private MyVolley(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        mRequestQueue = Volley.newRequestQueue(mContext);
        mRequestQueue.start();
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(mContext));

    }

    public static MyVolley getInstance(Context context) {
        if (sMyVolley == null) {
            sMyVolley = new MyVolley(context);
        }
        return sMyVolley;
    }

    public void loadImage(final String url, ImageListener imageListener) {
        mImageLoader.get(url, imageListener);
    }

}
