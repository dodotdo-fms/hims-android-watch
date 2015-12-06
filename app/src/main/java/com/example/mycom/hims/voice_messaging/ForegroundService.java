package com.example.mycom.hims.voice_messaging;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Puts the service in a foreground state, where the system considers it to be
 * something the user is actively aware of and thus not a candidate for killing
 * when low on memory.
 */
public class ForegroundService extends Service {

    // Fixed ID for the 'foreground' notification
    private static final int NOTIFICATION_ID = -574543954;

    // Scheduler to exec periodic tasks
    final Timer scheduler = new Timer();

    // Used to keep the app alive
    TimerTask keepAliveTask;

    /**
     * Allow clients to call on to the service.
     */
    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }

    /**
     * Put the service in a foreground state to prevent app from being killed
     * by the OS.
     */
    @Override
    public void onCreate () {
        super.onCreate();
        keepAwake();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sleepWell();
    }

    /**
     * Put the service in a foreground state to prevent app from being killed
     * by the OS.
     */
    public void keepAwake() {
        final Handler handler = new Handler();

        startForeground(NOTIFICATION_ID, makeNotification());

        keepAliveTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Nothing to do here
                        //Log.d("YJ", "" + new Date().getTime());
                    }
                });
            }
        };

        scheduler.schedule(keepAliveTask, 0, 1000);
    }

    /**
     * Stop background mode.
     */
    private void sleepWell() {
        stopForeground(true);
        keepAliveTask.cancel();
    }

    /**
     * Create a notification as the visible part to be able to put the service
     * in a foreground state.
     *
     * @return
     *      A local ongoing notification which pending intent is bound to the
     *      main activity.
     */
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private Notification makeNotification() {
        Context context     = getApplicationContext();
        String pkgName      = context.getPackageName();

        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // PendingIntent를 등록 하고, noti를 클릭시에 어떤 클래스를 호출 할 것인지 등록.
        PendingIntent intent = PendingIntent.getActivity(
                context, 0, context.getPackageManager()
                        .getLaunchIntentForPackage(pkgName), 0);

        String ticker = "Walkie";
        String title = "Walkie";
        String text = "Walkie";

        // status bar 에 등록될 메시지(Tiker, 아이콘, 그리고 noti가 실행될 시간)
        Notification notification =
                new Notification(android.R.drawable.btn_star,
                        ticker, System.currentTimeMillis());

        // List에 표시될 항목
        notification.setLatestEventInfo(context,
                title, text, intent);

        // noti를 클릭 했을 경우 자동으로 noti Icon 제거
//          notification.flags = notification.flags | notification.FLAG_AUTO_CANCEL;

        // 1234 notification 의 고유아이디
        nm.notify(NOTIFICATION_ID, notification);

        return notification;
    }

    /**
     * Retrieves the resource ID of the app icon.
     *
     * @return
     *      The resource ID of the app icon
     */
    private int getIconResId () {
        Context context = getApplicationContext();
        Resources res   = context.getResources();
        String pkgName  = context.getPackageName();

        int resId;
        resId = res.getIdentifier("icon", "drawable", pkgName);

        return resId;
    }
}
