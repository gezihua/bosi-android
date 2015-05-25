package com.zy.booking.modle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emsg.sdk.util.JsonUtil;
import com.google.gson.JsonObject;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.qiniu.utils.FileUri;
import com.zy.booking.AppDefine;
import com.zy.booking.CpApplication;
import com.zy.booking.R;
import com.zy.booking.activitys.ChatActivity;
import com.zy.booking.activitys.ImageDitalActivity;
import com.zy.booking.activitys.SampleHolderActivity;
import com.zy.booking.activitys.ServiceDetailActivity;
import com.zy.booking.db.ChatMsgBean;
import com.zy.booking.util.EmojiUtils;
import com.zy.booking.util.PreferencesUtils;
import com.zy.booking.util.ThumbExtractor;
import com.zy.booking.util.TimeUtils;

public class ChatMsgViewAdapter extends SampleStructAdapter {

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();

	private List<ChatMsgBean> coll;

	private Context ctx;

	private LayoutInflater mInflater;
	private MediaPlayer mMediaPlayer = new MediaPlayer();

	String uid;

	public ChatMsgViewAdapter(Context context, List<ChatMsgBean> coll) {
		super(context, coll);
		ctx = context;
		this.coll = coll;
		mInflater = LayoutInflater.from(context);

		uid = PreferencesUtils.getString(context, "userid");
	}

	public int getCount() {
		return coll.size();
	}

	public Object getItem(int position) {
		return coll.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		ChatMsgBean entity = coll.get(position);

		if (!entity.msgFrom.contains(uid)) {
			return IMsgViewType.IMVT_COM_MSG;
		} else {
			return IMsgViewType.IMVT_TO_MSG;
		}

	}

	public int getViewTypeCount() {
		return 2;
	}

