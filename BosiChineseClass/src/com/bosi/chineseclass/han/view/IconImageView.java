package com.bosi.chineseclass.han.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class IconImageView extends ImageView {
    private static final float PRESS_ALPHA = 0.4f;

    public IconImageView(Context context) {
        super(context);
    }

    public IconImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void drawableStateChanged() {
        Thread.dumpStack();
        super.drawableStateChanged();
        if (isPressed()) {
            setAlpha(PRESS_ALPHA);
        } else {
            setAlpha(1f);
        }
    }

}
