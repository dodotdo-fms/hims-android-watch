package com.example.mycom.hims.voice_messaging;

import android.app.Activity;
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


    public static boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_page);

        // YJ ADD
        Intent intent = getIntent();
        userName = intent.getExtras().getString("username");
        fileName = intent.getExtras().getString("timestamp");
        channelId = intent.getExtras().getInt("channel_id");


        TextView userNameTextView = (TextView) findViewById(R.id.user_name);
        userNameTextView.setText(userName);

        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MMM d, h:mm a");

        TextView receivedTimeTextView = (TextView) findViewById(R.id.received_time);
        receivedTimeTextView.setText(format.format(now));
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
                startActivity(intent);
                break;
            case R.id.rcvd_msg:
                if (this.isPlaying == false) {
                    Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "loup", fileName));
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            UIPageActivity.isPlaying = false;
                        }
                    });
                    mediaPlayer.start();
                    this.isPlaying = true;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        TimerDisplayThread.timeDisplayView = null;
        TimerDisplayThread.dateDisplayView = null;
        VMReceiverThread.shouldDisplay = false;
    }

	@Override
	public void onBackPressed() {
		finish();
	}
}
