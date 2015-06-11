package com.bosi.chineseclass.han.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bosi.chineseclass.han.db.GameIconInfo;


public class IconUtils {

    public static int getDrawaleIdFromName(Context context,String iconName){
        int id = -1;
        try {
            id = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        } catch (Exception e) {
        }
        return id;
    }

    public static int getIconDrawableId(Context context,List<GameIconInfo> iconList, int position) {
        String iconName = iconList.get(position).getIconPath();
        int id = -1;
        id = getDrawaleIdFromName(context, iconName);
        return id;
    }

    public static Bitmap getImageFromAssetsFile(Context context,String fileName){
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        InputStream is = null;
        try {
            is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return image;
    }

}
