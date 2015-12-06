package com.example.mycom.hims.voice_messaging;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycom.hims.LockScreenActivity;
import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.data.Channels;
import com.example.mycom.hims.model.Channel;
import com.example.mycom.hims.model.ChannelList;
import com.example.mycom.hims.model.api_response.GetChannelResponse;
import com.example.mycom.hims.server_interface.VMServerAPI;
import com.example.mycom.hims.thread.TimerDisplayThread;
import com.example.mycom.hims.thread.VMReceiverThread;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.*;


public class ChannelListActivity extends Activity implements OnAsyncTaskCompleted {

    ArrayList my_team_list = new ArrayList();
    ArrayList my_team_id = new ArrayList();

    private static Gson gson = new Gson();

    public static String my_id = "";
    
    String position = "";

    private Typeface font;
    private TextView room_time, list_name;

    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;

    private static ServiceConnection connection;

    private class ViewHolder{
        public TextView channel_name;

    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;

        public ListViewAdapter(Context mContext){
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount(){
            return Channels.getInstance().getChannels().size();
        }
        @Override
        public Object getItem(int position){
            return Channels.getInstance().getChannels().get(position);
        }
        @Override
        public long getItemId(int position){
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");

            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.slist_item, null);

                holder.channel_name = (TextView) convertView.findViewById(R.id.channel_name);
                holder.channel_name.setTypeface(font);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }



            holder.channel_name.setText(((Channel)getItem(position)).getName());

            return convertView;
        }

        public void dataChange(){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAsyncTaskCompleted(InputStream inputStream) {
        mListView = (ListView) findViewById(R.id.list);
        mAdapter = new ListViewAdapter(this);

//        Log.e("response",getStringFromInputStream(inputStream));
        if (inputStream == null) {
            return;
        }
        /* get response */
        Reader reader = new InputStreamReader(inputStream);
        GetChannelResponse response = gson.fromJson(reader, GetChannelResponse.class);

        if (response == null || response.getChannels() == null || response.getChannels().size() == 0) {
            return;
        }

        int index = 0;
        List<Channel> channelList = response.getChannels();
        reverse(channelList);
        Channels.getInstance().setChannels(channelList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Channel mData = (Channel)mAdapter.getItem(position);
                Intent intent = new Intent(ChannelListActivity.this, MessageSendActivity.class);
                intent.putExtra("position", mData.getId());
                startActivity(intent);
                //온클릭!
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);
        font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");


        ArrayAdapter adapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1, my_team_list);
        mListView = (ListView)findViewById(R.id.list);

        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        this.my_id = prefs.getString("my_id", this.my_id.replace(" ", "%20"));
        position = prefs.getString("position", "");

        VMServerAPI.getChannelInfoByJoinedIdAsync(my_id, this);

        /* start ForegroundService */
        startService();

        //ListView를 Context 메뉴로 등록
        registerForContextMenu(mListView);

        /* start VM receiver thread */
        if (VMReceiverThread.isStarted == false) {
            Thread vmReciverThread = new Thread(new VMReceiverThread(getApplicationContext()));
            vmReciverThread.start();
        }
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.back:
                Intent lockScreenIntent = new Intent(ChannelListActivity.this, LockScreenActivity.class);
                lockScreenIntent.putExtra("position", position);
            	startActivity(lockScreenIntent);
                finish();
                break;
//            case R.id.plus:
//                Intent intent = new Intent(ChannelListActivity.this, PeerSelectionActivity.class);
//                startActivity(intent);
//                break;
            case R.id.historyButton:
                Intent i =new Intent(ChannelListActivity.this, HistoryActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    @Override
	public void onBackPressed() {
    	Intent lockScreenIntent = new Intent(ChannelListActivity.this, LockScreenActivity.class);
        lockScreenIntent.putExtra("position", position);
    	startActivity(lockScreenIntent);
        finish();
	}

	// YJ ADD
    private MediaPlayer mediaPlayer = null;

    public void play(String filename) {
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "loup", filename));
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
    }


    public void startService() {

        Intent intent = new Intent(getApplicationContext(), ForegroundService.class);

        if (connection == null) {
            connection = new ServiceConnection() {

                @Override
                public void onServiceConnected(ComponentName name, IBinder binder) {
                    // Nothing to do here
                    Log.i("YJ", "Service connected");
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.i("YJ", "Service disrupted");
                }
            };
        } else {
            getApplicationContext().unbindService(connection);
            getApplicationContext().stopService(intent);
        }

        getApplicationContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        getApplicationContext().startService(intent);
    }


    //Context 메뉴로 등록한 View(여기서는 ListView)가 처음 클릭되어 만들어질 때 호출되는 메소드
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub

        //res폴더의 menu플더안에 xml로 MenuItem추가하기.
        //mainmenu.xml 파일을 java 객체로 인플레이트(inflate)해서 menu객체에 추가
        getMenuInflater().inflate(R.menu.menu_listview, menu);

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    //Context 메뉴로 등록한 View(여기서는 ListView)가 클릭되었을 때 자동으로 호출되는 메소드
    public boolean onContextItemSelected(MenuItem item) {

        //AdapterContextMenuInfo
        //AdapterView가 onCreateContextMenu할때의 추가적인 menu 정보를 관리하는 클래스
        //ContextMenu로 등록된 AdapterView(여기서는 Listview)의 선택된 항목에 대한 정보를 관리하는 클래스
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int index= info.position; //AdapterView안에서 ContextMenu를 보여즈는 항목의 위치

        //선택된 ContextMenu의  아이템아이디를 구별하여 원하는 작업 수행
        //예제에서는 선택된 ListView의 항목(String 문자열) data와 해당 메뉴이름을 출력함
        switch( item.getItemId() ){

            case R.id.delete:
                //삭제하기 기능 만들기!!!

                Toast.makeText(this, my_team_list.get(index) + " Delete", Toast.LENGTH_SHORT).show();

                //task.execute("http://52.8.57.228/delete_channel.php?test=1&h_name=" + my_id.replace(" ", "%20"));

                break;

            default:
                return super.onContextItemSelected(item);
        }

        return true;

    };

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
