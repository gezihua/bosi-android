package com.zy.booking.fragments;

import java.util.ArrayList;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.R;
import com.zy.booking.activitys.SearchContactActivity;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.components.TabPageIndicatorComponents;
import com.zy.booking.fragments.ModelIndexFragment.OnSingleTouchListener;
import com.zy.booking.modle.SeverFragmentPagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class ContactsFragment extends BaseFragment{

	
	@ViewInject(R.id.headactionbar)
	View mViewHead;

	HeadLayoutComponents mHeadLayout;
	
	@ViewInject(R.id.ll_container_body)
	LinearLayout mLayoutBody;
	@Override
	protected View getBasedView() {
		return inflater.from(getActivity()).inflate(
				R.layout.layout_common_withoutscroll, null);
	}

	@Override
	void afterViewInject() {
		
		mViewPager = new ViewPager(getActivity());
		mViewPager.setId(0x0922223);
		initViewPager();
		initFragments();
		addHeadActionBar();
		addTabPageComponents();
		
		mLayoutBody.addView(mViewPager);
	}
	
	private void addHeadActionBar(){
		mHeadLayout = new HeadLayoutComponents(mActivity, mViewHead);
		mHeadLayout.setTextMiddle("通讯录", -1);
		mHeadLayout.setTextRight("", R.drawable.base_action_bar_add_bg_n);
		mHeadLayout.setRightOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent mIntent = new Intent(mActivity,
						SearchContactActivity.class);
				mIntent.putExtra("tag", SearchContactActivity.TAG_SEARCHANDJOIN);
				startActivity(mIntent);
			}
		});
	}
	
	
	ViewPager mViewPager;
	private void addTabPageComponents() {

		Context mContext = getActivity();
		TabPageIndicatorComponents mTabComponents = new TabPageIndicatorComponents(
				mContext);
		mTabComponents.setData(mContext.getResources()
				.getString(R.string.isp_contact_tabs).split("#"));
		mLayoutBody.addView(mTabComponents.getView());
		mTabComponents.setViewPagger(mViewPager);
	}
	
	
	SeverFragmentPagerAdapter mAdapetr;

	private void initViewPager() {
		mAdapetr = new SeverFragmentPagerAdapter(getActivity()
				.getSupportFragmentManager());
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);

	}

	ArrayList<Fragment> mList;

	private void initFragments() {
		mList = new ArrayList<Fragment>();
		ContactFriendsFragment mFriendFragment = new ContactFriendsFragment();
		ContactGroupFragment mContactGroupFragment = new ContactGroupFragment();		
		mList.add(mFriendFragment);
		mList.add(mContactGroupFragment);
		mAdapetr.appendList(mList);

	}

	/**
	 * ViewPager切换监听方法
	 */
	public OnPageChangeListener pageListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			mViewPager.setCurrentItem(position);
		}
	};

	public interface OnSingleTouchListener {
		public void onSingleTouch(View v);
	}

	public class MyViewPager extends ViewPager {

		public MyViewPager(Context context) {
			super(context);
		}

		public MyViewPager(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		PointF downPoint = new PointF();
		OnSingleTouchListener onSingleTouchListener;

		@Override
		public boolean onTouchEvent(MotionEvent evt) {
			switch (evt.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 记录按下时候的坐标
				downPoint.x = evt.getX();
				downPoint.y = evt.getY();
				if (this.getChildCount() > 1) { // 有内容，多于1个时
					// 通知其父控件，现在进行的是本控件的操作，不允许拦截
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (this.getChildCount() > 1) { // 有内容，多于1个时
					// 通知其父控件，现在进行的是本控件的操作，不允许拦截
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				break;
			case MotionEvent.ACTION_UP:
				// 在up时判断是否按下和松手的坐标为一个点
				if (PointF.length(evt.getX() - downPoint.x, evt.getY()
						- downPoint.y) < (float) 5.0) {
					onSingleTouch(this);
					return true;
				}
				break;
			}
			return super.onTouchEvent(evt);
		}

		public void onSingleTouch(View v) {
			if (onSingleTouchListener != null) {
				onSingleTouchListener.onSingleTouch(v);
			}
		}

		public void setOnSingleTouchListener(
				OnSingleTouchListener onSingleTouchListener) {
			this.onSingleTouchListener = onSingleTouchListener;
		}
	}

}
