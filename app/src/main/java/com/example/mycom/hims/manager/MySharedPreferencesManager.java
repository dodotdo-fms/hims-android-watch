package com.example.mycom.hims.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mycom.hims.Common.App;

/**
 * Created by Omjoon on 2015. 11. 28..
 */
public class MySharedPreferencesManager {

    protected final static String PREFERENCE_ACCOUNT = "PrefName";
    protected final static String PREFERENCE_UPDATE = "Update";
    protected final static String PREFERENCE_UTILL = "Utill";


    static MySharedPreferencesManager instance;
    Context context;
    public static MySharedPreferencesManager getInstance(){
        if(instance == null){
            instance = new MySharedPreferencesManager();
        }
        return instance;
    }

    private MySharedPreferencesManager(){
        context = App.context;
    }

    public void putMyID(String id){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_ACCOUNT,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("my_id", id);
        editor.commit();
    }

    public String getMyPassword(){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_ACCOUNT,context.MODE_PRIVATE);

        return preferences.getString("p_UserPW", null);

    }

    public void setMyPosition(String position){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_ACCOUNT,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("position", position);
        editor.commit();
    }

    public void setisScreenLock(boolean isScreenLock){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_UTILL,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isScreenLock", isScreenLock);
        editor.commit();

    }
    public boolean getIsScreenLock(){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_UTILL,context.MODE_PRIVATE);

        return preferences.getBoolean("isScreenLock", true);

    }



}
