package com.bosi.chineseclass.bean;

import com.bosi.chineseclass.db.EntityBase;

public class BpcyDataBean extends EntityBase{

//	  [cybh] INTEGER, 
//	  [CYCimu] CHAR(44), 
//	  [CYFayin] CHAR, 
//	  [CYShiyi] CHAR(100), 
//	  [CYChuchu] CHAR, 
//	  [CYShili] CHAR, 
//	  [CYJinyi] CHAR, 
//	  [CYFanyi] CHAR(20));
	
	public static final String  CYBH = "cybh"; //id
	public static final String CYCIMU = "CYCimu";//成语名词
	public static final String CYFAYIN = "CYFayin";//pingyin
	public static final String CYSHIYI = "CYShiyi";//解释
	public static final String CYCHUCHU = "CYChuchu";//出处
	public static final String CYSHILI = "CYShili";//示例
	public static final String CYJINYI = "CYJinyi";//jinyi
	public static final String CYFANYI = "CYFanyi";//翻译
	
	
	public String Cybh;
	public String CYCimu;
	public String CYFayin;
	public String CYShiyi;
	public String CYChuchu;
	public String CYShili;
	public String CYJinyi;
	public String CYFanyi;
	
	
}
