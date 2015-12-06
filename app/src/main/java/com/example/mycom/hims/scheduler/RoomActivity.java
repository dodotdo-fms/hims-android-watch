package com.example.mycom.hims.scheduler;

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

import com.example.mycom.hims.LockScreenActivity;
import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.model.FloorList;
import com.example.mycom.hims.model.Room;
import com.example.mycom.hims.model.api_response.GetRoomsResponse;
import com.example.mycom.hims.server_interface.SchedulerServerAPI;
import com.example.mycom.hims.thread.TimerDisplayThread;
import com.example.mycom.hims.thread.VMReceiverThread;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;


public class RoomActivity extends Activity implements OnAsyncTaskCompleted {
    int check_room = 0;
    private ImageView room_back, state_bar;
    private TextView room_time, state_name;
    private Typeface font;
    int[] floor_cnt = new int[100];
  //TextView txtView;
    private int now_floor = 0;

    ArrayList room_num = new ArrayList();
    ArrayList floor_num = new ArrayList();
    ArrayList room_state = new ArrayList();
    ArrayList room_reservation = new ArrayList();
    ArrayList room_request = new ArrayList();

    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;

    @Override
    public void onAsyncTaskCompleted(InputStream inputStream) {
    	if (inputStream != null) {
	    	Reader reader = new InputStreamReader(inputStream);
	    	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	    	GetRoomsResponse response = gson.fromJson(reader, GetRoomsResponse.class);
	    	if (response != null && response.getRooms() != null && response.getRooms().size() > 0) {
	    		mListView = (ListView) findViewById(R.id.list);
	            mAdapter = new ListViewAdapter(this);
	            mListView.setAdapter(mAdapter);

	            Map<String, Integer> floorMap = new HashMap<String, Integer>();
	            for (Room room : response.getRooms()) {
	            	String floor = (room.getNumber() / 100) + "F";
	            	if (floorMap.get(floor) == null) {
	            		// roomCnt: Number of dirty rooms
	            		if ("OD".equals(room.getState()) || "VD".equals(room.getState())) {
	            			floorMap.put(floor, 1);
	            		} else {
	            			floorMap.put(floor, 0);
	            		}
	            	} else {
	            		Integer roomCnt = floorMap.get(floor);
	            		// roomCnt: Number of dirty rooms
	            		if ("OD".equals(room.getState()) || "VD".equals(room.getState())) {
	            			roomCnt = roomCnt + 1;
	            		}
	            		floorMap.put(floor, roomCnt);
	            	}
	            }
	            
	            SortedSet<String> keys = new TreeSet<String>(floorMap.keySet());
	            for (String key : keys) {
	            	mAdapter.addItem(getResources().getDrawable(R.drawable.red_c), key, String.valueOf(floorMap.get(key)));
	            }
	
	            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
	                @Override
	                public void onItemClick(AdapterView<?> parent, View v, int position, long id){
	                    FloorList mData = mAdapter.mFloorList.get(position);
	                    String floorStr = mData.floor_name.substring(0, mData.floor_name.length() - 1);
	                    int selectedFloor = Integer.valueOf(floorStr);
	                    Intent intent = new Intent(RoomActivity.this, RoomDetailActivity.class);
	                    intent.putExtra("now_floor", selectedFloor);
	                    startActivity(intent);
	                }
	            });
	    	}
    	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        //txtView = (TextView) findViewById(R.id.txtView);
        room_back = (ImageView)findViewById(R.id.room_back);
        room_time = (TextView)findViewById(R.id.room_time);

        /* [yskim: new interface] - rooms */
        SchedulerServerAPI.getRoomsAsync(null, null, null, null, this);
    }

    public void reset_btn(View v){
//        switch(v.getId()){
//            case R.id.state_bar:
//                    SchedulerServerAPI.getRoomsAsync(null, null, null, null, this);
////                    mAdapter.notifyDataSetChanged();
//                break;
//        }
    }
    public void back_click(View v){
        switch(v.getId()){
            case R.id.room_back:
            	Intent intent = new Intent(RoomActivity.this, LockScreenActivity.class);
        		intent.putExtra("position", "manager");
        		startActivity(intent);
            	finish();
                break;
        }
    }
    
    @Override
	public void onBackPressed() {
    	Intent intent = new Intent(RoomActivity.this, LockScreenActivity.class);
		intent.putExtra("position", "manager");
		startActivity(intent);
    	finish();
	}

    //////////////////////////listvIew/////////////////////////////////
    private class ViewHolder{
        public ImageView floor_image;
        public TextView floor_name;
        public TextView floor_number;

    }
    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<FloorList> mFloorList = new ArrayList<FloorList>();

        public ListViewAdapter(Context mContext){
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount(){
            return mFloorList.size();
        }
        @Override
        public Object getItem(int position){
            return mFloorList.get(position);
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
                convertView = inflater.inflate(R.layout.floor_item, null);

                holder.floor_image = (ImageView) convertView.findViewById(R.id.floor_image);
                holder.floor_name = (TextView) convertView.findViewById(R.id.floor_name);
                holder.floor_number = (TextView) convertView.findViewById(R.id.floor_number);
                holder.floor_name.setTypeface(font);
                holder.floor_number.setTypeface(font);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            FloorList mData = mFloorList.get(position);
            if(mData.floor_image != null){
                holder.floor_image.setVisibility(View.VISIBLE);
                holder.floor_image.setImageDrawable(mData.floor_image);
            }else{
                holder.floor_image.setVisibility(View.GONE);
            }
            holder.floor_number.setText(mData.floor_number);
            holder.floor_name.setText(mData.floor_name);

            return convertView;
        }
        public void addItem(Drawable image, String name, String number){
            FloorList addInfo = null;
            addInfo = new FloorList();
            addInfo.floor_image = image;
            addInfo.floor_name = name;
            addInfo.floor_number = number;

            mFloorList.add(addInfo);
        }
        public void remove(int position){
            mFloorList.remove(position);
            dataChange();
        }
        public void dataChange(){
            mAdapter.notifyDataSetChanged();
        }
    }
////////////////////////////////////////////////////////////////////////////////

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



