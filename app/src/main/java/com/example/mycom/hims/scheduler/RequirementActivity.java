package com.example.mycom.hims.scheduler;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.model.api_response.PostRequirementResponse;
import com.example.mycom.hims.server_interface.SchedulerServerAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RequirementActivity extends Activity implements OnAsyncTaskCompleted {

	EditText txtRequirement;
	String room_number;
	String room_state;
	
	@Override
	public void onAsyncTaskCompleted(InputStream inputStream) {
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_requirement);
		txtRequirement = (EditText) findViewById(R.id.txtRequirement);
		room_number = getIntent().getStringExtra("room_number");
		room_state = getIntent().getStringExtra("room_state");
	}
	
	public void onClick(View v){
		Intent intent = new Intent(RequirementActivity.this, RoomStateActivity.class);
		intent.putExtra("room_number", Integer.parseInt(room_number));
        intent.putExtra("room_state", room_state);
        intent.putExtra("position", "manager");
        
		switch(v.getId()){
        	case R.id.back:
                startActivity(intent);
                finish();
        		break;
        	case R.id.submitRequirement:
        		String requirement = txtRequirement.getText().toString();
        		if (!TextUtils.isEmpty(requirement)) {
        			InputStream inputStream = SchedulerServerAPI.postRequirement(room_number, requirement);
        			if (inputStream != null) {
        				Reader reader = new InputStreamReader(inputStream);
        				Gson gson = new GsonBuilder().create();
        				PostRequirementResponse response = gson.fromJson(reader, PostRequirementResponse.class);
        				if (response != null && "success".equals(response.getResult())) {
        					startActivity(intent);
        	                finish();
        				}
        			}
        		}
        		break;
        }
    }

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(RequirementActivity.this, RoomStateActivity.class);
		intent.putExtra("room_number", Integer.parseInt(room_number));
        intent.putExtra("room_state", room_state);
        intent.putExtra("position", "manager");
        startActivity(intent);
        finish();
	}
}
