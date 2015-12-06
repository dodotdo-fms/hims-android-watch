package com.example.mycom.hims.scheduler;

import android.app.Activity;
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

import com.example.mycom.hims.Common.MyAccount;
import com.example.mycom.hims.LockScreenActivity;
import com.example.mycom.hims.MainActivity;
import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.data.Users;
import com.example.mycom.hims.manager.MySharedPreferencesManager;
import com.example.mycom.hims.model.User;
import com.example.mycom.hims.model.api_response.GetUsersResponse;
import com.example.mycom.hims.model.api_response.LoginResponse;
import com.example.mycom.hims.server_interface.QueryHimsServer;
import com.example.mycom.hims.server_interface.SchedulerServerAPI;
import com.example.mycom.hims.thread.TimerDisplayThread;
import com.example.mycom.hims.thread.VMReceiverThread;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity implements OnAsyncTaskCompleted{
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        p_userID = prefs.getString("p_UserID", "");
        p_UserPass = prefs.getString("p_UserPass", "");

        font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        room_time = (TextView)findViewById(R.id.room_time);

        SchedulerServerAPI.getUsersAsync(null, null, null, null, this);
    }

    @Override
    public void onAsyncTaskCompleted(InputStream inputStream) {
        if (inputStream != null) {
	    	Reader reader = new InputStreamReader(inputStream);
	        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	        
	        GetUsersResponse response = gson.fromJson(reader, GetUsersResponse.class);
	        if (response != null && response.getUsers() != null && response.getUsers().size() > 0) {
		        Users.getInstance().setUsers(response.getUsers());
		        
		        	if (Users.getInstance().isExistMe(p_userID)) {
		        		my_id = p_userID;
		        		Toast.makeText(LoginActivity.this, "AutoLogin: " + p_userID, Toast.LENGTH_LONG).show();
                        MySharedPreferencesManager.getInstance().putMyID(my_id);
		        		String userPW = MySharedPreferencesManager.getInstance().getMyPassword();
		        		
		        		if (!TextUtils.isEmpty(my_id) && !TextUtils.isEmpty(userPW)) {
		        			InputStream loginIS = SchedulerServerAPI.login(my_id, userPW);
		        			Reader loginReader = new InputStreamReader(loginIS);
		        			LoginResponse loginRes = gson.fromJson(loginReader, LoginResponse.class);
		        			if (loginRes != null && !TextUtils.isEmpty(loginRes.getToken())) {
                                MyAccount.getInstance().setPosition(loginRes.getPosition());
		        				QueryHimsServer.setToken(loginRes.getToken());
		        				Intent lockScreenIntent = new Intent(LoginActivity.this, LockScreenActivity.class);
		        				lockScreenIntent.putExtra("position", loginRes.getPosition());
                                MyAccount.getInstance().setPosition(loginRes.getPosition());
                                MySharedPreferencesManager.getInstance().setMyPosition(loginRes.getPosition());
		    	        		startActivity(lockScreenIntent);
		    	        		return;
		        			}
		        		}
		        		
		        	}

		        
		        mListView = (ListView) findViewById(R.id.list);
		        mAdapter = new ListViewAdapter(this);
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
        }
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
    protected void onStart(){
        super.onStart();
        TimerDisplayThread.timeDisplayView = room_time;
        TimerDisplayThread.dateDisplayView = null;
        VMReceiverThread.shouldDisplay = false;
    }

    @Override
    protected void onDestroy() {
        TimerDisplayThread.timeDisplayView = null;
        TimerDisplayThread.dateDisplayView = null;
        VMReceiverThread.shouldDisplay = false;
        super.onDestroy();
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
