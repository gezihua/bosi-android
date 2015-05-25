package com.zy.booking.modle.pagefactory;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.zy.booking.components.HeadLayoutComponents;


/**
 * 抽象工厂用于调整对于SampleHolderActivity 的显示
 * 
 * 
 * SampleHolderActivity 提供了一个视图的容器 ，这个容器是可以复用的
 * 通过工厂组装整个页面结构
 * */
public abstract class AbsFactory {
	Context mContext;
	public AbsFactory(Context mContext){
		this.mContext =mContext;
	}

	
	public abstract ArrayList<Fragment> productBody();
	
	public abstract void productMenu(ViewGroup mViewGroupBottom);
	
	
	public void productTopTab(ViewGroup mViewGroupBottom){
	}
	
}
