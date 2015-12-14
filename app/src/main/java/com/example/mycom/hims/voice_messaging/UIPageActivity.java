package com.example.mycom.hims.voice_messaging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mycom.hims.R;
import com.example.mycom.hims.thread.TimerDisplayThread;
import com.example.mycom.hims.thread.VMReceiverThread;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;


public class UIPageActivity extends Activity {

    private String userName;
    private String fileName;
    private int channelId;
    private MediaPlayer mediaPlayer = null;
    private TextView mTv_name,mTv_date;
    public static boolean isPlaying = false;
    private String receiveTimeStamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_page);

        // YJ ADD
        Intent intent = getIntent();
        userName = intent.getExtras().getString("username");
        fileName = intent.getExtras().getString("timestamp");
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
                Intent intent = new Intent(UIPageActivity.this, MessageSendActivity.class);
                intent.putExtra("channel_id", channelId);
                intent.putExtra("channel_name", "reply");
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.rcvd_msg:
                if (this.isPlaying == false) {
                    play(getApplicationContext(),fileName);
                    this.isPlaying = true;
                }
                break;
            default:
                break;
        }
    }



	@Override
	public void onBackPressed() {
		finish();
	}
}
