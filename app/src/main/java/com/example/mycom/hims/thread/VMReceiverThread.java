package com.example.mycom.hims.thread;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;

import com.example.mycom.hims.model.VoiceMessage;
import com.example.mycom.hims.model.api_response.GetMessageResponse;
import com.example.mycom.hims.server_interface.VMServerAPI;
import com.example.mycom.hims.voice_messaging.RecordManager;
import com.example.mycom.hims.voice_messaging.UIPageActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * Created by corekey on 10/20/15.
 */
public class VMReceiverThread implements Runnable {

    public static Context curContext = null;
    public static boolean shouldDisplay = false;
    public static boolean isStarted = false;
    private final int UPDATE_PERIOD = 5000; /* 5 seconds */


    public VMReceiverThread(Context context) {
        this.curContext = context;
        this.isStarted = true;
    }

    @Override
    public void run() {
        while (true) {
            try {
                handler.sendMessage(handler.obtainMessage());
                Thread.sleep(UPDATE_PERIOD);
            } catch (Throwable t) {
                Log.d(getClass().getSimpleName().toString(), "exception: " + t.getStackTrace());
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
        SharedPreferences msgTimePrefs = curContext.getSharedPreferences("LatestMsgTime", Context.MODE_PRIVATE);
        String latestDate = msgTimePrefs.getString("latestMsgDate", "0");
        InputStream inputStream = VMServerAPI.getNewMsg(latestDate, 1);
        if (inputStream == null) {
            return;
        }

        /* get response */
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Reader reader = new InputStreamReader(inputStream);
        GetMessageResponse response = gson.fromJson(reader, GetMessageResponse.class);

        if (response == null || response.getVoiceMessage() == null || response.getVoiceMessage().size() <= 0) {
            return;
        }

        VoiceMessage msg = response.getVoiceMessage().get(0);
        byte[] decodedBytes = Base64.decode(msg.getMessage(), 0);

        SharedPreferences.Editor msgTimeEditor = msgTimePrefs.edit();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        msgTimeEditor.putString("latestMsgDate", sdf.format(msg.getTimestamp()));
        msgTimeEditor.commit();

        /* save to file */
        File file = new File(RecordManager.filePath + msg.getTimestamp().getTime());
        if (file.exists() == false) {
            try {
                file.createNewFile();
            } catch (IOException ioe) {
                Log.e(getClass().getSimpleName().toString(), "cannot create file: " +
                        ioe.getStackTrace());
            }
        }
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file, false);
            os.write(decodedBytes);
            os.close();
        } catch (FileNotFoundException fnfe) {
            Log.e(getClass().getSimpleName().toString(), "file not found: " +
                    fnfe.getStackTrace());
        } catch (IOException ioe) {
            Log.e(getClass().getSimpleName().toString(), "IOException: " + ioe.getStackTrace());
        }

        /* SAVE HISTORY */
        JSONObject json = new JSONObject();
        SharedPreferences historyPref = curContext.getSharedPreferences("history", Context.MODE_PRIVATE);
        Collection<?> col =  historyPref.getAll().values();
        SharedPreferences.Editor historyEditor = historyPref.edit();
        try {
            json.put("sender", msg.getMemberId());
            json.put("timestamp", msg.getTimestamp().getTime());
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName().toString(), "JSON exception: " + e.getStackTrace());
        }
        historyEditor.putString("history_" + col.size(), json.toString());
        historyEditor.commit();

        /* VIBRATOR */
        Vibrator vibe = (Vibrator) curContext.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(500);

         /* return if display context is not set */
        if (curContext == null || shouldDisplay == false) {
            return;
        }

        /* display the received message */
        Intent intent = new Intent(curContext, UIPageActivity.class);
        intent.putExtra("username", msg.getMemberId());
        intent.putExtra("timestamp", String.valueOf(msg.getTimestamp().getTime()));
        intent.putExtra("channel_id", msg.getChannelId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        curContext.startActivity(intent);
    }
}
