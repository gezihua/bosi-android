package com.bosi.chineseclass.han.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import com.bosi.chineseclass.BSApplication;

public class BosiTextView extends TextView {
    
    private static final float PRESS_ALPHA = 0.4f;

    protected static Map<String, Typeface> mTypefaces;
    final static String typefaceAssetPath = "fonts/bosi.ttf";

    public BosiTextView(final Context context) {
        this(context, null);
    }


    public BosiTextView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public BosiTextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        setTypeFace(context);
    }
    
    protected void setTypeFace(Context context) {
        Typeface typeface = getSystemTypeFace();
        if (typeface != null)
            setTypeface(typeface);
    }

    public static Typeface getSystemTypeFace() {
        if (mTypefaces == null) {
            mTypefaces = new HashMap<String, Typeface>();
        }

        Typeface typeface = null;

        if (mTypefaces.containsKey(typefaceAssetPath)) {
            typeface = mTypefaces.get(typefaceAssetPath);
        } else {
            AssetManager assets = BSApplication.mApplication.getAssets();
            typeface = Typeface.createFromAsset(assets, typefaceAssetPath);
            mTypefaces.put(typefaceAssetPath, typeface);
        }
        return typeface;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (isPressed()) {
            setAlpha(PRESS_ALPHA);
        } else {
            setAlpha(1f);
        }
    }

}
