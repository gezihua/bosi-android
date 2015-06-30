package com.bosi.chineseclass.db;

import java.util.List;

import com.bosi.chineseclass.bean.BphzBean;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

public class BPHZ extends AbsDbOperation{

	@Override
	public <T extends List<? extends EntityBase>> T selectDataFromDb(String sql) {
		return null;
	}
	
	public BphzBean getListBpHzBeans(String sql,final BphzBean mBphzBean){
		List<DbModel> dbModels = null;
		final String countRemb="countremb";
		final String countUnRemb = "countunremb";
		
		try{
			dbModels = mDbManager.getContentDb().findDbModelAll(sql);
			for(DbModel mDbModel:dbModels){
				mBphzBean.mRemberNum = mDbModel.getString(countRemb);
				mBphzBean.mUnRemberNum = mDbModel.getString(countUnRemb);
			}
			
		}catch(DbException e){
		}finally{
			if(dbModels!=null){
				dbModels.clear();
				dbModels =null;
			}
		}
		
		return mBphzBean;
	}

	@Override
	public String getDbName() {
		return "bphzhistory";
	}

}
