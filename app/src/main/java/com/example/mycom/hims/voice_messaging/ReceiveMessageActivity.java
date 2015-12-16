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
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class ReceiveMessageActivity extends CommonActivity {

    private String userName;
    private String messageUrl;
    private String filePath;
    private int channelId;
    private MediaPlayer mediaPlayer = null;
    private TextView mTv_name,mTv_date;
    public static boolean isPlaying = false;
    private String receiveTimeStamp;
    ImageView mIv_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ui_page);
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getBundleExtra("data");
        Intent intent = getIntent();
        userName = data.getString("sender_message");
        messageUrl = intent.getExtras().getString("message");
        channelId = intent.getExtras().getInt("channel_id");


        mTv_name = (TextView) findViewById(R.id.user_name);
        mTv_name.setText(userName);

        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MMM d, h:mm a");
        receiveTimeStamp = format.format(now);
        mTv_date = (TextView) findViewById(R.id.received_time);
        mTv_date.setText(receiveTimeStamp);
        mIv_play = (ImageView)findViewById(R.id.rcvd_msg);

    }



    public  void play(final VoiceMessage voiceMessage) {
        Log.e("play", "asdwd");
        Uri uri = Uri.fromFile(new File(voiceMessage.getFilepath()));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTv_name.setText(voiceMessage.getSenderName());
                mIv_play.setImageDrawable(getResources().getDrawable(R.drawable.stop_1));
            }
        });
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("end", "asdwd");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTv_name.setText("");
                        mIv_play.setImageDrawable(getResources().getDrawable(R.drawable.play));
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
                intent.putExtra("channel_id", channelId);
                intent.putExtra("channel_name", "reply");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.rcvd_msg:

                if (this.isPlaying == false) {
                    receiveMessage(getIntent().getBundleExtra("data"));
                    this.isPlaying = true;
                }else{
                    mediaPlayer.stop();
                    isPlaying = false;
                }
                break;
            default:
                break;
        }
    }



    private synchronized void receiveMessage(final Bundle data){
        showLoadingDialog();
        final String path = data.getString("message");
        Log.e("path", path);
        ServerQuery.getMessage(path, new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                try {
                    Calendar cal = Calendar.getInstance();
                    String paths = FileConverter.convert(((ResponseBody) response.body()).bytes(), cal.getTimeInMillis() + "");
                    VoiceMessage vm = new VoiceMessage();
                    vm.setFilepath(paths);
                    vm.setSenderName(data.getString("sender_name"));
                    hideLoadingDialog();
                    play(vm);
                } catch (Exception e) {
                    Log.e("ex", e.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoadingDialog();
                Log.e("sdwd", t.toString());
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
        if(intent != null)
        setIntent(intent);
    }
}
