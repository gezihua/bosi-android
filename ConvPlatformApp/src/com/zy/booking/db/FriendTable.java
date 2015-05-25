package com.zy.booking.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;


@Table(name = "friends"  , execAfterTableCreated = "CREATE UNIQUE INDEX friend_union_index ON friends(userid)")
public class FriendTable extends EntityBase{
	
    
	@Column(column = "userid" ,defaultValue = "")
	public String userId ;
	
	@Column(column = "groupid" ,defaultValue = "")
	public String groupId ;
	
	
	public static final String USERID = "userid";
	public static final String GROUPID = "groupid";
	public static final String DBNAME= "friends";

	
}