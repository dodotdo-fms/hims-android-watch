package com.example.mycom.hims;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.mycom.hims.Common.MyAccount;


public class LockScreenActivity extends Activity {
    private ImageView search_btn, room_btn, walkie_btn, staff_room_btn, staff_radio_btn;
    private RelativeLayout lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

//        lay = (RelativeLayout) findViewById(R.id.lay);
//        search_btn = (ImageView) findViewById(R.id.search_btn);
//        room_btn = (ImageView) findViewById(R.id.room_btn);
//        walkie_btn = (ImageView) findViewById(R.id.walkie_btn);
//        staff_room_btn = (ImageView) findViewById(R.id.staff_room_btn);
//        staff_radio_btn = (ImageView) findViewById(R.id.staff_radio_btn);
//        lay.setVisibility(View.GONE);

//        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
//        MyAccount.getInstance().setId(prefs.getString("p_UserID", ""));
//        ChannelListActivity.my_id = myId;

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        /* Start the time display thread */
//        Thread timerDisplayThread = new Thread(new TimerDisplayThread(getApplicationContext()));
//        timerDisplayThread.start();
//
//        TimerDisplayThread.timeDisplayView = (TextView) findViewById(R.id.time);
//        TimerDisplayThread.dateDisplayView = (TextView) findViewById(R.id.date);
        if(MyAccount.getInstance().getPosition().equals("manager")){

        }else{
            startActivity(new Intent(this,MainStaffActivity.class));
        }
//        String position = getIntent().getExtras().getString("position");
//        if (!"manager".equals(position)) {
//    		search_btn.setVisibility(View.GONE);
//    		room_btn.setVisibility(View.GONE);
//    		walkie_btn.setVisibility(View.GONE);
//    		staff_room_btn.setVisibility(View.VISIBLE);
//    		staff_radio_btn.setVisibility(View.VISIBLE);
//    	}
    }

    public void moving_click(View v){
//        switch(v.getId()){
//            case R.id.search_btn:
//                Intent intent_search = new Intent(LockScreenActivity.this, SearchActivity.class);
//                startActivity(intent_search);
//                Log.d("1", "Search button clicked");
//                break;
//            case R.id.room_btn:
//            case R.id.staff_room_btn:
//            	String position = getIntent().getExtras().getString("position");
//            	if ("manager".equals(position)) {
//            		Intent intent_room = new Intent(LockScreenActivity.this, RoomActivity.class);
//                    startActivity(intent_room);
//            	} else {
//            		Intent assignedRoomIntent = new Intent(LockScreenActivity.this, AssignedRoomActivity.class);
//            		assignedRoomIntent.putExtra("cleanerId", realUserId);
//            		assignedRoomIntent.putExtra("position", position);
//            		startActivity(assignedRoomIntent);
//            	}
//                break;
//            case R.id.walkie_btn:
//            case R.id.staff_radio_btn:
//                Intent intent_walkie = new Intent(LockScreenActivity.this, ChannelListActivity.class);
//                startActivity(intent_walkie);
//                Log.d("3", "Walkie button clicked");
//                break;
//            case R.id.logout_btn:
//                lay.setVisibility(View.VISIBLE);
//                break;
//            case R.id.yes_btn:      /* [yskim: new interface] - users/logout */
//                InputStream inputStream = SchedulerServerAPI.logout();
//                if (inputStream != null) {
//	                Reader reader = new InputStreamReader(inputStream);
//	                Gson gson = new GsonBuilder().create();
//	                LogoutResponse response = gson.fromJson(reader, LogoutResponse.class);
//	                if (response != null && "success".equals(response.getResult())) {
//	                	SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
//	                    SharedPreferences.Editor editor = prefs.edit();
//	                    editor.putString("p_UserID", "");    // Boolean
//	                    editor.putString("p_UserPass", "");   // String
//	                    editor.putString("p_UserPW", "");   // String
//	                    editor.commit();
//	                    QueryHimsServer.setToken("1");
//	                    Intent intent = new Intent(LockScreenActivity.this, LoginActivity.class);
//	                    startActivity(intent);
//	                    finish();
//	                }
//                }
//                break;
//            case R.id.no_btn:
//                lay.setVisibility(View.GONE);
//                break;
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
