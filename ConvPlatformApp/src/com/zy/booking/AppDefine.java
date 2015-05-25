
package com.zy.booking;

public class AppDefine {
    // http://202.85.221.165:8080/booking/rest/v1.0?_wadl

    public static String BASE_URL = "http://202.85.221.165:8080/booking/rest/v1.0";// "http://192.168.15.16:12306";

    public static String BASE_URL_COMMON = "http://202.85.221.165:8080/app-base/rest/v1.0/";

    public static final String BASE_URL_SEARCHDITAL = BASE_URL + "/sp/detail";

    public static final String BASE_URL_SEARCOMMENTS = BASE_URL + "/user/comment";

    public static final String BASE_URL_REGISTER = BASE_URL + "/user/register";

    public static final String BASE_URL_ACTDITAL = BASE_URL + "/user/actdetail";

    /**
     * 查询默认列表 for user
     */
    public static final String BASE_URL_SEARCHDEFAULT = BASE_URL + "/user/search_default";

    public static final String BASE_URL_SERECOMMD = BASE_URL + "/user/searchbyrecom";

    public static final String BASE_URL_SENEARBY = BASE_URL + "/user/searchbynearby";

    // cp 时间管理
    public static final String BASE_URL_CHANGESCHEDULE = BASE_URL + "/sp/changeSchedule";

    // 查询固定时间段的预约用户情况
    public static final String BASE_URL_ISPSEARCH = BASE_URL + "/sp/queryBooking";

    public static final String BASE_URL_SEARCHBYISUSER = BASE_URL + "/user/search_issuer";

    public static final String BASE_URL_LOGIN = "http://202.85.221.165:8080/app-sso/rest/v1.0/sso/login";

    // url for 预约历史
    public static final String BASE_URL_QUERYBOOKING = BASE_URL + "/user/queryBooking";

    // 发起预约服务
    public static final String BASE_URL_BOOKING = BASE_URL + "/user/booking";

    public static final String BASE_URL_OPENDATE = BASE_URL + "/sp/openDate";

    public static final String BASE_URL_ADDSERVICE = BASE_URL + "/sp/create";

    public final static String KEY_USERNAME = "userName";

    public final static String KEY_PASWORD = "possword";

    public final static String KEY_ACCESSTOKEN = "accessToken";

    public final static int DBVERSION = 1;

    public static final int MAX_UPLOADPICS = 4;
    

    public static final String APP_GLOBLEFILEPATH = "zy-booking";

    // 查询开通服务的省份
    public static final String URL_SEARCHAVAILPROVICE = BASE_URL_COMMON + "area/getProvince";
    // 查询开通服务的区域
    public static final String URL_SEARCHAVAILAREA = BASE_URL_COMMON + "area/getCity";

    public static final String URL_SEARCHCITY = BASE_URL_COMMON + "area/getCounty";

    public static final String URL_SERACHCATEGORYS = BASE_URL + "/svc/getTypeList";

    // 修改用户信息

    public static final String URL_UPDATEUSERINFO = BASE_URL + "/user/updateUser";

    public static final String URL_GETUSERINFO = BASE_URL + "/user/getUser";

    public static final String URL_GETTOKENINFO = "http://202.85.221.165:8080/app-sso/rest/v1.0/sso/getTokenInfo";

    // 获取已经开放的时间 进行微调
    public static final String URL_GETDATE_OPENED = BASE_URL + "/sp/getDateOpened";

    // 微调时间
    public static final String URL_CHANGE_OPENDATE = BASE_URL + "/sp/changeOpenDate";

    public static final String ADMIN = "admin";

    // ----------evaluation 评论部分
    public static final String URL_EVALUATION_GETTOPICS = BASE_URL + "/evaluation/sp_get_topics";

    // /sp_add_topic
    public static final String URL_EVALUATION_ADDTOPICS = BASE_URL + "/evaluation/sp_add_topic";

    // /sp_add_topic
    public static final String URL_EVALUATION_ADDCOMMENT = BASE_URL + "/evaluation/sp_add_comment";

    // /sp_add_topic
    public static final String URL_EVALUATION_ADDCOMMENTREPLAY = BASE_URL
            + "/evaluation/sp_add_reply";

