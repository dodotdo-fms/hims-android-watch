package com.example.mycom.hims.DialogActivity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mycom.hims.Common.CommonActivity;
import com.example.mycom.hims.OnAsyncTaskCompleted;
import com.example.mycom.hims.R;
import com.example.mycom.hims.server_interface.VMServerAPI;
import com.example.mycom.hims.voice_messaging.ChannelCreationActivity;
import com.example.mycom.hims.voice_messaging.ChannelListActivity;
import com.example.mycom.hims.voice_messaging.PeerSelectionActivity;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ChanelAddDialogActivity extends CommonActivity implements OnAsyncTaskCompleted {


    TextView mTv_text;
    Button mBtn_add;
    String text;



    @Override
    public void onAsyncTaskCompleted(InputStream inputStream) {
        if (inputStream != null) {
            Intent intent = new Intent(getApplicationContext(), ChannelListActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chanel_add_dialog);
        super.onCreate(savedInstanceState);
    }





    @Override
    public void onMappingXml() {
        super.onMappingXml();
        mTv_text = (TextView)findViewById(R.id.tv_text);

        mBtn_add = (Button)findViewById(R.id.btn_add);
    }

    @Override
    public void setListener() {
        super.setListener();
        mBtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chanelAdd();
            }
        });

    }

    private void chanelAdd(){
        String newChannelName = mTv_text.getText().toString();
        if (newChannelName.equals("")) {
            newChannelName = mTv_text.getHint().toString();
        }
        List<String> members = new ArrayList<String>();
        for (int i = 0; i < PeerSelectionActivity.selected_people.size(); i++) {
            members.add(PeerSelectionActivity.selected_id.get(i).toString());
        }
        VMServerAPI.createChannelAsync(newChannelName, members, this);

    }

    @Override
    public void init() {
        super.init();
        ArrayList<String> names = getIntent().getStringArrayListExtra("names");
        Log.e("names", names.toString());
        for(String name : names){
            if(text==null){
                text = name;
            }else {
                text = text + "," + name;
            }
        }
        mTv_text.setText(text);
    }
}
