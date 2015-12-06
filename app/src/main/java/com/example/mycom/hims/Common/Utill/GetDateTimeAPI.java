package com.example.mycom.hims.Common.Utill;

import android.content.Context;

import com.example.mycom.hims.R;

/**
 * Created by Omjoon on 2015. 11. 26..
 */
public class GetDateTimeAPI {

    public static String getToday(Context context,int dayOfweek){
        String today = null;

        switch (dayOfweek){
            case 1 :
                today = context.getString(R.string.sunday);
                break;
            case 2 :
                today = context.getString(R.string.monday);
                break;
            case 3 :
                today = context.getString(R.string.tuesday);
                break;
            case 4 :
                today = context.getString(R.string.wednesday);
                break;
            case 5 :
                today = context.getString(R.string.thursday);
                break;
            case 6 :
                today = context.getString(R.string.friday);
                break;
            case 7  :
                today = context.getString(R.string.saturday);
                break;
        }
        return today;
    }

    public static String getToMonth(Context context,int monthOfYear){
        String toMonth = null;

        switch (monthOfYear){
            case 0 :
                toMonth = context.getString(R.string.january);
                break;
            case 1 :
                toMonth = context.getString(R.string.febuary);
                break;
            case 2 :
                toMonth = context.getString(R.string.march);
                break;
            case 3 :
                toMonth = context.getString(R.string.april);
                break;
            case 4 :
                toMonth = context.getString(R.string.may);
                break;
            case 5 :
                toMonth = context.getString(R.string.june);
                break;
            case 6 :
                toMonth = context.getString(R.string.july);
                break;
            case 7 :
                toMonth = context.getString(R.string.august);
                break;
            case 8 :
                toMonth = context.getString(R.string.september);
                break;
            case 9 :
                toMonth = context.getString(R.string.october);
                break;
            case 10 :
                toMonth = context.getString(R.string.november);
                break;
            case 11 :
                toMonth = context.getString(R.string.december);
                break;
        }

        return toMonth;
    }

}
