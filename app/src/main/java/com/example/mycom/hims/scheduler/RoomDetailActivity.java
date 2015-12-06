package com.example.mycom.hims.scheduler;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.model.Evaluation;
import com.example.mycom.hims.model.Room;
import com.example.mycom.hims.model.RoomDetailList;
import com.example.mycom.hims.model.api_response.GetRoomsResponse;
import com.example.mycom.hims.server_interface.SchedulerServerAPI;
import com.example.mycom.hims.thread.TimerDisplayThread;
import com.example.mycom.hims.thread.VMReceiverThread;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RoomDetailActivity extends Activity implements OnAsyncTaskCompleted {

    private ImageView room_back, state_bar;
    private TextView room_time, state_name;
    private Typeface font;
    int[] floor_cnt = new int[100];
    //TextView txtView;

    ArrayList room_num = new ArrayList();
    ArrayList floor_num = new ArrayList();
    ArrayList room_state = new ArrayList();
    ArrayList room_reservation = new ArrayList();
    ArrayList room_request = new ArrayList();

    private ListView r_ListView = null;
    private r_ListViewAdapter r_Adapter = null;
    int now_floor;

    @Override
    public void onAsyncTaskCompleted(InputStream inputStream) {
    	if (inputStream != null) {
    		Reader reader = new InputStreamReader(inputStream);
        	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        	GetRoomsResponse response = gson.fromJson(reader, GetRoomsResponse.class);
        	
        	if (response != null && response.getRooms() != null && response.getRooms().size() > 0) {
        		r_Adapter = new r_ListViewAdapter(this);
                r_ListView.setAdapter(r_Adapter);
                for (Room room : response.getRooms()) {
        			int floorVal = room.getNumber() / 100;
        			if (floorVal == now_floor) {
        				Drawable inspectionIcon = null;
        				if (room.getInspectAssignedDate() != null) {
        					Evaluation evalResult = room.getEvaluation();
        					if (evalResult != null && ("short".equals(evalResult.getType()) || "long".equals(evalResult.getType()))) {
        						inspectionIcon = getResources().getDrawable(R.drawable.inspec);
        					} else {
        						inspectionIcon = getResources().getDrawable(R.drawable.inspect_gray);
        					}
        				}
        				
        				Drawable stateBar = null;
        				if ("OD".equals(room.getState())) {
            				stateBar = getResources().getDrawable(R.drawable.od);
            			} else if ("VD".equals(room.getState())) {
            				stateBar = getResources().getDrawable(R.drawable.vd);
            			} else if ("OC".equals(room.getState())) {
            				stateBar = getResources().getDrawable(R.drawable.oc);
            			} else if ("VC".equals(room.getState())) {
            				stateBar = getResources().getDrawable(R.drawable.vc);
            			}
        				
        				r_Adapter.addItem(inspectionIcon, String.valueOf(room.getNumber()), stateBar, room);
        			}
        		}
        	}
        	
        	r_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    RoomDetailList mData = r_Adapter.mListData.get(position);
                    int room_number = mData.room.getNumber();
                    Intent intent = new Intent(RoomDetailActivity.this, RoomStateActivity.class);
                    intent.putExtra("room_number", room_number);
                    intent.putExtra("room_state", mData.room.getState());
                    intent.putExtra("position", "manager");
                    startActivity(intent);
                }
            });
    	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail_activity);
        Intent intent = getIntent();
        now_floor = intent.getExtras().getInt("now_floor");

        room_back = (ImageView)findViewById(R.id.room_back);
        room_time = (TextView)findViewById(R.id.room_time);
//        state_bar = (ImageView)findViewById(R.id.state_bar);
        r_ListView = (ListView)findViewById(R.id.list_room);

        /* [yskim: new interface] - rooms */
        SchedulerServerAPI.getRoomsAsync(null, null, null, null, this);
    }


    public void reset_btn(View v){
//        switch(v.getId()){
//            case R.id.state_bar:
//            	SchedulerServerAPI.getRoomsAsync(null, null, null, null, this);
//                break;
//        }
    }
    public void back_click(View v){
        switch(v.getId()){
            case R.id.room_back:
            	Intent intent_room = new Intent(RoomDetailActivity.this, RoomActivity.class);
                startActivity(intent_room);
            	finish();
                break;
        }
    }

    @Override
	public void onBackPressed() {
    	Intent intent_room = new Intent(RoomDetailActivity.this, RoomActivity.class);
        startActivity(intent_room);
    	finish();
	}

	private class r_ViewHolder{
        public ImageView inspec_image;
        public TextView room_name;
        public ImageView bar;


    }
    private class r_ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<RoomDetailList> mListData = new ArrayList<RoomDetailList>();

        public r_ListViewAdapter(Context mContext){
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount(){
            return mListData.size();
        }
        @Override
        public Object getItem(int position){
            return mListData.get(position);
        }
        @Override
        public long getItemId(int position){
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");

            r_ViewHolder holder;
            if(convertView == null){
                holder = new r_ViewHolder();

                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.room_item, null);

                holder.inspec_image = (ImageView) convertView.findViewById(R.id.inspec_image);
                holder.room_name = (TextView) convertView.findViewById(R.id.room_name);
                holder.bar = (ImageView) convertView.findViewById(R.id.bar);
                holder.room_name.setTypeface(font);


                convertView.setTag(holder);
            }else{
                holder = (r_ViewHolder) convertView.getTag();
            }

            RoomDetailList mData = mListData.get(position);
            if(mData.inspec_image != null){
                holder.inspec_image.setVisibility(View.VISIBLE);
                holder.inspec_image.setImageDrawable(mData.inspec_image);
                holder.bar.setVisibility(View.VISIBLE);
                holder.bar.setImageDrawable(mData.bar);
            }else{
                holder.inspec_image.setVisibility(View.GONE);
                holder.bar.setVisibility(View.VISIBLE);
                holder.bar.setImageDrawable(mData.bar);
            }

            holder.room_name.setText(mData.room_name);

            return convertView;
        }
        public void addItem(Drawable image, String name, Drawable bar, Room room){
            RoomDetailList addInfo = null;
            addInfo = new RoomDetailList();
            addInfo.inspec_image = image;
            addInfo.room_name = name;
            addInfo.bar = bar;
            addInfo.room = room;
            mListData.add(addInfo);
        }
        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }
        public void dataChange(){
            r_Adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        TimerDisplayThread.timeDisplayView = room_time;
        TimerDisplayThread.dateDisplayView = null;
        VMReceiverThread.shouldDisplay = false;
    }

    @Override
    protected void onDestroy() {
        TimerDisplayThread.timeDisplayView = null;
        TimerDisplayThread.dateDisplayView = null;
        VMReceiverThread.shouldDisplay = false;
        super.onDestroy();
    }
}
