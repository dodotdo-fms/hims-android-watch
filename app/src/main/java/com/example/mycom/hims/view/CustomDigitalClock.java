package com.example.mycom.hims.view;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Omjoon on 2015. 11. 26..
 */
public class CustomDigitalClock extends TextView {
    private final static String TAG = "DigitalClock";

    private Calendar mCalendar;
    private String mFormat = "yyyy.M.d E";

    private Runnable mTicker;
    private android.os.Handler mHandler;

    private boolean mTickerStopped = false;

    public CustomDigitalClock(Context context) {
        super(context);
        initClock(context);
    }

    public CustomDigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new android.os.Handler();

        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped)
                    return;
                mCalendar.setTimeInMillis(System.currentTimeMillis());
                // setText(mSimpleDateFormat.format(mCalendar.getTime()));
                String time = mCalendar.get(Calendar.HOUR_OF_DAY)+"";
                String ampm;
                if(Integer.valueOf(time)>=12){
                    time = (Integer.valueOf(time)-12)+":";
                    ampm = "pm";
                }else{
                    time+=":";
                    ampm = "am";
                }
                int minute = mCalendar.get(Calendar.MINUTE);
                if(minute<10){
                    time += "0"+minute;
                }else{
                    time += minute;
                }
                time += ampm;
                setText(time);
                invalidate();
                long now = SystemClock.uptimeMillis();
                // long next = now + (1000 - now % 1000);
                long next = now + (1000 - System.currentTimeMillis() % 1000);

                // Debug
//                Log.d(TAG, "" + now);
//                Log.d(TAG, "" + next);
//                Log.d(TAG, "" + mCalendar.getTimeInMillis());

                // TODO
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    public void setFormat(String format) {
        mFormat = format;
    }

}
