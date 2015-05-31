
package com.bosi.chineseclass.su.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;

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
            }, null, null, null);
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
}
