package com.bosi.chineseclass.han.db;

import java.util.ArrayList;

import java.util.List;

import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

public class GameDbOperation extends AbsDbOperation {

    @Override
    public <T extends List<? extends EntityBase>> T selectDataFromDb(String sql) {
        List<DbModel> dbModels = null;
        ArrayList<GameIconInfo> mData = new ArrayList<GameIconInfo>();
        try {
            dbModels = mDbManager.getContentDb().findDbModelAll(sql);
            for (DbModel mDbModle : dbModels) {
                GameIconInfo mGameIconInfo = new GameIconInfo();
                mGameIconInfo.setType(mDbModle.getInt(GameIconInfo.TYPE));
                mGameIconInfo.setIconPath(mDbModle.getString(GameIconInfo.ICON_PATH));
                mData.add(mGameIconInfo);
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
