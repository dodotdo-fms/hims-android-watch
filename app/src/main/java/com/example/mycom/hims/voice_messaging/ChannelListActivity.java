package com.example.mycom.hims.voice_messaging;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mycom.hims.common.CommonActivity;
import com.example.mycom.hims.common.MyAccount;
import com.example.mycom.hims.MainStaffActivity;
import com.example.mycom.hims.R;
import com.example.mycom.hims.view.FooterViewHolder;
import com.example.mycom.hims.view.MyRecyclerView;
import com.example.mycom.hims.view.ViewHolderFactory;
import com.example.mycom.hims.data.Channels;
import com.example.mycom.hims.data.Messages;
import com.example.mycom.hims.model.Channel;
import com.example.mycom.hims.model.api_response.GetChannelResponse;
import com.example.mycom.hims.server_interface.ServerQuery;
import com.google.gson.Gson;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static java.util.Collections.*;


public class ChannelListActivity extends CommonActivity {

    private static Gson gson = new Gson();
    boolean scrollCheck = false;
    boolean loadingMoreContent =false;

    String position = "";


    private MyRecyclerView recyclerView;
    private ChannelRecyclerViewAdpater adapter;

    private static ServiceConnection connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_list_page);
        super.onCreate(savedInstanceState);

//        /* start ForegroundService */
//        startService();
//
//        //ListView를 Context 메뉴로 등록
//        registerForContextMenu(mListView);
//
//        /* start VM receiver thread */
//        if (VMReceiverThread.isStarted == false) {
//            Thread vmReciverThread = new Thread(new VMReceiverThread(getApplicationContext()));
//            vmReciverThread.start();
//        }
    }

    public void onClick(View v){
        Intent lockScreenIntent;
        switch(v.getId()){
            case R.id.back:
                lockScreenIntent = new Intent(ChannelListActivity.this, MainStaffActivity.class);
                lockScreenIntent.putExtra("position", position);
            	startActivity(lockScreenIntent);
                finish();
                break;
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
    	Intent lockScreenIntent = new Intent(ChannelListActivity.this, MainStaffActivity.class);
        lockScreenIntent.putExtra("position", position);
    	startActivity(lockScreenIntent);
        finish();
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
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenu.ContextMenuInfo menuInfo) {
//        // TODO Auto-generated method stub
//
//        //res폴더의 menu플더안에 xml로 MenuItem추가하기.
//        //mainmenu.xml 파일을 java 객체로 인플레이트(inflate)해서 menu객체에 추가
//        getMenuInflater().inflate(R.menu.menu_listview, menu);
//
//        super.onCreateContextMenu(menu, v, menuInfo);
//    }

    //Context 메뉴로 등록한 View(여기서는 ListView)가 클릭되었을 때 자동으로 호출되는 메소드
//    public boolean onContextItemSelected(MenuItem item) {
//
//        //AdapterContextMenuInfo
//        //AdapterView가 onCreateContextMenu할때의 추가적인 menu 정보를 관리하는 클래스
//        //ContextMenu로 등록된 AdapterView(여기서는 Listview)의 선택된 항목에 대한 정보를 관리하는 클래스
//        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//        int index= info.position; //AdapterView안에서 ContextMenu를 보여즈는 항목의 위치
//
//        //선택된 ContextMenu의  아이템아이디를 구별하여 원하는 작업 수행
//        //예제에서는 선택된 ListView의 항목(String 문자열) data와 해당 메뉴이름을 출력함
//        switch( item.getItemId() ){
//
//            case R.id.delete:
//                //삭제하기 기능 만들기!!!
//
//                Toast.makeText(this, my_team_list.get(index) + " Delete", Toast.LENGTH_SHORT).show();
//
//                //task.execute("http://52.8.57.228/delete_channel.php?test=1&h_name=" + my_id.replace(" ", "%20"));
//
//                break;
//
//            default:
//                return super.onContextItemSelected(item);
//        }
//
//        return true;
//
//    }


    class ChannelRecyclerViewAdpater extends MyRecyclerView.Adapter<MyRecyclerView.ViewHolder> {
        private static final int VIEWTPYE_FOOTER = 0;
        private boolean footerVisible;
        public final Context mContext;
        public static final int VIEWTPYE_ROOM = 1;
        public ChannelRecyclerViewAdpater(Context context) {
            mContext = context;
        }

        public MyRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEWTPYE_ROOM:
                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.slist_item, parent, false);
                    return new RoomViewHolder(view);
                case VIEWTPYE_FOOTER:
                    return new FooterViewHolder(parent);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final MyRecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ViewHolderFactory.Updateable) {
                ((ViewHolderFactory.Updateable) holder).update(Channels.getInstance().getChannels().get(position));
            }
        }

        @Override
        public int getItemCount() {
            return Channels.getInstance().getChannels().size() + (useFooter() ? 1 : 0);
        }

        @Override
        public int getItemViewType(int position) {
            if(getItemCount() == position){
                if(useFooter()){
                    return VIEWTPYE_FOOTER;
                }
                return VIEWTPYE_ROOM;
            }
            return VIEWTPYE_ROOM;
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


    private class RoomViewHolder extends MyRecyclerView.ViewHolder implements ViewHolderFactory.Updateable<Channel> {
        View view;
        TextView name;
        public RoomViewHolder(final View itemView) {
            super(itemView);
            view = itemView;
            name = (TextView) itemView.findViewById(R.id.channel_name);
        }

        @Override
        public void update(final Channel item) {
            name.setText(item.getName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChannelListActivity.this, MessageSendActivity.class);
                    Log.e("itemid",item.getId()+"as");
                    intent.putExtra("channel_id", item.getId());
                    startActivity(intent);
                }
            });

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
        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        position = MyAccount.getInstance().getPosition();
        adapter = new ChannelRecyclerViewAdpater(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                refresh();

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
        Log.e("id",MyAccount.getInstance().getId()+"a");
    }


    private void refresh(){
        ServerQuery.getChannel(null, MyAccount.getInstance().getId(), new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                GetChannelResponse result = (GetChannelResponse)response.body();
                if (result == null) {
                    return;
                }
        /* get response */
                if (result == null || result.getChannels() == null || result.getChannels().size() == 0) {
                    return;
                }

                int index = 0;
                List<Channel> channelList = result.getChannels();
                reverse(channelList);
                Channels.getInstance().setChannels(channelList);

                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
