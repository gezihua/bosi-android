
package com.zy.booking;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.emsg.sdk.EmsgCallBack;
import com.emsg.sdk.EmsgClient;
import com.emsg.sdk.EmsgClient.EmsStateCallBack;
import com.emsg.sdk.EmsgClient.MsgTargetType;
import com.emsg.sdk.EmsgConstants;
import com.emsg.sdk.beans.EmsMessage;
import com.google.gson.Gson;
import com.zy.booking.activitys.IndexActivity;
import com.zy.booking.components.NiftyDialogComponents;
import com.zy.booking.components.NiftyDialogComponents.OnNiftyCallBack;
import com.zy.booking.db.ChatMsgBean;
import com.zy.booking.db.MSGHISTORY;
import com.zy.booking.util.PreferencesUtils;
import com.zy.booking.util.TimeUtils;

/**
 * 添加该类主要为了统一管理Emsg的相关操作
 */
public class EmsgManager {

    boolean isNeedVibrate = true;

    boolean isNeedShowNotifyWindow = true;

    boolean isNotifyWithSound = true;
    private EmsgClient mEmsgClient;

    public final static String EMSGAREA = "@booking.com";

    public final static String ACTION_NEWMSGCOMING = "zy.booking.newmsgcomes";
    public final static String ACTION_SESSONCLOSED = "zy.booking.emsg.closed";
    public final static String ACTION_SESSOOPEND = "zy.booking.emsg.opened";
    public final static String ACTION_ADDFRIENDCALLBACK = "zy.booking.emsg.afcb";
    private Context mContext;

    NotificationManager notifyManager;

    public boolean getIsAuthed() {
        return mEmsgClient.isAuth();
    }

    public EmsgManager(EmsgClient mEmsgClient, final Context mContext) {
        this.mContext = mContext;
        this.mEmsgClient = mEmsgClient;

        notifyManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mEmsgClient.init(mContext);

        mEmsgClient.setEmsStCallBack(new EmsStateCallBack() {

            @Override
            public void onAnotherClientLogin() {
                showOtherLoginDialog();
            }

            @Override
            public void onEmsgClosedListener() {
                mContext.sendBroadcast(new Intent(ACTION_SESSONCLOSED));
            }

            @Override
            public void onEmsgOpenedListener() {
                mContext.sendBroadcast(new Intent(ACTION_SESSOOPEND));
            }
        });

    }

    NiftyDialogComponents mDialogExit;

    private void showOtherLoginDialog() {
        mDialogExit = new NiftyDialogComponents(CpApplication.getApplication().mActivityStack.get(0));
        mDialogExit.setUpNifty(
                "退出",
                "重新登录",
                "下线提醒",
                "你的账号与"
                        + TimeUtils.getFormatDateTime(new Date(System.currentTimeMillis()),
                                TimeUtils.yyyy_MM_ddHHMMSS)
                        + "在其他客户端登录! ");
        mDialogExit.setNoftyCallBack(new OnNiftyCallBack() {

            @Override
            public void onBt2Click() {
                auth();
            }

            @Override
            public void onBt1Click() {
                CpApplication.getApplication().destroySystem();
            }
        });
        mDialogExit.showBuilder();
    }

    /**
     * 登陆emsg
     */
    public void auth() {
        String mcurrentId = CpApplication.getApplication().getUserId();
        if (TextUtils.isEmpty(mcurrentId)) {
            return;
        }
        final String msgFrom = mcurrentId + EMSGAREA;

        registerEmsgMessageReciver();
        new Thread() {

            public void run() {
                mEmsgClient.auth(msgFrom, "123", new EmsgCallBack() {

                    @Override
                    public void onSuccess() {
                        if (mDialogExit != null) {
                            mDialogExit.dismissBuilder();
                        }
                    }

                    @Override
                    public void onError(TypeError mErrorType) {
                    }
                });
            }

        }.start();

    }

