package com.bosi.chineseclass.views;

import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.utils.DataTools;
import com.bosi.chineseclass.views.paint.ColorPickerDialog;
import com.bosi.chineseclass.views.paint.ColorPickerDialog.OnColorChangedListener;
import com.bosi.chineseclass.views.paint.PaintView;
import com.firstpeople.paintpad.interfaces.PaintViewCallBack;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public class PaintPadWindow {
	
	private boolean isAdded = false; // 是否已增加悬浮窗
	private static WindowManager wm;
	private static WindowManager.LayoutParams params;
	
	View mBaseView;
	
	@ViewInject(R.id.paintpad_redo)
	private View mViewRedo;
	@ViewInject(R.id.paintpad_undo)
	private View mViewUndo;
	@ViewInject(R.id.paintpad_changecolor)
	private View mChageColor;
	@ViewInject(R.id.paintpad_size)
	private View mViewSize;
	@OnClick(R.id.paintpad_redo)
	public void actionRedo(View mView){
		mPaintView.redo();
	}
	
	@OnClick(R.id.paintpad_undo)
	public void actionUndo(View mView){
		mPaintView.undo();
	}
	ColorPickerDialog mColorPickDialog ;
	@OnClick(R.id.paintpad_changecolor)
	public void actionColor(View mView){
		mColorPickDialog.show();
	}
	
	

	private PaintView mPaintView;
	
	private void initBaseView(){
		mPaintView = (PaintView) mBaseView.findViewById(R.id.paintdialog_paintview);
		mPaintView.setPenSize(20);
		
		mColorPickDialog = new ColorPickerDialog(mContext, new OnColorChangedListener(){

			@Override
			public void colorChanged(int color) {
				mPaintView.setPenColor(color);
			}
			
		},mPaintView.getBackGroundColor());
		mPaintView.setCallBack(new PaintViewCallBack() {
			
			@Override
			public void onTouchDown() {
				
			}
			
			@Override
			public void onHasDraw() {
				
			}
		});
	}
	
	public void dismissView(){
		if(wm!=null && mBaseView!=null&&isAdded){
				wm.removeView(mBaseView);
				
				isAdded = false;
			
		}
	}
	
	
	private Context mContext;
	public PaintPadWindow(Context mContext){
		this.mContext = mContext;
		mBaseView  = View.inflate(mContext, R.layout.layout_paintpad, null);
		ViewUtils.inject(this, mBaseView);
		initBaseView();
	}
	
	public void showPaintPad(){
		createFloatView();
	}
	
	/**
	 * 创建悬浮窗
	 */
	public void createFloatView() {
		if(wm ==null){
			
			  wm = (WindowManager) BSApplication.getInstance()
			        	.getSystemService(Context.WINDOW_SERVICE);
			        params = new WindowManager.LayoutParams();
			        
			        // 设置window type
			        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
			        /*
			         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE;
			         * 那么优先级会降低一些, 即拉下通知栏不可见
			         */
			        
			        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
			        
			        // 设置Window flag
			        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
			                              | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
			        /*
			         * 下面的flags属性的效果形同“锁定”。
			         * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
			        wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
			                               | LayoutParams.FLAG_NOT_FOCUSABLE
			                               | LayoutParams.FLAG_NOT_TOUCHABLE;
			         */
			        
			        // 设置悬浮窗的长得宽
			        params.width = DataTools.dip2px(mContext,200);
			        params.height = DataTools.dip2px(mContext, 200);
		}
        if(isAdded){
        	wm.removeView(mBaseView);
		}
        
        mBaseView.setOnTouchListener(new OnTouchListener() {
        	int lastX, lastY;
        	int paramX, paramY;
        	
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					paramX = params.x;
					paramY = params.y;
					break;
				case MotionEvent.ACTION_MOVE:
					int dx = (int) event.getRawX() - lastX;
					int dy = (int) event.getRawY() - lastY;
					params.x = paramX + dx;
					params.y = paramY + dy;
					// 更新悬浮窗位置
			        wm.updateViewLayout(mBaseView, params);
					break;
				}
				return true;
			}
		});
        wm.addView(mBaseView, params);
        isAdded = true;
	}

}
