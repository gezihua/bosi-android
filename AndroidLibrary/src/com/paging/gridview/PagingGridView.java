package com.paging.gridview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import java.util.List;

public class PagingGridView extends HeaderGridView {

	public interface Pagingable {
		void onLoadMoreItems();

		void onReflesh();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//判断当前是像哪个方向滑动
		return super.onTouchEvent(ev);
	}

	private boolean isLoading;
	private boolean hasMoreItems;
	private Pagingable pagingableListener;
	private LoadingView loadinView;

	public PagingGridView(Context context) {
		super(context);
		init();
	}

	public PagingGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PagingGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public boolean isLoading() {
		return this.isLoading;
	}

	public void setIsLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	public void setPagingableListener(Pagingable pagingableListener) {
		this.pagingableListener = pagingableListener;
	}

	public void setHasMoreItems(boolean hasMoreItems) {
		this.hasMoreItems = hasMoreItems;
		if (!this.hasMoreItems) {
			removeFooterView(loadinView);
		}
	}

	public boolean hasMoreItems() {
		return this.hasMoreItems;
	}

	public void onFinishLoading(boolean hasMoreItems,
			List<? extends Object> newItems) {
		setHasMoreItems(hasMoreItems);
		setIsLoading(false);
		if (newItems != null && newItems.size() > 0) {
			ListAdapter adapter = ((FooterViewGridAdapter) getAdapter())
					.getWrappedAdapter();
			if (adapter instanceof PagingBaseAdapter) {
				((PagingBaseAdapter) adapter).addMoreItems(newItems);
			}
		}
	}

	public View getLoadingView() {
		return loadinView;
	}

	int mCurrentFirstVisiItem = -1;

	private void init() {
		isLoading = false;
		loadinView = new LoadingView(getContext());
		addFooterView(loadinView);
		setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				switch (scrollState) {
				// 当不滚动时
				case OnScrollListener.SCROLL_STATE_IDLE:

					if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
						pagingableListener.onLoadMoreItems();
					}

					if (view.getFirstVisiblePosition() == 0) {
						pagingableListener.onReflesh();
					}

					break;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				mCurrentFirstVisiItem = firstVisibleItem;

				// if (totalItemCount > 0) {
				// int lastVisibleItem = firstVisibleItem + visibleItemCount;
				// if (!isLoading && hasMoreItems && (lastVisibleItem ==
				// totalItemCount)) {
				// if (pagingableListener != null) {
				// isLoading = true;
				// pagingableListener.onLoadMoreItems();
				// }
				//
				// }
				// }
			}
		});
	}

}
