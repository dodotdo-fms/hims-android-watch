package com.example.mycom.hims.scheduler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycom.hims.Common.CommonActivity;
import com.example.mycom.hims.Common.MyAccount;
import com.example.mycom.hims.LockScreenActivity;
import com.example.mycom.hims.MainActivity;
import com.example.mycom.hims.R;
import com.example.mycom.hims.data.Users;
import com.example.mycom.hims.manager.MySharedPreferencesManager;
import com.example.mycom.hims.model.User;
import com.example.mycom.hims.model.api_response.GetUsersResponse;
import com.example.mycom.hims.model.api_response.LoginResponse;
import com.example.mycom.hims.server_interface.ServerCallback;
import com.example.mycom.hims.server_interface.ServerQuery;
import com.example.mycom.hims.server_interface.ServiceGenerator;
import com.google.gson.Gson;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends CommonActivity{
    private TextView room_time;
    private Typeface font;
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;

    public static String my_id;
    String p_userID;
    String p_UserPass;

    private static Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isUseLoadingDialog = true;
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        p_userID = prefs.getString("p_UserID", "");
        p_UserPass = prefs.getString("p_UserPass", "");

        font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        room_time = (TextView)findViewById(R.id.room_time);

        getUsers();




    }


    private void getUsers(){
        showLoadingDialog();
        ServerQuery.getUsers(new ServerCallback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit,int statuscode) {
                GetUsersResponse result = (GetUsersResponse) response.body();
                if (result != null && result.getUsers() != null && result.getUsers().size() > 0) {
                    Users.getInstance().setUsers(result.getUsers());
                    if (Users.getInstance().isExistMe(p_userID)) {
                        my_id = p_userID;
                        Toast.makeText(LoginActivity.this, "AutoLogin: " + p_userID, Toast.LENGTH_LONG).show();
                        MySharedPreferencesManager.getInstance().putMyID(my_id);
                        String userPW = MySharedPreferencesManager.getInstance().getMyPassword();

                        if (!TextUtils.isEmpty(my_id) && !TextUtils.isEmpty(userPW)) {
                            goLogin(userPW);
                        }

                    }
                }

                listViewInit();
                hideLoadingDialog();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoadingDialog();
            }

            @Override
            public void onFailure(int statuscode) {
                super.onFailure(statuscode);
                hideLoadingDialog();
            }
        });
    }

    private void goLogin(String pw){
        showLoadingDialog();
        ServerQuery.goLogin(my_id, pw, new ServerCallback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit,int statuscode) {
                LoginResponse result = (LoginResponse) response.body();
                if (result != null && !TextUtils.isEmpty(result.getToken())) {
                    MyAccount.getInstance().setPosition(result.getPosition());
                    MyAccount.getInstance().setId(my_id);
                    ServiceGenerator.setToken(result.getToken());
                    Intent lockScreenIntent = new Intent(LoginActivity.this, LockScreenActivity.class);
                    lockScreenIntent.putExtra("position", result.getPosition());
                    MyAccount.getInstance().setPosition(result.getPosition());
                    MySharedPreferencesManager.getInstance().setMyPosition(result.getPosition());
                    startActivity(lockScreenIntent);
                    return;
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
                hideLoadingDialog();
            }
        });
    }

    private void listViewInit(){
        mListView = (ListView) findViewById(R.id.list);
        mAdapter = new ListViewAdapter(getApplicationContext());
        mListView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = mAdapter.getItem(position);
                Intent passwordIntent = new Intent(LoginActivity.this, PasswordActivity.class);
                passwordIntent.putExtra("name", user.getName());
                passwordIntent.putExtra("id", user.getId());
                startActivity(passwordIntent);
            }
        });
    }

    public void back_click(View v) {
    	switch(v.getId()){
        	case R.id.room_back:
        		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        		startActivity(intent);
        		finish();
        		break;
    	}
}


    @Override
	public void onBackPressed() {
    	Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	//////////////////////////listvIew/////////////////////////////////
    private class ViewHolder{
        public TextView emp_name;

    }
    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;

        public ListViewAdapter(Context mContext){
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount(){
            return Users.getInstance().getUsers().size();
        }
        
        @Override
        public User getItem(int position){
            return Users.getInstance().getUsers().get(position);
        }
        
        @Override
        public long getItemId(int position){
            return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");

            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item, null);

                holder.emp_name = (TextView) convertView.findViewById(R.id.emp_name);
                holder.emp_name.setTypeface(font);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            User mData = Users.getInstance().getUsers().get(position);

            holder.emp_name.setText(mData.getName());

            return convertView;
        }




    }
}
