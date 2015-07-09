
package com.bosi.chineseclass.su.db;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.bosi.chineseclass.BSApplication;


public class DicOpenHelper extends SQLiteOpenHelper {
    private final static int VERSION = 1;
    private final static String DB_NAME = "dict.db";
    private final static String DB_PATH = "/data/data/"
            + BSApplication.getInstance().getPackageName() + "/" + "databases";
    private final Context mContext;
    public static DicOpenHelper sDicOpenHelper;
    private final static Object sLOCK = new Object();

    public DicOpenHelper(Context context) {
        this(context, DB_NAME, null, VERSION);
    }

    public static synchronized DicOpenHelper getInstance(Context context) {
        synchronized (sLOCK) {
            if (sDicOpenHelper == null) {
                sDicOpenHelper = new DicOpenHelper(context);
            }
            return sDicOpenHelper;
        }
    }

    public DicOpenHelper(Context context, String name, CursorFactory factory,
            int version) {
        super(context, name, factory, version);
        mContext = context;
    }

  

    @Override
    public void onCreate(SQLiteDatabase db) {
     
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
