package com.bosi.chineseclass.db;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;

import com.bosi.chineseclass.R;
import com.bosi.chineseclass.bean.BpStasticBean;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

public class BPHZ extends AbsDbOperation{

	@Override
	public <T extends List<? extends EntityBase>> T selectDataFromDb(String sql) {
		return null;
	}
	
	public String getAllLearnedData(Context mContext ,String mTag){
		String sql = mContext.getResources().getString(R.string.select_all_bphzhistory);
		String mSqlFormat = String.format(sql, mTag);
		List<DbModel> dbModels = null;
		StringBuilder mSb = new StringBuilder();
		try{
			dbModels = mDbManager.getContentDb().findDbModelAll(mSqlFormat);
			
			for(DbModel mDbModel:dbModels){
				String dictID = mDbModel.getString(BpcyHistory.DICTINDEX);
				mSb.append(dictID);
				mSb.append(",");
			}
		}catch(DbException e){
		}finally{
			if(dbModels!=null){
				dbModels.clear();
				dbModels =null;
			}
		}
		return mSb.toString();
	}
	
	public List<Integer> selectDictListBaseTag(Context mContext,int tag ,int start ,int end){
		List<Integer>  mLists = new ArrayList<Integer>();
		/**/
		String sql = mContext.getResources().getString(R.string.select_dictindexgroup);
		String sqlFormat = String.format(sql, start,end,tag);
		List<DbModel> dbModels = null;
		try{
			dbModels = mDbManager.getContentDb().findDbModelAll(sqlFormat);
			for(DbModel mDbModel:dbModels){
				int indexDict = mDbModel.getInt(BphzHistory.DICTINDEX);
				mLists.add(indexDict);
			}
		}catch(DbException e){
		}finally{
			if(dbModels!=null){
				dbModels.clear();
				dbModels =null;
			}
		}
		return mLists;
	}
	
	public BpStasticBean getListBpHzBeans(Context mContext ,int start,int end,BpStasticBean  mBpHzBean){
		
		String sqlSelectBphzLvStastic =  mContext.getResources().getString(R.string.select_bphz_lev1data);
		String sqlFormat = String.format(sqlSelectBphzLvStastic, 0,1,start,end);
		
		List<DbModel> dbModels = null;
		final String countRemb="countremb";
		final String countUnRemb = "countunremb";
		
		try{
			dbModels = mDbManager.getContentDb().findDbModelAll(sqlFormat);
			for(DbModel mDbModel:dbModels){
				mBpHzBean.mRemberNum = mDbModel.getString(countRemb);
				mBpHzBean.mUnRemberNum = mDbModel.getString(countUnRemb);
			}
			
		}catch(DbException e){
		}finally{
			if(dbModels!=null){
				dbModels.clear();
				dbModels =null;
			}
		}
		
		return mBpHzBean;
	}
	
	
	
	
//	清除学习数据 根据起始值
	
	public void deleteDbBaseBetweenSE(Context mContext,int start ,int end){
		String mdeleteSql = mContext.getResources().getString(R.string.delete_bphz_basedictindexbetween);
        String format = String.format(mdeleteSql, start,end);
        deleteDataFromDb(format);
	}

	@Override
	public String getDbName() {
		return "bphzhistory";
	}

}
