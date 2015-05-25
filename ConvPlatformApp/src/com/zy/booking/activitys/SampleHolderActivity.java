
package com.zy.booking.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.LinearLayout;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.BaseActivity;
import com.zy.booking.R;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.fragments.FunsGroupFragment;
import com.zy.booking.fragments.UserInfoFragment;
import com.zy.booking.modle.SeverFragmentPagerAdapter;
import com.zy.booking.modle.pagefactory.AbsFactory;
import com.zy.booking.modle.pagefactory.ChangeModelInfoFactory;
import com.zy.booking.modle.pagefactory.IspManagerPageFactory;
import com.zy.booking.modle.pagefactory.MapPagerFactory;
import com.zy.booking.modle.pagefactory.ModelInfoPageFactory;
import com.zy.booking.modle.pagefactory.OrderServiceFactory;
import com.zy.booking.modle.pagefactory.ShowLeaMsgFactory;

import java.util.ArrayList;

@ContentView(R.layout.activity_index)
public class SampleHolderActivity extends BaseActivity {
    @ViewInject(R.id.mViewPager)
    ViewPager mViewPager;

    @ViewInject(R.id.ll_indexbottom_container)
    LinearLayout mLinBottomContainer;

    @ViewInject(R.id.mRadioGroup_content)
    LinearLayout mRadioGroup_content;

    SeverFragmentPagerAdapter mAdapetr;

    public final static String TAG_ISPFUNS = "ISPFUNCATION";

    public final static String TAG_CUSTORMORDER = "CUSTORMORDER";

    public final static String TAG_MAPSEARCH = "MAPLOCAION";

    public final static String TAG_MODELINFO = "MODELINFO";

    public final static String TAG_GROUPINFO = "GROUPINFO";

    public final static String TAG_CHANGEMODELINFO = "CHANGEMODELINFO";
    
    public final static String TAG_SHOWUSERINFO = "SHOWUSERINFO";
    
    public final static String TAG_SHOWLEAVEMSG = "LEAVMSG";
    
    
    public static final String TAG_ID = "id";
    public static final String TAG_TAG = "tag";

    @ViewInject(R.id.ll_container_toptab)
    private LinearLayout mlayoutTopTab;

    @ViewInject(R.id.headactionbar)
    View mViewActionBar;

    HeadLayoutComponents mHeadActionBar;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mLinBottomContainer.setVisibility(View.GONE);
        mViewPager.setBackgroundColor(getResources().getColor(R.color.main_gray));
        initViewPager();
        initPageDital();
    }
    
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
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

    private void initViewPager() {
        mAdapetr = new SeverFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
    }

    private void initPageDital() {
        String tagForBuildPage = getIntent().getStringExtra("tag");
        if (tagForBuildPage.equals(TAG_ISPFUNS)) {
            initIspFragments();
        } else if (tagForBuildPage.equals(TAG_CUSTORMORDER)) {
            initMyOrderListFragments();
        } else if (tagForBuildPage.equals(TAG_MAPSEARCH)) {
            initMapLocListFragments();
        } else if (tagForBuildPage.equals(TAG_MODELINFO)) {
            initModelInfoFragments();
        } else if (tagForBuildPage.equals(TAG_GROUPINFO)) {
            initGroupInfoFragments();
        } else if (tagForBuildPage.equals(TAG_CHANGEMODELINFO)) {
            initChangeModelInfoFragments();
        }else if(tagForBuildPage.equals(TAG_SHOWUSERINFO)){
            initModelInfoFramgnet();
        }else if(tagForBuildPage.equals(TAG_SHOWLEAVEMSG)){
            initShowLeaveMsgFragment();
        }
    }

    AbsFactory mPageFactory;

    private void initChangeModelInfoFragments() {
        mHeadActionBar = new HeadLayoutComponents(this, mViewActionBar);
        mHeadActionBar.setTextMiddle("信息修改", -1);
        mHeadActionBar.setDefaultLeftCallBack(true);
        mPageFactory = new ChangeModelInfoFactory(this, mViewPager);
        mAdapetr.appendList(mPageFactory.productBody());
        mPageFactory.productTopTab(mlayoutTopTab);
    }
    
    private void initModelInfoFramgnet(){
        mHeadActionBar = new HeadLayoutComponents(this, mViewActionBar);
        mHeadActionBar.setTextMiddle("个人信息", -1);
        mHeadActionBar.setDefaultLeftCallBack(true);
        String id = getIntent().getStringExtra("id");
        
        ArrayList<Fragment> mLists = new ArrayList<Fragment>();
        mLists .add(new UserInfoFragment(id));
        mAdapetr.appendList(mLists);
    }

    private void initIspFragments() {

        mHeadActionBar = new HeadLayoutComponents(this, mViewActionBar);
        mHeadActionBar.setTextMiddle("服务管理", -1);
        mHeadActionBar.setDefaultLeftCallBack(true);
        String id = getIntent().getStringExtra("id");
        IspManagerPageFactory mPageFactory = new IspManagerPageFactory(this, mViewPager);
        mPageFactory.setServiceId(id);
        mAdapetr.appendList(mPageFactory.productBody());
        mPageFactory.productTopTab(mlayoutTopTab);
    }
    
    private void initShowLeaveMsgFragment(){
        mHeadActionBar = new HeadLayoutComponents(this, mViewActionBar);
        mHeadActionBar.setTextMiddle("留言中心", -1);
        mHeadActionBar.setDefaultLeftCallBack(true);
        ShowLeaMsgFactory mPageFactory = new ShowLeaMsgFactory(this, mViewPager);
        mAdapetr.appendList(mPageFactory.productBody());
        mPageFactory.productTopTab(mlayoutTopTab);
    }

    private void initGroupInfoFragments() {
        String id = getIntent().getStringExtra(TAG_ID);
        ArrayList<Fragment> mList = new ArrayList<Fragment>();
        FunsGroupFragment mGroupFragment = new FunsGroupFragment("");
        mList.add(mGroupFragment);
        mAdapetr.appendList(mList);
        mGroupFragment.setGroupId(id, true);
    }

    private void initModelInfoFragments() {
        mHeadActionBar = new HeadLayoutComponents(this, mViewActionBar);
        mHeadActionBar.setTextMiddle("模特相关", -1);
        mHeadActionBar.setDefaultLeftCallBack(true);
        String id = getIntent().getStringExtra("id");
        ModelInfoPageFactory mPageFactory = new ModelInfoPageFactory(this, mViewPager);
        mPageFactory.setServiceId(id);
        mAdapetr.appendList(mPageFactory.productBody());
        mPageFactory.productTopTab(mlayoutTopTab);
        mPageFactory.productMenu(null);
    }

    public void initMyOrderListFragments() {
        OrderServiceFactory mPageFactory = new OrderServiceFactory(this);
        mAdapetr.appendList(mPageFactory.productBody());
        mPageFactory.productMenu(mRadioGroup_content);
    }

    private void initMapLocListFragments() {
        MapPagerFactory mMapPagerFactory = new MapPagerFactory(this);
        mAdapetr.appendList(mMapPagerFactory.productBody());
    }

}
