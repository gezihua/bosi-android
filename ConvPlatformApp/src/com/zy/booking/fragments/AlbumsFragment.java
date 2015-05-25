
package com.zy.booking.fragments;

import com.emsg.sdk.util.JsonUtil;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.R;
import com.zy.booking.activitys.UpLoadImageToAlbumActivity;
import com.zy.booking.activitys.WaterFallImagesActivity;
import com.zy.booking.components.SwipListViewComponents;
import com.zy.booking.components.SwipListViewComponents.OnSwipCallBack;
import com.zy.booking.json.ModelUploadJsonData;
import com.zy.booking.modle.AlbumListAdapter;
import com.zy.booking.modle.AlbumListAdapter.OnAdapterItemClickListener;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.DataTools;
import com.zy.booking.util.PreferencesUtils;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.AbsListView.LayoutParams;

public class AlbumsFragment extends BaseFragment implements
        OnHttpActionListener ,OnAdapterItemClickListener{

    BaseActivity mContext;

    @Override
    protected View getBasedView() {
        return inflater.inflate(R.layout.layout_common_withoutscroll, null);
    }

    public AlbumsFragment(String modelId) {
        this.modelId = modelId;
    }

    SwipListViewComponents mSwipListView;

    @ViewInject(R.id.ll_container_body)
    LinearLayout mLayoutBody;

    AlbumListAdapter mAlbumListAdapter;

    JsonArray mJsonArray = new JsonArray();

    String modelId;

    String mCurrentModelId;

    public void setData(JsonArray mJsonArray) {
        this.mJsonArray = mJsonArray;
    }

    boolean isModify = false;

    public void setIsModify(boolean isModify) {
        this.isModify = isModify;
    }

    void afterViewInject() {
        mContext = (BaseActivity) getActivity();

        mSwipListView = new SwipListViewComponents(mContext);
        mSwipListView.setSwipCallBack(new OnSwipCallBack() {

            @Override
            public void onReflesh() {
                getMyAlbums();
            }
            @Override
            public void onLoadMore() {
            }
            @Override
            public void onItemClickListener(int position) {
                // 从数据中看是否有图片
                JsonObject mJsonObject = (JsonObject) mJsonArray.get(position);
                JsonArray mJsonArry = JsonUtil.getAsJsonArray(mJsonObject, "imageList");
                String alumbId = JsonUtil.getAsString(mJsonObject, "id");
                if (mJsonArry == null || mJsonArry.size() == 0) {
                    if (!isModify) {
                        mActivity.showToastShort("该相册无照片");
                        return;
                    }
                    Intent mIntent = new Intent(mContext, UpLoadImageToAlbumActivity.class);
                    mIntent.putExtra(UpLoadImageToAlbumActivity.TAG_ALBUMID, alumbId);
                    startActivity(mIntent);
                } else {
                    String[] mJsonData = new String[mJsonArry.size()];
                    String[] mJsonIdData = new String[mJsonArry.size()];
                    for (int i = 0; i < mJsonArry.size(); i++) {
                        mJsonData[i] = JsonUtil.getAsString((JsonObject) mJsonArry.get(i), "url");
                        mJsonIdData[i] = JsonUtil.getAsString((JsonObject) mJsonArry.get(i), "id");
                    }
                    Intent mIntent = new Intent(mContext, WaterFallImagesActivity.class);
                    mIntent.putExtra(WaterFallImagesActivity.TAG_ISMODIFY, isModify);
                    mIntent.putExtra(WaterFallImagesActivity.TAG_URLS, mJsonData);
                    mIntent.putExtra(WaterFallImagesActivity.TAG_ALBUMID, alumbId);
                    mIntent.putExtra(WaterFallImagesActivity.TAG_IDS, mJsonIdData);
                    startActivity(mIntent);
                }
            }
        });
        
        mLayoutBody.addView(mSwipListView.getView());
        mAlbumListAdapter = new AlbumListAdapter(mContext, mJsonArray);
        mAlbumListAdapter.setIsAlbumMaster(isModify);
        mAlbumListAdapter.setOnItemClickListener(this);
        mSwipListView.setAdapter(mAlbumListAdapter);
        mCurrentModelId = PreferencesUtils.getString(mContext, AppDefine.KEY_MODELID);
        if (isModify)
            addCreateAlbumBt();
        
        mAlbumListAdapter.changeDataSource(mJsonArray);
    }
    
    
    public void onModifyClicked(JsonObject mJsonObject){
        String alumbId = JsonUtil.getAsString(mJsonObject, "id");
        showCreateAlbumDialog(true,alumbId);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyAlbums();
    }

    private void addCreateAlbumBt() {
        LinearLayout mLinearLayout = new LinearLayout(mContext);
        mLinearLayout.setGravity(Gravity.CENTER);

        Button mButtonSendService = new Button(mContext);
        mButtonSendService.setText("新建相册");
        mButtonSendService.setTextColor(Color.WHITE);
        mButtonSendService.setBackgroundResource(R.drawable.selector_btn);
        mButtonSendService.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showCreateAlbumDialog(false,null);
            }
        });

        LayoutParams mLayoutParams = new LayoutParams(DataTools.dip2px(mContext, 300),
                LayoutParams.WRAP_CONTENT);
        mButtonSendService.setLayoutParams(mLayoutParams);
        mLinearLayout.addView(mButtonSendService);
        mSwipListView.getListView().addFooterView(mLinearLayout);
    }

    @Override
    public void onHttpError(Exception e, String msg, int requestCode) {
        dismissProgressDialog();
    }

    private final int CODE_GETALBUMS = 101;
    private final int CODE_CREATEALBUMS = 102;
    private final int CODE_UPDATEALBUMS = 103;

    private void getMyAlbums() {
        showProgresssDialog();
        sendData(ModelUploadJsonData.getAlbumsNameValuePairs(modelId, ""),
                AppDefine.URL_ALBUM_GETALBUMLIST, this, CODE_GETALBUMS);
    }

    @Override
    public void onDecoded(String reason, boolean isSuccess,
            JsonObject mJsonResult, JsonArray mLists, int resultCode) {
        dismissProgressDialog();
        mSwipListView.onLoadOver();
        if (resultCode == CODE_GETALBUMS) {
            if (isSuccess) {
                JsonArray mJsonList = JsonUtil.getAsJsonArray(mJsonResult, "album_list");
                if (mJsonList != null) {
                    mJsonArray = mJsonList;
                    mAlbumListAdapter.changeDataSource(mJsonArray);
                }
            }
        } else if (resultCode == CODE_CREATEALBUMS) {
            if (isSuccess) {
                mContext.showToastShort("相册创建成功");
                if (mBuilder.isShowing())
                    mBuilder.dismiss();

                getMyAlbums();
            }
        }
        else if (resultCode == CODE_UPDATEALBUMS) {
            if (isSuccess) {
                mContext.showToastShort("相册信息修改成功");
                if (mBuilder.isShowing())
                    mBuilder.dismiss();

                getMyAlbums();
            }
        }
    }

    private void createAlbums(String name, String instruation) {
        showProgresssDialog();
        sendData(ModelUploadJsonData.createAlbumsNameValuePairs(instruation, modelId,
                name), AppDefine.URL_ALBUM_CREATE, this, CODE_CREATEALBUMS);
    }
    
    private void updateAlbums(String name ,String instruation,String mAlbumsId){
        showProgresssDialog();
        sendData(ModelUploadJsonData.updateAlbumsNameValuePairs(instruation, mAlbumsId,
                name), AppDefine.URL_ALBUM_UPDATEIMG, this, CODE_UPDATEALBUMS);
    }

    NiftyDialogBuilder mBuilder;

    private void showCreateAlbumDialog(final boolean isModify,final String albumId) {
        View mViewAddAlbumLayout = LayoutInflater.from(mContext).inflate(
                R.layout.layout_album_create, null);
        final EditText mEtInstro = (EditText) mViewAddAlbumLayout
                .findViewById(R.id.et_album_create_instro);
        final EditText mEtName = (EditText) mViewAddAlbumLayout
                .findViewById(R.id.et_album_create_name);

        mBuilder = NiftyDialogBuilder
                .getInstance(mContext).withButton1Text("确定")
                .withEffect(Effectstype.Shake)
                .setCustomView(mViewAddAlbumLayout, mContext).withButton2Text("取消")
                .withEffect(Effectstype.Shake).withTitle(isModify?"更新相册":"创建相册");
        mBuilder.setButton1Click(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String mInstroData = mEtInstro.getText().toString();
                String mNameData = mEtName.getText().toString();
                if (TextUtils.isEmpty(mInstroData)) {
                    playYoYo(mEtInstro);
                    return;
                }
                if (TextUtils.isEmpty(mNameData)) {
                    playYoYo(mEtName);
                    return;
                }
                if(!isModify)
                createAlbums(mInstroData, mNameData);else{
                    updateAlbums(mInstroData, mNameData,albumId);
                }
            }
        });

        mBuilder.setButton2Click(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mBuilder.dismiss();
            }
        });
        try {
            mBuilder.show();
        } catch (Exception e) {
        }

    }

}
