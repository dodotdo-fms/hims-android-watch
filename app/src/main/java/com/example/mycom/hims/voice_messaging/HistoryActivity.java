package com.example.mycom.hims.voice_messaging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mycom.hims.Common.App;
import com.example.mycom.hims.Common.CommonActivity;
import com.example.mycom.hims.Common.Utill.DateToStringAPI;
import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.View.FooterViewHolder;
import com.example.mycom.hims.View.MyRecyclerView;
import com.example.mycom.hims.View.TimerView;
import com.example.mycom.hims.View.ViewHolderFactory;
import com.example.mycom.hims.data.Channels;
import com.example.mycom.hims.data.Messages;
import com.example.mycom.hims.data.Users;
import com.example.mycom.hims.model.History;
import com.example.mycom.hims.model.User;
import com.example.mycom.hims.model.VoiceMessage;
import com.example.mycom.hims.model.api_response.GetMessageResponse;
import com.example.mycom.hims.server_interface.VMServerAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class HistoryActivity extends CommonActivity implements OnAsyncTaskCompleted{

    MyRecyclerView recyclerView;
    HistoryRecyclerViewAdpater adapter;
    MediaPlayer mediaPlayer = null;
    boolean scrollCheck = false;
    boolean loadingMoreContent =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_history_page);
        super.onCreate(savedInstanceState);

    }

    public void go_back(View v){
        switch(v.getId()){
            case R.id.back:
                Intent intent = new Intent(HistoryActivity.this, ChannelListActivity.class);
                startActivity(intent);
            	finish();
                break;
            default:
                break;

        }
    }

    @Override
	public void onBackPressed() {
    	Intent intent = new Intent(HistoryActivity.this, ChannelListActivity.class);
        startActivity(intent);
    	finish();
	}

    public void play(Context context, String filename) {
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "loup", filename));
        mediaPlayer = MediaPlayer.create(context, uri);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
        mediaPlayer.start();
    }

    class HistoryRecyclerViewAdpater extends MyRecyclerView.Adapter<MyRecyclerView.ViewHolder> {
        private static final int VIEWTPYE_FOOTER = 0;
        private boolean footerVisible;
        public final Context mContext;
        public static final int VIEWTPYE_HISTORY = 1;
        public HistoryRecyclerViewAdpater(Context context) {
            mContext = context;
        }

        public MyRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEWTPYE_HISTORY:
                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.history_list, parent, false);
                    return new HistoryViewHolder(view);
                case VIEWTPYE_FOOTER:
                    return new FooterViewHolder(parent);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final MyRecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ViewHolderFactory.Updateable) {
                ((ViewHolderFactory.Updateable) holder).update(Messages.getInstance().getLists().get(position));
            }
        }

        @Override
        public int getItemCount() {
            return Messages.getInstance().getLists().size() + (useFooter() ? 1 : 0);
        }

        @Override
        public int getItemViewType(int position) {
           if(getItemCount() == position){
               if(useFooter()){
                   return VIEWTPYE_FOOTER;
               }
               return VIEWTPYE_HISTORY;
           }
            return VIEWTPYE_HISTORY;
        }

        private boolean useFooter() {
            return footerVisible;
        }

        public void setFooterVisible(boolean b) {
            if (footerVisible == b) {
                //no change state
                return;
            }
            footerVisible = b;
            if (footerVisible) {
                //invisible -> visible
                notifyItemInserted(getItemCount() + 1);
            } else {
                //visible -> invisible
                notifyItemRemoved(getItemCount());
            }
        }

    }


    private class HistoryViewHolder extends MyRecyclerView.ViewHolder implements ViewHolderFactory.Updateable<VoiceMessage> {

        TextView name;
        TimerView time;
        ImageView play;
        ImageView dot;
        View view;

        public HistoryViewHolder(final View itemView) {
            super(itemView);
            view = itemView;
            name = (TextView)itemView.findViewById(R.id.tv_name);
            time = (TimerView)itemView.findViewById(R.id.tv_time);
            play = (ImageView)itemView.findViewById(R.id.btn_play);
            dot = (ImageView)itemView.findViewById(R.id.iv_grrenDot);
            play.setTag("play");
            time.setListener(new TimerView.TimerStopListener() {
                @Override
                public void onStop() {
                    play.setImageDrawable(App.context.getResources().getDrawable(R.drawable.play_s_white));
                }
            });
        }

        @Override
        public void update(final VoiceMessage item) {
          name.setText(Users.getInstance().getUser(item.getMemberId()).getName());

          if(((String)play.getTag()).equals("play")){
              view.setBackgroundColor(App.context.getResources().getColor(R.color.listItemBackgroundColor));
              name.setTextColor(App.context.getResources().getColor(R.color.white));
              dot.setVisibility(View.GONE);
              play.setImageDrawable(App.context.getResources().getDrawable(R.drawable.play_s_gray));
              try {
                  time.setTimerStop(true);
                  Calendar calendar = Calendar.getInstance();
                  calendar.setTime(item.getTimestamp());
                  time.setText(DateToStringAPI.getString(item.getTimestamp(),"MM-dd HH:mm"));
              }catch (Exception e){
                  Log.e("date null","ss");
              }
              play.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      play.setTag("stop");
                      adapter.notifyDataSetChanged();
                      play(getApplicationContext(),String.valueOf(item.getTimestamp().getTime()));
                  }
              });

          }else{
              time.setTextColor(App.context.getResources().getColor(R.color.white));
              dot.setVisibility(View.VISIBLE);
              name.setTextColor(App.context.getResources().getColor(R.color.checkinoutColor));
              time.setDate(item.getDurationTime());
              play.setImageDrawable(App.context.getResources().getDrawable(R.drawable.stop_play));
              view.setBackgroundColor(App.context.getResources().getColor(R.color.listviewBackgroundColor));
              view.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      play.setTag("play");
                      adapter.notifyDataSetChanged();
                  }
              });
          }




        }


    }



    @Override
    public void onMappingXml() {
        super.onMappingXml();
        recyclerView = (MyRecyclerView)findViewById(R.id.list);
    }

    @Override
    public void setListener() {
        super.setListener();
    }

    @Override
    public void init() {
        super.init();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        VMServerAPI.getNewMsg(null, 1, this);
        HistoryRecyclerViewAdpater adapter = new HistoryRecyclerViewAdpater(getApplicationContext());
        recyclerView.setOnScrollListener(new MyRecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (firstVisibleItemPosition == 0 && scrollCheck == true) {
                    scrollCheck = false;
                } else if (firstVisibleItemPosition > 0 && scrollCheck == false) {
                    scrollCheck = true;
                }


                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && Messages.getInstance().getLists().size() != 0) {
                    showMore();
                }
            }

        });
    }

    @Override
    public void onAsyncTaskCompleted(InputStream inputStream) {
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
        List<VoiceMessage> lists = new ArrayList<>();
        for(VoiceMessage msg : response.getVoiceMessage()) {
            byte[] decodedBytes = Base64.decode(msg.getMessage(), 0);


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
            msg.setFilepath(file.getPath());
            lists.add(msg);
        }

        Messages.getInstance().setLists(lists);
        Log.e("ss", lists.size()+"");

         adapter = new HistoryRecyclerViewAdpater(getApplicationContext());
        Log.e("ss", "ss2");

        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();
        Log.e("ss", recyclerView.getAdapter().getItemCount() + "");
    }

    public void showMore() {
        if (loadingMoreContent == true) {
            return;
        }
        loadingMoreContent = true;
        adapter.setFooterVisible(loadingMoreContent);
                            loadingMoreContent = false;

    }

}