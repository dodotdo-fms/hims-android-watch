package com.example.mycom.hims.voice_messaging;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.server_interface.VMServerAPI;
import com.example.mycom.hims.thread.TimerDisplayThread;
import com.example.mycom.hims.thread.VMReceiverThread;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ChannelCreationActivity extends Activity implements OnAsyncTaskCompleted {

    private TextView room_time;
    private Typeface font;
    EditText set_name;

    private static Gson gson = new Gson();

    @Override
    public void onAsyncTaskCompleted(InputStream inputStream) {
        if (inputStream != null) {
        	Intent intent = new Intent(ChannelCreationActivity.this, ChannelListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_channel);
        font = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        room_time = (TextView) findViewById(R.id.room_time);
        room_time.setTypeface(font);

        set_name = (EditText) findViewById(R.id.set_name);
        set_name.setHint(PeerSelectionActivity.selected_people.get(0).toString());

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
            	Intent intent = new Intent(ChannelCreationActivity.this, PeerSelectionActivity.class);
            	startActivity(intent);
                finish();
                break;
            case R.id.channel_add:
                String newChannelName = set_name.getText().toString();
                if (newChannelName.equals("")) {
                    newChannelName = set_name.getHint().toString();
                }
                List<String> members = new ArrayList<String>();
                for (int i = 0; i < PeerSelectionActivity.selected_people.size(); i++) {
                    members.add(PeerSelectionActivity.selected_id.get(i).toString());
                }
                VMServerAPI.createChannelAsync(newChannelName, members, this);                
                break;
            default:
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        TimerDisplayThread.timeDisplayView = room_time;
        TimerDisplayThread.dateDisplayView = null;
        VMReceiverThread.shouldDisplay = false;
    }

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(ChannelCreationActivity.this, PeerSelectionActivity.class);
    	startActivity(intent);
        finish();
	}
}
