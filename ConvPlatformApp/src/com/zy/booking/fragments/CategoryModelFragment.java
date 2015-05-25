
package com.zy.booking.fragments;

import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.AppDefine;
import com.zy.booking.R;
import com.zy.booking.activitys.SampleHolderActivity;
import com.zy.booking.components.CategorysGirdComponents;
import com.zy.booking.json.ModelUploadJsonData;
import com.zy.booking.modle.ModelIndexAdapter;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.wedget.TotiPotentGridView;
import com.zy.booking.wedget.TotiPotentGridView.ICommViewListener;

public class CategoryModelFragment extends BaseFragment implements
        OnHttpActionListener ,ICommViewListener{

    ModelIndexAdapter modelIndexAdapter;

    CategorysGirdComponents mCategorysGirdComponents;

    TotiPotentGridView mRefreshGridView;

    public static final String TAG_MAN = "man";

    public static final String TAG_WOMEN = "woman";

    public static final String TAG_CHILD = "child";

    @ViewInject(R.id.ll_container_body)
    LinearLayout mLayoutBody;

    private String mCurrentTag;

    JsonArray mJsonArray = new JsonArray();

    public CategoryModelFragment(String mCurrentTag) {
        this.mCurrentTag = mCurrentTag;
    }

    View mViewReflash;
    
    boolean isRefalsh;
    
    public CategoryModelFragment(){
        
    }
    
    @Override
    protected View getBasedView() {
        // GridView mGridView = new GridView(getActivity());
        // mGridView.setNumColumns(3);
        //
        // mCategorysGirdComponents = new CategorysGirdComponents(getActivity(),
        // mGridView);
       


        return inflater.inflate(R.layout.layout_common_withoutscroll,null);
    }

    @Override
    void afterViewInject() {
        
        mViewReflash =
                LayoutInflater.from(getActivity()).inflate(R.layout.layout_pullreflesh,
                        null);
        
        mRefreshGridView = (TotiPotentGridView) mViewReflash.findViewById(R.id.pull_refresh_grid); //pull_refresh_grid
        mRefreshGridView.setCommViewListener(this);
        mLayoutBody.addView(mViewReflash);
        GridView mGridView = mRefreshGridView.getGridView();
        modelIndexAdapter = new ModelIndexAdapter(getActivity(), mJsonArray);
        mGridView.setAdapter(modelIndexAdapter);

        mGridView
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterview,
                            View view, int i, long l) {
                        JsonObject mJsonObj = (JsonObject) mJsonArray.get(i);
                        String id = JsonUtil.getAsString(mJsonObj,
                                ModelIndexAdapter.MODELID);
                        Intent mIntent = new Intent(getActivity(),
                                SampleHolderActivity.class);
                        mIntent.putExtra("tag", SampleHolderActivity.TAG_MODELINFO);
                        mIntent.putExtra("id", id);
                        getActivity().startActivity(mIntent);
                    }
                });

        // mRefreshGridView
        // .setOnRefreshListener(new OnRefreshListener2<GridView>()
        // {
        //
        // @Override
        // public void onPullDownToRefresh(
        // PullToRefreshBase<GridView> refreshView) {
        // searchData("北京","");
        // }
        //
        // @Override
        // public void onPullUpToRefresh(
        // PullToRefreshBase<GridView> refreshView) {
        // searchData("北京","");
        //
        // }});

//        mRefreshGridView.setHasMoreItems(true);
//        mRefreshGridView.setPagingableListener(new PagingGridView.Pagingable() {
//            @Override
//            public void onLoadMoreItems() {
//                // searchData("北京","");
//                mRefreshGridView.getLoadingView().setVisibility(View.VISIBLE);
//                searchData("北京", "",-1);
//
//            }
//
//            @Override
//            public void onReflesh() {
//                searchData("北京", "",0);
//                
//            }
//        });
        
        searchData("北京", "",0);

    }

    public void searchData(String city, String keyword, int begin) {
        if(begin==0){
            isRefalsh  = true;
        }else{
            isRefalsh = false;
        }
        sendData(ModelUploadJsonData.seachModelNameValuePairs("", keyword,
               mCurrentTag, !isRefalsh?mJsonArray.size():begin), AppDefine.URL_MODEL_SEARCHMODEL, this,
                101);
    }


    @Override
    public void onHttpError(Exception e, String msg, int requestCode) {
        // mGridView.onRefreshComplete();
       // mRefreshGridView.getLoadingView().setVisibility(View.GONE);
        mRefreshGridView.pullToRefreshView.onFooterRefreshComplete();
        mRefreshGridView.pullToRefreshView.onHeaderRefreshComplete();
    }

    @Override
    public void onDecoded(String reason, boolean isSuccess,
            JsonObject mJsonResult, JsonArray mLists, int resultCode) {
        // mGridView.onRefreshComplete();
        // "model_list"
        
        dismissProgressDialog();
        JsonArray mJsonList = mJsonResult.getAsJsonArray("model_list");
      //  mRefreshGridView.getLoadingView().setVisibility(View.GONE);
        
        if(isRefalsh){
            this.mJsonArray = mJsonList;
            if(mRefreshGridView!=null)
            mRefreshGridView.pullToRefreshView.onHeaderRefreshComplete();
        }else{
            mJsonArray.addAll(mJsonList);
            if(mRefreshGridView!=null)
            mRefreshGridView.pullToRefreshView.onFooterRefreshComplete();
        }
        modelIndexAdapter.changeDataSource(mJsonArray);
      
    }

	@Override
	public void onHeadRefresh() {
		   searchData("北京", "",0);
		
	}

	@Override
	public void onFootLoadData() {
		   searchData("北京", "",-1);
	}

}
