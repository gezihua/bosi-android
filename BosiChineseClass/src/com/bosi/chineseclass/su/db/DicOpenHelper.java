
package com.bosi.chineseclass.su.db;

import android.R.integer;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.su.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DicOpenHelper extends SQLiteOpenHelper {
    private final static int VERSION = 1;
    private final static String DB_NAME = "dict.db";
    private final static String DB_PATH = BSApplication.getInstance().getPackageCodePath() + "/"
            + "databases";
    private final Context mContext;

    public DicOpenHelper(Context context) {
        this(context, DB_NAME, null, VERSION);
    }

    public DicOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!FileUtils.isExist(DB_PATH + "/" + DB_NAME)) {
            FileUtils.mkdir(DB_PATH, DB_NAME);
            copyToDb();
        } else {
            // 判定大小
            int size =0;
            boolean flag =FileUtils.judeSize(DB_PATH + "/" + DB_NAME,size);
            if (!flag) {
                // remove the file and copy
                File file = new File(DB_PATH + "/" + DB_NAME);
                file.delete();
                copyToDb();
            }
        }

    }

    private void copyToDb() {
        CopyTask task = new CopyTask();
        task.execute();
    }

    private class CopyTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object... params) {
            File file = new File(DB_PATH + "/" + DB_NAME);
            InputStream in = null;
            FileOutputStream fos = null;
            try {
                in = mContext.getAssets().open(DB_NAME);
                fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = (in.read(buffer, 0, 1024))) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                in = null;
                fos = null;
            }
            return null;
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
