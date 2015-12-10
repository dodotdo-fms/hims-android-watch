package com.example.mycom.hims.scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mycom.hims.Common.CommonActivity;
import com.example.mycom.hims.DialogActivity.LogoutDialogAcitivity;
import com.example.mycom.hims.R;
import com.example.mycom.hims.manager.MySharedPreferencesManager;

public class SetUpActivity extends CommonActivity {
    Button mBtn_OnOff;
    Button mBtn_logout;
    TextView mTv_OnOff;
    TextView mTv_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_up);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMappingXml() {
        super.onMappingXml();
        mBtn_logout = (Button)findViewById(R.id.btn_logout);
        mBtn_OnOff = (Button)findViewById(R.id.btn_lockOnOff);

        mTv_logout = (TextView)findViewById(R.id.tv_logout);
        mTv_OnOff = (TextView)findViewById(R.id.tv_OnOff);

    }

    @Override
    public void setListener() {
        super.setListener();

        mBtn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LogoutDialogAcitivity.class));
            }
        });

        mTv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LogoutDialogAcitivity.class));

            }
        });

        mBtn_OnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MySharedPreferencesManager.getInstance().getIsScreenLock()){
                    mTv_OnOff.setTextColor(getResources().getColor(R.color.white));
                    mTv_OnOff.setText("Off");
                    mBtn_OnOff.setBackgroundDrawable(getResources().getDrawable(R.drawable.off_xtouch));
                }else{
                    mTv_OnOff.setText("On");
                    mTv_OnOff.setTextColor(getResources().getColor(R.color.yesButtonBackgroundColor));
                    mBtn_OnOff.setBackgroundDrawable(getResources().getDrawable(R.drawable.on_xtouch));
                }
                MySharedPreferencesManager.getInstance().setisScreenLock(!MySharedPreferencesManager.getInstance().getIsScreenLock());
            }
        });

    }

    @Override
    public void init() {
        super.init();
        if(MySharedPreferencesManager.getInstance().getIsScreenLock()){
            mTv_OnOff.setText("On");
            mTv_OnOff.setTextColor(getResources().getColor(R.color.yesButtonBackgroundColor));
            mBtn_OnOff.setBackgroundDrawable(getResources().getDrawable(R.drawable.on_xtouch));
        }else{
            mTv_OnOff.setTextColor(getResources().getColor(R.color.white));
            mTv_OnOff.setText("Off");
            mBtn_OnOff.setBackgroundDrawable(getResources().getDrawable(R.drawable.off_xtouch));
        }
    }
}
