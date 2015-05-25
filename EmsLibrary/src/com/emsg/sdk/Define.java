
package com.emsg.sdk;

/**
 * @author liangc
 */
public interface Define {

     final static String KILL = "\01\02\03";
    /**
	 * 
	 */
     static final String END_TAG = "\01";
    /**
     * 心跳
     */
     static final String HEART_BEAT = "\02";
    /**
     * 服务器KILL信号
     */
     static final String SERVER_KILL = "\03";
    /**
     * 心跳频率
     */
     static final int HEART_BEAT_FREQ = 1000 * 50;

    public static final int MSG_TYPE_OPEN_SESSION = 0;
    public static final int MSG_TYPE_CHAT = 1;
    public static final int MSG_TYPE_GROUP_CHAT = 2;
    public static final int MSG_TYPE_STATE = 3;
    public static final int MSG_TYPE_SYSTEM = 4;
    public static final int MSG_TYPE_P2P_SOUND = 5;
    public static final int MSG_TYPE_P2P_VIDEO = 6;
    public static final int MSG_TYPE_PUBSUB = 7;

    public static final int ACK_DISABLE = 0;
    public static final int ACK_ENABLE = 1;

    static final String VSN = "0.0.1";
    static String EMSG_HOST = "server.lcemsg.com";
    static int EMSG_PORT = 4222;
    static String TOKEN_HOST = "http://server.lcemsg.com/uptoken/";
    
    static final String ACTION_HEATBEAT = "com.emsg.client";

}
