
package com.bosi.chineseclass.su.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.su.utils.FileUtils;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.NewsPaper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        checkDb();
    }

    private void checkDb() {
        try {
            Log.i("print", "onCreate");
            if (!FileUtils.isExist(DB_PATH + "/" + DB_NAME)) {
                FileUtils.mkdir(DB_PATH, DB_NAME);
                copyToDb();
                Log.i("print", "judge----------1");
            } else {
                // 判定大小
                long db = mContext.getDatabasePath(DB_NAME).length();
                long assets = 0;
                InputStream in = mContext.getAssets().open(DB_NAME);
                assets = in.available();
                Log.i("print", db + "--------db------------");
                Log.i("print", assets + "-------assets-------------");
                boolean flag = db > assets;
                if (!flag) {
                    Log.i("print", "delete");
                    File file = new File(DB_PATH + "/" + DB_NAME);
                    file.delete();
                    FileUtils.mkdir(DB_PATH, DB_NAME);
                    copyToDb();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    private void copyToDb() {
        File file = new File(DB_PATH + "/" + DB_NAME);
        InputStream in = null;
        FileOutputStream fos = null;
        try {
            in = mContext.getAssets().open(DB_NAME);
            fos = new FileOutputStream(file, false);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = (in.read(buffer, 0, 1024))) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            in = null;
            fos = null;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
