package com.example.mycom.hims.View;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.example.mycom.hims.Common.Utill.DateToStringAPI;
import com.example.mycom.hims.Common.Utill.StringToDateAPI;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Omjoon on 2015. 11. 26..
 */
public class TimerView extends TextView {
    private final static String TAG = "DigitalClock";

    private String mFormat = "yyyy.M.d E";

    private Runnable mTicker;
    private android.os.Handler mHandler;

    private Date mDate;
    private static boolean mTickerStopped = false;
    public TimerView(Context context) {
        super(context);
    }

    public interface TimerStopListener{
        void onStop();
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    TimerStopListener listener;

    public void setDate(String date){
        mTickerStopped = false;
        mHandler = new android.os.Handler();
        if(date != null) {
            mDate = StringToDateAPI.getDate(date, "HH:mm:ss");
            final Date defaultdate = new Date();
            defaultdate.setTime(0);
            mTicker = new Runnable() {
                public void run() {
                    if (mTickerStopped) {
                        Log.e("re",mTickerStopped+"");
                        listener.onStop();
                        return;
                    }
                    if (defaultdate.getTime() > mDate.getTime()) {
                        listener.onStop();
                        return;
                    }
                    Log.e("re1",mTickerStopped+"");
                    Log.e("go","go");
                    setText(DateToStringAPI.getString(defaultdate, "mm:ss"));
                    invalidate();
                    defaultdate.setTime(defaultdate.getTime() + 1000);
                    mHandler.postDelayed(mTicker, 1000);
                }
            };
            mTicker.run();
        }else{
            final Date defaultdate = new Date();
            defaultdate.setTime(0);
            mTicker = new Runnable() {
                public void run() {
                    if (mTickerStopped) {
                        listener.onStop();
                        return;
                    }
                    setText(DateToStringAPI.getString(defaultdate, "mm:ss"));
                    invalidate();
                    defaultdate.setTime(defaultdate.getTime() + 1000);
                    mHandler.postDelayed(mTicker, 1000);
                }
            };
            mTicker.run();
        }
    }

    public void setTimerStop(boolean bool){
        mTickerStopped = bool;
    }

    public void setListener(TimerStopListener listener){
        this.listener = listener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

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
