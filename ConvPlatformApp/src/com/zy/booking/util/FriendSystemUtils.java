package com.zy.booking.util;

import android.content.Context;
import android.text.TextUtils;

import com.zy.booking.EmsgManager;
import com.zy.booking.R;
import com.zy.booking.db.DBFRIEND;

import java.util.List;

public class FriendSystemUtils {
    
public static boolean isMyFriend(String mUid,Context mContext){
    if(TextUtils.isEmpty(mUid)){
        return false;
    }
    DBFRIEND mDbFrined = new DBFRIEND();
    String mSqlSelectFrineds = mContext
            .getResources().getString(R.string.select_friend_baseuserid);
    List<?> mLists = mDbFrined.selectDataFromDb(String.format(mSqlSelectFrineds,
            mUid.replaceAll(EmsgManager.EMSGAREA, "")));
    if(mLists!=null&&mLists.size()>0){
        return true;
    }
    return false;
    
}
}
