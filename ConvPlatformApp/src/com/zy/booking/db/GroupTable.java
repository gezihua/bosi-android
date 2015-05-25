package com.zy.booking.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "friendGroups" , execAfterTableCreated = "CREATE UNIQUE INDEX group_union_index ON chathistory(groupId)")
public class GroupTable extends EntityBase{
	
	public static String TABLENAME = "friendGroup";
	
	@Column(column = "groupId")
	public String groupId;
	
	@Column(column = "masterId")
	public String masterId ;
	
	
	
}
