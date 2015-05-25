
package com.zy.booking.components;

import android.content.Context;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.R;
import com.zy.booking.wedget.InitView;
import com.zy.booking.wedget.swiptlistview.SwipeListView;

public class SwipListViewComponents extends BaseComponents implements
        OnRefreshListener, OnItemClickListener {

    public SwipListViewComponents(Context mContext) {
        super(mContext);
    }

    public SwipListViewComponents(Context mContext, View mView) {
        super(mContext, mView);
    }

    @ViewInject(R.id.swipe_container)
    private SwipeRefreshLayout mSwipRefreshLayout;
    @ViewInject(R.id.listview)
    private SwipeListView mSwipListView;

    @ViewInject(R.id.progressBar)
    ProgressBar mProgressBar;

    public void loading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void onLoadOver() {
        mProgressBar.setVisibility(View.GONE);
        mSwipRefreshLayout.setRefreshing(false);
    }

    public void onLoadOver(boolean isLoadingBottom) {
        onLoadOver();
        mSwipListView.setHasMore(isLoadingBottom);
        mSwipListView.onBottomComplete();

    }

    public void isLoadBottomAuto(boolean isLoadAuto) {
        mSwipListView.setAutoLoadOnBottom(isLoadAuto);
    }

    public void setAdapter(BaseAdapter mBaseAdapter) {
        mSwipListView.setAdapter(mBaseAdapter);
    }

    @Override
    public void initFatherView() {
        if (mFatherView == null)
            mFatherView = LayoutInflater.from(mContext).inflate(
                    R.layout.layout_swip, null);
        if (mFatherView != null)
            ViewUtils.inject(this, mFatherView);
        initProperties();
    }

    private void initProperties() {
        InitView.instance().initSwipeRefreshLayout(mSwipRefreshLayout);
        mSwipRefreshLayout.setOnRefreshListener(this);
        mSwipListView.setOnItemClickListener(this);
        mSwipListView.setDivider(null);
        mSwipListView.setAutoLoadOnBottom(false);
        mSwipListView.setOnBottomListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (monOnSwipCallBack != null) {
                    monOnSwipCallBack.onLoadMore();
                }
            }
        });
    }

    OnSwipCallBack monOnSwipCallBack;

    public void setOnItemLongClickedListener(OnItemLongClickListener mLongClicked) {
        mSwipListView.setOnItemLongClickListener(mLongClicked);
    }

    public void setSwipCallBack(OnSwipCallBack mOnSwipCallBack) {
        this.monOnSwipCallBack = mOnSwipCallBack;
    }

    @Override
    public void onRefresh() {
        if (monOnSwipCallBack != null) {
            loading();
            monOnSwipCallBack.onReflesh();
            return;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView,
            int position, long paramLong) {
        if (monOnSwipCallBack != null) {
            monOnSwipCallBack.onItemClickListener(position);
            return;
        }

    }

    public ListView getListView() {
        return mSwipListView;
    }

    public interface OnSwipCallBack {
        public void onReflesh();

        public void onLoadMore();

        public void onItemClickListener(int position);
    }

}
