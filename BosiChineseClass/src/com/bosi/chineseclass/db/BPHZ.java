package com.bosi.chineseclass.db;

import java.util.List;

public class BPHZ extends AbsDbOperation{

	@Override
	public <T extends List<? extends EntityBase>> T selectDataFromDb(String sql) {
		return null;
	}

	@Override
	public String getDbName() {
		return "bphzhistory";
	}

}
