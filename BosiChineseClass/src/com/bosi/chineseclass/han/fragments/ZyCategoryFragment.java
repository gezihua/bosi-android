package com.bosi.chineseclass.han.fragments;

import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.bosi.chineseclass.AppDefine;
import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.activitys.ZyObjectActivity;
import com.bosi.chineseclass.han.db.ZyCategoryDbOperation;
import com.bosi.chineseclass.han.db.ZyCategoryInfo;
import com.bosi.chineseclass.han.modle.ZyCategoryAdapter;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ZyCategoryFragment extends BaseFragment {

    private int mCategoryType = 0;
    
    @ViewInject(R.id.zy_category_grid)
    private GridView mGridView;
    private ZyCategoryAdapter mGridAdapter;
    
    private Intent mIntent;


    private List<ZyCategoryInfo> mIconList = null;

    @Override
    protected View getBasedView() {
        return inflater.inflate(R.layout.fragment_layout_zy_category, null);
    }

    @Override
    protected void afterViewInject() {
        mIconList = getZyInfoList();
        Log.d("HNX", "" + mIconList.size());
        for (ZyCategoryInfo info : mIconList) {
            Log.e("HNX", info.getIconPath());
        }
        
        mIntent = new Intent(mActivity, ZyObjectActivity.class);

        initGridView();
    }

    public void setCategory(int mCategory) {
        this.mCategoryType = mCategory;
    }

    private List<ZyCategoryInfo> getZyInfoList() {
        ZyCategoryDbOperation gameDbOperation = new ZyCategoryDbOperation();
        //TODO:sql需要加入mCurrentStep
        String sql = "select * from zy_category where type = " + mCategoryType;
//        String sql = "select * from zy_category";
//        String sql = "select * from zy_category where type = 4";
        List<ZyCategoryInfo> iconList = gameDbOperation.selectDataFromDb(sql);
        return iconList;
    }

    private void initGridView() {
        mGridAdapter = new ZyCategoryAdapter(mActivity, mIconList);
        //TODO:不同类别，mGridView的列数不同（mCategoryType）
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int currenItemIndex,
                    long arg3) {
                mIntent.putExtra(AppDefine.ZYDefine.ZY_OBJECT_ID, 
                        mIconList.get(currenItemIndex).getWeb_path_id());
                startActivity(mIntent);
            }
        });
    }

}
