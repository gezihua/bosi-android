
package com.emsg.sdk;

import android.annotation.SuppressLint;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;

import com.emsg.sdk.EmsgCallBack.TypeError;
import com.emsg.sdk.beans.DefPacket;
import com.emsg.sdk.beans.DefPayload;
import com.emsg.sdk.beans.DefProvider;
import com.emsg.sdk.beans.IEnvelope;
import com.emsg.sdk.beans.IPacket;
import com.emsg.sdk.beans.IProvider;
import com.emsg.sdk.beans.EmsMessage;
import com.emsg.sdk.beans.Pubsub;
import com.emsg.sdk.client.android.asynctask.AbsFileServerTarget;
import com.emsg.sdk.client.android.asynctask.IUpLoadTask;
import com.emsg.sdk.client.android.asynctask.TaskCallBack;
import com.emsg.sdk.client.android.asynctask.qiniu.QiNiuFileServerTarget;
import com.emsg.sdk.util.NetStateUtil;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class EmsgClient implements Define {

	private BlockingQueue<String> heart_beat_ack = null;

	static MyLogger logger = new MyLogger(EmsgClient.class);

	private String jid = null;
	private String pwd = null;
	private String appKey = null;
	private String heart = null;
	private int heartBeat = 50 * 1000;
	private int socketTimeOut = 10 * 1000;

	private Socket socket = null;

	protected InputStream reader = null;
	protected OutputStream writer = null;

	public PacketReader<DefPayload> packetReader = null;
	public PacketWriter packetWriter = null;

	protected PacketListener<DefPayload> listener = null;
	private IProvider<DefPayload> provider = null;

	private boolean auth = false; // 用于返回当前认证状态
	private boolean isClose = true; // 用于返回当前连接状态
	private String reconnectSN = null;

	private volatile Context mAppContext;
	public static EmsgClient mEmsgClient;
	private WakeLock wakeLock;

	private HeatBeatManager mHeartBeatManger;
	private String mAppKey = null;
	public AtomicBoolean isLogOut = new AtomicBoolean(true);// 用于主动发起的断线 不需要重练

	private EmsgCallbackHolder mCallBackHolder;

	private Handler mMainHandler;

	private AbsFileServerTarget mFileServerTarget;
	private final static Object mObject = new Object();

	/**
	 * 用于获取EmsgClient 服务引擎对象 建议在主线程中操作
	 */
	public static EmsgClient getInstance() {
		synchronized (mObject) {
			if (mEmsgClient == null) {
				mEmsgClient = new EmsgClient();
			}
		}
		return mEmsgClient;
	}

	/**
	 * 初始化 emsg服务引擎相关参数
	 * 
	 * @param mAppContext
	 *            android上下文对象
	 */
	public void init(Context mAppContext) {
		this.mAppContext = mAppContext;
		startBgService();
		mMainHandler = new Handler();
		mCallBackHolder = new EmsgCallbackHolder(mAppContext, mMainHandler);
		mFileServerTarget = new QiNiuFileServerTarget(mAppContext);
	}

	private void startBgService() {
		try {
			mAppContext
					.startService(new Intent(mAppContext, EmsgService.class));
		} catch (Exception e) {
		}
	}

	public HeatBeatManager getHeartBeatManager() {
		return mHeartBeatManger;
	}

	private EmsgClient() {
		mHeartBeatManger = new HeatBeatManager();
		System.setProperty("emsg.packet.provider", DefProvider.class.getName());
		mEmsgClient = this;
		setProvider(new DefProvider());
		this.listener = new Receiver();
	}

	EmsgCallBack mAuthEmsgCallBack;

	/**
	 * 
	 * 登陆认证
	 * 
	 * @param jid
	 *            用户账户
	 * @param pwd
	 *            用户密码
	 * @mEmsgCallBack 回调接口用于判断认证成功与否
	 */
	public synchronized void auth(String jid, String pwd,
			EmsgCallBack mEmsgCallBack) {
		if (isAuth()) {
			if (mEmsgCallBack != null)
				mEmsgCallBack.onSuccess();
			return;
		}
		if (jid != null && pwd != null) {
			this.jid = jid;
			this.pwd = pwd;
		}
		if (mEmsgCallBack != null) {
			this.mAuthEmsgCallBack = mEmsgCallBack;
		}
		try {
			initConnection();
		} catch (SocketTimeoutException e) {
			if (mEmsgCallBack != null) {
				mEmsgCallBack.onError(TypeError.TIMEOUT);
			}
		} catch (Exception e) {
			if (mEmsgCallBack != null) {
				mEmsgCallBack.onError(TypeError.SOCKETERROR);
			}
		}
	}

	private void setAppKey() throws NameNotFoundException {
		String mPackageName = mAppContext.getPackageName();
		ApplicationInfo mAppInfo = mAppContext.getPackageManager()
				.getApplicationInfo(mPackageName, PackageManager.GET_META_DATA);
		this.appKey = mAppInfo.metaData.getString("myMsg");
	}

	void setPacketListener(PacketListener<DefPayload> listener) {
		// this.listener = listener;
	}

	private void initConnection() throws UnknownHostException, IOException,
			InterruptedException {
		this.socket = new Socket(EMSG_HOST, EMSG_PORT);
		reconnectSN = null;
		isClose = false;
		isLogOut.set(false);
		initReaderAndWriter();
		openSession();
		mHeartBeatManger.schduleNextHeartbeat();

		if (mEmsStateCallBack != null) {
			mEmsStateCallBack.onEmsgOpenedListener();
		}
	}

	private void openSession() throws InterruptedException {
		// {"envelope":{"id":"1234567890","type":0,"inner_token":"abc123"}}
		JsonObject j = new JsonObject();
		JsonObject envelope = new JsonObject();
		envelope.addProperty("id", UUID.randomUUID().toString());
		envelope.addProperty("type", MSG_TYPE_OPEN_SESSION);
		// envelope.put("inner_token", this.inner_token);
		envelope.addProperty("jid", this.jid);
		envelope.addProperty("pwd", this.pwd);
		envelope.addProperty("appkey", this.appKey);
		j.add("envelope", envelope);
		String open_session_packet = j.toString();
		logger.info("open_session ::> " + open_session_packet);
		packetWriter.write(open_session_packet);
	}

	/**
	 * 关闭Ems服务服务引擎
	 */
	public void closeClient() {
		isLogOut.set(true);
		shutdown();
		stopEmsService();
	}

	private void reconnection(String reconnectSN) {
		if (this.reconnectSN == null) {
			this.reconnectSN = reconnectSN;
			try {
				logger.debug("reconnect_do_at_" + reconnectSN);
				loop_queue.put("do");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			loop();
		} else {
			logger.info("======== reconnection_skip ========" + reconnectSN);
		}
	}

	private final BlockingQueue<String> loop_queue = new ArrayBlockingQueue<String>(
			2, true);;

	private void initReaderAndWriter() throws UnsupportedEncodingException,
			IOException {
		reader = socket.getInputStream();
		writer = socket.getOutputStream();

		packetReader = new PacketReader<DefPayload>(this);
		packetWriter = new PacketWriter();
		heart_beat_ack = new ArrayBlockingQueue<String>(2, true);
		this.heart = UUID.randomUUID().toString();
		new IOListener(heart);
	}

	/**
	 * 判断当前是否已经认证成功
	 * 
	 * @return true 当前已经认证
	 * @return false当前未认证
	 */
	public boolean isAuth() {
		return auth;
	}

	class IOListener extends PacketDecoder {
		Thread readThread = null;
		Thread writeThread = null;
		String _heart = null;

		IOListener(String _heart) {
			listenerRead();
			listenerWriter();
			this._heart = _heart;
		}

		void listenerRead() {
			readThread = new Thread() {
				public void run() {
					try {
						byte[] buff = new byte[1024];
						int len = 0;
						List<Byte> part = new ArrayList<Byte>();
						while ((len = reader.read(buff)) != 0 && len != -1) {// 当远程流断开时，会返�?0
							List<Byte> list = parseBinaryList(buff, len);
							List<String> packetList = new ArrayList<String>();
							List<Byte> new_part = new ArrayList<Byte>();
							splitByteArray(list, END_TAG, packetList, new_part,
									part);
							for (int i = 0; i < packetList.size(); i++) {
								String packet = packetList.get(i);
								// dispach heart beat and message
								if (HEART_BEAT.equals(packet)) {
									// 心跳单独处理
									heart_beat_ack.poll();
								} else if (SERVER_KILL.equals(packet)) {
									shutdown();
									isLogOut.set(true);
									runOnMainThread(new Runnable() {
										@Override
										public void run() {
											mEmsStateCallBack
													.onAnotherClientLogin();
										}
									});

                                } else {
                                    packetReader.recv(packet);
                                }
                                part.clear();
                            }
                            if (new_part != null && new_part.size() > 0) {
                                for (byte pb : new_part) {
                                    part.add(pb);
                                }
                            }
                        }
                        throw new Exception(
                                "emsg_retome_socket_closed__reader="
                                        + new String(buff));
                    } catch (Exception e) {
                        shutdown();
                        if (!isLogOut.get())
                            reconnection("listenerRead");
                    }
                }
            };
            readThread.setName("IOListener__read__" + new Date());
            readThread.setDaemon(true);
            readThread.start();
        }

		void listenerWriter() {
			writeThread = new Thread() {
				public void run() {
					try {
						while (true) {
							String msg = packetWriter.take();
							if (KILL.equals(msg)) {
								return;
							}
							if (!msg.endsWith(END_TAG)) {
								msg = msg + END_TAG;
							}
							logger.debug("IOListener_writer socket_is_close="
									+ isClose + " send_message ==> " + msg);
							writer.write(msg.getBytes());
							writer.flush();
						}
					} catch (Exception e) {
						e.printStackTrace();
						shutdown();
						reconnection("listenerWriter");
					}
				}
			};
			writeThread.setName("IOListener__writer__" + new Date());
			writeThread.setDaemon(true);
			writeThread.start();
		}
	}

    private void send(IPacket<DefPayload> packet, EmsgCallBack mEmsgCallBack)
            throws InterruptedException {
        if (isClose && mEmsgCallBack != null) {
            mEmsgCallBack.onError(TypeError.SESSIONCLOSED);
            return;
        }
        if (packet.getEnvelope().getFrom() == null) {
            packet.getEnvelope().setFrom(this.jid);
        }
        String id = UUID.randomUUID().toString();
        packet.getEnvelope().setId(id);
        mEmsgCallBack.mCallBackTime = System.currentTimeMillis();
        mCallBackHolder.addtoCollections(id, mEmsgCallBack);
        String encode_message = getProvider().encode(packet);
        packetWriter.write(encode_message);
    }

	protected void send(String message) throws InterruptedException {
		if(packetWriter ==null) return;
		packetWriter.write(message);
	}

	void shutdown() {
		mHeartBeatManger.stopSchduleHeartBeat();
		try {
			isClose = true;
			auth = false;
			if (packetReader != null) {
				packetReader.kill();
				packetReader = null;
			}
			if (packetWriter != null) {
				packetWriter.kill();
				packetWriter = null;
			}
			heart_beat_ack = null;
			if (socket != null && !socket.isClosed()) {
				socket.close();
			}
			logger.info("shutdown...");

			if (mEmsStateCallBack != null) {
				mEmsStateCallBack.onEmsgClosedListener();
			}

		} catch (Exception e1) {
		}
	}

	private int getHeartBeat() {
		return heartBeat;
	}

	public String getJid() {
		return jid;
	}

	IProvider<DefPayload> getProvider() {
		return provider;
	}

	void setProvider(IProvider<DefPayload> provider) {
		this.provider = provider;
	}

	/**
	 * 判断当和服务器是否处于连接状态
	 * 
	 * @return true 和服务器连接断开
	 * @return false 和服务器已经连接
	 */
	public boolean isClose() {
		return isClose;
	}

	void setAuth(boolean auth) {
		this.auth = auth;
	}

	void startEmsService() {
		Intent mIntent = new Intent(mAppContext, EmsgService.class);
		mAppContext.startService(mIntent);
	}

	void stopEmsService() {
		Intent mIntent = new Intent(mAppContext, EmsgService.class);
		mAppContext.stopService(mIntent);
	}

	private void loop() {
		String cmd = null;
		try {
			cmd = loop_queue.take();
			if (!NetStateUtil.isNetWorkAlive(mAppContext)) {
				return;
			}
			if ("do".equals(cmd)) {
				initConnection();
			}
			reconnectSN = null;
		} catch (Exception e) {
		} finally {
			if (Define.KILL.equals(cmd)) {
				logger.info("reconnect_thread_shutdown");
			}
		}
	}

    /**
     * manager the heartbeat use AlarmManager to setRepeat sendHeat data
     **/
    class HeatBeatManager {
        private HeartBeatReciver mHeartBeatReciver;
        private PendingIntent mPendingIntent;

		@SuppressLint("NewApi")
		public void schduleNextHeartbeat() {
			if (isLogOut.get())
				return;
			try {
				AlarmManager mAlarmManager = (AlarmManager) mAppContext
						.getSystemService(Context.ALARM_SERVICE);
				if (mHeartBeatReciver == null) {
					mHeartBeatReciver = new HeartBeatReciver();
					mAppContext.registerReceiver(mHeartBeatReciver,
							new IntentFilter("com.emsg.client"));
				}
				if (mPendingIntent == null) {
					Intent mIntent = new Intent("com.emsg.client");
					mPendingIntent = PendingIntent.getBroadcast(mAppContext, 0,
							mIntent, 0);
				}
				long mCurrentTimeMin = System.currentTimeMillis();
				mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						mCurrentTimeMin, getHeartBeat(), mPendingIntent);
			} catch (Exception e) {
			}
		}

		public void stopSchduleHeartBeat() {
			try {
				AlarmManager mAlarmManager = (AlarmManager) mAppContext
						.getSystemService(Context.ALARM_SERVICE);
				mAlarmManager.cancel(mPendingIntent);
				mAppContext.unregisterReceiver(mHeartBeatReciver);
				mHeartBeatReciver = null;
			} catch (Exception e) {
			}
		}

		public void sendHeartBeat() {
			startBgService();
			new Thread() {
				public void run() {
					try {
						if (isAuth()) {
							heart_beat_ack.add("1");
							send(HEART_BEAT);
							mCallBackHolder.checkOutTime();
						}
						if (isClose) {
							return;
						}
					} catch (Exception e) {
						shutdown();
						reconnection("heart_beat");
					}
				}
			}.start();
		}
	}

	private void runOnMainThread(Runnable mRunable) {
		if (mRunable != null)
			mMainHandler.post(mRunable);
	}

	private void runCallBackError(final EmsgCallBack mEmsgCallBack,
			final TypeError message) {
		if (mEmsgCallBack == null)
			return;
		mMainHandler.post(new Runnable() {

			@Override
			public void run() {
				mEmsgCallBack.onError(message);
			}
		});
	}

	private void runCallBackSuccess(final EmsgCallBack mEmsgCallBack) {
		if (mEmsgCallBack == null)
			return;
		mMainHandler.post(new Runnable() {

			@Override
			public void run() {
				mEmsgCallBack.onSuccess();
			}
		});
	}

	/**
	 * the Reciver to reciver the data from emsg-server
	 */
	public class Receiver implements PacketListener<DefPayload> {

		IProvider<DefPayload> provider = new DefProvider();

		@Override
		public void mediaPacket(IPacket<DefPayload> arg0) {
		}

		@Override
		public void processPacket(IPacket<DefPayload> packet) {
			Intent intent = new Intent();
			try {
				IEnvelope mEnveloper = packet.getEnvelope();
				int envolpeType = mEnveloper.getType();
				if (envolpeType == IEnvelope.TYPE_MESSAGE_SERVER) {// target
					if (mEnveloper.getFrom().equals("server_ack")) {
						String id = mEnveloper.getId();
						runCallBackSuccess(mCallBackHolder.onCallBackAction(id));
					}
				}else{
					EmsMessage message = insertMessage(packet);
					intent.setAction(EmsgConstants.MSG_ACTION_RECDATA);
					Bundle bundle = new Bundle();
					bundle.putParcelable("message", message);
					intent.putExtras(bundle);
					mAppContext.sendBroadcast(intent);
				}

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

		private EmsMessage insertMessage(IPacket<DefPayload> packet)
				throws Exception {
			String spacket = provider.encode(packet);
			IEnvelope envelope = packet.getEnvelope();
			EmsMessage message = new EmsMessage();
			message.setMid(envelope.getId());
			message.setmAccFrom(envelope.getFrom());
			message.setmAccTo(envelope.getTo());
			message.setGid(envelope.getGid());
			message.setType(envelope.getType());
			message.setCt(System.currentTimeMillis());
			String contentType = EmsgConstants.MSG_TYPE_FILETEXT;
			if (packet.getPayload() != null) {
				Map<String, String> mExtendMap = packet.getPayload().getAttrs();
				message.setmExtendsMap(new HashMap<String, String>(mExtendMap));
				String mRecContentType = mExtendMap.get("Content-type");
				if (!TextUtils.isEmpty(mRecContentType))
					contentType = mRecContentType;
				message.setContentType(contentType);
				message.setContentLength(packet.getPayload().getAttrs()
						.get("Content-length"));
				String content = packet.getPayload().getContent();
				if (contentType.equals(EmsgConstants.MSG_TYPE_FILEIMG)) {
					message.setContent(mFileServerTarget
							.getImageUrlPath(content));
				} else if (contentType.equals(EmsgConstants.MSG_TYPE_FILEAUDIO)) {
					message.setContent(mFileServerTarget
							.getAudioUrlPath(content));
				} else {
					message.setContent(content);
				}

			} else {
				message.setContent(spacket);
			}
			return message;
		}

		@Override
		public void sessionPacket(IPacket<DefPayload> packet) {
			if (packet.getEnvelope().getType() == 0) {

				if ("ok".equals(packet.getEntity().getResult())) {
					if (mAuthEmsgCallBack != null) {
						runCallBackSuccess(mAuthEmsgCallBack);
						mAuthEmsgCallBack = null;
					}
				} else {
					if (mAuthEmsgCallBack != null)
						runCallBackError(mAuthEmsgCallBack, TypeError.AUTHERROR);
				}
			}
		}

		@Override
		public void offlinePacket(List<IPacket<DefPayload>> packets) {
			try {
				for (IPacket<DefPayload> packet : packets) {
					EmsMessage message = insertMessage(packet);
					Intent intent = new Intent();
					intent.setAction(EmsgConstants.MSG_ACTION_RECOFFLINEDATA);
					Bundle bundle = new Bundle();
					bundle.putParcelable("message", message);
					intent.putExtras(bundle);
					mAppContext.sendBroadcast(intent);
				}
			} catch (Exception e) {
			}
		}

		@Override
		public void pubsubPacket(Pubsub pubsub) {
		}
	}

	/**
	 * 发送普通文本消息
	 * 
	 * @param msgTo
	 *            消息发送给对方的账户
	 * @param content
	 *            文本内容
	 * @param mTargetType
	 *            消息类型枚举 SINGLECHAT 单聊，GROUPCHAT群聊
	 * @param mCallBack
	 *            用于发送成功与否的回调
	 **/
	public void sendMessage(String msgTo, String content,
			MsgTargetType mTargetType, EmsgCallBack mCallBack) {
		if (msgTo == null || mCallBack == null) {
			return;
		}
		if (!NetStateUtil.isNetWorkAlive(mAppContext)) {
			runCallBackError(mCallBack, TypeError.NETERROR);
			return;
		}
		int type = (mTargetType == MsgTargetType.SINGLECHAT ? 1 : 2);
		IPacket<DefPayload> packet = new DefPacket(msgTo, content, type);

		try {
			send(packet, mCallBack);
		} catch (Exception e) {
			runCallBackError(mCallBack, TypeError.SOCKETERROR);
		}
	}

	public void sendMessageWithExtendMsg(String msgTo, String content,
			MsgTargetType mTargetType, EmsgCallBack mCallBack,
			Map<String, String> mExtendMap) {

		if (msgTo == null || mCallBack == null) {
			return;
		}
		if (!NetStateUtil.isNetWorkAlive(mAppContext)) {
			runCallBackError(mCallBack, TypeError.NETERROR);
			return;
		}
		int type = (mTargetType == MsgTargetType.SINGLECHAT ? 1 : 2);
		IPacket<DefPayload> packet = new DefPacket(msgTo, content, type, 1,
				mExtendMap);
		try {
			send(packet, mCallBack);
		} catch (Exception e) {
			runCallBackError(mCallBack, TypeError.SOCKETERROR);
		}

	}

	/**
	 * 使用sdk默认文件服务器进行图片文件的发送
	 * 
	 * @param uri
	 *            图片文件对应的Uri
	 * @param msgTo
	 *            发送给对方的账号
	 * @param mDataMap
	 *            用于消息扩展 (无则传null)
	 * @param mTargetType
	 *            消息类型枚举 SINGLECHAT 单聊，GROUPCHAT群聊
	 * @param mCallBack
	 *            用于发送成功与否的回调
	 */
	public void sendImageMessage(Uri uri, final String msgTo,
			final Map<String, String> mDataMap,
			final MsgTargetType mTargetType, final EmsgCallBack mCallBack) {

        if (mCallBack == null) {
            return;
        }
        if (!NetStateUtil.isNetWorkAlive(mAppContext)) {
            runCallBackError(mCallBack, TypeError.NETERROR);
            return;
        }
        IUpLoadTask task = mFileServerTarget.getUpLoadTask();
        task.upload(uri, new TaskCallBack() {

			@Override
			public void onSuccess(String message) {
				sendImageTextMessage(mDataMap, msgTo, mTargetType, mCallBack,
						message);
			}

			@Override
			public void onFailure() {
				runCallBackError(mCallBack, TypeError.FILEUPLOADERROR);
			}
		});
	}

	/**
	 * 使用sdk默认文件服务器进行音频文件的发送
	 * 
	 * @param uri
	 *            图片文件对应的Uri
	 * @param voiceDuring
	 *            音频文件时长
	 * @param messageTo
	 *            发送给对方的账号
	 * @param mDataMap
	 *            用于消息扩展 (无则传null)
	 * @param mTargetType
	 *            消息类型枚举 SINGLECHAT 单聊，GROUPCHAT群聊
	 * @param mCallBack
	 *            用于消息发送成功与否的回调
	 */
	public void sendAudioMessage(Uri uri, final int voiceDuring,
			final String msgTo, final Map<String, String> mDataMap,
			final MsgTargetType mTargetType, final EmsgCallBack mCallBack) {
		if (mCallBack == null) {
			return;
		}
		if (!NetStateUtil.isNetWorkAlive(mAppContext)) {
			runCallBackError(mCallBack, TypeError.NETERROR);
			return;
		}
		IUpLoadTask task = mFileServerTarget.getUpLoadTask();
		task.upload(uri, new TaskCallBack() {

			@Override
			public void onSuccess(String message) {
				sendAudioTextMessage(mDataMap, voiceDuring, msgTo, mTargetType,
						mCallBack, message);
			}

			@Override
			public void onFailure() {
				runCallBackError(mCallBack, TypeError.FILEUPLOADERROR);
			}
		});
	}

	/**
	 * 自备文件服务器时发送语音相关信息
	 * 
	 * @param mDataMap
	 *            用于消息扩展 (无则传null)
	 * @param voiceDuring
	 *            音频文件时长
	 * @param msgTo
	 *            发送给对方的账号
	 * @param mTargetType
	 *            消息类型枚举 SINGLECHAT 单聊，GROUPCHAT群聊
	 * @param mCallBack
	 *            用于消息发送成功与否的回调
	 * @param content
	 *            发送的音频在文件服务器的相关信息用于对方接收到消息后的对音频文件的 下载
	 */
	public void sendAudioTextMessage(Map<String, String> mDataMap,
			int voiceDuring, String msgTo, MsgTargetType mTargetType,
			EmsgCallBack mCallBack, String content) {
		Map<String, String> mExtendMap = null;
		if (mDataMap == null) {
			mExtendMap = new HashMap<String, String>();
		} else {
			mExtendMap = mDataMap;
		}
		mExtendMap.put("Content-type", EmsgConstants.MSG_TYPE_FILEAUDIO);
		mExtendMap.put("Content-length", String.valueOf(voiceDuring));
		String to = msgTo;
		int type = (mTargetType == MsgTargetType.SINGLECHAT ? 1 : 2);
		IPacket<DefPayload> packet = new DefPacket(to, content, type, 1,
				mExtendMap);
		try {
			send(packet, mCallBack);
		} catch (Exception e) {
			runCallBackError(mCallBack, TypeError.SOCKETERROR);
		}
	}

	/**
	 * 自备文件服务器时发送图片相关信息
	 * 
	 * @param mDataMap
	 *            用于消息扩展 (无则传null)
	 * @param msgTo
	 *            发送给对方的账号
	 * @param mTargetType
	 *            消息类型枚举 SINGLECHAT 单聊，GROUPCHAT群聊
	 * @param mCallBack
	 *            用于消息发送成功与否的回调
	 * @param content
	 *            发送的图片在文件服务器的相关信息用于对方接收到消息后的对图片文件的 下载
	 */
	public void sendImageTextMessage(Map<String, String> mDataMap,
			String msgTo, MsgTargetType mTargetType, EmsgCallBack mCallBack,
			String content) {

		Map<String, String> mExtendMap = null;
		if (mDataMap == null) {
			mExtendMap = new HashMap<String, String>();
		} else {
			mExtendMap = mDataMap;
		}
		mExtendMap.put("Content-type", EmsgConstants.MSG_TYPE_FILEIMG);
		String to = msgTo;
		int type = (mTargetType == MsgTargetType.SINGLECHAT ? 1 : 2);
		IPacket<DefPayload> packet = new DefPacket(to, content, type, 1,
				mExtendMap);
		try {
			send(packet, mCallBack);
		} catch (Exception e) {
			runCallBackError(mCallBack, TypeError.SOCKETERROR);
		}
	}

	public enum MsgTargetType {
		SINGLECHAT, GROUPCHAT
	}

	EmsStateCallBack mEmsStateCallBack;

	/**
	 * 设置对ems服务连接状态的监控
	 * 
	 * @param mEmsClosedCallback
	 *            服务连接状态接口
	 */
	public void setEmsStCallBack(EmsStateCallBack mEmsClosedCallback) {
		this.mEmsStateCallBack = mEmsClosedCallback;
	}

	/**
	 * 服务连接状态接口(待扩展) {@value onAnotherClientLogin} 其他客户端登陆时连接断开提示
	 */
	public interface EmsStateCallBack {
		public void onAnotherClientLogin();

		public void onEmsgClosedListener();

		public void onEmsgOpenedListener();

	}

}
