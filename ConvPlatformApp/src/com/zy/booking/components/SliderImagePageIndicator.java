package com.zy.booking.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.R;
import com.zy.booking.activitys.ImageDitalActivity;
import com.zy.booking.wedget.viewimage.Animations.DescriptionAnimation;
import com.zy.booking.wedget.viewimage.Animations.SliderLayout;
import com.zy.booking.wedget.viewimage.SliderTypes.BaseSliderView;
import com.zy.booking.wedget.viewimage.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.zy.booking.wedget.viewimage.SliderTypes.TextSliderView;

public class SliderImagePageIndicator extends BaseComponents implements
		OnSliderClickListener {
    public SliderImagePageIndicator(LayoutInflater mLayoutInflater,
			Context mContext) {
		super(mLayoutInflater, mContext);
	}

	@ViewInject(R.id.slider)
	SliderLayout mSliderLayout;
    


	@Override
	public void initFatherView() {
		mFatherView =mLayoutInflater.inflate(
				R.layout.head_slidelayout, null);
		ViewUtils.inject(this,mFatherView);
		initData(getDefaultMapData());
	}

	ArrayList<String> mArrayPath ;
	@SuppressLint("ClickableViewAccessibility")
	public void initData(Map<String, String> datas) {
		mSliderLayout.removeAllSliders();
		mArrayPath= new ArrayList<String>();
		for (String name : datas.keySet()) {
			mArrayPath.add(datas.get(name));
			TextSliderView textSliderView = new TextSliderView(mContext);
			textSliderView.setOnSliderClickListener(this);
			textSliderView.description(name).image(datas.get(name));
			textSliderView.getBundle().putString("extra", name);
			mSliderLayout.addSlider(textSliderView);
		}
		
		mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
		mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
		mSliderLayout.setCustomAnimation(new DescriptionAnimation());
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {

		
		Intent mIntent = new Intent(mContext,ImageDitalActivity.class);
		mIntent.putExtra(ImageDitalActivity.TAG_INTENT_FILEPATH,(String [])mArrayPath.toArray(new String[mArrayPath.size()]));
		mContext.startActivity(mIntent);
	
	
	}
	
	private Map<String ,String> getDefaultMapData(){
		HashMap<String, String> url_maps = new HashMap<String, String>();
		url_maps.put("美美的", "http://img3.redocn.com/20100325/20100325_c5033d6e061af716f6e5nDIxjCxSqP6X.jpg");
		url_maps.put("美美的1", "http://pic9.nipic.com/20100811/2029588_192113002712_2.jpg");
		url_maps.put("美美的2", "http://pic9.nipic.com/20100811/2029588_192316007289_2.jpg");
		return url_maps;
	}

}
