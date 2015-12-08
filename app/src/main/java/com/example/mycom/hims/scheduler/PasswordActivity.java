package com.example.mycom.hims.scheduler;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycom.hims.Common.CommonActivity;
import com.example.mycom.hims.Common.MyAccount;
import com.example.mycom.hims.LockScreenActivity;
import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.model.api_request.RequestLogin;
import com.example.mycom.hims.model.api_response.LoginResponse;
import com.example.mycom.hims.server_interface.QueryHimsServer;
import com.example.mycom.hims.server_interface.SchedulerServerAPI;
import com.example.mycom.hims.server_interface.ServerCallback;
import com.example.mycom.hims.server_interface.ServerQuery;
import com.example.mycom.hims.server_interface.ServiceGenerator;
import com.example.mycom.hims.thread.TimerDisplayThread;
import com.example.mycom.hims.thread.VMReceiverThread;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PasswordActivity extends CommonActivity {
    private TextView pass_num;
    String pass_number="";
    String manager_name;
    private RelativeLayout lay;

    int pass_length = 0;

    String password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_password);
        isUseLoadingDialog = true;
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        password = intent.getExtras().getString("pass");
        manager_name = intent.getExtras().getString("name");
        lay = (RelativeLayout)findViewById(R.id.lay);
        pass_num = (TextView)findViewById(R.id.search_num);
        lay.setVisibility(View.GONE);
    }

    public void check_condition(String str){
        if(pass_length<4){
            pass_number += str;
            pass_length++;
            pass_num.setText(pass_number);
        }else{}
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.ok_btn:
                lay.setVisibility(View.GONE);
                break;
            case R.id.search_back:
                Intent intent = new Intent(PasswordActivity.this, LoginActivity.class);
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
                if(pass_length>0){
                    pass_number = pass_number.substring(0, pass_number.length()-1);
                    pass_length--;
                    pass_num.setText(pass_number);
                }else{}
                break;
            case R.id.btn_search:
                goLogin();
                break;
            default:
                Log.d("1", "default");
                break;
        }
    }

    private void goLogin(){
        String id = getIntent().getExtras().getString("id");
        showLoadingDialog();
        ServerQuery.goLogin(id, pass_number, new ServerCallback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit,int statuscode) {
                LoginResponse result = (LoginResponse)response.body();
                if (!TextUtils.isEmpty(result.getToken())) {
                    QueryHimsServer.setToken(result.getToken());
                    SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("my_id", getIntent().getExtras().getString("id"));
                    editor.putString("p_UserID", result.getName());    // Boolean
                    editor.putString("p_UserPW", pass_number);
                    editor.putString("position", result.getPosition());
                    editor.commit();
                    ServiceGenerator.setToken(result.getToken());
                    MyAccount.getInstance().setPosition(result.getPosition());
                    MyAccount.getInstance().setId(getIntent().getExtras().getString("id"));
                    MyAccount.getInstance().setPosition(result.getPosition());
                    Intent intent = new Intent(PasswordActivity.this, LockScreenActivity.class);
                    intent.putExtra("position", result.getPosition());
                    startActivity(intent);
                } else {
                    lay.setVisibility(View.VISIBLE);
                }
                hideLoadingDialog();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoadingDialog();
            }

            @Override
            public void onFailure(int statuscode) {
                super.onFailure(statuscode);
                if(statuscode == 400){
                    Toast.makeText(getApplicationContext(),"id or password not incorrect",Toast.LENGTH_SHORT).show();
                }
                hideLoadingDialog();
            }
        });

    }



	@Override
	public void onBackPressed() {
		Intent intent = new Intent(PasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
	}
	
}
