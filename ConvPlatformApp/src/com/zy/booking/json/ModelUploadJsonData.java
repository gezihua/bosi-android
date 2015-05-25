
package com.zy.booking.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.zy.booking.CpApplication;
import com.zy.booking.util.PreferencesUtils;

public class ModelUploadJsonData {

    // Album 部分的相关回调接口
    public static List<NameValuePair> getAlbumsNameValuePairs(String referId,
            String referType) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("model_id", referId));
        mLists.add(new BasicNameValuePair("refer_type", "model"));
        return mLists;
    }

    // Album 创建相册部分<param name="name" style="query" type="xs:string"/>
    public static List<NameValuePair> createAlbumsNameValuePairs(
            String introduction, String modelId, String name) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("introduction", introduction));
        mLists.add(new BasicNameValuePair("refer_type", "model"));
        mLists.add(new BasicNameValuePair("model_id", modelId));
        mLists.add(new BasicNameValuePair("name", name));
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mLists.add(new BasicNameValuePair("token", token));
        return mLists;
    }
    
    
    // Album 创建相册部分<param name="name" style="query" type="xs:string"/>
    public static List<NameValuePair> updateAlbumsNameValuePairs(
            String introduction, String albumId, String name) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("introduction", introduction));
        mLists.add(new BasicNameValuePair("album_id", albumId));
        mLists.add(new BasicNameValuePair("name", name));
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mLists.add(new BasicNameValuePair("token", token));
        return mLists;
    }
    

    // Album 删除相册中的某一张图片
    public static List<NameValuePair> removeAlbumsImageNameValuePairs(
            String albumId, String imageId) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("album_id", albumId));
        mLists.add(new BasicNameValuePair("image_id", imageId));
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mLists.add(new BasicNameValuePair("token", token));
        return mLists;
    }

    // 创建相册 <param name="album_id" style="query" type="xs:string"/>
    public static List<NameValuePair> addAlbumsImageNameValuePairs(
            String albumId, String imgName) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("album_id", albumId));
        mLists.add(new BasicNameValuePair("imgName", imgName));
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mLists.add(new BasicNameValuePair("token", token));
        return mLists;
    }

    // update model

    // <param name="modelId" style="query" type="xs:string"/>
    // <param name="name" style="query" type="xs:string"/>
    // <param name="sex" style="query" type="xs:string"/>
    // <param name="type" style="query" type="xs:string"/>
    // <param name="age" style="query" type="xs:int"/>
    // <param name="introduction" style="query" type="xs:string"/>
    // <param name="tags" style="query" type="xs:string"/>
    // <param name="token" style="query" type="xs:string"/>
    public static List<NameValuePair> createModleNameValuePairs(String name,String modelId,String sex,
            String introduction, String tags,String city) {

        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("modelId", modelId));
        mLists.add(new BasicNameValuePair("sex", sex));
        mLists.add(new BasicNameValuePair("type", tags));
        mLists.add(new BasicNameValuePair("name", name));
        mLists.add(new BasicNameValuePair("introduction", introduction));
        mLists.add(new BasicNameValuePair("tags", introduction));
        mLists.add(new BasicNameValuePair("city", city));
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mLists.add(new BasicNameValuePair("token", token));
        return mLists;
    }

    // 获取当前model 信息
    public static List<NameValuePair> getModelNameValuePairs(String modelId) {

        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("model_id", modelId));
        return mLists;
    }

    // <param name="city" style="query" type="xs:string"/>
    // <param name="keyword" style="query" type="xs:string"/>
    // <param name="type" style="query" type="xs:string"/>
    // <param name="begin" style="query" type="xs:int"/>
    // <param name="size" style="query" type="xs:int"/>

    public static List<NameValuePair> seachModelNameValuePairs(String city, String keyword,
            String type, int begin) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("city", city));
        mLists.add(new BasicNameValuePair("keyword", keyword));
        mLists.add(new BasicNameValuePair("type", type));
        mLists.add(new BasicNameValuePair("begin", begin + ""));
        mLists.add(new BasicNameValuePair("size", 10 + ""));
        return mLists;
    }
    
    
    //updateGroup
    public static List<NameValuePair> updatGroupNameValuePairs(String mGroupId, String intro,String name) {
        List<NameValuePair> mLists = new ArrayList<NameValuePair>();
        mLists.add(new BasicNameValuePair("groupId", mGroupId));
        mLists.add(new BasicNameValuePair("introduction", intro));
        mLists.add(new BasicNameValuePair("name", name));
        
        String token = PreferencesUtils.getString(
                CpApplication.getApplication(), "token");
        if (token != null)
            mLists.add(new BasicNameValuePair("token", token));
        return mLists;
    }
    
  
}
