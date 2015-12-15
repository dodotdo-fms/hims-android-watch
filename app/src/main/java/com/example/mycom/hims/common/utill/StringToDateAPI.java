package com.example.mycom.hims.common.utill;

import java.util.Date;

/**
 * Created by Omjoon on 2015. 12. 2..
 */
public class StringToDateAPI {

    public static Date getDate(String date,String format){
        Long time;
        String arr[] = date.split(":");

        time = Long.parseLong(String.valueOf(Integer.parseInt(arr[0]) * 1000 * 60 * 60) + (Integer.parseInt(arr[1]) * 1000 * 60) + (Integer.parseInt(arr[2]) * 1000));

        return new Date(time);
    }
}
