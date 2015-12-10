package com.example.mycom.hims.scheduler;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycom.hims.Common.CommonActivity;
import com.example.mycom.hims.Common.MyAccount;
import com.example.mycom.hims.DialogActivity.RoomAlreadyCleanDialogAcitivity;
import com.example.mycom.hims.MainStaffActivity;
import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.View.FooterViewHolder;
import com.example.mycom.hims.View.MyRecyclerView;
import com.example.mycom.hims.View.ViewHolderFactory;
import com.example.mycom.hims.data.Messages;
import com.example.mycom.hims.data.Rooms;
import com.example.mycom.hims.model.Room;
import com.example.mycom.hims.model.api_response.GetRoomsResponse;
import com.example.mycom.hims.server_interface.SchedulerServerAPI;
import com.example.mycom.hims.server_interface.ServerCallback;
import com.example.mycom.hims.server_interface.ServerQuery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AssignedRoomActivity extends CommonActivity {

    private Typeface font;
	private String cleanerId;
    private MyRecyclerView recyclerView;
    private TextView mTv_willCleanRoom;
    private RoomRecyclerViewAdpater adapter;
    boolean scrollCheck = false;
    boolean loadingMoreContent =false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_assigned_room);
        isUseLoadingDialog = true;
        super.onCreate(savedInstanceState);

	}
	

    public void back_click(View v){
        switch(v.getId()){
            case R.id.room_back:
                Intent lockScreenIntent = new Intent(AssignedRoomActivity.this, MainStaffActivity.class);
                lockScreenIntent.putExtra("position", MyAccount.getInstance().getPosition());
                startActivity(lockScreenIntent);
            	finish();
                break;
        }
    }
    

	@Override
	public void onBackPressed() {
		Intent lockScreenIntent = new Intent(AssignedRoomActivity.this, MainStaffActivity.class);
        lockScreenIntent.putExtra("position",  MyAccount.getInstance().getPosition());
        startActivity(lockScreenIntent);
    	finish();
	}



    class RoomRecyclerViewAdpater extends MyRecyclerView.Adapter<MyRecyclerView.ViewHolder> {
        private static final int VIEWTPYE_FOOTER = 0;
        private boolean footerVisible;
        public final Context mContext;
        public static final int VIEWTPYE_ROOM = 1;
        public RoomRecyclerViewAdpater(Context context) {
            mContext = context;
        }

        public MyRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEWTPYE_ROOM:
                    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.staff_room_item, parent, false);
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
                    ((ViewHolderFactory.Updateable) holder).update(Rooms.getInstance().getLists().get(position));
                }catch (Exception e){}
            }
        }

        @Override
        public int getItemCount() {
            return Rooms.getInstance().getLists().size() + (useFooter() ? 1 : 0);
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


    private class RoomViewHolder extends MyRecyclerView.ViewHolder implements ViewHolderFactory.Updateable<Room> {
        View view;
        public ImageView inspec_image;
        public TextView room_name;
        public ImageView bar;
        public RoomViewHolder(final View itemView) {
            super(itemView);
            view = itemView;
            inspec_image = (ImageView) itemView.findViewById(R.id.inspec_image);
            room_name = (TextView) itemView.findViewById(R.id.room_name);
            bar = (ImageView) itemView.findViewById(R.id.bar);
            room_name.setTypeface(font);
        }

        @Override
        public void update(final Room item) {
            Drawable stateBar = null;
            switch (item.getState()){
                case "OD" :
                    stateBar = getResources().getDrawable(R.color.red);
                    inspec_image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.manicon2));
                    inspec_image.setVisibility(View.VISIBLE);
                    break;
                case "OC" :
                    stateBar = getResources().getDrawable(R.color.yesButtonBackgroundColor);
                    inspec_image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.manicon2));
                    inspec_image.setVisibility(View.VISIBLE);
                    break;
                case "VC" :
                    stateBar = getResources().getDrawable(R.color.yesButtonBackgroundColor);
                    inspec_image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.manicon2));
                    inspec_image.setVisibility(View.GONE);
                    break;
                case "VD" :
                    stateBar = getResources().getDrawable(R.color.red);
                    inspec_image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.manicon2));
                    inspec_image.setVisibility(View.GONE);
                    break;
                case "DND" :
                    stateBar = getResources().getDrawable(R.color.red);
                    inspec_image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.dnd_icon_01));
                    inspec_image.setVisibility(View.VISIBLE);
                    break;
                case "RSA" :
                    stateBar = getResources().getDrawable(R.color.red);
                    inspec_image.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.rsa_icon_02));
                    inspec_image.setVisibility(View.VISIBLE);
                    break;
            }


            bar.setImageDrawable(stateBar);
            room_name.setText(item.getNumber() + "");

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item != null) {
                        String roomState = item.getState();
                        if ("OC".equals(roomState) || "VC".equals(roomState)) {
                            startActivity(new Intent(getApplicationContext(), RoomAlreadyCleanDialogAcitivity.class));
                        } else {
                            int room_number = item.getNumber();
                            Intent intent = new Intent(AssignedRoomActivity.this, RoomStateActivity.class);
                            intent.putExtra("room_number", room_number);
//                            intent.putExtra("room_state", mData.getState());
//                            intent.putExtra("cleanerId", cleanerId);
//                            intent.putExtra("position", MyAccount.getInstance().getPosition());
                            startActivity(intent);
                        }
                    }
                }
            });

        }

    }

    private void refresh(){
        showLoadingDialog();
        ServerQuery.getRooms(null, cleanerId, null, null, null, new ServerCallback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit,int statuscode) {
                GetRoomsResponse result = (GetRoomsResponse) response.body();
                if (result != null && result.getRooms() != null && result.getRooms().size() > 0) {

                    int count = 0;
                    for (Room room : result.getRooms()) {
                        if (room.getState().equals("VD") || room.getState().equals("OD") || room.getState().equals("DND") || room.getState().equals("RSA")) {
                            count++;
                        }
                    }
                    mTv_willCleanRoom.setText(count + "");
                    Rooms.getInstance().setLists(result.getRooms());

                }
                adapter = new RoomRecyclerViewAdpater(getApplicationContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
        adapter = new RoomRecyclerViewAdpater(getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



    }

    @Override
    public void onMappingXml() {
        super.onMappingXml();
        mTv_willCleanRoom = (TextView)findViewById(R.id.tv_willCleanRoom);
        recyclerView = (MyRecyclerView)findViewById(R.id.list_room);
    }

    @Override
    public void setListener() {
        super.setListener();
    }

    @Override
    public void init() {
        super.init();
        cleanerId = MyAccount.getInstance().getId();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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

    public void showMore() {
        if (loadingMoreContent == true) {
            return;
        }
        loadingMoreContent = true;
        adapter.setFooterVisible(loadingMoreContent);
        loadingMoreContent = false;

    }

}
