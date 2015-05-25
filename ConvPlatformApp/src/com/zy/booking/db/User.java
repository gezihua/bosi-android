package com.zy.booking.db;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "user", execAfterTableCreated = "CREATE UNIQUE INDEX userIndex ON user(userid)")
public class User extends EntityBase {
	
	public static final String USERNAME = "username";
	public static final String NICKNAME = "nickname";
	public static final String USERID = "userid";
	public static final String PHONE = "phone";
	public static final String PHOTO = "photo";
	public static final String CLUMN0 = "clumn0"; 
	public static final String CLUMN1 = "clumn1"; 
	public static final String CLUMN2 = "clumn2"; 
	
	@Column(column = "username" ,defaultValue = "")
	public String username;
	@Column(column = "nickname" ,defaultValue = "")
	public String nickname;
	@Column(column = "userid" ,defaultValue = "")
	public String userid;
	@Column(column = "photo" ,defaultValue = "")
	public String photo;
	@Column(column = "phone" ,defaultValue = "")
	public String phone;
	@Column(column = "clumn0" ,defaultValue = "")
	public String clumn0;
	@Column(column = "clumn1" ,defaultValue = "")
	public String clumn1;
	@Column(column = "clumn2" ,defaultValue = "")
	public String clumn2;

}
