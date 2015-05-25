package com.zy.booking.db;


import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;


@Table(name = "sendhistory", execAfterTableCreated = "CREATE UNIQUE INDEX index_name ON history(serviceid)")  // 建议加上注解， 混淆后表名不受影响
public class HistoryDbBean extends EntityBase{
	@Column(column = "contect")
	public String contect; 
	
	@Column(column = "isaskd" ,defaultValue="0")
	public String isAskd;
	
	@Column(column = "serviceid")
	public String serviceId;

}
