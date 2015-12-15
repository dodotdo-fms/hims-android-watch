package com.example.mycom.hims.common.utill;

import android.util.Base64;
import android.util.Log;

import com.example.mycom.hims.voice_messaging.RecordManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Omjoon on 2015. 12. 15..
 */
public class FileConverter {
    private static String TAG = "FileConverter";
    public static String convert(String message,String timeStamp){
        byte[] decodedBytes = Base64.decode(message, 0);


        File file = new File(RecordManager.filePath + timeStamp);
        if (file.exists() == false) {
            try {
                file.createNewFile();
            } catch (IOException ioe) {
                Log.e(TAG, "cannot create file: " +
                        ioe.getStackTrace());
            }
        }
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file, false);
            os.write(decodedBytes);
            os.close();
        } catch (FileNotFoundException fnfe) {
            Log.e(TAG, "file not found: " +
                    fnfe.getStackTrace());
        } catch (IOException ioe) {
            Log.e(TAG, "IOException: " + ioe.getStackTrace());
        }
        return file.getPath();
    }
}
