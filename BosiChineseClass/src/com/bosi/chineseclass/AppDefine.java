package com.bosi.chineseclass;

public class AppDefine {

	public static class FilePathDefine {
		public static String APP_GLOBLEFILEPATH = "bosi-chinese";
		public static String APP_PINYINLEARNPATH = "bosi-chinese/Pylearn/";
		public static String APP_DICTDITALNPATH = "bosi-chinese/Dict/";
		public static String APP_CYDITALNPATH = "bosi-chinese/CY/";
		public static String APP_CYPATH = "bosi-chinese/CY-DICT/";
		public static String APP_JCZY = "bosi-chinese/CACHE_JCZY/";
	}

	public static class ZYDefine {

		public static String CODE_SUCCESS = "1"; // 登陆成功
		public static String CODE_FAILED = "0"; // 登陆失败
		public static String CODE_NOEXISTUSER = "2"; // 用户名不存在
		public static String CODE_INCURETPASWORD = "3"; // 密码不正确
		public static String CODE_OUTOFPROMISSION = "4"; // 超出使用权限

		public static final String TYPE_CATEGORY = "type_ziyuan_category";
		public static final int CATEGORY_ZIRAN = 0;
		public static final int CATEGORY_ZHIWU = 1;
		public static final int CATEGORY_REN = 2;
		public static final int CATEGORY_QIWU = 3;
		public static final int CATEGORY_OTHER = 4;

		public static final String ZY_OBJECT_ID = "id_zyObject";

		public static final int BPHZ_TAG_REMBER = 0;
		public static final int BPHZ_TAG_UNREMBER = 1;
		public static final int BPHZ_TAG_NORMAL = 2;
		public static final long TIMELIMIT_ROLE_PHONEUSER = 10*60*1000;

		public static final int BPHZ_REFID_ADDED = 120000;
		public static final int BPCH_REFID_ADDED = 220000;
		public static final int BPHZ_TOTALNUM = 7000;

		public static final String EXTRA_DATA_BPHZ_SATSTICSTART = "start";
		public static final String EXTRA_DATA_BPHZ_SATSTICEND = "end";

		public static final String EXTRA_DATA_BPCY_SATSTICSTART = "cy_start";
		public static final String EXTRA_DATA_BPCY_SATSTICEND = "cy_end";

		public static final String EXTRA_DATA_USERID = "userId";
		
		public static final String PARAM_PHONELOGIN_PHONE = "phonelogin_phone";
		public static final String PARAM_PHONELOGIN_PASSWORD = "phonelogin_password";
		
		public static final String ACTION_BROADCAST_UPBPCYOVER = "com.action.bpcytastover";
		public static final String ACTION_BRPADCAST_UPBPHZOVER = "com.action.bphztastover";
		
		public static final String ACTION_BRPADCAST_UPBPHZBGTIN = "com.action.bphztastbegin";
		public static final String ACTION_BRPADCAST_UPBPCYBGTIN = "com.action.bpcytastbegin";
		
	}

	public static class URLDefine {
		public static final String URL_PINYINVOICE = "http://www.yuwen100.cn/yuwen100/hzzy/Android/pyxx/";
		public static final String URL_PINREADER = "http://www.yuwen100.cn/yuwen100/zy/zyzd-clips/pinyinread/";
		public static final String URL_HZJC_ZJJZY_VIDEO = "http://www.yuwen100.cn/yuwen100/hzzy/jbzy-clips/video/";

		// 关于博思的介绍
		public static final String URL_LOGIN_REMOTE_ABOUTBOSI = "http://www.yuwen100.cn/yuwen100/hzzy/Android/bosi/bosi.mp4";
		public static final String URL_AUTH = "http://verify.yuwen100.cn";

		public static final String URL_INSTRO_HELP = "http://www.yuwen100.cn/yuwen100/hzzy/Android/help/index.html";

		public static final String URL_SYNCBOSICHARDATA = "http://study.yuwen100.cn/char";
		public static final String URL_SYNCBOSIIDIOM = "http://study.yuwen100.cn/idiom";
		
		public static final String URL_BASEURL = "http://www.yuwen100.cn/yuwen100";
		public static final String URL_ZJKT_ZYINSTRO = URL_BASEURL+"/zhuanjia/zyref/js/zyref-an.html";
		
		//专家课堂的第一个页面的左边菜单
		public static final String URL_ZJKT_FIRSTPAGEMENU = "http://www.yuwen100.cn/yuwen100/hzzy/Android/zhuanjiaindex/firstpage/index.html";
		
		public static final String URL_ZJKT_BASEURL = "http://www.yuwen100.cn/yuwen100/hzzy/Android/zhuanjiaindex";

	
	    public static final String URL_SENDEMS= "http://sms.bsccedu.com/securityCode";
	    
	    public static final String URL_CHECKEMSSECURITY = "http://sms.bsccedu.com/securityCode";
	    
	    public static final String URL_USEREGISTER = "http://passport1.bsccedu.com/registration/mobilephone";
	    
	    public static final String URL_PHONELOGIN = "http://passport1.bsccedu.com/authorization/account";
	    
	    public static final String URL_BUNDPHONE = "http://passport1.bsccedu.com/authorization/mobilephone";
	    //检查超时时间
	    public static final String URL_CHECKPHONEUSETIME = "http://order1.bsccedu.com/deadline";
	}

	public static class STUFFDEFICE {
		public static final String STUFF_VOICE = ".mp3";
	}

}
