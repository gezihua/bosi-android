package com.bosi.chineseclass.su.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

import com.bosi.chineseclass.R;

public class DbUtils {
	private static DbUtils sDbUtils;
	private Context mContext;

	private DbUtils(Context context) {
		mContext = context;
	}

	public static DbUtils getInstance(Context ctx) {
		if (sDbUtils == null) {
			sDbUtils = new DbUtils(ctx);
		}
		return sDbUtils;
	}

	public ArrayList<String> getFilterListByPy(String string) {
		Cursor cursor = null;
		try {
			if (!TextUtils.isEmpty(string)) {
				String res = string.toLowerCase();
				SQLiteDatabase database = new DicOpenHelper(mContext)
						.getReadableDatabase();
				cursor = database.query("orderpy", null, "head = ?",
						new String[] { res }, null, null, null);
				ArrayList<String> list = new ArrayList<String>();
				while (cursor.moveToNext()) {
					list.add(cursor.getString(cursor.getColumnIndex("py")));
				}
				return list;
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return null;
	}

	public ArrayList<String> getFilterWordsByPy(String string) {
		Cursor cursor = null;
		try {
			if (!TextUtils.isEmpty(string)) {
				String res = string.toLowerCase();
				SQLiteDatabase database = new DicOpenHelper(mContext)
						.getReadableDatabase();
				cursor = database.query("py", null, "py = ?",
						new String[] { res }, null, null, null);
				ArrayList<String> list = new ArrayList<String>();
				while (cursor.moveToNext()) {
					list.add(cursor.getString(cursor.getColumnIndex("zi")));
				}
				return list;
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return null;
	}

	public ArrayList<Entity> getFilterListByStoke(String begin) {
		if (TextUtils.isEmpty(begin)) {
			return null;
		}
		Cursor curosr = null;
		try {
			ArrayList<Entity> list = new ArrayList<Entity>();
			DicOpenHelper openHelper = new DicOpenHelper(mContext);
			SQLiteDatabase database = openHelper.getReadableDatabase();
			curosr = database.query("zbh", null, "begin = ?",
					new String[] { begin }, null, null, "bihua");
			while (curosr.moveToNext()) {
				Entity temp = new Entity();
				temp.id = curosr.getString(curosr.getColumnIndex("xuhao"));
				temp.stokes = curosr.getString(curosr.getColumnIndex("bihua"));
				temp.word = curosr.getString(curosr.getColumnIndex("zi"));
				list.add(temp);
			}
			return list;
		} catch (Exception e) {
		} finally {
			if (curosr != null && !curosr.isClosed()) {
				curosr.close();
			}
		}
		return null;
	}

	public ArrayList<String> getFilterListByRadical(String valueOf) {
		if (TextUtils.isEmpty(valueOf)) {
			return null;
		}
		
		Cursor cursor = null;
		try {
			DicOpenHelper openHelper = new DicOpenHelper(mContext);
			SQLiteDatabase database = openHelper.getReadableDatabase();
			ArrayList<String> list = new ArrayList<String>();
			if (valueOf.equals("0")) {
				cursor = database.query("bsbh", null, "bihua > 0", null, null,
						null, "bihua");
			} else {
				cursor = database.query("bsbh", null, "bihua=?",
						new String[] { valueOf }, null, null, null);
			}
			while (cursor.moveToNext()) {
				list.add(cursor.getString(cursor.getColumnIndex("bushow")));
			}
			return list;
		} catch (Exception e) {
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return null;
	}

	public List<String> getFilterBu(String string) {
		Cursor cursor = null;
		try {
			if (!TextUtils.isEmpty(string)) {
				ArrayList<String> list = new ArrayList<String>();

				DicOpenHelper openHelper = new DicOpenHelper(mContext);
				SQLiteDatabase database = openHelper.getReadableDatabase();
				cursor = database.query("bsbh", null, "bushow = ?",
						new String[] { string }, null, null, null);
				while (cursor.moveToNext()) {
					list.add(cursor.getString(cursor.getColumnIndex("bu")));
				}
				cursor.close();
				cursor = null;
				return list;
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
		return null;
	}

	/**
	 * 根据部首获取具体有哪些文字
	 * 
	 * */
	public ArrayList<Entity> getFilterRadicalsBy(String bu) {
		Cursor cursor = null;
		try {
			if (!TextUtils.isEmpty(bu)) {
				ArrayList<Entity> list = new ArrayList<Entity>();

				DicOpenHelper openHelper = new DicOpenHelper(mContext);
				SQLiteDatabase database = openHelper.getReadableDatabase();
				cursor = database.query("bushou", null, "bu = ?",
						new String[] { bu }, null, null, null);
				while (cursor.moveToNext()) {
					Entity temp = new Entity();
					temp.word = cursor.getString(cursor.getColumnIndex("zi"));
					temp.stokes = cursor
							.getString(cursor.getColumnIndex("sbh"));
					list.add(temp);
				}
				return list;
			}
		} catch (Exception e) {
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
		return null;
	}

	public Word getExplain(String word, String id) {
		Cursor cursor = null;
		try {
			DicOpenHelper openHelper = new DicOpenHelper(mContext);
			SQLiteDatabase database = openHelper.getReadableDatabase();
			String sql = mContext.getResources().getString(
					R.string.select_fromzidian_basezitouorid);
			String sqlFormat = String.format(sql, word, id);
			cursor = database.rawQuery(sqlFormat, null);
			Word words = new Word();
			if (cursor != null && cursor.moveToFirst()) {
				words.refid = cursor.getString(cursor.getColumnIndex("refid"));
				words.pinyin = cursor
						.getString(cursor.getColumnIndex("pinyin"));
				words.cy = cursor.getString(cursor.getColumnIndex("cy"));
				words.cysy = cursor.getString(cursor.getColumnIndex("cysy"));
				words.yanbian = cursor.getString(cursor
						.getColumnIndex("yanbian"));
				words.shiyi = cursor.getString(cursor.getColumnIndex("shiyi"));
				words.ytzi = cursor.getString(cursor.getColumnIndex("ytzi"));
				words.zitou = cursor.getString(cursor.getColumnIndex("zitou"));
			}

			return words;

		} catch (Exception e) {
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return null;
	}

	public List<String> getPyList(String pinyin) {
		String sql = "select pyj from unipy where ";
		StringBuilder mSb = new StringBuilder(sql);
		Cursor cursor = null;
		try {
			String[] pys = pinyin.split("/");
			if (pys != null && pys.length > 0) {
				for (int i = 0; i < pys.length; i++) {
					mSb.append("pinyin = '" + pys[i] + "' ");
					if (i != pys.length - 1) {
						mSb.append(" or ");
					}
				}
			}
			DicOpenHelper openHelper = DicOpenHelper.getInstance(mContext);
			SQLiteDatabase database = openHelper.getReadableDatabase();
			List<String> pyList = new ArrayList<String>();
			cursor = database.rawQuery(mSb.toString(), null);
			while (cursor.moveToNext()) {
				String tempString = cursor.getString(cursor
						.getColumnIndex("pyj"));
				if (!TextUtils.isEmpty(tempString))
					pyList.add(tempString);
			}
			return pyList;
		} catch (Exception e) {
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}

		return null;
	}
}
