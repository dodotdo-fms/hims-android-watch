package com.example.mycom.hims.voice_messaging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.example.mycom.hims.R;
import com.example.mycom.hims.common.CommonActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ReceiveMessageActivity extends CommonActivity {

    private String userName;
    private String messageUrl;
    private String filePath;
    private int channelId;
    private MediaPlayer mediaPlayer = null;
    private TextView mTv_name,mTv_date;
    public static boolean isPlaying = false;
    private String receiveTimeStamp;

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
    }


    public void play(Context context, String filename) {
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "loup", filename));
        mediaPlayer = MediaPlayer.create(context, uri);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlaying = false;
                mTv_date.setText(receiveTimeStamp);
            }
        });
        mediaPlayer.start();
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
                if(filePath == null){
                    getMessage();
                    return;

                }
                if (this.isPlaying == false) {
                    play(getApplicationContext(), messageUrl);
                    this.isPlaying = true;
                }
                break;
            default:
                break;
        }
    }

    private void getMessage(){
        showLoadingDialog();

    }



	@Override
	public void onBackPressed() {
		finish();
	}
}
