package com.bosi.chineseclass.views;


import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;

/**
 * Displays a video file. The VideoView class can load images from various
 * sources (such as resources or content providers), takes care of computing its
 * measurement from the video so that it can be used in any layout manager, and
 * provides various display options such as scaling and tinting.
 * 
 * mVideoView.setVideoPath("http://www.yuwen100.cn/yuwen100/zy/hanzi-flash/120001.mp4");
 */
public class VideoView extends SurfaceView implements MediaPlayerControl {
	private String TAG = "VideoView";
	// settable by the client
	/**
	 * ��ƵURI·��
	 */
	private Uri mUri;
	/**
	 * ��Ƶ����Ƶ�ļ�����ʱ���ܳ���
	 */
	private int mDuration;
	
	private Context mContext;

	// all possible internal states
	private static final int STATE_ERROR = -1;
	/**
	 * MediaPlayer�Ŀ���״̬
	 */
	private static final int STATE_IDLE = 0;
	/**
	 * MediaPlayer����׼����״̬
	 */
	private static final int STATE_PREPARING = 1;
	/**
	 * MediaPlayer׼�����˵�״̬
	 */
	private static final int STATE_PREPARED = 2;
	/**
	 * MediaPlayer�����е�״̬
	 */
	private static final int STATE_PLAYING = 3;
	/**
	 * MediaPlayer��ͣ��״̬
	 */
	private static final int STATE_PAUSED = 4;
	/**
	 * MediaPlayer������ɵ�״̬
	 */
	private static final int STATE_PLAYBACK_COMPLETED = 5;

	// mCurrentState is a VideoView object's current state.
	// mTargetState is the state that a method caller intends to reach.
	// For instance, regardless the VideoView object's current state,
	// calling pause() intends to bring the object to a target state
	// of STATE_PAUSED.
	/**Ĭ���ǿ���״̬w
	 */
	private int mCurrentState = STATE_IDLE;
	/**Ĭ���ǿ���״̬
	 */
	private int mTargetState = STATE_IDLE;

	// All the stuff we need for playing and showing a video
	/**
	 * ���ź���ʾ��Ƶ��Ҫ,����ʾ��Ƶ�İ����ࡣ
	 */
	private SurfaceHolder mSurfaceHolder = null;
	/**
	 * ������Ƶ��������ʵ���ǿ�����ࡣ
	 */
	private MediaPlayer mMediaPlayer = null;
	/**
	 * ��Ƶ�ļ�����ʱ��ʾ���
	 */
	private int mVideoWidth;
	/**
	 * ��Ƶ�ļ�����ʱ��ʾ�߶�
	 */
	private int mVideoHeight;
	/**
	 * ��Ƶ�ļ�����ʱ��ʾ�߶�
	 */
	private int mSurfaceWidth;
	private int mSurfaceHeight;
	/**
	 * ý�������壬��������ͣ�������ˡ��϶����Ȱ�ť�Ϳؼ���
	 */
	private MediaController mMediaController;
	/**
	 * ������������Ƶ����Ƶ����Ƶ�������MediaPlayer����߼�����-�����Ҳ������ˡ�
	 */
	private OnCompletionListener mOnCompletionListener;
	/**
	 * add by yangguangfu
	 * ������������Ƶ����Ƶ����Ƶ����ǰ�û���
	 */
	private OnBufferingUpdateListener mOnBufferingUpdateListener;
	/**
	 * ������������Ƶ����Ƶ����Ƶ����ǰ׼������MediaPlayer����߸ü�����-����׼���ÿ��Բ����˵����ҿ�ʼ�����ˡ�
	 */
	private MediaPlayer.OnPreparedListener mOnPreparedListener;
	
	 private MediaPlayer.OnInfoListener mOnInfoListener;
	/**
	 * 
	 * ������Ƶ������İٷֱȣ�������ʾ�û�����Ƶ���ض����ˣ��ò����û����ż���
	 */
	
	private int mCurrentBufferPercentage;
	/**
	 * 
	 * ��������MediaPlayer������Ƶʱ�����߲�����Ƶʱ�������?MediaPlayer����߸ü�����-"���������Ҳ��ŵ���Ƶ��ʽ��AVI�����Ҳ�֧�֣��Ҳ�����"
	 */
	private OnErrorListener mOnErrorListener;
	private int mSeekWhenPrepared; // recording the seek position while
	/**
	 * �Ƿ�����ͣ
	 */
	private boolean mCanPause;
	/**
	 * �Ƿ��ܿ���
	 */
	private boolean mCanSeekBack;
	/**
	 * �Ƿ��ܿ��
	 */
	private boolean mCanSeekForward;
	
