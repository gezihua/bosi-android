package com.zy.booking.db;

import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

public class DBFRIEND  extends AbsDbOperation{

    @Override
    public <T extends List<? extends EntityBase>> T selectDataFromDb(String sql) {
        List<DbModel> dbModels = null;
        ArrayList<FriendTable> mData = new ArrayList<FriendTable>();
        try {
            dbModels = mDbManager.getContentDb().findDbModelAll(sql);
            for (DbModel mDbModle : dbModels) {
                FriendTable mFriendTable = new FriendTable();
                mFriendTable.groupId = mDbModle
                        .getString(FriendTable.GROUPID);
                mFriendTable.userId = mDbModle.getString(FriendTable.USERID);
                
                mData.add(mFriendTable);
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
        return FriendTable.DBNAME;
    }

}