    private void registerEmsgMessageReciver() {

        IntentFilter mIntentFiter = new IntentFilter();
        // 注册即时消息接收广播
        mIntentFiter.addAction(EmsgConstants.MSG_ACTION_RECDATA);
        // 接收离线消息广播
        mIntentFiter.addAction(EmsgConstants.MSG_ACTION_RECOFFLINEDATA);
        // 对应的上下文对象
        mContext.registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context mContext, Intent mIntent) {

                String mcurrentId = CpApplication.getApplication().getUserId();
                String msgFrom = mcurrentId + EMSGAREA;

                EmsMessage message = (EmsMessage) mIntent
                        .getParcelableExtra("message");

                if (message == null)
                    return;
                MSGHISTORY mdbMsg = new MSGHISTORY();
                ChatMsgBean mChatMsgBean = new ChatMsgBean();
                mChatMsgBean.msgContent = message.getContent();
                mChatMsgBean.msgFrom = message.getmAccFrom();

                mChatMsgBean.msgTo = msgFrom;
                mChatMsgBean.msgTime = System.currentTimeMillis();
                mChatMsgBean.msgId = message.getMid() + "";
                mChatMsgBean.type = message.getType() + "";
                mChatMsgBean.gid = message.getGid();
                mChatMsgBean.column1 = TYPE_COMMON;
                mChatMsgBean.contentType = message.getContentType();
                if (message.getmExtendsMap() != null) {
                    HashMap<String, String> mHashMap = message.getmExtendsMap();
                    if(!mHashMap.isEmpty()){
                        mChatMsgBean.column1 = mHashMap.get("type");
                        if (mChatMsgBean.column1 != null) {
                            // 添加好友
                            if (mChatMsgBean.column1.equals(TYPE_ADDFRIENDS)) {
                                mChatMsgBean.column2 = mHashMap.get("message");
                            }
                            // 好友添加回执
                            else if (mChatMsgBean.column1
                                    .equals(TYPE_ADDFRIENDSCALLBACK)) {
                                mContext.sendBroadcast(new Intent(
                                        ACTION_ADDFRIENDCALLBACK));
                            }
                        }
                    }
                    
                }
                mdbMsg.saveData(mChatMsgBean);
                mContext.sendBroadcast(new Intent(ACTION_NEWMSGCOMING));
                vibrateWhenMsgComing();

                showNotifyCenter(mChatMsgBean.name, mChatMsgBean.msgContent);
            }

        }, mIntentFiter);

    }

    public EmsgClient getEmsgClient() {
        return mEmsgClient;
    }

    // 关闭引擎
    public void exitAppCloseEmsg() {
        mEmsgClient.closeClient();
    }

    private void vibrateWhenMsgComing() {
        boolean isNotifyVibrate = PreferencesUtils.getBoolean(mContext,
                "isshakeopen");
        if (!isNotifyVibrate)
            return;
        Vibrator vibrator = (Vibrator) mContext
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

    // 添加通知栏的显示
    private void showNotifyCenter(String name, String content) {
        if (!isNeedShowNotifyWindow)
            return;

        Notification notification = new Notification(R.drawable.ic_launcher,
                "", System.currentTimeMillis());
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(),
                R.layout.notify_xml_emsg);
        remoteView.setTextViewText(R.id.notify_msgname,
                TextUtils.isEmpty(name) ? "" : name);
        remoteView.setTextViewText(R.id.notify_tv_content, content);
        notification.contentView = remoteView;

        boolean isNotifyWindow = PreferencesUtils.getBoolean(mContext,
                "isvoiceeopen");
        if (isNotifyWindow)
            notification.defaults |= Notification.DEFAULT_SOUND;
        
        Intent mIntent = new Intent(mContext, IndexActivity.class);
        PendingIntent pendingintent = PendingIntent.getActivity(mContext, 0,
                mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.contentIntent = pendingintent;
        notifyManager.notify(2000, notification);
    }

    public void cancleNotify() {
        if (notifyManager != null)
            notifyManager.cancel(2000);
    }

    // ---------------
    /**
     * 添加好友 {type=afs,message=""}
     */
    MSGHISTORY mDBHistory = new MSGHISTORY();

    public void sendJoinFriendsMessage(String messgeTo, String message,
            EmsgCallBack mEmsgCallBack) {

        Map<String, String> mExtendMap = new HashMap<String, String>();
        mExtendMap.put("type", TYPE_ADDFRIENDS);
        mExtendMap.put("message", message);

        mEmsgClient.sendMessageWithExtendMsg(messgeTo, "",
                MsgTargetType.SINGLECHAT, mEmsgCallBack, mExtendMap);
    }

    /**
     * 添加好友 {type=slm,message=""}
     */

    public void sendLeaveMessage(String messgeTo, String message,
            EmsgCallBack mEmsgCallBack) {
        Map<String, String> mExtendMap = new HashMap<String, String>();
        mExtendMap.put("type", TYPE_SENDLEAVEMESSAGE);
        String mcurrentId = CpApplication.getApplication().getUserId();
        String msgFrom = mcurrentId + EMSGAREA;
        ChatMsgBean mChatMsgBean = new ChatMsgBean();
        mChatMsgBean.column1 = TYPE_SENDLEAVEMESSAGE;
        mChatMsgBean.msgContent = message;
        mChatMsgBean.msgFrom = msgFrom;
        mChatMsgBean.msgTo = messgeTo;
        mChatMsgBean.type = "1";
        mChatMsgBean.isRead = "1";
        mChatMsgBean.updateChatMsgBean(mcurrentId, mContext);
        mDBHistory.saveData(mChatMsgBean);

        mEmsgClient.sendMessageWithExtendMsg(messgeTo, message,
                MsgTargetType.SINGLECHAT, mEmsgCallBack, mExtendMap);
    }

    public static String TYPE_ADDFRIENDS = "af";// askFriends
    public static String TYPE_SENDLEAVEMESSAGE = "slm";
    public static String TYPE_ADDFRIENDSCALLBACK = "afcb";// askFriends
    public static String TYPE_COMMON = "com";
}
