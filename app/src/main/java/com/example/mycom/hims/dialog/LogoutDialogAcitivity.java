package com.example.mycom.hims.dialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mycom.hims.common.App;
import com.example.mycom.hims.common.CommonActivity;
import com.example.mycom.hims.common.MyAccount;
import com.example.mycom.hims.MainStaffActivity;
import com.example.mycom.hims.R;
import com.example.mycom.hims.model.api_response.LogoutResponse;
import com.example.mycom.hims.server_interface.ServerQuery;
import com.example.mycom.hims.server_interface.ServiceGenerator;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
                showLoadingDialog();
	                ServerQuery.goLogout(new Callback() {
                        @Override
                        public void onResponse(Response response, Retrofit retrofit) {
                            LogoutResponse result = (LogoutResponse)response.body();
                            if (result != null && "success".equals(result.getResult())) {
                                SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("p_UserID", "");    // Boolean
                                editor.putString("p_UserPass", "");   // String
                                editor.putString("p_UserPW", "");   // String
                                editor.commit();
                                MyAccount.getInstance().setId(null);
                                MyAccount.getInstance().setPosition(null);
                                ServiceGenerator.setToken("");
                                App.cleanMemory();
                                setResult(1);
                                finish();
                            }
                            hideLoadingDialog();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            hideLoadingDialog();
                        }
                    });

            }
        });
    }

    @Override
    public void init() {
        super.init();
    }
}