	private boolean isComingMsg(String msgComing) {
		return !msgComing.contains(uid);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		final ChatMsgBean entity = coll.get(position);
		
		entity.updateChatMsgBean(entity.msgFrom, ctx);

		ViewHolder viewHolder = null;
		final boolean isComingmsg = isComingMsg(entity.msgFrom);
		if (entity.msgFrom.startsWith(AppDefine.ADMIN)) {
			return getViewForAdmin(position, convertView, entity);
		}
		if (convertView == null) {
			
			if (isComingmsg) {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_left, null);
			} else {
				convertView = mInflater.inflate(
						R.layout.chatting_item_msg_right, null);
			}

			viewHolder = new ViewHolder();
			viewHolder.tvSendTime = (TextView) convertView
					.findViewById(R.id.tv_sendtime);

			viewHolder.tvUserName = (TextView) convertView
					.findViewById(R.id.tv_username);
			viewHolder.tvText = (TextView) convertView
					.findViewById(R.id.tv_chat_text);
			viewHolder.tvImage = (ImageView) convertView
					.findViewById(R.id.tv_chat_image);
			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.mImageViewHead = (ImageView) convertView
					.findViewById(R.id.iv_userhead);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (!TextUtils.isEmpty(entity.column0)) {
			mBitmapUtils.display(viewHolder.mImageViewHead, entity.column0,
					mConfig);
			
		}
		viewHolder.mImageViewHead.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                intentToUserInfo(entity.msgFrom);
            }
        });
		
		viewHolder.tvSendTime.setText(TimeUtils.getFormatDateTime(new Date(
				entity.msgTime), TimeUtils.yyyy_MM_ddHHMMSS));

		if (entity.contentType != null && entity.contentType.equals("audio")) {
			new File(android.os.Environment.getExternalStorageDirectory()
					+ "/emsg/receive/audio/").mkdirs();
			new File(android.os.Environment.getExternalStorageDirectory()
					+ "/emsg/send/audio/").mkdirs();
			viewHolder.tvText.setText("");
			viewHolder.tvText.setVisibility(View.VISIBLE);
			viewHolder.tvImage.setVisibility(View.GONE);
			viewHolder.tvText.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.chatto_voice_playing, 0);
			// viewHolder.tvTime.setText(entity.getTime());
			final String content = entity.msgContent;
			if (isComingMsg(entity.msgFrom)) {

				String filename = android.os.Environment
						.getExternalStorageDirectory()
						+ "/emsg/receive/audio/"
						+ getKeyFroComingMsg(content);
				if (!new File(filename).exists()) {
					new AudioTask(filename).execute(content);
				}
			}
			viewHolder.tvText.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (isComingmsg) {
						playMusic(android.os.Environment
								.getExternalStorageDirectory()
								+ "/emsg/receive/audio/"
								+ getKeyFroComingMsg(content));
					} else {
						playMusic(android.os.Environment
								.getExternalStorageDirectory() + "/" + content);
					}
				}
			});
		} else if (entity.contentType != null
				&& entity.contentType.equals("image")) {
			viewHolder.tvText.setVisibility(View.GONE);
			viewHolder.tvImage.setVisibility(View.VISIBLE);
			viewHolder.tvTime.setText("");

			new File(android.os.Environment.getExternalStorageDirectory()
					+ "/emsg/receive/image/thumb/").mkdirs();
			new File(android.os.Environment.getExternalStorageDirectory()
					+ "/emsg/receive/image/original/").mkdirs();
			new File(android.os.Environment.getExternalStorageDirectory()
					+ "/emsg/send/image/thumb/").mkdirs();
			new File(android.os.Environment.getExternalStorageDirectory()
					+ "/emsg/send/image/original/").mkdirs();

			final String content = entity.msgContent;
			if (isComingmsg) {
				final ImageView mImageView = viewHolder.tvImage;
				CpApplication.getApplication().mBitmapManager.disPlayWithListener(mImageView, content,new BitmapLoadCallBack<View>() {
					
					@Override
					public void onLoadFailed(View container, String uri, Drawable drawable) {
						
					}
					
					@Override
					public void onLoadCompleted(View container, String uri, Bitmap bitmap,
							BitmapDisplayConfig config, BitmapLoadFrom from) {
							mImageView.setImageBitmap(bitmap);
						setImageClickListener(container, uri);
					}
				});
				
			} else {
				String filename = entity.msgContent;
				CpApplication.getApplication().mBitmapManager.disPlayImage(viewHolder.tvImage, filename);
				setImageClickListener(viewHolder.tvImage, filename);
			}

		} else {
			SpannableStringBuilder sb = EmojiUtils.handler(viewHolder.tvText,
					entity.msgContent, ctx);
			viewHolder.tvText.setText(sb);
			viewHolder.tvText.setVisibility(View.VISIBLE);
			viewHolder.tvImage.setVisibility(View.GONE);
			viewHolder.tvTime.setText("");
		}

		viewHolder.tvUserName.setText(entity.msgFrom);

		return convertView;
	}

	private void setImageClickListener(View mImageView,
			final String filePath) {
		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent mIntent = new Intent(ctx, ImageDitalActivity.class);
				String [] mImageArray = new String[]{filePath};
				mIntent.putExtra(ImageDitalActivity.TAG_INTENT_FILEPATH,
						mImageArray);

				ctx.startActivity(mIntent);
			}
		});

	}

	private View getViewForAdmin(int position, View mView, ChatMsgBean mData) {
		// type for user_booking
		if (mView == null) {
			mView = LayoutInflater.from(ctx).inflate(
					R.layout.layout_emsg_admin, null);
		}
		final JsonObject mJsonData = JsonUtil.parse(mData.msgContent);

		if (mJsonData != null) {
			TextView mTvContent = com.zy.booking.util.ViewHolder.get(mView,
					R.id.tv_chat_text);
			TextView mTime = com.zy.booking.util.ViewHolder.get(mView,
					R.id.tv_sendtime);
			View mdital = com.zy.booking.util.ViewHolder.get(mView,
					R.id.tv_admin_detail);
			View mChatWithSp = com.zy.booking.util.ViewHolder.get(mView,
					R.id.tv_admin_chatwiwhisp);
			mTvContent.setText(JsonUtil.getAsString(mJsonData, "content"));
			mTime.setText(TimeUtils.getFormatDateTime(new Date(mData.msgTime), TimeUtils.yyyy_MM_ddHHMMSS));
			mdital.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					JsonObject mParams = JsonUtil.getAsJsonObject(mJsonData,
							"params");
					Intent mIntent = new Intent(ctx,
							ServiceDetailActivity.class);
					mIntent.putExtra(ServiceDetailActivity.TAG_ID,
							JsonUtil.getAsString(mParams, "sp"));
					ctx.startActivity(mIntent);
				}
			});
			mChatWithSp.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					JsonObject mParams = JsonUtil.getAsJsonObject(mJsonData,
							"params");
					Intent mIntent = new Intent(ctx, ChatActivity.class);
					mIntent.putExtra(ChatActivity.TAG_MESSAGE_TO,
							JsonUtil.getAsString(mParams, "userId"));
					ctx.startActivity(mIntent);
				}
			});

		}

		return mView;

	}

	private static class ViewHolder {
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvText;
		public ImageView tvImage;
		public TextView tvTime;
		public ImageView mImageViewHead;
	}

	public String getKeyFroComingMsg(String content) {
		return content.substring(content.lastIndexOf("/"), content.length());
	}

	/**
	 * @Description
	 * @param name
	 */
	private void playMusic(String name) {
		AudioManager audioManager = (AudioManager) ctx
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setSpeakerphoneOn(true);
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(name);
			mMediaPlayer.prepare();

			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class ImageTask extends AsyncTask<String, Void, Boolean> {
		ViewHolder viewHolder;
		String filename;

		public ImageTask(ViewHolder viewHolder, String filename) {
			this.viewHolder = viewHolder;
			this.filename = filename;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				String url = params[1];

				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response = client.execute(get);

				InputStream is = response.getEntity().getContent();

				FileOutputStream fos = new FileOutputStream(filename);
				int ch = 0;
				while ((ch = is.read()) != -1) {
					fos.write(ch);
				}
				fos.close();

				return true;
			} catch (Exception ex) {
				Log.e(TAG, "发送异常." + ex.getMessage(), ex);
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (result) {
				Bitmap bMap = BitmapFactory.decodeFile(filename);
				viewHolder.tvImage.setImageBitmap(bMap);
			}
		}
	}
	
	
	private void intentToUserInfo(String id ){
	    Intent mIntent = new Intent(ctx,SampleHolderActivity.class);
	    mIntent.putExtra(SampleHolderActivity.TAG_ID, id);
	    mIntent.putExtra(SampleHolderActivity.TAG_TAG, SampleHolderActivity.TAG_SHOWUSERINFO);
	    ctx.startActivity(mIntent);
	    ((Activity) ctx).finish();
	}

	class AudioTask extends AsyncTask<String, Void, Boolean> {
		String filename;

		public AudioTask(String filename) {
			this.filename = filename;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				String url = params[0];

				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response = client.execute(get);

				InputStream is = response.getEntity().getContent();

				FileOutputStream fos = new FileOutputStream(filename);
				int ch = 0;
				while ((ch = is.read()) != -1) {
					fos.write(ch);
				}
				fos.close();

				return true;
			} catch (Exception ex) {
				Log.e(TAG, "发送异常." + ex.getMessage(), ex);
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
		}
	}

}
