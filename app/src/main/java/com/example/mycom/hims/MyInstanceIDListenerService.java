package com.example.mycom.hims;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Omjoon on 2015. 12. 7..
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService{
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
