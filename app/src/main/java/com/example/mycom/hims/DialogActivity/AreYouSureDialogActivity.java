package com.example.mycom.hims.DialogActivity;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mycom.hims.Common.CommonActivity;
import com.example.mycom.hims.R;

public class AreYouSureDialogActivity extends CommonActivity {
    Button mBtn_yes;
    Button mBtn_no;
    String state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_are_you_sure_dialog);
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onMappingXml() {
        super.onMappingXml();
        mBtn_yes = (Button)findViewById(R.id.btn_yes);
        mBtn_no = (Button)findViewById(R.id.btn_no);

    }

    @Override
    public void setListener() {
        super.setListener();
        mBtn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0);
                finish();
            }
        });

        mBtn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1,getIntent().putExtra("state", state));
                finish();
            }
        });

    }

    @Override
    public void init() {
        super.init();
        state = getIntent().getStringExtra("state");
    }
}