    // ------------相册 部分

    // 相册中添加图片
    public static final String URL_ALBUM_ADDIMAGE = BASE_URL + "/album/addImages";

    // 创建相册
    public static final String URL_ALBUM_CREATE = BASE_URL + "/model/addAlbum";

    // 删除某一个相册的一张图片removeImage
    public static final String URL_ALBUM_REMOVEIMG = BASE_URL + "/album/removeImage";
    
    // 删除某一个相册的一张图片removeImage
    public static final String URL_ALBUM_UPDATEIMG = BASE_URL + "/album/updateAlbum";
    

    // ---------------/model

    public static final String URL_MODLE_CREATE = BASE_URL + "/model/updateModel";

    public static final String URL_MODLE_GETMODEL = BASE_URL + "/model/getModel";
    // 相册列表
    public static final String URL_ALBUM_GETALBUMLIST = BASE_URL + "/model/getAlbumList";

    public static final String URL_MODEL_SEARCHMODEL = BASE_URL + "/model/searchModel";
    
    
    public static final String URL_MODEL_UPDATEGROUP = BASE_URL + "/group/updateGroup";

    public static final String KEY_MODELID = "modelId";
    public static final String KEY_MODELGROUPID = "modelGroupId";
    public static final String KEY_MODELNAME = "modelName";
    public static final String KEY_MODELICONURL = "modelIconUrl";
    public static final String KEY_MODELINSTRO = "intro";
    public static final String KEY_MODELSEX = "sex";
    public static final String KEY_MODELTAGS = "tags";
    public static final String KEY_USERICONURL = "iconUrl";
    
    
    
    public static final String KEY_MODELCITY = "modelCity";
    
    
    public static final String KEY_GROUPNAME = "mgroupName";
    public static final String KEY_GROUPICON = "mgroupicon";
    public static final String KEY_GROUPINTRO = "mgroupintro"; 
    public static final String KEY_USERID = "userid";
    public static final String KEY_SEX = "sex";

    
    
    public static final String KEY_NICKNAME = "nickName";
    public static final String KEY_ICONURL= "iconUrl";
    public static final String KEY_ACCOUNT= "account";
    // group
    
    
    public static final String KEY_USERUPDATETIME = "userUptime";
    public static final String KEY_GROUPDATETIME = "groupUpTime";
    public static final String KEY_MODELUPDATETIME = "modelUpTime";
    
    
    public static final String KEY_USERLASTUPDATETIME = "";
    // getGroup
    public static final String URL_GROUP_GETGROUP = BASE_URL + "/group/getGroup";

    public static final String URL_USER_JOINGROUP = BASE_URL + "/user/joinGroup";
    
    public static final String URL_USER_QUITGROUP = BASE_URL + "/user/quitGroup";

    public static final String URL_USER_GETGROUPS = BASE_URL + "/user/getGroups";

    public static final String URL_USER_GETFRIENDS = BASE_URL + "/user/getFriends";

    public static final String URL_USER_ADDFRIEND = BASE_URL + "/user/addFriend";

    public static final String URL_USER_SEARCHFRIENDS = BASE_URL + "/user/search_nickname";
    public static final String URL_USER_BECOMEMODEL = BASE_URL + "/user/becomeModel";
    
    public static final String URL_USER_GETUSERINFO = BASE_URL + "/user/getUserInfo";
    public static final String URL_USER_GETCACHEDATA = BASE_URL + "/user/getUserCachedData";
    
    public static final String URL_USER_REMOVEUSER = BASE_URL + "/user/removeFriend";
    
    public static final String URL_MODEL_REMOVEFUNS = BASE_URL + "/model/kickOffFans";
    
    public static final String ACTION_REMOVEFUNS = "zy.remove.funs";
    
//  {	
//    “status”:{”status_code”:0,”status_reason”:””},
//    “result”: {“access_token”:”ACCESS_TOKEN”,”expires_in”:7200}
//    }
    
    
    // 微店的接口 
    public static final String URL_SALE_GETASSESSTOKEN = "https://api.vdian.com/token?grant_type=client_credential&appkey=%1$s&secret=%2$s";
}
