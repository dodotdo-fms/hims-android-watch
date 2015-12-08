package com.example.mycom.hims.Common;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.example.mycom.hims.data.Channels;
import com.example.mycom.hims.data.Messages;
import com.example.mycom.hims.data.Rooms;
import com.example.mycom.hims.model.History;

/**
 * Created by Omjoon on 2015. 11. 26..
 */
public class App extends Application {
    public  static Typeface font;
    public  static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        context = getApplicationContext();
    }

    public static void cleanMemory(){
        Channels.getInstance().clean();
        Rooms.getInstance().clean();
        Messages.getInstance().clean();

    }
}
