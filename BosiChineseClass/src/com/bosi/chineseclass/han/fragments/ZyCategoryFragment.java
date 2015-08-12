package com.bosi.chineseclass.han.fragments;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

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

    @ViewInject(R.id.title_category)
    private TextView mTitleTV;
    private HashMap mTitles;
    
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

        mIntent = new Intent(mActivity, ZyObjectActivity.class);

        initTitle();
        initGridView();
    }



    public void setCategory(int mCategory) {
        this.mCategoryType = mCategory;
    }

    private List<ZyCategoryInfo> getZyInfoList() {
        ZyCategoryDbOperation gameDbOperation = new ZyCategoryDbOperation();
        String sql = "select * from zy_category where type = " + mCategoryType;
        List<ZyCategoryInfo> iconList = gameDbOperation.selectDataFromDb(sql);
        return iconList;
    }

    private void initTitle() {
        String title = getTitleStr();
        mTitleTV.setText(title);
    }

    private String getTitleStr() {
        HashMap<Integer, String> titles = new HashMap<Integer, String>();
        titles.put(AppDefine.ZYDefine.CATEGORY_ZIRAN, getStringByid(R.string.category_ziran));
        titles.put(AppDefine.ZYDefine.CATEGORY_ZHIWU, getStringByid(R.string.category_zhiwu));
        titles.put(AppDefine.ZYDefine.CATEGORY_REN, getStringByid(R.string.category_ren));
        titles.put(AppDefine.ZYDefine.CATEGORY_QIWU, getStringByid(R.string.category_qiwu));
        titles.put(AppDefine.ZYDefine.CATEGORY_OTHER, getStringByid(R.string.category_dongwu));
        String title = String.format(getStringByid(R.string.category_title), titles.get(mCategoryType), mIconList.size());
        return title;
    }
    
    private String getStringByid(int id){
        return getResources().getString(id);
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
