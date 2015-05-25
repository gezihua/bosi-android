package com.zy.booking.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.lidroid.xutils.exception.DbException;

public class DbManager {

	private DbUtils mDbUtil;

	private Context mContext;
	
	
	public DbManager(Context mContext){
		this.mContext = mContext;
		getContentDb();
	}

	
	int mCurrentVersion =1 ;
	public DbUtils getContentDb() {
		mDbUtil = DbUtils.create(mContext, "history", mCurrentVersion,
				new DbUpgradeListener() {

					@Override
					public void onUpgrade(DbUtils db, int oldVersion,
							int newVersion) {
						if(oldVersion==0){
							upgradeVersion1();
						}
						
					}
				});
		return mDbUtil;
	}
	
	//添加群组id项
	private void upgradeVersion1(){
		try {
			mDbUtil.execNonQuery(" alter table chathistory add gid TEXT");
		} catch (DbException e) {
		}
	}

	/**
	 * 插入发布数据
	 * 
	 * */
	public void insertHistoryData(HistoryDbBean mDbBean) {
		try {
			mDbUtil.save(mDbBean);
		} catch (DbException e) {
		}
	}
	
	
	
	
	/**
	 * 删除所有的发布服务
	 * 
	 * */
	
	public boolean deleteAllService(){
		String sql = "delete from history";
		try {
			mDbUtil.execNonQuery(sql);
			return true;
		} catch (DbException e) {
			return false;
		}
	}

	public boolean askTheService(String id) {
		String sql = "update history set isaskd = '1' WHERE serviceid = '%1$s'";
		try {
			mDbUtil.execNonQuery(sql.format(sql, id));
			return true;
		} catch (DbException e) {
			return false;
		}
	}

	public ArrayList<HistoryDbBean> getAskedList() {
		Cursor mCursor = null;
		try {
			ArrayList<HistoryDbBean> mData = new ArrayList<HistoryDbBean>();
			mCursor = mDbUtil
					.execQuery("select * from history where isaskd = '1'");
			mCursor.moveToPosition(-1);
			while (mCursor.moveToNext()) {
				HistoryDbBean dbBeans = new HistoryDbBean();
				dbBeans.serviceId = mCursor.getString(2);
				dbBeans.isAskd = mCursor.getString(1);
				dbBeans.contect = mCursor.getString(3);
				mData.add(dbBeans);
			}
			return mData;
		} catch (DbException e) {
			e.printStackTrace();
		} finally {
			if(mCursor!=null)
			mCursor.close();
		}
		return null;
	}
	
	
	

}
