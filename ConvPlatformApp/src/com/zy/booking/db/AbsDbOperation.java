package com.zy.booking.db;


import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.zy.booking.CpApplication;
import com.zy.booking.util.LogUtils;

public abstract class AbsDbOperation implements IDbOperation{
	DbManager mDbManager =CpApplication.getApplication().mDbManager;
	@Override
	public boolean saveData(EntityBase mEntity) {
		
		try {
			mDbManager.getContentDb().save(mEntity);
			return true;
		} catch (DbException e) {
			return false;
		}
	}
	
	public abstract String  getDbName ();
	@Override
	public boolean deleteDataFromDb(String sql) {
		try {
			mDbManager.getContentDb().execNonQuery(sql);
			return true;
		} catch (DbException e) {
			return false;
		}
	}
	@Override
	public boolean updateDataFromDb(String sql) {
		try {
			mDbManager.getContentDb().execNonQuery(sql);
			LogUtils.i("SQL", sql);
			return true;
		} catch (DbException e) {
			LogUtils.i("EXCEPTIONSQL", sql);
			return false;
		}
	}
	
	public DbManager getDBDbManager(){
	    return mDbManager;
	}
	
	 public void clearDbData() {
	        try {
	            mDbManager.getContentDb().execNonQuery("delete from "+ getDbName());
	        } catch (DbException e) {
	            e.printStackTrace();
	        }
	    }
	 
	  public void insertOrUpdate(EntityBase mUser,WhereBuilder mWhereBuilder){
	        boolean isInsertSuccess = saveData(mUser);
	        if (!isInsertSuccess) {
	            try {
	                getDBDbManager().getContentDb()
	                        .update(mUser, mWhereBuilder);
	            } catch (DbException e) {
	            }
	        }
	    }
}
