package com.example.mycom.hims.Common;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.StrictMode;

import com.example.mycom.hims.MainActivity;
import com.example.mycom.hims.RegistrationIntentService;
import com.example.mycom.hims.ScreenService;
import com.example.mycom.hims.data.Channels;
import com.example.mycom.hims.data.Messages;
import com.example.mycom.hims.data.Rooms;
import com.example.mycom.hims.manager.MySharedPreferencesManager;
import com.example.mycom.hims.model.History;

/**
 * Created by Omjoon on 2015. 11. 26..
 */
public class App extends Application {
    public  static Typeface font;
    public  static Context context;
    private Thread.UncaughtExceptionHandler uncaughtHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        context = getApplicationContext();
        setExceptionHandler();
        goGcmRegister();
        goScreenService();
    }

    private void setExceptionHandler(){
        uncaughtHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new AppRestarter());
    }

    private void goGcmRegister(){
        Intent intent3 = new Intent(this, RegistrationIntentService.class);
        startService(intent3);
    }

    private void goScreenService(){
        if(MySharedPreferencesManager.getInstance().getIsScreenLock()) {
            Intent i = new Intent(context, ScreenService.class);
            context.startService(i);
        }
    }

    public static void cleanMemory(){
        Channels.getInstance().clean();
        Rooms.getInstance().clean();
        Messages.getInstance().clean();

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
