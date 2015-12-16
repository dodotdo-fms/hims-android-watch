package com.example.mycom.hims.model.api_request;

/**
 * Created by Omjoon on 2015. 12. 7..
 */
public class RequestLogin {
    String id;
    String pw;
    String register_id;

    public RequestLogin(String id,String pw,String register_id){
        this.id = id;
        this.pw = pw;
        this.register_id = register_id;
    }
}
