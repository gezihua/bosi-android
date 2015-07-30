package com.bosi.chineseclass.han.db;

import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.han.util.LogUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

public abstract class AbsDbOperation implements IDbOperation {
	
	
	public static DbManager getDbManager(){
		return BSApplication.getInstance().mDbManager;
	}

	@Override
	public boolean saveData(EntityBase mEntity) {

		try {
			getDbManager().getContentDb().save(mEntity);
			return true;
		} catch (DbException e) {
			return false;
		}
	}

	public abstract String getDbName();

	@Override
	public boolean deleteDataFromDb(String sql) {
		try {
			getDbManager().getContentDb().execNonQuery(sql);
			return true;
		} catch (DbException e) {
			return false;
		}
	}

	@Override
	public boolean updateDataFromDb(String sql) {
		try {
			getDbManager().getContentDb().execNonQuery(sql);
			LogUtils.i("SQL", sql);
			return true;
		} catch (DbException e) {
			LogUtils.i("EXCEPTIONSQL", sql);
			return false;
		}
	}

	public void clearDbData() {
		try {
			getDbManager().getContentDb()
					.execNonQuery("delete from " + getDbName());
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public void insertOrUpdate(EntityBase mUser, WhereBuilder mWhereBuilder) {
		boolean isInsertSuccess = saveData(mUser);
		if (!isInsertSuccess) {
			try {
				getDbManager().getContentDb().update(mUser, mWhereBuilder);
			} catch (DbException e) {
			}
		}
	}
}
