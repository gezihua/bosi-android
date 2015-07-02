package com.bosi.chineseclass.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "bphzhistory", execAfterTableCreated = "CREATE UNIQUE INDEX bphz_history_index ON bphzhistory(dictindex)") 
public class BphzHistory extends EntityBase{
	
	public static final String DICTINDEX = "dictindex";
	
	public static final String ISREMBER = "isrember";
	@Column(column = "dictindex")
	public String dictindex;
	//0 是未记住
	@Column(column = "isrember", defaultValue = "0")
	public int  isRember;
	
}
