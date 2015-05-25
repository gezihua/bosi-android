
package com.zy.booking.modle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.util.BubbleImageHelper;
import com.zy.booking.util.Utilities;
import com.zy.booking.util.ViewHolder;

public class AlbumListAdapter extends SampleListAdapter {

    private final String NAME = "name";
    private final String INTRODUCTION = "introduction";

    private final String IMAGELIST = "imageList";
    
    //当前操作的是否是查看 
    boolean isMaster = false;

    public AlbumListAdapter(Context mContext, JsonArray mData) {
        super(mContext, mData);
    }

    public void setIsAlbumMaster(boolean isMaster) {
        this.isMaster = isMaster;
    }

    @Override
    public View getView(int posi, View viewTemp, ViewGroup arg2) {

        if (viewTemp == null) {
            viewTemp = LayoutInflater.from(context).inflate(R.layout.layout_album_listitem, null);
        }

        final JsonObject mJsonData = getItem(posi);
        TextView mTvAlbumName = ViewHolder.get(viewTemp, R.id.tv_album_name);
        TextView mTvAlbuIntro = ViewHolder.get(viewTemp, R.id.tv_album_instro);

        String albumName = JsonUtil.getAsString(mJsonData, NAME);
        String albumIntro = JsonUtil.getAsString(mJsonData, INTRODUCTION);

        JsonArray mJsonArray = JsonUtil.getAsJsonArray(mJsonData, IMAGELIST);

        final ImageView mImageIcon = ViewHolder.get(viewTemp, R.id.iv_album_listitem_face);

        Button mButton = ViewHolder.get(viewTemp, R.id.bt_albums_modify);
        if (!isMaster) {
            mButton.setVisibility(View.GONE);
        }
        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mOnItemClickListener != null)
                {
                    mOnItemClickListener.onModifyClicked(mJsonData);

                }
            }
        });
        if (mJsonArray != null && mJsonArray.size() > 0) {
            JsonObject mUrlObject = (JsonObject) mJsonArray.get(0);
            String mUrl = JsonUtil.getAsString(mUrlObject, "url");
            CpApplication.getApplication().mBitmapManager.disPlayWithListener(mImageIcon, mUrl, new BitmapLoadCallBack<View>() {
				
				@Override
				public void onLoadFailed(View container, String uri, Drawable drawable) {
					
				}
				
				@Override
				public void onLoadCompleted(View container, String uri, Bitmap bitmap,
						BitmapDisplayConfig config, BitmapLoadFrom from) {
					mImageIcon.setImageBitmap(BubbleImageHelper.getInstance(context).getBubbleImageBitmap(bitmap, R.drawable.icon_downbg_album));
				}
				@Override
				public void onPreLoad(View container, String uri,
						BitmapDisplayConfig config) {
					super.onPreLoad(container, uri, config);
				}
			});
        }

        mTvAlbumName.setText(albumName);
        mTvAlbuIntro.setText(albumIntro);
        return viewTemp;
    }

    OnAdapterItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnAdapterItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnAdapterItemClickListener {
        public void onModifyClicked(JsonObject mJson);
    }

}
