package com.example.mycom.hims;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycom.hims.common.CommonActivity;
import com.example.mycom.hims.common.utill.MyBatteryManager;
import com.example.mycom.hims.view.CustomDigitalClock;
import com.example.mycom.hims.scheduler.AssignedRoomActivity;
import com.example.mycom.hims.scheduler.SetUpActivity;
import com.example.mycom.hims.voice_messaging.ChannelListActivity;

public class MainStaffActivity extends CommonActivity {
    TextView mTv_energy;
    CustomDigitalClock mTv_time;
    Button mBtn_radio,mBtn_room;
    ImageView mIv_setUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_staff);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();
//        Calendar calendar = Calendar.getInstance();
//        String date = GetDateTimeAPI.getToday(getApplicationContext(), calendar.get(Calendar.DAY_OF_WEEK)) + ", " +GetDateTimeAPI.getToMonth(getApplicationContext(), calendar.get(Calendar.MONTH)) +" " + calendar.get(Calendar.DAY_OF_MONTH);
//        mTv_date.setText(date);

    }

    @Override
    public void onMappingXml() {
        super.onMappingXml();
        mTv_energy = (TextView)findViewById(R.id.tv_energy);
//        mTv_date = (TextView)findViewById(R.id.tv_date);
        mTv_time = (CustomDigitalClock)findViewById(R.id.tv_time);
        mBtn_radio = (Button)findViewById(R.id.btn_radio);
        mBtn_room = (Button)findViewById(R.id.btn_room);


        mIv_setUp = (ImageView)findViewById(R.id.iv_setUp);
    }


    @Override
    public void setListener() {
        super.setListener();

        mIv_setUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), LogoutDialogAcitivity.class));
                startActivity(new Intent(getApplicationContext(), SetUpActivity.class));
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
        mTv_energy.setText(MyBatteryManager.getBatteryPercentage(getApplicationContext()) + "%");
    }
}
