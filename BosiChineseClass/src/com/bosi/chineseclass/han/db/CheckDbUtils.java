package com.bosi.chineseclass.han.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import com.bosi.chineseclass.BSApplication;
import com.bosi.chineseclass.su.utils.FileUtils;

public class CheckDbUtils {

	private final static String DB_NAME = "dict.db";
	private final static String DB_PATH = "/data/data/"
			+ BSApplication.getInstance().getPackageName() + "/" + "databases";

	private static boolean copyToDb() {
		File file = new File(DB_PATH + "/" + DB_NAME);
		InputStream in = null;
		FileOutputStream fos = null;
		try {
			in = BSApplication.getInstance().getAssets().open(DB_NAME);
			fos = new FileOutputStream(file, false);
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = (in.read(buffer, 0, 1024))) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.flush();
			return true;
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
		return false;
	}

	public static boolean checkDb() {
		try {
			Log.i("print", "onCreate");
			if (!FileUtils.isExist(DB_PATH + "/" + DB_NAME)) {
				FileUtils.mkdir(DB_PATH, DB_NAME);
				return copyToDb();
			} else {
				// 判定大小
				long db = BSApplication.getInstance().getDatabasePath(DB_NAME)
						.length();
				long assets = 0;
				InputStream in = BSApplication.getInstance().getAssets()
						.open(DB_NAME);
				assets = in.available();
				Log.i("print", db + "--------db------------");
				Log.i("print", assets + "-------assets-------------");
				boolean flag = db > assets;
				if (!flag) {
					Log.i("print", "delete");
					File file = new File(DB_PATH + "/" + DB_NAME);
					file.delete();
					FileUtils.mkdir(DB_PATH, DB_NAME);
					return copyToDb();
				}
				return true;
			}
		} catch (IOException e) {
		}
		return false;
	}

	//把爆破成语的加密文件copy到根目录下 ，临时目录里存放加密文件 根目录存
}
