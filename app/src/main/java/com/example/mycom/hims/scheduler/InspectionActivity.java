package com.example.mycom.hims.scheduler;

import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.thread.TimerDisplayThread;
import com.example.mycom.hims.thread.VMReceiverThread;

public class InspectionActivity extends Activity implements OnAsyncTaskCompleted{
    private TextView room_time, room_num, emp_name;
    private Typeface font;
    private ImageView shortFormBtn, longFormBtn;
    int room_number;
    String room_state;
    String employee_name="";
    boolean isInspectedShort = false;
    boolean isInspectedLong = false;

    @Override
    public void onAsyncTaskCompleted(InputStream inputStream) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);
        Intent intent = getIntent();
        room_number = intent.getExtras().getInt("room_number");
        employee_name = intent.getExtras().getString("emp_name");
        room_state = intent.getExtras().getString("room_state");
        isInspectedShort = intent.getExtras().getBoolean("isInspectedShort");
        isInspectedLong = intent.getExtras().getBoolean("isInspectedLong");
        emp_name = (TextView)findViewById(R.id.emp_name);
        room_time = (TextView)findViewById(R.id.room_time);
        room_num = (TextView)findViewById(R.id.room_number);
        shortFormBtn = (ImageView)findViewById(R.id.s_form);
        longFormBtn = (ImageView)findViewById(R.id.l_form);
        
        if (isInspectedShort) {
        	shortFormBtn.setImageResource(R.drawable.shortform_b);
        	shortFormBtn.setOnClickListener(null);
        } else {
        	shortFormBtn.setImageResource(R.drawable.shortform_g);
        }
        
        if (isInspectedLong) {
        	longFormBtn.setImageResource(R.drawable.longform_b);
        	longFormBtn.setOnClickListener(null);
        } else {
        	longFormBtn.setImageResource(R.drawable.longform_g);
        }

        font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");

        room_time.setTypeface(font);
        room_num.setTypeface(font);
        emp_name.setTypeface(font);
        emp_name.setText(employee_name);


        room_num.setText(String.valueOf(room_number));
        set_state_color();
    }

    public void set_state_color(){
    	if ("OC".equals(room_state)) {
    		String strColor_r = "#F91243";
            room_num.setTextColor(Color.parseColor(strColor_r));
    	} else if ("OD".equals(room_state)) {
    		String strColor_p = "#AE00FF";
            room_num.setTextColor(Color.parseColor(strColor_p));
    	} else if ("VC".equals(room_state)) {
    		String strColor_g = "#03DC72";
            room_num.setTextColor(Color.parseColor(strColor_g));
    	} else if ("VD".equals(room_state)) {
    		String strColor_ig = "#03DC72";
            room_num.setTextColor(Color.parseColor(strColor_ig));
    	}
    }
    public void back_click(View v){
        switch(v.getId()){
            case R.id.room_back:
                Intent intent = new Intent(InspectionActivity.this, RoomStateActivity.class);
                intent.putExtra("room_number", room_number);
                intent.putExtra("room_state", room_state);
                intent.putExtra("position", "manager");
                startActivity(intent);
            	finish();
                break;
        }
    }
    
    @Override
	public void onBackPressed() {
    	Intent intent = new Intent(InspectionActivity.this, RoomStateActivity.class);
        intent.putExtra("room_number", room_number);
        intent.putExtra("room_state", room_state);
        intent.putExtra("position", "manager");
        startActivity(intent);
    	finish();
	}

	public void onClick(View v){
        switch(v.getId()){
            case R.id.s_form:
                Intent intent = new Intent(InspectionActivity.this, ShortEvaluationActivity.class);
                intent.putExtra("room_number", room_number);
                intent.putExtra("room_state", room_state);
                intent.putExtra("cleanerId", employee_name);
                intent.putExtra("evalType", "short");
                intent.putExtra("isInspectedShort", isInspectedShort);
                intent.putExtra("isInspectedLong", isInspectedLong);
                startActivity(intent);
                break;
            case R.id.l_form:
                Intent intent2 = new Intent(InspectionActivity.this, ShortEvaluationActivity.class);
                intent2.putExtra("room_number", room_number);
                intent2.putExtra("room_state", room_state);
                intent2.putExtra("cleanerId", employee_name);
                intent2.putExtra("evalType", "long");
                intent2.putExtra("isInspectedShort", isInspectedShort);
                intent2.putExtra("isInspectedLong", isInspectedLong);
                startActivity(intent2);
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
}
