package com.example.mycom.hims.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mycom.hims.Common.MyAccount;
import com.example.mycom.hims.DialogActivity.RoomAlreadyCleanDialogAcitivity;
import com.example.mycom.hims.LockScreenActivity;
import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.data.Rooms;
import com.example.mycom.hims.model.Room;
import com.example.mycom.hims.model.RoomDetailList;
import com.example.mycom.hims.model.api_response.GetRoomsResponse;
import com.example.mycom.hims.server_interface.SchedulerServerAPI;
import com.example.mycom.hims.thread.TimerDisplayThread;
import com.example.mycom.hims.thread.VMReceiverThread;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AssignedRoomActivity extends Activity implements OnAsyncTaskCompleted {

    private Typeface font;
	private String cleanerId;
	private ImageView room_back;//, state_bar;
    private TextView  state_name;
    private ListView assignedRoomLV = null;
    private TextView mTv_willCleanRoom;
    private ARListViewAdapter assignedRoomLVA = null;
	
    @Override
	public void onAsyncTaskCompleted(InputStream inputStream) {
		if (inputStream != null) {
			Reader reader = new InputStreamReader(inputStream);
        	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        	GetRoomsResponse response = gson.fromJson(reader, GetRoomsResponse.class);
        	if (response != null && response.getRooms() != null && response.getRooms().size() > 0) {
        		assignedRoomLVA = new ARListViewAdapter(this);
        		assignedRoomLV.setAdapter(assignedRoomLVA);
                Rooms.getInstance().setLists(response.getRooms());
                for (Room room : response.getRooms()) {
                	Drawable stateBar = null;
    				if ("OD".equals(room.getState())) {//여기 수정해야댐 dnd rsa
    					stateBar = getResources().getDrawable(R.color.red);
        			} else if ("VD".equals(room.getState())) {
        				stateBar = getResources().getDrawable(R.color.red);
        			} else if ("OC".equals(room.getState())) {
        				stateBar = getResources().getDrawable(R.color.yesButtonBackgroundColor);
        			} else if ("VC".equals(room.getState())) {
        				stateBar = getResources().getDrawable(R.color.yesButtonBackgroundColor);
        			}
        		}
                int count = 0;
                for(Room room : response.getRooms()){
                    if(room.getState().equals("VD") || room.getState().equals("OD")){
                        count ++;
                    }
                }
                mTv_willCleanRoom.setText(count+"");
        	}
        	
        	assignedRoomLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                	Room mData = (Room)assignedRoomLVA.getItem(position);
                    if (mData != null) {
                    	String roomState = mData.getState();
                    	if ("OC".equals(roomState) || "VC".equals(roomState)) {
                    		startActivity(new Intent(getApplicationContext(), RoomAlreadyCleanDialogAcitivity.class));
                    	} else {
                    		int room_number = mData.getNumber();
                            Intent intent = new Intent(AssignedRoomActivity.this, RoomStateActivity.class);
                            intent.putExtra("room_number", room_number);
//                            intent.putExtra("room_state", mData.getState());
//                            intent.putExtra("cleanerId", cleanerId);
//                            intent.putExtra("position", MyAccount.getInstance().getPosition());
                            startActivity(intent);
                    	}
                    }
                }
            });
		}
	}
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_room);
        mTv_willCleanRoom = (TextView)findViewById(R.id.tv_willCleanRoom);
        room_back = (ImageView)findViewById(R.id.room_back);
        assignedRoomLV = (ListView)findViewById(R.id.list_room);
        cleanerId = MyAccount.getInstance().getId();
        font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        SchedulerServerAPI.getRoomsAsync(null, null, cleanerId, null, this);
	}
	

    public void back_click(View v){
        switch(v.getId()){
            case R.id.room_back:
                Intent lockScreenIntent = new Intent(AssignedRoomActivity.this, LockScreenActivity.class);
                lockScreenIntent.putExtra("position", MyAccount.getInstance().getPosition());
                startActivity(lockScreenIntent);
            	finish();
                break;
        }
    }
    

	@Override
	public void onBackPressed() {
		Intent lockScreenIntent = new Intent(AssignedRoomActivity.this, LockScreenActivity.class);
        lockScreenIntent.putExtra("position",  MyAccount.getInstance().getPosition());
        startActivity(lockScreenIntent);
    	finish();
	}

	private class r_ViewHolder{
        public ImageView inspec_image;
        public TextView room_name;
        public ImageView bar;
    }
	
	private class ARListViewAdapter extends BaseAdapter {
        private Context mContext = null;

        public ARListViewAdapter(Context mContext){
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount(){
            return Rooms.getInstance().getLists().size();
        }
        
        @Override
        public Object getItem(int position){
            return Rooms.getInstance().getLists().get(position);
        }
        
        @Override
        public long getItemId(int position){
            return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Typeface font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");

            r_ViewHolder holder;
            if (convertView == null) {
                holder = new r_ViewHolder();
                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.staff_room_item, null);
                holder.inspec_image = (ImageView) convertView.findViewById(R.id.inspec_image);
                holder.room_name = (TextView) convertView.findViewById(R.id.room_name);
                holder.bar = (ImageView) convertView.findViewById(R.id.bar);
                holder.room_name.setTypeface(font);
                convertView.setTag(holder);
            } else {
                holder = (r_ViewHolder) convertView.getTag();
            }

            Room mData = Rooms.getInstance().getLists().get(position);
            Drawable stateBar = null;
            boolean is=false;

            switch (mData.getState()){
                case "OD" :
                    stateBar = getResources().getDrawable(R.color.red);
                    holder.inspec_image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.manicon2));
                    holder.inspec_image.setVisibility(View.VISIBLE);
                    break;
                case "OC" :
                    stateBar = getResources().getDrawable(R.color.yesButtonBackgroundColor);
                    holder.inspec_image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.manicon2));
                    holder.inspec_image.setVisibility(View.VISIBLE);
                    break;
                case "VC" :
                    stateBar = getResources().getDrawable(R.color.yesButtonBackgroundColor);
                    holder.inspec_image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.manicon2));
                    holder.inspec_image.setVisibility(View.GONE);
                    break;
                case "VD" :
                    stateBar = getResources().getDrawable(R.color.red);
                    holder.inspec_image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.manicon2));
                    holder.inspec_image.setVisibility(View.GONE);
                    break;
                case "DND" :
                    stateBar = getResources().getDrawable(R.color.red);
                    holder.inspec_image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.dnd_icon_01));
                    holder.inspec_image.setVisibility(View.VISIBLE);
                    break;
                case "RSA" :
                    stateBar = getResources().getDrawable(R.color.red);
                    holder.inspec_image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.rsa_icon_02));
                    holder.inspec_image.setVisibility(View.VISIBLE);
                    break;
            }


            holder.bar.setImageDrawable(stateBar);
            holder.room_name.setText(mData.getNumber()+"");

            return convertView;
        }
        
//        public void addItem(Drawable image, String name, Drawable bar, Room room){
//            RoomDetailList addInfo = null;
//            addInfo = new RoomDetailList();
//            addInfo.inspec_image = image;
//            addInfo.room_name = name;
//            addInfo.bar = bar;
//            addInfo.room = room;
//            mListData.add(addInfo);
//        }
        
//        public void remove(int position){
//            mListData.remove(position);
//            dataChange();
//        }
        
        public void dataChange(){
        	assignedRoomLVA.notifyDataSetChanged();
        }
    }


}
