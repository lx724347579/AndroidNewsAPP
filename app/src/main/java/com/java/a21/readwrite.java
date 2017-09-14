package com.java.a21;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Kyle on 2017/9/14.
 */

public class readwrite {
    public static void save(String text,String path) {
        try {
            Log.d("write",text);
            File file = new File(path);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(text.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String read(String path) {
        String content = null;
        int len = 0 ;
        int sum = 0;
        try {
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

            StringBuilder stringBuilder = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1) {
                //arrayOutputStream.write(bytes, 0, bytes.length);
               // sum += len;
                stringBuilder.append(new String(bytes, 0, len));
            }
            inputStream.close();
            arrayOutputStream.close();
            //content = new String(arrayOutputStream.toByteArray());
            content = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("read",content);
        return content;

    }

    public static void Saveimg(Bitmap mbitmap, String path) {

        Log.d("save",mbitmap.toString());
        Log.d("save",path);

        try {
            File file = new File(path);
            FileOutputStream out = new FileOutputStream(file);
            mbitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
