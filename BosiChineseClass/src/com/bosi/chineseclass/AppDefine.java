
package com.bosi.chineseclass;

public class AppDefine {

    public static class FilePathDefine {
        public static String APP_GLOBLEFILEPATH = "bosi-chinese";
        public static String APP_PINYINLEARNPATH = "bosi-chinese/Pylearn/";
        public static String APP_DICTDITALNPATH =  "bosi-chinese/Dict/";
        public static String APP_CYDITALNPATH =  "bosi-chinese/CY/";
        public static String APP_CYPATH = "bosi-chinese/CY-DICT/";
        public static String APP_JCZY = "bosi-chinese/CACHE_JCZY/";
    }

    public static class ZYDefine {
    	
        public static final String TYPE_CATEGORY = "type_ziyuan_category";
        
        public static final int CATEGORY_ZIRAN = 0;
        public static final int CATEGORY_ZHIWU = 1;
        public static final int CATEGORY_REN = 2;
        public static final int CATEGORY_QIWU = 3;
        public static final int CATEGORY_OTHER = 4;

        public static final String ZY_OBJECT_ID = "id_zyObject";
        
        public static final int BPHZ_TAG_REMBER = 0;
        public static final int BPHZ_TAG_UNREMBER =1;
        public static final int BPHZ_TAG_NORMAL =2;
        
        public static final int BPHZ_REFID_ADDED =  120000;
        public static final int BPCH_REFID_ADDED = 220000;
        public static final int BPHZ_TOTALNUM = 7000;
        
        
        public static final String EXTRA_DATA_BPHZ_SATSTICSTART = "start";
        public static final String EXTRA_DATA_BPHZ_SATSTICEND = "end";
        
        public static final String EXTRA_DATA_BPCY_SATSTICSTART = "cy_start";
        public static final String EXTRA_DATA_BPCY_SATSTICEND = "cy_end";
    }

    public static class URLDefine {
        public static final String URL_PINYINVOICE = "http://www.yuwen100.cn/yuwen100/hzzy/Android/pyxx/";
        public static final String URL_PINREADER = "http://www.yuwen100.cn/yuwen100/zy/zyzd-clips/pinyinread/";
        public static final String URL_HZJC_ZJJZY_VIDEO = "http://www.yuwen100.cn/yuwen100/hzzy/jbzy-clips/video/";
        
        //关于博思的介绍
        public static final String URL_LOGIN_REMOTE_HOWTOUSER = "http://www.yuwen100.cn/yuwen100/hzzy/Android/bosi/bosi.mp4";
        public static final String URL_AUTH = "http://verify.yuwen100.cn";
        
        public static final String URL_INSTRO_HELP = "http://www.yuwen100.cn/yuwen100/hzzy/Android/help/index.html";
    }

    public static class STUFFDEFICE {
        public static final String STUFF_VOICE = ".mp3";
    }

}
