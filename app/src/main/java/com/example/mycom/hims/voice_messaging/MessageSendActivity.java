package com.example.mycom.hims.voice_messaging;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycom.hims.Common.CommonActivity;
import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.View.TimerView;
import com.example.mycom.hims.data.Channels;
import com.example.mycom.hims.model.Channel;
import com.example.mycom.hims.server_interface.ServerQuery;
import com.example.mycom.hims.server_interface.VMServerAPI;
import com.example.mycom.hims.thread.TimerDisplayThread;
import com.example.mycom.hims.thread.VMReceiverThread;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Random;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class MessageSendActivity extends CommonActivity{
    private static Gson gson = new Gson();

    TimerView mTimerView;
    TextView mTv_Name;
    ImageView mIv_message;
    Button mBtn_play;
    boolean isSending = false;
    boolean isSending2 = false;
    // YJ ADD
    private MediaPlayer mediaPlayer = null;
    private RecordManager recordManager = new RecordManager();
    Channel mChannel;
    String filename;
    public void play(String filename) {
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "loup", filename));
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isUseLoadingDialog = true;
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

    private void goMemberList(){
//        Intent intent = new Intent(getApplicationContext(),ChannelMemberListActivity.class)
//        intent.putExtra("position",cha)
//        startActivity(intent);

    }

    private void goPlay(){



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
    public void setListener() {
        super.setListener();

        mBtn_play.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if(!isSending) {
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
//
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (isSending && !isSending2) {
                        isSending2 = true;
                        mTimerView.setTimerStop(true);
                        mBtn_play.setEnabled(false);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showLoadingDialog();
                                recordManager.stop();
                                //play();

                                File file = new File(recordManager.filePath + filename);
//                                byte[] bytes = new byte[0];
//
//                                RandomAccessFile f = null;
//                                try {
//                                    f = new RandomAccessFile(file, "r");
//                                } catch (FileNotFoundException e) {
//                                    e.printStackTrace();
//                                }
//                                try {
//                                    // Get and check length
//                                    if (f != null) {
//                                        long longlength = f.length();
//                                        int length = (int) longlength;
//                                        if (length != longlength)
//                                            throw new IOException("File size >= 2 GB");
//                                        // Read file and return data
//                                        bytes = new byte[length];
//                                        f.readFully(bytes);
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                } finally {
//                                    try {
//                                        if (f != null) {
//                                            f.close();
//                                        }
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }

                                // SEND FILE

//                                    ServerQuery.postMsg(mChannel.getId(), file, new Callback() {
//                                        @Override
//                                        public void onResponse(Response response, Retrofit retrofit) {
//                                            Log.e("ddd","aaa");
//                                            hideLoadingDialog();
//                                            isSending2 = false;
//                                            isSending = false;
//                                        }
//
//                                        @Override
//                                        public void onFailure(Throwable t) {
//                                            t.printStackTrace();
//                                            Log.e("ddd12", t.toString());
//
//                                            hideLoadingDialog();
//                                            isSending2 = false;
//                                            isSending = false;
//                                        }
//                                    });
//                                    VMServerAPI.sendMsgToChannel(Integer.valueOf(mChannel.getId()),
//                                            recordManager.filePath + filename[0], new OnAsyncTaskCompleted() {
//                                                @Override
//                                                public void onAsyncTaskCompleted(InputStream inputStream) {
//                                                    mBtn_play.setEnabled(true);
//                                                }
//                                            });
                            }
                        }, 500);
                    }
                }
                return false;

            }
        });

    }

    @Override
    public void init() {
        super.init();
        mChannel = Channels.getInstance().getChannel(getIntent().getStringExtra("position"));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
}
