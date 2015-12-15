package com.example.mycom.hims.voice_messaging;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycom.hims.common.CommonActivity;
import com.example.mycom.hims.R;
import com.example.mycom.hims.model.VoiceMessage;
import com.example.mycom.hims.view.TimerView;
import com.example.mycom.hims.data.Channels;
import com.example.mycom.hims.model.Channel;
import com.example.mycom.hims.server_interface.ServerQuery;
import com.google.gson.Gson;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class MessageSendActivity extends CommonActivity{
    private static Gson gson = new Gson();

    TimerView mTimerView;
    TextView mTv_Name;
    ImageView mIv_message;
    Queue<VoiceMessage> messageQueue;
    public static boolean isOnSendActivity = false;
    public static String chanelId  = null;
    Button mBtn_play;
    boolean isSending = false;
    boolean isSending2 = false;
    // YJ ADD
    private MediaPlayer mediaPlayer = null;
    private RecordManager recordManager = new RecordManager();
    Channel mChannel;
    String filename;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(messageQueue.size()==0){
                try {
                    messagePlayThread.wait();
                }catch (InterruptedException e){

                }
            }else{
                messagePlayThread.run();
            }
        }
    };

    Thread messagePlayThread = new Thread(new Runnable() {
        @Override
        public void run() {
            VoiceMessage voiceMessage = messageQueue.peek();
            play(voiceMessage.getFilepath());
        }
    });
    public void play(String filename) {
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "loup", filename));
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                handler.sendMessage(handler.obtainMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_walkie);
        super.onCreate(savedInstanceState);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.list:
            	Intent intent_walkie = new Intent(MessageSendActivity.this, ChannelListActivity.class);
                startActivity(intent_walkie);
            	finish();
                break;
            default:
                break;

        }
    }


	@Override
	public void onBackPressed() {
		Intent intent_walkie = new Intent(MessageSendActivity.this, ChannelListActivity.class);
        startActivity(intent_walkie);
    	finish();
	}

    private void receiveMessage(Bundle data){

        messageQueue.add(new VoiceMessage());
    }

    @Override
    public void onMappingXml() {
        super.onMappingXml();
        mBtn_play = (Button)findViewById(R.id.button);

        mTimerView = (TimerView)findViewById(R.id.timer);

        mTv_Name = (TextView)findViewById(R.id.tv_name);
        mTimerView.setListener(new TimerView.TimerStopListener() {
            @Override
            public void onStop() {
                mTimerView.setText("00:00 ");
            }
        });


    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

    }

    @Override
    public void setListener() {
        super.setListener();

        mBtn_play.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!isSending) {
                        startRecord();
                    }
//
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (isSending && !isSending2) {
                        stopRecord();
                    }
                }
                return false;

            }
        });

    }

    private void startRecord(){
        isSending = true;
        mTimerView.setDate(null);
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        filename = sb.toString();
        recordManager.start(filename);
    }

    private void stopRecord(){
        isSending2 = true;
        mTimerView.setTimerStop(true);
        mBtn_play.setEnabled(false);

                showLoadingDialog();
                recordManager.stop();
                File file = new File(recordManager.filePath + filename);
                ServerQuery.postMsg(mChannel.getId(), file, new Callback() {
                    @Override
                    public void onResponse(Response response, Retrofit retrofit) {
                        Log.e("ddd", "aaa");
                        hideLoadingDialog();
                        isSending2 = false;
                        isSending = false;
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                        Log.e("ddd12", t.toString());

                        hideLoadingDialog();
                        isSending2 = false;
                        isSending = false;
                    }
                });

    }

    @Override
    public void init() {
        super.init();
        messageQueue = new LinkedList<>();

        mChannel = Channels.getInstance().getChannel(getIntent().getStringExtra("position"));
        chanelId = mChannel.getId();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isOnSendActivity = true;
//        showLoadingDialog();
//        ServerQuery.postChannelEnter(mChannel.getId(),new ServerCallback(){
//            @Override
//            public void onResponse(Response response, Retrofit retrofit, int statuscode) {
//                super.onResponse(response, retrofit, statuscode);
//                hideLoadingDialog();
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                super.onFailure(t);
//                Toast.makeText(getApplicationContext(),"please enter try again",Toast.LENGTH_SHORT).show();
//                hideLoadingDialog();
//            }
//
//            @Override
//            public void onFailure(int statuscode) {
//                super.onFailure(statuscode);
//                Toast.makeText(getApplicationContext(),"please enter try again",Toast.LENGTH_SHORT).show();
//                hideLoadingDialog();
//            }
//        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnSendActivity = false;
        chanelId = null;
//        showLoadingDialog();
//        ServerQuery.postChannelExit(mChannel.getId(), new ServerCallback() {
//            @Override
//            public void onResponse(Response response, Retrofit retrofit, int statuscode) {
//                super.onResponse(response, retrofit, statuscode);
//                hideLoadingDialog();
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                super.onFailure(t);
//                Toast.makeText(getApplicationContext(), "please enter try again", Toast.LENGTH_SHORT).show();
//                hideLoadingDialog();
//            }
//
//            @Override
//            public void onFailure(int statuscode) {
//                super.onFailure(statuscode);
//                Toast.makeText(getApplicationContext(), "please enter try again", Toast.LENGTH_SHORT).show();
//                hideLoadingDialog();
//            }
//        });

    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        receiveMessage(intent.getBundleExtra("data"));
    }


}
