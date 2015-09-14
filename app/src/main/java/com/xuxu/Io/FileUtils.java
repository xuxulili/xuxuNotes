package com.xuxu.Io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Administrator on 2015/6/28.
 */
public class FileUtils {
    private String readFromText(String fileName) {
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            //FileInputStream fin = openFileInput(fileName);
            //用这个就不行了，必须用FileInputStream

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
