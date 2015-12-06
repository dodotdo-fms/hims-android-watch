package com.example.mycom.hims.thread;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by corekey on 10/20/15.
 */
public class TimerDisplayThread implements Runnable {

    public static TextView timeDisplayView;
    public static TextView dateDisplayView;
    Typeface typeface;
    private final int UPDATE_PERIOD = 3000; /* 3 seconds */


    public TimerDisplayThread(Context context) {
        typeface = Typeface.createFromAsset(context.getResources().getAssets(),"Roboto-Regular.ttf");
    }


    @Override
    public void run() {

        while (true) {
            try{
                handler.sendMessage(handler.obtainMessage());
                Thread.sleep(UPDATE_PERIOD);
            }catch(Throwable t){

            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            updateThread();
        }
    };

    private void updateThread(){
        String[] now_time = now_time();
        String mon;
        if (timeDisplayView != null) {
            timeDisplayView.setText(now_time[0]);
            timeDisplayView.setTypeface(typeface);
        }
        mon = getMonth(now_time[1]);
        if (dateDisplayView != null) {
            dateDisplayView.setText(now_time[3] + ", " + mon + " " + now_time[2]);
            dateDisplayView.setTypeface(typeface);
        }
    }

    public String[] now_time(){
        long now = System.currentTimeMillis();
// 현재 시간을 저장 한다.
        Date date = new Date(now);
// 시간 포맷으로 만든다.
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd");
        SimpleDateFormat sdfDay = new SimpleDateFormat("EEEEEEEEE");

        String[] dates = new String[4];
        dates[0] = sdfNow.format(date);
        dates[1] = sdfMonth.format(date);
        dates[2] = sdfDate.format(date);
        dates[3] = sdfDay.format(date);
        return dates;
    }

    public String getMonth(String str){
        String mon;
        switch(Integer.parseInt(str)){
            case 1:
                mon = "Jan";
                break;
            case 2:
                mon = "Feb";
                break;
            case 3:
                mon = "Mar";
                break;
            case 4:
                mon = "Apr";
                break;
            case 5:
                mon = "May";
                break;
            case 6:
                mon = "Jun";
                break;
            case 7:
                mon = "Jul";
                break;
            case 8:
                mon = "Aug";
                break;
            case 9:
                mon = "Sep";
                break;
            case 10:
                mon  = "Oct";
                break;
            case 11:
                mon = "Nov";
                break;
            case 12:
                mon = "Dec";
                break;
            default:
                mon = "";
                break;
        }
        return mon;
    }
}
