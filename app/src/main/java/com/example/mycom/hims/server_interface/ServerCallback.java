package com.example.mycom.hims.server_interface;

import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Omjoon on 2015. 12. 8..
 */
public class ServerCallback extends MyAbstractCallback {

    @Override
    public void onResponse(Response response, Retrofit retrofit, int statuscode) {
        super.onResponse(response, retrofit, statuscode);
    }

    @Override
    public void onFailure(Throwable t) {
        super.onFailure(t);
    }

    @Override
    public void onFailure(int statuscode) {
        super.onFailure(statuscode);
    }
}
