package com.bosi.chineseclass.han.db;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

public class ZyCategoryDbOperation extends AbsDbOperation {

    @Override
    public <T extends List<? extends EntityBase>> T selectDataFromDb(String sql) {
        List<DbModel> dbModels = null;
        ArrayList<ZyCategoryInfo> mData = new ArrayList<ZyCategoryInfo>();
        try {
            dbModels = mDbManager.getContentDb().findDbModelAll(sql);
            for (DbModel mDbModle : dbModels) {
                ZyCategoryInfo zyCategoryInfo = new ZyCategoryInfo();
                zyCategoryInfo.setType(mDbModle.getInt(ZyCategoryInfo.TYPE));
                zyCategoryInfo.setIconPath(mDbModle.getString(ZyCategoryInfo.ICON_PATH));
                zyCategoryInfo.setWeb_path_id(mDbModle.getString(ZyCategoryInfo.WEB_PATH_ID));
                zyCategoryInfo.setTitle(mDbModle.getString(zyCategoryInfo.TITLE));
                mData.add(zyCategoryInfo);
            }
        } catch (DbException e) {
        } finally {
            if (dbModels != null) {
                dbModels.clear();
                dbModels = null;
            }

        }
        return (T) mData;
    }

    @Override
    public String getDbName() {
        return null;
    }

}
