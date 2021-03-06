
package com.bosi.chineseclass.su.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static void mkdir(String db_path, String db_path2) {
        try {
            File file = new File(db_path + "/" + db_path2);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    public static void mkdir(String path) {
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                mkdir(file.getParentFile().getPath());
            }
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public static boolean isExist(String path) {
        File file = new File(path);
        try {
            if (file.getParentFile().exists()) {
                if (file.exists()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean judeSize(String string, int size) {
        // TODO Auto-generated method stub
        return false;
    }

}
