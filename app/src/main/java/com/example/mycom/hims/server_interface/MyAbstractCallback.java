package com.example.mycom.hims.server_interface;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Omjoon on 2015. 12. 8..
 */
public abstract class MyAbstractCallback implements retrofit.Callback {

    @Override
    public void onResponse(Response response, Retrofit retrofit) {
        if(response.code()!=200){
            onFailure(response.code());
        }else{
            onResponse(response,retrofit,response.code());
        }
    }

    public void onResponse(Response response, Retrofit retrofit,int statuscode){

    }

    @Override
    public void onFailure(Throwable t) {

    }

    public void onFailure(int statuscode){

    }
}
