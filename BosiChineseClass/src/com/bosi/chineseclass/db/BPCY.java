package com.bosi.chineseclass.db;

import java.util.ArrayList;

import java.util.List;


import android.content.Context;
import android.text.TextUtils;

import com.bosi.chineseclass.R;
import com.bosi.chineseclass.bean.BpStasticBean;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

public class BPCY extends AbsDbOperation{

	@Override
	public <T extends List<? extends EntityBase>> T selectDataFromDb(String sql) {
		return null;
	}
	
	public List<Integer> selectDictListBaseTag(Context mContext,int tag ,int start ,int end){
		List<Integer>  mLists = new ArrayList<Integer>();
		/**/
		String sql = mContext.getResources().getString(R.string.select_bpcy_dictindexgroup);
		String sqlFormat = String.format(sql, start,end,tag);
		List<DbModel> dbModels = null;
		try{
			dbModels = mDbManager.getContentDb().findDbModelAll(sqlFormat);
			for(DbModel mDbModel:dbModels){
				int indexDict = mDbModel.getInt(BpcyHistory.DICTINDEX);
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
	
	public String getAllLearnedData(Context mContext ,String mTag){
		String sql = mContext.getResources().getString(R.string.select_all_bpcyhistory);
		String mSqlFormat = String.format(sql, mTag);
		List<DbModel> dbModels = null;
		StringBuilder mSb = new StringBuilder();
		try{
			dbModels = mDbManager.getContentDb().findDbModelAll(mSqlFormat);
			
			for(DbModel mDbModel:dbModels){
				String dictID = mDbModel.getString(BpcyHistory.DICTINDEX);
				if(TextUtils.isEmpty(dictID))continue;
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
		String result = mSb.toString();
		if(result.endsWith(","))result = result.substring(0, result.length()-1);
		return result;
	}
	
	public BpStasticBean getListBpHzBeans(Context mContext ,int start,int end,BpStasticBean  mBpHzBean){
		
		String sqlSelectBphzLvStastic =  mContext.getResources().getString(R.string.select_bpcy_lev1data);
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
		String mdeleteSql = mContext.getResources().getString(R.string.delete_bpcy_basedictindexbetween);
        String format = String.format(mdeleteSql, start,end);
        deleteDataFromDb(format);
	}

	@Override
	public String getDbName() {
		return "bpcyhistory";
	}
	
	
	
	

}
