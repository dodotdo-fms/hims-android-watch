package com.example.mycom.hims.scheduler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.model.Evaluation;
import com.example.mycom.hims.model.Form;
import com.example.mycom.hims.model.api_response.GetFormsResponse;
import com.example.mycom.hims.model.api_response.PostEvalResponse;
import com.example.mycom.hims.server_interface.SchedulerServerAPI;
import com.example.mycom.hims.thread.TimerDisplayThread;
import com.example.mycom.hims.thread.VMReceiverThread;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShortEvaluationActivity extends Activity implements OnAsyncTaskCompleted {
    private TextView room_time, room_num;
    private TextView v_category, v_body;
    private Typeface font;
    private TextView received, percentage, perfect_score_view;

    int total_score = 0;
    int percent = 0;
    double perc = 0;
    private ScrollView scroll_view;
    private RelativeLayout lay;

    int now_index = 0;

    int room_number;
    String room_state;
    int[] s = new int[33];

    int[] idx = new int[33];
    String[] category = new String[33];
    String[] body = new String[33];
    int[] score = new int[33];

    String remarks="";
    
    List<Form> forms;
    List<Evaluation> evals = new ArrayList<Evaluation>();
    int totalScore = 0;
    int perfectScore = 0;
    int formIndex = 0;
    String cleanerId = "";
    String evalType = "";
    boolean isInspectedShort = false;
    boolean isInspectedLong = false;

    @Override
    public void onAsyncTaskCompleted(InputStream inputStream) {
    	if (inputStream != null) {
    		Reader reader = new InputStreamReader(inputStream);
    		Gson gson = new GsonBuilder().create();
    		PostEvalResponse response = gson.fromJson(reader, PostEvalResponse.class);
    		if (response != null) {
    			forms = null;
    	        evals = new ArrayList<Evaluation>();
    	        totalScore = 0;
    	        perfectScore = 0;
    	        if ("short".equals(evalType)) {
    	        	isInspectedShort = true;
    	        } else if ("long".equals(evalType)) {
    	        	isInspectedLong = true;
    	        }
    	        Intent intent = new Intent(ShortEvaluationActivity.this, InspectionActivity.class);
                intent.putExtra("room_number", room_number);
                intent.putExtra("emp_name", cleanerId);
                intent.putExtra("room_state", room_state);
                intent.putExtra("isInspectedShort", isInspectedShort);
                intent.putExtra("isInspectedLong", isInspectedLong);
                startActivity(intent);
                finish();
    		}
    	}
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_eval_form);

        for(int i=0; i<33; i++){
            s[i] = 0;
        }
        Intent intent = getIntent();
        room_number = intent.getExtras().getInt("room_number");
        room_state = intent.getExtras().getString("room_state");
        cleanerId = intent.getExtras().getString("cleanerId");
        evalType = intent.getExtras().getString("evalType");
        isInspectedShort = intent.getExtras().getBoolean("isInspectedShort");
        isInspectedLong = intent.getExtras().getBoolean("isInspectedLong");

        room_time = (TextView)findViewById(R.id.room_time);
        room_num = (TextView)findViewById(R.id.room_number);
        scroll_view = (ScrollView)findViewById(R.id.scroll_view);
        received = (TextView)findViewById(R.id.received_score);
        percentage = (TextView)findViewById(R.id.percen_score);
        perfect_score_view = (TextView)findViewById(R.id.perfect_score);
        lay = (RelativeLayout) findViewById(R.id.lay);
        lay.setVisibility(View.GONE);
        font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");

        room_time.setTypeface(font);
        room_num.setTypeface(font);

        v_category = (TextView)findViewById(R.id.category);
        v_body = (TextView)findViewById(R.id.body);
        room_num.setText(String.valueOf(room_number));
        set_state_color();

        InputStream inputStream = SchedulerServerAPI.getForms(evalType, null, null, null);
        if (inputStream != null) {
        	Reader reader = new InputStreamReader(inputStream);
        	Gson gson = new GsonBuilder().create();
        	GetFormsResponse response = gson.fromJson(reader, GetFormsResponse.class);
        	if (response != null && response.getForms() != null && response.getForms().size() > 0) {
        		forms = response.getForms();
        		v_body.setText(forms.get(formIndex).getDescription());
        	} else {
        		findViewById(R.id.no_btn).setVisibility(View.GONE);
        		findViewById(R.id.no_s_btn).setVisibility(View.GONE);
        		findViewById(R.id.yes_btn).setVisibility(View.GONE);
        		findViewById(R.id.yes_s_btn).setVisibility(View.GONE);
        	}
        }
    }
    public void onClick(View v){
    	switch(v.getId()){
            case R.id.finish_btn:
            	SchedulerServerAPI.sendEvaluationAsync(String.valueOf(room_number), evalType, evals, this);
                break;
            case R.id.no_s_btn:
            case R.id.no_btn:
            case R.id.yes_s_btn:
            case R.id.yes_btn:
                if (formIndex < forms.size()) {
                	Form form = forms.get(formIndex);
                	Evaluation eval = new Evaluation();
                	eval.setId(form.getId());
                	eval.setCheck(v.getId() == R.id.yes_btn || v.getId() == R.id.yes_s_btn);
                	eval.setCleanerId(cleanerId);
                	eval.setEvaluatorId(LoginActivity.my_id);
                	eval.setInspectionDesc(form.getDescription());
                	eval.setRoomNumber(room_number);
                	eval.setTimestamp(new Date());
                	eval.setTotalPoint(form.getTotalPoint());
                	evals.add(eval);
                	perfectScore += form.getTotalPoint();
                	totalScore += eval.isCheck() ? form.getTotalPoint() : 0;
                	if (formIndex < forms.size() - 1) {
                		v_body.setText(forms.get(++formIndex).getDescription());
                	} else {
                		go_finish_page();
                	}
                } else {
                	go_finish_page();
                }
                break;
        }
    }
    
    public void go_finish_page(){
        perc = ((double)totalScore/(double)perfectScore)*100.0;
        percent = (int)perc;
        Log.d("1", String.valueOf(perc));
        Log.d("1", String.valueOf(percent));

        received.setText(String.valueOf(totalScore));
        percentage.setText(String.valueOf(percent));
        perfect_score_view.setText(String.valueOf(perfectScore));
        
        lay.setVisibility(View.VISIBLE);
        scroll_view.setVisibility(View.GONE);
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
                if(now_index == 0)
                    finish();
                else if(now_index == 32) {
                        s[now_index] =0;
                        v_category.setText(category[now_index]);
                        v_body.setText(body[now_index]);
                    lay.setVisibility(View.GONE);
                    scroll_view.setVisibility(View.VISIBLE);
                }
                else{
                        s[now_index] = 0;
                        now_index --;
                        s[now_index] = 0;
                        v_category.setText(category[now_index]);
                        v_body.setText(body[now_index]);
                }
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
