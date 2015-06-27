package com.bosi.chineseclass.su.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class WordPadView extends View {
	// 缩放
	public static Bitmap resizeImage(Bitmap bitmap, int width, int height) {
		int originWidth = bitmap.getWidth();
		int originHeight = bitmap.getHeight();

		float scaleWidth = ((float) width) / originWidth;
		float scaleHeight = ((float) height) / originHeight;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, originWidth,
				originHeight, matrix, true);
		return resizedBitmap;
	}

	private Paint mPaint;
	private Path mPath;
	private Bitmap mBitmap;

	private Canvas mCanvas;
	private int screenWidth, screenHeight;

	private float currentX, currentY;

	public WordPadView(Context context) {
		this(context, 0, 0);
	}

	public WordPadView(Context context, AttributeSet attrs) {
		this(context, null, 0);
		// TODO Auto-generated constructor stub
	}

	public WordPadView(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, 0, 0);
		// TODO Auto-generated constructor stub
	}

	public WordPadView(Context context, int screenWidth, int screenHeight) {
		super(context);
		this.screenWidth = 400;
		this.screenHeight = 400;
		init();
	}
	public void rest(){
	    if (mCanvas!=null) {
	        mPath.reset();
	        mCanvas.drawPath(mPath, mPaint);
	        invalidate();
        }
	}

	// 清除画板
	public void clear() {
		if (mCanvas != null) {
			mPath.reset();
			mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
			invalidate();
		}
	}

	public Bitmap getPaintBitmap() {
		return resizeImage(mBitmap, 320, 480);
	}

	public Path getPath() {
		return mPath;
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true); // 去除锯齿
		mPaint.setStrokeWidth(5);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.BLACK);

		mPath = new Path();

		mBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
				Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		// mCanvas.drawColor(Color.WHITE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBitmap, 0, 0, null);
		canvas.drawPath(mPath, mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			currentX = x;
			currentY = y;
			mPath.moveTo(currentX, currentY);
			break;
		case MotionEvent.ACTION_MOVE:
			currentX = x;
			currentY = y;
			mPath.quadTo(currentX, currentY, x, y); // 画线
			break;
		case MotionEvent.ACTION_UP:
			mCanvas.drawPath(mPath, mPaint);
			break;
		}

		invalidate();
		return true;
	}
}