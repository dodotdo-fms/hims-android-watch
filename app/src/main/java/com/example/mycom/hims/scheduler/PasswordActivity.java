package com.example.mycom.hims.scheduler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mycom.hims.common.CommonActivity;
import com.example.mycom.hims.common.MyAccount;
import com.example.mycom.hims.MainStaffActivity;
import com.example.mycom.hims.R;
import com.example.mycom.hims.model.api_response.LoginResponse;
import com.example.mycom.hims.server_interface.QueryHimsServer;
import com.example.mycom.hims.server_interface.ServerQuery;
import com.example.mycom.hims.server_interface.ServiceGenerator;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PasswordActivity extends CommonActivity {
    private TextView pass_num;
    String pass_number="";
    String manager_name;
//    private RelativeLayout lay;
    boolean isBack = true;
    int pass_length = 0;

    String password;

    ImageView mBtn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_password);
        super.onCreate(savedInstanceState);
        mBtn_cancel = (ImageView)findViewById(R.id.btn_cancel);

        Intent intent = getIntent();
        password = intent.getExtras().getString("pass");
        manager_name = intent.getExtras().getString("name");
//        lay = (RelativeLayout)findViewById(R.id.lay);
        pass_num = (TextView)findViewById(R.id.search_num);
        pass_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0){
                    isBack = true;
                    mBtn_cancel.setImageDrawable(getResources().getDrawable(R.drawable.back_white));
                    RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()),(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
                    mBtn_cancel.setLayoutParams(param);
                }else if(s.length() == 1){
                    isBack = false;
                    mBtn_cancel.setImageDrawable(getResources().getDrawable(R.drawable.cancel));
                    RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 33, getResources().getDisplayMetrics()));
                    mBtn_cancel.setLayoutParams(param);
                }
            }
        });
//        lay.setVisibility(View.GONE);
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
//            case R.id.ok_btn:
////                lay.setVisibility(View.GONE);
//                break;
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
                goCancel();
                break;
            case R.id.btn_search:
                goLogin();
                break;
            case R.id.view_search:
                goLogin();
                break;
            case R.id.view_cancel :
                goCancel();
                break;
            default:
                Log.d("1", "default");
                break;
        }
    }

    private void goCancel(){
        if(!isBack){
            pass_number = pass_number.substring(0, pass_number.length()-1);
            pass_length--;
            pass_num.setText(pass_number);
        }else{
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }
    }

    private void goLogin(){
        String id = getIntent().getExtras().getString("id");
        showLoadingDialog();
        ServerQuery.goLogin(id, pass_number, new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
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
                    Intent intent = new Intent(PasswordActivity.this, MainStaffActivity.class);
                    intent.putExtra("position", result.getPosition());
                    startActivity(intent);
                    finish();
                } else {
//                    lay.setVisibility(View.VISIBLE);
                }
                hideLoadingDialog();
            }

            @Override
            public void onFailure(Throwable t) {
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
