package com.example.mycom.hims.Common;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

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
}
