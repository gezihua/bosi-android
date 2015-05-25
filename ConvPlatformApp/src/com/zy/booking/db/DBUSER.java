package com.zy.booking.db;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

public class DBUSER extends AbsDbOperation{

	@Override
	public <T extends List<? extends EntityBase>> T selectDataFromDb(String sql) {
		List<DbModel> dbModels = null;
		ArrayList<User> mData = new ArrayList<User>();
		try {
			dbModels = mDbManager.getContentDb().findDbModelAll(sql);
			for (DbModel mDbModle : dbModels) {
				User mChatMsgBean = new User();
				mChatMsgBean.clumn0 = mDbModle
						.getString(User.CLUMN0);
				mChatMsgBean.clumn1 = mDbModle.getString(User.CLUMN1);
				
				mChatMsgBean.clumn2 = mDbModle.getString(User.CLUMN2);
				
				mChatMsgBean.nickname = mDbModle.getString(User.NICKNAME);
				
				mChatMsgBean.userid = mDbModle.getString(User.USERID);
				
				mChatMsgBean.username = mDbModle.getString(User.USERNAME);
				
				mChatMsgBean.phone = mDbModle.getString(User.PHONE);
				
				mChatMsgBean.photo = mDbModle.getString(User.PHOTO);
				
			
				mData.add(mChatMsgBean);
			}
		} catch (DbException e) {
			System.out.println(e);
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
