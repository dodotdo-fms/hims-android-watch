package com.example.mycom.hims.common.utill;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Omjoon on 2015. 12. 15..
 */
public class FileHelper {
    public static byte[] getBytesFromStream(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        buf = new byte[size];
        while((len = is.read(buf, 0, size)) != -1) {
            bos.write(buf, 0, len);
        }
        buf = bos.toByteArray();

        return buf;
    }


    public static void saveBytesToFile(byte[] bytes, String path) {
        try {
            FileOutputStream fileOuputStream = new FileOutputStream(path);
            fileOuputStream.write(bytes);
            fileOuputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
