package com.bosi.chineseclass.han.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseComponents {
    
    public BaseComponents(LayoutInflater mLayoutInflater,Context mContext){
        this.mContext = mContext;
        this.mLayoutInflater = mLayoutInflater;
        initFatherView();
    }
    
    public BaseComponents (Context mContext){
    	this.mContext = mContext;
    	initFatherView();
    }
    
    public void setVisiable(int isvisiable){
    	if(mFatherView!=null)mFatherView.setVisibility(isvisiable);
    }
    
    public BaseComponents (Context mContext,View mView){
    	this.mContext = mContext;
    	this.mFatherView = mView;
    	initFatherView();
    }
    protected Context mContext;
    protected View mFatherView;
    protected LayoutInflater mLayoutInflater;
    public abstract void initFatherView();
    
    //用于传递组件中的控件被点击了
    OnComponentsActionListener mOnComponentsActionListener;
    
    public void setOnComponentsAction(OnComponentsActionListener mOnComponentsActionListener){
    	this.mOnComponentsActionListener = mOnComponentsActionListener;
    }
    public View getView(){
        return mFatherView;
    }
    
    public interface OnComponentsActionListener{
    	public void onAction(View mView);
    }

}
