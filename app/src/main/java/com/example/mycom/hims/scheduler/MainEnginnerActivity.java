package com.example.mycom.hims.scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycom.hims.R;
import com.example.mycom.hims.common.CommonActivity;
import com.example.mycom.hims.common.utill.MyBatteryManager;
import com.example.mycom.hims.view.CustomDigitalClock;
import com.example.mycom.hims.voice_messaging.ChannelListActivity;

public class MainEnginnerActivity extends CommonActivity {
    TextView mTv_energy,mTv_date;
    CustomDigitalClock mTv_time;
    Button mBtn_radio, mBtn_repair;
    ImageView mIv_setUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_enginner);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();

    }

    @Override
    public void onMappingXml() {
        super.onMappingXml();
        mTv_energy = (TextView)findViewById(R.id.tv_energy);
//        mTv_date = (TextView)findViewById(R.id.tv_date);
        mTv_time = (CustomDigitalClock)findViewById(R.id.tv_time);
        mBtn_radio = (Button)findViewById(R.id.btn_radio);
        mBtn_repair = (Button)findViewById(R.id.btn_refair);


        mIv_setUp = (ImageView)findViewById(R.id.iv_setUp);
    }


    @Override
    public void setListener() {
        super.setListener();

        mIv_setUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SetUpActivity.class));
            }
        });

        mBtn_repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Coming soon.",Toast.LENGTH_SHORT).show();
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
