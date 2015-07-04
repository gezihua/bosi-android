package com.bosi.chineseclass.db;

import java.util.List;


public interface IDbOperation {
	
	public boolean  saveData(EntityBase mEntity);
	
	public <T extends  List<? extends EntityBase>> T selectDataFromDb(String sql);
	
	
	public boolean deleteDataFromDb(String sql);
	
	public boolean updateDataFromDb(String sql);

}
