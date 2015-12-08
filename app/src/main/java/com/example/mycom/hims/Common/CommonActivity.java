package com.example.mycom.hims.Common;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.mycom.hims.R;

/**
 * Created by Omjoon on 2015. 11. 26..
 */
public abstract class CommonActivity extends Activity implements defaultSetting {
    public boolean isUseLoadingDialog = false;
    View mView_loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onMappingXml() {
        if(isUseLoadingDialog){
            mView_loading = (View)findViewById(R.id.view_loading);
        }
    }

    public void showLoadingDialog(){
        if(mView_loading.getVisibility() != View.VISIBLE) {
            mView_loading.setVisibility(View.VISIBLE);
        }
    }

    public void hideLoadingDialog(){
        if(mView_loading.getVisibility() == View.VISIBLE) {
            mView_loading.setVisibility(View.GONE);
        }
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
