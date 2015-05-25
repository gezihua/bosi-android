
package com.zy.booking.activitys;

import java.io.File;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emsg.sdk.EmsgCallBack;
import com.emsg.sdk.EmsgClient;
import com.emsg.sdk.EmsgClient.MsgTargetType;
import com.emsg.sdk.util.NetStateUtil;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qiniu.utils.FileUri;
import com.zy.booking.AppDefine;
import com.zy.booking.BaseActivity;
import com.zy.booking.CpApplication;
import com.zy.booking.EmsgManager;
import com.zy.booking.R;
import com.zy.booking.components.ChatPicsSelectComponents;
import com.zy.booking.components.HeadLayoutComponents;
import com.zy.booking.components.MyFaceContainer;
import com.zy.booking.components.MyFaceContainer.OnCallBack;
import com.zy.booking.db.ChatMsgBean;
import com.zy.booking.db.DBUSER;
import com.zy.booking.db.MSGHISTORY;
import com.zy.booking.modle.ChatMsgViewAdapter;
import com.zy.booking.util.EmojiUtils;
import com.zy.booking.util.MyThumbnailUtils;
import com.zy.booking.util.NetWorkUtil;
import com.zy.booking.util.PreferencesUtils;
import com.zy.booking.util.SoundMeter;
import com.zy.booking.wedget.DropdownListView;
import com.zy.booking.wedget.DropdownListView.OnRefreshListenerHeader;

@ContentView(R.layout.chat)
public class ChatActivity extends BaseActivity implements OnClickListener {

    /** Called when the activity is first created. */
    private Button mBtnSend;
    private TextView mBtnRcd;
    private EditText mEditTextContent;
    private RelativeLayout mBottom;
    private DropdownListView mListView;
    private ChatMsgViewAdapter mAdapter;
    private List<ChatMsgBean> mDataArrays = new ArrayList<ChatMsgBean>();
    private boolean isShosrt = false;
    private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding,
            voice_rcd_hint_tooshort;
    private ImageView img1, sc_img1, mBtnVoiceRecode;
    private SoundMeter mSensor;
    private View rcChat_popup;
    private LinearLayout del_re;
    private ImageView select_image, volume, emoji_choice;
    private boolean btn_vocie = false;
    private int flag = 1;
    private Handler mHandler = new Handler();
    private String voiceName;
    private long startVoiceT, endVoiceT;

    // emsg相关变量
    private EmsgClient mEmsgClient;

    private int RESULT_LOAD_IMAGE = 111;

    @ViewInject(R.id.headactionbar)
    private View mViewHead;

    @ViewInject(R.id.chat_container_body)
    private LinearLayout mLayoutBottomContainer;

    HeadLayoutComponents mLayoutComponent;

    MyFaceContainer mFaceContainer;

    @ViewInject(R.id.ll_bottom)
    private View mLLBottom;
    @ViewInject(R.id.ll_chatremotesesson)
    private View mLL_chatremotesesson;

    private static final HandlerThread sWorkerThread = new HandlerThread(
            "launcher-loader");
    static {
        sWorkerThread.start();
    }
    private static final Handler sWorker = new Handler(
            sWorkerThread.getLooper());

    private void runOnWorkerThread(Runnable r) {
        sWorker.post(r);
    }

    String mMessageTo;
    String mMessageFrom;

    private boolean isMsgFromAdmin = false;

    public static final String TAG_CHAT_NAME = "chat-name";
    public static final String TAG_MESSAGE_TO = "message-to";
    public static final String TAG_FROM_SERVER = "isFromServer";
    public static final String TAG_ISCHATGROUP = "isGroup";

    boolean isGroupChat = false;

    ChatPicsSelectComponents mChatPicsSelectComponents;

    DBUSER mDbUser = new DBUSER();

