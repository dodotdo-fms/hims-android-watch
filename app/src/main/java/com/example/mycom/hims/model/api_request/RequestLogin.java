package com.example.mycom.hims.model.api_request;

/**
 * Created by Omjoon on 2015. 12. 7..
 */
public class RequestLogin {
    String id;
    String pw;

    public RequestLogin(String id,String pw){
        this.id = id;
        this.pw = pw;
    }
}
