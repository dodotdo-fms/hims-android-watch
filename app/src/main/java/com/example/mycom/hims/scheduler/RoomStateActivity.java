package com.example.mycom.hims.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycom.hims.Common.CommonActivity;
import com.example.mycom.hims.DialogActivity.AreYouSureDialogActivity;
import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.data.Rooms;
import com.example.mycom.hims.model.Room;
import com.example.mycom.hims.model.api_response.GetRoomsResponse;
import com.example.mycom.hims.model.api_response.PostCleanResponse;
import com.example.mycom.hims.server_interface.ServerQuery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RoomStateActivity extends CommonActivity {
    private TextView mTv_roomNumber;
    private Typeface font;
    Button mBtn_vc,mBtn_dnd,mBtn_rsa,mBtn_oc;
    TextView mTv_inTime,mTv_outTime,mTv_checkedOut,mTv_requirment;
    int room_number;
    Room room;
    ImageView mBtn_back;
    ScrollView mScrollView;
    boolean scrollCheck = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_room_state);
        isUseLoadingDialog = true;
        super.onCreate(savedInstanceState);


    }

    public void onClick(View v){
//        switch(v.getId()){
//        	case R.id.spanish_btn:
//        		if (requirement != null) {
//        			request.setText(requirement.getSpanish());
//        		}
//        		spanish_btn.setVisibility(View.GONE);
//        		english_btn.setVisibility(View.VISIBLE);
//        		break;
//        	case R.id.english_btn:
//        		if (requirement != null) {
//        			request.setText(requirement.getOrigin());
//        		}
//        		spanish_btn.setVisibility(View.VISIBLE);
//        		english_btn.setVisibility(View.GONE);
//        		break;
//            case R.id.inspection_btn:
//               //if(room_state == 3){
//            	if (currentRoom != null && realUserId.equals(currentRoom.getEvaluatorId())) {
//            		Evaluation evalResult = currentRoom.getEvaluation();
//    				Intent intent = new Intent(RoomStateActivity.this, InspectionActivity.class);
//					intent.putExtra("room_number", room_number);
//	                intent.putExtra("emp_name", employee_name);
//	                intent.putExtra("room_state", room_state);
//	                intent.putExtra("isInspectedShort", evalResult != null && "short".equals(evalResult.getType()));
//	                intent.putExtra("isInspectedLong", evalResult != null && "long".equals(evalResult.getType()));
//	                startActivity(intent);
//               }
//               break;
//            case R.id.vc_btn:
//            	cleanType = "VC";
//            	lay.setVisibility(View.VISIBLE);
//            	break;
//            case R.id.oc_btn:
//            	cleanType = "OC";
//            	lay.setVisibility(View.VISIBLE);
//            	break;
//            case R.id.no_btn:
//            	lay.setVisibility(View.GONE);
//            	break;
//            case R.id.yes_btn:
//            	InputStream cleanIS = SchedulerSeasrverAPI.postClean(String.valueOf(room_number), cleanType);
//            	if (cleanIS != null) {
//            		Reader reader = new InputStreamReader(cleanIS);
//            		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                	PostCleanResponse response = gson.fromJson(reader, PostCleanResponse.class);
//        			if (response != null && "success".equals(response.getResult())) {
//        				Intent assignedRoomIntent = new Intent(RoomStateActivity.this, AssignedRoomActivity.class);
//        				assignedRoomIntent.putExtra("cleanerId", employee_name);
//                		assignedRoomIntent.putExtra("position", position);
//                        startActivity(assignedRoomIntent);
//                        finish();
//        			} else {
//        				lay.setVisibility(View.GONE);
//        			}
//            	} else {
//            		lay.setVisibility(View.GONE);
//            	}
//
//            	break;
//            case R.id.request:
//            	if ("manager".equals(position)) {
//            		Intent requirementIntent = new Intent(RoomStateActivity.this, RequirementActivity.class);
//                	String roomNum = currentRoom != null ? String.valueOf(currentRoom.getNumber()) : "";
//                	String roomState = currentRoom != null ? currentRoom.getState() : "";
//                	requirementIntent.putExtra("room_number", roomNum);
//                	requirementIntent.putExtra("room_state", roomState);
//                	startActivity(requirementIntent);
//            	}
//            	break;
//        }
    }
    public void set_state_color(){
        Log.e("roo", room.getState() + "a");
    	if ("OC".equals(room.getState())) {
            mBtn_oc.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_c));
            mBtn_oc.setTextColor(getResources().getColor(R.color.white));
    	} else if ("DND".equals(room.getState())) {
            mBtn_dnd.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_dnd_rsa));
            mBtn_dnd.setTextColor(getResources().getColor(R.color.white));
    	} else if ("VC".equals(room.getState())) {
            mBtn_vc.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_c));
            mBtn_vc.setTextColor(getResources().getColor(R.color.white));
    	} else if ("RSA".equals(room.getState())) {
            mBtn_rsa.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_background_dnd_rsa));
            mBtn_rsa.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
	public void onBackPressed() {
//    	if ("manager".equals(position)) {
//            int selectedFloor = room_number / 100;
//            Intent intent = new Intent(RoomStateActivity.this, RoomDetailActivity.class);
//            intent.putExtra("now_floor", selectedFloor);
//            startActivity(intent);
//        } else {
        	Intent assignedRoomIntent = new Intent(RoomStateActivity.this, AssignedRoomActivity.class);
    		startActivity(assignedRoomIntent);
//        }
    	finish();
	}

    @Override
    public void onMappingXml() {
        super.onMappingXml();
        mBtn_dnd = (Button)findViewById(R.id.btn_dnd);
        mBtn_oc = (Button)findViewById(R.id.btn_oc);
        mBtn_rsa = (Button)findViewById(R.id.btn_rsa);
        mBtn_vc = (Button)findViewById(R.id.btn_vc);
        mBtn_back = (ImageView)findViewById(R.id.room_back);

        mTv_checkedOut = (TextView)findViewById(R.id.tv_checkedOut);
        mTv_inTime = (TextView)findViewById(R.id.tv_inTime);
        mTv_outTime = (TextView)findViewById(R.id.tv_outTime);
        mTv_requirment = (TextView)findViewById(R.id.tv_requirement);
        mTv_roomNumber = (TextView)findViewById(R.id.tv_roomNumber);

        mScrollView = (ScrollView)findViewById(R.id.scrollView);

    }

    @Override
    public void setListener() {
        super.setListener();
        mBtn_dnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDialogActivity("DND");
            }
        });

        mBtn_oc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDialogActivity("OC");
            }
        });

        mBtn_rsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDialogActivity("RSA");
            }
        });

        mBtn_vc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDialogActivity("VC");
            }
        });

        mTv_requirment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTv_requirment.getText().equals(room.getRequirement().getOrigin())){
                    mTv_requirment.setText(room.getRequirement().getSpanish()+"");
                }else{
                    mTv_requirment.setText(room.getRequirement().getOrigin()+"");
                }
            }
        });

        mBtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent assignedRoomIntent = new Intent(RoomStateActivity.this, AssignedRoomActivity.class);
                startActivity(assignedRoomIntent);
                finish();
            }
        });




    }

    private void goDialogActivity(String state){
        startActivityForResult(new Intent(getApplicationContext(), AreYouSureDialogActivity.class).putExtra("state", state), 0);

    }

    private void goPost(String state){
        showLoadingDialog();
        ServerQuery.postClean(String.valueOf(room_number), state, new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {

                    PostCleanResponse result = (PostCleanResponse)response.body();
                if (result != null && "success".equals(result.getResult())) {
                    Intent assignedRoomIntent = new Intent(RoomStateActivity.this, AssignedRoomActivity.class);
                    startActivity(assignedRoomIntent);
                }
                hideLoadingDialog();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(),"please try again",Toast.LENGTH_SHORT).show();
                hideLoadingDialog();
            }

        });

    }

    @Override
    public void init() {
        super.init();

        Intent intent = getIntent();
        room_number = intent.getExtras().getInt("room_number");
        room = Rooms.getInstance().getRoom(room_number);
        font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        mTv_roomNumber.setTypeface(font);
        mTv_roomNumber.setText(String.valueOf(room_number));
        set_state_color();
        refresh();

    }

    private void refresh(){
        showLoadingDialog();
        ServerQuery.getRooms(null, null, null, String.valueOf(room_number), null, new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                GetRoomsResponse result = (GetRoomsResponse)response.body();
                if (result != null && result.getRooms() != null && result.getRooms().size() > 0) {
                    room = result.getRooms().get(0);

                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd", Locale.US);

                    if (room.getRequirement() != null && !TextUtils.isEmpty(room.getRequirement().getOrigin())) {
                        mTv_requirment.setText(room.getRequirement().getOrigin());
                    } else {
                        mTv_requirment.setText("");
                        mTv_requirment.setEnabled(false);
                    }

                    if (room.getArrivalDate() != null && room.getDepartureDate() != null) {
                        mTv_inTime.setText(sdf.format(room.getArrivalDate()));
                        mTv_outTime.setText(sdf.format(room.getDepartureDate()));
                        if (room.getDepartureDate().before(new Date())) {
                            mTv_checkedOut.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mTv_inTime.setText("--/--");
                        mTv_outTime.setText("--/--");
                        mTv_checkedOut.setVisibility(View.GONE);
                    }



                }
                hideLoadingDialog();
            }

            @Override
            public void onFailure(Throwable t) {
                finish();
                hideLoadingDialog();
            }


        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0) {
            if (resultCode == 1) {
                goPost(data.getStringExtra("state"));
            }
        }


    }
}


