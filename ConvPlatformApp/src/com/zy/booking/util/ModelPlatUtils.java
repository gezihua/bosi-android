package com.zy.booking.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonObject;
import com.zy.booking.AppDefine;
import com.zy.booking.activitys.ChangeUserInfoActivity;
public class ModelPlatUtils {
    
    
    public static void insertGroupInfo(Context mContext,JsonObject mJsonModel){
//        "groupId" : "ab02532daefa4cc68b5d3e621f597236",
//        "name" : "123456的群",
//        "introduction" : null
        
       
        String groupId = JsonUtil.getAsString(mJsonModel,
                "id");
        if(groupId ==null) return;
        String name = JsonUtil.getAsString(mJsonModel,
                "name");
        String introduction = JsonUtil.getAsString(mJsonModel,
                "introduction");
        String photo = JsonUtil.getAsString(mJsonModel,
                "photo");
        PreferencesUtils.putString(mContext, AppDefine.KEY_MODELGROUPID, groupId);
        PreferencesUtils.putString(mContext, AppDefine.KEY_GROUPNAME, name);
        PreferencesUtils.putString(mContext, AppDefine.KEY_GROUPICON, photo);
        PreferencesUtils.putString(mContext, AppDefine.KEY_GROUPINTRO, introduction);

    }
    
    
    public static void insertUserInfo(Context mContext,JsonObject mJsonModel){
        if(mJsonModel==null)return ;
        
//{"user":{"id":"5b5db57ff9ae47fb9f1280bcdd93e82f",
//        "username":"18310665040","password":"","nickname":"猪乐",
//        "photo":"http://202.85.221.165:8080/app-img/5b5db57ff9ae47fb9f1280bcdd93e82f/2f295ac2abd3487b9b94a477372c24b7.jpeg"
//            ,"sex":"0","phone":"123456","sp":"true"}}
//        public static final String KEY_NICKNAME = "nickName";
//        public static final String KEY_ICONURL= "iconUrl";
//        public static final String KEY_ACCOUNT= "account";
        
        String photo = JsonUtil.getAsString(mJsonModel, "photo");
        String nickname = JsonUtil.getAsString(mJsonModel, "nickname");
        String id = JsonUtil.getAsString(mJsonModel, "id");
        String sex = JsonUtil.getAsString(mJsonModel, "sex");
        
        
       
        String userName  = JsonUtil.getAsString(mJsonModel, "username");
        if(!TextUtils.isEmpty(userName)){
            PreferencesUtils.putString(mContext, AppDefine.KEY_NICKNAME, nickname);
        }
        PreferencesUtils.putString(mContext, AppDefine.KEY_SEX, sex);
        PreferencesUtils.putString(mContext, AppDefine.KEY_NICKNAME, nickname);
        if(!TextUtils.isEmpty(photo)){
            PreferencesUtils.putString(mContext, AppDefine.KEY_ICONURL, photo);
        }
       
        PreferencesUtils.putString(mContext, AppDefine.KEY_ACCOUNT, userName);
        PreferencesUtils.putString(mContext, AppDefine.KEY_USERID, id);
        
      
    }
    
    public static void insertModelInfo(Context mContext,JsonObject mJsonModel){
        
        String name = JsonUtil.getAsString(mJsonModel,
                "name");
        String photo = JsonUtil.getAsString(mJsonModel,
                "photo");
        String modelId = JsonUtil.getAsString(mJsonModel,
                "id");
        String introduction = JsonUtil.getAsString(mJsonModel,
                "introduction");
        String mGroupId = JsonUtil.getAsString(mJsonModel,
                "groupId");
        
        String mSex = JsonUtil.getAsString(mJsonModel,
                "sex");
        String city =  JsonUtil.getAsString(mJsonModel,
                "city");
        String mTag =  JsonUtil.getAsString(mJsonModel,
                "tag");
        if(!TextUtils.isEmpty(mSex))
        PreferencesUtils.putString(mContext, AppDefine.KEY_MODELSEX, mSex);
        
        
        if(!TextUtils.isEmpty(city)){
            PreferencesUtils.putString(mContext, AppDefine.KEY_MODELCITY, city);
        }
        
        if(!TextUtils.isEmpty(mTag)){
            PreferencesUtils.putString(mContext, AppDefine.KEY_MODELTAGS, mTag);
        }
        PreferencesUtils.putString(mContext, AppDefine.KEY_MODELGROUPID, mGroupId);
        PreferencesUtils.putString(mContext, AppDefine.KEY_MODELID, modelId);
        PreferencesUtils.putString(mContext, AppDefine.KEY_MODELNAME, name);
        PreferencesUtils.putString(mContext, AppDefine.KEY_MODELICONURL, photo);
        PreferencesUtils.putString(mContext, AppDefine.KEY_MODELINSTRO, introduction);
    }

}
