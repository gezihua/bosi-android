
package com.zy.booking.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zy.booking.R;
import com.zy.booking.activitys.IndexActivity;
import com.zy.booking.components.TabPageIndicatorComponents;
import com.zy.booking.control.LocationCtrol.OnLocationChangedListener;
import com.zy.booking.modle.SeverFragmentPagerAdapter;

import java.util.ArrayList;

public class ModelIndexFragment extends BaseFragment implements
        OnLocationChangedListener {

    @ViewInject(R.id.ll_container_lists)
    LinearLayout mLayoutBody;

    ViewPager mViewPager;

    @ViewInject(R.id.headactionbar)
    View mView;

    @Override
    protected View getBasedView() {
        return inflater.inflate(R.layout.fragment_layout_index, null);
    }

    private void addTabPageComponents() {

        Context mContext = getActivity();
        TabPageIndicatorComponents mTabComponents = new TabPageIndicatorComponents(
                mContext);
        mTabComponents.setData(mContext.getResources()
                .getString(R.string.model_categorys_tabs).split("#"));
        mLayoutBody.addView(mTabComponents.getView());
        mTabComponents.setViewPagger(mViewPager);
        mTabComponents.getView().setBackgroundColor(Color.WHITE);
        
    }

    @Override
    void afterViewInject() {
        mViewPager = new ViewPager(getActivity());
        mViewPager.setId(0x0922222);
        initViewPager();
        initFragments();
        addTabPageComponents();
        mLayoutBody.addView(mViewPager);

        initactionSearchEdit();
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
        CategoryModelFragment mCategoryFragment0 = new CategoryModelFragment(
                CategoryModelFragment.TAG_WOMEN);
        CategoryModelFragment mCategoryFragment1 = new CategoryModelFragment(
                CategoryModelFragment.TAG_MAN);
        CategoryModelFragment mCategoryFragment2 = new CategoryModelFragment(
                CategoryModelFragment.TAG_CHILD);
        mList.add(mCategoryFragment0);
        mList.add(mCategoryFragment1);
        mList.add(mCategoryFragment2);

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

    @ViewInject(R.id.tv_headleft)
    TextView mTvHeadLeft;

    @ViewInject(R.id.et_headviewsearch)
    public EditText mEtSearch;

    String lng, lat;

    @ViewInject(R.id.ll_head_left)
    public LinearLayout mlayoutHeadLeft;

    @ViewInject(R.id.bt_action_search)
    public Button mBtSearch;

    @OnClick(R.id.tv_headleft)
    public void actionHeadLeft(View mView) {
        ((IndexActivity) getActivity()).requestLocation();
    }

    @OnClick(R.id.bt_action_search)
    public void actionSearch(View mView){
        String content = mEtSearch.getText().toString().trim();
        CategoryModelFragment mFragment = (CategoryModelFragment) mList.get(mViewPager.getCurrentItem());
        mFragment.searchData("", content, 0);
        
    }
    private void initactionSearchEdit() {
        mEtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String content = mEtSearch.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    mBtSearch.setVisibility(View.VISIBLE);
                } else {
                    mBtSearch.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onLocationChanged(BDLocation mBdLocation) {

        if (mBdLocation != null) {
            String provice = mBdLocation.getProvince();
            String city = mBdLocation.getDistrict();

            if (provice != null && city != null) {
                if (mTvHeadLeft == null)
                    return;
                mTvHeadLeft.setText(provice + "\n" + city);

                lng = mBdLocation.getLongitude() + "";
                lat = mBdLocation.getLatitude() + "";
            }

//            for (Fragment mFragment : mList) {
//                ((CategoryModelFragment) mFragment).searchData(city, "", 0);
//            }
        }

    }

}
