package com.zy.booking.components;

import java.util.ArrayList;

import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zy.booking.R;
import com.zy.booking.modle.FaceGVAdapter;
import com.zy.booking.modle.FaceVPAdapter;
import com.zy.booking.util.EmojiUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MyFaceContainer {
	private int columns = 7;
	private int rows = 3;

	@ViewInject(R.id.face_viewpager)
	private ViewPager mViewPager;

	@ViewInject(R.id.face_dots_container)
	private LinearLayout mDotsLayout;

	public View mBaseView;

	private Context mContext;

	public MyFaceContainer(View mViewBase, Context mContext) {
		this.mContext = mContext;
		
		if(mViewBase==null) {
			this.mBaseView = LayoutInflater.from(mContext).inflate(R.layout.chat_face_container, null);
		}else
		this.mBaseView = mViewBase;
		ViewUtils.inject(this, mBaseView);
		initStaticFaces();
		initViewPager();

	}

	public void showFaceContainer(int isVisiable) {
		mBaseView.setVisibility(isVisiable);
	}

	private int getPagerCount() {
		int count = staticFacesList.size();
		return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
				: count / (columns * rows - 1) + 1;
	}

	private List<View> views = new ArrayList<View>();
	private List<String> staticFacesList;

	private void initStaticFaces() {
		try {
			staticFacesList = new ArrayList<String>();
			String[] faces = mContext.getAssets().list("face/png");
			for (int i = 0; i < faces.length; i++) {
				staticFacesList.add(faces[i]);
			}
			staticFacesList.remove("emotion_del_normal.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 初始表情 *
	 */
	private void initViewPager() {
		// 获取页数
		for (int i = 0; i < getPagerCount(); i++) {
			views.add(viewPagerItem(i));
			LayoutParams params = new LayoutParams(16, 16);
			mDotsLayout.addView(dotsItem(i), params);
		}
		FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
		mViewPager.setAdapter(mVpAdapter);
		mViewPager.setOnPageChangeListener(new PageChange());
		mDotsLayout.getChildAt(0).setSelected(true);
	}

	private ImageView dotsItem(int position) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dot_image, null);
		ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
		iv.setId(position);
		return iv;
	}

	private View viewPagerItem(int position) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.face_gridview, null);
		GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);

		/**
		 * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
		 * */
		List<String> subList = new ArrayList<String>();
		subList.addAll(staticFacesList
				.subList(position * (columns * rows - 1),
						(columns * rows - 1) * (position + 1) > staticFacesList
								.size() ? staticFacesList.size() : (columns
								* rows - 1)
								* (position + 1)));

		/**
		 * 末尾添加删除图标
		 * */
		subList.add("emotion_del_normal.png");
		FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, mContext);
		gridview.setAdapter(mGvAdapter);
		gridview.setNumColumns(columns);

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> paramAdapterView, View view,
					int paramInt, long paramLong) {

				try {
					String png = ((TextView) ((LinearLayout) view)
							.getChildAt(1)).getText().toString();

					if (mOnCallBack == null)
						return;
					if (png.contains("emotion_del_normal")) {
						mOnCallBack.onDeleteEmojiCallBack();
						return;
					}

					mOnCallBack.onEmojiInsertCallBack(EmojiUtils.getFace(png,
							mContext));

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		return layout;
	}

	/**
	 * 表情页改变时，dots效果也要跟着改变
	 * */
	class PageChange implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
				mDotsLayout.getChildAt(i).setSelected(false);
			}
			mDotsLayout.getChildAt(arg0).setSelected(true);
		}

	}

	OnCallBack mOnCallBack;

	public void setCallBack(OnCallBack mOnCallBack) {
		this.mOnCallBack = mOnCallBack;
	}

	public interface OnCallBack {

		public void onEmojiInsertCallBack(CharSequence mCharsequence);

		public void onDeleteEmojiCallBack();

	}

}