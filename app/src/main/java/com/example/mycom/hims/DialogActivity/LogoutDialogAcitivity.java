package com.example.mycom.hims.DialogActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.example.mycom.hims.Common.CommonActivity;
import com.example.mycom.hims.Common.MyAccount;
import com.example.mycom.hims.LockScreenActivity;
import com.example.mycom.hims.MainStaffActivity;
import com.example.mycom.hims.R;
import com.example.mycom.hims.model.api_response.LogoutResponse;
import com.example.mycom.hims.scheduler.LoginActivity;
import com.example.mycom.hims.server_interface.QueryHimsServer;
import com.example.mycom.hims.server_interface.SchedulerServerAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class LogoutDialogAcitivity extends CommonActivity {

    Button mBtn_yes,mBtn_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_logout_dialog_acitivity);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onMappingXml() {
        super.onMappingXml();
        mBtn_no = (Button)findViewById(R.id.btn_no);
        mBtn_yes = (Button)findViewById(R.id.btn_yes);
    }

    @Override
    public void setListener() {
        super.setListener();
        mBtn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if (!MyAccount.getInstance().getPosition().equals("manager")) {
                    startActivity(new Intent(getApplicationContext(), MainStaffActivity.class));
                } else {

                }
            }
        });

        mBtn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream inputStream = SchedulerServerAPI.logout();
                if (inputStream != null) {
	                Reader reader = new InputStreamReader(inputStream);
	                Gson gson = new GsonBuilder().create();
	                LogoutResponse response = gson.fromJson(reader, LogoutResponse.class);
	                if (response != null && "success".equals(response.getResult())) {
	                	SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
	                    SharedPreferences.Editor editor = prefs.edit();
	                    editor.putString("p_UserID", "");    // Boolean
	                    editor.putString("p_UserPass", "");   // String
	                    editor.putString("p_UserPW", "");   // String
	                    editor.commit();
	                    QueryHimsServer.setToken("1");
	                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
	                    startActivity(intent);
	                    finish();
	                }
                }
            }
        });
    }

    @Override
    public void init() {
        super.init();
    }
}
