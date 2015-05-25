package com.zy.booking.modle;

import android.content.Context;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.activitys.SampleHolderActivity;
import com.zy.booking.db.DBFRIEND;
import com.zy.booking.json.UserUpLoadJsonData;
import com.zy.booking.struct.OnHttpActionListener;
import com.zy.booking.util.ViewHolder;

import org.apache.http.NameValuePair;

import java.util.List;

public class ModifyFriendAdapter extends FriendsListAdapter  implements OnHttpActionListener{

    private final String PHOTO = "photo";
    private static final String USRID = "userid";
    private final String SEX = "clumn0";
    
    private final int TAG_REMOVEFRIEND = 101;
    
    private String mCurrendId = "";
    
    public ModifyFriendAdapter(Context mContext, JsonArray mData) {
        super(mContext, mData);
    }

    @Override
    public View getView(int posi, View viewTemp, ViewGroup arg2) {
        if (viewTemp == null) {
            viewTemp = LayoutInflater.from(context).inflate(
                    R.layout.child_item_layout, null);
        }
        JsonObject mJsonData = (JsonObject) mListData.get(posi);
        String nickName = JsonUtil.getAsString(mJsonData, NICKNAME);
        String photo = JsonUtil.getAsString(mJsonData, PHOTO);
        final String mId  =  JsonUtil.getAsString(mJsonData, USRID);
        
        ImageView mImageButton = ViewHolder.get(viewTemp, R.id.item_friend_icon);
        TextView mTvName = ViewHolder.get(viewTemp, R.id.tv_item_friend_name);
        TextView mTvIntro = ViewHolder.get(viewTemp, R.id.tv_item_friendslist_intro);
        
        CpApplication.getApplication().mBitmapManager.disPlayImage(mImageButton, photo);
        
        
        Button mButton = ViewHolder.get(viewTemp, R.id.bt_deletemember);
        
        if(isModifyStatus){
            mButton.setVisibility(View.VISIBLE);
            mButton.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    mCurrendId = mId;
                    removeFriendAction(mId);
                }
            });
        }else{
            mButton.setVisibility(View.GONE);
        }
        mTvName.setText("昵称:"+nickName);
        
        String  sex = JsonUtil.getAsString(mJsonData, SEX);
        if(!TextUtils.isEmpty(sex)){
            mTvIntro.setText(sex.equals("0")?"性别:男":"性别：女");
        }
        
        
        mImageButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Intent mIntent = new Intent(context,SampleHolderActivity.class);
                mIntent.putExtra(SampleHolderActivity.TAG_ID, mId);
                mIntent.putExtra(SampleHolderActivity.TAG_TAG, SampleHolderActivity.TAG_SHOWUSERINFO);
                context.startActivity(mIntent);
            }
        });
        
        return viewTemp;
    }
    
    public void removeFriendAction(String friendId){
        List<NameValuePair>  mListNameValuePairs = UserUpLoadJsonData.removeFriendNameValuePairs(friendId);
        BaseActivity mBaseActivity = (BaseActivity) context;
        mBaseActivity.sendData(mListNameValuePairs, AppDefine.URL_MODEL_REMOVEFUNS, this, TAG_REMOVEFRIEND);
    }

    @Override
    public void onHttpError(Exception e, String msg, int requestCode) {
        
    }

    @Override
    public void onDecoded(String reason, boolean isSuccess, JsonObject mJsonResult,
            JsonArray mLists, int resultCode) {
        
        if(isSuccess && resultCode ==TAG_REMOVEFRIEND){
            if(mCallBack!=null){
                mCallBack.removeFriendCallBack(mCurrendId);
            }
        }
    }
    OnRemoveFriendCallBack mCallBack;
    public void setRemoveFriendCallBack(OnRemoveFriendCallBack mCallBack){
        this.mCallBack =mCallBack;
    }
    public interface OnRemoveFriendCallBack{
        public void removeFriendCallBack(String friendId);
    }
    
    

}
