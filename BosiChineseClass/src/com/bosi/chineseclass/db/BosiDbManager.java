package com.bosi.chineseclass.db;
import android.content.Context;

import com.lidroid.xutils.DbUtils;

public class BosiDbManager {
    private final static int VERSION = 1;
    private final static String DB_NAME = "bosiclass.db";

    private DbUtils mDbUtil;

    private Context mContext;

    public BosiDbManager(Context mContext) {
        this.mContext = mContext;
        getContentDb();
    }

    int mCurrentVersion = 1;

    public DbUtils getContentDb() {
        mDbUtil = DbUtils.create(mContext, DB_NAME, VERSION, null);
        return mDbUtil;
    }

}
