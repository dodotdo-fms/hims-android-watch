package com.example.mycom.hims.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.mycom.hims.R;

/**
 * Created by Omjoon on 2015. 11. 26..
 */
public abstract class CommonActivity extends Activity implements DefaultSetting {
    View rootView;
    View loadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onMappingXml() {
         rootView = this.getWindow().getDecorView().findViewById(android.R.id.content);
        loadingView = getLayoutInflater().inflate(R.layout.view_loading,null);
    }

    public void showLoadingDialog(){
        ((FrameLayout)rootView).addView(loadingView);
    }

    public void hideLoadingDialog(){
        ((FrameLayout)rootView).removeView(loadingView);
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