    private void getIntentData() {
        String msgTo = getIntent().getStringExtra(TAG_MESSAGE_TO);
        isGroupChat = getIntent().getBooleanExtra(TAG_ISCHATGROUP, false);
        if (msgTo != null) {
            if (!isGroupChat) {
                // 发送给某人
                mMessageTo = msgTo.contains(EmsgManager.EMSGAREA) ? msgTo
                        : msgTo + EmsgManager.EMSGAREA;
            } else {
                mMessageTo = msgTo.split("@")[0];
            }

        } else {
            showToastShort("没有发送方");
            finish();
        }

        mMessageFrom = PreferencesUtils.getString(
                CpApplication.getApplication(), "userid")
                + EmsgManager.EMSGAREA;

        isMsgFromAdmin = getIntent().getBooleanExtra(TAG_FROM_SERVER, false);

        if (mMessageTo.equals(mMessageFrom)) {
            finish();
            Toast.makeText(this, "不能给自己发消息", Toast.LENGTH_SHORT).show();
        }

        // 显示系统信息
        if (isMsgFromAdmin)
            mLLBottom.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEmsgClient = CpApplication.getApplication().getEmsgClient();

        // 启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView();

        getIntentData();
        initData();

        registerMessageReciver();

        mFaceContainer = new MyFaceContainer(null, this);
        mFaceContainer.setCallBack(new OnCallBack() {

            @Override
            public void onEmojiInsertCallBack(CharSequence mCharsequence) {
                EmojiUtils.insertEmojiToEditText(mEditTextContent,
                        mCharsequence);
            }

            @Override
            public void onDeleteEmojiCallBack() {
                EmojiUtils.deleteEmoji(mEditTextContent);
            }
        });

        if (!NetStateUtil.isNetWorkAlive(this) || !mEmsgClient.isAuth()) {
            updateUIWhenAuthError();
        }

        mChatPicsSelectComponents = new ChatPicsSelectComponents(this);
    }

    @OnClick(R.id.btn_send_voice)
    public void actionVoiceRecode(View mView) {
        mFaceContainer.showFaceContainer(View.GONE);

        if (btn_vocie) {
            mBtnRcd.setVisibility(View.GONE);
            mEditTextContent.setVisibility(View.VISIBLE);
            btn_vocie = false;
            mBtnVoiceRecode
                    .setImageResource(R.drawable.chatting_setmode_msg_btn);

        } else {
            mBtnRcd.setVisibility(View.VISIBLE);
            mEditTextContent.setVisibility(View.GONE);
            mBtnVoiceRecode
                    .setImageResource(R.drawable.chatting_setmode_voice_btn);

            btn_vocie = true;
        }

    }

