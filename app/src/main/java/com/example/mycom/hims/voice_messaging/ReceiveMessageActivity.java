package com.example.mycom.hims.voice_messaging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycom.hims.R;
import com.example.mycom.hims.common.CommonActivity;
import com.example.mycom.hims.common.utill.FileConverter;
import com.example.mycom.hims.model.VoiceMessage;
import com.example.mycom.hims.server_interface.ServerQuery;
import com.example.mycom.hims.view.TimerView;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class ReceiveMessageActivity extends CommonActivity {

    private String filePath;
    private String fileName;
    private MediaPlayer mediaPlayer = null;
    private TextView mTv_name;
    TimerView mTv_date;
    public static boolean isPlaying = false;
    private String receiveTimeStamp;
    ImageView mIv_play;
    Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ui_page);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        data = intent.getBundleExtra("data");

        mTv_name = (TextView) findViewById(R.id.user_name);

        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MMM d, h:mm a");
        receiveTimeStamp = format.format(now);
        mTv_date = (TimerView) findViewById(R.id.received_time);
        mTv_date.setText(receiveTimeStamp);
        mIv_play = (ImageView)findViewById(R.id.rcvd_msg);

        mTv_date.setListener(new TimerView.TimerStopListener() {
            @Override
            public void onStop() {
                stop();
            }
        });
        mTv_name.setText(data.getString("sender_name"));
    }


    private void stop(){
        mTv_date.setTimerStop(true);
        mIv_play.setImageDrawable(getResources().getDrawable(R.drawable.play));
        mTv_date.setText(receiveTimeStamp);
        mediaPlayer.stop();
        isPlaying = false;
    }

    public  void play(final VoiceMessage voiceMessage) {
        Log.e("play", "asdwd");
        Uri uri = Uri.fromFile(new File(voiceMessage.getFilepath()));
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTv_date.setTime(mediaPlayer.getDuration());
                mIv_play.setImageDrawable(getResources().getDrawable(R.drawable.stop_1));
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("end", "asdwd");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                     stop();
                    }
                });
            }
        });
    }

    public void play() {
        Log.e("play", "asdwd");
        Uri uri = Uri.fromFile(new File(filePath));
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTv_date.setTime(mediaPlayer.getDuration());
                mIv_play.setImageDrawable(getResources().getDrawable(R.drawable.stop_1));
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("end", "asdwd");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stop();
                    }
                });
            }
        });
    }

    // YJ ADD
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.ui_cancel:
                finish();
                break;
            case R.id.ui_reply:
                Intent intent = new Intent(ReceiveMessageActivity.this, MessageSendActivity.class);
                intent.putExtra("channel_id", data.getString("channel_id"));
                intent.putExtra("channel_name", "reply");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.rcvd_msg:

                if (this.isPlaying == false) {
                    if(filePath == null || !fileName.equals(data.getString("message"))) {
                        receiveMessage();
                    }else{
                        play();
                    }
                    this.isPlaying = true;
                }else{
                    stop();
                }
                break;
            default:
                break;
        }
    }



    private synchronized void receiveMessage(){
        showLoadingDialog();
        final String path = data.getString("message");
        ServerQuery.getMessage(path, new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                try {
                    fileName = path;
                    Calendar cal = Calendar.getInstance();
                    filePath = FileConverter.convert(((ResponseBody) response.body()).bytes(), cal.getTimeInMillis() + "");
                    VoiceMessage vm = new VoiceMessage();
                    vm.setFilepath(filePath);
                    vm.setSenderName(data.getString("sender_name"));
                    hideLoadingDialog();
                    play(vm);
                } catch (Exception e) {
                    Log.e("exception", e.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoadingDialog();
                Log.e("exception", t.toString());
            }
        });
    }


	@Override
	public void onBackPressed() {
		finish();
	}


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent != null) {
            setIntent(intent);
            mTv_name.setText(intent.getBundleExtra("data").getString("sender_name"));
        }
    }


}
