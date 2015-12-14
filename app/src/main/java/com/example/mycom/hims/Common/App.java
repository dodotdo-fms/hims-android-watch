package com.example.mycom.hims.common;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.util.Log;

import com.example.mycom.hims.MainActivity;
import com.example.mycom.hims.RegistrationIntentService;
import com.example.mycom.hims.ScreenService;
import com.example.mycom.hims.data.Channels;
import com.example.mycom.hims.data.Messages;
import com.example.mycom.hims.data.Rooms;
import com.example.mycom.hims.manager.MySharedPreferencesManager;
import com.example.mycom.hims.server_interface.ServiceGenerator;

/**
 * Created by Omjoon on 2015. 11. 26..
 */
public class App extends Application {
    public static Typeface font;
    public static Context context;
    public static boolean isLogin=false;
    private Thread.UncaughtExceptionHandler uncaughtHandler;
    public static boolean isAliveMemory;
    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        context = getApplicationContext();
//        setExceptionHandler();
        goScreenService();
    }

    private void setExceptionHandler(){
        uncaughtHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new AppRestarter());
    }

    public static void goGcmRegister(){
        Log.e("ass","assd");
        Intent intent3 = new Intent(context, RegistrationIntentService.class);
        context.startService(intent3);
    }

    public static void goScreenService(){
        if(MySharedPreferencesManager.getInstance().getIsScreenLock()) {
            Intent i = new Intent(context, ScreenService.class);
            context.startService(i);
        }
    }

    public static void stopScreenService(){
        if(MySharedPreferencesManager.getInstance().getIsScreenLock()) {
            Intent i = new Intent(context, ScreenService.class);
            i.setAction("stop");
            context.startService(i);
        }
    }

    public static void cleanMemory(){
        Channels.getInstance().clean();
        Rooms.getInstance().clean();
        Messages.getInstance().clean();
        isLogin = false;

    }


    private class AppRestarter implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            PendingIntent restartIntent = PendingIntent.getService(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class), 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, restartIntent);
            System.exit(2);
        }
    }


}