	/**
	 *  add by yangguangfu
	 *  �Զ�������������ڼ������δ�С�Ƿ�ı�
	 */
	private MySizeChangeLinstener mMyChangeLinstener;
	/**
	 *  add by yangguangfu
	 *  �Զ�������������ڼ������δ�С�Ƿ�ı�
	 */
	interface MySizeChangeLinstener {
		void doMyThings();
	}

	/**
	 *  add by yangguangfu
	 *  �Զ�������������ڼ������δ�С�Ƿ�ı�,�����ṩ����������á�
	 */
	public void setMySizeChangeLinstener(MySizeChangeLinstener l) {
		mMyChangeLinstener = l;
	}
	/**
	 * 
	 * context ��ͼ���е�Ӧ�ó��������ģ�ͨ������Է��ʵ�ǰ���⡢��Դ�ȵȡ�
	 */
	public VideoView(Context context) {
		super(context);
		initVideoView();
		// add by yangguangfu
		this.mContext = context;
	}
	/**
	 * 
	 * context ��ͼ���е�Ӧ�ó��������ģ�ͨ������Է��ʵ�ǰ���⡢��Դ�ȵȡ� attrs ������
	
	 *	ͼ�� XML ��ǩ���Լ��ϡ�
	 */
	public VideoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initVideoView();
		// add by yangguangfu
		this.mContext = context;
	}
	/**
	 * 
	 * context ��ͼ���е�Ӧ�ó��������ģ�ͨ������Է��ʵ�ǰ���⡢��Դ�ȵȡ�
	 * 
	 * attrs ������ͼ�� XML ��ǩ���Լ��ϡ�
	 * 
	 * defStyle Ӧ�õ���ͼ��Ĭ�Ϸ�����Ϊ 0 ��Ӧ�ã�������ǰ�����еģ���� ��ֵ��

���ǵ�ǰ�����е�������Դ����������ȷ�ķ����ԴID��
	 */
	public VideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initVideoView();
		// add by yangguangfu
		this.mContext = context;
	}
	/**
	 * ��ʹ�� View ǰ��Ҫ���õķ���. ֪ͨView��������ߴ����.
	 * ����Լ���д�Ļ������������Сע����Ҫ����setMeasuredDimension(int, int);�������

		���ÿ� ����С.
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Log.i("@@@@", "onMeasure");
		// Modify by yangguangfu
		// int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
		// int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
		// if (mVideoWidth > 0 && mVideoHeight > 0) {
		// if (mVideoWidth * height > width * mVideoHeight) {
		// // Log.i("@@@", "image too tall, correcting");
		// height = width * mVideoHeight / mVideoWidth;
		// } else if (mVideoWidth * height < width * mVideoHeight) {
		// // Log.i("@@@", "image too wide, correcting");
		// width = height * mVideoWidth / mVideoHeight;
		// } else {
		// // Log.i("@@@", "aspect ratio is correct: " +
		// // width+"/"+height+"="+
		// // mVideoWidth+"/"+mVideoHeight);
		// }
		// }
		// // Log.i("@@@@@@@@@@", "setting size: " + width + 'x' + height);
		// setMeasuredDimension(width, height);

		int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
		int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
		setMeasuredDimension(width, height);

	}

	public int resolveAdjustedSize(int desiredSize, int measureSpec) {
		int result = desiredSize;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		switch (specMode) {
		case MeasureSpec.UNSPECIFIED:
			/*
			 * Parent says we can be as big as we want. Just don't be larger
			 * than max size imposed on ourselves.
			 */
			result = desiredSize;
			break;

		case MeasureSpec.AT_MOST:
			/*
			 * Parent says we can be as big as we want, up to specSize. Don't be
			 * larger than specSize, and don't be larger than the max size
			 * imposed on ourselves.
			 */
			result = Math.min(desiredSize, specSize);
			break;

		case MeasureSpec.EXACTLY:
			// No choice. Do what we are told.
			result = specSize;
			break;
		}
		return result;
	}

	/**
	 * ������Ƶ��ģ,�Ƿ�ȫ�� add by yangguangfu
	 * 
	 * @param width
	 * @param height
	 */
	public void setVideoScale(int width, int height) {
		LayoutParams lp = getLayoutParams();
		lp.height = height;
		lp.width = width;
		setLayoutParams(lp);
	}
	/**
	 * ��ʼ��VideoView��������ز���
	 */
	private void initVideoView() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		// ��SurfaceView��ǰ�ĳ�����һ���ص��������û������������Ǻ���
		getHolder().addCallback(mSHCallback);
		// ��������SurfaceView��ά���Լ��Ļ�����,���ǵȴ���Ļ����Ⱦ���潫�������͵��û���ǰ
		getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
