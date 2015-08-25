package com.bosi.chineseclass.han.fragments;

import java.util.HashMap;
import java.util.List;

import android.media.AudioManager;
import android.media.SoundPool;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bosi.chineseclass.BaseFragment;
import com.bosi.chineseclass.R;
import com.bosi.chineseclass.han.components.HeadLayoutComponents;
import com.bosi.chineseclass.han.db.GameDbOperation;
import com.bosi.chineseclass.han.db.GameIconInfo;
import com.bosi.chineseclass.han.modle.ImageAdapter;
import com.bosi.chineseclass.han.util.PreferencesUtils;
import com.bosi.chineseclass.han.util.Utils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class GameFragment extends BaseFragment {
	@ViewInject(R.id.headactionbar)
	View mHeadActionBar;
	HeadLayoutComponents mHeadActionBarComp;

	@ViewInject(R.id.title_game)
	private TextView mTitleTV;

	@ViewInject(R.id.game_grid)
	private GridView mGameGrid;
	private ImageAdapter mGridAdapter;
	private View mIcon_focuse;

	@ViewInject(R.id.game_step_layout)
	private LinearLayout mStepLayout;

	@ViewInject(R.id.game_success_layout)
	private LinearLayout mSuccessLayout;
	@ViewInject(R.id.game_success_bt_next_step)
	private TextView mNextStepTv;
	@ViewInject(R.id.game_success_bt_restart)
	private TextView mRestartTv;

	private final int MAXSTEP = 9;

	private int mCurrentStep;
	private boolean isFirstClick = true;
	private int mFirstClickItemIndex = -1;
	private int matchCount = 0;

	private String COMPLETE_STEP = "max_step";
	private String GAME_ICON_NULL = "game_icon_null";
	private int mCompleteStep;

	private SoundPool mSoundPool;
	private HashMap<Integer, Integer> mSoundPoolMap;
	private final int GOODSOUND = 1;
	private final int BADSOUD = 2;

	private List<GameIconInfo> mIconList = null;

	@Override
	protected View getBasedView() {
		return inflater.inflate(R.layout.fragment_layout_game, null);
	}

	@Override
	protected void afterViewInject() {
		initHeadActionBarComp();

		mCompleteStep = PreferencesUtils.getInt(mActivity, COMPLETE_STEP, 0);
		mIconList = getGameIconList();

		initStepLayout();
		initGameGrid();
		initSuccessLayout();

		initSoundPool();
	}

	private void initSoundPool() {
		mSoundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
		mSoundPoolMap = new HashMap<Integer, Integer>();
		mSoundPoolMap.put(GOODSOUND, mSoundPool.load(mActivity, R.raw.good, 1));
		mSoundPoolMap.put(BADSOUD, mSoundPool.load(mActivity, R.raw.bad, 2));

	}

	private List<GameIconInfo> getGameIconList() {
		GameDbOperation gameDbOperation = new GameDbOperation();
		// TODO:sql需要加入mCurrentStep
		String sql = "select * from game where step = " + mCurrentStep;
		// String sql = "select * from game";
		List<GameIconInfo> iconList = gameDbOperation.selectDataFromDb(sql);
		return iconList;
	}

	private void initStepLayout() {
		TextView stepBt = null;
		for (int i = 0; i < MAXSTEP; i++) {
			final int step = i;
			stepBt = new TextView(getActivity());
			stepBt.setBackgroundResource(R.drawable.game_step_bt);
			stepBt.setPadding(20, 14, 20, 14);
			stepBt.setText(String.valueOf(i + 1));

			stepBt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (mCurrentStep != step && step <= mCompleteStep) {
						mCurrentStep = step;
						refreshGameView();
					} else if (step > mCompleteStep) {
						Toast.makeText(mActivity, "请先完成前面的关卡",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			mStepLayout.addView(stepBt);
		}

		initButtonTextColor();
	}

	private void initButtonTextColor() {
		for (int i = 0; i < MAXSTEP; i++) {
			initButtonColor(i);
		}
	}

	private void initButtonColor(int btIndex) {
		// TODO:颜色值
		TextView stepBt = (TextView) mStepLayout.getChildAt(btIndex);
		if (btIndex == mCurrentStep) {
			stepBt.setTextColor(getResources().getColor(
					android.R.color.holo_red_light));
		} else if (btIndex > mCompleteStep) {
			stepBt.setTextColor(getResources().getColor(
					android.R.color.darker_gray));
		} else {
			stepBt.setTextColor(getResources().getColor(android.R.color.black));
		}
	}

	private void initGameGrid() {
		mGridAdapter = new ImageAdapter(mActivity, mIconList);
		mGameGrid.setAdapter(mGridAdapter);
		mGameGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int currenItemIndex, long arg3) {

				String path = mIconList.get(currenItemIndex).getIconPath();
				if (path.equals(GAME_ICON_NULL)) {
					return;
				}

				if (isFirstClick) {
					ImageAdapter.GridHolder holder = (ImageAdapter.GridHolder) view
							.getTag();
					mIcon_focuse = holder.getIcon_above();
					mIcon_focuse.setVisibility(View.VISIBLE);
					mFirstClickItemIndex = currenItemIndex;
					isFirstClick = false;
				} else {
					if (mFirstClickItemIndex != -1) {
						mIcon_focuse.setVisibility(View.GONE);
					}
					if (isIconMatch(mFirstClickItemIndex, currenItemIndex)) {// 判断如果两个图片匹配的话，消失
						mIconList.get(mFirstClickItemIndex).setIconPath(
								GAME_ICON_NULL);
						mIconList.get(currenItemIndex).setIconPath(
								GAME_ICON_NULL);
						mGridAdapter.notifyDataSetChanged();
						matchCount += 2;
						playGoodMusic();
					} else {
						playBadMusic();
					}
					if (matchCount == mIconList.size()) {
						setSuccessLayoutVisible();
						Toast.makeText(mActivity, "完成本关", Toast.LENGTH_SHORT)
								.show();
					}
					isFirstClick = true;
				}
			}
		});
	}

	private void playGoodMusic() {
		mSoundPool.play(mSoundPoolMap.get(GOODSOUND), 1, 1, 0, 0, 1);
	}

	private void playBadMusic() {
		mSoundPool.play(mSoundPoolMap.get(BADSOUD), 1, 1, 0, 0, 1);
	}

	private void initSuccessLayout() {
		mNextStepTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mCurrentStep++;
				refreshGameView();
			}
		});
		mRestartTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				refreshGameView();
			}
		});
	}

	private void refreshGameView() {
		mSuccessLayout.setVisibility(View.GONE);
		mIconList.clear();
		mIconList.addAll(getGameIconList());
		mGridAdapter.notifyDataSetChanged();
		matchCount = 0;
		initButtonTextColor();
	}

	private boolean isIconMatch(int mFirstClickItemIndex, int currenIndex) {
		if (mFirstClickItemIndex != currenIndex) {
			int firstItemType = mIconList.get(mFirstClickItemIndex).getType();
			int currenItemType = mIconList.get(currenIndex).getType();
			if (firstItemType == currenItemType) {
				return true;
			}
		}
		return false;
	}

	private void setSuccessLayoutVisible() {
		if (mCurrentStep >= mCompleteStep) {
			mCompleteStep = mCurrentStep + 1;
			PreferencesUtils.putInt(mActivity, COMPLETE_STEP, mCompleteStep);
		}
		if (mCompleteStep == MAXSTEP) {
			// 完成第九关后，不显示下一关按钮
			mNextStepTv.setVisibility(View.GONE);
		} else {
			mNextStepTv.setVisibility(View.VISIBLE);
		}
		mSuccessLayout.setVisibility(View.VISIBLE);
	}

	private void initHeadActionBarComp() {
		mHeadActionBarComp = new HeadLayoutComponents(mActivity, mHeadActionBar);

		String mTitle = mActivity.getIntent().getStringExtra("title");
		if (!TextUtils.isEmpty(mTitle)) {
			mHeadActionBarComp.setTextMiddle(mTitle, -1);
		} else {
			mHeadActionBarComp.setTextMiddle("趣味游戏", -1);
		}

		mHeadActionBarComp.setDefaultLeftCallBack(true);
		mHeadActionBarComp.setDefaultRightCallBack(true);
	}
}
