package com.bosi.chineseclass.db.dict;

import java.util.ArrayList;
import java.util.List;



import android.database.sqlite.SQLiteDatabase;

import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.bean.BpcyDataBean;
import com.bosi.chineseclass.han.db.AbsDbOperation;
import com.bosi.chineseclass.utils.DesUtils;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

public class BPCYDATA extends AbsDbOperation{

	@Override
	public List<BpcyDataBean> selectDataFromDb(String sql) {
		  List<DbModel> dbModels = null;
		   ArrayList<BpcyDataBean> mData = new ArrayList<BpcyDataBean>();
	        try {
	            dbModels = getDbManager().getContentDb().findDbModelAll(sql);
	            for (DbModel mDbModle : dbModels) {
	            	BpcyDataBean mBpcy = new BpcyDataBean();
	            	mBpcy.CYChuchu = mDbModle
	                        .getString(BpcyDataBean.CYCHUCHU);
	            	mBpcy.CYCimu = mDbModle.getString(BpcyDataBean.CYCIMU);
	            	mBpcy.CYFanyi = mDbModle.getString(BpcyDataBean.CYFANYI);
	            	mBpcy.CYFayin  = mDbModle.getString(BpcyDataBean.CYFAYIN);
	            	mBpcy.CYJinyi  = mDbModle.getString(BpcyDataBean.CYJINYI);
	            	mBpcy.CYShili  = mDbModle.getString(BpcyDataBean.CYSHILI);
	            	mBpcy.CYShiyi  = mDbModle.getString(BpcyDataBean.CYSHIYI);
	            	mBpcy.Cybh  = mDbModle.getString(BpcyDataBean.CYBH);
	                mData.add(mBpcy);
	            }
	        } catch (DbException e) {
	        	System.out.println();
	        } finally {
	            if (dbModels != null) {
	                dbModels.clear();
	                dbModels = null;
	            }
	        }
		return mData;
	}

	  /*update chengyu set CYCimu =\'%1$s\'  ,  CYFayin = \'%2$s\'   , CYChuchu =\'%3$s\'   , CYJinyi= \'%4$s\'  ,CYFanyi= \'%5$s\' where cybh = \'%6$s\' </string>
*/	public void syncEncryptData(List<BpcyDataBean> mList) throws Exception{
	    SQLiteDatabase mDb = getDbManager().getContentDb().getDatabase();
	   mDb.beginTransaction();
		for(int i=0;i<mList.size();i++){
			BpcyDataBean mBean = mList.get(i);
			String sql = BSApplication.getInstance().getResources().getString(R.string.update_bpcy_baseid);
			String formatSql = String.format(sql, DesUtils.encode(mBean.CYCimu),DesUtils.encode(mBean.CYFayin),DesUtils.encode(mBean.CYChuchu),DesUtils.encode(mBean.CYJinyi),DesUtils.encode(mBean.CYFanyi),mBean.Cybh);
			updateDataFromDb(formatSql);
		}
		mDb.setTransactionSuccessful();
		mDb.endTransaction();
	}
	@Override
	public String getDbName() {
		return "chengyu" ;
	}


}
