package com.example.mycom.hims.scheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycom.hims.LockScreenActivity;
import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
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


public class SearchActivity extends Activity implements OnAsyncTaskCompleted {
    private ImageView search_back;
    private TextView search_num;
    String search_number="";
    int[] floor_cnt = new int[100];
    private int now_floor = 0;


    int search_length = 0;
    ArrayList room_num = new ArrayList();
    ArrayList room_state = new ArrayList();

    @Override
    public void onAsyncTaskCompleted(InputStream inputStream) {
    	if (inputStream != null) {
    		Reader reader = new InputStreamReader(inputStream);
    		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    		GetRoomsResponse response = gson.fromJson(reader, GetRoomsResponse.class);
    		if (response != null && response.getRooms() != null && response.getRooms().size() > 0) {
    			Intent intent = new Intent(SearchActivity.this, RoomStateActivity.class);
                intent.putExtra("room_number", Integer.parseInt(search_number));
                intent.putExtra("room_state", response.getRooms().get(0).getState());
                intent.putExtra("position", "manager");
                startActivity(intent);
    		} else {
    			search_num.setText("None");
                search_length = 0;
                search_number = "";
    		}
    	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search_num = (TextView)findViewById(R.id.search_num);
        search_back = (ImageView)findViewById(R.id.search_back);
    }

    public void check_condition(String str){
        if(search_length<4){
            search_number += str;
            search_length++;
            search_num.setText(search_number);
        }else{}
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.search_back:
            	Intent intent = new Intent(SearchActivity.this, LockScreenActivity.class);
        		intent.putExtra("position", "manager");
        		startActivity(intent);
            	finish();
                break;
            case R.id.btn1:
                check_condition("1");
                break;
            case R.id.btn2:
                check_condition("2");
                break;
            case R.id.btn3:
                check_condition("3");
                break;
            case R.id.btn4:
                check_condition("4");
                break;
            case R.id.btn5:
                check_condition("5");
                break;
            case R.id.btn6:
                check_condition("6");
                break;
            case R.id.btn7:
                check_condition("7");
                break;
            case R.id.btn8:
                check_condition("8");
                break;
            case R.id.btn9:
                check_condition("9");
                break;
            case R.id.btn0:
                check_condition("0");
                break;
            case R.id.btn_cancel:
                if(search_length>0){
                    search_number = search_number.substring(0, search_number.length()-1);
                    search_length--;
                    search_num.setText(search_number);
                }else{}
                break;
            case R.id.btn_search:
            	if (!TextUtils.isEmpty(search_number) && TextUtils.isDigitsOnly(search_number)) {
            		SchedulerServerAPI.getRoomsAsync(search_number, null, null, null, this);
            	}
                break;
            default:
                Log.d("1", "default");
                break;
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        TimerDisplayThread.timeDisplayView = null;
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
    
    @Override
	public void onBackPressed() {
    	Intent intent = new Intent(SearchActivity.this, LockScreenActivity.class);
		intent.putExtra("position", "manager");
		startActivity(intent);
    	finish();
	}
}