//		getHolder().setType(SurfaceHolder.SURFACE_TYPE_NORMAL);//�����
		// ͨ��setFocusable().������SurfaceView���ܽ�����ʸ�,
		setFocusable(true);
		// ��Ӧ�ڴ���ģʽ�£�����Ե���isFocusableInTouchMode().����֪�Ƿ��н�������Ӧ�㴥��
		// Ҳ����ͨ��setFocusableInTouchMode().�������Ƿ��н�������Ӧ�㴥���ʸ�.
		setFocusableInTouchMode(true);
		// ���û�������ĳ������ۼ�����ʱ�������requestFocus().���������
		requestFocus();
		mCurrentState = STATE_IDLE;
		mTargetState = STATE_IDLE;
	}
	/**
	 * ����Ҫ������Ƶ����Ƶ��·����
	 * @param path ��Ƶ����Ƶ·��
	 */
	public void setVideoPath(String path) {
		setVideoURI(Uri.parse(path));
	}
	/**
	 * ����Ҫ������Ƶ����Ƶ��·����
	 * @param uri ��Ƶ����Ƶ·��
	 */
	public void setVideoURI(Uri uri) {
		mUri =Uri.parse("http://www.yuwen100.cn/yuwen100/hzzy/jbzy-clips/video/1.wmv") ;
		mSeekWhenPrepared = 0;
		openVideo();
		//��ĳЩ�������ͼ�Ĳ���ʧЧʱ���ø÷������÷���������ͼ����˳����á�
		requestLayout();
		//������ͼ
		invalidate();
	}
	/**
	 * ֹͣ���ţ����ͷ���Դ����MediaPlayer���ڿ���״̬��
	 */
	public void stopPlayback() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
			mCurrentState = STATE_IDLE;
			mTargetState = STATE_IDLE;
		}
	}
	/**
	 * ����Ƶ·������������ز���-�÷�����Ҫ��
	 */
	private void openVideo() {
		if (mUri == null || mSurfaceHolder == null) {
			// not ready for playback just yet, will try again later
			return;
		}
		// Tell the music playback service to pause
		// TODO: these constants need to be published somewhere in the
		// framework.
		Intent i = new Intent("com.android.music.musicservicecommand");
		i.putExtra("command", "pause");
		mContext.sendBroadcast(i);

		// we shouldn't clear the target state, because somebody might have
		// called start() previously
		release(false);
		try {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setOnPreparedListener(mPreparedListener);
			mMediaPlayer.setOnInfoListener(mOnInfoListeners);
			mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
			mDuration = -1;
			mMediaPlayer.setOnCompletionListener(mCompletionListener);
			mMediaPlayer.setOnErrorListener(mErrorListener);
			mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
			mCurrentBufferPercentage = 0;
			mMediaPlayer.setDataSource(mContext, mUri);
			mMediaPlayer.setDisplay(mSurfaceHolder);
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setScreenOnWhilePlaying(true);
			mMediaPlayer.prepareAsync();
			// we don't set the target state here either, but preserve the
			// target state that was there before.
			mCurrentState = STATE_PREPARING;
			attachMediaController();
		} catch (IOException ex) {
			Log.w(TAG, "Unable to open content: " + mUri, ex);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			mErrorListener.onError(mMediaPlayer,
					MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
			return;
		} catch (IllegalArgumentException ex) {
			Log.w(TAG, "Unable to open content: " + mUri, ex);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			mErrorListener.onError(mMediaPlayer,
					MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
			return;
		}
	}
	/**
	 * ����ϵͳĬ�Ͽ������
	 * @param controller
	 */
	public void setMediaController(MediaController controller) {
		if (mMediaController != null) {
			mMediaController.hide();
		}
		mMediaController = controller;
		attachMediaController();
	}
	/**
	 * ��ʾϵͳĬ�Ͽ������
	 */
	private void attachMediaController() {
		if (mMediaPlayer != null && mMediaController != null) {
			mMediaController.setMediaPlayer(this);
			View anchorView = this.getParent() instanceof View ? (View) this
					.getParent() : this;
			mMediaController.setAnchorView(anchorView);
			mMediaController.setEnabled(isInPlaybackState());
		}
	}

	MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
		public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
			mVideoWidth = mp.getVideoWidth();
			mVideoHeight = mp.getVideoHeight();
			//add by yangguangfu
			if (mMyChangeLinstener != null) {
				mMyChangeLinstener.doMyThings();
			}
			if (mVideoWidth != 0 && mVideoHeight != 0) {
				getHolder().setFixedSize(mVideoWidth, mVideoHeight);
			}
		}
	};
	
	MediaPlayer.OnInfoListener mOnInfoListeners = new MediaPlayer.OnInfoListener() {
		
		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			// TODO Auto-generated method stub
			
			if(mOnInfoListener != null){
				
				mOnInfoListener.onInfo(mp, what, extra);
			}
			return true;
		}
	};

	MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
			mCurrentState = STATE_PREPARED;
			// modify by yangguangfu
			// // Get the capabilities of the player for this stream
			// Metadata data = mp.getMetadata(MediaPlayer.METADATA_ALL,
			// MediaPlayer.BYPASS_METADATA_FILTER);
			//
			// if (data != null) {
			// mCanPause = !data.has(Metadata.PAUSE_AVAILABLE)
			// || data.getBoolean(Metadata.PAUSE_AVAILABLE);
			// mCanSeekBack = !data.has(Metadata.SEEK_BACKWARD_AVAILABLE)
			// || data.getBoolean(Metadata.SEEK_BACKWARD_AVAILABLE);
			// mCanSeekForward = !data.has(Metadata.SEEK_FORWARD_AVAILABLE)
			// || data.getBoolean(Metadata.SEEK_FORWARD_AVAILABLE);
			// } else {
			// mCanPause = mCanSeekForward = mCanSeekForward = true;
			// }
			// add by yangguangfu
			mCanPause = mCanSeekBack = mCanSeekForward = true;
			if (mOnPreparedListener != null) {
				mOnPreparedListener.onPrepared(mMediaPlayer);
			}
			if (mMediaController != null) {
				mMediaController.setEnabled(true);
			}
			mVideoWidth = mp.getVideoWidth();
			mVideoHeight = mp.getVideoHeight();

			int seekToPosition = mSeekWhenPrepared; // mSeekWhenPrepared may be
			// changed after seekTo()
			// call
			if (seekToPosition != 0) {
				seekTo(seekToPosition);
			}
			if (mVideoWidth != 0 && mVideoHeight != 0) {
				// Log.i("@@@@", "video size: " + mVideoWidth +"/"+
				// mVideoHeight);
				getHolder().setFixedSize(mVideoWidth, mVideoHeight);
				if (mSurfaceWidth == mVideoWidth
						&& mSurfaceHeight == mVideoHeight) {
					// We didn't actually change the size (it was already at the
					// size
					// we need), so we won't get a "surface changed" callback,
					// so
					// start the video here instead of in the callback.
					if (mTargetState == STATE_PLAYING) {
						start();
						if (mMediaController != null) {
							mMediaController.show();
						}
					} else if (!isPlaying()
							&& (seekToPosition != 0 || getCurrentPosition() > 0)) {
						if (mMediaController != null) {
							// Show the media controls when we're paused into a
							// video and make 'em stick.
							mMediaController.show(0);
						}
					}
				}
			} else {
				// We don't know the video size yet, but should start anyway.
				// The video size might be reported to us later.
				if (mTargetState == STATE_PLAYING) {
					start();
				}
			}
		}
	};

	private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
		public void onCompletion(MediaPlayer mp) {
			mCurrentState = STATE_PLAYBACK_COMPLETED;
			mTargetState = STATE_PLAYBACK_COMPLETED;
			if (mMediaController != null) {
				mMediaController.hide();
			}
			if (mOnCompletionListener != null) {
				mOnCompletionListener.onCompletion(mMediaPlayer);
			}
		}
	};

	private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
		public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
			Log.d(TAG, "Error: " + framework_err + "," + impl_err);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			if (mMediaController != null) {
				mMediaController.hide();
			}

			/* If an error handler has been supplied, use it and finish. */
			if (mOnErrorListener != null) {
				if (mOnErrorListener.onError(mMediaPlayer, framework_err,
						impl_err)) {
					return true;
				}
			}

			/*
			 * Otherwise, pop up an error dialog so the user knows that
			 * something bad has happened. Only try and pop up the dialog if
			 * we're attached to a window. When we're going away and no longer
			 * have a window, don't bother showing the user an error.
			 */
			if (getWindowToken() != null) {
				// Modify by yangguangfu				
//											  Resources r =  mContext.getResources(); 
//											  int  messageId;
//											  
//											  if (framework_err ==MediaPlayer.
//											  MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK
//											  ) { messageId =
//											  com.android.internal.R.string.
//											  VideoView_error_text_invalid_progressive_playback
//											  ; } else { messageId =
//											  com.android.internal.R.string.
//											  VideoView_error_text_unknown; }
//											  
//											  new AlertDialog.Builder(mContext)
//											  .setTitle(com.android.internal.R.
//											  string.VideoView_error_title)
//											  .setMessage(messageId)
//											  .setPositiveButton
//											  (com.android.internal
//											  .R.string.VideoView_error_button,
//											  new
//											  DialogInterface.OnClickListener()
//											  { public void
//											  onClick(DialogInterface dialog,
//											  int whichButton) { If we get
//											  here, there is no onError
//											  listener, so at least inform them
//											  that the video is over.
//											  
//											  if (mOnCompletionListener !=
//											  null) {
//											  mOnCompletionListener.onCompletion
//											  (mMediaPlayer); } } })
//											  .setCancelable(false) .show();
											
			}
			return true;
		}
	};

	private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			mCurrentBufferPercentage = percent;
