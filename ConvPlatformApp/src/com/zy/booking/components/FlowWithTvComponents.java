package com.zy.booking.components;

import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zy.booking.R;
import com.zy.booking.util.DataTools;
import com.zy.booking.util.DrawableUtils;
import com.zy.booking.wedget.FlowLayout;

public class FlowWithTvComponents extends BaseComponents {

	FlowLayout mFlowLayout;

	public interface OnItemFlowViewClickListener {
		public void onItemClickListener(String name);
	}

	public FlowWithTvComponents(Context mContext) {
		super(mContext);
	}

	OnItemFlowViewClickListener mOnItemOncListener;

	public void setOnItemClickListener(
			OnItemFlowViewClickListener mOnItemOncListener) {
		this.mOnItemOncListener = mOnItemOncListener;
	}

	@Override
	public void initFatherView() {

		mFlowLayout = new FlowLayout(mContext);
		mFlowLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		mFatherView = mFlowLayout;
	}

	public void addItems(JsonArray mJsonArray){
		int layoutPadding = DataTools.dip2px(mContext, 18);
		mFlowLayout.setPadding(layoutPadding, layoutPadding, layoutPadding,
				layoutPadding);
		mFlowLayout.setHorizontalSpacing(layoutPadding);
		mFlowLayout.setVerticalSpacing(layoutPadding);
		int textPaddingV = DataTools.dip2px(mContext, 6);
		int textPaddingH = DataTools.dip2px(mContext, 7);
		int backColor = 0xffcecece;
		int radius = DataTools.dip2px(mContext, 5);
		// 代码动态创建一个图片
		GradientDrawable pressDrawable = DrawableUtils.createDrawable(
				backColor, backColor, radius);
		Random mRdm = new Random();
		
		
		for (int i = 0; i < mJsonArray.size(); i++) {
			TextView tv = new TextView(mContext);
			// 随机颜色的范围0x202020~0xefefef
//			int red = 32 + mRdm.nextInt(208);
//			int green = 32 + mRdm.nextInt(208);
//			int blue = 32 + mRdm.nextInt(208);
//			int color = 0xff000000 | (red << 16) | (green << 8) | blue;
			// 创建背景图片选择器
			GradientDrawable normalDrawable = DrawableUtils.createDrawable(
					Color.WHITE, backColor, radius);
			StateListDrawable selector = DrawableUtils.createSelector(
					normalDrawable, pressDrawable);
			tv.setBackgroundDrawable(selector);
			final JsonObject mJsonObj = (JsonObject) mJsonArray.get(i);
			final String name = JsonUtil.getAsString(mJsonObj, "name");
			tv.setText(name);
			tv.setTextColor(mContext.getResources().getColor(R.color.main_red));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(textPaddingH, textPaddingV, textPaddingH,
					textPaddingV);
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(mOnItemOncListener!=null){
						mOnItemOncListener.onItemClickListener(name);
					}
				}
			});
					
			mFlowLayout.addView(tv);
		}
		
		
	}
}
