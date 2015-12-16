package com.example.mycom.hims;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class ScreenService extends Service {

    private ScreenReceiver mReceiver = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Notification notification = new Notification(R.drawable.ic_launcher, "Running HIMS", System.currentTimeMillis());
        notification.setLatestEventInfo(getApplicationContext(), "HIMS", "Running HIMS", null);
        startForeground(1, notification);

        super.onStartCommand(intent, flags, startId);

        if(intent != null){
            if(intent.getAction() == null){
                if(mReceiver==null){
                    mReceiver = new ScreenReceiver();
                    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
                    registerReceiver(mReceiver, filter);
                }
            }else{
                if(mReceiver != null) {
                    unregisterReceiver(mReceiver);
                }
            }
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mReceiver != null){
            unregisterReceiver(mReceiver);
        }
    }
}