//			Log.i("yangguangfu", "videoView"+String.valueOf(percent));
			if(mOnBufferingUpdateListener!=null){
				mOnBufferingUpdateListener.onBufferingUpdate(mMediaPlayer, percent);
			}
			
		}
	};

	/**
	 * Register a callback to be invoked when the media file is loaded and ready
	 * to go.
	 * 
	 * @param l
	 *            The callback that will be run
	 */
	public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) {
		mOnPreparedListener = l;
	}
	
	 public void setOnInfoListener(OnInfoListener listener)
	    {
	        mOnInfoListener = listener;
	    }

	  

	/**
	 * Register a callback to be invoked when the end of a media file has been
	 * reached during playback.
	 * 
	 * @param l
	 *            The callback that will be run
	 */
	public void setOnBufferingUpdateListener(OnBufferingUpdateListener l) {
		mOnBufferingUpdateListener = l;
	}

	public void setOnCompletionListener(OnCompletionListener l) {
		mOnCompletionListener = l;
	}
	/**
	 * Register a callback to be invoked when an error occurs during playback or
	 * setup. If no listener is specified, or if the listener returned false,
	 * VideoView will inform the user of any errors.
	 * 
	 * @param l
	 *            The callback that will be run
	 */
	public void setOnErrorListener(OnErrorListener l) {
		mOnErrorListener = l;
	}

	SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			mSurfaceWidth = w;
			mSurfaceHeight = h;
			boolean isValidState = (mTargetState == STATE_PLAYING);
			boolean hasValidSize = (mVideoWidth == w && mVideoHeight == h);
			if (mMediaPlayer != null && isValidState && hasValidSize) {
				if (mSeekWhenPrepared != 0) {
					seekTo(mSeekWhenPrepared);
				}
				start();
				if (mMediaController != null) {
					mMediaController.show();
				}
			}
		}

		public void surfaceCreated(SurfaceHolder holder) {
			mSurfaceHolder = holder;
			openVideo();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// after we return from this we can't use the surface any more
			mSurfaceHolder = null;
			if (mMediaController != null)
				mMediaController.hide();
			release(true);
		}
	};

	/**
	 * release the media player in any state
	 * ����MediaPlayer�Ǵ������ڲ��ţ�������ͣ״̬��ֻҪ��MediaPlayer���ڣ��Ҿ�ҪMediaPlayer���Ž����ڿ���״̬��
	 */
	private void release(boolean cleartargetstate) {
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
			mCurrentState = STATE_IDLE;
			if (cleartargetstate) {
				mTargetState = STATE_IDLE;
			}
		}
	}
	/**
	 * ���ڴ��?��ʱ��
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isInPlaybackState() && mMediaController != null) {
			//toggleMediaControlsVisiblity();
		}
		return false;
	}
/**
 * ����һ���������¼��� Ĭ������£������κζ����� ..
 */
	@Override
	public boolean onTrackballEvent(MotionEvent ev) {
		if (isInPlaybackState() && mMediaController != null) {
			toggleMediaControlsVisiblity();
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean isKeyCodeSupported = keyCode != KeyEvent.KEYCODE_BACK
				&& keyCode != KeyEvent.KEYCODE_VOLUME_UP
				&& keyCode != KeyEvent.KEYCODE_VOLUME_DOWN
				&& keyCode != KeyEvent.KEYCODE_MENU
				&& keyCode != KeyEvent.KEYCODE_CALL
				&& keyCode != KeyEvent.KEYCODE_ENDCALL;
		if (isInPlaybackState() && isKeyCodeSupported
				&& mMediaController != null) {
			if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
					|| keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
				if (mMediaPlayer.isPlaying()) {
					pause();
					mMediaController.show();
				} else {
					start();
					mMediaController.hide();
				}
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
					&& mMediaPlayer.isPlaying()) {
				pause();
				mMediaController.show();
			} else {
				toggleMediaControlsVisiblity();
			}
		}

		return super.onKeyDown(keyCode, event);
	}
	/**
	 * ��ʾ�����ؿ������
	 */
	private void toggleMediaControlsVisiblity() {
		mMediaController.hide();
//		if (mMediaController.isShowing()) {
//			mMediaController.hide();
//		} else {
//			mMediaController.show();
//		}
	}
	/**
	 * ��ʼ����
	 */
	public void start() {
		if (isInPlaybackState()) {
			mMediaPlayer.start();
			mCurrentState = STATE_PLAYING;
		}
		mTargetState = STATE_PLAYING;
	}
