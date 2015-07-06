
package com.bosi.chineseclass.su.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.UserDictionary.Words;
import android.text.TextUtils;
import android.util.Log;

import u.aly.cu;

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
        try {
            if (!TextUtils.isEmpty(string)) {
                String res = string.toLowerCase();
                SQLiteDatabase database = new DicOpenHelper(mContext).getReadableDatabase();
                Cursor cursor = database.query("orderpy", null, "head = ?", new String[] {
                        res
                }, null, null, null);
                ArrayList<String> list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(cursor.getColumnIndex("py")));
                }
                return list;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getFilterWordsByPy(String string) {
        try {
            if (!TextUtils.isEmpty(string)) {
                String res = string.toLowerCase();
                SQLiteDatabase database = new DicOpenHelper(mContext).getReadableDatabase();
                Cursor cursor = database.query("py", null, "py = ?", new String[] {
                        res
                }, null, null, null);
                ArrayList<String> list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(cursor.getColumnIndex("zi")));
                }
                cursor.close();
                cursor = null;
                return list;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Entity> getFilterListByStoke(String begin) {
        if (!TextUtils.isEmpty(begin)) {
            ArrayList<Entity> list = new ArrayList<Entity>();
            DicOpenHelper openHelper = new DicOpenHelper(mContext);
            SQLiteDatabase database = openHelper.getReadableDatabase();
            Cursor curosr = database.query("zbh", null, "begin = ?", new String[] {
                    begin
            }, null, null, "bihua");
            while (curosr.moveToNext()) {
                Entity temp = new Entity();
                temp.id = curosr.getString(curosr.getColumnIndex("xuhao"));
                temp.stokes = curosr.getString(curosr.getColumnIndex("bihua"));
                temp.word = curosr.getString(curosr.getColumnIndex("zi"));
                list.add(temp);
            }
            return list;
        }
        return null;
    }

    public ArrayList<String> getFilterListByRadical(String valueOf) {
        if (!TextUtils.isEmpty(valueOf)) {
            ArrayList<String> list = new ArrayList<String>();
            Cursor cursor = null;
            DicOpenHelper openHelper = new DicOpenHelper(mContext);
            SQLiteDatabase database = openHelper.getReadableDatabase();
            if (valueOf.equals("0")) {
                cursor = database.query("bsbh", null, "bihua > 0", null, null, null, "bihua");
            } else {
                cursor = database.query("bsbh", null, "bihua=?", new String[] {
                        valueOf
                }, null, null, null);
            }
            while (cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex("bushow")));
            }
            cursor.close();
            cursor = null;
            return list;
        }
        return null;
    }

    public List<String> getFilterBu(String string) {
        try {
            if (!TextUtils.isEmpty(string)) {
                ArrayList<String> list = new ArrayList<String>();
                Cursor cursor = null;
                DicOpenHelper openHelper = new DicOpenHelper(mContext);
                SQLiteDatabase database = openHelper.getReadableDatabase();
                cursor = database.query("bsbh", null, "bushow = ?", new String[] {
                        string
                }, null, null, null);
                while (cursor.moveToNext()) {
                    String temp = cursor.getString(cursor.getColumnIndex("bushow"));
                    Log.e("print", "temp" + temp);
                    list.add(cursor.getString(cursor.getColumnIndex("bushow")));
                }
                cursor.close();
                cursor = null;
                return list;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Entity> getFilterRadicalsBy(String bu) {
        try {
            if (!TextUtils.isEmpty(bu)) {
                ArrayList<Entity> list = new ArrayList<Entity>();
                Cursor cursor = null;
                DicOpenHelper openHelper = new DicOpenHelper(mContext);
                SQLiteDatabase database = openHelper.getReadableDatabase();
                cursor = database.query("bushou", null, "bu = ?", new String[] {
                        bu
                }, null, null, null);
                while (cursor.moveToNext()) {
                    Entity temp = new Entity();
                    temp.word = cursor.getString(cursor.getColumnIndex("zi"));
                    temp.stokes = cursor.getString(cursor.getColumnIndex("sbh"));
                    list.add(temp);
                }
                cursor.close();
                cursor = null;
                return list;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public Word getExplain(String word, String id) {
    	  Cursor cursor =null;
        try {
            DicOpenHelper openHelper = new DicOpenHelper(mContext);
            SQLiteDatabase database = openHelper.getReadableDatabase();
            String sql = mContext.getResources()
                    .getString(R.string.select_fromzidian_basezitouorid);
            String sqlFormat = String.format(sql, word, id);
            cursor = database.rawQuery(sqlFormat, null);
            Word words = new Word();
            if (cursor != null && cursor.moveToFirst()) {
                words.refid = cursor.getString(cursor.getColumnIndex("refid"));
                words.pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
                words.cy = cursor.getString(cursor.getColumnIndex("cy"));
                words.cysy = cursor.getString(cursor.getColumnIndex("cysy"));
                words.yanbian = cursor.getString(cursor.getColumnIndex("yanbian"));
                words.shiyi = cursor.getString(cursor.getColumnIndex("shiyi"));
                words.ytzi = cursor.getString(cursor.getColumnIndex("ytzi"));
            }
          
            return words;

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	if(cursor!=null&&!cursor.isClosed()){
        		 cursor.close();
        	}
        }
        return null;
    }

    public List<String> getPyList(String pinyin) {
        try {
            String[] pys = pinyin.split("/");
            if (pys != null) {
                DicOpenHelper openHelper = DicOpenHelper.getInstance(mContext);
                SQLiteDatabase database = openHelper.getReadableDatabase();
                List<String> pyList = new ArrayList<String>();
                for (String temp : pys) {
                    Cursor cursor = database.query("unipy", null, "pinyin = ?", new String[] {
                            temp
                    }, null, null, null);
                    while (cursor.moveToNext()) {
                        String tempString = cursor.getString(cursor.getColumnIndex("pyj"));
                        Log.e("print", tempString);
                        pyList.add(tempString);
                    }
                }
                return pyList;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("print", e.getMessage());
        }

        return null;
    }
}
