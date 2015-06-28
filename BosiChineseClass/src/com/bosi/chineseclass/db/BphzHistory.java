package com.bosi.chineseclass.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "bphzhistory", execAfterTableCreated = "CREATE UNIQUE INDEX bphz_history_index ON bphzhistory(index)") 
public class BphzHistory extends EntityBase{
	@Column(column = "index")
	public String index;
	
	@Column(column = "remberNum")
	public String remberNum;
	
	@Column(column = "unRemberNum")
	public String unRemberNum;
}