/**
 * ��ͣ����
 */
	public void pause() {
		if (isInPlaybackState()) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
				mCurrentState = STATE_PAUSED;
			}
		}
		mTargetState = STATE_PAUSED;
	}

	/**
	 * �õ���Ƶ�Ŀ�
	 */
	public int getVideoWidth() {
		return mVideoWidth;
	}

	/**
	 * 
	 * @return����Ƶ�߶�
	 */
	public int getVideoHeight() {
		return mVideoHeight;
	}

	// cache duration as mDuration for faster access
	/**
	 * �õ����֡���Ƶ�ļ�����ʱʱ����ܳ���
	 */
	public int getDuration() {
		if (isInPlaybackState()) {
			if (mDuration > 0) {
				return mDuration;
			}
			mDuration = mMediaPlayer.getDuration();
			return mDuration;
		}
		mDuration = -1;
		return mDuration;
	}
/**
 * �õ����֡���Ƶ�ļ����ŵ��ĵ�ǰʱ��λ��
 */
	public int getCurrentPosition() {
		if (isInPlaybackState()) {
			return mMediaPlayer.getCurrentPosition();
		}
		return 0;
	}
/**
 * ��������ָ����λ��
 */
	public void seekTo(int msec) {
		if (isInPlaybackState()) {
			mMediaPlayer.seekTo(msec);
			mSeekWhenPrepared = 0;
		} else {
			mSeekWhenPrepared = msec;
		}
	}
/**
 * MediaPlayer�Ƿ��Ǵ��ڲ���״̬
 */
	public boolean isPlaying() {
		return isInPlaybackState() && mMediaPlayer.isPlaying();
	}
	/**
	 *  �õ���ǰ������Ƶ�İٷֱ�
	 */
	public int getBufferPercentage() {
		if (mMediaPlayer != null) {
			return mCurrentBufferPercentage;
		}
		return 0;
	}

	/**
	 * �Ƿ���Բ������һ�пɺ�
	 * @return
	 */
	private boolean isInPlaybackState() {
		return (mMediaPlayer != null && mCurrentState != STATE_ERROR
				&& mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
	}
	/**
	 * �Ƿ�MediaPlayer�ܷ���ͣ
	 */
	public boolean canPause() {
		return mCanPause;
	}
	/**
	 * �Ƿ�MediaPlayer�ܷ����
	 */
	public boolean canSeekBackward() {
		return mCanSeekBack;
	}
	/**
	 * �Ƿ�MediaPlayer�ܷ���
	 */
	public boolean canSeekForward() {
		return mCanSeekForward;
	}
	@Override
	public int getAudioSessionId() {
		return 0;
	}
}
