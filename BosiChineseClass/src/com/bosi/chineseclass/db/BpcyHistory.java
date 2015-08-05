package com.bosi.chineseclass.db;

import com.lidroid.xutils.db.annotation.Column;

import com.lidroid.xutils.db.annotation.Table;

@Table(name = "bpcyhistory", execAfterTableCreated = "CREATE UNIQUE INDEX bpcy_history_uniue ON bpcyhistory(dictindex)")
public class BpcyHistory extends EntityBase{
	
	public static final String DICTINDEX = "dictindex";
	
	public static final String ISREMBER = "isrember";
	@Column(column = "dictindex")
	public int dictindex;
	//0 是未记住
	@Column(column = "isrember", defaultValue = "0")
	public int  isRember;
	
}
