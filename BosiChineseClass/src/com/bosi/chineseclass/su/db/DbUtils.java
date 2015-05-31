
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
                return list;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
