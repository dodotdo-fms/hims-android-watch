package com.example.mycom.hims.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mycom.hims.common.CommonActivity;
import com.example.mycom.hims.R;

public class RoomAlreadyCleanDialogAcitivity extends CommonActivity {

    TextView mBtn_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_room_already_clean_dialog_acitivity);
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onMappingXml() {
        super.onMappingXml();
        mBtn_ok = (Button)findViewById(R.id.btn_ok);
    }

    @Override
    public void setListener() {
        super.setListener();
        mBtn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void init() {
        super.init();
    }
}
