package com.example.mycom.hims;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycom.hims.Common.CommonActivity;
import com.example.mycom.hims.Common.Utill.GetDateTimeAPI;
import com.example.mycom.hims.Common.Utill.MyBatteryManager;
import com.example.mycom.hims.DialogActivity.LogoutDialogAcitivity;
import com.example.mycom.hims.View.CustomDigitalClock;
import com.example.mycom.hims.scheduler.AssignedRoomActivity;
import com.example.mycom.hims.voice_messaging.ChannelListActivity;

import java.util.Calendar;

public class MainStaffActivity extends CommonActivity {
    TextView mTv_energy,mTv_date,mTv_logout;
    CustomDigitalClock mTv_time;
    Button mBtn_radio,mBtn_room;
    ImageView mIv_logout;
    View mRel_logout,mRel_logoutDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_staff);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();
        Calendar calendar = Calendar.getInstance();
        String date = GetDateTimeAPI.getToday(getApplicationContext(), calendar.get(Calendar.DAY_OF_WEEK)) + ", " +GetDateTimeAPI.getToMonth(getApplicationContext(), calendar.get(Calendar.MONTH)) +" " + calendar.get(Calendar.DAY_OF_MONTH);
        mTv_date.setText(date);

    }

    @Override
    public void onMappingXml() {
        super.onMappingXml();
        mTv_energy = (TextView)findViewById(R.id.tv_energy);
        mTv_date = (TextView)findViewById(R.id.tv_date);
        mTv_time = (CustomDigitalClock)findViewById(R.id.tv_time);
        mTv_logout = (TextView)findViewById(R.id.tv_logout);

        mBtn_radio = (Button)findViewById(R.id.btn_radio);
        mBtn_room = (Button)findViewById(R.id.btn_room);

        mRel_logout = findViewById(R.id.rel_logout);

        mIv_logout = (ImageView)findViewById(R.id.iv_logout);
    }


    @Override
    public void setListener() {
        super.setListener();
        mRel_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), LogoutDialogAcitivity.class));
            }
        });

        mIv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), LogoutDialogAcitivity.class));
            }
        });

        mTv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), LogoutDialogAcitivity.class));
            }
        });

        mBtn_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent assignedRoomIntent = new Intent(getApplicationContext(), AssignedRoomActivity.class);
            		startActivity(assignedRoomIntent);
            }
        });

        mBtn_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_walkie = new Intent(getApplicationContext(), ChannelListActivity.class);
                startActivity(intent_walkie);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mTv_energy.setText(MyBatteryManager.getBatteryPercentage(getApplicationContext()) + "");
    }
}
