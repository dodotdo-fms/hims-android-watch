package com.example.mycom.hims.scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycom.hims.Common.App;
import com.example.mycom.hims.Common.CommonActivity;
import com.example.mycom.hims.DialogActivity.LogoutDialogAcitivity;
import com.example.mycom.hims.R;
import com.example.mycom.hims.manager.MySharedPreferencesManager;
import com.example.mycom.hims.server_interface.ServerQuery;

public class SetUpActivity extends CommonActivity {
    Button mBtn_OnOff;
    Button mBtn_logout;
    TextView mTv_OnOff;
    TextView mTv_logout;
    View mView_onOff;
    View mView_logout;
    ImageView mIv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set_up);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMappingXml() {
        super.onMappingXml();
        mIv_back = (ImageView)findViewById(R.id.room_back);

        mBtn_logout = (Button)findViewById(R.id.btn_logout);
        mBtn_OnOff = (Button)findViewById(R.id.btn_lockOnOff);

        mTv_logout = (TextView)findViewById(R.id.tv_logout);
        mTv_OnOff = (TextView)findViewById(R.id.tv_OnOff);

        mView_logout = findViewById(R.id.view_logout);
        mView_onOff = findViewById(R.id.view_onOff);
    }

    @Override
    public void setListener() {
        super.setListener();

        mBtn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), LogoutDialogAcitivity.class), 0);
            }
        });

        mTv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), LogoutDialogAcitivity.class),0);

            }
        });

        mTv_OnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnOff();
            }
        });

        mBtn_OnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnOff();
                           }
        });

        mView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), LogoutDialogAcitivity.class), 0);

            }
        });

        mView_onOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnOff();
            }
        });

        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void clickOnOff(){
        if(MySharedPreferencesManager.getInstance().getIsScreenLock()){
            mTv_OnOff.setTextColor(getResources().getColor(R.color.white));
            mTv_OnOff.setText("Off");
            mBtn_OnOff.setBackgroundDrawable(getResources().getDrawable(R.drawable.off_xtouch));
        }else{
            mTv_OnOff.setText("On");
            mTv_OnOff.setTextColor(getResources().getColor(R.color.yesButtonBackgroundColor));
            mBtn_OnOff.setBackgroundDrawable(getResources().getDrawable(R.drawable.on_xtouch));
            App.goScreenService();
        }
        MySharedPreferencesManager.getInstance().setisScreenLock(!MySharedPreferencesManager.getInstance().getIsScreenLock());

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1 && requestCode == 0){
            Log.e("aaa","sdwdwd");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
