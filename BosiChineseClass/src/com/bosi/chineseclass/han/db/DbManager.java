package com.bosi.chineseclass.han.db;

import android.content.Context;

import com.lidroid.xutils.DbUtils;

public class DbManager {
    private final static int VERSION = 1;
    private final static String DB_NAME = "dict.db";

    private DbUtils mDbUtil;

    private Context mContext;

    public DbManager(Context mContext) {
        this.mContext = mContext;
        getContentDb();
    }

    int mCurrentVersion = 1;

    public DbUtils getContentDb() {
        mDbUtil = DbUtils.create(mContext, DB_NAME, VERSION, null);
        return mDbUtil;
    }

}
