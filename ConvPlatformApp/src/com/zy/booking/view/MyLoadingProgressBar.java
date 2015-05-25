
package com.zy.booking.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zy.booking.R;

public class MyLoadingProgressBar {
    public interface OnLoading {
        public void onLoadingSuccess();
        public void onLoadFailed();

    }
    AlertDialog mAlertDialog;
    OnLoading mOnLoading = new OnLoading() {

        @Override
        public void onLoadingSuccess() {
            OnShowSuccess();
        }

        @Override
        public void onLoadFailed() {
            dialogDismiss();
        }
    };
    
    public boolean isProgressShowing(){
        return mAlertDialog != null && mAlertDialog.isShowing();
    }
    ProgressBar mProgressBar;
    ImageView mImageView;
    Context mContext;
    public MyLoadingProgressBar(Context mContext) {
        this.mContext = mContext;
    }
    public enum PrgressType {
        CYCYLE, CYCLEWITHPROG
    }

    
    TextView mTextViewHint;
    public void show() {
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        mAlertDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface paramDialogInterface) {
				
			}
		});
        mAlertDialog.setCanceledOnTouchOutside(false);
        Window window = mAlertDialog.getWindow();
        
        window.setContentView(R.layout.progress_layout);
        window.setGravity(Gravity.CENTER);
    }
    
    public void showWithHint(String hint){
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        mAlertDialog.show();
        mAlertDialog.setOnCancelListener(new OnCancelListener() {
            
            @Override
            public void onCancel(DialogInterface paramDialogInterface) {
                
            }
        });
        mAlertDialog.setCanceledOnTouchOutside(false);
        Window window = mAlertDialog.getWindow();
        
        View mView = LayoutInflater.from(mContext).inflate(R.layout.progress_layout, null);
        mTextViewHint = (TextView) mView.findViewById(R.id.tv_loadinghint);
        if(TextUtils.isEmpty(hint)){
            mTextViewHint.setText(hint);
        }
        window.setContentView(mView);
        window.setGravity(Gravity.CENTER);
    }

    public OnLoading getOnLoadInterface() {
        return mOnLoading;
    }

    public void dialogDismiss() {
        if (isProgressShowing()) {
            try {
                mAlertDialog.cancel();
            } catch (Exception e) {
            }
        }
    }

    public void OnShowSuccess() {
        mProgressBar.setVisibility(View.GONE);
        mImageView.setVisibility(View.VISIBLE);
    }
}