    public void initView() {
        mListView = (DropdownListView) findViewById(R.id.listview);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnRcd = (TextView) findViewById(R.id.btn_rcd);

        mBtnSend.setOnClickListener(this);
        mBottom = (RelativeLayout) findViewById(R.id.btn_bottom);

        select_image = (ImageView) this.findViewById(R.id.chat_select_img);
        emoji_choice = (ImageView) this.findViewById(R.id.chat_show_emojipanel);

        mBtnVoiceRecode = (ImageView) this.findViewById(R.id.btn_send_voice);
        volume = (ImageView) this.findViewById(R.id.volume);
        rcChat_popup = this.findViewById(R.id.rcChat_popup);
        img1 = (ImageView) this.findViewById(R.id.img1);
        sc_img1 = (ImageView) this.findViewById(R.id.sc_img1);
        del_re = (LinearLayout) this.findViewById(R.id.del_re);
        voice_rcd_hint_rcding = (LinearLayout) this
                .findViewById(R.id.voice_rcd_hint_rcding);
        voice_rcd_hint_loading = (LinearLayout) this
                .findViewById(R.id.voice_rcd_hint_loading);
        voice_rcd_hint_tooshort = (LinearLayout) this
                .findViewById(R.id.voice_rcd_hint_tooshort);

        mSensor = new SoundMeter();
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
        mEditTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(mEditTextContent.getText().toString())) {
                    mBtnVoiceRecode.setVisibility(View.VISIBLE);
                    mBtnSend.setVisibility(View.INVISIBLE);
                } else {
                    mBtnVoiceRecode.setVisibility(View.INVISIBLE);
                    mBtnSend.setVisibility(View.VISIBLE);
                }

            }
        });

        emoji_choice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View sendImage) {
                mBtnRcd.setVisibility(View.GONE);
                mBottom.setVisibility(View.VISIBLE);
                int visiable = mLayoutBottomContainer.getVisibility();
                if (visiable == View.GONE) {
                    mLayoutBottomContainer.setVisibility(View.VISIBLE);
                    mLayoutBottomContainer.removeAllViews();
                    mLayoutBottomContainer.addView(mFaceContainer.mBaseView);
                } else {
                    mLayoutBottomContainer.setVisibility(View.GONE);
                }

            }

        });

        // 语音文字切换按钮
        select_image.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // Intent i = new Intent(
                // Intent.ACTION_PICK,
                // android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //
                // startActivityForResult(i, RESULT_LOAD_IMAGE);

                int visiable = mLayoutBottomContainer.getVisibility();
                if (visiable == View.GONE) {
                    mLayoutBottomContainer.setVisibility(View.VISIBLE);
                    mLayoutBottomContainer.removeAllViews();
                    mLayoutBottomContainer.addView(mChatPicsSelectComponents
                            .getView());
                } else {
                    mLayoutBottomContainer.setVisibility(View.GONE);
                }
            }
        });
        mBtnRcd.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // 按下语音录制按钮时返回false执行父类OnTouch
                return false;
            }
        });
        initListView();
    }
    
    private void initListView(){
        mListView.setOnRefreshListenerHead(new OnRefreshListenerHeader() {
            
            @Override
            public void onRefresh() {
                selectFromLocalDb(false,true);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent mIntent) {
        super.onActivityResult(requestCode, resultCode, mIntent);
        if (resultCode != RESULT_OK) {
            return;
        }
        showProgresssDialog();
        boolean isSaved = false;
        if (requestCode == ChatPicsSelectComponents.REQUEST_CODE_CAMERA) {
            isSaved = onActivityResult(
                    mChatPicsSelectComponents.mCurrentPhotoPath,
                    mChatPicsSelectComponents.mCurrentFileName);

        }
        if (requestCode == ChatPicsSelectComponents.REQUEST_CODE_LOCAL) {
            Uri uri = mIntent.getData();
            File mFileFromUri = FileUri.getFile(mContext, uri);

            String filePath = mFileFromUri.getAbsolutePath();
            isSaved = onActivityResult(filePath,
                    mChatPicsSelectComponents.mCurrentFileName);
        }

        if (isSaved) {
            sendImage(Uri.fromFile(new File(
                    mChatPicsSelectComponents.mCurrentPhotoPath)));
        }
        dismissProgressDialog();
    }

    private boolean onActivityResult(String filePath, String fileName) {
        try {
            Bitmap mBitmapThumbnail = MyThumbnailUtils.createImageThumbnail(
                    filePath, 480);
            boolean isCreateThumbFileSuccess = CpApplication.getApplication().mStorage
                    .createFile(AppDefine.APP_GLOBLEFILEPATH, fileName,
                            mBitmapThumbnail);
            if (isCreateThumbFileSuccess) {
                mBitmapThumbnail.recycle();
            }
            return true;
        } catch (Exception e) {
            showToastShort("图片处理失败请重新选择" + e.getMessage());
        }
        return false;

    }

    String mContent;

    public void initData() {

        mLayoutComponent = new HeadLayoutComponents(this, mViewHead);

        mLayoutComponent.setDefaultLeftCallBack(true);
        mContent = getIntent().getStringExtra("content");
        String mTitle = getIntent().getStringExtra(TAG_CHAT_NAME);

        if (isGroupChat) {
            mLayoutComponent.setTextMiddle(mTitle, -1);
            mLayoutComponent.setTextRight("", R.drawable.icon_group_tags);
            mLayoutComponent.setRightOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent mIntent = new Intent(mContext, SampleHolderActivity.class);
                    mIntent.putExtra(SampleHolderActivity.TAG_ID, mMessageTo);
                    mIntent.putExtra(SampleHolderActivity.TAG_TAG,
                            SampleHolderActivity.TAG_GROUPINFO);
                    mContext.startActivity(mIntent);
                }
            });
        } else {
            mLayoutComponent.setTextMiddle("正在同" + mTitle + "聊天", -1);
        }
        if (TextUtils.isEmpty(mTitle)) {
            mLayoutComponent.setTextMiddle("沟通无限", -1);
        }
        mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);
        selectFromLocalDb(true,false);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                send();
                break;
        }
    }

    private void send() {
        final String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {
            ChatMsgBean entity = getSendChatMsgBean(contString);

            mDataArrays.add(entity);
            mDbMsgHistory.saveData(entity);
            mAdapter.notifyDataSetChanged();

            mEditTextContent.setText("");

            mListView.setSelection(mListView.getCount() - 1);

            try {
                runOnWorkerThread(new Runnable() {

                    @Override
                    public void run() {
                        mEmsgClient.sendMessage(mMessageTo, contString,
                                isGroupChat ? MsgTargetType.GROUPCHAT
                                        : MsgTargetType.SINGLECHAT,
                                new EmsgCallBack() {

                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError(TypeError mTypeError) {

                                    }
                                });
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // 按下语音录制按钮时
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!Environment.getExternalStorageDirectory().exists()) {
            Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
            return false;
        }

        if (btn_vocie) {
            System.out.println("1");
            int[] location = new int[2];
            mBtnRcd.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
            int btn_rc_Y = location[1];
            int btn_rc_X = location[0];
            int[] del_location = new int[2];
            del_re.getLocationInWindow(del_location);
            int del_Y = del_location[1];
            int del_x = del_location[0];
            if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
                if (!Environment.getExternalStorageDirectory().exists()) {
                    Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
                    return false;
                }
                System.out.println("2");
                if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {// 判断手势按下的位置是否是语音录制按钮的范围内
                    System.out.println("3");
                    mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
                    rcChat_popup.setVisibility(View.VISIBLE);
                    voice_rcd_hint_loading.setVisibility(View.VISIBLE);
                    voice_rcd_hint_rcding.setVisibility(View.GONE);
                    voice_rcd_hint_tooshort.setVisibility(View.GONE);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (!isShosrt) {
                                voice_rcd_hint_loading.setVisibility(View.GONE);
                                voice_rcd_hint_rcding
                                        .setVisibility(View.VISIBLE);
                            }
                        }
                    }, 300);
                    img1.setVisibility(View.VISIBLE);
                    del_re.setVisibility(View.GONE);
                    startVoiceT = System.currentTimeMillis();
                    voiceName = startVoiceT + ".amr";
                    start(voiceName);
                    flag = 2;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {// 松开手势时执行录制完成
                System.out.println("4");
                mBtnRcd.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
                if (event.getY() >= del_Y
                        && event.getY() <= del_Y + del_re.getHeight()
                        && event.getX() >= del_x
                        && event.getX() <= del_x + del_re.getWidth()) {
                    rcChat_popup.setVisibility(View.GONE);
                    img1.setVisibility(View.VISIBLE);
                    del_re.setVisibility(View.GONE);
                    stop();
                    flag = 1;
                    File file = new File(
                            android.os.Environment
                                    .getExternalStorageDirectory()
                                    + "/"
                                    + voiceName);
                    Log.d("debug", file.getAbsolutePath());
                    if (file.exists()) {
                        file.delete();
                    }
                } else {

                    voice_rcd_hint_rcding.setVisibility(View.GONE);
                    stop();
                    endVoiceT = System.currentTimeMillis();
                    flag = 1;
                    int time = (int) ((endVoiceT - startVoiceT) / 1000);
                    if (time < 1) {
                        isShosrt = true;
                        voice_rcd_hint_loading.setVisibility(View.GONE);
                        voice_rcd_hint_rcding.setVisibility(View.GONE);
                        voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                voice_rcd_hint_tooshort
                                        .setVisibility(View.GONE);
                                rcChat_popup.setVisibility(View.GONE);
                                isShosrt = false;
                            }
                        }, 500);
                        return false;
                    }
                    File file = new File(
                            android.os.Environment
                                    .getExternalStorageDirectory()
                                    + "/"
                                    + voiceName);
                    sendVoice(file, time);
                    rcChat_popup.setVisibility(View.GONE);

                }
            }
            if (event.getY() < btn_rc_Y) {// 手势按下的位置不在语音录制按钮的范围内
                System.out.println("5");
                Animation mLitteAnimation = AnimationUtils.loadAnimation(this,
                        R.anim.cancel_rc);
                Animation mBigAnimation = AnimationUtils.loadAnimation(this,
                        R.anim.cancel_rc2);
                img1.setVisibility(View.GONE);
                del_re.setVisibility(View.VISIBLE);
                del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg);
                if (event.getY() >= del_Y
                        && event.getY() <= del_Y + del_re.getHeight()
                        && event.getX() >= del_x
                        && event.getX() <= del_x + del_re.getWidth()) {
                    del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg_focused);
                    sc_img1.startAnimation(mLitteAnimation);
                    sc_img1.startAnimation(mBigAnimation);
                }
            } else {

                img1.setVisibility(View.VISIBLE);
                del_re.setVisibility(View.GONE);
                del_re.setBackgroundResource(0);
            }
        }
        return super.onTouchEvent(event);
    }

    private static final int POLL_INTERVAL = 300;

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            stop();
        }
    };
    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();
            updateDisplay(amp);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);

        }
    };

    private void start(String name) {
        mSensor.start(name);
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    private void stop() {
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
        volume.setImageResource(R.drawable.amp1);
    }

    private void updateDisplay(double signalEMA) {

        switch ((int) signalEMA) {
            case 0:
            case 1:
                volume.setImageResource(R.drawable.amp1);
                break;
            case 2:
            case 3:
                volume.setImageResource(R.drawable.amp2);

                break;
            case 4:
            case 5:
                volume.setImageResource(R.drawable.amp3);
                break;
            case 6:
            case 7:
                volume.setImageResource(R.drawable.amp4);
                break;
            case 8:
            case 9:
                volume.setImageResource(R.drawable.amp5);
                break;
            case 10:
            case 11:
                volume.setImageResource(R.drawable.amp6);
                break;
            default:
                volume.setImageResource(R.drawable.amp6);
                break;
        }
    }

    private void updateUIWhenAuthError() {
        mLLBottom.setVisibility(View.GONE);
        mLL_chatremotesesson.setVisibility(View.VISIBLE);
    }

    private void updateUiWhenAuthCurrect() {
        dismissProgressDialog();
        if (!isMsgFromAdmin)
            mLLBottom.setVisibility(View.VISIBLE);
        mLL_chatremotesesson.setVisibility(View.GONE);
    }

    @OnClick(R.id.ll_chatremotesesson)
    public void actionViewReconnect(View mView) {
        if (!NetWorkUtil.isNetworkAvailable(this)) {
            showToastShort("网络不通!");
            return;
        }
        showProgresssDialog();
        CpApplication.getApplication().mEmsgManager.auth();
    }

    BroadcastReceiver mBroadCastReciver;

    // 注册接收当消息传递到的时候的广播
    private void registerMessageReciver() {
        IntentFilter mIntentFiter = new IntentFilter();
        mIntentFiter.addAction(EmsgManager.ACTION_NEWMSGCOMING);
        mIntentFiter.addAction(EmsgManager.ACTION_SESSOOPEND);
        mIntentFiter.addAction(EmsgManager.ACTION_SESSONCLOSED);
        mIntentFiter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mBroadCastReciver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context mContext, Intent mIntent) {

                String action = mIntent.getAction();
                if (action.equals(EmsgManager.ACTION_NEWMSGCOMING)) {
                    selectAddedMsgSqlFormat();
                } else if (action.equals(EmsgManager.ACTION_SESSOOPEND)) {
                    updateUiWhenAuthCurrect();
                } else if (action.equals(EmsgManager.ACTION_SESSONCLOSED)) {
                    updateUIWhenAuthError();

                } else if (action
                        .equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    if (!NetWorkUtil.isAnyNetConnected(mContext)) {
                        updateUIWhenAuthError();
                    }
                }
            }

        };
        registerReceiver(mBroadCastReciver, mIntentFiter);
    }

    private void unRegisterBrodCastReciver() {
        try {
            unregisterReceiver(mBroadCastReciver);
        } catch (Exception e) {

        }
    }

    private ChatMsgBean getSendChatMsgBean(String msgContent) {
        ChatMsgBean entity = new ChatMsgBean();
        entity.msgTime = System.currentTimeMillis();
        entity.name = "我";
        entity.msgFrom = mMessageFrom;
        entity.msgTo = mMessageTo;
        
        entity.column1 = EmsgManager.TYPE_COMMON;
        entity.gid = isGroupChat ? entity.msgTo.split("@")[0] : "";
        entity.msgContent = msgContent;
        entity.type = isGroupChat ? "2" : "1";
        entity.contentType = "text";
        entity.isRead = "1";// entity.contentType = "text";
        return entity;
    }

    void sendVoice(final File file, final int length) {

        ChatMsgBean entity = getSendChatMsgBean(voiceName);
        entity.contentType = "audio";
        mDbMsgHistory.saveData(entity);
        // entity.setTime(length + "\"");
        mDataArrays.add(entity);
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mListView.getCount() - 1);
        runOnWorkerThread(new Runnable() {

            @Override
            public void run() {

                mEmsgClient.sendAudioMessage(Uri.fromFile(file), length,
                        mMessageTo, null, isGroupChat ? MsgTargetType.GROUPCHAT
                                : MsgTargetType.SINGLECHAT, new EmsgCallBack() {

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(TypeError mTypeError) {
                            }
                        });
            }
        });

    }

    private void sendImage(final Uri imageUri) {

        runOnWorkerThread(new Runnable() {

            @Override
            public void run() {
                mEmsgClient.sendImageMessage(imageUri, mMessageTo, null,
                        isGroupChat ? MsgTargetType.GROUPCHAT
                                : MsgTargetType.SINGLECHAT, new EmsgCallBack() {

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(TypeError mTypeError) {

                            }
                        });
            }
        });

        ChatMsgBean entity = getSendChatMsgBean(FileUri.getFile(this, imageUri)
                .getAbsolutePath());
        entity.contentType = "image";
        mDataArrays.add(entity);
        mDbMsgHistory.saveData(entity);
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mListView.getCount() - 1);

    }


    Runnable runnableUi = new Runnable() {
        public void run() {
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(mListView.getCount() - 1);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterBrodCastReciver();
    }

    MSGHISTORY mDbMsgHistory = new MSGHISTORY();

    private void selectAddedMsgSqlFormat() {

        if (isGroupChat) {
            String sql = getString(R.string.select_chat_panel_groupaddmsg);
            String sqlFormat = getStringFormat(sql, mMessageTo);
            getDataFromDb(sqlFormat, false,false);
        } else {
            String sql = getString(R.string.select_chat_panel_addmsg);
            String sqlFormat = getStringFormat(sql, mMessageTo, mMessageFrom);
            getDataFromDb(sqlFormat, false,false);
        }

    }

    private void getDataFromDb(String sqlFormat, boolean isReverse,boolean isAddHead) {
        List<ChatMsgBean> mlocalData = mDbMsgHistory
                .selectDataFromDb(sqlFormat);

        if (mlocalData != null && mlocalData.size() > 0) {
            if(!isAddHead)
            mDataArrays.addAll(mlocalData);else{
                Collections.reverse(mlocalData);
                mDataArrays.addAll(0,mlocalData);
            }
            String sql = isGroupChat ? getString(R.string.update_isread_bygid)
                    : getString(R.string.update_isread_bymsgfrom);
            String updateSqlFormat = getStringFormat(sql, mMessageTo,EmsgManager.TYPE_COMMON);
            mDbMsgHistory.updateDataFromDb(updateSqlFormat);

            // mDbMsgHistory.updateAllUnReadStateByMsgFrom(mMessageTo,
            // mlocalData);
            if (isReverse)
                Collections.reverse(mDataArrays);
        }

        runOnUiThread(runnableUi);

    }

    
    //isReasver 需要不需要 翻转 ， isAddHead 是不是添加头
    
    private void selectFromLocalDb(boolean isReasver,boolean isAddHead) {
        //long OndayBefore = System.currentTimeMillis() - 60 * 60 * 24 * 1000;
        int currentSize = mDataArrays.size();
        int limitSize = currentSize+10;
        if (isGroupChat) {
            String mGroupChatSql = getString(R.string.select_msggrouphistorythreedays);
            String sqlFormat = getStringFormat(mGroupChatSql, mMessageTo,currentSize+"",limitSize+"");
            getDataFromDb(sqlFormat, isReasver,isAddHead);
        } else {
            String sql = getString(R.string.select_msghistorythreedays);
            String sqlFormat = getStringFormat(sql, mMessageFrom, mMessageTo,currentSize+"",limitSize+"");
            getDataFromDb(sqlFormat, isReasver,isAddHead);
        }
     
        if(isAddHead){
            mListView.setSelection(0);
            mListView.onRefreshCompleteHeader();
        }
        // String updateSql =
        // getString(R.string.update_chat_history_changehistoryreadstate);
        //
        // //更新昨天的数据为已经读
        // String sqlFormatUpdate = getStringFormat(updateSql,
        // OndayBefore + "");
        // mDbMsgHistory.updateDataFromDb(sqlFormatUpdate);
    }
}
