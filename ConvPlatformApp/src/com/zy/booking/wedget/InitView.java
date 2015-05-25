package com.zy.booking.wedget;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;


@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class InitView {
    private static InitView initView;


    public static InitView instance() {
        if (initView == null) {
            initView = new InitView();
        }
        return initView;
    }


    /**
     * 设置下拉刷新控件颜色
     * 
     * @param swipeLayout
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void initSwipeRefreshLayout(SwipeRefreshLayout swipeLayout) {
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

  
}
