package com.bosi.chineseclass.db;



import com.bosi.chineseclass.BSApplication;

import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;


public abstract class AbsDbOperation implements IDbOperation{
	BosiDbManager mDbManager = BSApplication.getInstance().mDbBosiClass;
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
			return true;
		} catch (DbException e) {
			return false;
		}
	}
	
	public BosiDbManager getDBDbManager(){
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
