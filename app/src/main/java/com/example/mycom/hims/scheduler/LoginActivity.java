package com.example.mycom.hims.scheduler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycom.hims.Common.App;
import com.example.mycom.hims.Common.CommonActivity;
import com.example.mycom.hims.Common.DefaultSetting;
import com.example.mycom.hims.Common.MyAccount;
import com.example.mycom.hims.DialogActivity.RoomAlreadyCleanDialogAcitivity;
import com.example.mycom.hims.MainActivity;
import com.example.mycom.hims.MainStaffActivity;
import com.example.mycom.hims.R;
import com.example.mycom.hims.View.FooterViewHolder;
import com.example.mycom.hims.View.MyRecyclerView;
import com.example.mycom.hims.View.ViewHolderFactory;
import com.example.mycom.hims.data.Messages;
import com.example.mycom.hims.data.Rooms;
import com.example.mycom.hims.data.Users;
import com.example.mycom.hims.manager.MySharedPreferencesManager;
import com.example.mycom.hims.model.Room;
import com.example.mycom.hims.model.User;
import com.example.mycom.hims.model.api_response.GetRoomsResponse;
import com.example.mycom.hims.model.api_response.GetUsersResponse;
import com.example.mycom.hims.model.api_response.LoginResponse;
import com.example.mycom.hims.server_interface.ServerQuery;
import com.example.mycom.hims.server_interface.ServiceGenerator;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends CommonActivity implements DefaultSetting{
    private Typeface font;
    private MyRecyclerView recyclerView;
    private UserRecyclerViewAdpater adapter;
    public static String my_id;
    String p_userID;
    String p_UserPass;
    boolean scrollCheck = false;
    boolean loadingMoreContent =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isUseLoadingDialog = true;
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
        p_userID = prefs.getString("p_UserID", "");
        p_UserPass = prefs.getString("p_UserPass", "");

        font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");






    }




    private void goLogin(String pw){
//        showLoadingDialog();
        ServerQuery.goLogin(my_id, pw, new Callback() {


            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                LoginResponse result = (LoginResponse) response.body();
                if (result != null && !TextUtils.isEmpty(result.getToken())) {
                    App.isAliveMemory = true;
                    MyAccount.getInstance().setPosition(result.getPosition());
                    MyAccount.getInstance().setId(my_id);
                    ServiceGenerator.setToken(result.getToken());
                    if (!result.getPosition().equals("manager")) {
                        Intent lockScreenIntent = new Intent(LoginActivity.this, MainStaffActivity.class);
                        lockScreenIntent.putExtra("position", result.getPosition());
                        MyAccount.getInstance().setPosition(result.getPosition());
                        MySharedPreferencesManager.getInstance().setMyPosition(result.getPosition());
                        startActivity(lockScreenIntent);
                    }
                }
                hideLoadingDialog();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoadingDialog();
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




    class UserRecyclerViewAdpater extends MyRecyclerView.Adapter<MyRecyclerView.ViewHolder> {
        private static final int VIEWTPYE_FOOTER = 0;
        private boolean footerVisible;
        public final Context mContext;
        public static final int VIEWTPYE_ROOM = 1;
        public UserRecyclerViewAdpater(Context context) {
            mContext = context;
        }

        public MyRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEWTPYE_ROOM:
                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_item, parent, false);
                    return new RoomViewHolder(view);
                case VIEWTPYE_FOOTER:
                    return new FooterViewHolder(parent);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final MyRecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ViewHolderFactory.Updateable) {
                try {
                    ((ViewHolderFactory.Updateable) holder).update(Users.getInstance().getUsers().get(position));
                }catch (Exception e){}
            }
        }

        @Override
        public int getItemCount() {
            return Users.getInstance().getUsers().size() + (useFooter() ? 1 : 0);
        }

        @Override
        public int getItemViewType(int position) {
            if(getItemCount() == position){
                if(useFooter()){
                    return VIEWTPYE_FOOTER;
                }
                return VIEWTPYE_ROOM;
            }
            return VIEWTPYE_ROOM;
        }

        private boolean useFooter() {
            return footerVisible;
        }

        public void setFooterVisible(boolean b) {
            if (footerVisible == b) {
                //no change state
                return;
            }
            footerVisible = b;
            if (footerVisible) {
                //invisible -> visible
                notifyItemInserted(getItemCount() + 1);
            } else {
                //visible -> invisible
                notifyItemRemoved(getItemCount());
            }
        }

    }


    private class RoomViewHolder extends MyRecyclerView.ViewHolder implements ViewHolderFactory.Updateable<User> {
        View view;
        public TextView name;
        public RoomViewHolder(final View itemView) {
            super(itemView);
            view = itemView;
            name = (TextView)itemView.findViewById(R.id.emp_name);
            name.setTypeface(font);
        }

        @Override
        public void update(final User item) {


            name.setText(item.getName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent passwordIntent = new Intent(LoginActivity.this, PasswordActivity.class);
                    passwordIntent.putExtra("name", item.getName());
                    passwordIntent.putExtra("id", item.getId());
                    startActivity(passwordIntent);
                }
            });

        }

    }

    private void refresh(){
        showLoadingDialog();
        ServerQuery.getUsers(new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                Log.e("ass","Asss");
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
                        } else {
                            hideLoadingDialog();
                        }

                    } else {
                        hideLoadingDialog();
                    }
                }

                recyclerViewInit();
                hideLoadingDialog();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoadingDialog();
                Log.e("fail",t.toString());
            }

        });




    }

    public void showMore() {
        if (loadingMoreContent == true) {
            return;
        }
        loadingMoreContent = true;
        adapter.setFooterVisible(loadingMoreContent);
        loadingMoreContent = false;

    }


    @Override
    public void onMappingXml() {
        super.onMappingXml();
        recyclerView = (MyRecyclerView)findViewById(R.id.list);
    }

    @Override
    public void setListener() {
        super.setListener();
    }

    @Override
    public void init() {
        super.init();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new UserRecyclerViewAdpater(getApplicationContext());
        refresh();

        recyclerView.setOnScrollListener(new MyRecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (firstVisibleItemPosition == 0 && scrollCheck == true) {
                    scrollCheck = false;
                } else if (firstVisibleItemPosition > 0 && scrollCheck == false) {
                    scrollCheck = true;
                }


                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && Messages.getInstance().getLists().size() != 0) {
                    showMore();
                }
            }

        });
    }

    private void recyclerViewInit(){
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

