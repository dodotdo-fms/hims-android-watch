package com.example.mycom.hims.Common;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Omjoon on 2015. 11. 26..
 */
public class CommonActivity extends Activity implements defaultSetting {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onMappingXml() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void init() {
        onMappingXml();
        setListener();
    }
}
