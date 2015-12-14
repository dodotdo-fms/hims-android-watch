package com.example.mycom.hims.common.utill;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Omjoon on 2015. 12. 2..
 */
public class DateToStringAPI {

    public static String getString(Date date) {
        String result = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            result = sdf.format(date);
        } catch (Exception e) {

        }

        return result;

    }

    public static String getString(Date date,String format){
        String result = null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            result = sdf.format(date);
        }catch (Exception e){

        }

return result;
    }






}
